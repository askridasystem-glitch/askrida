/***********************************************************************
 * Module:  com.webfin.gl.report2.form.FinReportForm
 * Author:  Denny Mahendra
 * Created: Apr 1, 2007 11:01:40 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.gl.report2.form;

import com.crux.common.controller.FOPServlet;
import com.crux.util.*;
import com.crux.common.model.HashDTO;
import com.crux.web.controller.SessionManager;
import com.crux.web.form.Form;
import com.crux.lov.LOVManager;
import com.crux.common.parameter.Parameter;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.webfin.gl.ejb.*;
import com.webfin.gl.model.*;
import com.webfin.system.region.model.RegionView;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ejb.CreateException;
import javax.naming.NamingException;

import java.util.HashMap;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletOutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FinReportForm extends Form {

    private String stLang;
    private String stPrintForm;
    private HashMap rptRef;
    private String stReportType;
    private String stReport;
    private String stReportTitle;
    private boolean enableSelectForm = true;
    private static HashSet formList = null;
    private String ref1;
    private HashMap refPropMap;
    private String status;
    private String periodFrom;
    private String periodTo;
    private String period;
    private String yearFrom;
    private String yearTo;
    private String year;
    private String branch = SessionManager.getInstance().getSession().getStBranch();
    private String branchDesc;
    private String person1Name;
    private String person2Name;
    private String person1Title;
    private String person2Title;
    private String rptfmt;
    private String stFlag;
    private Date appDateFrom;
    private Date appDateTo;
    private Date dtApplyDateFrom;
    private Date dtApplyDateTo;
    private Date dtEntryDateFrom;
    private Date dtEntryDateTo;
    private Date dtPrintDate;
    private String clShowNo;
    private String clAccNo;
    private String clTrxNo;
    private String clDate;
    int lineNo;
    private String stEntityID;
    private String stEntityName;
    private String stAccountID;
    private String stAccountNo;
    private String stDescription;
    private String stKas;
    private String stNeraca;
    private String stNeracaHarian;
    private String clAccDet;
    private Date perDateFrom;
    private AccountView2 account;
    private String region;
    private String regionDesc;
    private String stKeterangan;
    private String stKeteranganDesc;
    private String periodFromName;
    private String periodToName;

    public String getClDate() {
        return clDate;
    }

    public void setClDate(String clDate) {
        this.clDate = clDate;
    }

    public boolean isClAccNo() {
        return Tools.isYes(clAccNo);
    }

    public boolean isClTrxNo() {
        return Tools.isYes(clTrxNo);
    }

    public boolean isClShowNo() {
        return Tools.isYes(clShowNo);
    }

    public String getClShowNo() {
        return clShowNo;
    }

    public void setClShowNo(String clShowNo) {
        this.clShowNo = clShowNo;
    }

    public String getClAccNo() {
        return clAccNo;
    }

    public void setClAccNo(String clAccNo) {
        this.clAccNo = clAccNo;
    }

    public String getClTrxNo() {
        return clTrxNo;
    }

    public void setClTrxNo(String clTrxNo) {
        this.clTrxNo = clTrxNo;
    }

    public String getRptfmt() {
        return rptfmt;
    }

    public void setRptfmt(String rptfmt) {
        this.rptfmt = rptfmt;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getStLang() {
        return stLang;
    }

    public void setStLang(String stLang) {
        this.stLang = stLang;
    }
    private final static transient LogManager logger = LogManager.getInstance(FinReportForm.class);

    public void clickPrint() throws Exception {

        loadFormList();

        String pf = stPrintForm;
        String lv = clAccDet;

        String level = null;
        if (stNeraca != null) {
            level = stNeraca;
        }

        if (stKas != null) {
            level = stKas;
        }

        if (stNeracaHarian != null) {
            level = stNeracaHarian;
        }

        if (rptfmt != null && !"default".equals(rptfmt)) {
            pf += "_" + rptfmt;
        }

        //if (pf.equalsIgnoreCase("account") && level.equalsIgnoreCase("3")) {
        //    if (branch == null) {
        //        clickLabaBersih();
        //    }
        //}

        //if (yearTo==null) yearTo=yearFrom;
        //if (periodTo==null) periodTo=periodFrom;

        /*super.redirect("/pages/fin_report/"+pf+"_"+level+".fop?xlang="+stLang);*/

        final ArrayList plist = new ArrayList();

        plist.add(pf + "_" + level + "_" + clAccDet);

        plist.add(pf + "_" + level + "_" + clIntRkp);

        plist.add(pf + "_" + level);

        plist.add(pf);

        String urx = null;

        logger.logDebug("print: scanlist:" + plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains(s + ".fop.jsp")) {
                urx = "/pages/fin_report/" + s + ".fop?xlang=" + stLang;
                break;
            }
        }

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        super.redirect(urx);

    }

    private void loadFormList() {
        if (formList == null || true) {
            final String[] filez = new File(SessionManager.getInstance().getRequest().getSession().getServletContext().getRealPath("/pages/fin_report")).list();

            formList = new HashSet();

            for (int i = 0; i < filez.length; i++) {
                String s = filez[i];

                formList.add(s);
            }
        }
    }

    public String getStPrintForm() {
        return stPrintForm;
    }

    public void setStPrintForm(String stPrintForm) {
        this.stPrintForm = stPrintForm;
    }

    public void initialize() {
        stPrintForm = (String) getAttribute("rpt");
        setTitle(getReportTitle());

        rptRef = Tools.getPropMap(LOVManager.getInstance().getRef1("FIN_RPT", stPrintForm));
        //rptRef = Tools.getPropMap(LOVManager.getInstance().getRef1("PROD_ACCOUNT",stPrintForm));

        if (rptRef.containsKey("FOP")) {
            stPrintForm = (String) rptRef.get("FOP");
        }
    }

    private String getReportTitle() {
        return LOVManager.getInstance().getLOV("VS_FIN_RPT", null).getComboDesc(stPrintForm);
        //return LOVManager.getInstance().getLOV("VS_PROD_ACCOUNT",null).getComboDesc(stPrintForm);
    }

    public HashMap getRptRef() {
        return rptRef;
    }

    public void setRptRef(HashMap rptRef) {
        this.rptRef = rptRef;
    }

    public void go() {
    }
    /*
    public void go() {
    stPrintForm = (String) getAttribute("rpt");
    enableSelectForm = false;

    stReportTitle = LOVManager.getInstance().getDescription(stPrintForm,"VS_PROD_ACCOUNT");
    }
     */

    public String getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(String periodFrom) {
        this.periodFrom = periodFrom;
    }

    public String getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(String periodTo) {
        this.periodTo = periodTo;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
        this.periodFrom = period;
        this.periodTo = period;
    }

    public String getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(String yearFrom) {
        this.yearFrom = yearFrom;
    }

    public String getYearTo() {
        return yearTo;
    }

    public void setYearTo(String yearTo) {
        this.yearTo = yearTo;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
        this.yearFrom = year;
        this.yearTo = year;

    }

    public long getLPeriodFrom() {
        return Long.parseLong(periodFrom);
    }

    public long getLPeriodTo() {
        return Long.parseLong(periodTo);
    }

    public long getLYearFrom() {
        return Long.parseLong(yearFrom);
    }

    public long getLYearTo() {
        return Long.parseLong(yearTo);
    }

    public String getPeriodTitleDescription() throws Exception {

        if (year != null) {

            PeriodDetailView pd = PeriodManager.getInstance().getPeriod(periodTo, year);

            Date d = pd.getDtEndDate();

            return "PER " + DateUtil.getDateStr(d, "d ^^ yyyy");
        }

        if (yearFrom != null) {
            PeriodDetailView pd1 = PeriodManager.getInstance().getPeriod(periodFrom, yearFrom);
            PeriodDetailView pd2 = PeriodManager.getInstance().getPeriod(periodTo, yearFrom);

            Date d1 = pd1.getDtStartDate();
            Date d2 = pd2.getDtEndDate();

            return //"PERIODE "+ DateUtil.getDateStr(d1,"d ^^ yyyy") +" - " + DateUtil.getDateStr(d2,"d ^^ yyyy")
                    "PER " + DateUtil.getDateStr(d2, "d ^^ yyyy");
        }

        return "???";
    }

    public String getMonthFromTitleDescription() throws Exception {

        PeriodDetailView pd1 = PeriodManager.getInstance().getPeriod(periodFrom, yearFrom);

        Date d1 = pd1.getDtEndDate();

        return DateUtil.getDateStr(d1, "^^ yyyy");
    }

    public String getMonthTitleDescription() throws Exception {

        PeriodDetailView pd1 = PeriodManager.getInstance().getPeriod(periodTo, yearFrom);

        Date d1 = pd1.getDtEndDate();

        return DateUtil.getDateStr(d1, "^^ yyyy");
    }

    public String getPeriodBeforeTitleDescription() throws Exception {

        long LperiodTo = Long.parseLong(periodTo);

        LperiodTo = LperiodTo - 1;

        String PeriodBeforeTo = String.valueOf(LperiodTo);

        if (year != null) {

            PeriodDetailView pd = PeriodManager.getInstance().getPeriod(PeriodBeforeTo, year);

            Date d = pd.getDtEndDate();

            return "PER " + DateUtil.getDateStr(d, "d ^^ yyyy");
        }

        if (yearFrom != null) {
            PeriodDetailView pd1 = PeriodManager.getInstance().getPeriod(periodFrom, yearFrom);
            PeriodDetailView pd2 = PeriodManager.getInstance().getPeriod(PeriodBeforeTo, yearFrom);

            Date d1 = pd1.getDtStartDate();
            Date d2 = pd2.getDtEndDate();

            return //"PERIODE "+ DateUtil.getDateStr(d1,"d ^^ yyyy") +" - " + DateUtil.getDateStr(d2,"d ^^ yyyy")
                    "PER " + DateUtil.getDateStr(d2, "d ^^ yyyy");
        }

        return "???";
    }

    public String getPerson1Name() {
        if (person1Name == null) {
            person1Name = Parameter.readString("FIN_RPT_P1N_" + stPrintForm);

        }
        return person1Name;
    }

    public void setPerson1Name(String person1Name) throws Exception {
        Parameter.setString("FIN_RPT_P1N_" + stPrintForm, person1Name);
        this.person1Name = person1Name;
    }

    public String getPerson2Name() {
        if (person2Name == null) {
            person2Name = Parameter.readString("FIN_RPT_P2N_" + stPrintForm);

        }
        return person2Name;
    }

    public void setPerson2Name(String person2Name) throws Exception {
        Parameter.setString("FIN_RPT_P2N_" + stPrintForm, person2Name);
        this.person2Name = person2Name;
    }

    public String getPerson1Title() {
        if (person1Title == null) {
            person1Title = Parameter.readString("FIN_RPT_P1T_" + stPrintForm);

        }
        return person1Title;
    }

    public void setPerson1Title(String person1Title) throws Exception {
        Parameter.setString("FIN_RPT_P1T_" + stPrintForm, person1Title);
        this.person1Title = person1Title;
    }

    public String getPerson2Title() {
        if (person2Title == null) {
            person2Title = Parameter.readString("FIN_RPT_P2T_" + stPrintForm);

        }
        return person2Title;
    }

    public void setPerson2Title(String person2Title) throws Exception {
        Parameter.setString("FIN_RPT_P2T_" + stPrintForm, person2Title);
        this.person2Title = person2Title;
    }

    public String getLineNo() {
        lineNo++;

        return String.valueOf(lineNo);
    }

    public boolean isClDate() {
        return Tools.isYes(clDate);
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

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    public String getBranchDesc() {
        return branchDesc;
    }

    public void setBranchDesc(String branchDesc) {
        this.branchDesc = branchDesc;
    }

    public String getStFlag() {
        return stFlag;
    }

    public void setStFlag(String stFlag) {
        this.stFlag = stFlag;
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

    public String getStAccountNo() {
        return stAccountNo;
    }

    public void setStAccountNo(String stAccountNo) {
        this.stAccountNo = stAccountNo;
    }

    public String getStDescription() {
        return stDescription;
    }

    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    public String getStAccountID() {
        return stAccountID;
    }

    public void setStAccountID(String stAccountID) {
        this.stAccountID = stAccountID;
    }

    public String getStReportType() {
        return stReportType;
    }

    public void setStReportType(String stReportType) {
        this.stReportType = stReportType;
    }

    public String getStReport() {
        return stReport;
    }

    public void setStReport(String stReport) {
        this.stReport = stReport;
    }

    public boolean isEnableSelectForm() {
        return enableSelectForm;
    }

    public void setEnableSelectForm(boolean enableSelectForm) {
        this.enableSelectForm = enableSelectForm;
    }

    public void onChangeReport() {
    }

    public boolean isClAccDet() {
        return Tools.isYes(clAccDet);
    }

    public String getClAccDet() {
        return clAccDet;
    }

    public void setClAccDet(String clAccDet) {
        this.clAccDet = clAccDet;
    }

    public String getStKas() {
        return stKas;
    }

    public void setStKas(String stKas) {
        this.stKas = stKas;
    }

    public String getStNeraca() {
        return stNeraca;
    }

    public void setStNeraca(String stNeraca) {
        this.stNeraca = stNeraca;
    }

    public Date getDtApplyDateFrom() {
        return dtApplyDateFrom;
    }

    public void setDtApplyDateFrom(Date dtApplyDateFrom) {
        this.dtApplyDateFrom = dtApplyDateFrom;
    }

    public Date getDtApplyDateTo() {
        return dtApplyDateTo;
    }

    public void setDtApplyDateTo(Date dtApplyDateTo) {
        this.dtApplyDateTo = dtApplyDateTo;
    }

    public Date getDtEntryDateFrom() {
        return dtEntryDateFrom;
    }

    public void setDtEntryDateFrom(Date dtEntryDateFrom) {
        this.dtEntryDateFrom = dtEntryDateFrom;
    }

    public Date getDtEntryDateTo() {
        return dtEntryDateTo;
    }

    public void setDtEntryDateTo(Date dtEntryDateTo) {
        this.dtEntryDateTo = dtEntryDateTo;
    }

    public void clickPrintExcel() throws Exception {

        if (stKas != null) {
            EXCEL_BUKUHARIAN();
        }

        if (stNeracaHarian != null) {
            final DTOList l = EXCEL_LABARUGIHARIAN();

            SessionManager.getInstance().getRequest().setAttribute("RPT", l);

            EXPORT_LABARUGIHARIAN();
        }
//
//        final DTOList l = EXCEL_BUKUHARIAN();
//
//        SessionManager.getInstance().getRequest().setAttribute("RPT", l);
//
//        EXPORT_BUKUHARIAN();
    }

    public DTOList EXCEL_BUKUHARIANOLD() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();
        int q = stAccountNo.substring(0, 5).length();

        int start = DateUtil.getMonthDigit(dtApplyDateFrom);
        int end = DateUtil.getMonthDigit(dtApplyDateTo);

        String gljedetail = null;
        String gljedetailYear = DateUtil.getYear(dtApplyDateFrom);
        String gljedetailYearCurrent = DateUtil.getYear(new Date());

        if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
            gljedetail = "gl_je_detail";
        } else {
            gljedetail = "gl_je_detail_" + gljedetailYear;
        }

        //sqa.addSelect(" a.*, b.accountno, d.ar_settlement_id, case when d.description is not null then d.description else a.ref_trx_type end as menu ");
        sqa.addSelect(" a.applydate, a.trx_no, b.accountno, case when d.description is not null then d.description else a.ref_trx_type end as menu, "
                + " a.description, a.debit, a.credit ");

        sqa.addQuery(" from " + gljedetail + " a "
                + " left join gl_accounts b on b.account_id = a.accountid "
                + " left join ar_receipt c on a.ref_trx_no::bigint = c.ar_receipt_id "
                + " left join ar_settlement d on d.ar_settlement_id = c.ar_settlement_id ");

        sqa.addClause("((substr(b.accountno,1," + q + ")) not between ? and ?) ");
        sqa.addPar(stAccountNo.substring(0, 5));
        sqa.addPar(stAccountNo.substring(0, 5));
        sqa.addClause("a.fiscal_year = ?");
        sqa.addPar(DateUtil.getYear(dtApplyDateFrom));
        sqa.addClause("(a.period_no between ? and ?)");
        sqa.addPar(Integer.toString(start));
        sqa.addPar(Integer.toString(end));

        if (dtApplyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= ?");
            sqa.addPar(dtApplyDateFrom);
        }

        if (dtApplyDateTo != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= ?");
            sqa.addPar(dtApplyDateTo);
        }

        if (branch != null) {
            sqa.addClause("substr(b.accountno,14,2) = ? ");
            sqa.addPar(branch);
        }

        if (stAccountNo != null) {
            sqa.addClause("a.hdr_accountno = ? ");
            sqa.addPar(stAccountNo);
        }

        final String sql = sqa.getSQL() + " order by a.applydate,a.trx_no,a.trx_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXCEL_BUKUHARIAN() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();
        int q = stAccountNo.substring(0, 5).length();

        int start = DateUtil.getMonthDigit(dtApplyDateFrom);
        int end = DateUtil.getMonthDigit(dtApplyDateTo);

        String gljedetail = null;
        String gljedetailYear = DateUtil.getYear(dtApplyDateFrom);
        String gljedetailYearCurrent = DateUtil.getYear(new Date());

        if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
            gljedetail = "gl_je_detail";
        } else {
            gljedetail = "gl_je_detail_" + gljedetailYear;
        }

        //sqa.addSelect(" a.*, b.accountno, d.ar_settlement_id, case when d.description is not null then d.description else a.ref_trx_type end as menu ");
        sqa.addSelect(" a.applydate, a.trx_no, b.accountno, case when d.description is not null then d.description else a.ref_trx_type end as menu, "
                + " a.description, a.debit, a.credit ");

        sqa.addQuery(" from " + gljedetail + " a "
                + " left join gl_accounts b on b.account_id = a.accountid "
                + " left join ar_receipt c on a.ref_trx_no::bigint = c.ar_receipt_id "
                + " left join ar_settlement d on d.ar_settlement_id = c.ar_settlement_id ");

        sqa.addClause("((substr(b.accountno,1," + q + ")) not between '" + stAccountNo.substring(0, 5) + "' and '" + stAccountNo.substring(0, 5) + "') ");
        //sqa.addPar(stAccountNo.substring(0, 5));
        //sqa.addPar(stAccountNo.substring(0, 5));
        sqa.addClause("a.fiscal_year = '" + DateUtil.getYear(dtApplyDateFrom) + "'");
        //sqa.addPar(DateUtil.getYear(dtApplyDateFrom));
        sqa.addClause("(a.period_no between '" + Integer.toString(start) + "' and '" + Integer.toString(end) + "')");
        //sqa.addPar(Integer.toString(start));
        //sqa.addPar(Integer.toString(end));

        if (dtApplyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= '" + dtApplyDateFrom + "'");
            //sqa.addPar(dtApplyDateFrom);
        }

        if (dtApplyDateTo != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= '" + dtApplyDateTo + "'");
            //sqa.addPar(dtApplyDateTo);
        }

        if (branch != null) {
            sqa.addClause("substr(b.accountno,14,2) = '" + branch + "'");
            //sqa.addPar(branch);
        }

        if (stAccountNo != null) {
            sqa.addClause("a.hdr_accountno = '" + stAccountNo + "'");
            //sqa.addPar(stAccountNo);
        }

        String sql = sqa.getSQL() + " order by a.applydate,a.trx_no,a.trx_id ";

        /*
        final DTOList l = ListUtil.getDTOListFromQuery(
        sql,
        sqa.getPar(),
        HashDTO.class
        );

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
         */

        SQLUtil S = new SQLUtil();

        String nama_file = "EXCEL_BUKUHARIAN_" + System.currentTimeMillis() + ".csv";

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

    public void EXPORT_BUKUHARIAN() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Tgl Mutasi");
            row1.createCell(1).setCellValue("Transaksi");
            row1.createCell(2).setCellValue("Akun");

            row1.createCell(3).setCellValue("Menu");

            row1.createCell(4).setCellValue("Uraian");
            row1.createCell(5).setCellValue("Debit");
            row1.createCell(6).setCellValue("Kredit");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("applydate"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("trx_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("accountno"));

            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("menu"));

            row.createCell(4).setCellValue(JSPUtil.xmlEscape(h.getFieldValueByFieldNameST("description")));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("debit").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("credit").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=buku harian.xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public Date getPerDateFrom() {
        return perDateFrom;
    }

    public void setPerDateFrom(Date perDateFrom) {
        this.perDateFrom = perDateFrom;
    }

    /**
     * @return the stNeracaHarian
     */
    public String getStNeracaHarian() {
        return stNeracaHarian;
    }

    /**
     * @param stNeracaHarian the stNeracaHarian to set
     */
    public void setStNeracaHarian(String stNeracaHarian) {
        this.stNeracaHarian = stNeracaHarian;
    }

    public void clickLabaBersih() throws Exception {

        account = new AccountView2();

        final DTOList cabang = account.getCabang();

        for (int i = 0; i < cabang.size(); i++) {
            GLCostCenterView cab = (GLCostCenterView) cabang.get(i);

            calculateLabaBersih(cab.getStCostCenterCode());

        }
    }

    public void calculateLabaBersih(String cccode) throws Exception {

        long lPeriodFrom = Integer.parseInt(periodTo);
        long lPeriodTo = Integer.parseInt(periodTo);
        long lYearFrom = Integer.parseInt(yearFrom);

        GLReportEngine2 glr = new GLReportEngine2();

        glr.setBranch(cccode);

        BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
        BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
        BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
        BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

        BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
        BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
        BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
        BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
        jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

        BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
        BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
        BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
        jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

        BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
        BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

        BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
        BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

        BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
        BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
        laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
        BigDecimal penghasilanBeban69 = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
        BigDecimal penghasilanBeban89 = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
        BigDecimal penghasilanBeban = BDUtil.add(penghasilanBeban69, penghasilanBeban89);

        BigDecimal labaSebelumPajak = BDUtil.negate(BDUtil.add(laba_usaha, penghasilanBeban));

        BigDecimal laba_bersih = BDUtil.sub(labaSebelumPajak, pajakPenghasilan);
        laba_bersih = BDUtil.negate(BDUtil.add(laba_bersih, BDUtil.negate(pajakPenghasilanTangguhan)));

        //BigDecimal laba_bersih = BDUtil.roundUp(glr.getSummaryRangedOnePeriod("BAL|NEG=5", "51611", "51611", lPeriodTo, lYearFrom, lYearFrom));
        //BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

        updateLabaBersih(laba_bersih, lPeriodTo, lYearFrom, cccode, "Y");
    }
    /*
    public void calculateLabaBersih00() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("00");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih01() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("01");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih10() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("10");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih11() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("11");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih12() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("12");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih13() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("13");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih14() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("14");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih15() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("15");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih16() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("16");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih17() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("17");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih18() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("18");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih19() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("19");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih20() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("20");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih21() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("21");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih22() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("22");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih23() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("23");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih24() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("24");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih25() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("25");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih30() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("30");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih31() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("31");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih32() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("32");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih33() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("33");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih40() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("40");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih41() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("41");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih42() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("42");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih43() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("43");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih44() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("44");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih45() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("45");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih50() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("50");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih51() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("51");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih52() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("52");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih60() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("60");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih61() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("61");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }

    public void calculateLabaBersih70() throws Exception {

    long lPeriodFrom = Integer.parseInt(periodTo);
    long lPeriodTo = Integer.parseInt(periodTo);
    long lYearFrom = Integer.parseInt(yearFrom);
    String branch = new String("70");

    GLReportEngine2 glr = new GLReportEngine2();

    glr.setBranch(branch);

    BigDecimal premi_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal premi_kenaikan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

    BigDecimal klaim_bruto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal klaim_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal kenaikan_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
    jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

    BigDecimal beban_komisi_netto = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_und_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

    BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal beban_usaha = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTangguhan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
    BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
    BigDecimal penghasilanBeban = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));

    BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

    updateLabaBersih(BDUtil.roundUp(laba_bersih), lPeriodTo, lYearFrom, branch, "Y");
    }
     */

    public BigDecimal updateLabaBersih(BigDecimal labaBersih, long months, long years, String branch, String flag) throws Exception {
        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.account_id::text ");

        sqa.addQuery(" from gl_accounts a ");

        sqa.addClause(" a.accountno like '51611%'  ");
        sqa.addClause(" a.acctype is null ");
        sqa.addClause(" a.enabled = 'Y' ");

        sqa.addClause(" a.cc_code = ? ");
        sqa.addPar(branch);

        final DTOList l = ListUtil.getDTOListFromQuery(
                sqa.getSQL(),
                sqa.getPar(),
                HashDTO.class);

        if (flag.equalsIgnoreCase("N")) {
            flag = null;
        }

        HashDTO h = (HashDTO) l.get(0);

        getRemoteGeneralLedger().AccountLabaBersih(h.getFieldValueByFieldNameST("account_id"), months, years, labaBersih, flag);

        return labaBersih;
    }

    /**
     * @return the account
     */
    public AccountView2 getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(AccountView2 account) {
        this.account = account;
    }

    /**
     * @return the dtPrintDate
     */
    public Date getDtPrintDate() {
        return dtPrintDate;
    }

    /**
     * @param dtPrintDate the dtPrintDate to set
     */
    public void setDtPrintDate(Date dtPrintDate) {
        this.dtPrintDate = dtPrintDate;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the regionDesc
     */
    public String getRegionDesc() {
        return regionDesc;
    }

    /**
     * @param regionDesc the regionDesc to set
     */
    public void setRegionDesc(String regionDesc) {
        this.regionDesc = regionDesc;
    }

    public void onChangeBranchGroup() {
    }

    public GLCostCenterView getCostCenter() {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, branch);

        return costcenter;
    }

    public RegionView getRegionCenter() {
        final RegionView reg = (RegionView) DTOPool.getInstance().getDTO(RegionView.class, region);

        return reg;
    }

    public boolean isPosted() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isPosted = false;

        try {
            String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y' and final_flag = 'Y' and cc_code is null";

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, getPeriodTo());
            PS.setString(2, getYearFrom());

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isPosted = true;
            }

        } finally {
            S.release();
        }

        return isPosted;
    }

    public void onChangeKeterangan() {
    }

    /**
     * @return the stKeterangan
     */
    public String getStKeterangan() {
        return stKeterangan;
    }

    /**
     * @param stKeterangan the stKeterangan to set
     */
    public void setStKeterangan(String stKeterangan) {
        this.stKeterangan = stKeterangan;
    }

    /**
     * @return the stKeteranganDesc
     */
    public String getStKeteranganDesc() {
        return stKeteranganDesc;
    }

    /**
     * @param stKeteranganDesc the stKeteranganDesc to set
     */
    public void setStKeteranganDesc(String stKeteranganDesc) {
        this.stKeteranganDesc = stKeteranganDesc;
    }

    public boolean isJournalSyariah() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isPosted = false;

        try {
            String cek = " select a.* from gl_je_detail_syariah a where a.period_no = ? and a.fiscal_year = ? and a.approved = 'Y' order by a.trx_id limit 1 ";

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, getPeriodTo());
            PS.setString(2, getYearFrom());

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isPosted = true;
            }

        } finally {
            S.release();
        }

        return isPosted;
    }

    public void clickPrint2() throws Exception {
        //PostProcessorManager.getInstance().runPolicyProcess();

        loadFormList();

        final DTOList l = loadList();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        String pf = stPrintForm + "_" + stNeraca;

        final ArrayList plist = new ArrayList();

        plist.add(pf + "_bod");

        String urx = null;

        logger.logDebug("print: scanlist:" + plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains(s + ".fop.jsp")) {
                urx = "/pages/fin_report/" + s + ".fop?xlang=" + stLang;
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
        ref1 = LOVManager.getInstance().getRef1("FIN_RPT", stPrintForm);

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

    public DTOList ACCOUNTING() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String bulan = "b.bal" + getLPeriodTo();

        sqa.addSelect("b.period_year, sum(getpremi2(a.gl_ins_id = 1001," + bulan + ")) as Deposito,"
                + "sum(getpremi2(a.gl_ins_id = 1002," + bulan + ")) as Sertifikat_Deposito,"
                + "sum(getpremi2(a.gl_ins_id = 1003," + bulan + ")) as Surat_Berharga,"
                + "sum(getpremi2(a.gl_ins_id = 1004," + bulan + ")) as Penyertaan,"
                + "sum(getpremi2(a.gl_ins_id = 1005," + bulan + ")) as Properti,"
                + "sum(getpremi2(a.gl_ins_id = 1006," + bulan + ")) as Properti_Pinjaman_Hipotek,"
                + "sum(getpremi2(a.gl_ins_id = 1007," + bulan + ")) as Investasi_Lain,"
                + "sum(getpremi2(a.gl_ins_id = 1008," + bulan + ")) as Kas_dan_Bank,"
                + "sum(getpremi2(a.gl_ins_id = 1009," + bulan + ")) as Piutang_Premi,"
                + "sum(getpremi2(a.gl_ins_id = 1010," + bulan + ")) as Piutang_Reasuransi,"
                + "sum(getpremi2(a.gl_ins_id = 1011," + bulan + ")) as Aset_Reasuransi,"
                + "sum(getpremi2(a.gl_ins_id = 1012," + bulan + ")) as Piutang_Hasil_Investasi,"
                + "sum(getpremi2(a.gl_ins_id = 1013," + bulan + ")) as Piutang_Lain,"
                + "sum(getpremi2(a.gl_ins_id = 1014," + bulan + ")) as Piutang_Lain_2,"
                + "sum(getpremi2(a.gl_ins_id = 1015," + bulan + ")) as Biaya_Dibayar_Dimuka,"
                + "sum(getpremi2(a.gl_ins_id = 1016," + bulan + ")) as Aktiva_Tetap,"
                + "sum(getpremi2(a.gl_ins_id = 1017," + bulan + ")) as Aktiva_Lain,"
                + "sum(getpremi2(a.gl_ins_id = 1018," + bulan + ")) as Aktiva_Pajak_Tangguhan,"
                + "sum(getpremi2(a.gl_ins_id = 1019," + bulan + ")) as Utang_Klaim,"
                + "sum(getpremi2(a.gl_ins_id = 1020," + bulan + ")) as Estimasi_Klaim_Retensi_Sendiri,"
                + "sum(getpremi2(a.gl_ins_id = 1021," + bulan + ")) as Premi_YBMP,"
                + "sum(getpremi2(a.gl_ins_id = 1022," + bulan + ")) as Utang_Reasuransi,"
                + "sum(getpremi2(a.gl_ins_id = 1023," + bulan + ")) as Utang_Komis,"
                + "sum(getpremi2(a.gl_ins_id = 1024," + bulan + ")) as Utang_Pajak,"
                + "sum(getpremi2(a.gl_ins_id = 1025," + bulan + ")) as Biaya_yg_masih_harus_dibayar,"
                + "sum(getpremi2(a.gl_ins_id = 1026," + bulan + ")) as Utang_Sewa_Guna_Usaha,"
                + "sum(getpremi2(a.gl_ins_id = 1027," + bulan + ")) as Utang_Lain,"
                + "sum(getpremi2(a.gl_ins_id = 1028," + bulan + ")) as Utang_Jangka_Panjang,"
                + "sum(getpremi2(a.gl_ins_id = 1029," + bulan + ")) as Titipan_Modal_Disetor,"
                + "sum(getpremi2(a.gl_ins_id = 1030," + bulan + ")) as Titipan_Modal_Disetor_2,"
                + "sum(getpremi2(a.gl_ins_id = 1031," + bulan + ")) as Agio_Saham,"
                + "sum(getpremi2(a.gl_ins_id = 1032," + bulan + ")) as Cadangan_Umum,"
                + "sum(getpremi2(a.gl_ins_id = 1033," + bulan + ")) as KeuntunganKerugian_blm_disetor,"
                + "sum(getpremi2(a.gl_ins_id = 1034," + bulan + ")) as Cadangan_Khusus,"
                + "sum(getpremi2(a.gl_ins_id = 1035," + bulan + ")) as Laba_Rugi_Ditahan,"
                + "sum(getpremi2(a.gl_ins_id = 1036," + bulan + ")) as Saldo_Laba_Rugi_Tahun_Lalu,"
                + "sum(getpremi2(a.gl_ins_id = 1037," + bulan + ")) as Saldo_Laba_Rugi_Tahun_Berjalan,"
                + "sum(getpremi2(a.gl_ins_id = 1038," + bulan + ")) as Premi_Bruto,"
                + "sum(getpremi2(a.gl_ins_id = 1039," + bulan + ")) as Premi_Reasuransi,"
                + "sum(getpremi2(a.gl_ins_id = 1040," + bulan + ")) as Penurunan_Kenaikan_PYBMP,"
                + "sum(getpremi2(a.gl_ins_id = 1041," + bulan + ")) as Klaim_Bruto,"
                + "sum(getpremi2(a.gl_ins_id = 1042," + bulan + ")) as Klaim_Reasuransi,"
                + "sum(getpremi2(a.gl_ins_id = 1043," + bulan + ")) as Kenaikan_Penurunan_EKRS,"
                + "sum(getpremi2(a.gl_ins_id = 1044," + bulan + ")) as Beban_Komisi_Netto,"
                + "sum(getpremi2(a.gl_ins_id = 1045," + bulan + ")) as Beban_Underwriting_Lain_Neto,"
                + "sum(getpremi2(a.gl_ins_id = 1046," + bulan + ")) as Hasil_Investasi,"
                + "sum(getpremi2(a.gl_ins_id = 1047," + bulan + ")) as Beban_Usaha,"
                + "sum(getpremi2(a.gl_ins_id = 1048," + bulan + ")) as Laba_Bersih ");

        sqa.addQuery(" from gl_neraca_total a "
                + "inner join gl_acct_bal_neracatotal b on b.account_id  = a.gl_ins_id ");

        sqa.addClause(" a.active_flag = 'Y' ");

        if (year != null) {
            sqa.addClause(" b.period_year = ?");
            sqa.addPar(periodTo);
        }

        final String sql = sqa.getSQL() + " group by b.period_year ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                GLNeracaTotalView.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void save(String pf, String level, String periodTo, String year) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] content = out.toByteArray();
        String fileName = pf + "_" + level + "_" + periodTo + "_" + year;

        File fo = new File("C:/");

        String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String sf = sdf.format(new Date());
        String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
        String path1 = fileFOlder + File.separator + "report_temp" + File.separator;
        String pathTemp = tempPath + File.separator + fileName + "_temp.pdf";
        String pathFinal = fileFOlder + File.separator + "report" + File.separator + fileName + ".pdf";

        if (pf.equals("account")) {
            if (level.equals("1") || level.equals("3")) {
                try {
                    new File(path1).mkdir();
                    new File(tempPath).mkdir();
                } catch (Exception e) {
                }

                fo = new File(pathTemp);

                FileOutputStream fop = new FileOutputStream(fo.getCanonicalPath());

                try {
                    fop.write(content);
                    fop.close();

                } catch (Exception e) {

                    fop.close();
                    fo.delete();

                    throw e;
                }

                List<InputStream> list = new ArrayList<InputStream>();

                list.add(new FileInputStream(new File(pathTemp)));

                // Resulting pdf
                OutputStream out2 = new FileOutputStream(new File(pathFinal));
                PDFMerge merge = new PDFMerge();
                merge.doMerge(list, out2);
            }
        }
    }

    public String getUserApprove(String koda, Date start, Date end) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(" select user_id from s_users_period where cc_code = ? "
                    + "and date_trunc('day',period_start) <= ? "
                    + "and date_trunc('day',period_end) >= ? ");

            S.setParam(1, koda);
            S.setParam(2, start);
            S.setParam(3, end);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public void refresh() {
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

    public void clickPrintValidate() throws Exception {

        loadFormList();

        String pf = stPrintForm;
        String lv = clAccDet;

        String level = null;
        if (stNeraca != null) {
            level = stNeraca;
        }

        if (stKas != null) {
            level = stKas;
        }

        if (stNeracaHarian != null) {
            level = stNeracaHarian;
        }

        if (rptfmt != null && !"default".equals(rptfmt)) {
            pf += "_" + rptfmt;
        }

        String filename = pf + level + "-" + periodToName + year;
        SessionManager.getInstance().getRequest().setAttribute("SAVE_TO_FILE", "Y");
        SessionManager.getInstance().getRequest().setAttribute("FILE_NAME", filename);
        SessionManager.getInstance().getRequest().setAttribute("REPORT_PROD", "Y");

        final ArrayList plist = new ArrayList();

        plist.add(pf + "_" + level + "_" + clAccDet);

        plist.add(pf + "_" + level);

        plist.add(pf);

        String urxSave = null;

        logger.logDebug("print: scanlist:" + plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains(s + ".fop.jsp")) {
                urxSave = "/pages/fin_report/" + s + "_SAVE.fop";
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
        String uploadPath = "fi-" + filename + ".pdf";
//        copyWithFTP(filePath, uploadPath);

        super.redirect(urxSave);
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

    public UserSessionView getUser(String stUserID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stUserID);
    }

    public void chgform() {
    }
    private String clIntRkp;

    /**
     * @return the clIntRkp
     */
    public String getClIntRkp() {
        return clIntRkp;
    }

    /**
     * @param clIntRkp the clIntRkp to set
     */
    public void setClIntRkp(String clIntRkp) {
        this.clIntRkp = clIntRkp;
    }

//    public void EXCEL_LABARUGIHARIAN() throws Exception {
    public DTOList EXCEL_LABARUGIHARIAN() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();
//        int q = stAccountNo.substring(0, 5).length();

//        int start = DateUtil.getMonthDigit(dtApplyDateFrom);
//        int end = DateUtil.getMonthDigit(dtApplyDateTo);

        String gljedetail = null;
//        String gljedetailYear = DateUtil.getYear(dtApplyDateFrom);
        String gljedetailYear = year;
        String gljedetailYearCurrent = DateUtil.getYear(new Date());

        if (gljedetailYear.equalsIgnoreCase(getYearPosting())
                || gljedetailYear.equalsIgnoreCase(gljedetailYearCurrent)) {
            gljedetail = "gl_je_detail";
        } else {
            gljedetail = "gl_je_detail_" + gljedetailYear;
        }

        sqa.addSelect(" distinct a.trx_id,a.accountid,a.applydate,a.period_no,a.fiscal_year,b.accountno,"
                + "substr(b.accountno,1,2) as len2,substr(b.accountno,1,3) as len3,substr(b.accountno,1,4) as len4,substr(b.accountno,1,5) as len5,"
                + "a.debit,a.credit,(a.debit-a.credit) as balance,substr(b.accountno,14,2) as kodaakun,substr(b.accountno,11,2) as kodacob,"
                + "c.cc_code,(case when c.cc_code_source is not null then c.cc_code_source else substr(b.accountno,14,2) end) as cc_code_source ");

        sqa.addQuery(" from " + gljedetail + " a "
                + " inner join gl_accounts b on b.account_id = a.accountid and b.acctype is null "
                + "left join ins_policy c on c.pol_no = a.pol_no and c.status in ('POLICY','RENEWAL','ENDORSE') and c.active_flag = 'Y' and c.effective_flag = 'Y' ");

        sqa.addClause("substr(b.accountno,1,1) between '6' and '9'");
        sqa.addClause("a.fiscal_year = '" + year + "'");
        sqa.addClause("a.period_no = '" + Integer.parseInt(periodFrom) + "'");
//        sqa.addClause("a.fiscal_year = '" + DateUtil.getYear(dtApplyDateFrom) + "'");
//        sqa.addClause("a.period_no = '" + Integer.toString(start) + "'");

        if (dtApplyDateFrom != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= '" + dtApplyDateFrom + "'");
            //sqa.addPar(dtApplyDateFrom);
        }

        if (dtApplyDateTo != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= '" + dtApplyDateTo + "'");
            //sqa.addPar(dtApplyDateTo);
        }

        if (branch != null) {
            sqa.addClause("substr(b.accountno,14,2) = '" + branch + "'");
            //sqa.addPar(branch);
        }

        String sql = "select len2,len3,len5,kodaakun,cc_code_source,kodacob,"
                + "(case when len2 = '61' then 1 when len2 = '63' then 2 when len2 = '64' then 3 when len5 in ('71713','71723','71733','71813','71823') then 5 "
                + "when len2 = '71' then 4 when len2 = '72' then 6 when len2 = '75' then 7 when len3 = '771' then 8 when len3 = '772' then 9 when len2 = '79' then 10 "
                + "when len2 = '65' then 11 when len2 = '81' then 12 when len2 = '82' then 13 when len2 = '83' then 14 when len2 in ('69','89') then 15 end) as norut,"
                + "(case when len2 = '61' then 'PREMIBRUTO (61)' when len2 = '63' then 'PREMIREAS (63)' when len2 = '64' then 'KYBMP PREMI (64)' "
                + "when len5 in ('71713','71723','71733','71813','71823') then 'SUBROGASI (717,718)' when len2 = '71' then 'KLAIMBRUTO (71)' "
                + "when len2 = '72' then 'KLAIMREAS (72)' when len2 = '75' then 'ESTKLAIM (75)' when len3 = '771' then 'KOMISIBAYAR (771)' "
                + "when len3 = '772' then 'KOMISITERIMA (772)' when len2 = '79' then 'BEBANUW (79)' when len2 = '65' then 'INVESTASI (65)' "
                + "when len2 = '81' then 'BEBANPMS (81)' when len2 = '82' then 'BEBANUMUM (82)' when len2 = '83' then 'BEBANADM (83)' "
                + "when len2 in ('69','89') then 'BEBANLAIN (69,89)' end) as ket,"
                + "(case when len2 = '61' then balance*-1 when len2 = '63' then balance when len2 = '64' then balance "
                + "when len5 in ('71713','71723','71733','71813','71823') then balance*-1 when len2 = '71' then balance "
                + "when len2 = '72' then balance*-1 when len2 = '75' then balance when len3 = '771' then balance when len3 = '772' then balance*-1 "
                + "when len2 = '79' then balance when len2 = '65' then balance*-1 when len2 = '81' then balance when len2 = '82' then balance when len2 = '83' then balance "
                + "when len2 in ('69','89') then balance*-1 end) as balance from ( "
                + "select kodaakun,cc_code,cc_code_source,kodacob,len2,len3,len4,len5,sum(debit) as debit,sum(credit) as credit,sum(balance) as balance from ( "
                + sqa.getSQL() + " order by a.trx_id ) a group by 1,2,3,4,5,6,7,8 order by 1,2,3,8 ) a ";

        String sqlKoda = "select norut,ket,sum(balance) as nasional,"
                + "sum(getpremi2(kodaakun = '10',balance)) as koda10,sum(getpremi2(kodaakun = '11',balance)) as koda11,sum(getpremi2(kodaakun = '12',balance)) as koda12,"
                + "sum(getpremi2(kodaakun = '13',balance)) as koda13,sum(getpremi2(kodaakun = '14',balance)) as koda14,sum(getpremi2(kodaakun = '15',balance)) as koda15,"
                + "sum(getpremi2(kodaakun = '16',balance)) as koda16,sum(getpremi2(kodaakun = '17',balance)) as koda17,sum(getpremi2(kodaakun = '18',balance)) as koda18,"
                + "sum(getpremi2(kodaakun = '19',balance)) as koda19,sum(getpremi2(kodaakun = '20',balance)) as koda20,sum(getpremi2(kodaakun = '21',balance)) as koda21,"
                + "sum(getpremi2(kodaakun = '22',balance)) as koda22,sum(getpremi2(kodaakun = '23',balance)) as koda23,sum(getpremi2(kodaakun = '24',balance)) as koda24,"
                + "sum(getpremi2(kodaakun = '25',balance)) as koda25,sum(getpremi2(kodaakun = '30',balance)) as koda30,sum(getpremi2(kodaakun = '31',balance)) as koda31,"
                + "sum(getpremi2(kodaakun = '32',balance)) as koda32,sum(getpremi2(kodaakun = '33',balance)) as koda33,sum(getpremi2(kodaakun = '40',balance)) as koda40,"
                + "sum(getpremi2(kodaakun = '41',balance)) as koda41,sum(getpremi2(kodaakun = '42',balance)) as koda42,sum(getpremi2(kodaakun = '43',balance)) as koda43,"
                + "sum(getpremi2(kodaakun = '44',balance)) as koda44,sum(getpremi2(kodaakun = '45',balance)) as koda45,sum(getpremi2(kodaakun = '50',balance)) as koda50,"
                + "sum(getpremi2(kodaakun = '51',balance)) as koda51,sum(getpremi2(kodaakun = '52',balance)) as koda52,sum(getpremi2(kodaakun = '60',balance)) as koda60,"
                + "sum(getpremi2(kodaakun = '61',balance)) as koda61,sum(getpremi2(kodaakun = '70',balance)) as koda70,sum(getpremi2(kodaakun = '80',balance)) as koda80,"
                + "sum(getpremi2(kodaakun = '00',balance)) as koda00 from (" + sql + ") a where norut is not null group by 1,2 order by 1 ";

        String sqlKodaAKS = "select norut,ket,sum(getpremi2(kodaakun = '80',balance)) as nasional,"
                + "sum(getpremi2(kodaakun = '80' and cc_code_source = '10',balance)) as akskoda10,sum(getpremi2(kodaakun = '80' and cc_code_source = '11',balance)) as akskoda11,sum(getpremi2(kodaakun = '80' and cc_code_source = '12',balance)) as akskoda12,"
                + "sum(getpremi2(kodaakun = '80' and cc_code_source = '13',balance)) as akskoda13,sum(getpremi2(kodaakun = '80' and cc_code_source = '14',balance)) as akskoda14,sum(getpremi2(kodaakun = '80' and cc_code_source = '15',balance)) as akskoda15,"
                + "sum(getpremi2(kodaakun = '80' and cc_code_source = '16',balance)) as akskoda16,sum(getpremi2(kodaakun = '80' and cc_code_source = '17',balance)) as akskoda17,sum(getpremi2(kodaakun = '80' and cc_code_source = '18',balance)) as akskoda18,"
                + "sum(getpremi2(kodaakun = '80' and cc_code_source = '19',balance)) as akskoda19,sum(getpremi2(kodaakun = '80' and cc_code_source = '20',balance)) as akskoda20,sum(getpremi2(kodaakun = '80' and cc_code_source = '21',balance)) as akskoda21,"
                + "sum(getpremi2(kodaakun = '80' and cc_code_source = '22',balance)) as akskoda22,sum(getpremi2(kodaakun = '80' and cc_code_source = '23',balance)) as akskoda23,sum(getpremi2(kodaakun = '80' and cc_code_source = '24',balance)) as akskoda24,"
                + "sum(getpremi2(kodaakun = '80' and cc_code_source = '25',balance)) as akskoda25,sum(getpremi2(kodaakun = '80' and cc_code_source = '30',balance)) as akskoda30,sum(getpremi2(kodaakun = '80' and cc_code_source = '31',balance)) as akskoda31,"
                + "sum(getpremi2(kodaakun = '80' and cc_code_source = '32',balance)) as akskoda32,sum(getpremi2(kodaakun = '80' and cc_code_source = '33',balance)) as akskoda33,sum(getpremi2(kodaakun = '80' and cc_code_source = '40',balance)) as akskoda40,"
                + "sum(getpremi2(kodaakun = '80' and cc_code_source = '41',balance)) as akskoda41,sum(getpremi2(kodaakun = '80' and cc_code_source = '42',balance)) as akskoda42,sum(getpremi2(kodaakun = '80' and cc_code_source = '43',balance)) as akskoda43,"
                + "sum(getpremi2(kodaakun = '80' and cc_code_source = '44',balance)) as akskoda44,sum(getpremi2(kodaakun = '80' and cc_code_source = '45',balance)) as akskoda45,sum(getpremi2(kodaakun = '80' and cc_code_source = '50',balance)) as akskoda50,"
                + "sum(getpremi2(kodaakun = '80' and cc_code_source = '51',balance)) as akskoda51,sum(getpremi2(kodaakun = '80' and cc_code_source = '52',balance)) as akskoda52,sum(getpremi2(kodaakun = '80' and cc_code_source = '60',balance)) as akskoda60,"
                + "sum(getpremi2(kodaakun = '80' and cc_code_source = '61',balance)) as akskoda61,sum(getpremi2(kodaakun = '80' and cc_code_source = '70',balance)) as akskoda70,sum(getpremi2(kodaakun = '80' and cc_code_source = '80',balance)) as akskoda80,"
                + "sum(getpremi2(kodaakun = '80' and cc_code_source = '00',balance)) as akskoda00 from (" + sql + ") a where norut is not null group by 1,2 order by 1 ";

        String sqlCob = "select norut,ket,sum(balance) as nasional,"
                + "sum(getpremi2(kodacob = '01',balance)) as cob01,sum(getpremi2(kodacob = '02',balance)) as cob02,sum(getpremi2(kodacob = '03',balance)) as cob03,"
                + "sum(getpremi2(kodacob = '04',balance)) as cob04,sum(getpremi2(kodacob = '05',balance)) as cob05,sum(getpremi2(kodacob = '06',balance)) as cob06,"
                + "sum(getpremi2(kodacob = '07',balance)) as cob07,sum(getpremi2(kodacob = '08',balance)) as cob08,sum(getpremi2(kodacob = '09',balance)) as cob09,"
                + "sum(getpremi2(kodacob = '10',balance)) as cob10,sum(getpremi2(kodacob = '11',balance)) as cob11,sum(getpremi2(kodacob = '12',balance)) as cob12,"
                + "sum(getpremi2(kodacob = '13',balance)) as cob13,sum(getpremi2(kodacob = '14',balance)) as cob14,sum(getpremi2(kodacob = '15',balance)) as cob15,"
                + "sum(getpremi2(kodacob = '16',balance)) as cob16,sum(getpremi2(kodacob = '17',balance)) as cob17,sum(getpremi2(kodacob = '18',balance)) as cob18,"
                + "sum(getpremi2(kodacob = '19',balance)) as cob19,sum(getpremi2(kodacob = '20',balance)) as cob20,sum(getpremi2(kodacob = '21',balance)) as cob21,"
                + "sum(getpremi2(kodacob = '23',balance)) as cob23,sum(getpremi2(kodacob = '24',balance)) as cob24,sum(getpremi2(kodacob = '25',balance)) as cob25,"
                + "sum(getpremi2(kodacob = '26',balance)) as cob26,sum(getpremi2(kodacob = '27',balance)) as cob27,sum(getpremi2(kodacob = '31',balance)) as cob31,"
                + "sum(getpremi2(kodacob = '32',balance)) as cob32,sum(getpremi2(kodacob = '33',balance)) as cob33,sum(getpremi2(kodacob = '35',balance)) as cob35,"
                + "sum(getpremi2(kodacob = '36',balance)) as cob36,sum(getpremi2(kodacob = '37',balance)) as cob37,sum(getpremi2(kodacob = '38',balance)) as cob38,"
                + "sum(getpremi2(kodacob = '41',balance)) as cob41,sum(getpremi2(kodacob = '42',balance)) as cob42,sum(getpremi2(kodacob = '43',balance)) as cob43,"
                + "sum(getpremi2(kodacob = '44',balance)) as cob44,sum(getpremi2(kodacob = '45',balance)) as cob45,sum(getpremi2(kodacob = '46',balance)) as cob46,"
                + "sum(getpremi2(kodacob = '47',balance)) as cob47,sum(getpremi2(kodacob = '48',balance)) as cob48,sum(getpremi2(kodacob = '51',balance)) as cob51,"
                + "sum(getpremi2(kodacob = '52',balance)) as cob52,sum(getpremi2(kodacob = '53',balance)) as cob53,sum(getpremi2(kodacob = '54',balance)) as cob54,"
                + "sum(getpremi2(kodacob = '55',balance)) as cob55,sum(getpremi2(kodacob = '56',balance)) as cob56,sum(getpremi2(kodacob = '57',balance)) as cob57,"
                + "sum(getpremi2(kodacob = '58',balance)) as cob58,sum(getpremi2(kodacob = '59',balance)) as cob59,sum(getpremi2(kodacob = '60',balance)) as cob60,"
                + "sum(getpremi2(kodacob = '61',balance)) as cob61,sum(getpremi2(kodacob = '62',balance)) as cob62,sum(getpremi2(kodacob = '63',balance)) as cob63,"
                + "sum(getpremi2(kodacob = '64',balance)) as cob64,sum(getpremi2(kodacob = '65',balance)) as cob65,sum(getpremi2(kodacob = '66',balance)) as cob66,"
                + "sum(getpremi2(kodacob = '67',balance)) as cob67,sum(getpremi2(kodacob = '68',balance)) as cob68,sum(getpremi2(kodacob = '69',balance)) as cob69,"
                + "sum(getpremi2(kodacob = '70',balance)) as cob70,sum(getpremi2(kodacob = '71',balance)) as cob71,sum(getpremi2(kodacob = '72',balance)) as cob72,"
                + "sum(getpremi2(kodacob = '73',balance)) as cob73,sum(getpremi2(kodacob = '74',balance)) as cob74,sum(getpremi2(kodacob = '75',balance)) as cob75,"
                + "sum(getpremi2(kodacob = '76',balance)) as cob76,sum(getpremi2(kodacob = '77',balance)) as cob77,sum(getpremi2(kodacob = '78',balance)) as cob78,"
                + "sum(getpremi2(kodacob = '80',balance)) as cob80,sum(getpremi2(kodacob = '81',balance)) as cob81,sum(getpremi2(kodacob = '82',balance)) as cob82,"
                + "sum(getpremi2(kodacob = '83',balance)) as cob83,sum(getpremi2(kodacob = '84',balance)) as cob84,sum(getpremi2(kodacob = '85',balance)) as cob85,"
                + "sum(getpremi2(kodacob = '86',balance)) as cob86,sum(getpremi2(kodacob = '87',balance)) as cob87,sum(getpremi2(kodacob = '88',balance)) as cob88,"
                + "sum(getpremi2(kodacob = '92',balance)) as cob92,sum(getpremi2(kodacob = '95',balance)) as cob95,sum(getpremi2(kodacob = '96',balance)) as cob96,"
                + "sum(getpremi2(kodacob = '97',balance)) as cob97,sum(getpremi2(kodacob = '00',balance)) as cob00  "
                + "from (" + sql + ") a where norut is not null group by 1,2 order by 1 ";

        final DTOList lKoda = ListUtil.getDTOListFromQuery(
                sqlKoda,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", lKoda);

        final DTOList lCob = ListUtil.getDTOListFromQuery(
                sqlCob,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", lCob);

        final DTOList lKodaAKS = ListUtil.getDTOListFromQuery(
                sqlKodaAKS,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT3", lKodaAKS);

        return lKoda;

        /*
        SQLUtil S = new SQLUtil();

        String nama_file = "EXCEL_LABARUGIHARIAN" + System.currentTimeMillis() + ".csv";

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
         */
    }

    public void EXPORT_LABARUGIHARIAN() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheetKoda = wb.createSheet("Koda");
        XSSFSheet sheetCob = wb.createSheet("Cob");
        XSSFSheet sheetKodaAKS = wb.createSheet("KodaAKS");

        final DTOList listKoda = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        final DTOList listCob = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");
        final DTOList listKodaAKS = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT3");

        for (int i = 0; i < listKoda.size(); i++) {
            HashDTO h = (HashDTO) listKoda.get(i);

            XSSFRow row0 = sheetKoda.createRow(0);
            if (getDtApplyDateFrom() != null) {
                row0.createCell(0).setCellValue("Mutasi : " + DateUtil.getDateStr(getDtApplyDateFrom()) + " sd " + DateUtil.getDateStr(getDtApplyDateTo()));
            }

            //bikin header
            XSSFRow row1 = sheetKoda.createRow(2);
            row1.createCell(0).setCellValue("KETERANGAN");
            row1.createCell(1).setCellValue("ACEH");
            row1.createCell(2).setCellValue("MEDAN");
            row1.createCell(3).setCellValue("PADANG");
            row1.createCell(4).setCellValue("RIAU");
            row1.createCell(5).setCellValue("JAMBI");
            row1.createCell(6).setCellValue("PALEMBANG");
            row1.createCell(7).setCellValue("BENGKULU");
            row1.createCell(8).setCellValue("LAMPUNG");
            row1.createCell(9).setCellValue("BANGKA BELITUNG");
            row1.createCell(10).setCellValue("BATAM");
            row1.createCell(11).setCellValue("JAKARTA");
            row1.createCell(12).setCellValue("BANDUNG");
            row1.createCell(13).setCellValue("SEMARANG");
            row1.createCell(14).setCellValue("YOGYAKARTA");
            row1.createCell(15).setCellValue("SURABAYA");
            row1.createCell(16).setCellValue("SERANG");
            row1.createCell(17).setCellValue("PONTIANAK");
            row1.createCell(18).setCellValue("PALANGKARAYA");
            row1.createCell(19).setCellValue("BANJARMASIN");
            row1.createCell(20).setCellValue("SAMARINDA");
            row1.createCell(21).setCellValue("MANADO");
            row1.createCell(22).setCellValue("PALU");
            row1.createCell(23).setCellValue("KENDARI");
            row1.createCell(24).setCellValue("MAKASSAR");
            row1.createCell(25).setCellValue("MAMUJU");
            row1.createCell(26).setCellValue("GORONTALO");
            row1.createCell(27).setCellValue("DENPASAR");
            row1.createCell(28).setCellValue("MATARAM");
            row1.createCell(29).setCellValue("KUPANG");
            row1.createCell(30).setCellValue("AMBON");
            row1.createCell(31).setCellValue("TERNATE");
            row1.createCell(32).setCellValue("PAPUA");
            row1.createCell(33).setCellValue("UNIT NON AKS");
            row1.createCell(34).setCellValue("KANTOR PUSAT");
            row1.createCell(35).setCellValue("NASIONAL");

            //bikin isi cell
            XSSFRow row = sheetKoda.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ket"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("koda10").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("koda11").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("koda12").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("koda13").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("koda14").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("koda15").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("koda16").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("koda17").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("koda18").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("koda19").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("koda20").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("koda21").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("koda22").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("koda23").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("koda24").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("koda25").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("koda30").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("koda31").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("koda32").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("koda33").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("koda40").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("koda41").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("koda42").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("koda43").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("koda44").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("koda45").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("koda50").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("koda51").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("koda52").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("koda60").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("koda61").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("koda70").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("koda80").doubleValue());
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("koda00").doubleValue());
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("nasional").doubleValue());

        }

        for (int i = 0; i < listCob.size(); i++) {
            HashDTO h = (HashDTO) listCob.get(i);

            XSSFRow row0 = sheetCob.createRow(0);
            if (getDtApplyDateFrom() != null) {
                row0.createCell(0).setCellValue("Mutasi : " + DateUtil.getDateStr(getDtApplyDateFrom()) + " sd " + DateUtil.getDateStr(getDtApplyDateTo()));
            }

            //bikin header
            XSSFRow row1 = sheetCob.createRow(2);
            row1.createCell(0).setCellValue("KETERANGAN");
            row1.createCell(1).setCellValue("Kebakaran");
            row1.createCell(2).setCellValue("Konsorsium Pasar");
            row1.createCell(3).setCellValue("Kendaraan Bermotor");
            row1.createCell(4).setCellValue("Kecelakaan Diri");
            row1.createCell(5).setCellValue("Cash in Safe");
            row1.createCell(6).setCellValue("Contractors All Risks");
            row1.createCell(7).setCellValue("Erection All Risks");
            row1.createCell(8).setCellValue("Machinery Breakdown");
            row1.createCell(9).setCellValue("Contractor Plant Machinery and Equipment insurance (CPME)");
            row1.createCell(10).setCellValue("Electronic Equipment");
            row1.createCell(11).setCellValue("Public Liability");
            row1.createCell(12).setCellValue("Burglary");
            row1.createCell(13).setCellValue("Pengangkutan");
            row1.createCell(14).setCellValue("Cash in Transit");
            row1.createCell(15).setCellValue("Rangka Kapal");
            row1.createCell(16).setCellValue("Golfers Indemnity");
            row1.createCell(17).setCellValue("General Accident");
            row1.createCell(18).setCellValue("Aviation Hull");
            row1.createCell(19).setCellValue("Gempa Bumi");
            row1.createCell(20).setCellValue("PJTKI");
            row1.createCell(21).setCellValue("PA Kreasi");
            row1.createCell(22).setCellValue("Terrorism dan Sabotage");
            row1.createCell(23).setCellValue("Heavy Equipment");
            row1.createCell(24).setCellValue("Kebakaran");
            row1.createCell(25).setCellValue("Kendaraan Bermotor");
            row1.createCell(26).setCellValue("Kecelakaan Diri");
            row1.createCell(27).setCellValue("Sipanda (Tab. Simpeda)");
            row1.createCell(28).setCellValue("Sipanda (Tab. Non Simpeda)");
            row1.createCell(29).setCellValue("Sipanda (Tab. Haji)");
            row1.createCell(30).setCellValue("Marine");
            row1.createCell(31).setCellValue("Non Marine");
            row1.createCell(32).setCellValue("Engineering");
            row1.createCell(33).setCellValue("Surety Bond");
            row1.createCell(34).setCellValue("Cash Management");
            row1.createCell(35).setCellValue("CIS (Cash Management)");
            row1.createCell(36).setCellValue("Asset (Oil and Gas)");
            row1.createCell(37).setCellValue("Project (Oil and Gas)");
            row1.createCell(38).setCellValue("Jaminan Pelaksanaan Sisa Pekerjaan");
            row1.createCell(39).setCellValue("Jaminan Sanggahan Banding");
            row1.createCell(40).setCellValue("KBG - Jaminan Pelaksanaan Sisa Pekerjaan");
            row1.createCell(41).setCellValue("KBG - Jaminan Sanggahan Banding");
            row1.createCell(42).setCellValue("Jaminan Penawaran");
            row1.createCell(43).setCellValue("Jaminan Pelaksanaan");
            row1.createCell(44).setCellValue("Jaminan Pembayaran Uang Muka");
            row1.createCell(45).setCellValue("Jaminan Pemeliharaan");
            row1.createCell(46).setCellValue("KBG - Jaminan Penawaran");
            row1.createCell(47).setCellValue("KBG - Jaminan Pelaksanaan");
            row1.createCell(48).setCellValue("KBG - Jaminan Pembayaran Uang Muka");
            row1.createCell(49).setCellValue("KBG - Jaminan Pemeliharaan");
            row1.createCell(50).setCellValue("Kredit");
            row1.createCell(51).setCellValue("Kredit Konstruksi");
            row1.createCell(52).setCellValue("Automobile Liability");
            row1.createCell(53).setCellValue("Comprehensive General Liability");
            row1.createCell(54).setCellValue("Employers Liability");
            row1.createCell(55).setCellValue("Travel");
            row1.createCell(56).setCellValue("Moveable All Risks");
            row1.createCell(57).setCellValue("Plate Glass / Neon Signs");
            row1.createCell(58).setCellValue("Professional Indemnity");
            row1.createCell(59).setCellValue("Boiler Explosion Insurance");
            row1.createCell(60).setCellValue("Workmens Compensation");
            row1.createCell(61).setCellValue("PPJU");
            row1.createCell(62).setCellValue("Kesehatan");
            row1.createCell(63).setCellValue("Gempa Bumi Konsorsium");
            row1.createCell(64).setCellValue("Kredit Modal Kerja");
            row1.createCell(65).setCellValue("Kredit Investasi");
            row1.createCell(66).setCellValue("Customs Bonds");
            row1.createCell(67).setCellValue("Customs Bonds KITE");
            row1.createCell(68).setCellValue("Customs Bonds KABER");
            row1.createCell(69).setCellValue("Customs Bonds OB23");
            row1.createCell(70).setCellValue("Kredit Produktif");
            row1.createCell(71).setCellValue("Property All Risks");
            row1.createCell(72).setCellValue("Civil Engineering Completed Risk (CECR)");
            row1.createCell(73).setCellValue("House Hold");
            row1.createCell(74).setCellValue("Fidelity Guarantee");
            row1.createCell(75).setCellValue("KPR BTN Kebakaran dan Gempa Bumi");
            row1.createCell(76).setCellValue("Professional Indemnity - Medis dan Kesehatan");
            row1.createCell(77).setCellValue("Kredit (Macet Only)");
            row1.createCell(78).setCellValue("Kredit Serbaguna");
            row1.createCell(79).setCellValue("Hole in One");
            row1.createCell(80).setCellValue("General (PA)");
            row1.createCell(81).setCellValue("General (Non PA)");
            row1.createCell(82).setCellValue("TP");
            row1.createCell(83).setCellValue("Non COB");
            row1.createCell(84).setCellValue("Nasional");

            //bikin isi cell
            XSSFRow row = sheetCob.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ket"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("cob01").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("cob02").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("cob03").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("cob04").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("cob05").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("cob06").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("cob07").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("cob08").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("cob09").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("cob10").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("cob11").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("cob12").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("cob13").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("cob14").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("cob15").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("cob16").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("cob17").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("cob18").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("cob19").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("cob20").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("cob21").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("cob23").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("cob24").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("cob25").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("cob26").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("cob27").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("cob31").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("cob32").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("cob33").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("cob35").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("cob36").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("cob37").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("cob38").doubleValue());
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("cob41").doubleValue());
            row.createCell(35).setCellValue(h.getFieldValueByFieldNameBD("cob42").doubleValue());
            row.createCell(36).setCellValue(h.getFieldValueByFieldNameBD("cob43").doubleValue());
            row.createCell(37).setCellValue(h.getFieldValueByFieldNameBD("cob44").doubleValue());
            row.createCell(38).setCellValue(h.getFieldValueByFieldNameBD("cob45").doubleValue());
            row.createCell(39).setCellValue(h.getFieldValueByFieldNameBD("cob46").doubleValue());
            row.createCell(40).setCellValue(h.getFieldValueByFieldNameBD("cob47").doubleValue());
            row.createCell(41).setCellValue(h.getFieldValueByFieldNameBD("cob48").doubleValue());
            row.createCell(42).setCellValue(h.getFieldValueByFieldNameBD("cob51").doubleValue());
            row.createCell(43).setCellValue(h.getFieldValueByFieldNameBD("cob52").doubleValue());
            row.createCell(44).setCellValue(h.getFieldValueByFieldNameBD("cob53").doubleValue());
            row.createCell(45).setCellValue(h.getFieldValueByFieldNameBD("cob54").doubleValue());
            row.createCell(46).setCellValue(h.getFieldValueByFieldNameBD("cob55").doubleValue());
            row.createCell(47).setCellValue(h.getFieldValueByFieldNameBD("cob56").doubleValue());
            row.createCell(48).setCellValue(h.getFieldValueByFieldNameBD("cob57").doubleValue());
            row.createCell(49).setCellValue(h.getFieldValueByFieldNameBD("cob58").doubleValue());
            row.createCell(50).setCellValue(h.getFieldValueByFieldNameBD("cob59").doubleValue());
            row.createCell(51).setCellValue(h.getFieldValueByFieldNameBD("cob60").doubleValue());
            row.createCell(52).setCellValue(h.getFieldValueByFieldNameBD("cob61").doubleValue());
            row.createCell(53).setCellValue(h.getFieldValueByFieldNameBD("cob62").doubleValue());
            row.createCell(54).setCellValue(h.getFieldValueByFieldNameBD("cob63").doubleValue());
            row.createCell(55).setCellValue(h.getFieldValueByFieldNameBD("cob64").doubleValue());
            row.createCell(56).setCellValue(h.getFieldValueByFieldNameBD("cob65").doubleValue());
            row.createCell(57).setCellValue(h.getFieldValueByFieldNameBD("cob66").doubleValue());
            row.createCell(58).setCellValue(h.getFieldValueByFieldNameBD("cob67").doubleValue());
            row.createCell(59).setCellValue(h.getFieldValueByFieldNameBD("cob68").doubleValue());
            row.createCell(60).setCellValue(h.getFieldValueByFieldNameBD("cob69").doubleValue());
            row.createCell(61).setCellValue(h.getFieldValueByFieldNameBD("cob70").doubleValue());
            row.createCell(62).setCellValue(h.getFieldValueByFieldNameBD("cob71").doubleValue());
            row.createCell(63).setCellValue(h.getFieldValueByFieldNameBD("cob72").doubleValue());
            row.createCell(64).setCellValue(h.getFieldValueByFieldNameBD("cob73").doubleValue());
            row.createCell(65).setCellValue(h.getFieldValueByFieldNameBD("cob74").doubleValue());
            row.createCell(66).setCellValue(h.getFieldValueByFieldNameBD("cob75").doubleValue());
            row.createCell(67).setCellValue(h.getFieldValueByFieldNameBD("cob76").doubleValue());
            row.createCell(68).setCellValue(h.getFieldValueByFieldNameBD("cob77").doubleValue());
            row.createCell(69).setCellValue(h.getFieldValueByFieldNameBD("cob78").doubleValue());
            row.createCell(70).setCellValue(h.getFieldValueByFieldNameBD("cob80").doubleValue());
            row.createCell(71).setCellValue(h.getFieldValueByFieldNameBD("cob81").doubleValue());
            row.createCell(72).setCellValue(h.getFieldValueByFieldNameBD("cob82").doubleValue());
            row.createCell(73).setCellValue(h.getFieldValueByFieldNameBD("cob83").doubleValue());
            row.createCell(74).setCellValue(h.getFieldValueByFieldNameBD("cob84").doubleValue());
            row.createCell(75).setCellValue(h.getFieldValueByFieldNameBD("cob85").doubleValue());
            row.createCell(76).setCellValue(h.getFieldValueByFieldNameBD("cob86").doubleValue());
            row.createCell(77).setCellValue(h.getFieldValueByFieldNameBD("cob87").doubleValue());
            row.createCell(78).setCellValue(h.getFieldValueByFieldNameBD("cob88").doubleValue());
            row.createCell(79).setCellValue(h.getFieldValueByFieldNameBD("cob92").doubleValue());
            row.createCell(80).setCellValue(h.getFieldValueByFieldNameBD("cob95").doubleValue());
            row.createCell(81).setCellValue(h.getFieldValueByFieldNameBD("cob96").doubleValue());
            row.createCell(82).setCellValue(h.getFieldValueByFieldNameBD("cob97").doubleValue());
            row.createCell(83).setCellValue(h.getFieldValueByFieldNameBD("cob00").doubleValue());
            row.createCell(84).setCellValue(h.getFieldValueByFieldNameBD("nasional").doubleValue());

        }

        for (int i = 0; i < listKodaAKS.size(); i++) {
            HashDTO h = (HashDTO) listKodaAKS.get(i);

            XSSFRow row0 = sheetKodaAKS.createRow(0);
            if (getDtApplyDateFrom() != null) {
                row0.createCell(0).setCellValue("Mutasi : " + DateUtil.getDateStr(getDtApplyDateFrom()) + " sd " + DateUtil.getDateStr(getDtApplyDateTo()));
            }

            //bikin header
            XSSFRow row1 = sheetKodaAKS.createRow(2);
            row1.createCell(0).setCellValue("KETERANGAN");
            row1.createCell(1).setCellValue("ACEH");
            row1.createCell(2).setCellValue("MEDAN");
            row1.createCell(3).setCellValue("PADANG");
            row1.createCell(4).setCellValue("RIAU");
            row1.createCell(5).setCellValue("JAMBI");
            row1.createCell(6).setCellValue("PALEMBANG");
            row1.createCell(7).setCellValue("BENGKULU");
            row1.createCell(8).setCellValue("LAMPUNG");
            row1.createCell(9).setCellValue("BANGKA BELITUNG");
            row1.createCell(10).setCellValue("BATAM");
            row1.createCell(11).setCellValue("JAKARTA");
            row1.createCell(12).setCellValue("BANDUNG");
            row1.createCell(13).setCellValue("SEMARANG");
            row1.createCell(14).setCellValue("YOGYAKARTA");
            row1.createCell(15).setCellValue("SURABAYA");
            row1.createCell(16).setCellValue("SERANG");
            row1.createCell(17).setCellValue("PONTIANAK");
            row1.createCell(18).setCellValue("PALANGKARAYA");
            row1.createCell(19).setCellValue("BANJARMASIN");
            row1.createCell(20).setCellValue("SAMARINDA");
            row1.createCell(21).setCellValue("MANADO");
            row1.createCell(22).setCellValue("PALU");
            row1.createCell(23).setCellValue("KENDARI");
            row1.createCell(24).setCellValue("MAKASSAR");
            row1.createCell(25).setCellValue("MAMUJU");
            row1.createCell(26).setCellValue("GORONTALO");
            row1.createCell(27).setCellValue("DENPASAR");
            row1.createCell(28).setCellValue("MATARAM");
            row1.createCell(29).setCellValue("KUPANG");
            row1.createCell(30).setCellValue("AMBON");
            row1.createCell(31).setCellValue("TERNATE");
            row1.createCell(32).setCellValue("PAPUA");
            row1.createCell(33).setCellValue("NON CABANG");
            row1.createCell(34).setCellValue("UNIT NON AKS");

            //bikin isi cell
            XSSFRow row = sheetKodaAKS.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ket"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("akskoda10").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("akskoda11").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("akskoda12").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("akskoda13").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("akskoda14").doubleValue());
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("akskoda15").doubleValue());
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("akskoda16").doubleValue());
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("akskoda17").doubleValue());
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("akskoda18").doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("akskoda19").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameBD("akskoda20").doubleValue());
            row.createCell(12).setCellValue(h.getFieldValueByFieldNameBD("akskoda21").doubleValue());
            row.createCell(13).setCellValue(h.getFieldValueByFieldNameBD("akskoda22").doubleValue());
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameBD("akskoda23").doubleValue());
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameBD("akskoda24").doubleValue());
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameBD("akskoda25").doubleValue());
            row.createCell(17).setCellValue(h.getFieldValueByFieldNameBD("akskoda30").doubleValue());
            row.createCell(18).setCellValue(h.getFieldValueByFieldNameBD("akskoda31").doubleValue());
            row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("akskoda32").doubleValue());
            row.createCell(20).setCellValue(h.getFieldValueByFieldNameBD("akskoda33").doubleValue());
            row.createCell(21).setCellValue(h.getFieldValueByFieldNameBD("akskoda40").doubleValue());
            row.createCell(22).setCellValue(h.getFieldValueByFieldNameBD("akskoda41").doubleValue());
            row.createCell(23).setCellValue(h.getFieldValueByFieldNameBD("akskoda42").doubleValue());
            row.createCell(24).setCellValue(h.getFieldValueByFieldNameBD("akskoda43").doubleValue());
            row.createCell(25).setCellValue(h.getFieldValueByFieldNameBD("akskoda44").doubleValue());
            row.createCell(26).setCellValue(h.getFieldValueByFieldNameBD("akskoda45").doubleValue());
            row.createCell(27).setCellValue(h.getFieldValueByFieldNameBD("akskoda50").doubleValue());
            row.createCell(28).setCellValue(h.getFieldValueByFieldNameBD("akskoda51").doubleValue());
            row.createCell(29).setCellValue(h.getFieldValueByFieldNameBD("akskoda52").doubleValue());
            row.createCell(30).setCellValue(h.getFieldValueByFieldNameBD("akskoda60").doubleValue());
            row.createCell(31).setCellValue(h.getFieldValueByFieldNameBD("akskoda61").doubleValue());
            row.createCell(32).setCellValue(h.getFieldValueByFieldNameBD("akskoda70").doubleValue());
            row.createCell(33).setCellValue(h.getFieldValueByFieldNameBD("akskoda80").doubleValue());
            row.createCell(34).setCellValue(h.getFieldValueByFieldNameBD("nasional").doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=buku harian_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }
}

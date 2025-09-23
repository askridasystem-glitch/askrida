/***********************************************************************
 * Module:  com.webfin.gl.accounts.forms.CurrencyList
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:32 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.form;

import com.crux.common.model.HashDTO;
import com.crux.lang.LanguageManager;
import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLAssembler;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.crux.web.controller.SessionManager;
import com.webfin.insurance.model.InsuranceClosingReportView;
import com.webfin.insurance.model.InsuranceClosingView;
import com.webfin.insurance.model.InsurancePolicySOAView;
import com.webfin.insurance.model.InsurancePostingView;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.Date;
import javax.servlet.ServletOutputStream;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class InsuranceClosingList extends Form {

    private final static transient LogManager logger = LogManager.getInstance(InsuranceClosingList.class);
    private DTOList list;
    private DTOList listcl;
    private String glpostingid;
    private String stCurrencyCode;
    private String stCurrencyDesc;
    private BigDecimal dbRate;
    private String stActiveFlag;
    private String branch = SessionManager.getInstance().getSession().getStBranch();
    private boolean enableReverse = SessionManager.getInstance().getSession().hasResource("INS_CLOSING_REVERSE");
    private boolean enableApproveClosing = SessionManager.getInstance().getSession().hasResource("POL_RI_CLOSING_APRV");
    private String stMonths;
    private String stYears;
    private Date dtPolicyDateStart;
    private Date dtPolicyDateEnd;
    private Date dtPeriodStartStart;
    private Date dtPeriodStartEnd;
    private String stPolicyTypeGroupID;
    private String stPolicyTypeID;
    private Date dtInvoiceDateStart;
    private Date dtInvoiceDateEnd;
    private String printLang;

    private DTOList listtax;

    private DTOList listreport;

    private DTOList listprod;

    private String stPeriodFrom;
    private String stPeriodTo;

    private String stSOAReportID;
    private String stPolCredit;

    private DTOList listSOA;

    public DTOList getListSOA() throws Exception {

        if (listSOA == null) {
            listSOA = new DTOList();
            listSOA.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.*");

        sqa.addQuery(" from ins_gl_soa a ");

        /*
        sqa.addClause("closing_type in ('PREMIUM_RI_OUTWARD','PREMIUM_RI_INWARD','PREMIUM_RI_INWARD_OUTWARD')");

        if (dtPolicyDateStart != null) {
            sqa.addClause("date_trunc('day',a.policy_date_start) >= ?");
            sqa.addPar(dtPolicyDateStart);
        }

        if (dtPolicyDateEnd != null) {
            sqa.addClause("date_trunc('day',a.policy_date_end) <= ?");
            sqa.addPar(dtPolicyDateEnd);
        }

        if (dtPeriodStartStart != null) {
            sqa.addClause("date_trunc('day',a.period_start_start) >= ?");
            sqa.addPar(dtPeriodStartStart);
        }

        if (dtPeriodStartEnd != null) {
            sqa.addClause("date_trunc('day',a.period_start_end) <= ?");
            sqa.addPar(dtPeriodStartEnd);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.pol_type_group_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (dtInvoiceDateStart != null) {
            sqa.addClause("date_trunc('day',a.invoice_date) >= ?");
            sqa.addPar(dtInvoiceDateStart);
        }

        if (dtInvoiceDateEnd != null) {
            sqa.addClause("date_trunc('day',a.invoice_date) <= ?");
            sqa.addPar(dtInvoiceDateEnd);
        }

        if (Tools.isYes(stSOAReportID)) {
            sqa.addClause("a.treaty_type in ('SPL','QS')");
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80)");
            }
        }*/

        sqa.addOrder("soa_id desc");

        sqa.addFilter(listSOA.getFilter());

        listSOA = sqa.getList(InsurancePolicySOAView.class);

        return listSOA;
    }

    public void setListSOA(DTOList listSOA) {
        this.listSOA = listSOA;
    }

    public DTOList getList() throws Exception {

        if (list == null) {
            list = new DTOList();
            list.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.*,b.description as policy_desc");

        sqa.addQuery(" from ins_gl_closing a "
                + "left join ins_policy_types b on a.pol_type_id = b.pol_type_id ");

        sqa.addClause("closing_type in ('PREMIUM_RI_OUTWARD','PREMIUM_RI_INWARD','PREMIUM_RI_INWARD_OUTWARD','PREMIUM_RI_OUTWARD_XOL','PREMIUM_RI_INWARD_FAC','PROFIT_COMMISION_INWARD')");

        if (dtPolicyDateStart != null) {
            sqa.addClause("date_trunc('day',a.policy_date_start) >= ?");
            sqa.addPar(dtPolicyDateStart);
        }

        if (dtPolicyDateEnd != null) {
            sqa.addClause("date_trunc('day',a.policy_date_end) <= ?");
            sqa.addPar(dtPolicyDateEnd);
        }

        if (dtPeriodStartStart != null) {
            sqa.addClause("date_trunc('day',a.period_start_start) >= ?");
            sqa.addPar(dtPeriodStartStart);
        }

        if (dtPeriodStartEnd != null) {
            sqa.addClause("date_trunc('day',a.period_start_end) <= ?");
            sqa.addPar(dtPeriodStartEnd);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.pol_type_group_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (dtInvoiceDateStart != null) {
            sqa.addClause("date_trunc('day',a.invoice_date) >= ?");
            sqa.addPar(dtInvoiceDateStart);
        }

        if (dtInvoiceDateEnd != null) {
            sqa.addClause("date_trunc('day',a.invoice_date) <= ?");
            sqa.addPar(dtInvoiceDateEnd);
        }

        if (Tools.isYes(stSOAReportID)) {
            sqa.addClause("a.treaty_type in ('SPL','QS')");
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80)");
            }
        }

        sqa.addOrder("closing_id desc");

        sqa.setLimit(60);

        sqa.addFilter(list.getFilter());

        list = sqa.getList(InsuranceClosingView.class);

        return list;
    }

    public void setList(DTOList list) {
        this.list = list;
    }

    public void clickCreate() throws Exception {
        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closing_form", this);

        x.createNew();

        x.show();

    }

    public void clickEdit() throws Exception {

        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closing_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.edit();

        x.show();
    }

    public void clickView() throws Exception {
        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closing_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.view();

        x.show();

    }

    public void list() {
    }

    public void refresh() {
    }

    public String getStCurrencyCode() {
        return stCurrencyCode;
    }

    public void setStCurrencyCode(String stCurrencyCode) {
        this.stCurrencyCode = stCurrencyCode;
    }

    public String getStCurrencyDesc() {
        return stCurrencyDesc;
    }

    public void setStCurrencyDesc(String stCurrencyDesc) {
        this.stCurrencyDesc = stCurrencyDesc;
    }

    public BigDecimal getDbRate() {
        return dbRate;
    }

    public void setDbRate(BigDecimal dbRate) {
        this.dbRate = dbRate;
    }

    public String getStActiveFlag() {
        return stActiveFlag;
    }

    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    public String getGlpostingid() {
        return glpostingid;
    }

    public void setGlpostingid(String glpostingid) {
        this.glpostingid = glpostingid;
    }

    /**
     * @return the branch
     */
    public String getBranch() {
        return branch;
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void chgBranch() {
    }

    public void clickReopen() throws Exception {

        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closing_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.openPosting();

        x.show();
    }

    /**
     * @return the enableReverse
     */
    public boolean isEnableReverse() {
        return enableReverse;
    }

    /**
     * @param enableReverse the enableReverse to set
     */
    public void setEnableReverse(boolean enableReverse) {
        this.enableReverse = enableReverse;
    }

    public String getStMonths() {
        return stMonths;
    }

    public void setStMonths(String stMonths) {
        this.stMonths = stMonths;
    }

    public String getStYears() {
        return stYears;
    }

    public void setStYears(String stYears) {
        this.stYears = stYears;
    }

    public void clickExcelBalance() throws Exception {

        if (getDtInvoiceDateStart() == null) {
            throw new RuntimeException("Tanggal Awal Pembukuan harus diisi ");
        }

        if (getDtInvoiceDateEnd() == null) {
            throw new RuntimeException("Tanggal Akhir Pembukuan harus diisi ");
        }

        final DTOList l = EXCEL_BALANCING();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        EXPORT_BALANCING();
    }

    public DTOList EXCEL_BALANCING() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String sql = " select recap_no,case when closing_id is not null then 'CLOSING' else 'NON CLOSING' end as data_from "
                + "  ,round(coalesce(x.premi_reins_total,0),2) as premi_reins_total,round(debit-credit,2) as jumlah_jurnal, "
                + "  coalesce(round(x.premi_reins_total,2) - round(debit-credit,2),0) as selisih "
                + "   from ( "
                + "    select recap_no,substr(b.accountno,1,2) as akun,(select y.premi_reins_total from ins_gl_closing y "
                + "  where y.no_surat_hutang = a.recap_no) as premi_reins_total,sum(c.comm_reins_total) as comm_reins_total,sum(a.debit) as debit,sum(a.credit) as credit "
                + "    from gl_je_detail a "
                + "     inner join gl_accounts b on a.accountid = b.account_id "
                + "    left join ins_gl_closing c on a.recap_no = c.no_surat_hutang "
                + "    where date_trunc('day',applydate) >= '" + getDtInvoiceDateStart() + "'"
                + "     and date_trunc('day',applydate) <= '" + getDtInvoiceDateEnd() + "'"
                + "    and substr(a.trx_no,1,1) = 'I'  "
                + "     and b.accountno like '63%' "
                + "     group by recap_no,substr(b.accountno,1,2) "
                + "    ) x "
                + "    left join ins_gl_closing z on x.recap_no = z.no_surat_hutang "
                + "    order by recap_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_BALANCING() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("CEK BALANCE");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("NO REKAP");
            row1.createCell(1).setCellValue("ASAL DATA");
            row1.createCell(2).setCellValue("PREMI R/I");
            row1.createCell(3).setCellValue("JUMLAH JURNAL");
            row1.createCell(4).setCellValue("SELISIH");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("recap_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("data_from"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("premi_reins_total").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("jumlah_jurnal").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("selisih").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=cek_balance_reas.xlsx;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public void clickExcelMigrasi() throws Exception {

        //final DTOList l = EXCEL_MIGRATION();
        EXCEL_MIGRATION();

        //SessionManager.getInstance().getRequest().setAttribute("RPT",l);

        //EXPORT_MIGRATION();
    }

    public void EXCEL_MIGRATION() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.trx_id,a.trx_no as bukti,b.noper,b.accountno,a.accountid,substr(b.accountno,14,2) as koda,"
                + "date_trunc('day',a.create_date) as tgentry,date_trunc('day',a.applydate) as tanggal,a.description as uraian2,"
                + "b.description as keterangan,getpremi(debit=0,credit,debit) as nilai,getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs ");

        sqa.addQuery(" from gl_je_detail a "
                + " left join gl_accounts b on b.account_id = a.accountid ");

        sqa.addClause(" a.flag = 'Y' ");
        sqa.addClause(" substr(b.accountno,1,3) in ('122','121','133','489','431','432','433','441','442','443','169') ");

        if (branch != null) {
            //sqa.addClause(" substr(a.hdr_accountno,14,2) = ? ");
            //sqa.addPar(branch);
            sqa.addClause(" substr(a.hdr_accountno,14,2) = '" + branch + "'");
            //sqa.addPar(branch);
        }

        if (stMonths != null) {
            //sqa.addClause(" substr(a.applydate::text,6,2) = ? ");
            //sqa.addPar(stMonths);
            sqa.addClause(" substr(a.applydate::text,6,2) = '" + stMonths + "'");
            //sqa.addPar(stMonths);
        }

        if (stYears != null) {
            //sqa.addClause(" a.fiscal_year = ? ");
            //sqa.addPar(stYears);

            sqa.addClause(" a.fiscal_year = '" + stYears + "'");
            //sqa.addPar(stYears);
        }

        String sql = " select bukti,row_number() over(partition by a.bukti order by a.bukti,a.noper) AS nomor,noper,accountno,accountid,"
                + " koda,tgentry,tanggal,geturaian(uraian) as uraian,keterangan,nilai,kode,kodval,kurs "
                + " from ( select bukti,noper,accountno,accountid,koda,tgentry,tanggal,"
                + " (case when (select b.description from ar_receipt b where b.receipt_no = a.bukti) is null then uraian2 else (select b.description from ar_receipt b where b.receipt_no = a.bukti) end) as uraian,"
                + " keterangan,sum(nilai) as nilai,kode,kodval,kurs from ( " + sqa.getSQL()
                + " order by a.trx_id,a.trx_no,b.accountno ) a group by bukti,noper,accountno,accountid,"
                + " koda,tgentry,tanggal,uraian,kode,kodval,kurs,keterangan "
                + " union all "
                + " select a.trx_no as bukti,b.noper,b.accountno,a.accountid,substr(b.accountno,14,2) as koda,"
                + " date_trunc('day',a.create_date) as tgentry,date_trunc('day',a.applydate) as tanggal,a.description as uraian,"
                + " b.description as keterangan,getpremi(a.debit=0,a.credit,a.debit) as nilai,getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs "
                + " from gl_je_detail a "
                + " left join gl_accounts b on b.account_id = a.accountid "
                + " where a.flag = 'Y' and substr(b.accountno,1,3) not in ('122','121','133','489','431','432','433','441','442','443','169') ";

        if (getBranch() != null) {
            //sql = sql + " and substr(a.hdr_accountno,14,2) = ? ";
            //sqa.addPar(branch);

            sql = sql + " and substr(a.hdr_accountno,14,2) = '" + branch + "'";
            //sqa.addPar(branch);
        }

        if (getStMonths() != null) {
            //sql = sql + " and substr(a.applydate::text,6,2) = ? ";
            //sqa.addPar(stMonths);

            sql = sql + " and substr(a.applydate::text,6,2) = '" + stMonths + "'";
            //sqa.addPar(stMonths);
        }

        if (getStYears() != null) {
            //sql = sql + " and a.fiscal_year = ? ";
            //sqa.addPar(stYears);

            sql = sql + " and a.fiscal_year = '" + stYears + "'";
            //sqa.addPar(stYears);
        }

        sql = sql + " ) a order by a.bukti,a.noper ";

        SQLUtil S = new SQLUtil();

        String nama_file = "jurnal_migrasi_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        //File file = new File("//192.168.1.16//exportdb/csv/"+nama_file);
        File file = new File("//192.168.250.52//exportdb/csv/" + nama_file);
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

        //final DTOList l = new DTOList();

//        final DTOList l = ListUtil.getDTOListFromQuery(
//                sql,
//                sqa.getPar(),
//                HashDTO.class
//                );

        //SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        //return l;

    }

    public void EXPORT_MIGRATION() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("bukti");
            row1.createCell(1).setCellValue("nomor");
            row1.createCell(2).setCellValue("noper");
            row1.createCell(3).setCellValue("accountno");
            row1.createCell(4).setCellValue("accountid");
            row1.createCell(5).setCellValue("koda");
            row1.createCell(6).setCellValue("tgentry");
            row1.createCell(7).setCellValue("tanggal");
            row1.createCell(8).setCellValue("uraian");
            row1.createCell(9).setCellValue("keterangan");
            row1.createCell(10).setCellValue("nilai");
            row1.createCell(11).setCellValue("kode");
            row1.createCell(12).setCellValue("kodval");
            row1.createCell(13).setCellValue("kurs");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("bukti"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("nomor").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("noper"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("accountno"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("accountid").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("koda"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("tgentry"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("tanggal"));
            row.createCell(8).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("uraian")));
            row.createCell(9).setCellValue(LanguageManager.getInstance().translate(h.getFieldValueByFieldNameST("keterangan")));
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("nilai").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("kode"));
            row.createCell(12).setCellValue("IDR");
            row.createCell(13).setCellValue("1");
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=titipan.xlsx;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList CEK_BALANCEREKENINGAK() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.accountno,sum(a.debit) as debit,sum(a.credit) as credit ");

        sqa.addQuery(" from gl_je_detail a "
                + " inner join gl_accounts b on a.accountid = b.account_id ");
        sqa.addClause(" a.flag = 'Y' ");

        if (branch != null) {
            sqa.addClause(" substr(b.accountno,14,2) = ? ");
            sqa.addPar(branch);
        }

        if (stMonths != null) {
            sqa.addClause(" substr(a.applydate::text,6,2) = ? ");
            sqa.addPar(stMonths);
        }

        if (stYears != null) {
            sqa.addClause(" a.fiscal_year = ? ");
            sqa.addPar(stYears);
        }

        if (branch != null) {
            sqa.addClause("b.accountno like '210000000001%'");
        } else {
            sqa.addClause("b.accountno like '21%'");
        }

        final String sql = "select *,(debit-credit) as selisih from ( " + sqa.getSQL()
                + " group by b.accountno ) a where debit <> credit ";

        final DTOList k = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        //SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return k;

    }

    public DTOList EXCEL_REKENINGAK() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("  a.trx_no,b.accountno,a.description,substr(a.trx_no,10,5) as gl_code, a.applydate,a.debit,a.credit, "
                + " (select description "
                + " from gl_accounts x where x.accountno = hdr_accountno "
                + " ) as bank_account");

        sqa.addQuery(" from gl_je_detail a "
                + " inner join gl_accounts b on a.accountid = b.account_id ");
        sqa.addClause(" a.flag = 'Y' ");

        if (branch != null) {
            sqa.addClause(" substr(b.accountno,14,2) = ? ");
            sqa.addPar(branch);
        }

        if (stMonths != null) {
            sqa.addClause(" substr(a.applydate::text,6,2) = ? ");
            sqa.addPar(stMonths);
        }

        if (stYears != null) {
            sqa.addClause(" a.fiscal_year = ? ");
            sqa.addPar(stYears);
        }

        if (branch != null) {
            sqa.addClause("b.accountno like '210000000001%'");
        } else {
            sqa.addClause("b.accountno like '21%'");
        }

        sqa.addOrder("trx_no,applydate");

        final DTOList n = ListUtil.getDTOListFromQuery(
                sqa.getSQL(),
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT2", n);

        return n;

    }

    /**
     * @return the dtPolicyDateStart
     */
    public Date getDtPolicyDateStart() {
        return dtPolicyDateStart;
    }

    /**
     * @param dtPolicyDateStart the dtPolicyDateStart to set
     */
    public void setDtPolicyDateStart(Date dtPolicyDateStart) {
        this.dtPolicyDateStart = dtPolicyDateStart;
    }

    /**
     * @return the dtPolicyDateEnd
     */
    public Date getDtPolicyDateEnd() {
        return dtPolicyDateEnd;
    }

    /**
     * @param dtPolicyDateEnd the dtPolicyDateEnd to set
     */
    public void setDtPolicyDateEnd(Date dtPolicyDateEnd) {
        this.dtPolicyDateEnd = dtPolicyDateEnd;
    }

    /**
     * @return the dtPeriodStartStart
     */
    public Date getDtPeriodStartStart() {
        return dtPeriodStartStart;
    }

    /**
     * @param dtPeriodStartStart the dtPeriodStartStart to set
     */
    public void setDtPeriodStartStart(Date dtPeriodStartStart) {
        this.dtPeriodStartStart = dtPeriodStartStart;
    }

    /**
     * @return the dtPeriodStartEnd
     */
    public Date getDtPeriodStartEnd() {
        return dtPeriodStartEnd;
    }

    /**
     * @param dtPeriodStartEnd the dtPeriodStartEnd to set
     */
    public void setDtPeriodStartEnd(Date dtPeriodStartEnd) {
        this.dtPeriodStartEnd = dtPeriodStartEnd;
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
     * @return the dtInvoiceDateStart
     */
    public Date getDtInvoiceDateStart() {
        return dtInvoiceDateStart;
    }

    /**
     * @param dtInvoiceDateStart the dtInvoiceDateStart to set
     */
    public void setDtInvoiceDateStart(Date dtInvoiceDateStart) {
        this.dtInvoiceDateStart = dtInvoiceDateStart;
    }

    /**
     * @return the dtInvoiceDateEnd
     */
    public Date getDtInvoiceDateEnd() {
        return dtInvoiceDateEnd;
    }

    /**
     * @param dtInvoiceDateEnd the dtInvoiceDateEnd to set
     */
    public void setDtInvoiceDateEnd(Date dtInvoiceDateEnd) {
        this.dtInvoiceDateEnd = dtInvoiceDateEnd;
    }

    public void clickReverse() throws Exception {

        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closing_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.reverse();

        x.show();
    }

    /**
     * @return the enableApproveClosing
     */
    public boolean isEnableApproveClosing() {
        return enableApproveClosing;
    }

    /**
     * @param enableApproveClosing the enableApproveClosing to set
     */
    public void setEnableApproveClosing(boolean enableApproveClosing) {
        this.enableApproveClosing = enableApproveClosing;
    }

    /*
    public void exportCSV() throws Exception{

    SQLAssembler sqa = new SQLAssembler();

    sqa.addSelect(" a.trx_id,a.trx_no as bukti,b.noper,b.accountno,a.accountid,substr(b.accountno,14,2) as koda," +
    "date_trunc('day',a.create_date) as tglentry,date_trunc('day',a.applydate) as tanggal," +
    "a.description as uraian,b.description as keterangan,getpremi(debit=0,credit,debit) as nilai," +
    "getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs ");

    sqa.addQuery(" from gl_je_detail a " +
    " left join gl_accounts b on b.account_id = a.accountid " +
    " left join ar_receipt c on c.ar_receipt_id = a.ref_trx_no::bigint " );

    sqa.addClause(" a.flag = 'Y' ");

    if(branch!=null) {
    sqa.addClause(" substr(a.hdr_accountno,14,2) = ? ");
    sqa.addPar(branch);
    }

    if(stMonths!=null) {
    sqa.addClause(" substr(a.applydate::text,6,2) = ? ");
    sqa.addPar(stMonths);
    }

    if(stYears!=null) {
    sqa.addClause(" a.fiscal_year = ? ");
    sqa.addPar(stYears);
    }


    final String sql = "select * from gl_accounts";
    final SQLUtil S = new SQLUtil();

    PreparedStatement PS = S.setQuery(sql);

    ResultSet RS = PS.executeQuery();

    CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"), '\t');

    SessionManager.getInstance().getResponse().setContentType("text/csv");
    SessionManager.getInstance().getResponse().setHeader("Content-Disposition","attachment; filename=yourfile.csv;");
    ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

    if(RS.next()){
    writer.writeAll(RS, true);
    }

    sosStream.flush();
    sosStream.close();

    }
     */
    public DTOList getListcl() throws Exception {
        if (listcl == null) {
            listcl = new DTOList();
            listcl.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.*,b.description as policy_desc");

        sqa.addQuery(
                "from ins_gl_closing a left join ins_policy_types b on a.pol_type_id = b.pol_type_id ");

        sqa.addClause("closing_type IN ('CLAIM_RI_OUTWARD','CLAIM_RI_INWARD','CLAIM_RI_INWARD_TO_OUTWARD')");

        if (dtPolicyDateStart != null) {
            sqa.addClause("date_trunc('day',a.policy_date_start) >= ?");
            sqa.addPar(dtPolicyDateStart);
        }

        if (dtPolicyDateEnd != null) {
            sqa.addClause("date_trunc('day',a.policy_date_end) <= ?");
            sqa.addPar(dtPolicyDateEnd);
        }

        if (dtPeriodStartStart != null) {
            sqa.addClause("date_trunc('day',a.period_start_start) >= ?");
            sqa.addPar(dtPeriodStartStart);
        }

        if (dtPeriodStartEnd != null) {
            sqa.addClause("date_trunc('day',a.period_start_end) <= ?");
            sqa.addPar(dtPeriodStartEnd);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.pol_type_group_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (dtInvoiceDateStart != null) {
            sqa.addClause("date_trunc('day',a.invoice_date) >= ?");
            sqa.addPar(dtInvoiceDateStart);
        }

        if (dtInvoiceDateEnd != null) {
            sqa.addClause("date_trunc('day',a.invoice_date) <= ?");
            sqa.addPar(dtInvoiceDateEnd);
        }

        if (Tools.isYes(stSOAReportID)) {
            sqa.addClause("a.treaty_type in ('SPL','QS')");
        }

        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33)");
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80)");
            }
        }

        sqa.addOrder("closing_id desc");

        sqa.setLimit(60);

        sqa.addFilter(listcl.getFilter());

        listcl = sqa.getList(InsuranceClosingView.class);

        return listcl;
    }

    /**
     * @param listcl the listcl to set
     */
    public void setListcl(DTOList listcl) {
        this.listcl = listcl;
    }

    public void clickCreateCl() throws Exception {
        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingcl_form", this);

        x.createNew();

        x.show();

    }

    public void clickEditCl() throws Exception {

        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingcl_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.edit();

        x.show();
    }

    public void clickViewCl() throws Exception {
        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingcl_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.view();

        x.show();

    }

    public void clickReverseCl() throws Exception {

        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingcl_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.reverse();

        x.show();
    }

    public void clickExcelBalanceClaim() throws Exception {

        if (getDtInvoiceDateStart() == null) {
            throw new RuntimeException("Tanggal Awal Pembukuan harus diisi ");
        }

        if (getDtInvoiceDateEnd() == null) {
            throw new RuntimeException("Tanggal Akhir Pembukuan harus diisi ");
        }

        final DTOList l = EXCEL_BALANCING_CLAIM();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        EXPORT_BALANCING_CLAIM();
    }

    public DTOList EXCEL_BALANCING_CLAIM() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        /*
        String sql = " select recap_no,case when closing_id is not null then 'CLOSING' else 'NON CLOSING' end as data_from "
                + "  ,round(coalesce(x.claim_reins_total,0),2) as claim_reins_total,round(credit-debit,2) as jumlah_jurnal, "
                + "  coalesce(round(x.claim_reins_total,2) - round(credit-debit,2),0) as selisih "
                + "   from ( "
                + "    select recap_no,substr(b.accountno,1,2) as akun,(select coalesce(y.claim_reins_total,0) from ins_gl_closing y "
                + "  where y.no_surat_hutang = a.recap_no) as claim_reins_total,"
                + " sum(case when substr(b.accountno,1,2) = '71' then a.debit*-1 else a.debit end) as debit,"
                + " sum(case when substr(b.accountno,1,2) = '71' then a.credit*-1 else a.credit end) as credit "
                //+ "sum(a.debit) as debit,sum(a.credit) as credit "
                + "    from gl_je_detail a "
                + "     inner join gl_accounts b on a.accountid = b.account_id "
                + "    left join ins_gl_closing c on a.recap_no = c.no_surat_hutang "
                + "    where date_trunc('day',applydate) >= '" + getDtInvoiceDateStart() + "'"
                + "     and date_trunc('day',applydate) <= '" + getDtInvoiceDateEnd() + "'"
                //+ "    and substr(a.trx_no,1,1) = 'N'  "
                + "     and (b.accountno like '718%' or b.accountno like '72%') "
                + "     group by recap_no,substr(b.accountno,1,2) "
                + "    ) x "
                + "    left join ins_gl_closing z on x.recap_no = z.no_surat_hutang where x.recap_no is not null"
                + "    order by recap_no ";
        */

        String sql = "select x.no_surat_hutang,case when closing_id is not null then 'CLOSING' else 'NON CLOSING' end as data_from "+
                       " ,round(coalesce(x.claim_reins_total,0),2) as claim_reins_total,round(coalesce(credit,0)-coalesce(debit,0),2) as jumlah_jurnal,  "+
                       "  coalesce(round(x.claim_reins_total,2) - round(coalesce(credit,0)-coalesce(debit,0),2),0) as selisih  "+
                       "   from (  "+
                       "            select c.no_surat_hutang,invoice_date,(select coalesce(y.claim_reins_total,0) from ins_gl_closing y  "+
                       "          where y.no_surat_hutang = c.no_surat_hutang) as claim_reins_total, "+
                       "          ( "+
                       "                  select sum(case when substr(z.accountno,1,2) = '71' then x.debit*-1 else x.debit end)  "+
                       "                  from gl_je_detail x inner join gl_accounts z on x.accountid = z.account_id where (z.accountno like '718%' or z.accountno like '72%') and x.recap_no = c.no_surat_hutang "+
                       "          ) as debit, "+
                       "          ( "+
                       "                  select sum(case when substr(e.accountno,1,2) = '71' then d.credit*-1 else d.credit end)  "+
                       "                  from gl_je_detail d inner join gl_accounts e on d.accountid = e.account_id where (e.accountno like '718%' or e.accountno like '72%') and d.recap_no = c.no_surat_hutang "+
                       "          ) as credit "+
                       "            from ins_gl_closing c "+
                       "            where closing_type in ('CLAIM_RI_OUTWARD','CLAIM_RI_INWARD')  "+
                       "            and date_trunc('day',c.invoice_date) >= '" + getDtInvoiceDateStart() + "'"+
                       "             and date_trunc('day',c.invoice_date) <= '" + getDtInvoiceDateEnd() + "'"+
                       "    ) x  "+
                       "    left join ins_gl_closing z on x.no_surat_hutang = z.no_surat_hutang where x.no_surat_hutang is not null "+
                       "    order by no_surat_hutang";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_BALANCING_CLAIM() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("CEK BALANCE");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("NO REKAP");
            row1.createCell(1).setCellValue("ASAL DATA");
            row1.createCell(2).setCellValue("CLAIM R/I");
            row1.createCell(3).setCellValue("JUMLAH JURNAL");
            row1.createCell(4).setCellValue("SELISIH");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("no_surat_hutang"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("data_from"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("claim_reins_total").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("jumlah_jurnal").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("selisih").doubleValue());
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=cek_balance_reas.xlsx;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    /**
     * @return the printLang
     */
    public String getPrintLang() {
        return printLang;
    }

    /**
     * @param printLang the printLang to set
     */
    public void setPrintLang(String printLang) {
        this.printLang = printLang;
    }

    public DTOList getListtax() throws Exception {
        if (listtax == null) {
            listtax = new DTOList();
            listtax.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from ins_gl_closing a ");

        sqa.addClause("closing_type IN ('TAX_PPH')");

        if (dtPolicyDateStart != null) {
            sqa.addClause("date_trunc('day',a.policy_date_start) >= ?");
            sqa.addPar(dtPolicyDateStart);
        }

        if (dtPolicyDateEnd != null) {
            sqa.addClause("date_trunc('day',a.policy_date_end) <= ?");
            sqa.addPar(dtPolicyDateEnd);
        }

        if (dtInvoiceDateStart != null) {
            sqa.addClause("date_trunc('day',a.invoice_date) >= ?");
            sqa.addPar(dtInvoiceDateStart);
        }

        if (dtInvoiceDateEnd != null) {
            sqa.addClause("date_trunc('day',a.invoice_date) <= ?");
            sqa.addPar(dtInvoiceDateEnd);
        }

        sqa.addOrder("closing_id desc");

        sqa.setLimit(100);

        sqa.addFilter(listtax.getFilter());

        listtax = sqa.getList(InsuranceClosingView.class);

        return listtax;
    }

    /**
     * @param listtax the listtax to set
     */
    public void setListtax(DTOList listtax) {
        this.listtax = listtax;
    }

    public void clickCreateTax() throws Exception {
        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingtax_form", this);

        x.createNew();

        x.show();

    }

    public void clickEditTax() throws Exception {

        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingtax_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.edit();

        x.show();
    }

    public void clickViewTax() throws Exception {
        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingtax_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.view();

        x.show();

    }

    public void clickReverseTax() throws Exception {

        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingtax_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.reverse();

        x.show();
    }

    /**
     * @return the listreport
     */
    /**
     * @return the listreport
     */
    public DTOList getListreport() throws Exception {

        if (listreport == null) {
            listreport = new DTOList();
            listreport.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.*");

        sqa.addQuery(" from ins_gl_closing_report a ");

        if (stMonths != null) {
            sqa.addClause("months = ?");
            sqa.addPar(stMonths);
        }

        if (stYears != null) {
            sqa.addClause("years = ?");
            sqa.addPar(stYears);
        }

//        if (dtPolicyDateStart != null) {
//            sqa.addClause("date_trunc('day',a.policy_date_start) >= ?");
//            sqa.addPar(dtPolicyDateStart);
//        }
//
//        if (dtPolicyDateEnd != null) {
//            sqa.addClause("date_trunc('day',a.policy_date_end) <= ?");
//            sqa.addPar(dtPolicyDateEnd);
//        }
//
//        if (dtPeriodStartStart != null) {
//            sqa.addClause("date_trunc('day',a.period_start_start) >= ?");
//            sqa.addPar(dtPeriodStartStart);
//        }
//
//        if (dtPeriodStartEnd != null) {
//            sqa.addClause("date_trunc('day',a.period_start_end) <= ?");
//            sqa.addPar(dtPeriodStartEnd);
//        }

        sqa.addOrder("closing_id desc");

        sqa.addFilter(listreport.getFilter());

        listreport = sqa.getList(InsuranceClosingReportView.class);

        return listreport;
    }

    /**
     * @param listreport the listreport to set
     */
    public void setListreport(DTOList listreport) {
        this.listreport = listreport;
    }

    public void clickCreateReport() throws Exception {
        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingreport_form", this);

        x.createNewReport();

        x.show();

    }

    public void clickEditReport() throws Exception {

        if (glpostingid == null) {
            throw new RuntimeException("Pilih dulu data yang ingin diproses");
        }

        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingreport_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.editReport();

        x.show();
    }

    public void clickViewReport() throws Exception {

        if (glpostingid == null) {
            throw new RuntimeException("Pilih dulu data yang ingin diproses");
        }

        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingreport_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.viewReport();

        x.show();

    }

    public void clickPrint() {
        super.redirect("/pages/insurance/prodrpt/closingrekap_report.fop");
    }

    public SQLAssembler getSQA() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.*");

        sqa.addQuery("from ins_gl_closing_report a");

        if (stMonths != null) {
            sqa.addClause("months = ?");
            sqa.addPar(stMonths);
        }

        if (stYears != null) {
            sqa.addClause("years = ?");
            sqa.addPar(stYears);
        }

//        if (dtPolicyDateStart != null) {
//            sqa.addClause("date_trunc('day',a.policy_date_start) >= ?");
//            sqa.addPar(dtPolicyDateStart);
//        }
//
//        if (dtPolicyDateEnd != null) {
//            sqa.addClause("date_trunc('day',a.policy_date_end) <= ?");
//            sqa.addPar(dtPolicyDateEnd);
//        }

        return sqa;
    }

     /**
     * @return the listprod
     */
    public DTOList getListprod() throws Exception {
        if (listprod == null) {
            listprod = new DTOList();
            listprod.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.gl_post_id,a.posted_flag,a.years,a.months,"
                + "(select round(sum(z.premi),0) as premi "
                + "from ins_rekap_produksi z "
                + "where z.tahun = a.years and z.bulan = a.months ) as produksi,"
                + "(select round(sum(z.klaim),0) as klaim "
                + "from ins_rekap_klaim z "
                + "where z.tahun = a.years and z.bulan = a.months ) as klaim ");

        sqa.addQuery(" from ins_closing a ");

        if (stMonths != null) {
            sqa.addClause("months = ?");
            sqa.addPar(stMonths);
        }

        if (stYears != null) {
            sqa.addClause("years = ?");
            sqa.addPar(stYears);
        }

        sqa.addOrder("gl_post_id desc");
        sqa.setLimit(100);

        sqa.addFilter(listprod.getFilter());

        listprod = sqa.getList(InsurancePostingView.class);

        return listprod;
    }

    /**
     * @param listprod the listprod to set
     */
    public void setListprod(DTOList listprod) {
        this.listprod = listprod;
    }

    public void clickCreateP() throws Exception {
        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingprod_form", this);

        x.createNewP();

        x.show();

    }

    public void clickEditP() throws Exception {

        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingprod_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.editP();

        x.show();
    }

    public void clickViewP() throws Exception {
        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingprod_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.viewP();

        x.show();

    }

    public void clickReopenP() throws Exception {

        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_closingprod_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.openPostingP();

        x.show();
    }

    /**
     * @return the stPeriodFrom
     */
    public String getStPeriodFrom() {
        return stPeriodFrom;
    }

    /**
     * @param stPeriodFrom the stPeriodFrom to set
     */
    public void setStPeriodFrom(String stPeriodFrom) {
        this.stPeriodFrom = stPeriodFrom;
    }

    /**
     * @return the stPeriodTo
     */
    public String getStPeriodTo() {
        return stPeriodTo;
    }

    /**
     * @param stPeriodTo the stPeriodTo to set
     */
    public void setStPeriodTo(String stPeriodTo) {
        this.stPeriodTo = stPeriodTo;
    }

    /**
     * @return the stSOAReportID
     */
    public String getStSOAReportID() {
        return stSOAReportID;
    }

    /**
     * @param stSOAReportID the stSOAReportID to set
     */
    public void setStSOAReportID(String stSOAReportID) {
        this.stSOAReportID = stSOAReportID;
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



    public void clickPrintByJurnal() {
        super.redirect("/pages/insurance/prodrpt/jurnalSHK_report.fop");
    }

    public SQLAssembler getSQAByJurnal() throws Exception {

        if (getDtInvoiceDateStart() == null) {
            throw new RuntimeException("Tanggal Awal Pembukuan harus diisi ");
        }

        if (getDtInvoiceDateEnd() == null) {
            throw new RuntimeException("Tanggal Akhir Pembukuan harus diisi ");
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.recap_no,substr(b.accountno,1,5) as akun,sum (a.debit) as debit,sum (a.credit) as credit ");

        sqa.addQuery("from gl_je_detail a "
                + "inner join gl_accounts b on b.account_id = a.accountid ");

        sqa.addClause("substr(b.accountno,1,5) in ('63410','77220','42410')");

        String closing = null;
        closing = " select a.no_surat_hutang from ins_gl_closing a "
                + " left join ins_policy_types b on a.pol_type_id = b.pol_type_id "
                + " where closing_type in ('PREMIUM_RI_OUTWARD','CLAIM_RI_OUTWARD') ";
        if (dtInvoiceDateStart != null) {
            closing = closing + " and date_trunc('day',a.invoice_date) >= '" + dtInvoiceDateStart + "' ";
        }
        if (dtInvoiceDateEnd != null) {
            closing = closing + " and date_trunc('day',a.invoice_date) <= '" + dtInvoiceDateEnd + "' ";
        }
        if (Tools.isYes(stSOAReportID)) {
            closing = closing + " and a.treaty_type in ('SPL','QS')";
        }
        if (stPolCredit != null) {
            if (stPolCredit.equalsIgnoreCase("1")) {
                closing = closing + "and a.pol_type_id not in (21,59,31,32,33)";
            } else if (stPolCredit.equalsIgnoreCase("2")) {
                closing = closing + "and a.pol_type_id in (59,80)";
            }
        }
        closing = closing + " order by closing_id desc ";

        sqa.addClause("a.recap_no in (" + closing + ")");

        if (dtInvoiceDateStart != null) {
            sqa.addClause("date_trunc('day',a.applydate) >= ?");
            sqa.addPar(dtInvoiceDateStart);
        }

        if (dtInvoiceDateEnd != null) {
            sqa.addClause("date_trunc('day',a.applydate) <= ?");
            sqa.addPar(dtInvoiceDateEnd);
        }

        sqa.addGroup("1,2");
        sqa.addOrder("1,2");

        return sqa;
    }

    public void clickProcessSOA() throws Exception {
        InsuranceClosingForm x = new InsuranceClosingForm();

        x.setAttribute("glpostingid", glpostingid);

        x.processSOA();

    }

    public void clickViewSOA() throws Exception {
        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_soa_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.viewSOA();

        x.show();

    }

    public void clickEditSOA() throws Exception {

        InsuranceClosingForm x = (InsuranceClosingForm) super.newForm("ins_soa_form", this);

        x.setAttribute("glpostingid", glpostingid);

        x.editSOA();

        x.show();
    }
    

}

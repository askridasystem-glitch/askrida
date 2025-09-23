/***********************************************************************
 * Module:  com.webfin.gl.accounts.forms.CurrencyList
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:32 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.gl.form;

import com.crux.common.model.HashDTO;
import com.crux.lang.LanguageManager;
import com.crux.util.BDUtil;
import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.FTPUploadFile;
import com.crux.util.FileTransferDirectory;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLAssembler;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.crux.web.controller.SessionManager;
import com.webfin.gl.ejb.GLReportEngine2;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.*;
import com.webfin.insurance.model.InsurancePolicyTypeView;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class GLPostingList extends Form {

    private final static transient LogManager logger = LogManager.getInstance(GLPostingList.class);

    private DTOList list;
    private String glpostingid;
    private String stCurrencyCode;
    private String stCurrencyDesc;
    private BigDecimal dbRate;
    private String stActiveFlag;

    private String branch = SessionManager.getInstance().getSession().getStBranch();
    private boolean enableReverse = SessionManager.getInstance().getSession().hasResource("POSTING_REVERSE");
    private boolean enableFinalize = SessionManager.getInstance().getSession().hasResource("POSTING_FINALIZE");

    private String stMonths;
    private String stYears;

    private AccountView2 account;
    
    public DTOList getList() throws Exception {

        if (list==null) {
            list=new DTOList();
            list.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.*,b.description as cost_center, c.user_name,d.user_name as user_name_e,date_trunc('seconds',a.change_date)::character varying as last_changed ");

        sqa.addQuery(
                "from gl_posting a left join gl_cost_center b on a.cc_code = b.cc_code "+
                " inner join s_users c on a.create_who = c.user_id"+
                " left join s_users d on a.change_who = d.user_id"
                );

        if(branch!=null){
            sqa.addClause(" a.cc_code = ?");
            sqa.addPar(branch);
        }

        if(stMonths!=null){
            sqa.addClause(" a.months = ?");
            sqa.addPar(stMonths);
        }

        if(stYears!=null){
            sqa.addClause(" a.years = ?");
            sqa.addPar(stYears);
        }

        sqa.addOrder("years::bigint desc,months::int desc,gl_post_id desc");

        sqa.addFilter(list.getFilter());

        list = sqa.getList(GLPostingView.class);

        return list;
    }
    
    public void setList(DTOList list) {
        this.list = list;
    }
    
    public void clickCreate() throws Exception {
        GLPostingForm x = (GLPostingForm) super.newForm("gl_posting_form",this);
        
        x.createNew();
        
        x.show();
        
    }
    
    public void clickEdit() throws Exception {
        
        GLPostingForm x = (GLPostingForm) super.newForm("gl_posting_form",this);
        
        x.setAttribute("glpostingid", glpostingid);
        
        x.edit();
        
        x.show();
    }
    
    public void clickView() throws Exception {
        GLPostingForm x = (GLPostingForm) super.newForm("gl_posting_form",this);
        
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

        GLPostingForm x = (GLPostingForm) super.newForm("gl_posting_form",this);

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

        final DTOList l = EXCEL_TITIPAN();

        SessionManager.getInstance().getRequest().setAttribute("RPT",l);

        final DTOList m = CEK_BALANCEREKENINGAK();

        if(m.size()>0){
            final DTOList n = EXCEL_REKENINGAK();

            SessionManager.getInstance().getRequest().setAttribute("RPT2",n);
        }

        EXPORT_TITIPAN();
    }

    public DTOList EXCEL_TITIPAN() throws Exception {

        if(stMonths==null || stYears==null)
            throw new RuntimeException("Bulan dan Tahun wajib diisi");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.trx_no,sum(a.debit) as debit,sum(a.credit) as credit ");

        sqa.addQuery(" from gl_je_detail a "+
                    " inner join gl_accounts b on a.accountid = b.account_id ");
        sqa.addClause(" a.flag = 'Y' ");

        if(branch!=null) {
            sqa.addClause(" substr(b.accountno,14,2) = ? ");
            sqa.addPar(branch);
        }

        if(stMonths!=null) {
            //sqa.addClause(" substr(a.applydate::text,6,2) = ? ");
            //sqa.addPar(stMonths);

            sqa.addClause(" a.period_no = ? ");
            sqa.addPar(String.valueOf(Integer.parseInt(stMonths)));
        }

        if(stYears!=null) {
            sqa.addClause(" a.fiscal_year = ? ");
            sqa.addPar(stYears);
        }

        final String sql = "select *,(debit-credit) as selisih from ( " +sqa.getSQL()+
                " group by a.trx_no ) a where debit <> credit order by trx_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class
                );

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_TITIPAN()  throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("CEK BALANCE(NO BUKTI)");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i=0;i< list.size() ; i++){
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("NOBUK");
            row1.createCell(1).setCellValue("DEBIT");
            row1.createCell(2).setCellValue("CREDIT");
            row1.createCell(3).setCellValue("SELISIH");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("trx_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("debit").doubleValue());
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("credit").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("selisih").doubleValue());
        }

        //bikin sheet rek antar kantor
        final DTOList listRAK = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");

        if(listRAK!=null){
            XSSFSheet sheetRAK = wb.createSheet("JURNAL REK. ANTAR KANTOR");

            //final DTOList listRAK = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");

            for (int j=0;j< listRAK.size() ; j++){
                HashDTO h = (HashDTO) listRAK.get(j);

                //bikin header
                XSSFRow row1RAK = sheetRAK.createRow(0);

                row1RAK.createCell(0).setCellValue("TANGGAL");
                row1RAK.createCell(1).setCellValue("NO BUKTI");
                row1RAK.createCell(2).setCellValue("GL CODE");
                row1RAK.createCell(3).setCellValue("BANK");
                row1RAK.createCell(4).setCellValue("NO REKENING");
                row1RAK.createCell(5).setCellValue("KETERANGAN");
                row1RAK.createCell(6).setCellValue("DEBET");
                row1RAK.createCell(7).setCellValue("KREDIT");

                //bikin isi cell
                // a.trx_no,b.accountno,a.applydate,a.debit,a.credit
                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

                XSSFRow rowRAK = sheetRAK.createRow(j+1);
                rowRAK.createCell(0).setCellValue(h.getFieldValueByFieldNameDT("applydate"));
                rowRAK.getCell(0).setCellStyle(cellStyle);
                rowRAK.createCell(1).setCellValue(h.getFieldValueByFieldNameST("trx_no"));
                rowRAK.createCell(2).setCellValue(h.getFieldValueByFieldNameST("gl_code"));
                rowRAK.createCell(3).setCellValue(h.getFieldValueByFieldNameST("bank_account"));
                rowRAK.createCell(4).setCellValue(h.getFieldValueByFieldNameST("accountno"));
                rowRAK.createCell(5).setCellValue(h.getFieldValueByFieldNameST("description"));
                rowRAK.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("debit").doubleValue());
                rowRAK.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("credit").doubleValue());
            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition","attachment; filename=cek_balance.xlsx;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }



   public void clickExcelMigrasi() throws Exception {

        final DTOList l = EXCEL_MIGRATION();
        //EXCEL_MIGRATION();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        EXPORT_MIGRATION();
    }

    public DTOList EXCEL_MIGRATION() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String bulan = null;
        if (stMonths.startsWith("0")) {
            bulan = stMonths.substring(1, 2);
        } else {
            bulan = stMonths;
        }

        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@@@@@@ bulan : " + bulan);

        sqa.addSelect(" a.trx_id,getname(a.ref_trx = 'INV',substr(a.trx_no,1,13),a.trx_no) as bukti,b.noper,b.accountno,a.accountid,';'||substr(b.accountno,14,2) as koda,"
                + "getperiod(a.ref_trx = 'INV',date_trunc('month',a.create_date),date_trunc('day',a.create_date)) as tgentry,"
                + "date_trunc('month',a.applydate) as tanggal,b.description as keterangan,"
                + "getpremi(debit=0,credit,debit) as nilai,getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs ");

        sqa.addQuery(" from gl_je_detail a "
                + " left join gl_accounts b on b.account_id = a.accountid ");

        //sqa.addClause(" a.flag = 'Y' ");
        sqa.addClause(" (substr(b.accountno,1,3) in (select accountno from gl_accounts_summary where active_flag = 'Y') or substr(b.accountno,1,4) = '4892') ");
        //sqa.addClause(" a.trx_no in ('C161018181512700038','C161020202018700502') ");

        if (branch != null) {
            sqa.addClause(" substr(a.hdr_accountno,14,2) = ? ");
            sqa.addPar(branch);
//
//            sqa.addClause(" substr(a.hdr_accountno,14,2) = '" + branch + "'");
        }

        if (stMonths != null) {
            sqa.addClause(" a.period_no = ? ");
            sqa.addPar(bulan);

//            sqa.addClause(" substr(a.applydate::text,6,2) = '" + stMonths + "'");
        }

        if (stYears != null) {
            sqa.addClause(" a.fiscal_year = ? ");
            sqa.addPar(stYears);

//            sqa.addClause(" a.fiscal_year = '" + stYears + "'");
        }

        String sql = " select bukti,row_number() over(partition by a.bukti order by a.bukti,a.noper) AS nomor,noper,accountno,accountid,koda,tgentry,tanggal,"
                + " geturaian(uraian) as uraian,keterangan,nilai,kode,kodval,kurs from ( "
                + " select bukti,noper,accountno,accountid,koda,tgentry,tanggal,keterangan as uraian,keterangan,sum(nilai) as nilai,kode,kodval,kurs"
                + " from ( " + sqa.getSQL()
                + " order by a.trx_id,a.trx_no,b.accountno ) a group by  bukti,noper,accountno,accountid,koda,tgentry,tanggal,kode,kodval,kurs,keterangan,uraian "
                + " union all "
                + " select a.trx_no as bukti,b.noper,b.accountno,a.accountid,';'||substr(b.accountno,14,2) as koda,date_trunc('day',a.create_date) as tgentry,"
                + " date_trunc('day',a.applydate) as tanggal,"
                + " a.description as uraian,b.description as keterangan,getpremi(a.debit=0,a.credit,a.debit) as nilai,"
                + " getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs "
                + " from gl_je_detail a "
                + " left join gl_accounts b on b.account_id = a.accountid "
                + " where substr(trx_no,1,1) not in ('I','J') and (substr(b.accountno,1,3) not in (select accountno from gl_accounts_summary where active_flag = 'Y') and substr(b.accountno,1,4) <> '4892') ";
                //+ " and a.trx_no in ('C161018181512700038','C161020202018700502')";


        if (getBranch() != null) {
            sql = sql + " and substr(a.hdr_accountno,14,2) = ? ";
            sqa.addPar(branch);

//            sql = sql + " and substr(a.hdr_accountno,14,2) = '" + branch + "'";
        }

        if (getStMonths() != null) {
            sql = sql + " and a.period_no = ? ";
            sqa.addPar(bulan);

//            sql = sql + " and substr(a.applydate::text,6,2) = '" + stMonths + "'";
        }

        if (getStYears() != null) {
            sql = sql + " and a.fiscal_year = ? ";
            sqa.addPar(stYears);

//            sql = sql + " and a.fiscal_year = '" + stYears + "'";
        }

        sql = sql + " union all "
                + " select bukti,noper,accountno,accountid,koda,tgentry,tanggal,keterangan as uraian,keterangan,sum(nilai) as nilai,kode,kodval,kurs"
                + " from ( select a.trx_id,a.trx_no as bukti,"
                //+ "substr(trx_no,1,1) || substr(trx_no,2,2) || case when a.period_no < 10 then '0' || a.period_no::character varying else a.period_no::character varying end  || substr(a.fiscal_year,3,2)  as bukti,"
                + " b.noper,b.accountno,a.accountid,';'||substr(b.accountno,14,2) as koda,date_trunc('day',a.create_date) as tgentry,"
                + " date_trunc('month',a.applydate) as tanggal,b.description as keterangan,getpremi(debit=0,credit,debit) as nilai,getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs"
                + " from gl_je_detail a"
                + " left join gl_accounts b on b.account_id = a.accountid"
                + " where substr(trx_no,1,1) in ('I','J') and substr(trx_no,1,3) not in ('I13','J13') "
                + " and substr(b.accountno,1,3) not in (select accountno from gl_accounts_summary where active_flag = 'Y') ";
                //+ " and a.trx_no in ('C161018181512700038','C161020202018700502')";


        if (getBranch() != null) {
            sql = sql + " and substr(a.hdr_accountno,14,2) = ? ";
            sqa.addPar(branch);

//            sql = sql + " and substr(a.hdr_accountno,14,2) = '" + branch + "'";
        }

        if (getStMonths() != null) {
            sql = sql + " and a.period_no = ? ";
            sqa.addPar(bulan);

//            sql = sql + " and substr(a.applydate::text,6,2) = '" + stMonths + "'";
        }

        if (getStYears() != null) {
            sql = sql + " and a.fiscal_year = ? ";
            sqa.addPar(stYears);

//            sql = sql + " and a.fiscal_year = '" + stYears + "'";
        }

        sql = sql + " order by a.trx_id,a.trx_no,b.accountno"
                + " ) a group by bukti,noper,accountno,accountid,koda,tgentry,tanggal,kode,kodval,kurs,keterangan,uraian";

        sql = sql + " union all "
                + " select bukti,noper,accountno,accountid,koda,tgentry,tanggal,"
                + " uraian,keterangan,sum(nilai) as nilai,kode,kodval,kurs "
                + " from ( select a.trx_id,a.trx_no as bukti,"
                //+ "substr(trx_no,1,1) || substr(trx_no,2,2) || case when a.period_no < 10 then '0' || a.period_no::character varying else a.period_no::character varying end  || substr(a.fiscal_year,3,2)  as bukti,"
                + " b.noper,b.accountno,a.accountid,';'||substr(b.accountno,14,2) as koda,date_trunc('day',a.create_date) as tgentry,date_trunc('month',a.applydate) as tanggal,b.description as keterangan,a.description as uraian,"
                + " getpremi(debit=0,credit,debit) as nilai,getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs "
                + " from gl_je_detail a "
                + " left join gl_accounts b on b.account_id = a.accountid "
                + " where substr(trx_no,1,1) in ('I','J') and substr(trx_no,1,3) in ('I13','J13') "
                + " and substr(b.accountno,1,3) not in (select accountno from gl_accounts_summary where active_flag = 'Y') ";
                //+ " and a.trx_no in ('C161018181512700038','C161020202018700502')";


        if (getBranch() != null) {
            sql = sql + " and substr(a.hdr_accountno,14,2) = ? ";
            sqa.addPar(branch);

//            sql = sql + " and substr(a.hdr_accountno,14,2) = '" + branch + "'";
        }

        if (getStMonths() != null) {
            sql = sql + " and a.period_no = ? ";
            sqa.addPar(bulan);

//            sql = sql + " and substr(a.applydate::text,6,2) = '" + stMonths + "'";
        }

        if (getStYears() != null) {
            sql = sql + " and a.fiscal_year = ? ";
            sqa.addPar(stYears);

//            sql = sql + " and a.fiscal_year = '" + stYears + "'";
        }

        sql = sql + " order by a.trx_id,a.trx_no,b.accountno "
                + ") a group by bukti,noper,accountno,accountid,koda,tgentry,tanggal,kode,kodval,kurs,keterangan,uraian,trx_id ";

        sql = sql + " ) a order by a.bukti,a.noper ";

//        SQLUtil S = new SQLUtil();
//
//        String nama_file = "jurnal_migrasi_" + System.currentTimeMillis() + ".csv";
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
//        File file = new File("//192.168.250.52//exportdb/csv/" + nama_file);
//        int length = 0;
//        ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();
//
//        SessionManager.getInstance().getResponse().setContentType("text/csv");
//        SessionManager.getInstance().getResponse().setContentLength((int) file.length());
//
//         sets HTTP header
//        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");
//
//        int BUFSIZE = 4096;
//        byte[] byteBuffer = new byte[BUFSIZE];
//        DataInputStream in = new DataInputStream(new FileInputStream(file));
//
//         reads the file's bytes and writes them to the response stream
//        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
//            outStream.write(byteBuffer, 0, length);
//        }
//
//        in.close();
//        outStream.close();
//
//        file.delete();

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_MIGRATION() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        int page = 0;
        int baris = 0;
        //int j = 0;
        int jumlahBarisPerSheet = 100000;

        //bikin sheet
        //XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin sheet fleksibel
            if (i % jumlahBarisPerSheet == 0) {
                page = page + 1;
                XSSFSheet sheet = wb.createSheet("migrasi" + page);
                baris = 0;
            }

            //bikin header
            XSSFRow row2 = wb.getSheet("migrasi" + page).createRow(1);
            if (getStMonths() != null) {
                row2.createCell(0).setCellValue("Bulan : " + getStMonths() + " Tahun " + getStYears());
            }

            //bikin header
            //XSSFRow row1 = sheet.createRow(0);
            XSSFRow row1 = wb.getSheet("migrasi" + page).createRow(2);
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
            //XSSFRow row = sheet.createRow(i + 1);
            XSSFRow row = wb.getSheet("migrasi" + page).createRow(baris + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("bukti"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("nomor").doubleValue());
            if (h.getFieldValueByFieldNameST("noper") != null) {
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("noper"));
            }
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("accountno"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("accountid").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("koda"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameDT("tgentry"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameDT("tanggal"));
            if (h.getFieldValueByFieldNameST("uraian") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("uraian"));
            }
            if (h.getFieldValueByFieldNameST("keterangan") != null) {
                row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("keterangan"));
            }
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("nilai").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("kode"));
            row.createCell(12).setCellValue("IDR");
            row.createCell(13).setCellValue("1");

            baris = baris + 1;
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

        if(stMonths==null || stYears==null)
            throw new RuntimeException("Bulan dan Tahun wajib diisi");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.accountno,sum(a.debit) as debit,sum(a.credit) as credit ");

        sqa.addQuery(" from gl_je_detail a "+
                    " inner join gl_accounts b on a.accountid = b.account_id ");
        sqa.addClause(" a.flag = 'Y' ");

        if(branch!=null) {
            sqa.addClause(" substr(b.accountno,14,2) = ? ");
            sqa.addPar(branch);
        }

        /*
        if(stMonths!=null) {
            sqa.addClause(" substr(a.applydate::text,6,2) = ? ");
            sqa.addPar(stMonths);
        }*/

        if(stMonths!=null) {
            //sqa.addClause(" substr(a.applydate::text,6,2) = ? ");
            //sqa.addPar(stMonths);

            sqa.addClause(" a.period_no = ? ");
            sqa.addPar(String.valueOf(Integer.parseInt(stMonths)));
        }

        if(stYears!=null) {
            sqa.addClause(" a.fiscal_year = ? ");
            sqa.addPar(stYears);
        }

        if(branch!=null) {
            sqa.addClause("b.accountno like '210000000001%'");
        }else{
            sqa.addClause("b.accountno like '21%'");
        }

        final String sql = "select *,(debit-credit) as selisih from ( " +sqa.getSQL()+
                " group by b.accountno ) a where debit <> credit ";

        final DTOList k = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class
                );

        //SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return k;

    }

    public DTOList EXCEL_REKENINGAK() throws Exception {

        if(stMonths==null || stYears==null)
            throw new RuntimeException("Bulan dan Tahun wajib diisi");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("  a.trx_no,b.accountno,a.description,substr(a.trx_no,10,5) as gl_code, a.applydate,a.debit,a.credit, "+
                        " (select description "+
                        " from gl_accounts x where x.accountno = hdr_accountno and enabled = 'Y' "+
                        " ) as bank_account");

        sqa.addQuery(" from gl_je_detail a "+
                    " inner join gl_accounts b on a.accountid = b.account_id ");
        sqa.addClause(" a.flag = 'Y' ");

        if(branch!=null) {
            sqa.addClause(" substr(b.accountno,14,2) = ? ");
            sqa.addPar(branch);
        }

        /*
        if(stMonths!=null) {
            sqa.addClause(" substr(a.applydate::text,6,2) = ? ");
            sqa.addPar(stMonths);
        }*/

        if(stMonths!=null) {
            //sqa.addClause(" substr(a.applydate::text,6,2) = ? ");
            //sqa.addPar(stMonths);

            sqa.addClause(" a.period_no = ? ");
            sqa.addPar(String.valueOf(Integer.parseInt(stMonths)));
        }

        if(stYears!=null) {
            sqa.addClause(" a.fiscal_year = ? ");
            sqa.addPar(stYears);
        }

        if(branch!=null) {
            sqa.addClause("b.accountno like '210000000001%'");
        }else{
            sqa.addClause("b.accountno like '21%'");
        }

        sqa.addOrder("trx_no,applydate");

        final DTOList n = ListUtil.getDTOListFromQuery(
                sqa.getSQL(),
                sqa.getPar(),
                HashDTO.class
                );

        SessionManager.getInstance().getRequest().setAttribute("RPT2", n);

        return n;

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

    public void clickExcelMigrasiResume() throws Exception {

        EXCEL_MIGRATION_RESUME();

    }

    public void EXCEL_MIGRATION_RESUME() throws Exception {

        if(stMonths==null || stYears==null)
            throw new RuntimeException("Bulan dan Tahun wajib diisi");

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.trx_id,getname(a.ref_trx = 'INV',substr(a.trx_no,1,13),a.trx_no) as bukti,b.noper,';'||substr(b.accountno,14,2) as koda,"
                + "getperiod(a.ref_trx = 'INV',date_trunc('month',a.create_date),date_trunc('day',a.create_date)) as tgentry,"
                + "date_trunc('month',a.applydate) as tanggal,coalesce(b.hdr_description,b.description) as keterangan,"
                + "getpremi(debit=0,credit,debit) as nilai,getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs ");

        sqa.addQuery(" from gl_je_detail a "
                + " left join gl_accounts b on b.account_id = a.accountid ");

        //sqa.addClause(" a.flag = 'Y' ");
        sqa.addClause(" (substr(b.accountno,1,3) in (select accountno from gl_accounts_summary where active_flag = 'Y') or substr(b.accountno,1,4) = '4892') ");

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

        String sql = " select bukti,row_number() over(partition by a.bukti order by a.bukti,a.noper) AS nomor,noper,koda,tgentry,tanggal,"
                + " geturaian(uraian) as uraian,keterangan,nilai,kode,kodval,kurs from ( "
                + " select bukti,noper,koda,tgentry,tanggal,keterangan as uraian,keterangan,sum(nilai) as nilai,kode,kodval,kurs"
                + " from ( " + sqa.getSQL()
                + " order by a.trx_id,a.trx_no,b.accountno ) a group by  bukti,noper,koda,tgentry,tanggal,kode,kodval,kurs,keterangan,uraian "
                + " union all "
                + " select a.trx_no as bukti,b.noper,';'||substr(b.accountno,14,2) as koda,date_trunc('day',a.create_date) as tgentry,"
                + " date_trunc('day',a.applydate) as tanggal,"
                + " a.description as uraian,coalesce(b.hdr_description,b.description) as keterangan,getpremi(a.debit=0,a.credit,a.debit) as nilai,"
                + " getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs "
                + " from gl_je_detail a "
                + " left join gl_accounts b on b.account_id = a.accountid "
                + " where substr(trx_no,1,1) not in ('I','J') and (substr(b.accountno,1,3) not in (select accountno from gl_accounts_summary where active_flag = 'Y') and substr(b.accountno,1,4) <> '4892') ";

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

        sql = sql + " union all "
                + " select bukti,noper,koda,tgentry,tanggal,keterangan as uraian,keterangan,sum(nilai) as nilai,kode,kodval,kurs"
                + " from ( select a.trx_id,substr(a.trx_no,1,13)  as bukti,"
                //+ "substr(trx_no,1,1) || substr(trx_no,2,2) || case when a.period_no < 10 then '0' || a.period_no::character varying else a.period_no::character varying end  || substr(a.fiscal_year,3,2)  as bukti,"
                + " b.noper,';'||substr(b.accountno,14,2) as koda,date_trunc('day',a.create_date) as tgentry,"
                + " date_trunc('month',a.applydate) as tanggal,coalesce(b.hdr_description,b.description) as keterangan,getpremi(debit=0,credit,debit) as nilai,getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs"
                + " from gl_je_detail a"
                + " left join gl_accounts b on b.account_id = a.accountid"
                + " where substr(trx_no,1,1) in ('I','J') and substr(trx_no,1,3) not in ('I13','J13') "
                + " and substr(b.accountno,1,3) not in (select accountno from gl_accounts_summary where active_flag = 'Y')" ;

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

        sql = sql + " order by a.trx_id,a.trx_no,b.accountno"
                + " ) a group by bukti,noper,koda,tgentry,tanggal,kode,kodval,kurs,keterangan,uraian";

        sql = sql + " union all "
                + " select bukti,noper,koda,tgentry,tanggal,"
                + " uraian,keterangan,sum(nilai) as nilai,kode,kodval,kurs "
                + " from ( select a.trx_id,substr(a.trx_no,1,13)  as bukti,"
                //+ "substr(trx_no,1,1) || substr(trx_no,2,2) || case when a.period_no < 10 then '0' || a.period_no::character varying else a.period_no::character varying end  || substr(a.fiscal_year,3,2)  as bukti,"
                + " b.noper,';'||substr(b.accountno,14,2) as koda,date_trunc('day',a.create_date) as tgentry,date_trunc('month',a.applydate) as tanggal,coalesce(b.hdr_description,b.description) as keterangan,a.description as uraian,"
                + " getpremi(debit=0,credit,debit) as nilai,getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs "
                + " from gl_je_detail a "
                + " left join gl_accounts b on b.account_id = a.accountid "
                + " where substr(trx_no,1,1) in ('I','J') and substr(trx_no,1,3) in ('I13','J13') "
                + " and substr(b.accountno,1,3) not in (select accountno from gl_accounts_summary where active_flag = 'Y')" ;

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

        sql = sql + " order by a.trx_id,a.trx_no,b.accountno "
                + ") a group by bukti,noper,koda,tgentry,tanggal,kode,kodval,kurs,keterangan,uraian,trx_id ";

        sql = sql + " ) a order by a.bukti,a.noper ";

        SQLUtil S = new SQLUtil();

        String nama_file = "jurnal_migrasi_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        //File file = new File("//192.168.1.16//exportdb/csv/" + nama_file);
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

    public void clickUpdateStatusNeraca() throws Exception {

        GLPostingForm x = (GLPostingForm) super.newForm("gl_posting_form",this);

        x.setAttribute("glpostingid", glpostingid);

        x.finalNeraca();

        x.show();
    }

    /**
     * @return the enableFinalize
     */
    public boolean isEnableFinalize() {
        return enableFinalize;
    }

    /**
     * @param enableFinalize the enableFinalize to set
     */
    public void setEnableFinalize(boolean enableFinalize) {
        this.enableFinalize = enableFinalize;
    }


     public void clickUpdateNeraca() throws Exception {

        clickNeracaTotal();
        clickNeracaTotalPerBulan();
        clickNeracaTotalJenas();
        clickNeracaTotalCabang();

    }

    public void clickNeracaTotal() throws Exception {

        long lPeriodFrom = Integer.parseInt(stMonths);
        long lPeriodTo = Integer.parseInt(stMonths);
        long lYearFrom = Integer.parseInt(stYears);
//        String postingid = glpostingid;

        AccountView2 account = new AccountView2();

        final DTOList neraca = account.getGLNeraca();

        for (int i = 0; i < neraca.size(); i++) {
            GLNeracaTotalView nrc = (GLNeracaTotalView) neraca.get(i);

            GLReportEngine2 glr = new GLReportEngine2();
            String accountid = nrc.getStGLNeracaID();
            glr.setStFlag("Y");

            String sheet[] = nrc.getStReference1().split("[\\|]");
            BigDecimal neracaItem = new BigDecimal(0);
            BigDecimal neracaItem2 = new BigDecimal(0);
            BigDecimal totalItem = new BigDecimal(0);
            if (sheet.length == 1) {

                String akun1 = nrc.getStReference1();

//                String akun2 = null;
//                if (nrc.getStReference2() != null) {
//                    akun2 = nrc.getStReference2();
//                } else {
//                    akun2 = nrc.getStReference1();
//                }

                if (accountid.equalsIgnoreCase("1037")) {
                    neracaItem = BDUtil.roundUp(glr.getSummaryRangedOnePeriod("BAL|ADD=0", akun1, akun1, lPeriodTo, lYearFrom, lYearFrom));
                } else {
                    neracaItem = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", akun1, akun1, lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
                }

                if(Tools.isEqual(neracaItem, BDUtil.zero)) continue;
                getRemoteGeneralLedger().AccountNeracaItem(accountid, lPeriodFrom, lYearFrom, neracaItem, "Y");

            } else if (sheet.length > 1) {

                String status = nrc.getStKeterangan();

                BigDecimal[] t = new BigDecimal[1];
                for (int k = 0; k < sheet.length; k++) {
                    /*
                    BigDecimal neracaItem = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", sheet[k], sheet[k], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

                    int n = 0;
                    if (status.equalsIgnoreCase("ADD")) {
                    t[n] = BDUtil.add(t[n++], neracaItem);
                    } else if (status.equalsIgnoreCase("SUB")) {
                    t[n] = BDUtil.sub(t[n++], neracaItem);
                    } else {
                    t[n] = BDUtil.add(t[n++], neracaItem);
                    }*/

                    if (k == 0) {
                        neracaItem = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", sheet[0], sheet[0], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
                    }

                    if (k > 0) {

                        neracaItem2 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", sheet[k], sheet[k], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

                        int n = 0;
                        t[n] = BDUtil.add(t[n++], neracaItem2);
                    }

                }

                if (status.equalsIgnoreCase("ADD")) {
                    totalItem = BDUtil.add(neracaItem, t[0]);
                } else if (status.equalsIgnoreCase("SUB")) {
                    totalItem = BDUtil.sub(neracaItem, t[0]);
                } else {
                    totalItem = BDUtil.add(neracaItem, t[0]);
                }

                if(Tools.isEqual(totalItem, BDUtil.zero)) continue;
                getRemoteGeneralLedger().AccountNeracaItem(accountid, lPeriodFrom, lYearFrom, totalItem, "Y");

            }

        }
    }

    public void clickNeracaTotalPerBulan() throws Exception {

        long lPeriodFrom = Integer.parseInt(stMonths);
        long lPeriodTo = Integer.parseInt(stMonths);
        long lYearFrom = Integer.parseInt(stYears);
//        String postingid = glpostingid;

        AccountView2 account = new AccountView2();

        final DTOList neraca = account.getGLNeraca();

        for (int i = 0; i < neraca.size(); i++) {
            GLNeracaTotalView nrc = (GLNeracaTotalView) neraca.get(i);

            GLReportEngine2 glr = new GLReportEngine2();
            String accountid = nrc.getStGLNeracaID();
            glr.setStFlag("Y");

            String sheet[] = nrc.getStReference1().split("[\\|]");
            if (sheet.length == 1) {

                String akun1 = nrc.getStReference1();
//                String akun2 = null;
//                if (nrc.getStReference2() != null) {
//                    akun2 = nrc.getStReference2();
//                } else {
//                    akun2 = nrc.getStReference1();
//                }

                BigDecimal neracaItem = BDUtil.roundUp(glr.getSummaryRangedOnePeriod("BAL|ADD=0", akun1, akun1, lPeriodTo, lYearFrom, lYearFrom));

                if(Tools.isEqual(neracaItem, BDUtil.zero)) continue;
                getRemoteGeneralLedger().AccountNeracaItemPerBulan(accountid, lPeriodFrom, lYearFrom, neracaItem, "Y");

            } else if (sheet.length > 1) {

                String status = nrc.getStKeterangan();

                BigDecimal[] t = new BigDecimal[1];
                BigDecimal neracaItem = new BigDecimal(0);
                BigDecimal neracaItem2 = new BigDecimal(0);
                BigDecimal totalItem = new BigDecimal(0);
                for (int k = 0; k < sheet.length; k++) {


                    if (k == 0) {
                        neracaItem = BDUtil.roundUp(glr.getSummaryRangedOnePeriod("BAL|ADD=0", sheet[0], sheet[0], lPeriodTo, lYearFrom, lYearFrom));
                    }

                    if (k > 0) {

                        neracaItem2 = BDUtil.roundUp(glr.getSummaryRangedOnePeriod("BAL|ADD=0", sheet[k], sheet[k], lPeriodTo, lYearFrom, lYearFrom));

                        int n = 0;
                        t[n] = BDUtil.add(t[n++], neracaItem2);
                    }

                }

                if (status.equalsIgnoreCase("ADD")) {
                    totalItem = BDUtil.add(neracaItem, t[0]);
                } else if (status.equalsIgnoreCase("SUB")) {
                    totalItem = BDUtil.sub(neracaItem, t[0]);
                } else {
                    totalItem = BDUtil.add(neracaItem, t[0]);
                }

                if(Tools.isEqual(totalItem, BDUtil.zero)) continue;
                getRemoteGeneralLedger().AccountNeracaItemPerBulan(accountid, lPeriodFrom, lYearFrom, totalItem, "Y");

            }

        }
    }

    public void clickNeracaTotalJenas() throws Exception {

        long lPeriodFrom = Integer.parseInt(stMonths);
        long lPeriodTo = Integer.parseInt(stMonths);
        long lYearFrom = Integer.parseInt(stYears);
//        String postingid = glpostingid;

        AccountView2 account = new AccountView2();

        final DTOList neraca = account.getGLNeraca();

        for (int i = 0; i < neraca.size(); i++) {
            GLNeracaTotalView nrc = (GLNeracaTotalView) neraca.get(i);

            GLReportEngine2 glr = new GLReportEngine2();
            String accountid = nrc.getStGLNeracaID();
//                String akun1 = nrc.getStReference1();
            boolean isJenas = Tools.isYes(nrc.getStKeterangan());

            String sheet[] = nrc.getStReference1().split("[\\|]");
            if (sheet.length == 1) {

                BigDecimal neracaItem = new BigDecimal(0);

                if (isJenas) {
                    final DTOList jenas = getPolicyType();
                    for (int j = 0; j < jenas.size(); j++) {
                        InsurancePolicyTypeView pol = (InsurancePolicyTypeView) jenas.get(j);

                        neracaItem = BDUtil.roundUp(glr.getSummaryRangedWithPolType("BAL|ADD=0", sheet[0], sheet[0], pol.getStPolicyTypeID(), lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

                        if(Tools.isEqual(neracaItem, BDUtil.zero)) continue;
                        getRemoteGeneralLedger().AccountNeracaItemJenas(accountid, lPeriodFrom, lYearFrom, neracaItem, "Y", pol.getStPolicyTypeID());
                    }
                } else {
                    neracaItem = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", sheet[0], sheet[0], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

                    if(Tools.isEqual(neracaItem, BDUtil.zero)) continue;
                    getRemoteGeneralLedger().AccountNeracaItemJenas(accountid, lPeriodFrom, lYearFrom, neracaItem, "Y", null);
                }

            } else if (sheet.length > 1) {
                BigDecimal neracaItem = new BigDecimal(0);

                if (isJenas) {
                    final DTOList jenas = getPolicyType();
                    for (int j = 0; j < jenas.size(); j++) {
                        InsurancePolicyTypeView pol = (InsurancePolicyTypeView) jenas.get(j);

                        BigDecimal[] t = new BigDecimal[1];
                        for (int k = 0; k < sheet.length; k++) {
                            neracaItem = BDUtil.roundUp(glr.getSummaryRangedWithPolType("BAL|ADD=0", sheet[k], sheet[k], pol.getStPolicyTypeID(), lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
                            logger.logDebug("!!!!!!!!!!!!!!!!!!! : " + sheet[k] + " - " + neracaItem + " - " + pol.getStPolicyTypeID());

                            int n = 0;
                            t[n] = BDUtil.add(t[n++], neracaItem);
                        }
                        logger.logDebug("@@@@@@@@@@@@@@@@@@@ : " + pol.getStPolicyTypeID() + " - " + t[0]);

                        if(Tools.isEqual(t[0], BDUtil.zero)) continue;
                        getRemoteGeneralLedger().AccountNeracaItemJenas(accountid, lPeriodFrom, lYearFrom, t[0], "Y", pol.getStPolicyTypeID());
                    }
                } else {
                    BigDecimal[] t = new BigDecimal[1];
                    for (int k = 0; k < sheet.length; k++) {

                        neracaItem = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", sheet[k], sheet[k], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

                        int n = 0;
                        t[n] = BDUtil.add(t[n++], neracaItem);
                    }
                    if(Tools.isEqual(t[0], BDUtil.zero)) continue;
                    getRemoteGeneralLedger().AccountNeracaItemJenas(accountid, lPeriodFrom, lYearFrom, t[0], "Y", null);
                }
            }
        }
    }

    public DTOList getPolicyType() {
        try {
            return ListUtil.getDTOListFromQuery(
                    "select * from ins_policy_types order by pol_type_id",
                    InsurancePolicyTypeView.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void clickNeracaTotalCabang() throws Exception {

        long lPeriodFrom = Integer.parseInt(stMonths);
        long lPeriodTo = Integer.parseInt(stMonths);
        long lYearFrom = Integer.parseInt(stYears);
        //String postingid = posting.getStGLPostingID();

        AccountView2 account = new AccountView2();

        final DTOList neraca = account.getGLNeraca();

        for (int i = 0; i < neraca.size(); i++) {
            GLNeracaTotalView nrc = (GLNeracaTotalView) neraca.get(i);

            GLReportEngine2 glr = new GLReportEngine2();
            String accountid = nrc.getStGLNeracaID();
//            boolean isJenas = Tools.isYes(nrc.getStKeterangan());

            String sheet[] = nrc.getStReference1().split("[\\|]");
            if (sheet.length == 1) {

                BigDecimal neracaItem = new BigDecimal(0);

//                if (isJenas) {
                final DTOList jenas = getCostCenterCode();
                for (int j = 0; j < jenas.size(); j++) {
                    GLCostCenterView pol = (GLCostCenterView) jenas.get(j);

                    neracaItem = BDUtil.roundUp(glr.getSummaryRangedBranch2("BAL|ADD=0", sheet[0], sheet[0], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom, pol.getStCostCenterCode()));

                    if(Tools.isEqual(neracaItem, BDUtil.zero)) continue;
                    getRemoteGeneralLedger().AccountNeracaItemCabang(accountid, lPeriodFrom, lYearFrom, neracaItem, "Y", pol.getStCostCenterCode());
                }
//                } else {
//                    neracaItem = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", sheet[0], sheet[0], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
//                    getRemoteGeneralLedger().AccountNeracaItemJenas(accountid, lPeriodFrom, lYearFrom, neracaItem, "Y", null);
//                }

            } else if (sheet.length > 1) {
                BigDecimal neracaItem = new BigDecimal(0);

//                if (isJenas) {
                final DTOList jenas = getCostCenterCode();
                for (int j = 0; j < jenas.size(); j++) {
                    GLCostCenterView pol = (GLCostCenterView) jenas.get(j);

                    BigDecimal[] t = new BigDecimal[1];
                    for (int k = 0; k < sheet.length; k++) {
                        neracaItem = BDUtil.roundUp(glr.getSummaryRangedBranch2("BAL|ADD=0", sheet[k], sheet[k], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom, pol.getStCostCenterCode()));
                        logger.logDebug("!!!!!!!!!!!!!!!!!!! : " + sheet[k] + " - " + neracaItem + " - " + pol.getStCostCenterCode());

                        int n = 0;
                        t[n] = BDUtil.add(t[n++], neracaItem);
                    }

                    logger.logDebug("@@@@@@@@@@@@@@@@@@@ : " + pol.getStCostCenterCode() + " - " + t[0]);

                    if(Tools.isEqual(neracaItem, BDUtil.zero)) continue;
                    getRemoteGeneralLedger().AccountNeracaItemCabang(accountid, lPeriodFrom, lYearFrom, t[0], "Y", pol.getStCostCenterCode());
                }
//                } else {
//                    BigDecimal[] t = new BigDecimal[1];
//                    for (int k = 0; k < sheet.length; k++) {
//
//                        neracaItem = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", sheet[k], sheet[k], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
//
//                        int n = 0;
//                        t[n] = BDUtil.add(t[n++], neracaItem);
//                    }
//                    getRemoteGeneralLedger().AccountNeracaItemJenas(accountid, lPeriodFrom, lYearFrom, t[0], "Y", null);
//                }
            }
        }
    }

    public DTOList getCostCenterCode() {
        try {
            return ListUtil.getDTOListFromQuery(
                    "select * from gl_cost_center order by cc_code",
                    GLCostCenterView.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    public void clickNeraca() throws Exception {

        account = new AccountView2();

        final DTOList cabang = account.getCabang();

        for (int i = 0; i < cabang.size(); i++) {
            GLCostCenterView cab = (GLCostCenterView) cabang.get(i);

            calculateLabaBersih(cab.getStCostCenterCode());

        }

        updateLabaBersih00();
    }

    public void calculateLabaBersih(String cccode) throws Exception {

        long lPeriodFrom = Integer.parseInt(stMonths);
        long lPeriodTo = Integer.parseInt(stMonths);
        long lYearFrom = Integer.parseInt(stYears);

        GLReportEngine2 glr = new GLReportEngine2();

        glr.setBranch(cccode);

        BigDecimal premi_bruto = glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal premi_reas = glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal premi_kenaikan = glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

        BigDecimal klaim_bruto = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal klaim_reas = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal kenaikan_klaim = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
        jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

        BigDecimal beban_komisi_netto = glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal beban_und_lain = glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
        jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

        BigDecimal investasi = glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal beban_usaha = glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);

//        BigDecimal pajakPenghasilan = glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
//        BigDecimal pajakPenghasilanTangguhan = glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
//        BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);
//
//        BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
//        BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
//        laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
//        BigDecimal penghasilanBeban = BDUtil.add(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)), glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom)));
//
//        BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);

        BigDecimal pajakPenghasilan = glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal pajakPenghasilanTangguhan = glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);

        BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
        BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
        laba_usaha = BDUtil.add(laba_usaha, beban_usaha);

        BigDecimal penghasilanBeban69 = glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal penghasilanBeban89 = glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal penghasilanBeban = BDUtil.add(penghasilanBeban69, penghasilanBeban89);

        BigDecimal labaSebelumPajak = BDUtil.negate(BDUtil.add(laba_usaha, penghasilanBeban));
        BigDecimal acc_laba_bersih = BDUtil.sub(labaSebelumPajak, pajakPenghasilan);
        acc_laba_bersih = BDUtil.add(acc_laba_bersih, BDUtil.negate(pajakPenghasilanTangguhan));

        updateLabaBersih(BDUtil.negate(acc_laba_bersih), lPeriodTo, lYearFrom, cccode);
    }

    public BigDecimal updateLabaBersih(BigDecimal labaBersih, long months, long years, String branchLaba) throws Exception {
        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.account_id::text ");

        sqa.addQuery(" from gl_accounts a ");

        sqa.addClause(" a.accountno like '51611%'  ");
        sqa.addClause(" a.acctype is null ");
        sqa.addClause(" a.enabled = 'Y' ");

        sqa.addClause(" a.cc_code = ? ");
        sqa.addPar(branchLaba);

        final DTOList l = ListUtil.getDTOListFromQuery(
                sqa.getSQL(),
                sqa.getPar(),
                HashDTO.class);

        HashDTO h = (HashDTO) l.get(0);

        getRemoteGeneralLedger().AccountLabaBersih(h.getFieldValueByFieldNameST("account_id"), months, years, labaBersih, "Y");

        return labaBersih;
    }

    public void updateLabaBersih00() throws Exception {

        long lPeriodFrom = Integer.parseInt(stMonths);
        long lPeriodTo = Integer.parseInt(stMonths);
        long lYearFrom = Integer.parseInt(stYears);

        BigDecimal labaBersih00 = calculateLabaBersih00();
        BigDecimal selisih_nilai = calculateSelisihLabaBersih();

        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ : " + labaBersih00);
        logger.logDebug("##################### : " + selisih_nilai);

        labaBersih00 = BDUtil.add(labaBersih00, selisih_nilai);

        logger.logDebug("$$$$$$$$$$$$$$$$$$$$$ : " + labaBersih00);

        getRemoteGeneralLedger().AccountLabaBersih("223379", lPeriodTo, lYearFrom, labaBersih00, "Y");

    }

    public BigDecimal calculateLabaBersih00() throws Exception {

        long lPeriodFrom = Integer.parseInt(stMonths);
        long lPeriodTo = Integer.parseInt(stMonths);
        long lYearFrom = Integer.parseInt(stYears);
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select bal" + lPeriodTo
                    + "   from gl_acct_bal2 a "
                    + "   where a.account_id = 223379 and a.idr_flag = 'Y' and a.period_year = ?");

            S.setParam(1, lYearFrom);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public BigDecimal calculateSelisihLabaBersih() throws Exception {

        long lPeriodFrom = Integer.parseInt(stMonths);
        long lPeriodTo = Integer.parseInt(stMonths);
        long lYearFrom = Integer.parseInt(stYears);

        GLReportEngine2 glr = new GLReportEngine2();

        BigDecimal premi_bruto = glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal premi_reas = glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal premi_kenaikan = glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

        BigDecimal klaim_bruto = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal klaim_reas = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal kenaikan_klaim = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
        jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

        BigDecimal beban_komisi_netto = glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal beban_und_lain = glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
        jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

        BigDecimal investasi = glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal beban_usaha = glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);

        BigDecimal pajakPenghasilan = glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal pajakPenghasilanTangguhan = glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);

        BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
        BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
        laba_usaha = BDUtil.add(laba_usaha, beban_usaha);

        BigDecimal penghasilanBeban69 = glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal penghasilanBeban89 = glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom);
        BigDecimal penghasilanBeban = BDUtil.add(penghasilanBeban69, penghasilanBeban89);

        BigDecimal labaSebelumPajak = BDUtil.negate(BDUtil.add(laba_usaha, penghasilanBeban));
        BigDecimal acc_laba_bersih = BDUtil.sub(labaSebelumPajak, pajakPenghasilan);
        acc_laba_bersih = BDUtil.add(acc_laba_bersih, BDUtil.negate(pajakPenghasilanTangguhan));

        BigDecimal laba_bersih = glr.getSummaryRangedOnePeriod("BAL|NEG=5", "51611", "51611", lPeriodTo, lYearFrom, lYearFrom);

        BigDecimal selisih_nilai = BDUtil.sub(BDUtil.negate(laba_bersih), acc_laba_bersih);

        return selisih_nilai;
    }

    public DTOList EXCEL_MIGRATION_TO_CSV() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String bulan = null;
        if (stMonths.startsWith("0")) {
            bulan = stMonths.substring(1, 2);
        } else {
            bulan = stMonths;
        }

        logger.logDebug("@@@@@@@@@@@@@@@@@@@@@@@@@@ bulan : " + bulan);

        sqa.addSelect(" a.trx_id,getname(a.ref_trx = 'INV',substr(a.trx_no,1,13),a.trx_no) as bukti,b.noper,b.accountno,a.accountid,';'||substr(b.accountno,14,2) as koda,"
                + "getperiod(a.ref_trx = 'INV',date_trunc('month',a.create_date),date_trunc('day',a.create_date)) as tgentry,"
                + "date_trunc('month',a.applydate) as tanggal,b.description as keterangan,"
                + "getpremi(debit=0,credit,debit) as nilai,getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs, "
                + "getsumbis(substr(a.pol_no,2,1)) as sumbis,owner_code as pemilik,user_code as pengguna ");

        sqa.addQuery(" from gl_je_detail a "
                + " left join gl_accounts b on b.account_id = a.accountid ");

        //sqa.addClause(" a.flag = 'Y' ");
        sqa.addClause(" (substr(b.accountno,1,3) in (select accountno from gl_accounts_summary where active_flag = 'Y') or substr(b.accountno,1,4) = '4892') ");
        //sqa.addClause(" a.trx_no in ('C161018181512700038','C161020202018700502') ");

        if (branch != null) {
//            sqa.addClause(" substr(a.hdr_accountno,14,2) = ? ");
//            sqa.addPar(branch);
//
            sqa.addClause(" substr(a.hdr_accountno,14,2) = '" + branch + "'");
        }

        if (stMonths != null) {
//            sqa.addClause(" a.period_no = ? ");
//            sqa.addPar(bulan);

            sqa.addClause(" a.period_no = " + bulan);
        }

        if (stYears != null) {
//            sqa.addClause(" a.fiscal_year = ? ");
//            sqa.addPar(stYears);

            sqa.addClause(" a.fiscal_year = '" + stYears + "'");
        }

        String sql = " select bukti,row_number() over(partition by a.bukti order by a.bukti,a.noper) AS nomor,noper,accountno,accountid,koda,tgentry,tanggal,"
                + " geturaian(uraian) as uraian,keterangan,nilai,kode,kodval,kurs,sumbis,pemilik,pengguna  from ( "
                + " select bukti,noper,accountno,accountid,koda,tgentry,tanggal,keterangan as uraian,keterangan,sum(nilai) as nilai,kode,kodval,kurs,sumbis,pemilik,pengguna "
                + " from ( " + sqa.getSQL()
                + " order by a.trx_id,a.trx_no,b.accountno ) a group by  bukti,noper,accountno,accountid,koda,tgentry,tanggal,kode,kodval,kurs,keterangan,uraian,sumbis,pemilik,pengguna  "
                + " union all "
                + " select a.trx_no as bukti,b.noper,b.accountno,a.accountid,';'||substr(b.accountno,14,2) as koda,date_trunc('day',a.create_date) as tgentry,"
                + " date_trunc('day',a.applydate) as tanggal,"
                + " a.description as uraian,b.description as keterangan,getpremi(a.debit=0,a.credit,a.debit) as nilai,"
                + " getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs, "
                + " getsumbis(substr(a.pol_no,2,1)) as sumbis,owner_code as pemilik,user_code as pengguna "
                + " from gl_je_detail a "
                + " left join gl_accounts b on b.account_id = a.accountid "
                + " where substr(trx_no,1,1) not in ('I','J') and (substr(b.accountno,1,3) not in (select accountno from gl_accounts_summary where active_flag = 'Y') and substr(b.accountno,1,4) <> '4892') ";
        //+ " and a.trx_no in ('C161018181512700038','C161020202018700502')";


        if (getBranch() != null) {
//            sql = sql + " and substr(a.hdr_accountno,14,2) = ? ";
//            sqa.addPar(branch);

            sql = sql + " and substr(a.hdr_accountno,14,2) = '" + branch + "'";
        }

        if (getStMonths() != null) {
//            sql = sql + " and a.period_no = ? ";
//            sqa.addPar(bulan);

            sql = sql + " and a.period_no = " + bulan;
        }

        if (getStYears() != null) {
//            sql = sql + " and a.fiscal_year = ? ";
//            sqa.addPar(stYears);

            sql = sql + " and a.fiscal_year = '" + stYears + "'";
        }

        sql = sql + " union all "
                + " select bukti,noper,accountno,accountid,koda,tgentry,tanggal,keterangan as uraian,keterangan,sum(nilai) as nilai,kode,kodval,kurs,sumbis,pemilik,pengguna "
                + " from ( select a.trx_id,a.trx_no as bukti,"
                //+ "substr(trx_no,1,1) || substr(trx_no,2,2) || case when a.period_no < 10 then '0' || a.period_no::character varying else a.period_no::character varying end  || substr(a.fiscal_year,3,2)  as bukti,"
                + " b.noper,b.accountno,a.accountid,';'||substr(b.accountno,14,2) as koda,date_trunc('day',a.create_date) as tgentry,"
                + " date_trunc('month',a.applydate) as tanggal,b.description as keterangan,getpremi(debit=0,credit,debit) as nilai,getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs,"
                + " getsumbis(substr(a.pol_no,2,1)) as sumbis,owner_code as pemilik,user_code as pengguna  "
                + " from gl_je_detail a"
                + " left join gl_accounts b on b.account_id = a.accountid"
                + " where substr(trx_no,1,1) in ('I','J') and substr(trx_no,1,3) not in ('I13','J13') "
                + " and substr(b.accountno,1,3) not in (select accountno from gl_accounts_summary where active_flag = 'Y') ";
        //+ " and a.trx_no in ('C161018181512700038','C161020202018700502')";


        if (getBranch() != null) {
//            sql = sql + " and substr(a.hdr_accountno,14,2) = ? ";
//            sqa.addPar(branch);

            sql = sql + " and substr(a.hdr_accountno,14,2) = '" + branch + "'";
        }

        if (getStMonths() != null) {
//            sql = sql + " and a.period_no = ? ";
//            sqa.addPar(bulan);

            sql = sql + " and a.period_no = " + bulan;
        }

        if (getStYears() != null) {
//            sql = sql + " and a.fiscal_year = ? ";
//            sqa.addPar(stYears);

            sql = sql + " and a.fiscal_year = '" + stYears + "'";
        }

        sql = sql + " order by a.trx_id,a.trx_no,b.accountno"
                + " ) a group by bukti,noper,accountno,accountid,koda,tgentry,tanggal,kode,kodval,kurs,keterangan,uraian,sumbis,pemilik,pengguna ";

        sql = sql + " union all "
                + " select bukti,noper,accountno,accountid,koda,tgentry,tanggal,"
                + " uraian,keterangan,sum(nilai) as nilai,kode,kodval,kurs,sumbis,pemilik,pengguna  "
                + " from ( select a.trx_id,a.trx_no as bukti,"
                //+ "substr(trx_no,1,1) || substr(trx_no,2,2) || case when a.period_no < 10 then '0' || a.period_no::character varying else a.period_no::character varying end  || substr(a.fiscal_year,3,2)  as bukti,"
                + " b.noper,b.accountno,a.accountid,';'||substr(b.accountno,14,2) as koda,date_trunc('day',a.create_date) as tgentry,date_trunc('month',a.applydate) as tanggal,b.description as keterangan,a.description as uraian,"
                + " getpremi(debit=0,credit,debit) as nilai,getname(debit=0,'K','D') as kode,a.ccy_code as kodval,a.ccy_rate as kurs, "
                + " getsumbis(substr(a.pol_no,2,1)) as sumbis,owner_code as pemilik,user_code as pengguna "
                + " from gl_je_detail a "
                + " left join gl_accounts b on b.account_id = a.accountid "
                + " where substr(trx_no,1,1) in ('I','J') and substr(trx_no,1,3) in ('I13','J13') "
                + " and substr(b.accountno,1,3) not in (select accountno from gl_accounts_summary where active_flag = 'Y') ";
        //+ " and a.trx_no in ('C161018181512700038','C161020202018700502')";


        if (getBranch() != null) {
//            sql = sql + " and substr(a.hdr_accountno,14,2) = ? ";
//            sqa.addPar(branch);

            sql = sql + " and substr(a.hdr_accountno,14,2) = '" + branch + "'";
        }

        if (getStMonths() != null) {
//            sql = sql + " and a.period_no = ? ";
//            sqa.addPar(bulan);

            sql = sql + " and a.period_no = " + bulan;
        }

        if (getStYears() != null) {
//            sql = sql + " and a.fiscal_year = ? ";
//            sqa.addPar(stYears);

            sql = sql + " and a.fiscal_year = '" + stYears + "'";
        }

        sql = sql + " order by a.trx_id,a.trx_no,b.accountno "
                + ") a group by bukti,noper,accountno,accountid,koda,tgentry,tanggal,kode,kodval,kurs,keterangan,uraian,trx_id,sumbis,pemilik,pengguna  ";

        sql = sql + " ) a order by a.bukti,a.noper ";

        SQLUtil S = new SQLUtil();

        String nama_file = "jurnal_migrasi_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/accounting/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        /*
        File file = new File("//192.168.250.52//exportdb/accounting/" + nama_file);
        int length = 0;
        ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();

        SessionManager.getInstance().getResponse().setContentType("text/csv");
        SessionManager.getInstance().getResponse().setContentLength((int) file.length());

        //sets HTTP header
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


        final DTOList l = ListUtil.getDTOListFromQuery(
        sql,
        sqa.getPar(),
        HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);
         */

        String user = "akuntansi"; //"akuntansi"; //"statistik";
        String pass = "Akunt@n$1234"; //"Akunt@n$1234"; //"St@t1sT!k234";
        String file = "P:\\accounting\\" + nama_file;//utk core 250.53
        String upload = "/laporan/" + nama_file;

        FTPUploadFile toFtp = new FTPUploadFile();
        toFtp.copyWithFTPUser(user, pass, file, upload);

        //COPY POLIS KE DIRECTORY
//        String INPUT_FILE = "P:\\statistik\\" + nama_file;//utk core 250.53
//        String COPY_FILE_TO = "W:\\statistik\\" + nama_file;
//        File source = new File(INPUT_FILE);
//        File target = new File(COPY_FILE_TO);
//
//        FileTransferDirectory transfer = new FileTransferDirectory();
//        transfer.copyWithStreams(source, target, false);
//        System.out.println(String.valueOf(INPUT_FILE));
//        System.out.println(String.valueOf(COPY_FILE_TO));
//        System.out.println("Done.");
//
//        if (source.delete()) {
//            System.out.println("Deleted the file: " + source.getName());
//        } else {
//            System.out.println("Failed to delete the file.");
//        }

        return new DTOList();

    }

    public void clickExcelMigrasiCsv() throws Exception {

        final DTOList l = EXCEL_MIGRATION_TO_CSV();

    }

}
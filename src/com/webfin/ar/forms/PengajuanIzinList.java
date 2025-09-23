/***********************************************************************
 * Module:  com.webfin.ar.forms.PengajuanIzinList
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:42 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

import com.crux.common.model.HashDTO;
import com.crux.lang.LanguageManager;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.*;
import com.crux.pool.DTOPool;
import com.webfin.ar.model.*;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Date;
import javax.servlet.ServletOutputStream;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class PengajuanIzinList extends Form {

    private String stActiveFlag = "Y";
    private String stEffectiveFlag;
    private Date dtDepoDateFrom;
    private Date dtDepoDateTo;
    private Date dtEndDate;
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("DEPO_NAVBR");
    private boolean canApprove = SessionManager.getInstance().getSession().hasResource("DEPO_APRV");
    private final static transient LogManager logger = LogManager.getInstance(PembentukanList.class);
    private DTOList izindepolist;
    private String arizinid;
    private ARInvestmentIzinDepositoView izindeposito;
    private boolean izinApprovedCab = SessionManager.getInstance().getSession().hasResource("IZIN_APPROVAL_CAB");
    private boolean izinApprovedPus = SessionManager.getInstance().getSession().hasResource("IZIN_APPROVAL_PUS");
    private boolean izinReverse = SessionManager.getInstance().getSession().hasResource("IZIN_REVERSE");
    private String printingLOV = "";
    private String stPrintForm;
    private String stFontSize;
    private DTOList izincairlist;
    private String arizincairid;
    private DTOList izinbungalist;
    private String arizinbngid;

    public void refresh() {
    }

    public Date getDtDepoDateFrom() {
        return dtDepoDateFrom;
    }

    public void setDtDepoDateFrom(Date dtDepoDateFrom) {
        this.dtDepoDateFrom = dtDepoDateFrom;
    }

    public Date getDtDepoDateTo() {
        return dtDepoDateTo;
    }

    public void setDtDepoDateTo(Date dtDepoDateTo) {
        this.dtDepoDateTo = dtDepoDateTo;
    }

    public String getStBranch() {
        return stBranch;
    }

    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }

    public boolean isCanNavigateBranch() {
        return canNavigateBranch;
    }

    public void setCanNavigateBranch(boolean canNavigateBranch) {
        this.canNavigateBranch = canNavigateBranch;
    }

    public boolean isCanApprove() {
        return canApprove;
    }

    public void setCanApprove(boolean canApprove) {
        this.canApprove = canApprove;
    }

    public String getStEffectiveFlag() {
        return stEffectiveFlag;
    }

    public void setStEffectiveFlag(String stEffectiveFlag) {
        this.stEffectiveFlag = stEffectiveFlag;
    }

    public String getStActiveFlag() {
        return stActiveFlag;
    }

    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    public Date getDtEndDate() {
        return dtEndDate;
    }

    public void setDtEndDate(Date dtEndDate) {
        this.dtEndDate = dtEndDate;
    }

    /**
     * @return the izindepolist
     */
    public DTOList getIzindepolist() throws Exception {

        if (izindepolist == null) {
            izindepolist = new DTOList();
            izindepolist.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("*");

        sqa.addQuery("from ar_izin_deposito");

        if (Tools.isYes(stActiveFlag)) {
            sqa.addClause("active_flag='Y'");
        }

        if (Tools.isYes(stEffectiveFlag)) {
            sqa.addClause("approvedpus_flag='Y' ");
        }

        if (stBranch != null) {
            sqa.addClause("cc_code = ?");
            sqa.addPar(stBranch);
        }

        sqa.addFilter(izindepolist.getFilter());

        sqa.addOrder("ar_izin_id desc");

        sqa.setLimit(100);

        izindepolist = sqa.getList(ARInvestmentIzinDepositoView.class);

        return izindepolist;
    }

    /**
     * @param izindepolist the izindepolist to set
     */
    public void setIzindepolist(DTOList izindepolist) {
        this.izindepolist = izindepolist;
    }

    private void validateIzinID() {
        if (arizinid == null) {
            throw new RuntimeException("Pilih dulu data yang ingin diproses");
        }
    }

    private void validateIzinCairID() {
        if (arizincairid == null) {
            throw new RuntimeException("Pilih dulu data yang ingin diproses");
        }
    }

    private void validateIzinBngID() {
        if (arizinbngid == null) {
            throw new RuntimeException("Pilih dulu data yang ingin diproses");
        }
    }

    public void clickCreateIzin() throws Exception {

        PengajuanIzinForm form = (PengajuanIzinForm) super.newForm("izinbentuk_form", this);

        form.createNewIzin();

        form.setEditMode(true);

        form.show();

    }

    public void clickEditIzin() throws Exception {
        validateIzinID();

        PengajuanIzinForm form = (PengajuanIzinForm) super.newForm("izinbentuk_form", this);

        form.editIzin(arizinid);

        form.setEditMode(true);

        form.show();
    }

    public void clickViewIzin() throws Exception {
        validateIzinID();

        PengajuanIzinForm form = (PengajuanIzinForm) super.newForm("izinbentuk_form", this);

        form.viewIzin(arizinid);

        form.setViewMode(true);

        form.show();

    }

    /**
     * @return the izinApprovedCab
     */
    public boolean isIzinApprovedCab() {
        return izinApprovedCab;
    }

    /**
     * @param izinApprovedCab the izinApprovedCab to set
     */
    public void setIzinApprovedCab(boolean izinApprovedCab) {
        this.izinApprovedCab = izinApprovedCab;
    }

    /**
     * @return the izinApprovedPus
     */
    public boolean isIzinApprovedPus() {
        return izinApprovedPus;
    }

    /**
     * @param izinApprovedPus the izinApprovedPus to set
     */
    public void setIzinApprovedPus(boolean izinApprovedPus) {
        this.izinApprovedPus = izinApprovedPus;
    }

    /**
     * @return the arizinid
     */
    public String getArizinid() {
        return arizinid;
    }

    /**
     * @param arizinid the arizinid to set
     */
    public void setArizinid(String arizinid) {
        this.arizinid = arizinid;
    }

    private ARInvestmentIzinDepositoView getIzinDeposito() {
        izindeposito = (ARInvestmentIzinDepositoView) DTOPool.getInstance().getDTO(ARInvestmentIzinDepositoView.class, arizinid);
        return izindeposito;
    }

    /**
     * @return the izindeposito
     */
    public ARInvestmentIzinDepositoView getIzindeposito() {
        return izindeposito;
    }

    /**
     * @param izindeposito the izindeposito to set
     */
    public void setIzindeposito(ARInvestmentIzinDepositoView izindeposito) {
        this.izindeposito = izindeposito;
    }

    public void clickAppIzinCab() throws Exception {
        validateIzinID();

        final PengajuanIzinForm form = (PengajuanIzinForm) newForm("izinbentuk_form", this);

        form.approvalCab(arizinid);

        form.setApprovalCab(true);

        form.setApprovedMode(true);

        form.show();
    }

    public void clickAppIzinPus() throws Exception {
        validateIzinID();

        final PengajuanIzinForm form = (PengajuanIzinForm) newForm("izinbentuk_form", this);

        form.approvalPus(arizinid);

        form.setApprovalPus(true);

        form.setApprovedMode(true);

        form.show();
    }

    /**
     * @return the printingLOV
     */
    public String getPrintingLOV() {
        return printingLOV;
    }

    /**
     * @param printingLOV the printingLOV to set
     */
    public void setPrintingLOV(String printingLOV) {
        this.printingLOV = printingLOV;
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
     * @return the izincairlist
     */
    public DTOList getIzincairlist() throws Exception {
        if (izincairlist == null) {
            izincairlist = new DTOList();
            izincairlist.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("*");

        sqa.addQuery("from ar_izin_pencairan");

        if (Tools.isYes(stActiveFlag)) {
            sqa.addClause("active_flag='Y'");
        }

        if (Tools.isYes(stEffectiveFlag)) {
            sqa.addClause("approvedpus_flag='Y' ");
        }

        if (stBranch != null) {
            sqa.addClause("cc_code = ?");
            sqa.addPar(stBranch);
        }

        sqa.addFilter(izincairlist.getFilter());

        sqa.addOrder("ar_izincair_id desc");

        sqa.setLimit(100);

        izincairlist = sqa.getList(ARInvestmentIzinPencairanView.class);

        return izincairlist;
    }

    /**
     * @param izincairlist the izincairlist to set
     */
    public void setIzincairlist(DTOList izincairlist) {
        this.izincairlist = izincairlist;
    }

    /**
     * @return the arizincairid
     */
    public String getArizincairid() {
        return arizincairid;
    }

    /**
     * @param arizincairid the arizincairid to set
     */
    public void setArizincairid(String arizincairid) {
        this.arizincairid = arizincairid;
    }

    public void clickCreateIzinCair() throws Exception {

        PengajuanIzinForm form = (PengajuanIzinForm) super.newForm("izincair_form", this);

        form.createNewIzinCair();

        form.setEditMode(true);

        form.show();

    }

    public void clickEditIzinCair() throws Exception {
        validateIzinCairID();

        PengajuanIzinForm form = (PengajuanIzinForm) super.newForm("izincair_form", this);

        form.editIzinCair(arizincairid);

        form.setEditMode(true);

        form.show();
    }

    public void clickViewIzinCair() throws Exception {
        validateIzinCairID();

        PengajuanIzinForm form = (PengajuanIzinForm) super.newForm("izincairnobuk_form", this);

        form.viewIzinCair(arizincairid);

        form.setViewMode(true);

        form.show();

    }

    public void clickAppIzinCairCab() throws Exception {
        validateIzinCairID();

        final PengajuanIzinForm form = (PengajuanIzinForm) newForm("izincair_form", this);

        form.approvalCabCair(arizincairid);

        form.setApprovalCab(true);

        form.setApprovedMode(true);

        form.show();
    }

    public void clickAppIzinCairPus() throws Exception {
        validateIzinCairID();

        final PengajuanIzinForm form = (PengajuanIzinForm) newForm("izincair_form", this);

        form.approvalPusCair(arizincairid);

        form.setApprovalPus(true);

        form.setApprovedMode(true);

        form.show();
    }

    /**
     * @return the izinbungalist
     */
    public DTOList getIzinbungalist() throws Exception {
        if (izinbungalist == null) {
            izinbungalist = new DTOList();
            izinbungalist.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("*");

        sqa.addQuery("from ar_izin_bunga ");

        if (Tools.isYes(stActiveFlag)) {
            sqa.addClause("active_flag='Y'");
        }

        if (Tools.isYes(stEffectiveFlag)) {
            sqa.addClause("approvedpus_flag='Y' ");
        }

        if (stBranch != null) {
            sqa.addClause("cc_code = ?");
            sqa.addPar(stBranch);
        }

        sqa.addFilter(izinbungalist.getFilter());

        sqa.addOrder("ar_izinbng_id desc");

        sqa.setLimit(100);

        izinbungalist = sqa.getList(ARInvestmentIzinBungaView.class);

        return izinbungalist;
    }

    /**
     * @param izinbungalist the izinbungalist to set
     */
    public void setIzinbungalist(DTOList izinbungalist) {
        this.izinbungalist = izinbungalist;
    }

    /**
     * @return the arizinbngid
     */
    public String getArizinbngid() {
        return arizinbngid;
    }

    /**
     * @param arizinbngid the arizinbngid to set
     */
    public void setArizinbngid(String arizinbngid) {
        this.arizinbngid = arizinbngid;
    }

    public void clickCreateIzinBng() throws Exception {

        PengajuanIzinForm form = (PengajuanIzinForm) super.newForm("izinbunga_form", this);

        form.createNewIzinBunga();

        form.setEditMode(true);

        form.show();

    }

    public void clickEditIzinBng() throws Exception {
        validateIzinBngID();

        PengajuanIzinForm form = (PengajuanIzinForm) super.newForm("izinbunga_form", this);

        form.editIzinBunga(arizinbngid);

        form.setEditMode(true);

        form.show();
    }

    public void clickViewIzinBng() throws Exception {
        validateIzinBngID();

        PengajuanIzinForm form = (PengajuanIzinForm) super.newForm("izinbunga_form", this);

        form.viewIzinBunga(arizinbngid);

        form.setViewMode(true);

        form.show();

    }

    public void clickAppIzinBngCab() throws Exception {
        validateIzinBngID();

        final PengajuanIzinForm form = (PengajuanIzinForm) newForm("izinbunga_form", this);

        form.approvalCabBunga(arizinbngid);

        form.setApprovalCab(true);

        form.setApprovedMode(true);

        form.show();
    }

    public void clickAppIzinBngPus() throws Exception {
        validateIzinBngID();

        final PengajuanIzinForm form = (PengajuanIzinForm) newForm("izinbunga_form", this);

        form.approvalPusBunga(arizinbngid);

        form.setApprovalPus(true);

        form.setApprovedMode(true);

        form.show();
    }

    public void clickAppIzinBng() throws Exception {
        validateIzinBngID();

        final PengajuanIzinForm form = (PengajuanIzinForm) newForm("izinbunga_form", this);

        form.approvalBunga(arizinbngid);

        form.setApprovalMode(true);

        form.show();
    }

    /**
     * @return the izinReverse
     */
    public boolean isIzinReverse() {
        return izinReverse;
    }

    /**
     * @param izinReverse the izinReverse to set
     */
    public void setIzinReverse(boolean izinReverse) {
        this.izinReverse = izinReverse;
    }

    public void clickReverseIzinCair() throws Exception {
        validateIzinCairID();

        PengajuanIzinForm form = (PengajuanIzinForm) super.newForm("izincair_form", this);

        form.reverseIzinCair(arizincairid);

        form.setReverseMode(true);

        form.show();
    }

    public void clickInputNobuk() throws Exception {
        validateIzinCairID();

        PengajuanIzinForm form = (PengajuanIzinForm) super.newForm("izincairnobuk_form", this);

        form.editInputNobukCair(arizincairid);

        form.show();
    }

    public void downloadKasbankCair() throws Exception {

        validateIzinCairID();

        final DTOList l = EXCEL_KASBANK_CAIR();

        EXPORT_KASBANK_CAIR();
    }

    public DTOList EXCEL_KASBANK_CAIROLD() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        /*select a.cc_code,a.months,a.years,a.mutation_date,a.ar_izincair_id,b.ar_izincairdet_id,b.ar_depo_id,b.bukti_b,b.nodefo,b.bilyet_amount
        from ar_izin_pencairan a 
        inner join ar_izin_pencairan_detail b on b.ar_izincair_id = a.ar_izincair_id
        where a.approvedcab_flag = 'Y' and a.approvedpus_flag = 'Y' 
        and b.line_type = 'INVOICE' and b.delete_flag is null and b.ar_izincair_id = 10073 
        order by b.ar_izincairdet_id*/

        /*select a.cc_code,a.months,a.years,a.mutation_date,a.ar_izincair_id,b.ar_izincairdet_id,b.ar_invoice_id,b.pencairan_ket,b.dla_no,b.nilai
        from ar_izin_pencairan a
        inner join ar_izin_pencairan_detail b on b.ar_izincair_id = a.ar_izincair_id
        where a.approvedcab_flag = 'Y' and a.approvedpus_flag = 'Y'
        and b.line_type = 'REALISASI' and b.delete_flag is null and b.ar_izincair_id = 10073
        order by b.ar_izincairdet_id         */

        sqa.addSelect(" a.cc_code,a.months,a.years,a.mutation_date,"
                + "b.ar_izincairdet_id,b.ar_depo_id,b.bukti_b,b.nodefo,b.bilyet_amount,c.account_depo  ");

        sqa.addQuery(" from ar_izin_pencairan a "
                + " inner join ar_izin_pencairan_detail b on b.ar_izincair_id = a.ar_izincair_id "
                + " inner join ar_inv_deposito c on c.ar_depo_id = b.ar_depo_id ");

        sqa.addClause("a.approvedcab_flag = 'Y' and a.approvedpus_flag = 'Y' ");
        sqa.addClause("b.line_type = 'INVOICE' and b.delete_flag is null ");

        sqa.addClause(" b.ar_izincair_id = ? ");
        sqa.addPar(arizincairid);

        String sql = sqa.getSQL() + " order by b.ar_izincairdet_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        final SQLAssembler sqa2 = new SQLAssembler();

        sqa2.addSelect(" a.cc_code,a.months,a.years,a.mutation_date,a.ar_izincair_id,"
                + "b.ar_izincairdet_id,b.ar_invoice_id,b.pencairan_ket,b.dla_no,b.nilai ");

        sqa2.addQuery(" from ar_izin_pencairan a "
                + " inner join ar_izin_pencairan_detail b on b.ar_izincair_id = a.ar_izincair_id ");

        sqa2.addClause("a.approvedcab_flag = 'Y' and a.approvedpus_flag = 'Y' and b.jns_pencairan in ('2','4','5','6','7','8') ");
        sqa2.addClause("b.line_type = 'REALISASI' and b.delete_flag is null ");

        sqa2.addClause(" b.ar_izincair_id = ? ");
        sqa2.addPar(arizincairid);

        String sql2 = sqa2.getSQL() + " order by b.ar_izincairdet_id ";

        final DTOList l2 = ListUtil.getDTOListFromQuery(
                sql2,
                sqa2.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);
        SessionManager.getInstance().getRequest().setAttribute("RPT2", l2);

        return l;
    }

    public void EXPORT_KASBANK_CAIROLD() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("Cashbank");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT2");

        String bank = null;
        HashDTO k = (HashDTO) list2.get(0);
        bank = k.getFieldValueByFieldNameST("dla_no");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("PROSES");
            row0.createCell(1).setCellValue("CABANG");
            row0.createCell(2).setCellValue("METODE");
            row0.createCell(3).setCellValue("BULAN");
            row0.createCell(4).setCellValue("TAHUN");
            row0.createCell(5).setCellValue("TANGGAL");
            row0.createCell(6).setCellValue("KETERANGAN");
            row0.createCell(7).setCellValue("NOPOLIS");
            row0.createCell(8).setCellValue("AKUN WEB");
            row0.createCell(9).setCellValue("DEBIT");
            row0.createCell(10).setCellValue("KREDIT");
            row0.createCell(11).setCellValue("AKUN BANK HEADER");

            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue("Y");
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(2).setCellValue("C");
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("months"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("years"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("mutation_date"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("nodefo"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("bukti_b"));
            row.createCell(8).setCellValue(h.getFieldValueByFieldNameST("account_depo"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("bilyet_amount").doubleValue());
            row.createCell(10).setCellValue(BDUtil.zero.doubleValue());
            row.createCell(11).setCellValue(bank);
        }

        for (int j = 0; j < list2.size(); j++) {
            HashDTO h = (HashDTO) list2.get(j);

            XSSFRow row = sheet.createRow(list.size() + 1);
            row.createCell(0).setCellValue("Y");
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(2).setCellValue("C");
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("months"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("years"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("mutation_date"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("pencairan_ket"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("pencairan_ket"));
            row.createCell(8).setCellValue("210000000000 " + h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(9).setCellValue(BDUtil.zero.doubleValue());
            row.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("nilai").doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("dla_no"));

        }

        XSSFRow rowend = sheet.createRow(list.size() + list2.size() + 1);
        rowend.createCell(0).setCellValue("END");

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=kasbankcair" + "_" + System.currentTimeMillis() + ".xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList EXCEL_KASBANK_CAIR() throws Exception {

        final SQLAssembler sqa2 = new SQLAssembler();

        sqa2.addSelect(" a.cc_code,a.months,a.years,a.mutation_date,a.ar_izincair_id,"
                + "b.ar_izincairdet_id,b.ar_invoice_id,b.ar_depo_id::text,b.pencairan_ket,b.dla_no,b.nilai ");

        sqa2.addQuery(" from ar_izin_pencairan a "
                + " inner join ar_izin_pencairan_detail b on b.ar_izincair_id = a.ar_izincair_id ");

        sqa2.addClause("a.approvedcab_flag = 'Y' and a.approvedpus_flag = 'Y' and b.jns_pencairan in ('2','4','5','6','7','8') ");
        sqa2.addClause("b.line_type = 'REALISASI' and b.delete_flag is null ");

        sqa2.addClause(" b.ar_izincair_id = ? ");
        sqa2.addPar(arizincairid);

        String sql2 = sqa2.getSQL() + " order by b.ar_izincairdet_id ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql2,
                sqa2.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_KASBANK_CAIR() throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();

        //bikin sheet
        HSSFSheet sheet = wb.createSheet("Cashbank");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        final DTOList list2 = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        logger.logDebug("####################list " + list.size());
        logger.logDebug("####################list " + list2.size());

        int kas = list.size() + 1;
        int bank = kas + list2.size();

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("PROSES");
            row0.createCell(1).setCellValue("CABANG");
            row0.createCell(2).setCellValue("METODE");
            row0.createCell(3).setCellValue("BULAN");
            row0.createCell(4).setCellValue("TAHUN");
            row0.createCell(5).setCellValue("TANGGAL");
            row0.createCell(6).setCellValue("KETERANGAN");
            row0.createCell(7).setCellValue("NOPOLIS");
            row0.createCell(8).setCellValue("AKUN WEB");
            row0.createCell(9).setCellValue("DEBIT");
            row0.createCell(10).setCellValue("KREDIT");
            row0.createCell(11).setCellValue("AKUN BANK HEADER");

            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue("Y");
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(2).setCellValue("C");
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("months"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameST("years"));
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("mutation_date"));
            row.createCell(6).setCellValue("Transfer Dana " + h.getFieldValueByFieldNameST("pencairan_ket"));
            row.createCell(7).setCellValue(h.getFieldValueByFieldNameST("ar_depo_id"));
            row.createCell(8).setCellValue("210000000000 " + h.getFieldValueByFieldNameST("cc_code"));
            row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("nilai").doubleValue());
            row.createCell(10).setCellValue(BDUtil.zero.doubleValue());
            row.createCell(11).setCellValue(h.getFieldValueByFieldNameST("dla_no"));

            HSSFRow row2 = sheet.createRow(i + kas);
            row2.createCell(0).setCellValue("Y");
            row2.createCell(1).setCellValue(h.getFieldValueByFieldNameST("cc_code"));
            row2.createCell(2).setCellValue("C");
            row2.createCell(3).setCellValue(h.getFieldValueByFieldNameST("months"));
            row2.createCell(4).setCellValue(h.getFieldValueByFieldNameST("years"));
            row2.createCell(5).setCellValue(h.getFieldValueByFieldNameDT("mutation_date"));
            row2.createCell(6).setCellValue("Transfer Dana " + h.getFieldValueByFieldNameST("pencairan_ket"));
            row2.createCell(7).setCellValue(h.getFieldValueByFieldNameST("ar_depo_id"));
            row2.createCell(8).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row2.createCell(9).setCellValue(BDUtil.zero.doubleValue());
            row2.createCell(10).setCellValue(h.getFieldValueByFieldNameBD("nilai").doubleValue());
            row2.createCell(11).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
        }

        HSSFRow rowend = sheet.createRow(bank);
        rowend.createCell(0).setCellValue("END");

//        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=kasbankcair" + "_" + System.currentTimeMillis() + ".xlsx;");
//        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        SessionManager.getInstance().getResponse().setContentType("application/vnd.ms-excel");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=kasbankcair" + "_" + System.currentTimeMillis() + ".xls;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public void clickReverseIzinBentuk() throws Exception {
        validateIzinID();

        PengajuanIzinForm form = (PengajuanIzinForm) super.newForm("izinbentuk_form", this);

        form.reverseIzinBentuk(arizinid);

        form.setReverseMode(true);

        form.show();
    }

    public void clickReverseIzinBng() throws Exception {
        validateIzinBngID();

        final PengajuanIzinForm form = (PengajuanIzinForm) newForm("izinbunga_form", this);

        form.reverseIzinBunga(arizinbngid);

        form.setReverseMode(true);

        form.show();
    }
}

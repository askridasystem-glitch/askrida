/***********************************************************************
 * Module:  com.webfin.gl.accounts.forms.CurrencyList
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:32 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.gl.form;

import com.crux.common.model.HashDTO;
import com.crux.util.Tools;
import com.crux.web.controller.SessionManager;
import com.crux.web.form.Form;
import com.crux.web.form.FormManager;
import com.crux.util.DTOList;
import com.crux.util.SQLAssembler;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.webfin.gl.model.*;
import com.webfin.gl.form.CurrencyForm;
import java.math.BigDecimal;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;
import javax.servlet.ServletOutputStream;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CurrencyList extends Form {
    
    private DTOList list;
    private String currencyid;
    private String stCurrencyCode;
    private String stCurrencyDesc;
    private BigDecimal dbRate;
    private String stActiveFlag;
    private Date dtPeriodStart;
    private Date dtPeriodEnd;
    private String showDeleted = "Y";
    
    public DTOList getList() throws Exception {
        
        if (list==null) {
            list=new DTOList();
            list.getFilter().activate();
            //list.getFilter().orderKey="ccy_hist_id";
        }
        
        SQLAssembler sqa = new SQLAssembler();
        
        sqa.addSelect("*");
        
        sqa.addQuery(
                "from gl_ccy_history"
                );
        
        if (getStCurrencyCode()!=null){
            sqa.addClause("ccy_code = ?");
            sqa.addPar(getStCurrencyCode());
        }
        
        if (getStCurrencyDesc()!=null){
            sqa.addClause("ccy_desc = ?");
            sqa.addPar(getStCurrencyDesc());
        }

        if (getDtPeriodStart()!=null){
            sqa.addClause("period_start >= ?");
            sqa.addPar(getDtPeriodStart());
        }

        if (getDtPeriodEnd()!=null){
            sqa.addClause("period_start <= ?");
            sqa.addPar(getDtPeriodEnd());
        }

        if (getShowDeleted()!=null){
            if(getShowDeleted().equalsIgnoreCase("Y")){
                sqa.addClause("coalesce(active_flag,'N') = 'Y'");
            }

        }else{
            //sqa.addClause("coalesce(active_flag,'N') = 'Y'");
        }

        sqa.addOrder(" ccy_hist_id desc");
        
        sqa.addFilter(list.getFilter());
        
        list = sqa.getList(GLCurrencyHistoryView.class);
        
        return list;
    }
    
    public void setList(DTOList list) {
        this.list = list;
    }
    
    public void clickCreate() throws Exception {
        CurrencyForm x = (CurrencyForm) super.newForm("currency_form",this);
        
        x.createNew();
        
        x.show();
        
    }
    
    public void clickEdit() throws Exception {
        
        CurrencyForm x = (CurrencyForm) super.newForm("currency_form",this);
        
        x.setAttribute("currencyid", currencyid);
        
        x.edit();
        
        x.show();
    }
    
    public void clickView() throws Exception {
        CurrencyForm x = (CurrencyForm) super.newForm("currency_form",this);
        
        x.setAttribute("currencyid", currencyid);
        
        x.view();
        
        x.show();
        
    }
    
    public void list() {
        
    }
    
    public void refresh() {
        
    }
    
    public String getCurrencyid() {
        return currencyid;
    }
    
    public void setCurrencyid(String currencyid) {
        this.currencyid = currencyid;
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

    /**
     * @return the dtPeriodStart
     */
    public Date getDtPeriodStart() {
        return dtPeriodStart;
    }

    /**
     * @param dtPeriodStart the dtPeriodStart to set
     */
    public void setDtPeriodStart(Date dtPeriodStart) {
        this.dtPeriodStart = dtPeriodStart;
    }

    /**
     * @return the dtPeriodEnd
     */
    public Date getDtPeriodEnd() {
        return dtPeriodEnd;
    }

    /**
     * @param dtPeriodEnd the dtPeriodEnd to set
     */
    public void setDtPeriodEnd(Date dtPeriodEnd) {
        this.dtPeriodEnd = dtPeriodEnd;
    }

    /**
     * @return the showDeleted
     */
    public String getShowDeleted() {
        return showDeleted;
    }

    /**
     * @param showDeleted the showDeleted to set
     */
    public void setShowDeleted(String showDeleted) {
        this.showDeleted = showDeleted;
    }

    public void clickExcel() throws Exception {

        if(getDtPeriodStart()==null) throw new RuntimeException("Tanggal Periode Awal harus diisi ");

        if(getDtPeriodEnd()==null) throw new RuntimeException("Tanggal Periode Akhir harus diisi ");

        final DTOList l = EXCEL();

        SessionManager.getInstance().getRequest().setAttribute("RPT",l);

        EXPORT();
    }

    public DTOList EXCEL() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        String sql = " select *"
                + " from gl_ccy_history"
                + " where date_trunc('day',period_start) >= '"+ getDtPeriodStart() +"'"+
                " and date_trunc('day',period_start) <= '"+ getDtPeriodEnd() +"'"+
                " and coalesce(active_flag,'N') = 'Y'";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                HashDTO.class
                );

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT()  throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("KURS");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i=0;i< list.size() ; i++){
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("KURS");
            row1.createCell(1).setCellValue("MATA UANG");
            row1.createCell(2).setCellValue("NILAI");
            row1.createCell(3).setCellValue("PERIODE AWAL");
            row1.createCell(4).setCellValue("PERIODE AKHIR");

            //bikin isi cell
            XSSFRow row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("ccy_code"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("ccy_desc"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameBD("ccy_rate").doubleValue());
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition","attachment; filename=kurs_matauang.xlsx;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

}
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
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLAssembler;
import com.crux.util.SQLUtil;
import com.crux.web.controller.SessionManager;
import com.webfin.gl.ejb.GLReportEngine2;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ClosingSettingList extends Form {

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
    
    public DTOList getList() throws Exception {

        if (list==null) {
            list=new DTOList();
            list.getFilter().activate();
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.*,b.description as cost_center");

        sqa.addQuery(
                " from closing_period a inner join gl_cost_center b on a.cc_code = b.cc_code");
 
        if(branch!=null){
            sqa.addClause(" a.cc_code = ?");
            sqa.addPar(branch);
        }

        if(stMonths!=null){
            sqa.addClause(" a.period_num = ?");
            sqa.addPar(stMonths);
        }

        if(stYears!=null){
            sqa.addClause(" a.fiscal_year = ?");
            sqa.addPar(stYears);
        }

        sqa.addOrder("closing_period_id desc");

        sqa.addFilter(list.getFilter());

        list = sqa.getList(ClosingHeaderView.class);

        return list;
    }
    
    public void setList(DTOList list) {
        this.list = list;
    }
    
    public void clickCreate() throws Exception {
        ClosingSettingForm x = (ClosingSettingForm) super.newForm("closing_setting_form",this);
        
        x.createNew();
        
        x.show();
        
    }
    
    public void clickEdit() throws Exception {
        
        ClosingSettingForm x = (ClosingSettingForm) super.newForm("closing_setting_form",this);
        
        x.setAttribute("glpostingid", glpostingid);
        
        x.edit();
        
        x.show();
    }
    
    public void clickView() throws Exception {
        ClosingSettingForm x = (ClosingSettingForm) super.newForm("closing_setting_form",this);
        
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


    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

}
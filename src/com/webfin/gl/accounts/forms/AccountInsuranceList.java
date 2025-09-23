/***********************************************************************
 * Module:  com.webfin.gl.accounts.forms.AccountInsuranceList
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:32 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.gl.accounts.forms;

import com.crux.util.Tools;
import com.crux.web.controller.SessionManager;
import com.crux.web.form.Form;
import com.crux.web.form.FormManager;
import com.crux.util.DTOList;
import com.crux.util.SQLAssembler;
import com.crux.util.JNDIUtil;
import com.webfin.gl.model.*;
import com.webfin.gl.accounts.forms.AccountInsuranceForm;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class AccountInsuranceList extends Form {
    
    private DTOList list;
    private String accountno;
    private String stAccountNo;
    private String stDescription;
    private String stActiveFlag;
    private String glinsid;
    
    public DTOList getList() throws Exception {

        if (list==null) {
            list=new DTOList();
            list.getFilter().activate();
            //list.getFilter().orderKey="accountno";
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" * ");

        sqa.addQuery(
                " from gl_accounts_insurance2 "
                );
        
        if (stAccountNo!=null){
            sqa.addClause("accountno = ?");
            sqa.addPar(stAccountNo);
        }

        if (stDescription!=null){
            sqa.addClause("description = ?");
            sqa.addPar(stDescription);
        }

        sqa.addFilter(list.getFilter());

        sqa.addOrder("gl_ins_id, cc_code");

        list = sqa.getList(AccountInsuranceView.class);

        return list;
    }
    
    public void setList(DTOList list) {
        this.list = list;
    }
    
    public void clickCreate() throws Exception {
        AccountInsuranceForm x = (AccountInsuranceForm) super.newForm("accountinsuranceform",this);
        
        x.createNew();
        
        x.show();
        
    }
    
    public void clickEdit() throws Exception {
        
        AccountInsuranceForm x = (AccountInsuranceForm) super.newForm("accountinsuranceform",this);
        
        x.setAttribute("accountno",glinsid);
        
        x.edit();
        
        x.show();
    }
    
    public void clickView() throws Exception {
        AccountInsuranceForm x = (AccountInsuranceForm) super.newForm("accountinsuranceform",this);
        
        x.setAttribute("accountno",glinsid);
        
        x.view();
        
        x.show();
        
    }
    
    public void list() {
        
    }
    
    public void refresh() {
        
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
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

    public String getStActiveFlag() {
        return stActiveFlag;
    }

    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    /**
     * @return the glinsid
     */
    public String getGlinsid() {
        return glinsid;
    }

    /**
     * @param glinsid the glinsid to set
     */
    public void setGlinsid(String glinsid) {
        this.glinsid = glinsid;
    }
    
}
/***********************************************************************
 * Module:  com.webfin.gl.accounts.forms.AccountList
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
import com.webfin.gl.accounts.forms.AccountForm;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class AccountList extends Form {
    
    private DTOList list;
    private String accountid;
    private String stAccountNo;
    private String stDescription;
    private String branch = SessionManager.getInstance().getSession().getStBranch();
    
     public DTOList getList() throws Exception {

        if (list==null) {
            list=new DTOList();
            list.getFilter().activate();
            list.getFilter().orderKey="accountno";
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" * ");

        sqa.addQuery(" from gl_accounts ");

        sqa.addClause(" (deleted is null or deleted = 'N') ");

        if (stAccountNo!=null){
            sqa.addClause("accountno = ?");
            sqa.addPar(stAccountNo);
        }

        if (stDescription!=null){
            sqa.addClause("description = ?");
            sqa.addPar(stDescription);
        }

        if (branch!=null){
            sqa.addClause("substr(accountno,14,2) = ?");
            sqa.addPar(branch);
        }

//        sqa.addClause("acctype is null");

        sqa.addFilter(list.getFilter());

        sqa.setLimit(500);

        list = sqa.getList(AccountView2.class);

        return list;
    }
    
    
    public void setList(DTOList list) {
        this.list = list;
    }
    
    public void clickCreate() throws Exception {
        AccountForm x = (AccountForm) super.newForm("account_form",this);
        
        x.createNew();
        
        x.show();
        
    }
    
    public void clickEdit() throws Exception {
        
        AccountForm x = (AccountForm) super.newForm("account_form",this);
        
        x.setAttribute("accountid",accountid);
        
        x.edit();
        
        x.show();
    }
    
    public void clickView() throws Exception {
        AccountForm x = (AccountForm) super.newForm("account_form",this);
        
        x.setAttribute("accountid",accountid);
        
        x.view();
        
        x.show();
        
    }
    
    public void list() {
        
    }
    
    public void refresh() {
        
    }
    
    public String getAccountid() {
        return accountid;
    }
    
    public void setAccountid(String accountid) {
        this.accountid = accountid;
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
    
    public String getBranch() {
        return branch;
    }
    
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    private DTOList accountsID;

    public void clickUploadExcel() throws Exception {
        AccountForm x = (AccountForm) super.newForm("uploadAccount_form",this);

        x.createNewUpload();

        x.show();

    }

    /**
     * @return the accountsID
     */
    public DTOList getAccountsID() {
        return accountsID;
    }

    /**
     * @param accountsID the accountsID to set
     */
    public void setAccountsID(DTOList accountsID) {
        this.accountsID = accountsID;
    }

    public void clickUploadNoper() throws Exception {
        AccountForm x = (AccountForm) super.newForm("uploadNoper_form",this);

        x.createNewUpload();

        x.show();

    }

}
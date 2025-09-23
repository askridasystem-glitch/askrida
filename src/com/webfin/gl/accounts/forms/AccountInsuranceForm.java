/***********************************************************************
 * Module:  com.webfin.gl.accounts.forms.AccountInsuranceForm
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:50 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.gl.accounts.forms;

import com.crux.web.controller.SessionManager;
import com.crux.web.form.Form;
import com.crux.pool.DTOPool;
import com.crux.util.JNDIUtil;
import com.webfin.gl.ejb.*;
import com.webfin.gl.model.*;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class AccountInsuranceForm extends Form {
    
    private AccountInsuranceView account;
    private String stAccountNo;
    private String stDescription;
    private String stActiveFlag;
    
    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName()))
        .create();
    }
    
    public void createNew() {
        account= new AccountInsuranceView();
        
        account.markNew();
        
        setTitle("CREATE ACCOUNT CATEGORY");
        
    }
    
    public void edit() {
        
        final String accountno = (String)getAttribute("accountno");
        
        account = (AccountInsuranceView) DTOPool.getInstance().getDTO(AccountInsuranceView.class, accountno);
        
        account.markUpdateO();
        
        setTitle("EDIT ACCOUNT CATEGORY");
    }
    
    public void view() {
        
        final String accountno = (String)getAttribute("accountno");
        
        account = (AccountInsuranceView) DTOPool.getInstance().getDTO(AccountInsuranceView.class, accountno);
        
        setReadOnly(true);
        
        setTitle("VIEW ACCOUNT CATEGORY");
    }
    
    public void save() throws Exception {
        
        getRemoteGeneralLedger().save3(account);
        
        close();
    }
    
    public void close() {
        super.close();
    }

    public AccountInsuranceView getAccount() {
        return account;
    }

    public void setAccount(AccountInsuranceView account) {
        this.account = account;
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
    
    
}

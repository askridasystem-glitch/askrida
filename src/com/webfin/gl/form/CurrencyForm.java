/***********************************************************************
 * Module:  com.webfin.gl.currencys.forms.CurrencyForm
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:50 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.gl.form;

import com.crux.web.controller.SessionManager;
import com.crux.web.form.Form;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.SQLUtil;
import com.crux.util.JNDIUtil;
import com.crux.util.LOV;
import com.crux.common.controller.FormTab;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.ejb.*;
import com.webfin.WebFinLOVRegistry;
import com.webfin.gl.model.*;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class CurrencyForm extends Form {
    
    private GLCurrencyHistoryView currency;
    
    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName()))
        .create();
    }
    
    public void createNew() {
        currency= new GLCurrencyHistoryView();
        
        currency.markNew();
        
        setTitle("CREATE ACCOUNT CATEGORY");
        
    }
    
    public void edit() {
        final String currency_id = (String)getAttribute("currencyid");
        
        currency = (GLCurrencyHistoryView) DTOPool.getInstance().getDTO(GLCurrencyHistoryView.class, currency_id);
        
        currency.markUpdate();
        
        setTitle("EDIT ACCOUNT CATEGORY");
    }
    
    public void view() {
        final String currency_id = (String)getAttribute("currencyid");
        
        currency = (GLCurrencyHistoryView) DTOPool.getInstance().getDTO(GLCurrencyHistoryView.class, currency_id);
        
        setReadOnly(true);
        
        setTitle("VIEW ACCOUNT CATEGORY");
    }
    
    public void save() throws Exception {
        
        getRemoteGeneralLedger().saveCurrency(currency);
        
        close();
    }
    
    public void close() {
        super.close();
    }

    public GLCurrencyHistoryView getCurrency() {
        return currency;
    }

    public void setCurrency(GLCurrencyHistoryView currency) {
        this.currency = currency;
    }
}

/***********************************************************************
 * Module:  com.webfin.master.category.form.ClausulesMasterForm
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:50 PM
 * Purpose:
 ***********************************************************************/
  
package com.webfin.master.clausules.form;

import com.crux.web.form.Form;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.SQLUtil;
import com.crux.util.JNDIUtil;
import com.crux.util.LOV;
import com.crux.common.controller.FormTab;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.WebFinLOVRegistry;
import com.webfin.insurance.model.*;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class ClausulesMasterForm extends Form {
    
    private InsuranceClausulesView clausules;
    
    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
        .create();
    }
    
    public void createNew() {
        clausules= new InsuranceClausulesView();
        
        clausules.markNew();
        clausules.setStActiveFlag("Y");
        
        setTitle("CREATE CLAUSULES");
        
    }
    
    public void edit() {
        final String clausules_id = (String)getAttribute("clausulesid");
        
        clausules = (InsuranceClausulesView) DTOPool.getInstance().getDTO(InsuranceClausulesView.class, clausules_id);
        
        clausules.markUpdate();
        
        setTitle("EDIT CLAUSULES");
    }
    
    public void view() {
        final String clausules_id = (String)getAttribute("clausulesid");
        
        clausules = (InsuranceClausulesView) DTOPool.getInstance().getDTO(InsuranceClausulesView.class, clausules_id);
        
        setReadOnly(true);
        
        setTitle("VIEW CLAUSULES");
    }
    
    public void save() throws Exception {
        
        getRemoteInsurance().save(clausules);
        
        close();
    }
    
    public void close() {
        super.close();
    }
    
    public InsuranceClausulesView getClausules() {
        return clausules;
    }
    
    public void setClausules(InsuranceClausulesView clausules) {
        this.clausules = clausules;
    }

    public void copyWording1(){
        clausules.setStDescriptionNew(clausules.getStDescription());
    }
    
}

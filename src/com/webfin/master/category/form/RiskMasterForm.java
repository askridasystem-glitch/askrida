/***********************************************************************
 * Module:  com.webfin.master.category.form.RiskMasterForm
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:50 PM
 * Purpose:
 ***********************************************************************/
  
package com.webfin.master.category.form;

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

public class RiskMasterForm extends Form {
    
    private InsuranceRiskCategoryView risk;
    
    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
        .create();
    }
    
    public void createNew() {
        risk= new InsuranceRiskCategoryView();
        
        risk.markNew();
        
        setTitle("CREATE RISK CATEGORY");
        
    }
    
    public void edit() {
        final String risk_id = (String)getAttribute("riskid");
        
        risk = (InsuranceRiskCategoryView) DTOPool.getInstance().getDTO(InsuranceRiskCategoryView.class, risk_id);
        
        risk.markUpdate();
        
        setTitle("EDIT RISK CATEGORY");
    }
    
    public void view() {
        final String risk_id = (String)getAttribute("riskid");
        
        risk = (InsuranceRiskCategoryView) DTOPool.getInstance().getDTO(InsuranceRiskCategoryView.class, risk_id);
        
        setReadOnly(true);
        
        setTitle("VIEW RISK CATEGORY");
    }
    
    public void save() throws Exception {
        
        getRemoteInsurance().save(risk);
        
        close();
    }
    
    public void close() {
        super.close();
    }
    
    public InsuranceRiskCategoryView getRisk() {
        return risk;
    }
    
    public void setRisk(InsuranceRiskCategoryView risk) {
        this.risk = risk;
    }
    
}

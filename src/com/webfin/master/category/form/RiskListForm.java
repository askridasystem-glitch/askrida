/***********************************************************************
 * Module:  com.webfin.master.category.form.RiskListForm
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:32 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.master.category.form;

import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.SQLAssembler;
import com.crux.util.JNDIUtil;
import com.crux.util.Tools;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.model.InsuranceRiskCategoryView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class RiskListForm extends Form {
    
    private DTOList list;
    private String riskid;
    private String stPolicyTypeID;
    private String stValidData;
    private String stRiskFlag;
    
    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
        .create();
    }
    
    public String getRiskid() {
        return riskid;
    }
    
    public void setRiskid(String riskid) {
        this.riskid = riskid;
    }
    
    public DTOList getList() throws Exception {
        
        if (list==null) {
            list=new DTOList();
            list.getFilter().activate();
            list.getFilter().orderKey="ins_risk_cat_id";
        }
        
        SQLAssembler sqa = new SQLAssembler();
        
        sqa.addSelect("*");
        
        sqa.addQuery(
                "from ins_risk_cat"
                );
        
        if (stPolicyTypeID!=null){
            sqa.addClause("poltype_id = ?");
            sqa.addPar(stPolicyTypeID);
        }
        
        if (Tools.isYes(stValidData)) {
            sqa.addClause("active_flag = 'Y'");
        }
        
        if (Tools.isYes(stRiskFlag)) {
            sqa.addClause("exc_risk_flag = 'Y'");
        }
        
        sqa.addFilter(list.getFilter());
        
        list = sqa.getList(InsuranceRiskCategoryView.class);
        
        return list;
    }
    
    public void setList(DTOList list) {
        this.list = list;
    }
    
    public void clickCreate() throws Exception {
        RiskMasterForm x = (RiskMasterForm) super.newForm("risk_form",this);
        
        x.createNew();
        
        x.show();
        
    }
    
    public void clickEdit() throws Exception {
        
        RiskMasterForm x = (RiskMasterForm) super.newForm("risk_form",this);
        
        x.setAttribute("riskid",riskid);
        
        x.edit();
        
        x.show();
    }
    
    public void clickView() throws Exception {
        RiskMasterForm x = (RiskMasterForm) super.newForm("risk_form",this);
        
        x.setAttribute("riskid",riskid);
        
        x.view();
        
        x.show();
        
    }
    
    public void list() {
        
    }
    
    public String getStPolicyTypeID() {
        return stPolicyTypeID;
    }
    
    public void setStPolicyTypeID(String stPolicyTypeID) {
        this.stPolicyTypeID = stPolicyTypeID;
    }
    
    public void refresh() {
        
    }
    
    public void btnRefresh() {
        
    }

    public String getStValidData() {
        return stValidData;
    }

    public void setStValidData(String stValidData) {
        this.stValidData = stValidData;
    }

    public String getStRiskFlag() {
        return stRiskFlag;
    }

    public void setStRiskFlag(String stRiskFlag) {
        this.stRiskFlag = stRiskFlag;
    }
}
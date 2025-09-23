/***********************************************************************
 * Module:  com.webfin.master.category.form.RiskListForm
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:32 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.master.clausules.form;

import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.SQLAssembler;
import com.crux.util.JNDIUtil;
import com.crux.util.Tools;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.model.InsuranceClausulesView;
import com.webfin.insurance.model.InsuranceRiskCategoryView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class ClausulesListForm extends Form {
    
    private DTOList list;
    private String clausulesid;
    private String stPolicyTypeID;
    private String stBranch;
    private InsuranceClausulesView clausul;

    private String stPolicyTypeDesc;
    private String stBranchDesc;
    
    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
        .create();
    }
    
  
    
    public DTOList getList() throws Exception {

        if (list==null) {
            list=new DTOList();
            list.getFilter().activate();
            //list.getFilter().orderKey="ins_clause_id";
        }

        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.*,"+
                        " (select y.description from gl_cost_center y where y.cc_code = a.cc_code) as cost_center,"+
                        " (select x.user_name from s_users x where x.user_id = a.create_who) as entry_user_name");

        sqa.addQuery(
                "from ins_clausules a "
                );

        sqa.addClause("coalesce(a.active_flag,'Y') = 'Y'");

        if (stPolicyTypeID!=null){
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }
        if (stBranch!=null){
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        sqa.addOrder("ins_clause_id desc");

        sqa.setLimit(500);

        sqa.addFilter(list.getFilter());

        list = sqa.getList(InsuranceClausulesView.class);

        return list;
    }
    
    public void setList(DTOList list) {
        this.list = list;
    }
    
    public void clickCreate() throws Exception {
        ClausulesMasterForm x = (ClausulesMasterForm) super.newForm("clausules_form",this);
        
        x.createNew();
        
        x.show();
        
    }
    
    public void clickEdit() throws Exception {
        
        ClausulesMasterForm x = (ClausulesMasterForm) super.newForm("clausules_form",this);
        
        x.setAttribute("clausulesid",clausulesid);
        
        x.edit();
        
        x.show();
    }
    
    public void clickView() throws Exception {
        ClausulesMasterForm x = (ClausulesMasterForm) super.newForm("clausules_form",this);
        
        x.setAttribute("clausulesid",clausulesid);
        
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

    public String getClausulesid()
    {
        return clausulesid;
    }

    public void setClausulesid(String clausulesid)
    {
        this.clausulesid = clausulesid;
    }

    public String getStBranch() {
        return stBranch;
    }

    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }
    
    public InsuranceClausulesView getClausules() {
        return clausul;
    }

    public void clickPrint() throws Exception {
        //goPrint = "Y";
        super.redirect("/pages/clausules/rpt_clausules.fop");
    }

    public SQLAssembler getSQAClausules() {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.* ");

        sqa.addQuery(" from ins_clausules a ");

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        sqa.addOrder("a.cc_code,a.pol_type_id");
        return sqa;
    }

    /**
     * @return the stPolicyTypeDesc
     */
    public String getStPolicyTypeDesc() {
        return stPolicyTypeDesc;
    }

    /**
     * @param stPolicyTypeDesc the stPolicyTypeDesc to set
     */
    public void setStPolicyTypeDesc(String stPolicyTypeDesc) {
        this.stPolicyTypeDesc = stPolicyTypeDesc;
    }

    /**
     * @return the stBranchDesc
     */
    public String getStBranchDesc() {
        return stBranchDesc;
    }

    /**
     * @param stBranchDesc the stBranchDesc to set
     */
    public void setStBranchDesc(String stBranchDesc) {
        this.stBranchDesc = stBranchDesc;
    }


}
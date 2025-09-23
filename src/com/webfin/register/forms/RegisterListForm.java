/***********************************************************************
 * Module:  com.webfin.entity.forms.EntityListForm
 * Author:  Denny Mahendra
 * Created: Jan 5, 2006 11:22:19 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.register.forms;

import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.SQLAssembler;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.login.model.UserSessionView;
import com.webfin.register.ejb.RegisterManager;
import com.webfin.register.ejb.RegisterManagerHome;
import com.webfin.register.filter.RegisterFilter;
import com.webfin.register.model.RegisterView;
import com.crux.util.*;
import com.webfin.insurance.ejb.UserManager;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class RegisterListForm extends Form {
    private DTOList list;
    private String outid;
    private String refno;
    private String branch;
    private String division;
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("REGISTER_NAVBR");
    
    public void initialize() {
        //branch = SessionManager.getInstance().getSession().getStBranch();
        setAutoRefresh(true);
        division = UserManager.getInstance().getUser().getStDivision();
    }
    
    public String getBranch() {
        return branch;
    }
    
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    public String getRefNo() {
        return refno;
    }
    
    public void setRefNo(String refno) {
        this.refno = refno;
    }
    
    public String getOutID() {
        return outid;
    }
    
    public void setOutID(String outid) {
        this.outid = outid;
    }
    
    private RegisterManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((RegisterManagerHome) JNDIUtil.getInstance().lookup("RegisterManagerEJB",RegisterManagerHome.class.getName()))
        .create();
    }
    
    public DTOList getList() throws Exception {
        if (list==null) {
            list=new DTOList();
            final RegisterFilter f = new RegisterFilter();
            f.orderKey="reg_id";
            list.setFilter(f.activate());
        }
        //list = getRemoteEntityManager().listEntities((EntityFilter) list.getFilter());
        
        final SQLAssembler sqa = new SQLAssembler();
        
        sqa.addSelect(" * ");
        sqa.addQuery(" from "+
                    " (select a.*,b.user_name "+
                    " from ins_pol_register a "+
                    " left join s_users b on a.user_id = b.user_id) a ");

        UserSessionView us = SessionManager.getInstance().getSession();
        
        //sqa.addClause("(sharef0=? or sharef1=? or sharef2='NAT')");
        //sqa.addPar(us.getStUserID());
        //sqa.addPar(us.getStBranch());
        //sqa.addClause("create_who=?");
        //sqa.addPar(us.getStUserID());
        
        sqa.addClause("delete_flag=?");
        sqa.addPar("N");
        
        
        if (branch!=null) {
            sqa.addClause("cc_code=?");
            sqa.addPar(branch);
        }
        
        if (division!=null) {
            sqa.addClause("division=?");
            sqa.addPar(division);
        }
        
        sqa.addFilter(list.getFilter());
        
        list = sqa.getList(RegisterView.class);
        
        return list;
    }
    
    
    public void setList(DTOList list) {
        this.list = list;
    }
    
    public void clickCreate() throws Exception {
        final RegisterMasterForm form = (RegisterMasterForm) super.newForm("register_edit", this);
        
        form.createNew();
        
        form.show();
    }
    
    public void clickEdit() throws Exception {
        final RegisterMasterForm form = (RegisterMasterForm) super.newForm("register_edit",this);
        
        form.setAttribute("outid",outid);
        
        form.edit();
        
        form.show();
    }
    
    public void clickView() throws Exception {
        final RegisterMasterForm form = (RegisterMasterForm) super.newForm("register_edit",this);
        
        form.setAttribute("outid",outid);
        
        form.view();
        
        form.show();
    }
    
    public void clickDelete()throws Exception{
        final RegisterMasterForm form = (RegisterMasterForm) super.newForm("register_edit",this);
        
        form.setAttribute("outid",outid);
        
        if(outid==null) {
            throw new RuntimeException("Choose letter first !");
        } else {
            String delete_master = "Update ins_pol_register set delete_flag = 'Y' where reg_id='"+ outid +"'";
            
            final SQLUtil sql = new SQLUtil();
            sql.execSQL(delete_master);
        }
        
        
        
    }
    
    public void refresh() {
    }

    public boolean isCanNavigateBranch() {
        return canNavigateBranch;
    }

    public void setCanNavigateBranch(boolean canNavigateBranch) {
        this.canNavigateBranch = canNavigateBranch;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}

/***********************************************************************
 * Module:  com.webfin.outcoming.forms.UploadBODListForm
 * Author:  Denny Mahendra
 * Created: Jan 5, 2006 11:22:19 PM
 * Purpose: 
 ***********************************************************************/
package com.webfin.outcoming.forms;

import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.login.model.UserSessionView;
import com.webfin.outcoming.ejb.OutcomingManager;
import com.webfin.outcoming.ejb.OutcomingManagerHome;
import com.webfin.outcoming.filter.OutcomingFilter;
import com.crux.util.*;
import com.webfin.outcoming.model.UploadBODView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class UploadBODListForm extends Form {

    private DTOList list;
    private String outid;
    private String refno;
    private String branch;

    public void initialize() {
        branch = SessionManager.getInstance().getSession().getStBranch();
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

    private OutcomingManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((OutcomingManagerHome) JNDIUtil.getInstance().lookup("OutcomingManagerEJB", OutcomingManagerHome.class.getName())).create();
    }

    public DTOList getList() throws Exception {
        if (list == null) {
            list = new DTOList();
            final OutcomingFilter f = new OutcomingFilter();
            list.setFilter(f.activate());
        }
        //list = getRemoteEntityManager().listEntities((EntityFilter) list.getFilter());

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("*,date_trunc('second',create_date)::time::varchar as jam");
        sqa.addQuery(" from uploadbod_letter");

        UserSessionView us = SessionManager.getInstance().getSession();

        sqa.addClause("create_who=?");
        sqa.addPar(us.getStUserID());

        sqa.addClause("delete_flag=?");
        sqa.addPar("N");
        sqa.addOrder(" out_id desc");

        sqa.addFilter(list.getFilter());

        list = sqa.getList(UploadBODView.class);

        return list;
    }

    public void setList(DTOList list) {
        this.list = list;
    }

    public void clickCreate() throws Exception {
        final UploadBODMasterForm form = (UploadBODMasterForm) super.newForm("uploadbod_edit", this);

        form.createNew();

        form.show();
    }

    public void clickEdit() throws Exception {
        final UploadBODMasterForm form = (UploadBODMasterForm) super.newForm("uploadbod_edit", this);

        form.setAttribute("outid", outid);

        form.edit();

        form.show();
    }

    public void clickView() throws Exception {
        final UploadBODMasterForm form = (UploadBODMasterForm) super.newForm("uploadbod_edit", this);

        form.setAttribute("outid", outid);

        form.view();

        form.show();
    }

    public void clickDelete() throws Exception {
        final UploadBODMasterForm form = (UploadBODMasterForm) super.newForm("uploadbod_edit", this);

        form.setAttribute("outid", outid);

        if (outid == null) {
            throw new RuntimeException("Choose letter first !");
        } else {
            String delete_master = "Update uploadbod_letter set delete_flag = 'Y' where out_id='" + outid + "'";
            //String delete_address = "Delete from outcoming_dist where out_id='"+ outid +"'";

            final SQLUtil sql = new SQLUtil();
            sql.execSQL(delete_master);
            //sql.execSQL(delete_address);
        }



    }

    public void refresh() {
    }

    public void clickForward() throws Exception {
        final UploadBODMasterForm form = (UploadBODMasterForm) super.newForm("uploadbod_edit", this);

        form.setAttribute("outid", outid);

        form.forward();

        form.show();
    }
}

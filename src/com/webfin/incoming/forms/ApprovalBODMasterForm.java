/***********************************************************************
 * Module:  com.webfin.entity.forms.EntityMasterForm
 * Author:  Denny Mahendra
 * Created: Dec 28, 2005 10:18:11 PM
 * Purpose: 
 ***********************************************************************/
package com.webfin.incoming.forms;

import com.crux.web.form.WebForm;
import com.crux.util.*;
import com.crux.common.controller.FormTab;
import com.webfin.incoming.ejb.IncomingManager;
import com.webfin.incoming.ejb.IncomingManagerHome;
import com.webfin.incoming.model.*;
import com.webfin.outcoming.forms.UploadBODMasterForm;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public class ApprovalBODMasterForm extends WebForm {

    private ApprovalBODView entity = null;
    private LOV lovPaymentTerm;
    private FormTab tabs;
    private ApprovalBODDistributionView address;
    private ApprovalBODDocumentsView doc;
    private String stSelectedAddress;
    private String userIndex;
    private String addressid;
    private String instIndex;
    private DTOList document;

//    public LOV getLovAddresses() {
//        return entity.getDistributions();
//    }
    public void createNew() {
        entity = new ApprovalBODView();
        entity.markNew();
        //entity.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());
    }

    public void onChangeBranch() {
    }

    public void onClassChange() {
    }

    public String getUserIndex() {
        return userIndex;
    }

    public void setUserIndex(String userIndex) {
        this.userIndex = userIndex;
    }

//    public void doDeleteAddress() {
//
//        int n = Integer.parseInt(stSelectedAddress);
//
//        entity.getDistributions().delete(n);
//
//        stSelectedAddress = null;
//
//        getTabs();
//
//    }
    public void edit() throws Exception {
        view();
        setReadOnly(false);
    }

    public void view() throws Exception {
        final String entity_id = (String) getAttribute("inid");

        entity = getRemoteEntityManager().loadEntity2(entity_id);

        //if (entity.getStReadFlag().equalsIgnoreCase("N")) {
        //    getRemoteEntityManager().updateReadStatus2(entity, "Y");
        //}

        if (entity == null) {
            throw new RuntimeException("Entity not found !");
        }

        //setReadOnly(true);
    }

//    public void afterUpdateForm() {
//        if (entity != null) {
//            final Integer idx = NumberUtil.getInteger(stSelectedAddress);
//            address = idx == null ? null : (ApprovalBODDistributionView) entity.getDistributions().get(idx.intValue());
//
//            final DTOList addresses = entity.getDistributions();
//
//            entity.setStReceiver(null);
//
//            for (int i = 0; i < addresses.size(); i++) {
//                ApprovalBODDistributionView adr = (ApprovalBODDistributionView) addresses.get(i);
//
//                if (entity.getStReceiver() == null) {
//                    entity.setStReceiver(adr.getStReceiver());
//                }
//            }
//        }
//    }
    public String getStSelectedAddress() {
        return stSelectedAddress;
    }

    public void setStSelectedAddress(String stSelectedAddress) {
        this.stSelectedAddress = stSelectedAddress;
    }

    public ApprovalBODDistributionView getAddress() {
        return address;
    }

    public void setAddress(ApprovalBODDistributionView address) {
        this.address = address;
    }

//    public void doNewAddress() {
//        final ApprovalBODDistributionView adr = new ApprovalBODDistributionView();
//
//        adr.markNew();
//
//        entity.getDistributions().add(adr);
//
//        stSelectedAddress = String.valueOf(entity.getDistributions().size() - 1);
//
//        address = adr;
//    }
    public void selectAddress() {
        // do nothing
    }

    public FormTab getTabs() {
        if (tabs == null) {
            tabs = new FormTab();

            tabs.add(new FormTab.TabBean("TAB0", "SURAT", true));
            tabs.add(new FormTab.TabBean("TAB1", "LAMPIRAN", true));

            tabs.setActiveTab("TAB0");
        }

        return tabs;
    }

    public void setTabs(FormTab tabs) {
        this.tabs = tabs;
    }

    public LOV getLovPaymentTerm() throws Exception {
        if (lovPaymentTerm == null) {
            lovPaymentTerm = ListUtil.getLookUpFromQuery("select payment_term_id,description from payment_term");
        }
        return lovPaymentTerm;
    }

    public void setLovPaymentTerm(LOV lovPaymentTerm) {
        this.lovPaymentTerm = lovPaymentTerm;
    }

    public ApprovalBODMasterForm() {
    }

    public ApprovalBODView getEntity() {
        return entity;
    }

    public void setEntity(ApprovalBODView entity) {
        this.entity = entity;
    }

    private IncomingManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((IncomingManagerHome) JNDIUtil.getInstance().lookup("IncomingManagerEJB", IncomingManagerHome.class.getName())).create();
    }

    public void doSave() throws Exception {
        final ApprovalBODView cloned = (ApprovalBODView) ObjectCloner.deepCopy(entity);
        getRemoteEntityManager().save2(cloned);
        super.close();
    }

    public void doDelete() throws Exception {
        //getRemoteEntityManager().
    }

    public void doClose() {
        super.close();
    }

    public void doReply() throws Exception {
        final UploadBODMasterForm form = (UploadBODMasterForm) super.newForm("uploadbod_edit", this);

        form.reply(entity);

        form.setReplyMode(true);

        form.show();
    }

    public void forward() throws Exception {
        final UploadBODMasterForm form = (UploadBODMasterForm) super.newForm("uploadbod_edit", this);

        form.replyForward(entity);

        form.setReplyMode(true);

        form.show();
    }

    public String getInstIndex() {
        return instIndex;
    }

    public void setInstIndex(String instIndex) {
        this.instIndex = instIndex;
    }

    public DTOList getDocument() throws Exception {
        return entity.getDocuments();
    }

    public void setDocument(DTOList document) {
        this.document = document;
    }

    public void doSave2() throws Exception {

        getRemoteEntityManager().updateApproval(entity, "Y");

        String replynote = entity.getStNote() + " \n \n \n "
                + entity.getStReceiverName() + " : " + entity.getStApprovalType() + " - " + entity.getStReplyNote();

        MailUtil2 mail = new MailUtil2();
        mail.sendEmail(entity.getStReceiver(), entity.getStSender(), entity.getStSubject(), replynote);

        super.close();
    }
    public String goPrint = "N";

    public void btnPrint() throws Exception {

        doRefresh();
        goPrint = "Y";
    }

    public void doRefresh() throws Exception {
        //getRemoteEntityManager().
    }
}

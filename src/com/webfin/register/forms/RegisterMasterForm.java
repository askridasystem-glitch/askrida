/***********************************************************************
 * Module:  com.webfin.entity.forms.EntityMasterForm
 * Author:  Denny Mahendra
 * Created: Dec 28, 2005 10:18:11 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.register.forms;

import com.crux.web.form.WebForm;
import com.crux.util.*;
import com.crux.common.controller.FormTab;
import com.webfin.register.model.RegisterView;

import com.webfin.register.ejb.RegisterManager;
import com.webfin.register.ejb.RegisterManagerHome;
import com.webfin.incoming.model.IncomingView;
import com.webfin.incoming.ejb.IncomingManager;
import com.webfin.incoming.ejb.IncomingManagerHome;
import com.crux.common.model.UserSession;
import com.crux.web.controller.SessionManager;
import com.webfin.insurance.ejb.UserManager;
import java.util.Date;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public class RegisterMasterForm extends WebForm {
    
    private HttpServletRequest rq;
    private RegisterView entity = null;
    
    private LOV lovPaymentTerm;
    private FormTab tabs;
    
    private String stSelectedAddress;
    private String userIndex;
    private String addressid;
    private boolean replyMode;
    
    public boolean getReplyMode(){
        return replyMode;
    }
    
    public void setReplyMode(boolean replyMode){
        this.replyMode = replyMode;
    }
    
    public boolean isReplyMode(){
        return replyMode;
    }

    public void createNew() {
        entity = new RegisterView();
        entity.markNew();
        entity.setStDivision(UserManager.getInstance().getUser().getStDivision());
        //entity.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());
    }
    
    public void reply(IncomingView incoming) {
        entity = new RegisterView();
        entity.markNew();
        
        entity.setDtLetterDate(new Date());
        entity.setStSubject("Re: "+incoming.getStSubject());
        entity.setStNote(incoming.getStNote()+"\n");
        entity.setStNote(entity.getStNote()+"-------------------------------------------------------------------------\n");
        
        //set reveiver
        
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
    
    public void doDeleteAddress() {
        
        
//	   final RegisterDistributionView adr = new RegisterDistributionView();
//
//	   final Integer id = NumberUtil.getInteger(stSelectedAddress);
//	   String id2 = id.toString();
//	   int id3 = Integer.parseInt(stSelectedAddress);
//
//	   entity.getDistributions().delete(id3);
//
//
//	   System.out.println("Id= "+ id);
        
        //
        //int n = Integer.parseInt(userIndex);
        //user.getUserroles().delete(n);
        int n = Integer.parseInt(stSelectedAddress);
        
        //final DTOList objects = entity.getDistributions();
        
        //entity.getDistributions().delete(n);
        
        //objects.delete(n);
        
        //getLovAddresses();
        
        stSelectedAddress=null;
        
        
    }
    
    public void edit() throws Exception {
        view();
        setReadOnly(false);
        
        entity.markUpdate();
        
    }
    
    public void view() throws Exception {
        final String entity_id = (String)getAttribute("outid");
        entity=getRemoteEntityManager().loadEntity(entity_id);
        
        if (entity==null) throw new RuntimeException("Entity not found !");
        
        setReadOnly(true);
    }
    
    public void afterUpdateForm() {

    }
    
    public String getStSelectedAddress() {
        return stSelectedAddress;
    }
    
    public void setStSelectedAddress(String stSelectedAddress) {
        this.stSelectedAddress = stSelectedAddress;
    }
    
    

    public FormTab getTabs() {
        if (tabs==null) {
            tabs = new FormTab();
            
            tabs.add(new FormTab.TabBean("TAB1","{L-ENG RECEIVER-L}{L-INA PENERIMA-L}",true));
            //tabs.add(new FormTab.TabBean("TAB2","RELATIONS",true));
            
            tabs.setActiveTab("TAB1");
            
        }
        return tabs;
    }
    
    public void setTabs(FormTab tabs) {
        this.tabs = tabs;
    }
    
    public LOV getLovPaymentTerm() throws Exception {
        if (lovPaymentTerm==null) lovPaymentTerm = ListUtil.getLookUpFromQuery("select payment_term_id,description from payment_term");
        return lovPaymentTerm;
    }
    
    public void setLovPaymentTerm(LOV lovPaymentTerm) {
        this.lovPaymentTerm = lovPaymentTerm;
    }
    
    public RegisterMasterForm() {
    }
    
    public RegisterView getEntity() {
        return entity;
    }
    
    public void setEntity(RegisterView entity) {
        this.entity = entity;
    }
    
    private RegisterManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((RegisterManagerHome) JNDIUtil.getInstance().lookup("RegisterManagerEJB",RegisterManagerHome.class.getName()))
        .create();
    }
    
    private IncomingManager getRemoteEntityManager2() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((IncomingManagerHome) JNDIUtil.getInstance().lookup("IncomingManagerEJB",IncomingManagerHome.class.getName()))
        .create();
    }
    
    public void doSave() throws Exception {
        UserSession us = SessionManager.getInstance().getSession();
        
        entity.setStSender(us.getStUserID());
        entity.setStDeleteFlag("N");
        final RegisterView cloned = (RegisterView)ObjectCloner.deepCopy(entity);
        
        getRemoteEntityManager().save(cloned);

        super.close();
    }
    

    
    public void doDelete() throws Exception {
        //getRemoteEntityManager().
    }
    
    public void doClose() {
        super.close();
    }
    
    public void onChangePolicyTypeGroup() {
        
    }
    
    public void onChangePolicyType() throws Exception {
               
    }
    
    public void onChangeRegion() throws Exception {

    }
    
     public void changeBranch() {
    }
}

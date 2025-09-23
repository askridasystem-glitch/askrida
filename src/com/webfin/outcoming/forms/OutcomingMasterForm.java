/***********************************************************************
 * Module:  com.webfin.entity.forms.EntityMasterForm
 * Author:  Denny Mahendra
 * Created: Dec 28, 2005 10:18:11 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.outcoming.forms;

import com.crux.web.form.WebForm;
import com.crux.web.controller.SessionManager;
import com.crux.login.model.UserRoleView;
import com.crux.util.*;
import com.crux.common.controller.FormTab;
import com.webfin.incoming.forms.IncomingListForm;
import com.webfin.incoming.model.IncomingDocumentsView;
import com.webfin.outcoming.model.OutcomingDocumentsView;
import com.webfin.outcoming.model.OutcomingView;
import com.webfin.outcoming.model.OutcomingDistributionView;
import com.webfin.outcoming.ejb.OutcomingManager;
import com.webfin.outcoming.ejb.OutcomingManagerHome;
import com.webfin.incoming.model.IncomingView;
import com.webfin.incoming.model.IncomingDistributionView;
import com.webfin.incoming.ejb.IncomingManager;
import com.webfin.incoming.ejb.IncomingManagerHome;
import com.crux.util.SQLAssembler;
import com.jspsmart.upload.Request;
import com.crux.login.model.UserSessionView;
import com.crux.common.model.UserSession;
import com.crux.common.controller.UserSessionMgr;
import com.crux.web.controller.SessionManager;
import java.util.Date;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class OutcomingMasterForm extends WebForm
{
    
    private final static transient LogManager logger = LogManager.getInstance(OutcomingMasterForm.class);
    
    private HttpServletRequest rq;
    private OutcomingView entity = null;
    
    private LOV lovPaymentTerm;
    private FormTab tabs;
    private DTOList address;
    private String stSelectedAddress;
    private String userIndex;
    private String addressid;
    private boolean replyMode;
    private String stSelectedDocument;
    private DTOList document;
    private String instIndex;
    private String addressIndex;
    
    public boolean getReplyMode()
    {
        return replyMode;
    }
    
    public void setReplyMode(boolean replyMode)
    {
        this.replyMode = replyMode;
    }
    
    public boolean isReplyMode()
    {
        return replyMode;
    }
    
    public LOV getLovAddresses()
    {
        return entity.getDistributions();
    }
    
    public void createNew()
    {
        entity = new OutcomingView();
        entity.markNew();
        //entity.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());
    }
    
    public void reply(IncomingView incoming)
    {
        entity = new OutcomingView();
        entity.markNew();
        
        entity.setStFilePhysic(incoming.getStFilePhysic());
        entity.setDtLetterDate(new Date());
        entity.setStSubject("Re: "+incoming.getStSubject());
        entity.setStNote("\n\n"+ "-------------------------------------------------------------------------\n");
        entity.setStNote(entity.getStNote() + incoming.getStNote()+"\n");
        
        //set reveiver
        final OutcomingDistributionView adr = new OutcomingDistributionView();
        
        adr.markNew();
        
        adr.setStReceiver(incoming.getStSender());
        
        entity.getDistributions().add(adr);
        
        stSelectedAddress = String.valueOf(entity.getDistributions().size()-1);
        
        //address = adr;
    }
    
    public void onChangeBranch()
    {
    }
    
    public void onClassChange()
    {
        
    }
    
    public String getUserIndex()
    {
        return userIndex;
    }
    
    public void setUserIndex(String userIndex)
    {
        this.userIndex = userIndex;
    }
    
    public void doDeleteAddress()
    {
        
        
//	   final OutcomingDistributionView adr = new OutcomingDistributionView();
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
        //int n = Integer.parseInt(stSelectedAddress);
        
        //final DTOList objects = entity.getDistributions();
        
        entity.getDistributions().delete(Integer.parseInt(addressIndex));
        
        //objects.delete(n);
        
        //getLovAddresses();
        
        //stSelectedAddress=null;
        
        
    }
    
    public void edit() throws Exception
    {
        view();
        setReadOnly(false);
        
        entity.markUpdate();
        
        final DTOList addresses = entity.getDistributions();
        
        for (int i = 0; i < addresses.size(); i++)
        {
            OutcomingDistributionView ead = (OutcomingDistributionView) addresses.get(i);
            
            ead.markUpdate();
        }
    }
    
    public void view() throws Exception
    {
        final String entity_id = (String)getAttribute("outid");
        entity=getRemoteEntityManager().loadEntity(entity_id);
        
        if (entity==null) throw new RuntimeException("Entity not found !");
        
        setReadOnly(true);
    }
    
    public void afterUpdateForm()
    {
      /*if (entity!=null) {
         final Integer idx = NumberUtil.getInteger(stSelectedAddress);
         address = idx==null?null:(OutcomingDistributionView) entity.getDistributions().get(idx.intValue());
       
         final DTOList addresses = entity.getDistributions();
       
         entity.setStReceiver(null);
       
         for (int i = 0; i < addresses.size(); i++) {
            OutcomingDistributionView adr = (OutcomingDistributionView) addresses.get(i);
       
            if (entity.getStReceiver()==null)
               entity.setStReceiver(adr.getStReceiver());
       
         }
      }*/
    }
    
    public String getStSelectedAddress()
    {
        return stSelectedAddress;
    }
    
    public void setStSelectedAddress(String stSelectedAddress)
    {
        this.stSelectedAddress = stSelectedAddress;
    }
    
    public DTOList getAddress()
    {
        return entity.getDistributions();
    }
    
    public void setAddress(DTOList address)
    {
        this.address = address;
    }
    
    
    
    public void doNewAddress()
    {
        final OutcomingDistributionView adr = new OutcomingDistributionView();
        
        adr.markNew();
        
        entity.getDistributions().add(adr);
        
        //stSelectedAddress = String.valueOf(entity.getDistributions().size()-1);
        
        //address = adr;
    }
    
    public void selectAddress()
    {
        // do nothing
    }
    
    public FormTab getTabs()
    {
        if (tabs==null)
        {
            tabs = new FormTab();
            
            tabs.add(new FormTab.TabBean("TAB0","{L-ENG LETTER-L}{L-INA SURAT-L}",true));
            tabs.add(new FormTab.TabBean("TAB1","{L-ENG RECEIVER-L}{L-INA PENERIMA-L}",true));
            tabs.add(new FormTab.TabBean("TAB2","{L-ENG ATTACHMENT-L}{L-INA LAMPIRAN-L}",true));
            
            tabs.setActiveTab("TAB0");
            
        }
        return tabs;
    }
    
    public void setTabs(FormTab tabs)
    {
        this.tabs = tabs;
    }
    
    public LOV getLovPaymentTerm() throws Exception
    {
        if (lovPaymentTerm==null) lovPaymentTerm = ListUtil.getLookUpFromQuery("select payment_term_id,description from payment_term");
        return lovPaymentTerm;
    }
    
    public void setLovPaymentTerm(LOV lovPaymentTerm)
    {
        this.lovPaymentTerm = lovPaymentTerm;
    }
    
    public OutcomingMasterForm()
    {
    }
    
    public OutcomingView getEntity()
    {
        return entity;
    }
    
    public void setEntity(OutcomingView entity)
    {
        this.entity = entity;
    }
    
    private OutcomingManager getRemoteEntityManager() throws NamingException, ClassNotFoundException, CreateException, RemoteException
    {
        return ((OutcomingManagerHome) JNDIUtil.getInstance().lookup("OutcomingManagerEJB",OutcomingManagerHome.class.getName()))
        .create();
    }
    
    private IncomingManager getRemoteEntityManager2() throws NamingException, ClassNotFoundException, CreateException, RemoteException
    {
        return ((IncomingManagerHome) JNDIUtil.getInstance().lookup("IncomingManagerEJB",IncomingManagerHome.class.getName()))
        .create();
    }
    
    public void doSave() throws Exception
    {
        UserSession us = SessionManager.getInstance().getSession();
        
        entity.setStSender(us.getStUserID());
        entity.setStRefNo(getRemoteEntityManager().generateRefNo());
        entity.setStDeleteFlag("N");
        final OutcomingView cloned = (OutcomingView)ObjectCloner.deepCopy(entity);
        
        if(getAddress().size()==0) throw new RuntimeException("Penerima Belum Dipilih");
        
        final DTOList addresses = cloned.getDistributions();
        
        if(addresses==null) throw new RuntimeException("Penerima Belum Dipilih");
        
        String receiverName = "";
        for (int i = 0; i < addresses.size(); i++)
        {
            OutcomingDistributionView adr = (OutcomingDistributionView) addresses.get(i);
            
            if (adr.getStReceiver()==null)
                throw new RuntimeException("Penerima Belum Dipilih");

            if(addresses.size()>1 && i>0) receiverName = receiverName + "; ";
            
            receiverName = receiverName + adr.getUser(adr.getStReceiver()).getStUserName();
            
        }
        if(receiverName.endsWith(";")) receiverName = receiverName.substring(0,receiverName.lastIndexOf(";"));
        cloned.setStReceiver(receiverName);
        
        getRemoteEntityManager().save(cloned);
        
        final DTOList distribut = cloned.getDistributions();
        
        for(int i=0;i<distribut.size();i++)
        {
            IncomingView incoming = new IncomingView();
            
            OutcomingDistributionView distView = (OutcomingDistributionView) distribut.get(i);
            
            incoming.setStRefNo(cloned.getStRefNo());
            incoming.setStSender(us.getStUserID());
            incoming.setStSenderName(us.getStUserName());
            incoming.setDtLetterDate(cloned.getDtLetterDate());
            incoming.setStSubject(cloned.getStSubject());
            incoming.setStNote(cloned.getStNote());
            incoming.setStReceiver(distView.getStReceiver());
            incoming.setStFilePhysic(cloned.getStFilePhysic());
            incoming.setStReadFlag("N");
            incoming.setStDeleteFlag("N");
            incoming.setStCC(receiverName);
            
            final DTOList documents = cloned.getDocuments();
            for(int j=0;j<documents.size();j++)
            {
                OutcomingDocumentsView doc = (OutcomingDocumentsView) documents.get(j);
                
                IncomingDocumentsView docIn = new IncomingDocumentsView();
                
                docIn.markNew();
                
                if(doc.getStFilePhysic()==null)
                    throw new RuntimeException("File Lampiran Belum di Upload");
                
                docIn.setStFilePhysic(doc.getStFilePhysic());
                
                incoming.getDocuments().add(docIn);
            }
            
            incoming.markNew();
            getRemoteEntityManager2().save(incoming);
        }
        
        
        super.close();
    }
    
    public void doSave2() throws Exception
    {
        UserSession us = SessionManager.getInstance().getSession();
        
        entity.setStSender(us.getStUserID());
        entity.setStRefNo(getRemoteEntityManager().generateRefNo());
        entity.setStDeleteFlag("N");
        final OutcomingView cloned = (OutcomingView)ObjectCloner.deepCopy(entity);
        
        final DTOList addresses = entity.getDistributions();
        
        if(addresses.size()==0) throw new RuntimeException("Penerima Belum Dipilih");
        
        String receiverName = "";
        for (int i = 0; i < addresses.size(); i++)
        {
            OutcomingDistributionView adr = (OutcomingDistributionView) addresses.get(i);
            
            if (adr.getStReceiver()==null)
                throw new RuntimeException("Penerima Belum Dipilih");

            if(addresses.size()>1  && i>0) receiverName = receiverName + "; ";
            
            receiverName = receiverName + adr.getUser(adr.getStReceiver()).getStUserName();
            
        }
        if(receiverName.endsWith(";")) receiverName = receiverName.substring(0,receiverName.lastIndexOf(";"));
        cloned.setStReceiver(receiverName);
        
        getRemoteEntityManager().save(cloned);
        
        final DTOList distribut = cloned.getDistributions();
        
        for(int i=0;i<distribut.size();i++)
        {
            IncomingView incoming = new IncomingView();
            
            OutcomingDistributionView distView = (OutcomingDistributionView) distribut.get(i);
            
            incoming.setStRefNo(cloned.getStRefNo());
            incoming.setStSender(us.getStUserID());
            incoming.setStSenderName(us.getStUserName());
            incoming.setDtLetterDate(cloned.getDtLetterDate());
            incoming.setStSubject(cloned.getStSubject());
            incoming.setStNote(cloned.getStNote());
            incoming.setStReceiver(distView.getStReceiver());
            incoming.setStFilePhysic(cloned.getStFilePhysic());
            incoming.setStReadFlag("N");
            incoming.setStDeleteFlag("N");
            
            final DTOList documents = cloned.getDocuments();
            for(int j=0;j<documents.size();j++)
            {
                OutcomingDocumentsView doc = (OutcomingDocumentsView) documents.get(j);
                
                IncomingDocumentsView docIn = new IncomingDocumentsView();
                
                docIn.markNew();
                
                if(doc.getStFilePhysic()==null)
                    throw new RuntimeException("File Lampiran Belum di Upload");
                
                docIn.setStFilePhysic(doc.getStFilePhysic());
                
                incoming.getDocuments().add(docIn);
            }
            
            incoming.markNew();
            getRemoteEntityManager2().save(incoming);
        }
        
        
        super.close();
        
        final IncomingListForm form = (IncomingListForm) super.newForm("incominglist",this);
        
        form.show();
        
        
    }
    
    public void doDelete() throws Exception
    {
        //getRemoteEntityManager().
    }
    
    public void doClose()
    {
        super.close();
    }
    
    public void forward() throws Exception
    {
        view();
        
        setReadOnly(false);
        
        entity.markNew();
        
        entity.getDistributions().deleteAll();
        
    }
    
    public void replyForward(IncomingView incoming)
    {
        entity = new OutcomingView();
        entity.markNew();
        
        entity.setStFilePhysic(incoming.getStFilePhysic());
        entity.setDtLetterDate(new Date());
        entity.setStSubject("Re: "+incoming.getStSubject());
        entity.setStNote(incoming.getStNote()+"\n");
        entity.setStNote(entity.getStNote()+"-------------------------------------------------------------------------\n");
        
        final DTOList document = incoming.getDocuments();
        for(int i=0; i<document.size(); i++)
        {
            IncomingDocumentsView doc = (IncomingDocumentsView) document.get(i);
            
            OutcomingDocumentsView docOut = new OutcomingDocumentsView();
            docOut.markNew();
            
            docOut.setStFilePhysic(doc.getStFilePhysic());
            
            entity.getDocuments().add(docOut);
        }
    }
    
    public LOV getLovDocuments()
    {
        return entity.getDocuments();
    }
    
    public String getStSelectedDocument()
    {
        return stSelectedDocument;
    }
    
    public void setStSelectedDocument(String stSelectedDocument)
    {
        this.stSelectedDocument = stSelectedDocument;
    }
    
    public void selectDocument()
    {
        // do nothing
    }
    
    public void doNewDocument()
    {
        final OutcomingDocumentsView adr = new OutcomingDocumentsView();
        
        adr.markNew();
        
        entity.getDocuments().add(adr);
        
        //stSelectedDocument = String.valueOf(entity.getDocuments().size()-1);
        
    }
    
    public void doDeleteDocument()
    {
        
        //int n = Integer.parseInt(stSelectedDocument);
        
        entity.getDocuments().delete(Integer.parseInt(instIndex));
        
        //stSelectedDocument=null;
        
    }
    
    public DTOList getDocument() throws Exception
    {
        return entity.getDocuments();
    }
    
    public void setDocument(DTOList document)
    {
        this.document = document;
    }
    
    public String getInstIndex()
    {
        return instIndex;
    }
    
    public void setInstIndex(String instIndex)
    {
        this.instIndex = instIndex;
    }
    
    public String getAddressIndex()
    {
        return addressIndex;
    }
    
    public void setAddressIndex(String addressIndex)
    {
        this.addressIndex = addressIndex;
    }
    
}

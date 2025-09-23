package com.webfin.incoming.ejb;

import com.webfin.incoming.model.IncomingView;
import com.webfin.incoming.filter.IncomingFilter;
import com.crux.util.DTOList;
import com.webfin.approval.model.ApprovalView;
import com.webfin.incoming.model.ApprovalBODView;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Opah
 * Date: Nov 9, 2005
 * Time: 11:28:50 PM
 * To change this template use File | Settings | File Templates.
 */

public interface IncomingManager extends EJBObject {
   String save(IncomingView entityView)  throws Exception, RemoteException;

   DTOList listEntities(IncomingFilter f) throws Exception, RemoteException;

   IncomingView loadEntity(String entity_id) throws Exception, RemoteException;
   
   void updateReadStatus(IncomingView entityView,String readStatus) throws Exception, RemoteException;

   String saveApprove(ApprovalView entityView)  throws Exception, RemoteException;

   ApprovalView loadApprove(String entity_id) throws Exception, RemoteException;

   String save2(ApprovalBODView entityView)  throws Exception, RemoteException;

   ApprovalBODView loadEntity2(String entity_id) throws Exception, RemoteException;

   void updateReadStatus2(ApprovalBODView entityView,String readStatus) throws Exception, RemoteException;

   void updateApproval(ApprovalBODView entityView,String approval) throws Exception, RemoteException;

}

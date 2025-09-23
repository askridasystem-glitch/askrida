package com.webfin.outcoming.ejb;

import com.webfin.outcoming.model.OutcomingView;
import com.webfin.outcoming.filter.OutcomingFilter;
import com.crux.util.DTOList;
import com.webfin.outcoming.model.UploadBODView;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Opah
 * Date: Nov 9, 2005
 * Time: 11:28:50 PM
 * To change this template use File | Settings | File Templates.
 */

public interface OutcomingManager extends EJBObject {
   String save(OutcomingView entityView)  throws Exception, RemoteException;

   DTOList listEntities(OutcomingFilter f) throws Exception, RemoteException;

   OutcomingView loadEntity(String entity_id) throws Exception, RemoteException;
   
   String generateRefNo() throws Exception, RemoteException;

   UploadBODView loadEntity2(String entity_id) throws Exception, RemoteException;

   String save2(UploadBODView entityView)  throws Exception, RemoteException;

}

package com.webfin.approval.ejb;

import com.webfin.incoming.model.IncomingView;
import com.webfin.incoming.filter.IncomingFilter;
import com.crux.util.DTOList;

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
}

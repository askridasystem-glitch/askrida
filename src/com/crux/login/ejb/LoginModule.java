/***********************************************************************
 * Module:  com.crux.login.ejb.LoginModule
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 10:54:49 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.login.ejb;

import com.crux.util.DTOList;
import com.crux.common.model.UserSession;
import com.crux.login.model.UserSessionView;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

public interface LoginModule extends EJBObject {
   DTOList getAllMenu() throws RemoteException, Exception;

   DTOList getAllResources() throws Exception, RemoteException;

   UserSession getUser(String stUserID, String stPassword) throws Exception, RemoteException;

   DTOList getMenu(String stUserID) throws Exception, RemoteException;

   void logActivity(UserSessionView uv, String s, String ref1) throws Exception, RemoteException;
}

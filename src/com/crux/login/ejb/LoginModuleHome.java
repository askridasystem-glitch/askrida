/***********************************************************************
 * Module:  com.crux.login.ejb.LoginModuleHome
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 10:54:47 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.login.ejb;

import javax.ejb.EJBHome;

public interface LoginModuleHome extends EJBHome {
   LoginModule create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

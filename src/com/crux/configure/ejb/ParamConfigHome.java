/***********************************************************************
 * Module:  com.crux.configure.ejb.ParamConfigHome
 * Author:  Denny Mahendra
 * Created: Jun 16, 2004 2:31:51 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.configure.ejb;

import javax.ejb.EJBHome;

public interface ParamConfigHome extends EJBHome {
   ParamConfig create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

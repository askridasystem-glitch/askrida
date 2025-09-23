/***********************************************************************
 * Module:  com.crux.login.ejb.UserMaintenanceHome
 * Author:  Denny Mahendra
 * Created: Apr 22, 2004 8:36:36 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.login.ejb;

import javax.ejb.EJBHome;

public interface UserMaintenanceHome extends EJBHome {
   UserMaintenance create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

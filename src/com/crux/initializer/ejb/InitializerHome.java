/***********************************************************************
 * Module:  com.crux.initializer.ejb.InitializerHome
 * Author:  Denny Mahendra
 * Created: Sep 2, 2004 2:00:21 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.initializer.ejb;

import javax.ejb.EJBHome;

public interface InitializerHome extends EJBHome {
   Initializer create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

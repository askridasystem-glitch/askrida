/***********************************************************************
 * Module:  com.crux.initializer.ejb.Initializer
 * Author:  Denny Mahendra
 * Created: Sep 2, 2004 2:00:23 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.initializer.ejb;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

public interface Initializer extends EJBObject {
   void initializeDataSource(String stDataSourceName) throws Exception, RemoteException;
}

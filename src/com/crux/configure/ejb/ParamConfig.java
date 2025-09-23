/***********************************************************************
 * Module:  com.crux.configure.ejb.ParamConfig
 * Author:  Denny Mahendra
 * Created: Jun 16, 2004 2:31:52 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.configure.ejb;

import com.crux.util.DTOList;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

public interface ParamConfig extends EJBObject {
   DTOList getParams(String stParamGroupID) throws Exception, RemoteException;

   void saveParams(DTOList l) throws Exception, RemoteException;
}

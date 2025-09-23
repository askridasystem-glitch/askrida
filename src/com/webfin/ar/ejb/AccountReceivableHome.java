package com.webfin.ar.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Opah
 * Date: Oct 11, 2005
 * Time: 10:09:24 PM
 * To change this template use File | Settings | File Templates.
 */

public interface AccountReceivableHome extends EJBHome {
   com.webfin.ar.ejb.AccountReceivable create() throws RemoteException, CreateException;
}

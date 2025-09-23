package com.webfin.gl.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Opah
 * Date: Jul 18, 2005
 * Time: 5:43:03 AM
 * To change this template use File | Settings | File Templates.
 */

public interface GeneralLedgerHome extends EJBHome {
    com.webfin.gl.ejb.GeneralLedger create() throws RemoteException, CreateException;
}

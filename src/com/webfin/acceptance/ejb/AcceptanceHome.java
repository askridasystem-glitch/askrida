package com.webfin.acceptance.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Opah
 * Date: Oct 9, 2005
 * Time: 9:59:08 PM
 * To change this template use File | Settings | File Templates.
 */

public interface AcceptanceHome extends EJBHome {
   com.webfin.acceptance.ejb.Acceptance create() throws RemoteException, CreateException;
}

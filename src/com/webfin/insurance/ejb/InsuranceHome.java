package com.webfin.insurance.ejb;

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

public interface InsuranceHome extends EJBHome {
   com.webfin.insurance.ejb.Insurance create() throws RemoteException, CreateException;
}

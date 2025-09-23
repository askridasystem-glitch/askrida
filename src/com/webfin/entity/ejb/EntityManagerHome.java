package com.webfin.entity.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Opah
 * Date: Nov 9, 2005
 * Time: 11:28:50 PM
 * To change this template use File | Settings | File Templates.
 */

public interface EntityManagerHome extends EJBHome {
   com.webfin.entity.ejb.EntityManager create() throws RemoteException, CreateException;
}

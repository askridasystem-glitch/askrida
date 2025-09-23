package com.webfin.contact.ejb;

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

public interface ContactManagerHome extends EJBHome {
   com.webfin.contact.ejb.ContactManager create() throws RemoteException, CreateException;
}

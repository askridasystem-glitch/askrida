/**
 * Created by IntelliJ IDEA.
 * User: Andi Adhyaksa
 * Date: Nov 22, 2004
 * Time: 4:17:20 PM
 * To change this template use Options | File Templates.
 */
package com.webfin.postalcode.ejb;

import javax.ejb.EJBHome;

public interface PostalCodeHome extends EJBHome{
    PostalCode create() throws javax.ejb.CreateException,java.rmi.RemoteException;
}

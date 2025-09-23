package com.webfin.contact.ejb;

import com.webfin.contact.model.ContactView;
import com.webfin.contact.filter.ContactFilter;
import com.crux.util.DTOList;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Opah
 * Date: Nov 9, 2005
 * Time: 11:28:50 PM
 * To change this template use File | Settings | File Templates.
 */

public interface ContactManager extends EJBObject {
   String save(ContactView entityView)  throws Exception, RemoteException;

   DTOList listEntities(ContactFilter f) throws Exception, RemoteException;

   ContactView loadEntity(String entity_id) throws Exception, RemoteException;
}

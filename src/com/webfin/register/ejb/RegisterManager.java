package com.webfin.register.ejb;

import com.webfin.register.model.RegisterView;
import com.webfin.register.filter.RegisterFilter;
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

public interface RegisterManager extends EJBObject {
   String save(RegisterView entityView)  throws Exception, RemoteException;

   DTOList listEntities(RegisterFilter f) throws Exception, RemoteException;

   RegisterView loadEntity(String entity_id) throws Exception, RemoteException;
   
   String generateRefNo() throws Exception, RemoteException;
}

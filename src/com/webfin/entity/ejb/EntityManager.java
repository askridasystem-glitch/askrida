package com.webfin.entity.ejb;

import com.webfin.entity.model.EntityView;
import com.webfin.entity.filter.EntityFilter;
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

public interface EntityManager extends EJBObject {
   String save(EntityView entityView)  throws Exception, RemoteException;

   DTOList listEntities(EntityFilter f) throws Exception, RemoteException;

   EntityView loadEntity(String entity_id) throws Exception, RemoteException;

   String saveAgent(EntityView entityView) throws Exception, RemoteException;

}

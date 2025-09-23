package com.webfin.archive.ejb;

import com.webfin.archive.model.ArchiveView;
import com.webfin.archive.filter.ArchiveFilter;
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

public interface ArchiveManager extends EJBObject {
   String save(ArchiveView entityView)  throws Exception, RemoteException;

   DTOList listEntities(ArchiveFilter f) throws Exception, RemoteException;

   ArchiveView loadEntity(String entity_id) throws Exception, RemoteException;
   
   void updateReadStatus(ArchiveView entityView,String readStatus) throws Exception, RemoteException;
}

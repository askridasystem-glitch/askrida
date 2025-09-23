package com.webfin.company.ejb;

import com.webfin.company.model.CompanyView;
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

public interface CompanyManager extends EJBObject {
   String save(CompanyView company_View)  throws Exception, RemoteException;

   DTOList listCompany(EntityFilter f) throws Exception, RemoteException;

   CompanyView loadCompany(String entity_id) throws Exception, RemoteException;
}

/***********************************************************************
 * Module:  com.crux.initializer.ejb.InitializerEJB
 * Author:  Denny Mahendra
 * Created: Sep 2, 2004 2:00:23 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.initializer.ejb;

import com.crux.common.config.DataSourceNames;
import com.crux.util.LogManager;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

public class InitializerEJB implements SessionBean {
   public InitializerEJB() {
   }

   private final static transient LogManager logger = LogManager.getInstance(InitializerEJB.class);

   public void initializeDataSource(String stDataSourceName) throws Exception {

   }

   public void ejbCreate() throws javax.ejb.CreateException {
   }

   public void ejbActivate() throws EJBException {
   }

   public void ejbPassivate() throws EJBException {
   }

   public void ejbRemove() throws EJBException {
   }

   public void setSessionContext(SessionContext sessionContext) throws EJBException {
   }
}

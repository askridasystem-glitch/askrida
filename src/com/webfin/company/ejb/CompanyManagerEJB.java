package com.webfin.company.ejb;

import com.webfin.company.model.CompanyView;
import com.webfin.entity.model.EntityAddressView;
import com.webfin.entity.filter.EntityFilter;

import com.crux.util.*;

import com.webfin.company.model.*;


import javax.ejb.SessionBean;
import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import javax.ejb.EJBException;

/**
 * Created by IntelliJ IDEA.
 * User: Opah
 * Date: Nov 9, 2005
 * Time: 11:28:50 PM
 * To change this template use File | Settings | File Templates.
 */

public class CompanyManagerEJB implements SessionBean {
   public SessionContext ctx;

   private final static transient LogManager logger = LogManager.getInstance(CompanyManagerEJB.class);

   public CompanyManagerEJB() {
   }

   public void ejbCreate() throws CreateException {
   }

   public void setSessionContext(SessionContext sessionContext) throws EJBException {
      ctx = sessionContext;
   }

   public void ejbRemove() throws EJBException {
   }

   public void ejbActivate() throws EJBException {
   }

   public void ejbPassivate() throws EJBException {
   }

   public String save(CompanyView companyView) throws Exception {
      logger.logDebug("save: "+companyView);
      final SQLUtil S = new SQLUtil();
      try {
         if (companyView.isNew()) companyView.setStVSCode(String.valueOf(IDFactory.createNumericID("COMPANY_GROUP")));

         S.store(companyView);

         return companyView.getStVSCode();

      } catch (Exception e) {
         ctx.setRollbackOnly();
         throw e;
      } finally {
         S.release();
      }
   }

   public DTOList listCompany(EntityFilter f) throws Exception {
      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");
      sqa.addQuery(" from s_company_group");

      sqa.addFilter(f);

      final DTOList l = sqa.getList(CompanyView.class);
      
      return l;
   }

   public CompanyView loadCompany(String entity_id) throws Exception {
      final CompanyView ent = (CompanyView)ListUtil.getDTOListFromQuery(
                    "select * from s_company_group where vs_code = ?",
                    new Object [] {entity_id},
                    CompanyView.class
            ).getDTO();


      return ent;
   }
}

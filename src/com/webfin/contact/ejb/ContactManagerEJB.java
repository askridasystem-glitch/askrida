package com.webfin.contact.ejb;

import com.webfin.contact.model.ContactView;
import com.webfin.contact.model.ContactAddressView;
import com.webfin.contact.filter.ContactFilter;
//import com.webfin.insurance.model.InsurancePolicyEntityView;
//import com.webfin.insurance.model.InsuranceEntityView;
import com.crux.util.*;

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

public class ContactManagerEJB implements SessionBean {
   public SessionContext ctx;

   private final static transient LogManager logger = LogManager.getInstance(ContactManagerEJB.class);

   public ContactManagerEJB() {
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

   public String save(ContactView entityView) throws Exception {
      logger.logDebug("save: "+entityView);
      final SQLUtil S = new SQLUtil();
      try {
         if (entityView.isNew()) entityView.setStEntityID(String.valueOf(IDFactory.createNumericID("ENTTTY")));

         S.store(entityView);

         final DTOList addresses = entityView.getAddresses();

         for (int i = 0; i < addresses.size(); i++) {
            ContactAddressView adr = (ContactAddressView) addresses.get(i);

            if (adr.isNew()) adr.setStEntityAddressID(String.valueOf(IDFactory.createNumericID("ENTTTYADR")));

            adr.setStEntityID(entityView.getStEntityID());
         }

         S.store(addresses);

         return entityView.getStEntityID();

      } catch (Exception e) {
         ctx.setRollbackOnly();
         throw e;
      } finally {
         S.release();
      }
   }

   public DTOList listEntities(ContactFilter f) throws Exception {
      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");
      sqa.addQuery(" from customer_master");

      sqa.addFilter(f);

      final DTOList l = sqa.getList(ContactView.class);
      
      return l;
   }

   public ContactView loadEntity(String entity_id) throws Exception {
      final ContactView ent = (ContactView)ListUtil.getDTOListFromQuery(
                    "select * from customer_master where ent_id = ?",
                    new Object [] {entity_id},
                    ContactView.class
            ).getDTO();

      if (ent!=null) {
         ent.setAddresses(
                 ListUtil.getDTOListFromQuery(
                         "select * from customer_address where ent_id = ?",
                         new Object [] {entity_id},
                         ContactAddressView.class
                 )
         );
      }

      return ent;
   }
}

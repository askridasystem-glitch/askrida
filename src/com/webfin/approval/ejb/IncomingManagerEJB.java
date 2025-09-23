package com.webfin.approval.ejb;

import com.webfin.incoming.model.IncomingDocumentsView;
import com.webfin.incoming.model.IncomingView;
import com.webfin.incoming.model.IncomingDistributionView;
import com.webfin.incoming.filter.IncomingFilter;
import com.webfin.insurance.model.InsurancePolicyEntityView;
import com.webfin.insurance.model.InsuranceEntityView;
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

public class IncomingManagerEJB implements SessionBean {
   public SessionContext ctx;

   private final static transient LogManager logger = LogManager.getInstance(IncomingManagerEJB.class);

   public IncomingManagerEJB() {
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

   public String save(IncomingView entityView) throws Exception {
      //logger.logDebug("save: "+entityView);
      final SQLUtil S = new SQLUtil();
      try {
         if (entityView.isNew()) entityView.setStInID(String.valueOf(IDFactory.createNumericID("INCOMING")));

         S.store(entityView);

         final DTOList addresses = entityView.getDistributions();

         for (int i = 0; i < addresses.size(); i++) {
            IncomingDistributionView adr = (IncomingDistributionView) addresses.get(i);

            if (adr.isNew()) adr.setStIdDist(String.valueOf(IDFactory.createNumericID("DISTRIBUTION")));

            adr.setStInID(entityView.getStInID());
         }

         S.store(addresses);
         
         final DTOList documents = entityView.getDocuments();

         for (int j = 0; j < documents.size(); j++) {
            IncomingDocumentsView doc = (IncomingDocumentsView) documents.get(j);

            if (doc.isNew()) doc.setStDocumentInID(String.valueOf(IDFactory.createNumericID("INDOCUMENT")));

            doc.setStInID(entityView.getStInID());
         }

         S.store(documents);


         return entityView.getStInID();

      } catch (Exception e) {
         ctx.setRollbackOnly();
         throw e;
      } finally {
         S.release();
      }
   }
   
   public void updateReadStatus(IncomingView entityView,String readStatus) throws Exception {
      //logger.logDebug("update status read ");
      final SQLUtil S = new SQLUtil();
      try {
         //if (entityView.isNew()) entityView.setStInID(String.valueOf(IDFactory.createNumericID("INCOMING")));
		 
		 entityView.setStReadFlag(readStatus);
		 entityView.markUpdate();
		 
         S.store(entityView);

//         final DTOList addresses = entityView.getDistributions();
//
//         for (int i = 0; i < addresses.size(); i++) {
//            IncomingDistributionView adr = (IncomingDistributionView) addresses.get(i);
//
//            if (adr.isNew()) adr.setStIdDist(String.valueOf(IDFactory.createNumericID("DISTRIBUTION")));
//
//            adr.setStInID(entityView.getStInID());
//         }
//
//         S.store(addresses);

      } catch (Exception e) {
         ctx.setRollbackOnly();
         throw e;
      } finally {
         S.release();
      }
   }

   public DTOList listEntities(IncomingFilter f) throws Exception {
      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");
      sqa.addQuery(" from incoming_letter");

      sqa.addFilter(f);

      final DTOList l = sqa.getList(IncomingView.class);
      
      return l;
   }

   public IncomingView loadEntity(String entity_id) throws Exception {
      final IncomingView ent = (IncomingView)ListUtil.getDTOListFromQuery(
                    "select * from incoming_letter where in_id = ?",
                    new Object [] {entity_id},
                    IncomingView.class
            ).getDTO();

      if (ent!=null) {
         ent.setDistributions(
                 ListUtil.getDTOListFromQuery(
                         "select * from incoming_dist where in_id = ? and delete_flag is null",
                         new Object [] {entity_id},
                         IncomingDistributionView.class
                 )
         );
         
         ent.setDocuments(
                 ListUtil.getDTOListFromQuery(
                         "select * from incoming_documents where in_id = ?",
                         new Object [] {entity_id},
                         IncomingDocumentsView.class
                 )
         );
      }

      return ent;
   }
   
}

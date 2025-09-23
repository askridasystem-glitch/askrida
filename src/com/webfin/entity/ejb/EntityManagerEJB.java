package com.webfin.entity.ejb;

import com.webfin.entity.model.EntityView;
import com.webfin.entity.model.EntityAddressView;
import com.webfin.entity.filter.EntityFilter;
import com.webfin.insurance.model.InsurancePolicyEntityView;
import com.webfin.insurance.model.InsuranceEntityView;
import com.crux.util.*;
import com.webfin.entity.model.EntityDocumentView;

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

public class EntityManagerEJB implements SessionBean {
   public SessionContext ctx;

   private final static transient LogManager logger = LogManager.getInstance(EntityManagerEJB.class);

   public EntityManagerEJB() {
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

   public String save(EntityView entityView) throws Exception {
      //logger.logDebug("save: "+entityView);
      final SQLUtil S = new SQLUtil();
      try {
         if (entityView.isNew()){
             entityView.setStEntityID(String.valueOf(IDFactory.createNumericID("ENTTTY")));

             if(entityView.getStReferenceEntityID()!=null){
                 entityView.setStGLCode(loadEntity(entityView.getStReferenceEntityID()).getStGLCode());
             }
             
             if(entityView.getStGLCode()==null) entityView.makeGLCOde();
         }

        if(entityView.getStReferenceEntityID()!=null){
             entityView.setStGLCode(loadEntity(entityView.getStReferenceEntityID()).getStGLCode());
        }

        if(entityView.getStGLCode()==null)  throw new RuntimeException("GL CODE tidak boleh kosong, hub. admin jika terjadi error ini");
         
         S.store(entityView);

         final DTOList addresses = entityView.getAddresses();

         for (int i = 0; i < addresses.size(); i++) {
            EntityAddressView adr = (EntityAddressView) addresses.get(i);

            if (adr.isNew()) adr.setStEntityAddressID(String.valueOf(IDFactory.createNumericID("ENTTTYADR")));

            adr.setStEntityID(entityView.getStEntityID());
         }

         S.store(addresses);

         final DTOList entityDocuments = entityView.getEntityDocuments();

        for (int i = 0; i < entityDocuments.size(); i++) {
            EntityDocumentView doc = (EntityDocumentView) entityDocuments.get(i);

            doc.setStEntityID(entityView.getStEntityID());

            final boolean marked = doc.isMarked();

            if (marked) {
                if (doc.getStEntityDocumentID() != null)
                    doc.markUpdate();
                else {
                    doc.setStEntityDocumentID(String.valueOf(IDFactory.createNumericID("ENTDOC")));
                    doc.markNew();
                }
            }

            if (!marked && doc.getStEntityDocumentID() != null) doc.markDelete();
        }

        S.store(entityDocuments);

         return entityView.getStEntityID();

      } catch (Exception e) {
         ctx.setRollbackOnly();
         throw e;
      } finally {
         S.release();
      }
   }

   public DTOList listEntities(EntityFilter f) throws Exception {
      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");
      sqa.addQuery(" from ent_master");

      sqa.addFilter(f);

      final DTOList l = sqa.getList(EntityView.class);
      
      return l;
   }

   public EntityView loadEntity(String entity_id) throws Exception {
      final EntityView ent = (EntityView)ListUtil.getDTOListFromQuery(
                    "select * from ent_master where ent_id = ?",
                    new Object [] {entity_id},
                    EntityView.class
            ).getDTO();

      if (ent!=null) {
         ent.setAddresses(
                 ListUtil.getDTOListFromQuery(
                         "select * from ent_address where ent_id = ?",
                         new Object [] {entity_id},
                         EntityAddressView.class
                 )
         );

      }

      return ent;
   }

   public String saveAgent(EntityView entityView) throws Exception {
        //logger.logDebug("save: "+entityView);
        final SQLUtil S = new SQLUtil();
        try {
            if (entityView.isNew()) {
                entityView.setStEntityID(String.valueOf(IDFactory.createNumericID("ENTTTY")));

                if (entityView.getStReferenceEntityID() != null) {
                    entityView.setStGLCode(loadEntity(entityView.getStReferenceEntityID()).getStGLCode());
                }

                if (entityView.getStGLCode() == null) {
                    entityView.makeGLCOde();
                }
            }
/*
            entityView.setStShareLevel1(entityView.getStCostCenterCode());
            entityView.setStRef1("39");
            entityView.setStRef2("230");
 */

            if (entityView.getStReferenceEntityID() != null) {
                entityView.setStGLCode(loadEntity(entityView.getStReferenceEntityID()).getStGLCode());
            }

            if (entityView.getStGLCode() == null) {
                throw new RuntimeException("GL CODE tidak boleh kosong, hub. admin jika terjadi error ini");
            }

            S.store(entityView);

            final DTOList addresses = entityView.getAddresses();

            for (int i = 0; i < addresses.size(); i++) {
                EntityAddressView adr = (EntityAddressView) addresses.get(i);

                if (adr.isNew()) {
                    adr.setStEntityAddressID(String.valueOf(IDFactory.createNumericID("ENTTTYADR")));
                }

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

}

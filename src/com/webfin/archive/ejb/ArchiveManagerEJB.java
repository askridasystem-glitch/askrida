package com.webfin.archive.ejb;

import com.webfin.archive.model.ArchiveView;
//import com.webfin.archive.model.ArchiveDistributionView;
import com.webfin.archive.filter.ArchiveFilter;
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

public class ArchiveManagerEJB implements SessionBean {
   public SessionContext ctx;

   private final static transient LogManager logger = LogManager.getInstance(ArchiveManagerEJB.class);

   public ArchiveManagerEJB() {
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

   public String save(ArchiveView entityView) throws Exception {
      //logger.logDebug("save: "+entityView);
      final SQLUtil S = new SQLUtil();
      try {
         if (entityView.isNew()) entityView.setStArchiveID(String.valueOf(IDFactory.createNumericID("ARCHIVE")));

         S.store(entityView);

         

         return entityView.getStArchiveID();

      } catch (Exception e) {
         ctx.setRollbackOnly();
         throw e;
      } finally {
         S.release();
      }
   }
   
   public void updateReadStatus(ArchiveView entityView,String readStatus) throws Exception {
      //logger.logDebug("update status read ");
      final SQLUtil S = new SQLUtil();
      try {
         //if (entityView.isNew()) entityView.setStInID(String.valueOf(IDFactory.createNumericID("INCOMING")));
		 
		 //entityView.setStReadFlag(readStatus);
		 entityView.markUpdate();
		 
         S.store(entityView);

//         final DTOList addresses = entityView.getDistributions();
//
//         for (int i = 0; i < addresses.size(); i++) {
//            ArchiveDistributionView adr = (ArchiveDistributionView) addresses.get(i);
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

   public DTOList listEntities(ArchiveFilter f) throws Exception {
      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");
      sqa.addQuery(" from archive_doc");

      sqa.addFilter(f);

      final DTOList l = sqa.getList(ArchiveView.class);
      
      return l;
   }

   public ArchiveView loadEntity(String entity_id) throws Exception {
      final ArchiveView ent = (ArchiveView)ListUtil.getDTOListFromQuery(
                    "select * from archive_doc where archive_id = ?",
                    new Object [] {entity_id},
                    ArchiveView.class
            ).getDTO();

      

      return ent;
   }
}

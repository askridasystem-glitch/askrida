package com.webfin.outcoming.ejb;

import com.webfin.outcoming.model.OutcomingDocumentsView;
import com.webfin.outcoming.model.OutcomingView;
import com.webfin.outcoming.model.OutcomingDistributionView;
import com.webfin.outcoming.filter.OutcomingFilter;
import com.webfin.insurance.model.InsurancePolicyEntityView;
import com.webfin.insurance.model.InsuranceEntityView;
import com.crux.util.*;
import com.webfin.outcoming.model.UploadBODDistributionView;
import com.webfin.outcoming.model.UploadBODDocumentsView;
import com.webfin.outcoming.model.UploadBODView;
import java.util.Date;

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

public class OutcomingManagerEJB implements SessionBean {
   public SessionContext ctx;

   private final static transient LogManager logger = LogManager.getInstance(OutcomingManagerEJB.class);

   public OutcomingManagerEJB() {
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

   public String save(OutcomingView entityView) throws Exception {
      //logger.logDebug("save: "+entityView);
      final SQLUtil S = new SQLUtil();
      try {
         if (entityView.isNew()) entityView.setStOutID(String.valueOf(IDFactory.createNumericID("OUTCOMING")));

         S.store(entityView);

         final DTOList addresses = entityView.getDistributions();

         for (int i = 0; i < addresses.size(); i++) {
            OutcomingDistributionView adr = (OutcomingDistributionView) addresses.get(i);

            if (adr.isNew()) adr.setStIdDist(String.valueOf(IDFactory.createNumericID("DISTRIBUTION2")));

            adr.setStOutID(entityView.getStOutID());
         }

         S.store(addresses);
         
         final DTOList documents = entityView.getDocuments();

         for (int j = 0; j < documents.size(); j++) {
            OutcomingDocumentsView doc = (OutcomingDocumentsView) documents.get(j);

            if (doc.isNew()) doc.setStDocumentOutID(String.valueOf(IDFactory.createNumericID("OUTDOCUMENT")));

            doc.setStOutID(entityView.getStOutID());
         }

         S.store(documents);

         return entityView.getStOutID();

      } catch (Exception e) {
         ctx.setRollbackOnly();
         throw e;
      } finally {
         S.release();
      }
   }

   public DTOList listEntities(OutcomingFilter f) throws Exception {
      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");
      sqa.addQuery(" from outcoming_letter");

      sqa.addFilter(f);

      final DTOList l = sqa.getList(OutcomingView.class);
      
      return l;
   }

   public OutcomingView loadEntity(String entity_id) throws Exception {
      final OutcomingView ent = (OutcomingView)ListUtil.getDTOListFromQuery(
                    "select * from outcoming_letter where out_id = ?",
                    new Object [] {entity_id},
                    OutcomingView.class
            ).getDTO();

      if (ent!=null) {
         ent.setDistributions(
                 ListUtil.getDTOListFromQuery(
                         "select * from outcoming_dist where out_id = ? and delete_flag is null",
                         new Object [] {entity_id},
                         OutcomingDistributionView.class
                 )
         );
         
         ent.setDocuments(
                 ListUtil.getDTOListFromQuery(
                         "select * from outcoming_documents where out_id = ? ",
                         new Object [] {entity_id},
                         OutcomingDocumentsView.class
                 )
         );
      }

      return ent;
   }
   
    public String generateRefNo() throws Exception {

	  String refNo="";
  
      String timeMark =
              DateUtil.getMonth2Digit(new Date())+
              DateUtil.getYear2Digit(new Date());

      String rn = String.valueOf(IDFactory.createNumericID("REFNO"));

      rn = Tools.getDigitRightJustified(rn,5);

      //no
      //  MAIL/0509/00001
      //  01234567890123

      refNo =
              "MAIL" +
              "/" +
              timeMark +
              "/" +
              rn;
              
      return refNo;

   }


   public UploadBODView loadEntity2(String entity_id) throws Exception {
      final UploadBODView ent = (UploadBODView)ListUtil.getDTOListFromQuery(
                    "select * from uploadbod_letter where out_id = ?",
                    new Object [] {entity_id},
                    UploadBODView.class
            ).getDTO();

      if (ent!=null) {
         ent.setDistributions(
                 ListUtil.getDTOListFromQuery(
                         "select * from uploadbod_dist where out_id = ? and delete_flag is null",
                         new Object [] {entity_id},
                         UploadBODDistributionView.class
                 )
         );

         ent.setDocuments(
                 ListUtil.getDTOListFromQuery(
                         "select * from uploadbod_documents where out_id = ? ",
                         new Object [] {entity_id},
                         UploadBODDocumentsView.class
                 )
         );
      }

      return ent;
   }

    public String save2(UploadBODView entityView) throws Exception {
        //logger.logDebug("save: "+entityView);
        final SQLUtil S = new SQLUtil();
        try {
            if (entityView.isNew()) {
                entityView.setStOutID(String.valueOf(IDFactory.createNumericID("UPLOADBOD")));
            }

            S.store(entityView);

            final DTOList addresses = entityView.getDistributions();

            for (int i = 0; i < addresses.size(); i++) {
                UploadBODDistributionView adr = (UploadBODDistributionView) addresses.get(i);

                if (adr.isNew()) {
                    adr.setStIdDist(String.valueOf(IDFactory.createNumericID("UPLOADBODDIST")));
                }

                adr.setStOutID(entityView.getStOutID());
            }

            S.store(addresses);

            final DTOList documents = entityView.getDocuments();

            for (int j = 0; j < documents.size(); j++) {
                UploadBODDocumentsView doc = (UploadBODDocumentsView) documents.get(j);

                if (doc.isNew()) {
                    doc.setStDocumentOutID(String.valueOf(IDFactory.createNumericID("UPLOADBODDOC")));
                }

                doc.setStOutID(entityView.getStOutID());
            }

            S.store(documents);

            return entityView.getStOutID();

        } catch (Exception e) {
            ctx.setRollbackOnly();
            throw e;
        } finally {
            S.release();
        }
    }

}

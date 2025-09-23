package com.webfin.register.ejb;

import com.webfin.register.model.RegisterView;
import com.webfin.register.filter.RegisterFilter;
import com.webfin.insurance.model.InsurancePolicyEntityView;
import com.webfin.insurance.model.InsuranceEntityView;
import com.crux.util.*;
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

public class RegisterManagerEJB implements SessionBean {
   public SessionContext ctx;

   private final static transient LogManager logger = LogManager.getInstance(RegisterManagerEJB.class);

   public RegisterManagerEJB() {
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

   public String save(RegisterView entityView) throws Exception {

      final SQLUtil S = new SQLUtil();
      try {
         if (entityView.isNew()) entityView.setStRegID(String.valueOf(IDFactory.createNumericID("REGISTER")));

         S.store(entityView);

         return entityView.getStRegID();

      } catch (Exception e) {
         ctx.setRollbackOnly();
         throw e;
      } finally {
         S.release();
      }
   }

   public DTOList listEntities(RegisterFilter f) throws Exception {
      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");
      sqa.addQuery(" from ins_pol_register");

      sqa.addFilter(f);

      final DTOList l = sqa.getList(RegisterView.class);
      
      return l;
   }

   public RegisterView loadEntity(String entity_id) throws Exception {
      final RegisterView ent = (RegisterView)ListUtil.getDTOListFromQuery(
                    "select * from ins_pol_register where reg_id = ?",
                    new Object [] {entity_id},
                    RegisterView.class
            ).getDTO();

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

}

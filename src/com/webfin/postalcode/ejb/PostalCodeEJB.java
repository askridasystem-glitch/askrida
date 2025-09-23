/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Nov 22, 2004
 * Time: 4:16:40 PM
 * To change this template use Options | File Templates.
 */
package com.webfin.postalcode.ejb;

import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.SQLUtil;
import com.webfin.postalcode.model.PostalCodeView;
import com.webfin.postalcode.filter.PostalCodeFilter;

import javax.ejb.SessionBean;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;


public class PostalCodeEJB implements SessionBean{
	private SessionContext ctx;
	
   public PostalCodeEJB(){}

	/*
   public DTOList ListValueSet(ValueSetFilter vf)throws Exception{
       return ListUtil.getDTOListFromQuery(" select distinct VS_GROUP " +
               " from s_value_set"+ ListUtil.getOrderExpression(vf),
               ValueSetView.class,vf
       );
   }

   public DTOList getValueSet(String stGroupName)throws Exception{
       return ListUtil.getDTOListFromQuery(
                "Select * from s_value_set where vs_group=? order by vs_order asc",
               new Object[]{stGroupName},
               ValueSetView.class
        );
   }*/

   public void save(PostalCodeView view)throws Exception{
        final SQLUtil s = new SQLUtil();
       try{
           s.store(view);
       }catch (Exception e) {
         ctx.setRollbackOnly();
         throw e;
       }
       finally{
           s.release();
       }
   }
   public  void setSessionContext(SessionContext sessionContext) throws EJBException{

   }
   public void ejbCreate() throws javax.ejb.CreateException {
   }
   public void ejbRemove() throws EJBException {
   }

   public void ejbActivate() throws EJBException {
   }

   public void ejbPassivate() throws EJBException {
   }
}

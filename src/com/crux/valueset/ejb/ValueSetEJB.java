/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Nov 22, 2004
 * Time: 4:16:40 PM
 * To change this template use Options | File Templates.
 */
package com.crux.valueset.ejb;

import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.SQLUtil;
import com.crux.valueset.model.ValueSetView;
import com.crux.valueset.filter.ValueSetFilter;

import javax.ejb.SessionBean;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;


public class ValueSetEJB implements SessionBean{
   public ValueSetEJB(){}

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
   }

   public void save(DTOList l)throws Exception{
        final SQLUtil s = new SQLUtil();
       try{
           s.store(l);
       }finally{
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

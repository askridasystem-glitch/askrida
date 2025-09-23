/***********************************************************************
 * Module:  com.crux.configure.ejb.ParamConfigEJB
 * Author:  Denny Mahendra
 * Created: Jun 16, 2004 2:31:52 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.configure.ejb;

import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.SQLUtil;
import com.crux.configure.model.ParameterView;
import com.crux.common.config.Config;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.sql.PreparedStatement;

public class ParamConfigEJB implements SessionBean {
   public ParamConfigEJB() {
   }

   public DTOList getParams(String stParamGroupID) throws Exception {
      return ListUtil.
            getDTOListFromQuery(
                  "select * from s_parameter where param_group = ? order by param_order, param_desc",
                  new Object [] {stParamGroupID},
                  ParameterView.class
            );
   }

   public void switchMachineID(String stBranchID) throws Exception {
      final SQLUtil S = new SQLUtil();

      try {
         final PreparedStatement PS = S.setQuery("update s_users set branchid = ?");

         PS.setString(1,stBranchID);

         PS.executeUpdate();
      }
      finally {
         S.release();
      }
   }

   public void saveParams(DTOList l) throws Exception {
      final SQLUtil S = new SQLUtil();

      try {

         for (int i = 0; i < l.size(); i++) {
            ParameterView par = (ParameterView) l.get(i);

            if (par.isModified())
               if (Config.PARID_PARAMETER_ID.equalsIgnoreCase(par.getStParamID())) {
                  switchMachineID(par.getStValueString());
               }
         }

         S.store(l);
      }
      finally {
         S.release();
      }
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

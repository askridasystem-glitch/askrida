/***********************************************************************
 * Module:  com.webfin.insurance.ejb.UserManager
 * Author:  Denny Mahendra
 * Created: Oct 25, 2006 12:34:52 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.acceptance.ejb;

import com.crux.util.SQLUtil;
import com.crux.web.controller.SessionManager;
import com.crux.login.model.UserSessionView;

import java.sql.ResultSet;
import java.math.BigDecimal;

public class UserManager {
   private static UserManager staticinstance;

   public static UserManager getInstance() {
      if (staticinstance == null) staticinstance = new UserManager();
      return staticinstance;
   }

   private UserManager() {
   }

   public UserSessionView getUser() {
      return (UserSessionView) SessionManager.getInstance().getSession();
   }

   public BigDecimal getTransactionLimit2(String refname) throws Exception {

      final SQLUtil S = new SQLUtil();

      try {
         S.setQuery(
                 "   select " +
                 "      max(a."+refname+") " +
                 "   from " +
                 "      s_roles a " +
                 "         inner join s_user_roles b on b.role_id=a.role_id and b.user_id=?");

         S.setParam(1,SessionManager.getInstance().getUserID());

         final ResultSet RS = S.executeQuery();

         if (RS.next()) return RS.getBigDecimal(1);

         return null;
      } finally {

         S.release();
      }

   }
}

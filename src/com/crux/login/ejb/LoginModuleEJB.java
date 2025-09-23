/***********************************************************************
 * Module:  com.crux.login.ejb.LoginModuleEJB
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 10:54:50 AM
 * Purpose:
 ***********************************************************************/

package com.crux.login.ejb;

import com.crux.common.model.UserSession;
import com.crux.login.model.FunctionsView;
import com.crux.login.model.UserLogView;
import com.crux.login.model.UserSessionView;
import com.crux.util.*;
import com.crux.util.Tools;
import com.crux.util.crypt.Digest;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;

public class LoginModuleEJB implements SessionBean {

   private final static transient LogManager logger = LogManager.getInstance(LoginModuleEJB.class);

   public LoginModuleEJB() {
   }

   private UserMaintenance getRemoteUserMaintenance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((UserMaintenanceHome) JNDIUtil.getInstance().lookup("UserMaintenanceEJB",UserMaintenanceHome.class.getName()))
            .create();
   }

   public DTOList getAllMenu() throws Exception {
      return ListUtil.getDTOListFromQuery("select * from s_functions where (ctl_id is not null or resource_id is NULL) order by function_id", FunctionsView.class);
   }

   public DTOList getMenu(String stUserID) throws Exception {
      if ("admin".equalsIgnoreCase(stUserID)) return getAllMenu();
      return
            ListUtil.getDTOListFromQuery(
                  "  select " +
                  "     distinct " +
           //   "on (f.function_id) " + ----> not supported
                  "     f.* " +
                  "  from" +
                  "     s_user_roles ur" +
                  "        inner join s_func_roles r on r.role_id = ur.role_id" +
                  "        inner join s_functions f on  f.function_id = r.function_id and (ctl_id is not null or resource_id is NULL)" +
                  "  where " +
                  "     ur.user_id = ?" +
                  "  order by f.orderseq, f.function_id",
                  new Object [] {stUserID},
                  FunctionsView.class
            );
   }

   public DTOList getResources(String stUserID) throws Exception {
      return
            ListUtil.getDTOListFromQuery(
                  "  select " +
                  "     distinct " +
             // " on (f.function_id) " + --not supported
                  "     f.* " +
                  "  from" +
                  "     s_user_roles ur" +
                  "        inner join s_func_roles r on r.role_id = ur.role_id" +
                  "        inner join s_functions f on  f.function_id = r.function_id" +
                  "  where " +
                  "     ur.user_id = ?" +
                  "  order by f.function_id",
                  new Object [] {stUserID},
                  FunctionsView.class
            );
   }

   public DTOList getAllResources() throws Exception {
      return ListUtil.getDTOListFromQuery("select * from s_functions order by function_id", FunctionsView.class);
   }

   public UserSession getUser(String stUserID, String stPassword) throws Exception {
      if (stUserID!=null) stUserID=stUserID.toLowerCase();

      final UserSessionView uv = getRemoteUserMaintenance().getUserView(stUserID);

      if (uv != null) {

         if (uv.getDtActiveDate()!=null)
            if (Tools.compare(uv.getDtActiveDate(), new Date())>0) throw new RuntimeException("User not yet active");

//         if (uv.getDtInActiveDate()!=null)
//            if (Tools.compare(uv.getDtInActiveDate(), new Date())<0) throw new RuntimeException("User is inactive");

         final String pwd = Digest.computeDigest(uv.getStUserID(),stPassword);
         if (Tools.isEqual(pwd, uv.getStPasswd())) {
            //  if (uv.getStBranchID() == null) throw new Exception("Invalid user");

            if ("admin".equalsIgnoreCase(stUserID))
               uv.setResources(getAllResources());
            else
               uv.setResources(getResources(stUserID));

            {
               final SQLUtil S = new SQLUtil();

               try {
                  uv.markUpdate();
                  uv.setUserSession(uv);
                  uv.setDtTransactionDate(new Date());
                  //uv.setDtLastLogin(uv.getDtTransactionDate());
                  uv.setDtLoginSince(uv.getDtLastLogin());
                  uv.setDtLastLogin(DateUtil.getNewDateTime());

                  logActivity(uv, "LOGIN", null);

                  S.store(uv);
               }
               finally {
                  S.release();
               }
            }


            return uv;

         } else throw new Exception("Incorrect password");
      }

      throw new Exception("Incorrect user");
   }

   public void logActivity(UserSessionView uv, String s, String ref1) throws Exception {

      UserSessionView usv = (UserSessionView)ListUtil.getDTOListFromQuery("select * from s_users where user_id = ?",
                          new Object [] {uv.getStUserID()},
                          UserSessionView.class
                          ).getDTO();

      UserLogView log = new UserLogView();

      log.setStUserLogId(String.valueOf(IDFactory.createNumericID("USER_LOG")));
      log.setStReference1(usv.getStPasswd());
      log.setStUserAction(s);
      log.setStUserId(uv.getStUserID());

      log.markNew();

      SQLUtil S = new SQLUtil();

      uv.setDtTransactionDate(new Date());

      S.setUserSession(uv);

      try {
         S.store(log);
      } finally {
         S.release();
      }

   }

   public void ejbCreate() throws javax.ejb.CreateException {
   }

   public void setSessionContext(SessionContext sessionContext) throws EJBException {
      /*final InputStream str = this.getClass().getClassLoader().getResourceAsStream("com/crux/login/ejb/crap.txt");

      try {
         int n;

         while ((n=str.read())>-1) {
            logger.logDebug("setSessionContext: "+(char)n);
         }
      }
      catch (IOException e) {
         e.printStackTrace();  //To change body of catch statement use Options | File Templates.
      }*/
   }

   public void ejbRemove() throws EJBException {
   }

   public void ejbActivate() throws EJBException {
   }

   public void ejbPassivate() throws EJBException {
   }
}

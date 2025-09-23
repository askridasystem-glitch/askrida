/***********************************************************************
 * Module:  com.crux.login.ejb.UserMaintenanceEJB
 * Author:  Denny Mahendra
 * Created: Apr 22, 2004 8:36:38 AM
 * Purpose:
 ***********************************************************************/

package com.crux.login.ejb;

import com.crux.common.model.DTO;
import com.crux.common.model.UserSession;
import com.crux.common.model.Filter;
import com.crux.login.filter.RoleFilter;
import com.crux.login.filter.UserMaintenanceFilter;
import com.crux.login.model.*;
import com.crux.util.*;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.sql.PreparedStatement;
import java.rmi.RemoteException;

public class UserMaintenanceEJB implements SessionBean {
   private final static transient LogManager logger = LogManager.getInstance(UserMaintenanceEJB.class);
   private SessionContext ctx;

   public UserMaintenanceEJB() {
   }

   public DTOList getRoleCombo() throws Exception {
      return ListUtil.getDTOListFromQuery("select role_id,role_id || ' ' || role_name as role_name from s_roles order by role_name", RoleView.class);
   }

   public DTOList getVendorCombo() throws Exception {
      return ListUtil.getDTOListFromQuery("select vendor_id,vendor_name from v_vendors where ENABLED_FLAG = 'Y' order by vendor_name", VendorView.class);
   }

   public String createRoleID(UserSession us) throws Exception {
      return IDFactory.createID("ROL", 20);
   }

   public DTOList getRoleList(RoleFilter roleFilter) throws Exception {
      final SQLAssembler sqa = new SQLAssembler();
      DTOList listRole = new DTOList();

      sqa.addSelect(" * ");

      sqa.addQuery("  from s_roles  ");

      if (roleFilter.stSearchKey != null) {
         sqa.addClause(" (upper(ROLE_ID) like upper(?)) " +
                 " or (upper(ROLE_NAME) like upper(?)) ");

         sqa.addPar("%" + roleFilter.stSearchKey + "%");
         sqa.addPar("%" + roleFilter.stSearchKey + "%");
      }

      listRole = ListUtil.getDTOListFromQuery(
              sqa.getSQL(),
              sqa.getPar(),
              UserRoleView.class,
              roleFilter
      );

      return listRole;
   }

   public DTOList getFunctionList() throws Exception {
      return ListUtil.getDTOListFromQuery("select * from s_functions order by function_id", FunctionsView.class);
   }

   public DTOList getUserList(UserMaintenanceFilter g) throws Exception {
      final SQLAssembler sqa = new SQLAssembler();
      DTOList listUser = new DTOList();

      sqa.addSelect(" usr.*,  branch.branchID || ' - ' ||branch.branchname as branch_name,  c.branchID || ' - ' ||c.branchname as branch_source_name");

      sqa.addQuery("  from s_users usr left join branch on branch.branchID = usr.BRANCH  "+
                   "  left join branch c on c.branchID = usr.cc_code_source");
 
      if (g.stSearchKey != null) {
         sqa.addClause("( (upper(usr.USER_ID) like upper(?)) " +
                 " or (upper(usr.USER_NAME) like upper(?)) " +
                 " or (upper(usr.BRANCH) like upper(?)) )");

         sqa.addPar("%" + g.stSearchKey + "%");
         sqa.addPar("%" + g.stSearchKey + "%");
         sqa.addPar("%" + g.stSearchKey + "%");
      }

      if(Tools.isNo(g.showDeleted)){
          sqa.addClause("coalesce(usr.delete_flag,'N') <> 'Y'");
      }
      

      listUser = ListUtil.getDTOListFromQuery(
              sqa.getSQL(),
              sqa.getPar(),
              UserSessionView.class,
              g
      );

      return listUser;
   }

   public DTOList getUserListSopp(Filter g) throws Exception {
      return ListUtil.getDTOListFromQuery("select usr.*, branch.branchID || ' - ' ||branch.branchname as branch_name " +
              "from s_users usr left join branch on branch.branchID = usr.BRANCH " +
              ListUtil.getOrderExpression(g),
              UserSessionView.class,
              g);
   }

   public DTOList getFuncRoleView(String stRoleID) throws Exception {
      return ListUtil.getDTOListFromQuery("select * from s_func_roles where role_id = ? ",
              new Object[]{stRoleID},
              FuncRoleView.class);

   }

   public DTOList getUserRoleView(String stUserID) throws Exception {
      return ListUtil.getDTOListFromQuery("select usr.*, rl.role_name " +
              "from s_user_roles usr left join s_roles rl on rl.role_id = usr.role_id " +
              "where user_id = ? ",
              new Object[]{stUserID},
              UserRoleView.class);
   }

   public DTOList getUserVendorView(String stUserID) throws Exception {
      return ListUtil.getDTOListFromQuery("select usr.*, v.vendor_name " +
              "from s_user_vendor usr left join v_vendors v on v.vendor_id = usr.vendor_id " +
              "where user_id = ? ",
              new Object[]{stUserID},
              UserVendorView.class);

   }

   public RoleView getRoleView(String stRoleID) throws Exception {
      final DTOList l = ListUtil.getDTOListFromQuery("select * from s_roles where role_id = ?",
              new Object[]{stRoleID},
              RoleView.class);

      if (l.size() > 0) {
         final RoleView dv = (RoleView)l.get(0);
         return dv;
      } else
         return null;
   }

   public VendorView getVendorView(String stVendorID) throws Exception {
      final DTOList l = ListUtil.getDTOListFromQuery("select * from v_vendors where vendor_id = ?",
              new Object[]{stVendorID},
              VendorView.class);

      if (l.size() > 0) {
         final VendorView dv = (VendorView)l.get(0);
         return dv;
      } else
         return null;
   }

   public UserSessionView getUserView(String stUserID) throws Exception {
      final DTOList l = ListUtil.getDTOListFromQuery("select usr.*, branch.branchname as branch_name " +
              "from s_users usr left join branch on branch.branchID = usr.BRANCH  " +
              "where usr.user_id = ? ",
              new Object[]{stUserID},
              UserSessionView.class);

      if (l.size() > 0) {
         final UserSessionView usr = (UserSessionView)l.get(0);
         //usr.setStTempPassword(usr.getStPasswd());
         usr.setUserroles(getUserRoleView(stUserID));

         return usr;
      } else
         return null;
   }

   private void saveRole(RoleView rlv, UserSession us) throws Exception {
      final SQLUtil S = new SQLUtil();
      try {

         final PreparedStatement PS = S.setQuery("delete from s_func_roles where role_id = ?");
         S.setParam(1, rlv.getStRoleID());
         PS.executeUpdate();
         S.reset();

         final DTOList dl1 = rlv.getChecbox1();
         if (dl1 != null) {
            for (int i = 0;i < dl1.size();i++) {
               FuncRoleView frv = (FuncRoleView)dl1.get(i);
               frv.setUserSession(us);
               frv.setStRoleID(rlv.getStRoleID());
            }
            S.store(dl1);
         }

         final DTOList dl2 = rlv.getChecbox2();
         if (dl2 != null) {
            for (int i = 0;i < dl2.size();i++) {
               FuncRoleView frv = (FuncRoleView)dl2.get(i);
               frv.setUserSession(us);
               frv.setStRoleID(rlv.getStRoleID());
            }
            S.store(dl2);
         }

         final DTOList dl3 = rlv.getChecbox3();
         if (dl3 != null) {
            for (int i = 0;i < dl3.size();i++) {
               FuncRoleView frv = (FuncRoleView)dl3.get(i);
               frv.setUserSession(us);
               frv.setStRoleID(rlv.getStRoleID());
            }
            S.store(dl3);
         }
         final DTOList dl4 = rlv.getChecbox4();
         if (dl4 != null) {
            for (int i = 0;i < dl4.size();i++) {
               FuncRoleView frv = (FuncRoleView)dl4.get(i);
               frv.setUserSession(us);
               frv.setStRoleID(rlv.getStRoleID());
            }
            S.store(dl4);
         }

      } finally {
         S.release();
      }
   }

   public String save(RoleView rl) throws Exception {
      final SQLUtil S = new SQLUtil();
      final UserSession us = rl.getUserSession();
      String result = "";
      String Id = "";
      try {

         if (rl.isNew()) {

            String stRoleID = createRoleID(us);
            final RoleView rlv = getRoleView(stRoleID);
            if (rlv == null)
               Id = "";
            else
               Id = rlv.getStRoleID();

            if (rl.getStRoleID() == null)
               rl.setStRoleID(stRoleID);
            if (Id.equalsIgnoreCase(stRoleID))
               result = "FAIL";
            else {
               S.store(rl);
               saveRole(rl, us);
               result = rl.getStRoleID();
            }
         } else {
            S.store(rl);
            saveRole(rl, us);
            result = rl.getStRoleID();
         }

         return result;
      } finally {
         S.release();
      }
   }

   public String deleteUser(UserSessionView usv) throws Exception {
      final SQLUtil S = new SQLUtil();
      String result = "";
      try {

         DTOList urv = getUserRoleView(usv.getStUserID());
         if (urv != null) {
            for (int i = 0;i < urv.size();i++) {
               try {
                  S.delete((DTO)urv.get(i));
               } catch (Exception e) {
                  System.out.println("There is an error while deleting user's role no." + String.valueOf(i));
                  e.printStackTrace();
                  result = "FAIL";
               }
            }

         }

         S.delete(usv);


         {
            S.reset();

            PreparedStatement PS = S.setQuery("delete from s_user_roles where user_id = ?");

            PS.setString(1,usv.getStUserID());

            PS.executeUpdate();
         }

         result = "SUCCESS";
      } catch (Exception e) {
         System.out.println("there is an error");
         e.printStackTrace();
         result = "FAIL";
      } finally {
         S.release();
         return result;
      }
   }

   /*public String saveUser(UserSessionView usv) throws Exception {
      final SQLUtil S = new SQLUtil();
      final UserSession us = usv.getUserSession();
      String result = "";

      // usv.setStOutletID(null);
      System.out.println("saveuserejb : "+us.getStUserID());
      try {
         S.store(usv);
         final DTOList roles = usv.getUserroles();
         S.setUserSession(us);
         if (roles != null) {
            System.out.println("role .............. "+roles.size());
            for (int i = 0; i < roles.size(); i++) {
               UserRoleView urv = (UserRoleView) roles.get(i);
               urv.setUserSession(us);
               urv.setStUserID(usv.getStUserID());
            }
            System.out.println("save role maintenanceejb .............. ");
            S.store(roles);
         }

         result = usv.getStUserID();
         return result;
      } finally {
         S.release();
      }
   } */

   private LoginModule getRemoteLoginModule() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((LoginModuleHome) JNDIUtil.getInstance().lookup("LoginModuleEJB",LoginModuleHome.class.getName()))
            .create();
   }

   public String saveUser(UserSessionView usv) throws Exception {
      final SQLUtil S = new SQLUtil();
      String result = "";

      try {
         S.store(usv);



         final DTOList roles = usv.getUserroles();
         if (roles != null) {
            for (int i = 0;i < roles.size();i++) {
               UserRoleView urv = (UserRoleView)roles.get(i);
               urv.setStUserID(usv.getStUserID());
            }
            S.store(roles);
         }

         if (usv.isUpdate()) getRemoteLoginModule().logActivity(usv, "UPDATE", null);
         if (usv.isNew()) getRemoteLoginModule().logActivity(usv, "CREATE", null);

         result = usv.getStUserID();
         return result;
      } finally {
         S.release();
      }
   }

   public boolean isPasswordHasUsed(UserSessionView usv) throws Exception {
      /*final SQLAssembler sqa = new SQLAssembler();
      DTOList listPasswd = new DTOList();
      sqa.addSelect(" * ");
      sqa.addQuery("  from S_USER_LOG  ");
      sqa.addClause(" USER_ID = ? and rownum < 6  ");
      sqa.addPar(usv.getStUserID());


      listPasswd = ListUtil.getDTOListFromQuery(
              sqa.getSQL()+" order by USER_LOG_ID desc ",
              sqa.getPar(),
              UserLogView.class
      );*/

      /*
      "select x.* from ("+
      "   select * from ("+
      "      select (reference1), min(user_log_id) as u from s_user_log  a where user_id='admin' group by reference1"+
      "   ) order by u desc"+
      ") x"+
      "where rownum<6"+
      */

      DTOList listPasswd = ListUtil.getDTOListFromQuery(

              " select x.* from ("+
              "    select * from ("+
              "       select reference1, max(user_log_id) as u from s_user_log a where user_id=? group by reference1"+
              "    ) c order by u desc"+
              " ) x"+
              " limit 5",

              new Object [] {usv.getStUserID()},
              UserLogView.class
      );


      if (listPasswd!=null) {
         for (int i=0;i<listPasswd.size();i++) {
            UserLogView userlog = (UserLogView)listPasswd.get(i);
            System.out.println("cek password................."+usv.getStUserID()+" = "+userlog.getStReference1()+":"+usv.getStPasswd());
            if (Tools.isEqual(userlog.getStReference1(),(usv.getStPasswd()))) {
               return true;
            }
         }
      }

      return false;
   }

   public String savePassword(UserSessionView usv) throws Exception {
      final SQLUtil S = new SQLUtil();
      final UserSession us = usv.getUserSession();
      String result = "";
      String Id = "";
      try {
         S.store(usv);

         getRemoteLoginModule().logActivity(usv, "CPASS", null);

         result = usv.getStUserID();
         return result;
      } finally {
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
      ctx = sessionContext;
   }

   public void save(RoleView role, DTOList rolefunctions) throws Exception {
      SQLUtil S = new SQLUtil();

      try {
         S.store(role);

         S.releaseResource();

         PreparedStatement PS = S.setQuery("delete from  s_func_roles where role_id=?");

         PS.setString(1,role.getStRoleID());

         PS.executeUpdate();

         S.releaseResource();

         for (int i = 0; i < rolefunctions.size(); i++) {
            FuncRoleView fr = (FuncRoleView) rolefunctions.get(i);

            if (fr.isModified()) fr.setStRoleID(role.getStRoleID());
         }

         S.store(rolefunctions);

      } catch(Exception e) {

         ctx.setRollbackOnly();

         throw e;

      } finally {

         S.release();

      }

   }
}

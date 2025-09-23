/***********************************************************************
 * Module:  com.crux.login.helper.RoleHelper
 * Author:  Denny Mahendra
 * Created: Apr 22, 2004 8:45:29 AM
 * Purpose:
 ***********************************************************************/

package com.crux.login.helper;

import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.login.ejb.UserMaintenance;
import com.crux.login.ejb.UserMaintenanceHome;
import com.crux.login.model.RoleView;
import com.crux.login.model.FuncRoleView;
import com.crux.login.filter.RoleFilter;
import com.crux.common.controller.Helper;

import javax.servlet.http.HttpServletRequest;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public class RoleHelper extends Helper {

   private UserMaintenance getRemoteRole() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((UserMaintenanceHome) JNDIUtil.getInstance().lookup("UserMaintenanceEJB", UserMaintenanceHome.class.getName()))
            .create();
   }

   public void listRoles(HttpServletRequest rq) throws Exception {
      //System.err.println("listRoles..................................");
      final RoleFilter roleFilter = (RoleFilter) get(rq,"FILTER_USERROLE");
      updatePaging(rq, roleFilter);
      final DTOList roleList = getRemoteRole().getRoleList(roleFilter);
      put(rq,"FILTER_USERROLE", roleFilter);
      rq.setAttribute("ROLE_LIST", roleList);
   }

   public void listRolesDisplay(HttpServletRequest rq) throws Exception {
      //System.err.println("listRolesDisplay..................................");
      final RoleFilter roleFilter = new RoleFilter();
      updatePaging(rq, roleFilter);
      roleFilter.orderKey = "role_name";roleFilter.orderDir=1;
      put(rq,"FILTER_USERROLE", roleFilter);
      final DTOList roleList = getRemoteRole().getRoleList(roleFilter);
      rq.setAttribute("ROLE_LIST", roleList);
   }

   public void createRole(HttpServletRequest rq) throws Exception {
      final RoleView dv = new RoleView();

      dv.markNew();
      put(rq, "USER_ROLE", dv);
      rq.setAttribute("ACTION", "CREATE");
      populateRole(rq);
   }


   public void addResource(HttpServletRequest rq) throws Exception {
   }

   public void delResource(HttpServletRequest rq) throws Exception {
   }

   public void save(HttpServletRequest rq) throws Exception {
      int arrLength1 = 0;
      int arrLength2 = 0;
      int arrLength3 = 0;
      int arrLength4 = 0;

      final RoleView rlv = (RoleView) get(rq, "USER_ROLE");
      String arr1[] = rq.getParameterValues("root");
      String arr2[] = rq.getParameterValues("sub1");
      String arr3[] = rq.getParameterValues("sub2");
      String arr4[] = rq.getParameterValues("sub3");
      if (arr1 != null) arrLength1 = arr1.length;
      if (arr2 != null) arrLength2 = arr2.length;
      if (arr3 != null) arrLength3 = arr3.length;
      if (arr4 != null) arrLength4 = arr4.length;

      for (int i = 0; i < arrLength1; i++) {
         addRole1(rq, rlv, i);
      }
      for (int i = 0; i < arrLength2; i++) {
         addRole2(rq, rlv, i);
      }
      for (int i = 0; i < arrLength3; i++) {
         addRole3(rq, rlv, i);
      }
      for (int i = 0; i < arrLength4; i++) {
         addRole4(rq, rlv, i);
      }

      final RoleView rl = getRoleFromRequest(rq);
      rl.setUserSession(getUserSession(rq));

      String result = getRemoteRole().save(rl);


      rq.setAttribute("RESULT", result);
      rq.setAttribute("ROLE", rl);
   }

   public void viewRole(HttpServletRequest rq) throws Exception {

      final String stRoleID = deBlank(rq.getParameter("rlid"));
      final RoleView rl = getRemoteRole().getRoleView(stRoleID);
      put(rq, "USER_ROLE", rl);

      rq.setAttribute("ACTION", "VIEW");
      final DTOList funcrole = getRemoteRole().getFuncRoleView(stRoleID);
      rq.setAttribute("FUNCROLE_LIST", funcrole);
      populateRole(rq);

   }

   public void editRole(HttpServletRequest rq) throws Exception {

      final String stRoleID = deBlank(rq.getParameter("rlid"));
      final RoleView rl = getRemoteRole().getRoleView(stRoleID);
      put(rq, "USER_ROLE", rl);

      rl.markUpdate();
      rq.setAttribute("ACTION", "EDIT");
      final DTOList funcrole = getRemoteRole().getFuncRoleView(stRoleID);
      rq.setAttribute("FUNCROLE_LIST", funcrole);
      populateRole(rq);
   }

   private void addRole1(HttpServletRequest rq, final RoleView rv, int i) {
      final FuncRoleView frv = new FuncRoleView();
      frv.markNew();
      frv.setStRoleID(getString(rq.getParameter("roleid")));
      frv.setStFuncID(getString(rq.getParameterValues("root")[i]));


      DTOList l = rv.getChecbox1();

      if (l == null) {
         l = new DTOList();
         rv.setChecbox1(l);
      }

      l.add(frv);
   }

   private void addRole2(HttpServletRequest rq, final RoleView rv, int i) {
      final FuncRoleView frv = new FuncRoleView();
      frv.markNew();
      frv.setStRoleID(getString(rq.getParameter("roleid")));
      frv.setStFuncID(getString(rq.getParameterValues("sub1")[i]));


      DTOList l = rv.getChecbox2();

      if (l == null) {
         l = new DTOList();
         rv.setChecbox2(l);
      }

      l.add(frv);
   }

   private void addRole3(HttpServletRequest rq, final RoleView rv, int i) {
      final FuncRoleView frv = new FuncRoleView();
      frv.markNew();
      frv.setStRoleID(getString(rq.getParameter("roleid")));
      frv.setStFuncID(getString(rq.getParameterValues("sub2")[i]));


      DTOList l = rv.getChecbox3();

      if (l == null) {
         l = new DTOList();
         rv.setChecbox3(l);
      }

      l.add(frv);
   }

   private void addRole4(HttpServletRequest rq, final RoleView rv, int i) {
      final FuncRoleView frv = new FuncRoleView();
      frv.markNew();
      frv.setStRoleID(getString(rq.getParameter("roleid")));
      frv.setStFuncID(getString(rq.getParameterValues("sub3")[i]));
      DTOList l = rv.getChecbox4();

      if (l == null) {
         l = new DTOList();
         rv.setChecbox4(l);
      }

      l.add(frv);
   }

   public RoleView getRoleFromRequest(HttpServletRequest rq) throws Exception {

      final RoleView rl = (RoleView) get(rq, "USER_ROLE");

      rl.setStRoleID(getString(rq.getParameter("roleid")));
      rl.setStRoleName(getString(rq.getParameter("rolename")));
      rl.setDtActiveDate(getDate(rq.getParameter("activedate")));
      rl.setDtInActiveDate(getDate(rq.getParameter("inactivedate")));
      rl.setDbTransactionLimit(getNum(rq.getParameter("translimit")));


      final DTOList dl1 = rl.getChecbox1();
      if (dl1 != null)
         for (int i = 0; i < dl1.size(); i++) {
            FuncRoleView frv = (FuncRoleView) dl1.get(i);
            frv.setStRoleID(getString(rq.getParameter("roleid")));
            frv.setStFuncID(getString(rq.getParameterValues("root")[i]));
         }
      final DTOList dl2 = rl.getChecbox2();
      if (dl2 != null)
         for (int i = 0; i < dl2.size(); i++) {
            FuncRoleView frv = (FuncRoleView) dl2.get(i);
            frv.setStRoleID(getString(rq.getParameter("roleid")));
            frv.setStFuncID(getString(rq.getParameterValues("sub1")[i]));
         }
      final DTOList dl3 = rl.getChecbox3();
      if (dl3 != null)
         for (int i = 0; i < dl3.size(); i++) {
            FuncRoleView frv = (FuncRoleView) dl3.get(i);
            frv.setStRoleID(getString(rq.getParameter("roleid")));
            frv.setStFuncID(getString(rq.getParameterValues("sub2")[i]));
         }
      final DTOList dl4 = rl.getChecbox4();
      if (dl4 != null)
         for (int i = 0; i < dl4.size(); i++) {
            FuncRoleView frv = (FuncRoleView) dl4.get(i);
            frv.setStRoleID(getString(rq.getParameter("roleid")));
            frv.setStFuncID(getString(rq.getParameterValues("sub3")[i]));
         }
      return rl;
   }

   public void populateRole(HttpServletRequest rq) throws Exception {
      final RoleView rl = (RoleView) get(rq,"USER_ROLE");
      rq.setAttribute("ROLE", rl);
      final DTOList funcList = getRemoteRole().getFunctionList();
      rq.setAttribute("FUNC_LIST", funcList);
   }
}

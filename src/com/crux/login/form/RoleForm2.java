/***********************************************************************
 * Module:  com.crux.login.form.RoleForm2
 * Author:  Denny Mahendra
 * Created: Oct 24, 2006 2:29:49 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.login.form;

import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.login.model.RoleView;
import com.crux.login.model.FuncRoleView;
import com.crux.login.ejb.UserMaintenanceHome;
import com.crux.login.ejb.UserMaintenance;
import com.crux.pool.DTOPool;
import com.crux.web.form.Form;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;


public class RoleForm2 extends Form {
   private RoleView role;
   private DTOList rolefunctions;

   public DTOList getRolefunctions() throws Exception {
      if (rolefunctions==null) {
         rolefunctions = ListUtil.getDTOListFromQuery(
                 "   select " +
                 "      a.function_id, a.function_name, b.role_id " +
                 "   from s_functions a" +
                 "      left join s_func_roles b on a.function_id=b.function_id and b.role_id=?" +
                 "   order by a.function_id",
                 new Object [] {role.getStRoleID()},
                 FuncRoleView.class
         );

         for (int i = 0; i < rolefunctions.size(); i++) {
            FuncRoleView fr = (FuncRoleView) rolefunctions.get(i);

            boolean selected = fr.getStRoleID()!=null;

            fr.markUnmodified();

            if (selected) fr.markNew();
         }
      }

      return rolefunctions;
   }

   public void afterUpdateForm() {

      for (int i = 0; i < rolefunctions.size(); i++) {
         FuncRoleView fr = (FuncRoleView) rolefunctions.get(i);

         boolean selected = getAttribute("f"+i)!=null;

         fr.markUnmodified();

         if (selected) fr.markNew();
      }
   }

   public void setRolefunctions(DTOList rolefunctions) {
      this.rolefunctions = rolefunctions;
   }

   private UserMaintenance getRemoteUserMaintenance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((UserMaintenanceHome) JNDIUtil.getInstance().lookup("UserMaintenanceEJB",UserMaintenanceHome.class.getName()))
            .create();
   }

   public void createNew() {
      role = new RoleView();
      role.markNew();
   }

   public void edit(String roleid) {
      role = (RoleView)DTOPool.getInstance().getDTO(RoleView.class, roleid);

      role.markUpdate();
   }

   public void view(String roleid) {
      role = (RoleView)DTOPool.getInstance().getDTO(RoleView.class, roleid);

      super.setReadOnly(true);
   }

   public RoleView getRole() {
      return role;
   }

   public void setRole(RoleView role) {
      this.role = role;
   }

   public void clickSave() throws Exception {
      getRemoteUserMaintenance().save(role, rolefunctions);
      close();
   }

   public void clickCancel() {
      close();
   }
}

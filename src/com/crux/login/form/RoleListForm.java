/**
 * Created by IntelliJ IDEA.
 * User: dono
 * Date: Jun 17, 2006
 * Time: 3:45:37 PM
 * To change this template use Options | File Templates.
 */
package com.crux.login.form;

import com.crux.web.form.Form;
import com.crux.login.ejb.UserMaintenance;
import com.crux.login.ejb.UserMaintenanceHome;
import com.crux.login.model.RoleView;
import com.crux.login.model.FunctionsView;
import com.crux.login.model.FuncRoleView;
import com.crux.login.filter.RoleFilter;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.util.Date;

public class RoleListForm extends Form {
   private UserMaintenance getRemoteRole() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((UserMaintenanceHome)JNDIUtil.getInstance().lookup("UserMaintenanceEJB", UserMaintenanceHome.class.getName()))
              .create();
   }

   private RoleView role;
   private String roleid;
   private String searchKey;
   private RoleFilter roleFilter = new RoleFilter();
   private DTOList listRole;


   public void clickCreate2() throws Exception {
      final RoleForm form = (RoleForm)super.newForm("role_edit", this);
      role = new RoleView();
      role.markNew();
      role.setDtActiveDate(new Date());
      //getRole().setDtActiveDate(new Date());
      form.setListFunction(getRemoteRole().getFunctionList());
      form.createNew();

      form.show();
   }

   public void clickEdit() throws Exception {
      final RoleForm2 form = (RoleForm2)super.newForm("role_edit", this);

      form.edit(getRoleid());
      form.show();
   }

   public void clickCreate() throws Exception {
      final RoleForm2 form = (RoleForm2)super.newForm("role_edit", this);

      form.createNew();
      form.show();
   }

   public void clickEdit2() throws Exception {
      final RoleForm form = (RoleForm)super.newForm("role_edit", this);

      DTOList listFunctionRole = getRemoteRole().getFuncRoleView(getRoleid());
      DTOList listFunction = getRemoteRole().getFunctionList();
      //form.fillFuncRole(listFunction,listFunctionRole);
      //form.setListFunctionRole(listFunctionRole);
      //form.setListFunction(listFunction);

      form.setListFunctionRole(getRemoteRole().getFuncRoleView(getRoleid()));
      form.setListFunction(getRemoteRole().getFunctionList());


      if (getRoleid() != null) {
         form.edit(getRoleid());
         form.show();
      }
   }

   public void clickView() throws Exception {
      final RoleForm2 form = (RoleForm2)super.newForm("role_edit", this);

      form.view(getRoleid());
      form.show();
   }
   public void clickView2() throws Exception {
      final RoleForm form = (RoleForm)super.newForm("role_edit", this);
      form.setListFunctionRole(getRemoteRole().getFuncRoleView(getRoleid()));
      form.setListFunction(getRemoteRole().getFunctionList());

      if (getRoleid() != null) {
         form.view(getRoleid());
         form.show();
      }
   }

   public DTOList getListRole() throws Exception {
      if (listRole == null) {
         listRole = new DTOList();
      }
      roleFilter.stSearchKey = getSearchKey();
      listRole = getRemoteRole().getRoleList(roleFilter);
      return listRole;
   }

   public String getRoleid() {
      return roleid;
   }

   public void setRoleid(String roleid) {
      this.roleid = roleid;
   }

   public void setListRole(DTOList listRole) {
      this.listRole = listRole;
   }

   public String getSearchKey() {
      return searchKey;
   }

   public void setSearchKey(String searchKey) {
      this.searchKey = searchKey;
   }

   public RoleView getRole() {
      return role;
   }

   public void setRole(RoleView role) {
      this.role = role;
   }


}

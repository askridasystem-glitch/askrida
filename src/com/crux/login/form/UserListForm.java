/***********************************************************************
 * Module:  com.crux.login.form.UserListForm
 * Author:  Denny Mahendra
 * Created: Oct 24, 2006 4:55:52 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.login.form;

import com.crux.web.form.Form;

import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.login.ejb.UserMaintenance;
import com.crux.login.ejb.UserMaintenanceHome;
import com.crux.login.filter.UserMaintenanceFilter;
import com.crux.login.model.UserSessionView;
import com.crux.common.model.Filter;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public class UserListForm extends Form{
   private UserMaintenance getRemoteUser() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((UserMaintenanceHome) JNDIUtil.getInstance().lookup("UserMaintenanceEJB",UserMaintenanceHome.class.getName()))
            .create();
   }

   private DTOList listUser;
   private UserSessionView user;
   private String userid;
   private String searchKey;
   private UserMaintenanceFilter filter = new UserMaintenanceFilter();
   private String showDeleted;

   public DTOList getListUser() throws Exception {
      if (listUser==null)
      {
         listUser = new DTOList(); 
      }
      filter.stSearchKey = getSearchKey();
      filter.showDeleted = getShowDeleted();
      listUser = getRemoteUser().getUserList(filter);
      return listUser;
   }

   public void setListUser(DTOList listUser) {
      this.listUser = listUser;
   }

   public String getSearchKey() {
      return searchKey;
   }

   public void setSearchKey(String searchKey) {
      this.searchKey = searchKey;
   }

   public String getUserid() {
      return userid;
   }

   public void setUserid(String userid) {
      this.userid = userid;
   }

   public void clickCreate() throws Exception {
      final UserForm form = (UserForm) super.newForm("user_edit", this);

      form.createNew();

      form.show();
   }

   public void clickEdit() throws Exception {
      final UserForm form = (UserForm) super.newForm("user_edit", this);

      if (getUserid()!=null)
      {
         form.edit(getUserid());
         form.show();
      }
   }

   public void clickChangePassword() throws Exception {
      final UserForm form = (UserForm) super.newForm("user_newpassword", this);

      if (getUserid()!=null)
      {
         form.edit(getUserid());
         form.show();
      }
   }

   public void clickView() throws Exception {
      final UserForm form = (UserForm) super.newForm("user_edit", this);

      if (getUserid()!=null)
      {
         form.view(getUserid());
         form.show();
      }
   }

   public void clickDelete()throws Exception{
       user = getRemoteUser().getUserView(getUserid());
       user.markDelete();
       getRemoteUser().saveUser(user);
   }

    /**
     * @return the showDeleted
     */
    public String getShowDeleted() {
        return showDeleted;
    }

    /**
     * @param showDeleted the showDeleted to set
     */
    public void setShowDeleted(String showDeleted) {
        this.showDeleted = showDeleted;
    }
}

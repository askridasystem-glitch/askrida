/***********************************************************************
 * Module:  com.crux.login.form.UserForm
 * Author:  Denny Mahendra
 * Created: Oct 24, 2006 3:09:59 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.login.form;

import com.crux.web.form.Form;
import com.crux.login.ejb.UserMaintenance;
import com.crux.login.ejb.UserMaintenanceHome;
import com.crux.login.model.UserSessionView;
import com.crux.login.model.UserRoleView;
import com.crux.login.model.UserLogView;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;
import com.crux.util.StringTools;
import com.crux.util.Tools;
import com.crux.util.crypt.Digest;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import org.joda.time.DateTime;

public class UserForm extends Form {
   private UserMaintenance getRemoteUser() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((UserMaintenanceHome)JNDIUtil.getInstance().lookup("UserMaintenanceEJB", UserMaintenanceHome.class.getName()))
              .create();
   }

   private UserSessionView user;
   private UserLogView userlog = new UserLogView();
   private String userIndex;
   private String stPasswd;
   private String stPasswdConfirm;

   public String getStPasswd() {
      return stPasswd;
   }

   public void setStPasswd(String stPasswd) {
      this.stPasswd = stPasswd;
   }

   public String getStPasswdConfirm() {
      return stPasswdConfirm;
   }

   public void setStPasswdConfirm(String stPasswdConfirm) {
      this.stPasswdConfirm = stPasswdConfirm;
   }

   public UserLogView getUserlog() {
      return userlog;
   }

   public void setUserlog(UserLogView userlog) {
      this.userlog = userlog;
   }


   public void addRole() {
      if (user.getStUserName() == null) throw new RuntimeException("Anda belum mengisi user name");

      UserRoleView role = new UserRoleView();

      role.setUserSession(role.getUserSession());
      role.markNew();
      user.getUserroles().add(role);
   }

   public void deleteRole() {
      int n = Integer.parseInt(userIndex);
      user.getUserroles().delete(n);
   }

   public String getUserIndex() {
      return userIndex;
   }

   public void setUserIndex(String userIndex) {
      this.userIndex = userIndex;
   }


   public UserSessionView getUser() {
      return user;
   }

   public void setUser(UserSessionView user) {
      this.user = user;
   }

   public void createNew() {
      user = new UserSessionView();
      user.markNew();
      user.setUserroles(new DTOList());
      user.setUserlog(getUserlog());
   }

   public void doSave() throws Exception {
      final boolean pwdConfirm = Tools.isEqual(stPasswd, stPasswdConfirm);

      if(!pwdConfirm) throw new RuntimeException("Password not confirmed");

      if (stPasswd!=null) {

         StringTools.validatePassword(stPasswd);

         String encryptPswd = Digest.computeDigest(user.getStUserID(), stPasswd);
         user.setStPasswd(encryptPswd);
		 
		 /*
         if (getRemoteUser().isPasswordHasUsed(user)) {
            throw new Exception("Password pernah dipakai");
         }*/
      }
      
      if (user.isNew())
         if (user.getStPasswd()==null) throw new RuntimeException("Password required");

      if(user.getUserroles().size()<1)
          throw new RuntimeException("Role tidak boleh kosong");
	  
      getRemoteUser().saveUser(user);
      close();
   }

   public void doCancel() {
      close();
   }

   public void view(String userid) throws Exception {
      user = getRemoteUser().getUserView(userid);
      super.setReadOnly(true);
   }

   public void edit(String userid) throws Exception {
      user = getRemoteUser().getUserView(userid);
      user.markUpdate();

      user.getUserroles().markAllUpdate();
   }

   public void delete(String userid) throws Exception {
      user = getRemoteUser().getUserView(userid);

      user.markDelete();
   }

   public void afterUpdateForm() {
      try {
         user.setStUserID(user.getStUserID().toLowerCase());
      } catch (Exception e) {
      }
   }
   
   public void changeBranch(){
       
   }

   public void tesTgl(){
       int batasMaxHari = 30;
        DateTime maximumPasswordActive = new DateTime();
        maximumPasswordActive = maximumPasswordActive.plusDays(batasMaxHari);

        user.setDtInActiveDate(maximumPasswordActive.toDate());
   }
   
}

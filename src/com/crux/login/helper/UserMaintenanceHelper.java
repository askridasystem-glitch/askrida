/***********************************************************************
 * Module:  com.crux.login.helper.UserMaintenanceHelper***************
 * Module:  com.crux.login.helper.UserMaintenanceHelper
 * Author:  Denny Mahendra
 * Created: Apr 22, 2004 8:37:07 AM
 * Purpose:
 ***********************************************************************/

package com.crux.login.helper;

import com.crux.common.controller.Helper;
import com.crux.login.ejb.UserMaintenance;
import com.crux.login.ejb.UserMaintenanceHome;
import com.crux.login.model.*;
import com.crux.login.filter.UserMaintenanceFilter;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.StringTools;
import com.crux.util.crypt.Digest;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.util.Date;
import org.joda.time.DateTime;

public class UserMaintenanceHelper extends Helper{

   private UserMaintenance getRemoteUser() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((UserMaintenanceHome) JNDIUtil.getInstance().lookup("UserMaintenanceEJB",UserMaintenanceHome.class.getName()))
            .create();
   }

   public void listUserDisplay(HttpServletRequest rq)  throws Exception {
      final UserMaintenanceFilter usrFilter = (UserMaintenanceFilter) get(rq,"FILTER_USER");
      updatePaging(rq, usrFilter);
      final DTOList userList = getRemoteUser().getUserList(usrFilter);
      put(rq,"FILTER_USER", usrFilter);
      rq.setAttribute("USER_LIST", userList);
   }

   public void listUser(HttpServletRequest rq)  throws Exception {
      final UserMaintenanceFilter usrFilter = new UserMaintenanceFilter();
      updatePaging(rq, usrFilter);
      usrFilter.orderKey = "user_name";usrFilter.orderDir=1;
      put(rq,"FILTER_USER", usrFilter);
      final DTOList userList = getRemoteUser().getUserList(usrFilter);
      rq.setAttribute("USER_LIST", userList);
   }

   public void createUser(HttpServletRequest rq)  throws Exception {
      final UserSessionView us = new UserSessionView();

      us.markNew();
      put(rq,"USER",us);
      us.setAttribute("ACTION","CREATE");
      put(rq,"VendorStatus",new Boolean(false));
      populateUser(rq);
   }

   public void selBranch(HttpServletRequest rq)  throws Exception {
      final UserSessionView usv = getUserFromRequest(rq);
      populateUser(rq);
   }

   public void editUser(HttpServletRequest rq)  throws Exception {
      final String stUserID = deBlank(rq.getParameter("usrid"));
      final UserSessionView usv = getRemoteUser().getUserView(stUserID);
      put(rq,"USER",usv);
      Boolean vendorStat;

      final DTOList userrole = usv.getUserroles();

      for (int i=0; i< userrole.size(); i++){
         UserRoleView urv = (UserRoleView) userrole.get(i);
         urv.markUpdate();
      }

      usv.markUpdate();
      usv.setAttribute("ACTION","EDIT");
      populateUser(rq);
   }

   public void viewUser(HttpServletRequest rq)  throws Exception {
      final String stUserID = deBlank(rq.getParameter("usrid"));
      final UserSessionView usv = getRemoteUser().getUserView(stUserID);
      put(rq,"USER",usv);
      //String stBranchID = getString(usv.getStBranchID());
      rq.setAttribute("ACTION","VIEW");
      populateUser(rq);
   }

   public void save(HttpServletRequest rq)  throws Exception {
      try {
         final UserSessionView usv = getUserFromRequest(rq);
         String action = getString(rq.getParameter("ACTION"));
         if (action.equalsIgnoreCase("CREATE")) {
             if (getString(rq.getParameter("roleid")) != null)
                addRole(rq,usv);
             rq.setAttribute("ACTION","NEW");
         }else{
             rq.setAttribute("ACTION","EDIT");
         }
         usv.setUserSession(getUserSession(rq));

         String result = getRemoteUser().saveUser(usv);
         rq.setAttribute("RESULT",result);
         rq.setAttribute("USER",usv);
      }
      catch (Exception e) {
         populateUser(rq);
         throw e;
      }
   }

    public void showVendor(HttpServletRequest rq)  throws Exception {
      final UserSessionView usv = getUserFromRequest(rq);
      Boolean VendorStat ;

      if("on".equals(rq.getParameter("VendorChk")))
         VendorStat =  new Boolean(true);
      else
        VendorStat =  new Boolean(false);

      put(rq,"VendorStatus",VendorStat);

      populateUser(rq);
   }
    public void addVendor(HttpServletRequest rq)  throws Exception {
      final UserSessionView usv = getUserFromRequest(rq);

      final String vendorID = getString(rq.getParameter("vendorid"));


      if (vendorID!=null)
      {
         final UserVendorView ou = new UserVendorView();

         ou.markNew();
         ou.setUserSession(getUserSession(rq));
         ou.setStUserID(usv.getStUserID());
         ou.setStVendorID(vendorID);
         VendorView stVendorName = getRemoteUser().getVendorView(vendorID);
         ou.setStVendorName(stVendorName!=null?stVendorName.getStVendorName():null);
      }
      populateUser(rq);
   }

   public void delVendor(HttpServletRequest rq)  throws Exception {
      final UserSessionView usv = getUserFromRequest(rq);

      final int delN = getLong(rq.getParameter("deleteindex")).intValue();

      populateUser(rq);
   }

   public void addRole(HttpServletRequest rq)  throws Exception {
      final UserSessionView usv = getUserFromRequest(rq);
      String stRoles[] = rq.getParameterValues("roles");
      //String stBranchID = usv.getStBranchID();
      String stRoleName = "";
      RoleView rl = getRemoteRole().getRoleView(rq.getParameter("roleid"));
      stRoleName =  getString(rl.getStRoleName());
      boolean fn = false;
      if (stRoles != null) {
         System.out.println("============== "+stRoles.length);
         for (int i=0; i<stRoles.length; i++) {
             System.out.println(stRoleName + " = " + stRoles[i]);
             if (stRoleName.equalsIgnoreCase(stRoles[i])) {
               fn = true;
               break;
            }
         }
      }
      System.out.println("duplicate role === " +fn);
      if (!fn)addRole(rq, usv);
      populateUser(rq);
   }
   public void delUser(HttpServletRequest rq)throws Exception{
       final String stUserID = deBlank(rq.getParameter("usrid"));
       final UserSessionView usv = getRemoteUser().getUserView(stUserID);

       usv.markDelete();
       String result=getRemoteUser().deleteUser(usv);
       rq.setAttribute("RESULT",result);
       rq.setAttribute("ACTION","USER_DELETE");
   }
   public void delRole(HttpServletRequest rq)  throws Exception {
      final UserSessionView usv = getUserFromRequest(rq);

      final DTOList l = usv.getUserroles();
      final String stRoleToDelete = deBlank(rq.getParameter("roleselect"));


      for (int i = 0; i < l.size(); i++) {
         UserRoleView urv = (UserRoleView) l.get(i);

         if (stRoleToDelete.equalsIgnoreCase(urv.getStRoleID())) {
            l.delete(i); break;
         } else if (stRoleToDelete.equalsIgnoreCase(i+"")){
            l.delete(i); break;
         }
      }

      populateUser(rq);
   }

   private UserMaintenance getRemoteRole() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((UserMaintenanceHome) JNDIUtil.getInstance().lookup("UserMaintenanceEJB",UserMaintenanceHome.class.getName()))
            .create();
   }

   private void addRole(HttpServletRequest rq, final UserSessionView usr) throws Exception, RemoteException, ClassNotFoundException, NamingException {
      final UserRoleView usrrole = new UserRoleView();
      usrrole.markNew();
      usrrole.setStUserID(getString(rq.getParameter("userid")));
      usrrole.setStRoleID(getString(rq.getParameter("roleid")));
      RoleView rl = getRemoteRole().getRoleView(usrrole.getStRoleID());
      usrrole.setStRoleName(getString(rl.getStRoleName()));

      DTOList l = usr.getUserroles();

      if (l==null){
         l = new DTOList();
         usr.setUserroles(l);
      }

      l.add(usrrole);
   }

   public void changePassword(HttpServletRequest rq)  throws Exception {
      final String stUserID = getUserSession(rq).getStUserID();

      final UserSessionView usv = getRemoteUser().getUserView(stUserID);
      put(rq,"USER",usv);
     // String stBranchID = getString(usv.getStBranchID());
      usv.markUpdate();
      rq.setAttribute("ACTION","VIEW");
    //  rq.setAttribute("BR",stBranchID);
      populateUser(rq);
   }

   public void setPassword(HttpServletRequest rq)  throws Exception {
      final String stUserID = deBlank(rq.getParameter("usrid"));
      System.out.println("Change Password with User ID : "+ stUserID);
      final UserSessionView usv = getRemoteUser().getUserView(stUserID);
      put(rq,"USER",usv);
     // String stBranchID = getString(usv.getStBranchID());
      usv.markUpdate();
      rq.setAttribute("ACTION","VIEW");
   //   rq.setAttribute("BR",stBranchID);
      populateUser(rq);
   }

   public void savePassword(HttpServletRequest rq)  throws Exception {
      String encryptPswd = null;
      String action = getString(rq.getParameter("ACTION"));

      final UserSessionView usv =(UserSessionView) get(rq,"USER");
      if (action.equalsIgnoreCase("CHANGE")) {
          String oldPassword = getString(rq.getParameter("oldpassword"));
          String passwd = getString(usv.getStPasswd());


          System.out.println(passwd + " - " + oldPassword);
          if (passwd == null) {
              if (!(oldPassword == null)) {
                 rq.setAttribute("RESULT","FAIL");
                 rq.setAttribute("ACTION",action);
                 return;
              }
          } else if (oldPassword == null && passwd != null) {
             rq.setAttribute("RESULT","FAIL");
             rq.setAttribute("ACTION",action);
             return;
          } else if (!(Digest.computeDigest(usv.getStUserID(), oldPassword).equals(usv.getStPasswd()))) {
             rq.setAttribute("RESULT","FAIL");
             rq.setAttribute("ACTION",action);
             return;
          }
      }
      System.out.println("Save Password.....");
      String password = getString(rq.getParameter("newpassword"));

      StringTools.validatePassword(password);

      usv.setStUserID(getString(rq.getParameter("usrid")).toLowerCase());
      if (password != null)
         encryptPswd = Digest.computeDigest(usv.getStUserID(),password);
      usv.setStPasswd(encryptPswd);
      usv.setUserSession(getUserSession(rq));

      if (getRemoteUser().isPasswordHasUsed(usv)){
         throw new Exception("Password pernah dipakai sebelumnya, gunakan password lain");
      }

      int batasMaxHari = 30;
      DateTime maximumPasswordActive = new DateTime();
      maximumPasswordActive = maximumPasswordActive.plusDays(batasMaxHari);

      usv.setDtInActiveDate(maximumPasswordActive.toDate());
      usv.setDtLastPasswordChange(new Date());

      String result = getRemoteUser().savePassword(usv);
      rq.setAttribute("RESULT",result);
      rq.setAttribute("ACTION",action);
      rq.setAttribute("USER",usv);
   }

   public UserSessionView getUserFromRequest(HttpServletRequest rq)  throws Exception {
      final UserSessionView usv = (UserSessionView) get(rq,"USER");
      String encryptPswd = "";
      String usr_id = getString(rq.getParameter("userid"));
      String stRoles[] = rq.getParameterValues("roles") ;
      String password=null;
      if (rq.getParameter("ACTION").equals("CREATE")) {
          password = getString(rq.getParameter("password"));
          if (password != null)
             encryptPswd = Digest.computeDigest(usr_id,password);

          if ( usr_id != null) {
             usv.setStUserID(getString(rq.getParameter("userid")).toLowerCase());
          }
          usv.setStPasswd(encryptPswd);
          usv.setStTempPassword(password);
      } else {
          usv.setStUserID(getString(rq.getParameter("usrid")).toLowerCase());
      }
     // usv.setStUserType(getString(rq.getParameter("usertype")));
      usv.setStUserName(getString(rq.getParameter("username")));
    //  usv.setStBranchID(getString(rq.getParameter("branchid")));
      usv.setStDivision(getString(rq.getParameter("division")));
      usv.setStDepartment(getString(rq.getParameter("department")));
      usv.setStEmail(getString(rq.getParameter("email")));
      usv.setStPhone(getString(rq.getParameter("phone")));
      usv.setStMobileNumber(getString(rq.getParameter("mobile")));
      usv.setStContactNum(getString(rq.getParameter("contactnum")));
      usv.setStVendorID(getString(rq.getParameter("vendorid")));
      usv.setStBranch(getString(rq.getParameter("branch")));
    //  usv.setStDepoID(getString(rq.getParameter("depoid")));
    //  usv.setStWarehouseID(getString(rq.getParameter("warehouseid")));
      usv.setDtActiveDate(getDate(rq.getParameter("activedate")));
      usv.setDtInActiveDate(getDate(rq.getParameter("inactivedate")));

      final DTOList usrrole = usv.getUserroles();

      if (usrrole!=null)
      for (int i=0; i<usrrole.size(); i++){
         UserRoleView urv = (UserRoleView) usrrole.get(i);

         if (rq.getParameter("ACTION").equals("CREATE")) {
            urv.setStUserID(getString(rq.getParameter("userid")));
         } else {
            urv.setStUserID(getString(rq.getParameter("usrid")));
         }
         urv.setStRoleID(getString(rq.getParameter("roles"+i)));
      }

      return usv;
   }

   public void populateUser(HttpServletRequest rq)  throws Exception {
      final UserSessionView us = (UserSessionView) get(rq,"USER");
      final Boolean vendorStat = (Boolean)get(rq,"VendorStatus");
      final boolean isAdmin = "admin".equalsIgnoreCase(us.getStUserID());

     // final String stBranchID = us.getStBranchID();
      rq.setAttribute("isVendor",vendorStat);
      rq.setAttribute("USER", us);
      rq.setAttribute("ROLE", getRemoteRole().getRoleCombo());
     //rq.setAttribute("VENDOR", getRemoteUser().getVendorCombo());

   }

   public void switchVendor(HttpServletRequest rq)  throws Exception {
      final UserSessionView us = (UserSessionView) getUserSession(rq);

      final String vendorID = getString(rq.getParameter("vendor"));

   }


}

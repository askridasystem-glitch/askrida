/**
 * Created by IntelliJ IDEA.
 * User: dono
 * Date: Jun 17, 2006
 * Time: 3:45:22 PM
 * To change this template use Options | File Templates.
 */
package com.crux.login.form;

import com.crux.web.form.Form;
import com.crux.login.ejb.UserMaintenance;
import com.crux.login.ejb.UserMaintenanceHome;
import com.crux.login.model.RoleView;
import com.crux.login.model.FunctionsView;
import com.crux.login.model.FuncRoleView;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public class RoleForm extends Form {
   private UserMaintenance getRemoteRole() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((UserMaintenanceHome)JNDIUtil.getInstance().lookup("UserMaintenanceEJB", UserMaintenanceHome.class.getName()))
              .create();
   }

   private RoleView role;
   private FunctionsView[] root ;
   private FunctionsView[] sub1 ;
   private FunctionsView[] sub2 ;
   private FunctionsView[] sub3 ;
   private FunctionsView funcView;
   private DTOList listFunction;
   private DTOList listFunctionRole;

   //-------
   public FunctionsView[] getRoot() {
      return root;
   }

   public void setRoot(FunctionsView[] root) {
      this.root = root;
   }

   public FunctionsView[] getSub1() {
      return sub1;
   }

   public void setSub1(FunctionsView[] sub1) {
      this.sub1 = sub1;
   }

   public FunctionsView[] getSub2() {
      return sub2;
   }

   public void setSub2(FunctionsView[] sub2) {
      this.sub2 = sub2;
   }

   public FunctionsView[] getSub3() {
      return sub3;
   }

   public void setSub3(FunctionsView[] sub3) {
      this.sub3 = sub3;
   }

   /*public void fillFuncRole(DTOList listFunc, DTOList funcrole) {
      String selected = "";
      int nRoot = 0;
      int nSub1 = 0;
      int nSub2 = 0;
      int nSub3 = 0;
      for (int i = 0;i < listFunc.size();i++) {
         FunctionsView fv = (FunctionsView) listFunc.get(i);
         String kode = fv.getStFunctionID();

         if (funcrole != null) {
            for (int j = 0;j < funcrole.size();j++) {
               FuncRoleView frv = (FuncRoleView)funcrole.get(j);
               if (frv.getStFuncID().equals(kode)) {
                  selected = "checked";
                  break;
               }

               if (kode.substring(3).equals("00.00.00")) {
                  nRoot++;
                  getRoot()[nRoot].setStFunctionID(kode);
               } else if (kode.substring(6).equals("00.00")) {
                  nSub1++;
                  getSub1()[nSub1].setStFunctionID(kode);
               } else if (kode.substring(9).equals("00")) {
                  nSub2++;
                  getSub2()[nSub2].setStFunctionID(kode);
               } else if (!kode.substring(9).equals("00")) {
                  nSub3++;
                  getSub3()[nSub3].setStFunctionID(kode);
               }
            }
         }
      }
   }
   */
   //-------
   public DTOList getListFunctionRole() {
      return listFunctionRole;
   }

   public void setListFunctionRole(DTOList listFunctionRole) {
      this.listFunctionRole = listFunctionRole;
   }

   public FunctionsView getFuncView() {
      return funcView;
   }

   public void setFuncView(FunctionsView funcView) {
      this.funcView = funcView;
   }

   public DTOList getListFunction() throws Exception {
      return listFunction;
   }

   public void setListFunction(DTOList listFunction) {
      this.listFunction = listFunction;
   }


   public RoleView getRole() {
      return role;
   }

   public void setRole(RoleView role) {
      this.role = role;
   }

   public void createNew() {
      role = new RoleView();
      role.markNew();
   }

   public void doSave() throws Exception {
      //-------------------
      int arrLength1 = 0;
      int arrLength2 = 0;
      int arrLength3 = 0;
      int arrLength4 = 0;

      String arr1[];
      if (getAttribute("root") instanceof String)
      {
         String arr = (String)getAttribute("root");
         arr1 = new String[1];
         arr1[0]=arr;
      } else
      {
         arr1 = (String[])getAttribute("root");
      }

      String arr2[];
      if (getAttribute("sub1") instanceof String)
      {
         String arr = (String)getAttribute("sub1");
         arr2 = new String[1];
         arr2[0]=arr;
      } else
      {
         arr2 = (String[])getAttribute("sub1");
      }

      String arr3[];
      if (getAttribute("sub2") instanceof String)
      {
         String arr = (String)getAttribute("sub2");
         arr3 = new String[1];
         arr3[0]=arr;
      } else
      {
         arr3 = (String[])getAttribute("sub2");
      }

      String arr4[];
      if (getAttribute("sub3") instanceof String)
      {
         String arr = (String)getAttribute("sub3");
         arr4 = new String[1];
         arr4[0]=arr;
      } else
      {
         arr4 = (String[])getAttribute("sub3");
      }

      if (arr1 != null) arrLength1 = arr1.length;
      if (arr2 != null) arrLength2 = arr2.length;
      if (arr3 != null) arrLength3 = arr3.length;
      if (arr4 != null) arrLength4 = arr4.length;

      for (int i = 0; i < arrLength1; i++) {
         addRole1(arr1, getRole(), i);
      }
      for (int i = 0; i < arrLength2; i++) {
         addRole2(arr2, getRole(), i);
      }
      for (int i = 0; i < arrLength3; i++) {
         addRole3(arr3, getRole(), i);
      }
      for (int i = 0; i < arrLength4; i++) {
         addRole4(arr4, getRole(), i);
      }

      getRemoteRole().save(role);
      close();
   }

   private void addRole1(String[] arr, final RoleView rv, int i) {
      final FuncRoleView frv = new FuncRoleView();
      frv.markNew();
      frv.setStRoleID(getRole().getStRoleID());
      frv.setStFuncID(arr[i]);


      DTOList l = rv.getChecbox1();

      if (l == null) {
         l = new DTOList();
         rv.setChecbox1(l);
      }

      l.add(frv);
   }

   public void doCancel() {
      close();
   }

   private void addRole2(String[] arr, final RoleView rv, int i) {
      final FuncRoleView frv = new FuncRoleView();
      frv.markNew();
      frv.setStRoleID(getRole().getStRoleID());
      frv.setStFuncID(arr[i]);


      DTOList l = rv.getChecbox2();

      if (l == null) {
         l = new DTOList();
         rv.setChecbox2(l);
      }

      l.add(frv);
   }

   private void addRole3(String[] arr, final RoleView rv, int i) {
      final FuncRoleView frv = new FuncRoleView();
      frv.markNew();
      frv.setStRoleID(getRole().getStRoleID());
      frv.setStFuncID(arr[i]);


      DTOList l = rv.getChecbox3();

      if (l == null) {
         l = new DTOList();
         rv.setChecbox3(l);
      }

      l.add(frv);
   }

   private void addRole4(String[] arr, final RoleView rv, int i) {
      final FuncRoleView frv = new FuncRoleView();
      frv.markNew();
      frv.setStRoleID(getRole().getStRoleID());
      frv.setStFuncID(arr[i]);
      DTOList l = rv.getChecbox4();

      if (l == null) {
         l = new DTOList();
         rv.setChecbox4(l);
      }

      l.add(frv);
   }

   public void view(String roleid) throws Exception {
      role = getRemoteRole().getRoleView(roleid);
      super.setReadOnly(true);
   }

   public void edit(String roleid) throws Exception {
      role = getRemoteRole().getRoleView(roleid);

      role.markUpdate();
   }
}

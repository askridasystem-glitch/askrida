<%@ page import="com.crux.login.form.UserForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="USER" >
<%
   UserForm form = (UserForm)frame.getForm();
%>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <c:field hidden="true" name="userIndex"/>
            <c:field width="200" name="user.stUserID" mandatory="true" caption="User Id" type="string" presentation="standard" />
            <c:field width="200" name="user.stUserName" mandatory="true" caption="User Name" type="string" presentation="standard" />
            <c:field width="200" name="user.stDivision" caption="Division" type="string" presentation="standard"  />
            <c:field width="200" name="user.stDepartment" caption="Departement" type="string" presentation="standard"/>
            <c:field width="200" name="user.stEmail" caption="Email" type ="string" presentation="standard" />

            <%--<c:field width="200" name="user.stPasswd" mandatory="true" caption="Password" type="password" presentation="standard" />
            <c:field width="200" name="user.stTempPassword" mandatory="true" caption="Retype-Password" type="password" presentation="standard" />--%>
            <c:field width="200" name="user.stPhone" caption="Phone" type="string" presentation="standard" />
            <c:field width="200" name="user.stMobileNumber" caption="Mobile Number" type="string" presentation="standard" />
            <c:field width="200" name="user.stContactNum" caption="Contact Number" type="string" presentation="standard" />

            <c:field width="200" name="user.dtActiveDate" caption="Active Date" type="date" presentation="standard" />
            <c:field width="200" name="user.dtInActiveDate" caption="Expire Date" type="date" presentation="standard" />

            <c:field width="200" caption="Branch" lov="LOV_Branch" popuplov="true" name="user.stBranch" type="string" presentation="standard" />

            <c:listbox name="user.userroles" >
               <c:listcol title="" columnClass="header" ><c:button enabled="true" text="+" event="addRole"/></c:listcol>
               <c:listcol title="" columnClass="detail" ><c:button enabled="true" text="-" event="deleteRole" clientEvent="f.userIndex.value='$index$'"/></c:listcol>
               <c:listcol title="ID Grup Akses" >
                  <c:field name="user.userroles.[$index$].stRoleID" width="200" lov="LOV_Role" popuplov="true" type="string" caption="ID Grup Akses" mandatory="true" />
               </c:listcol>
            </c:listbox>
         </table>
      </td>
   </tr>
   <tr>
      <td align=center>
         <c:button text="Submit"  event="doSave" show="<%=!form.isReadOnly()%>" validate="true" />
         <c:button text="Cancel"  event="doCancel" show="<%=!form.isReadOnly()%>"/>
         <c:button text="Back"  event="doCancel" show="<%=form.isReadOnly()%>"/>
      </td>
   </tr>
</table>
</c:frame>
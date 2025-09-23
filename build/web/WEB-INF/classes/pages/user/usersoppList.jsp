<%@ page import="com.crux.login.form.UserListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="USER LIST">
<%
   UserListForm form = (UserListForm)frame.getForm();
%>

<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
         <c:field name="searchKey" caption="User/Branch" type="string" presentation="standard"/>
         </table>
      </td>
   </tr>
   <tr>
      <td>
         <c:button text="Filter" event="getListUser"/>
      </td>
   </tr>
   <tr>
      <td>
         <c:listbox name="listUser" selectable="true" paging="true" >
            <c:listcol name="stUserID" title="" selectid="userid"/>
            <c:listcol name="stUserID" title="User ID" />
            <c:listcol name="stUserName" title="User Name" />
            <c:listcol name="stBranchName" title="Branch" />
            <c:listcol name="stEmail" title="Email Address" />
            <c:listcol name="dtLastLogin" title="Last Login" />
         </c:listbox>
      </td>
   </tr>
   <tr>
      <td>
         <c:button  text="CREATE USER" event="clickCreate" resourceid="USER_CREATE" />
         <c:button  text="EDIT USER" event="clickEdit" resourceid="USER_EDIT" />
         <c:button  text="VIEW USER" event="clickView" />
         <c:button  text="DELETE USER" event="clickDelete"  resourceid="USER_DELETE" />
         <c:button  text="CHANGE PASSWORD" event="clickChangePassword" resourceid="USER_PASSWORD" />
      </td>
   </tr>
</table>
</c:frame>
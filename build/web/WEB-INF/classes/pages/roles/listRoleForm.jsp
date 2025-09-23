<%@ page import="com.crux.login.form.RoleListForm"%>
<%@ taglib prefix="c" uri="crux" %>
<c:frame title="ROLE LIST">
<%
   RoleListForm form = (RoleListForm)frame.getForm();
%>

<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
         <c:field name="searchKey" caption="Role" type="string" presentation="standard"/>
         </table>
      </td>
   </tr>
   <tr>
      <td>
         <c:button text="Filter" event="getListRole"/>
      </td>
   </tr>
   <tr>
      <td>
         <c:listbox name="listRole" selectable="true" paging="true" >
            <c:listcol name="stRoleID" title="" selectid="roleid"/>
            <c:listcol name="stRoleID" title="Role ID" />
            <c:listcol name="stRoleName" title="Role Name" />
         </c:listbox>
      </td>
   </tr>
   <tr>
      <td>
         <c:button  text="CREATE ROLE" event="clickCreate"/>
         <c:button  text="EDIT ROLE" event="clickEdit" />
         <c:button  text="VIEW ROLE" event="clickView" />
      </td>
   </tr>
</table>
</c:frame>
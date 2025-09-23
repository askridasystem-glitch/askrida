<%@ page import="com.crux.login.form.UserListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="USER LIST">
<%
   UserListForm form = (UserListForm)frame.getForm();
%>

<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
         <c:field name="searchKey" caption="User ID/Name/Branch" type="string" width="250" presentation="standard"/>
          <c:field name="showDeleted" caption="Show Deleted" type="check" width="250" presentation="standard"/>
         </table>
      </td>
   </tr>
   <tr>
      <td>
         <c:button text="Refresh" event="getListUser"/>
      </td>
   </tr>
   <tr>
      <td>
         <c:listbox name="listUser" selectable="true" paging="true" >
            <c:listcol name="stUserID" title="" selectid="userid"/>
            <c:listcol name="stUserID" title="User ID" />
            <c:listcol name="stUserName" title="{L-ENGUser Name-L}{L-INANama User-L}" />
            <c:listcol name="stBranchName" title="{L-ENGBranch-L}{L-INACabang-L}" />
            <c:listcol name="stBranchSourceName" title="{L-ENGBranch-L}{L-INACabang-L} Penerbit" />
            <c:listcol name="stEmail" title="{L-ENGEmail Address-L}{L-INAAlamat Email-L}" />
            <c:listcol name="dtLastLogin" title="{L-ENGLast Login-L}{L-INALogin Terakhir-L}" />
         </c:listbox>
      </td>
   </tr>
   <tr>
      <td>
         <c:button  text="{L-ENGCREATE USER-L}{L-INABUAT USER-L}" event="clickCreate" resourceid="USER_CREATE" />
         <c:button  text="{L-ENGEDIT USER-L}{L-INAUBAH USER-L}" event="clickEdit" resourceid="USER_EDIT" />
         <c:button  text="{L-ENGVIEW USER-L}{L-INALIHAT USER-L}" event="clickView" />
         <c:button  text="{L-ENGDELETE USER-L}{L-INAHAPUS USER-L}" event="clickDelete"  resourceid="USER_DELETE" />
         <c:button  text="{L-ENGCHANGE PASSWORD-L}{L-INAUBAH PASSWORD-L}" event="clickChangePassword" resourceid="USER_PASSWORD" />
      </td>
   </tr>
</table>
</c:frame>
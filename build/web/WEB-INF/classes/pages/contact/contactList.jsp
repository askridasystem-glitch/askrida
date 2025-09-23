<%@ page import="com.crux.web.controller.SessionManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="CONTACT LIST" >
<%
   boolean ENT_MASTER_NAV_BR = SessionManager.getInstance().hasResource("ENT_MASTER_NAV_BR");
%>
<table cellpadding=2 cellspacing=1>
   
   <tr>
      <td>
         <c:listbox autofilter="true" name="list" selectable="true" paging="true" view="com.webfin.entity.model.EntityView" >
            <c:listcol name="stEntityID" title="ID" selectid="entityid"/>
            <c:listcol filterable="true" name="stEntityClass" title="Class" />
            <c:listcol filterable="true" name="stEntityName" title="Name" />
            <c:listcol filterable="true" name="stAddress" title="Address" />
         </c:listbox>
      </td>
   </tr>
   <tr>
      <td>
         <c:button  text="Create" event="clickCreate" />
         <c:button  text="Edit" event="clickEdit" />
		 <c:button  text="Delete" event="clickDelete" />
         <c:button  text="View" event="clickView" />
      </td>
   </tr>
</table>
</c:frame>
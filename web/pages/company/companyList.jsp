<%@ page import="com.crux.web.controller.SessionManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="DATA GRUP PERUSAHAAN" >
<%
   boolean ENT_MASTER_NAV_BR = SessionManager.getInstance().hasResource("ENT_MASTER_NAV_BR");
%>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <c:listbox autofilter="true" name="list" selectable="true" paging="true" view="com.webfin.company.model.CompanyView" >
            <c:listcol name="stVSCode" title="ID" selectid="companyid"/>
            <c:listcol filterable="true" name="stVSCompanyGroup" title="{L-ENGName-L}{L-INANama-L}" />
         </c:listbox>
      </td>
   </tr>
   <tr>
      <td>
         <c:button  text="{L-ENGCreate-L}{L-INABuat-L}" event="clickCreate" />
         <c:button  text="{L-ENGEdit-L}{L-INAUbah-L}" event="clickEdit" />
         <c:button  text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
      </td>
   </tr>
</table>
</c:frame>
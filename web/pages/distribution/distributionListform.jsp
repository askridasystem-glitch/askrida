<%@ page import="com.crux.distribution.form.DistributionListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Distribution">
<%
   DistributionListForm form = (DistributionListForm)frame.getForm();
%>

<table cellpadding=2 cellspacing=1>
   
<tr>
      <td>
         <table cellpadding=2 cellspacing=1>
         <c:field name="searchKey" caption="Dist ID/Dist Name" type="string" presentation="standard"/>
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
            <c:listcol name="stIdDist" title="" selectid="refno" />
            <c:listcol name="stIdDist" title="Distribution ID" />
            <c:listcol name="stDistName" title="Distribution Name" />
         </c:listbox>
        
         <%--
         <c:listbox autofilter="true" name="listUser" selectable="true" paging="true" view="com.crux.incoming.model.UserSessionView" >
            <c:listcol name="stRefNo" title="ID" selectid="refno"/>
            <c:listcol filterable="true" name="stSender" title="Sender" />
            <c:listcol filterable="true" name="stEntityName" title="Subject" />
         </c:listbox>
          --%>
         
      </td>
   </tr>
   <tr>
      <td>
      	  
         <c:button  text="CREATE" event="clickCreate" />
         <c:button  text="EDIT" event="clickEdit" />
         <c:button  text="VIEW" event="clickView" />
         <c:button  text="DELETE" event="clickDelete" />
         <%--
         <c:button  text="CREATE USER" event="clickCreate" resourceid="INCOMING_CREATE" />
         <c:button  text="EDIT USER" event="clickEdit" resourceid="INCOMING_EDIT" />
         <c:button  text="VIEW USER" event="clickView" />
         <c:button  text="DELETE USER" event="clickDelete"  resourceid="INCOMING_DELETE" />
      --%>
      </td>
   </tr>
</table>
</c:frame>
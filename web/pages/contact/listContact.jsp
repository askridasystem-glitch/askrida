<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.contact.model.ContactView"%>
<%
   final JSPUtil jspUtil = new JSPUtil(request,response,"ENTITY LIST");

   final DTOList list = (DTOList) request.getAttribute("LIST");
%>
<input type=hidden name=ent_id>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <%=jspUtil.getPager(request,list)%>
         <table cellpadding=2 cellspacing=1>
            <tr class=header>
               <td></td>
               <td>ID tes</td>
               <td>NAME</td>
               <td>TYPE</td>
               <td>ADDRESS</td>
            </tr>
<%
   for (int i = 0; i < list.size(); i++) {
      EntityView ent = (EntityView) list.get(i);
%>
            <tr class=row<%=i%2%>>
               <td><input type=radio name=x onclick="f.ent_id.value='<%=jspUtil.print(ent.getStEntityID())%>'"></td>
               <td><%=jspUtil.print(ent.getStEntityID())%></td>
               <td><%=jspUtil.print(ent.getStEntityName())%></td>
               <td><%=jspUtil.print(ent.getStEntityType())%></td>
               <td></td>
            </tr>
<%
   }
%>
         </table>
      </td>
   </tr>
   <tr>
      <td>
         <%=jspUtil.getButtonEvent("Create","entity_edit.createNew.crux")%>
         <%=jspUtil.getButtonEvent("Edit","entity_edit.edit.crux")%>
         <%=jspUtil.getButtonEvent("View","entity_edit.view.crux")%>
      </td>
   </tr>
</table>
<script>

   function changePageList() {
      f.EVENT.value='ENT_LIST';
      f.submit();
   }

</script>
<%=jspUtil.release()%>

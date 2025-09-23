<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.login.model.RoleView"%>
<html>
<%
    final JSPUtil jspUtil = new JSPUtil(request, response);
    final DTOList rolelist = (DTOList)request.getAttribute("ROLE_LIST");
%>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
   <script>
   var oid;

   function selectRole(id) {
      form1.bview.disabled = false;
      form1.bedit.disabled = false;
      oid = id;
      form1.rlid.value = oid;
   }

	function remove(){

      if (oid==null) alert('Please check at least one Role to be deleted')
		else {
			msg = confirm('Are you sure to delete this Role?');
				if (msg){
					form1.EVENT.value='ROLE_DELETE';
					form1.submit();
				} else {
               form1.EVENT.value='ROLE_LIST';
					form1.submit();
            }
		}

	}
   </script>
   <body>
      <form name=form1 method=POST action="ctl.ctl">
	     <input type=hidden name=rlid>
         <input type=hidden name=EVENT value=SELECT_REF_TYPE>
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("LIST OF ROLE")%>
            <tr>
               <td>
                  <%=jspUtil.getPager(request, rolelist)%>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
					    <td width=10></td>
                        <td width=100 align="center">
                            <%=jspUtil.getSortHeader(request, rolelist, "Role ID","role_id")%>
                        </td>
                        <td width=200 align="center">
                            <%=jspUtil.getSortHeader(request, rolelist, "Role Name","role_name")%>
                        </td>
                     </tr>
<%
   if  (rolelist == null) {
       System.out.println("no record");
   } else {
   for (int i = 0; i < rolelist.size(); i++) {
      RoleView rl = (RoleView) rolelist.get(i);
%>
                     <tr class=row<%=i%2%>>
					    <td>
				             <input type="radio" name="rolerid" value="<%=jspUtil.print(rl.getStRoleID())%>" onClick="selectRole('<%=jspUtil.print(rl.getStRoleID())%>')">
			            </td>
                        <td><%=JSPUtil.print(rl.getStRoleID())%></td>
                        <td><%=JSPUtil.print(rl.getStRoleName())%></td>
                     </tr>
<% }
   }%>
                  </table>
               </td>
            </tr>
         </table>
		 </table>
         <%=jspUtil.getPager(request, rolelist)%>
<table cellpadding=2 cellspacing=1>
<tr>
	<td>
	<input type="submit" value="Create Role" onClick="form1.EVENT.value='ROLE_CREATE'">
	</td>
	<td>
	<td>
    <input type="submit" disabled name=bedit value="Edit Role" onClick="form1.EVENT.value='ROLE_EDIT'">
	</td>
    <td>
	<input type="submit" disabled name=bview value="View" onClick="form1.EVENT.value='ROLE_VIEW'">
	</td>
	<td>&nbsp;</td>
</tr>
</table>

</form>
</body>
<script>
function changePageList() {
      form1.EVENT.value='ROLE_LIST';
      form1.submit();
   }
</script>
</html>

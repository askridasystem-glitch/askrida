<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.valueset.model.ValueSetView"%>
<html>
<%
    final JSPUtil jspUtil = new JSPUtil(request, response);
    final DTOList valuelist = (DTOList)request.getAttribute("VALUE_LIST");
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
					form1.EVENT.value='VS_DELETE';
					form1.submit();
				} else {
               form1.EVENT.value='VS_LIST';
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
            <%=jspUtil.getHeader("LIST OF VALUE SET")%>
            <tr>
               <td>
                  <%=jspUtil.getPager(request, valuelist)%>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
					    <td width=10></td>
                        <td width=100 align="center">
                            <%=jspUtil.getSortHeader(request, valuelist, "VALUE SETS","VS_GROUP")%>
                        </td>

                     </tr>
<%
   if  (valuelist == null) {
       System.out.println("no record");
   } else {
   for (int i = 0; i < valuelist.size(); i++) {
      ValueSetView vs = (ValueSetView) valuelist.get(i);
%>
                     <tr class=row<%=i%2%>>
					    <td>
				             <input type="radio" name="vsid" value="<%=jspUtil.print(vs.getStVGroup())%>" onClick="selectRole('<%=jspUtil.print(vs.getStVGroup())%>')">
			            </td>
                        <td><%=JSPUtil.print(vs.getStVGroup())%></td>

                     </tr>
<% }
   }%>
                  </table>
               </td>
            </tr>
         </table>
		 </table>
         <%=jspUtil.getPager(request, valuelist)%>
<table cellpadding=2 cellspacing=1>
<tr>
	<td>
	<input type="submit" value="Create ValueSet" onClick="form1.EVENT.value='VS_CREATE'">
	</td>
	<td>
	<td>
    <input type="submit" disabled name=bedit value="Edit ValueSet" onClick="form1.EVENT.value='VS_EDIT'">
	</td>
    <td>
	<input type="submit" disabled name=bview value="View" onClick="form1.EVENT.value='VS_VIEW'">
	</td>
	<td>&nbsp;</td>
</tr>
</table>

</form>
</body>
<script>
function changePageList() {
      form1.EVENT.value='VS_LIST';
      form1.submit();
   }
</script>
</html>

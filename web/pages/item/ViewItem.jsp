<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 java.util.Date,
                 com.crux.login.model.UserSessionView,
                 com.crux.login.model.UserRoleView,
                 com.crux.login.model.UserVendorView,
                 com.ots.item.model.ItemView"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type='text/css' rel=STYLESHEET>
<head>
<%
   ItemView item = (ItemView) request.getAttribute("ITEM");
   String id = "";
   String sTitle = (String) request.getAttribute("ACTION");
   System.out.println("Action = " + sTitle);

%>
</head><body>
<form name=form1 action="item.ctl" method=POST>
<input type=hidden name=itmid value="<%=id%>">
<input type=hidden name=ACTION value="<%=sTitle%>">
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
<script language="javascript">

</script>
<table cellpadding=1 cellspacing=1>
<%=jspUtil.getHeader("VIEW ITEM MASTER")%>
<tr>
	<td width="30%"> </td>
</tr>
<tr>
	<td class="row0">
		Vendor
	</td>
	<td >
    <%=jspUtil.print(item.getStVendorName())%>
 	</td>
</tr>
<tr>
	<td class="row0">
		Item Code
	</td>
	<td >
    <%=jspUtil.print(item.getStItemCode())%>
 	</td>
</tr>
<tr>
	<td class="row0">
        Item Description
	</td>
	<td >
    <%=jspUtil.print(item.getStItemDesc())%>
 	</td>
</tr>
<tr>
	<td class="row0">
        Tarif HS
	</td>
	<td >
    <%=jspUtil.print(item.getDbTarifHS())%>
 	</td>
</tr>
<br>

</table>
<table cellspacing=1 cellpadding=1>
<tr>
   <td colspan=3 width=600></td>
</tr>
<tr>
	<td colspan=2>
      <input type=hidden name=EVENT value=FIRST_ITEM_LIST>
      <input class=button type=submit value="  Close  ">
	</td>
</tr>
</table>
</form>
</body>
</html>

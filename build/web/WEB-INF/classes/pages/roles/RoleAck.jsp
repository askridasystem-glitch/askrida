<%@ page import="com.crux.util.JSPUtil
                 "
%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<head>
</head>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type=text/css rel=STYLESHEET>
<script language="JavaScript">
<!--

	function list(){
		document.form1.action="<%=jspUtil.getPagesPath()%>/roles/listRole.jsp";
		document.form1.submit();
	}
-->
</script>
<body>
<%
   String result = (String) request.getAttribute("RESULT");
   String toggle = (String) request.getAttribute("ACTION");
   String sComment = "";
   if (toggle==null){
      if (result.equalsIgnoreCase("FAIL")) sComment = "Role has exist. Please enter another Role.";
      else sComment = "Role has been saved successfully. Your Role ID is " + result;
   } else if (toggle.equalsIgnoreCase("ROLE_DELETE")) {
      if (result.equalsIgnoreCase("FAIL")) sComment = "You cannot delete this Role since it is being linked to other module in the system.";
      else sComment = "Role has been deleted";
   }
%>
<table cellpadding=0 cellspacing=0>
<form name=form1 method=post action=role.ctl>
<%=jspUtil.getHeader("ROLE MANAGEMENT")%>
<tr>
	<td>
		<%=sComment%>
		<br>
		<br>
	</td>
</tr>
<tr>
	<td colspan=2>
		<input type=hidden name=EVENT value=ROLE_LIST>
      <input class=button type=submit value="Back to Role List">
	</td>
</tr>
</form>
</table>
</body>
</html>
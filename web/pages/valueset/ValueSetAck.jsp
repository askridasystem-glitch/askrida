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
   //String result = (String) request.getAttribute("RESULT");
   String toggle = (String) request.getAttribute("ACTION");
   String sComment = "";
   if (toggle==null){
     // if (result.equalsIgnoreCase("FAIL")) sComment = "Group/Code name has exist. Please enter another Group/code Name.";
     // else
       sComment = "Value Set has been saved successfully. ";
   } else if (toggle.equalsIgnoreCase("DELETE")) {
      //if (result.equalsIgnoreCase("FAIL")) sComment = "You cannot delete this Value Set since it is being linked to other module in the system.";
      //else
      sComment = "Value Set has been deleted";
   }
%>
<table cellpadding=0 cellspacing=0>
<form name=form1 method=post action=role.ctl>
<%=jspUtil.getHeader("VALUE SET")%>
<tr>
	<td>
		<%=sComment%>
		<br>
		<br>
	</td>
</tr>
<tr>
	<td colspan=2>
		<input type=hidden name=EVENT value=VS_LIST_GO>
      <input class=button type=submit value="Back to Value Set List">
	</td>
</tr>
</form>
</table>
</body>
</html>
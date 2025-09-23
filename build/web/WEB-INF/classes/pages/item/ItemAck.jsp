<%@ page import="com.crux.util.JSPUtil"
%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<head>
</head>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type=text/css rel=STYLESHEET>
<script language="JavaScript">
<!--
	function list(){
		document.form1.action="<%=jspUtil.getPagesPath()%>/item/listItem.jsp";
		document.form1.submit();
	}
-->
</script>
<body>
<%
   String result = (String) request.getAttribute("RESULT");
   String toggle = (String) request.getAttribute("ACTION");
   String event = null;
   System.out.println("action : " + toggle);
   String sComment = "";
   if (toggle.equalsIgnoreCase("CREATE")){
      if (result.equalsIgnoreCase("FAIL")) sComment = "Item has exist. Please enter another Item.";
      else sComment = "Item has been saved successfully. Your Item ID is " + result;

      event = "ITEM_CREATE";
   } else if(toggle.equalsIgnoreCase("EDIT")){
       if (result.equalsIgnoreCase("FAIL")) sComment = "Item has exist. Please enter another Item.";
      else sComment = "Item has been edited successfully.";
      event = "ITEM_LIST";
   }else if (toggle.equalsIgnoreCase("DELETE")) {
      if (result.equalsIgnoreCase("FAIL")) sComment = "You cannot delete this Item since it is being linked to other module in the system.";
      else sComment = "Item has been deleted";
      event = "ITEM_LIST";
   }
   System.out.println("Comment : " + sComment);
%>
<table cellpadding=0 cellspacing=0>
<form name=form1 method=post action=user.ctl>
<%=jspUtil.getHeader("ITEM CONFIRMATION")%>
<tr>
	<td>
		<%=sComment%>
		<br>
		<br>
	</td>
</tr>
<tr>
<td colspan=2>
		<input type=hidden name=EVENT value=<%=event%>>
      <input class=button type=submit value="Back">
	</td>
</tr>
</form>
</table>
</body>
</html>
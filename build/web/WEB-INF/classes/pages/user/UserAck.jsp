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
		document.form1.action="<%=jspUtil.getPagesPath()%>/user/listUser.jsp";
		document.form1.submit();
	}
-->
</script>
<body>
<%
   String result = (String) request.getAttribute("RESULT");
   String toggle = (String) request.getAttribute("ACTION");
   System.out.println("action : " + toggle);
   String sComment = "";
   if (toggle.equalsIgnoreCase("NEW")){
      if (result.equalsIgnoreCase("FAIL")) sComment = "User has exist. Please enter another User.";
      else sComment = "User has been saved successfully. Your User ID is " + result;
   } else if(toggle.equalsIgnoreCase("EDIT")){
       if (result.equalsIgnoreCase("FAIL")) sComment = "User has exist. Please enter another User.";
      else sComment = "User has been edited successfully.";
   }else if (toggle.equalsIgnoreCase("SET")) {
       sComment = "User " + result + " sudah berhasil mengubah password. Segera logout dan login menggunakan password baru anda";
   } else if (toggle.equalsIgnoreCase("CHANGE")) {
       if (result.equalsIgnoreCase("FAIL")) sComment = "Old Password not valid. Please enter another old Password";
       else sComment = "User " + result + " has been change Password successfully. Segera logout dan login menggunakan password baru anda";
   } else if (toggle.equalsIgnoreCase("USER_DELETE")) {
      if (result.equalsIgnoreCase("FAIL")) sComment = "You cannot delete this User since it is being linked to other module in the system.";
      else sComment = "User has been deleted";
   }
   System.out.println("Comment : " + sComment);
%>
<table cellpadding=0 cellspacing=0>
<form name=form1 method=post action=user.ctl>
<%=jspUtil.getHeader("USER MANAGEMENT")%>
<tr>
	<td>
		<%=sComment%>
		<br>
		<br>
	</td>
</tr>
<tr>
<% if (toggle.equalsIgnoreCase("NEW")||toggle.equalsIgnoreCase("EDIT") || toggle.equalsIgnoreCase("SET")||(toggle.equalsIgnoreCase("USER_DELETE"))) {%>
   <td colspan=2>
		<input type=hidden name=EVENT value=USER_LIST_DISPLAY>
      <input class=button type=submit value="Back to User List">
	</td>
<%  } else if (toggle.equalsIgnoreCase("CHANGE") && result.equalsIgnoreCase("FAIL")){%>
    <td colspan=2>
		<input type=hidden name=EVENT value=CHANGE_PASSWORD>
      <input class=button type=submit value="Back">
	</td>
<%}%>
</tr>
</form>
</table>
</body>
</html>
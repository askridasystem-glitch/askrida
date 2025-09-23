<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 java.util.Date,
                 com.crux.login.model.UserSessionView,
                 com.crux.login.validation.UserValidator"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type=text/css rel=STYLESHEET>
<head>
<%
   UserSessionView usr = (UserSessionView) request.getAttribute("USER");

   String id = usr.getStUserID();
   String br = (String) request.getAttribute("BR");
%>
</head><body>
<form name=form1 action="user.ctl" method=POST>
<input type=hidden name=EVENT value="SET_PASSWORD_SAVE">
<input type=hidden name=ACTION value="SET">
<input type=hidden name=usrid value="<%=id%>">
<input type=hidden name=branchcode>
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
<script language="javascript">

 function cekpassword() {
     if (form1.newpassword.value != form1.repassword.value) {
        return "Retype password not valid";
	 } else { return true; }
  }

</script>
<table cellpadding=1 cellspacing=1>
<%=jspUtil.getHeader("SET PASSWORD")%>
<tr>
	<td width="30%" <br></td>
</tr>
<tr>
	<td class="row0">
		User ID
	</td>
	<td >
    <%=jspUtil.print(usr.getStUserID())%>
 	</td>
</tr>
<tr>
	<td class="row1">
		User Name
	</td>
	<td >
    <%=jspUtil.print(usr.getStUserName())%>
 	</td>
</tr>
<!--tr>
	<td class="row0">
		Branch
	</td>
	<td>
	<%//jspUtil.print(usr.getStBranchName())%>
	</td>
</tr-->
<tr>
	<td class="row1">
        Division
	</td>
	<td >
    <%=jspUtil.print(usr.getStDivision())%>
 	</td>
</tr>
<tr>
	<td class="row0">
        Department
	</td>
	<td >
    <%=jspUtil.print(usr.getStDepartment())%>
 	</td>
</tr>
<tr>
	<td class="row1">
        New Password
	</td>
	<td >
    <%=jspUtil.getInputText("newpassword", UserValidator.vfRePassword, null, 200)%>
 	</td>
</tr>
<tr>
	<td class="row0">
        Retype Password
	</td>
	<td >
    <%=jspUtil.getInputText("repassword", UserValidator.vfRePassword, null, 200)%>
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
		<br><br>
      <%=jspUtil.getButtonSubmit("bsave","Submit") %>
      <%=jspUtil.getButtonNormal("bcancel","Cancel","window.location='"+jspUtil.getControllerURL("USER_LIST_DISPLAY")+"'")%>
	</td>
</tr>
</table>
</form>
<script>
 form1.repassword.validate=cekpassword
</script>
</body>
</html>

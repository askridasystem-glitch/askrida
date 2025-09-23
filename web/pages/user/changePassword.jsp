<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 java.util.Date,
                 com.crux.util.Tools,
                 com.crux.login.model.UserSessionView,
                 com.crux.login.validation.UserValidator"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type=text/css rel=STYLESHEET>
<head>
<%
   UserSessionView usr = (UserSessionView) request.getAttribute("USER");

   String id = usr.getStUserID();

   boolean expired = false;

    if (usr.getDtInActiveDate()!=null)
            if (Tools.compare(usr.getDtInActiveDate(), new Date())<0) expired = true;
%>
</head><body>
<form name=form1 action="user.ctl" method=POST>
<input type=hidden name=EVENT value="CHANGE_PASSWORD_SAVE">
<input type=hidden name=ACTION value="CHANGE">
<input type=hidden name=usrid value="<%=id%>">
<input type=hidden name=branchcode>
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
<script language="javascript">
  function cekpassword() {
      var pasww = document.form1.newpassword.value;
      if(pasww.length < 8) return "Password minimal kombinasi 8 karakter numeric";

     if (document.form1.newpassword.value != document.form1.repassword.value) {
            return "Retype password tidak sama";
	 } else { return true; }

  } 

</script>
<table cellpadding=1 cellspacing=1>
<tr>
<%=jspUtil.getHeader("CHANGE PASSWORD")%>
<tr>
	<td width="30%" ><br></td>
</tr>
<%if(expired){%>
<tr>
    <td colspan="2">
        <strong>Password sudah expired pada <%=jspUtil.print(usr.getDtInActiveDate())%>, ubah password untuk login ke sistem.<br>
        </strong>
    </td>
</tr>
<%}%>
<tr>
    <td colspan="2">
        <strong>Format : minimal 8 karakter dan mengandung karakter numerik.<br>
        </strong>
    </td>
</tr>
<tr>
    <td>
        <strong>Contoh : abcd1234, @bcdefgh5, atau 1234567m</strong>
    </td>
</tr>
<tr>
    <td>
        <strong>Masa Aktif Password 30 Hari Sejak Diubah<br></strong>
    </td>
</tr><br>
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
<tr>
	<td class="row1">
        Old Password
	</td>
	<td >
    <%=jspUtil.getInputText("oldpassword", UserValidator.vfPassword, null, 200)%>
 	</td>
</tr>
<tr>
	<td class="row0">
        New Password
	</td>
	<td >
    <%=jspUtil.getInputText("newpassword", UserValidator.vfRePassword, null, 200, JSPUtil.MANDATORY)%>
 	</td>
</tr>
<tr>
	<td class="row1">
        Retype Password
	</td>
	<td >
    <%=jspUtil.getInputText("repassword", UserValidator.vfRePassword, null, 200, JSPUtil.MANDATORY)%>
    <input type=hidden name=crap>
    <input type=hidden id=crap1>
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
      <%=jspUtil.getButtonNormal("bcancel","Cancel","form1.EVENT.value='MAIN_WELCOME';form1.submit();")%>
	</td>
</tr>
</table>
</form>
<script>
   document.getElementById('repassword').validate=cekpassword;
</script>
</body>
</html>

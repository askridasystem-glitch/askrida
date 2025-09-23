<%@ page import="com.crux.util.JSPUtil,
                 com.crux.lang.LanguageManager,
                 com.crux.util.DTOList,
                 com.crux.lang.LanguageView,
                 com.crux.util.Tools,
                 com.crux.common.parameter.Parameter"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<%
   final boolean firstTime = !"Y".equalsIgnoreCase((String) request.getSession().getAttribute("FIRST_TIME"));

   final DTOList languages = LanguageManager.getInstance().getLanguages();

    final boolean showLogo = Parameter.readBoolean("GEN_SHOW_LOGO");

   if (firstTime && false) {
      request.getSession().setAttribute("FIRST_TIME","Y");
      %>
<html>
<script language="JavaScript">

   window.open('index2.jsp','','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=yes,width=800,height=600');
   window.close();
</script>
</html>
 <%
   } else {
%>

<html>
<head>
<title></title>
<link rel="stylesheet" href="<%=jspUtil.getStyleSheetPath()%>" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>


<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" MarginWidth="0" MarginHeight="0">
<form name=loginform method=POST action="login.ctl">
<input type=hidden name=EVENT value=LOGIN_MAIN>
<table height="100%" width="100%" cellpadding=2 cellspacing=1>
   <tr height="600">
      <td align=center valign=middle>
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
              
        <%--  <img src="images/lgo_front.jpg">--%>
        <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0" width="1000" height="550" id="sub" align="middle">
		<param name="allowScriptAccess" value="sameDomain" />
		<param name="movie" value="<%=jspUtil.getImagePath()%>/Askrida7.swf" />
		<param name="quality" value="high" />
		<param name="bgcolor" value="ffffff" />
		<embed src="<%=jspUtil.getImagePath()%>/Askrida7.swf" quality="high" bgcolor="#000000" width="1024" height="550" name="sub" align="middle" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
	  </object>

               </td>
            </tr>
         </table>
      </td>
   </tr>
</table>
<script>
   document.getElementById('userid').focus();

</script>

<%--<table width="780" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="left" valign="top" width="139" height="47"></td>
    <td align="left" valign="top" width="122" height="47"></td>
    <td align="left" valign="top" width="241" height="47"></td>
    <td align="left" valign="top" width="278" height="47"></td>
  </tr>
  <tr>
    <td align="left" valign="top" width="139" height="44"></td>
    <td align="left" valign="top" width="122" height="44"></td>
    <td align="left" valign="top" width="241" height="44"></td>
    <td align="left" valign="top" width="278" height="44"></td>
  </tr>
  <tr>
    <td align="left" valign="top" width="139" height="203"></td>
    <td align="left" valign="top" width="122" height="203"><img src="images/webfin.gif"></td>
    <td align="left" valign="top" width="241" height="203"></td>
    <td align="center" valign="middle" width="278" height="203" background="images/m3a.jpg">
      <table width="250" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="60" align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><b>User ID </b></font></td>
          <td width="10" align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><b>:</b></font></td>
          <td colspan="1">
            <input type="text" name="userid">
          </td>
        </tr>
        <tr>
          <td width="60" align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><b>Password</b></font></td>
          <td width="10" align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><b>:</b></font></td>
          <td width="135">
            <input type="password" name="password">
          </td>
          <td width="45" align="center"><input type=image alt="Submit" name="submit" src="images/go_button.jpg" width="0" height="0"></td>
        </tr>
        <tr>
          <td width="60">&nbsp;</td>
          <td width="10">&nbsp;</td>
          <td colspan="2">&nbsp;</td>
        </tr>
      </table>
      <img src="images/pancing.gif" width="1" height="1">
    </td>
  </tr>
  <tr>
    <td align="left" valign="top" rowspan="2" height="161"></td>
    <td align="left" valign="top" rowspan="2" height="161"></td>
    <td align="left" valign="top" width="241" height="102"></td>
    <td align="left" valign="top" width="278" height="102"></td>
  </tr>
  <tr>
    <td align="left" valign="top" width="241" height="59"></td>
    <td align="left" valign="top" width="278" height="59"></td>
  </tr>
  <tr>
    <td align="left" valign="top" width="139" height="45"></td>
    <td align="left" valign="top" width="122" height="45"></td>
    <td align="left" valign="top" width="241" height="45"></td>
    <td align="left" valign="top" width="278" height="45"></td>
  </tr>
</table>--%>
<font color=black>Click To Continue</font>
</form>
</body>


</html>
<% } %>
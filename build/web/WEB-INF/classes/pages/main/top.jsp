<%@ page import="com.crux.util.JSPUtil,
                 com.crux.common.parameter.Parameter"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %><head>
<title>Telkomsel</title>
<link rel="stylesheet" href="<%=jspUtil.getStyleSheetPath()%>" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%
    final boolean showLogo = Parameter.readBoolean("GEN_SHOW_LOGO");
%>
</head>

<body bgcolor="#ffffff" text="#000000" leftmargin="0" topmargin="0" MarginWidth="0" MarginHeight="0">

<table cellpadding=2 cellspacing=1 background="<%=true?"images/askhdr1b.jpg":""%>" width="100%">
   <tr>
      <td height=55>
        <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0" width="993" height="58" id="sub" align="left">
		<param name="allowScriptAccess" value="sameDomain" />
		<param name="movie" value="<%=jspUtil.getImagePath()%>/TopBanner.swf" />
		<param name="quality" value="high" />
		<param name="bgcolor" value="ffffff" />
		<embed src="<%=jspUtil.getImagePath()%>/TopBanner.swf" quality="high" bgcolor="#000000" width="993" height="58" name="sub" align="left" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
	    </object>
      </td>
   </tr>
   <tr>
      <td align=right height=20>
      <b><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#de1010"><a href="#">Home</a>
                  | <a href="#" onclick="window.parent.basefrm.location='<%=jspUtil.getControllerURL("CHANGE_PASSWORD")%>';">Change Password</a> | <a href="#" onclick="window.parent.location='<%=jspUtil.getControllerURL("LOGOUT")%>';">Log Out</a></font></b>
      </td>
   </tr>
</table>
<%--
   <table width="780" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="left" valign="top" width="780" height="137">
      <table width="780" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="left" valign="top" width="252" height="137">
          <img src="images/webfin.gif" ></td>
          <td width="250" height="137" align="left" valign="top" >
            <table width="247" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td align="left" valign="top" width="247" height="74"></td>
              </tr>
              <tr>
                <td align="right" valign="top" width="247" height="19"><b><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000"><a href="#">Home</a>
                  | <a href="#" onclick="window.parent.basefrm.location='<%=jspUtil.getControllerURL("CHANGE_PASSWORD")%>';">Change Password</a> | <a href="#" onclick="window.parent.location='<%=jspUtil.getControllerURL("LOGOUT")%>';">Log Out</a></font></b></td>
              </tr>
            </table>
            </td>
          <td align="left" valign="top" width="278" height="137"></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td align="left" valign="top" width="780" height="489">&nbsp;    </td>
  </tr>
</table>
--%>
</body>
</html>

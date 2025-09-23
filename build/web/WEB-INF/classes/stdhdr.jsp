<%@ page import="com.crux.util.JSPUtil"%>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<html>
<head>
</head>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type=text/css rel=STYLESHEET>
<body>
<table cellpadding=0 cellspacing=0>
<tr>
  <td width="500" height="25" align="left" valign="middle" background="<%=jspUtil.getImagePath()%>/bg_hor.jpg"><span class="title"><%=stPageTitle%></span></td>
</tr>
</table>

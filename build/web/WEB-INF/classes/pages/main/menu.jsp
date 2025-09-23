<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.web.controller.SessionManager,
                 com.crux.login.model.FunctionsView"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<% final JSPUtil jspUtil = new JSPUtil(request, response); %><html>
    <%
    %>
<head>
<title>Askrida</title>
<link rel="stylesheet" href="<%=jspUtil.getStyleSheetPath("menu.css")%>" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=iso-8859-1" />
<meta name="keywords" content="" />
<meta name="description" content="" />	
<meta http-equiv="imagetoolbar" content="no" />
<script language="JavaScript" src="<%=jspUtil.getScriptURL("MM_common.js")%>"></script>
<script language="JavaScript" type="text/JavaScript">
	
	
var imagepath = "<%=jspUtil.getImagePath()%>";
var webpath = "<%=jspUtil.getRootPath()%>";
var menu = new Array(<%
   final DTOList menulist = (DTOList)request.getAttribute("MENU");
   for (int i = 0; i < menulist.size(); i++) {
      FunctionsView f = (FunctionsView) menulist.get(i);
      if (i>0) out.print(',');
      out.print("new Array('"+f.getStFunctionID()+"','"+f.getStFunctionName()+"','"+f.getStURL()+"')");
   }

%>);
</script>
<script language="JavaScript" src="<%=jspUtil.getPagesPath()%>/main/menu.js"></script>
</head>

<body bgcolor="#f4f4f4" text="#000000" leftmargin="0" topmargin="0" MarginWidth="0" MarginHeight="0">
    
<div id=content>
</div>
</body>
</html>
<script>
   renderMenu();
</script>

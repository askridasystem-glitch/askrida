<%@ page import="com.crux.util.JSPUtil,
                 com.ots.invoice.model.InvoiceView"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<head>
</head>
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
<%
   String result = (String) request.getAttribute("RESULT");
   InvoiceView invoice = (InvoiceView) request.getAttribute("INVOICE");
   String sComment = "";
   sComment = "Invoice has been created successfully. <table><tr><td>Your INVOICE ID is " + result +"</td></tr><tr><td> Your INVOICE NUMBER is "+invoice.getStInvoiceNumber()+"</td></tr></table>";
%>
<body>
<table cellpadding=0 cellspacing=0>

<form name=f method=post action=grn.ctl>
<%=jspUtil.getHeader("INVOICE")%>
<tr>
	<td><br></td>
</tr>
<tr>
	<td>
		<%=sComment%>
		<br>
		<br>
	</td>
</tr>
<tr>
	<td colspan=2>
      <%=jspUtil.getButtonNormal("bback","  Back  ","window.location='"+jspUtil.getControllerURL("INVOICE_LIST")+"'")%>
	</td>
</tr>
</form>
</table>
</body>
</html>
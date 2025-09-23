<%@ page import="com.crux.util.JSPUtil"
%>

<%
	final JSPUtil jspUtil = new JSPUtil(request, response);
	String description = "";
	String strAction = (String) request.getAttribute("ACTION");
	if("DELETE".equals(strAction)) description = "deleted";
	if("SAVE".equals(strAction)) description = "saved";
	if("TRIGGER".equals(strAction)) description = "trigger";
%>
<html>
<head>
</head>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type=text/css rel=STYLESHEET>
<script language="javascript">
</script>
<body>
<table cellpadding=0 cellspacing=0>
<form name="form1">
   <body>
      <table>
      <form name=f method=post action=job.ctl>
      <tr>
        <td width="598" height="25" align="left" valign="middle" background="<%=jspUtil.getImagePath()%>/bg_hor.jpg"><span class="title">JOB CONFIGURATION</span>
      </tr>
      <tr>
         <td>
            Job has been successfully <%= description%>.
            <br>
            <br>
         </td>
      </tr>
      <tr>
         <td>
            <BR><BR>
            <input type=hidden name=EVENT value=CONFIG_JOB>
            <input class=button type=submit value="Back to Job List">
         </td>
      </tr>
      </table>
      </form>
   </body>
</form>
</html>
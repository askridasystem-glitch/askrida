<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.crux.util.JSPUtil,
					com.crux.util.DTOList,
                                        com.crux.util.DateUtil,
                                        java.util.Date,
					com.crux.common.controller.*,
                                        com.crux.common.parameter.Parameter,
					com.crux.login.model.UserSessionView,
                 	com.crux.login.model.FunctionsView"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response);

    final UserSessionView uv = (UserSessionView)Helper.getUserSession(request);
%>
<html xmlns="http://www.w3.org/1999/xhtml">

<%

   String isi =  Parameter.readString("BROADCAST_MESSAGE");
   isi = isi.replaceAll("#FONT_RED_OPEN","<font style=\"color:red\">");
   isi = isi.replaceAll("#FONT_RED_CLOSE","</font>");
   isi = isi.replaceAll("#MARK","<font style=\"color:red\"> | </font>");

   int sisaHari = DateUtil.getDaysAmount(new Date(),uv.getDtInActiveDate());
    
%>
<head>

</head>

<body>
    <table width="100%">
<tr>
<td height="21" width ="22.5%">
        <font style="font-family:Arial; font-weight:bold; font-size:11px; color:red">Password Expired : <strong><%=jspUtil.print(uv.getDtInActiveDate())%> (<%=sisaHari%> Hari)</strong>
        </font>
</td>
<td height="21" width ="77.5%">
<marquee scrolldelay=170 onmouseover="this.stop()" onmouseout="this.start()">
    <font style="font-family:Arial; font-weight:bold; font-size:11px; color:#08407e"><%=isi%></font>
</marquee>
<%--<DIV ID="TICKER" STYLE="font-weight:bold; overflow:hidden; width:1200px"  
onmouseover="TICKER_PAUSED=true" onmouseout="TICKER_PAUSED=false">
  <%=isi%>
</DIV>
<script type="text/javascript" src="<%=jspUtil.getScriptURL("webticker_lib.js")%>" 
language="javascript"></script>--%>
</td>
</tr>
</table>


</body>
</html>


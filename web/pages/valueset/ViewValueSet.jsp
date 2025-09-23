<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.valueset.model.ValueSetView,
                 com.crux.valueset.validation.ValueSetValidator
                 "%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type=text/css rel=STYLESHEET>
<head>
<%
   final DTOList vl = (DTOList) request.getAttribute("VS_LIST");

%>
</head><body>
<form name=f action="valueset.ctl" method=POST>
<input type=hidden name=EVENT value="VS_LIST_GO">
<%=jspUtil.getHiddenText("DelVal",null,null,100)%>
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>

<table cellpadding=1 cellspacing=1>
<%=jspUtil.getHeader("VIEW VALUE SET ")%>
<tr>
	<td width="30%"> <br></td>
</tr>
<tr>
	<td class="row0">
		Group Name
	</td>
	<td > <%final ValueSetView vs1 = (ValueSetView)vl.get(0);%>
    <%=jspUtil.print(vs1.getStVGroup())%>
 	</td>
</tr>
<tr>
    <td><br></td>
</tr>
<tr><td colspan=2>
<table cellpadding=1 cellspacing=1>
<tr>
   <td width=500 colspan=4></td>
</tr>
<tr>
   <td class="header">Code</td>

   <td class="header">Description</td>

   <td class="header">Order</td>


</tr>
<%
    for(int i=0;i<vl.size();i++){
    final ValueSetView vs = (ValueSetView)vl.get(i);
    %>
<tr>
          <td class="row<%=i%2%>">
               <%=jspUtil.print(vs.getStVCode())%>
          </td>
          <td class="row<%=i%2%>">
             <%=jspUtil.print(vs.getStVDesc())%>
          </td>
          <td class="row<%=i%2%>">
             <%=jspUtil.print(vs.getLgVOrder())%>
          </td>

</tr>
<%}%>

</table>
</td></tr>
</table>
<table cellspacing=1 cellpadding=1>
<tr>
   <td colspan=3 width=600></td>
</tr>
<tr>

	<td colspan=2>
		<br><br>
      <%=jspUtil.getButtonNormal("bBack","Back","window.location='"+jspUtil.getControllerURL("VS_LIST_GO")+"'")%>
	</td>

</tr>
</table>
</form>
</body>

</html>


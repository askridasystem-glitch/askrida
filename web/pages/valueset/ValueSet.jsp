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
   final String sTitle =  (String) request.getAttribute("ACTION");
   final boolean sDisable = (sTitle.equals("EDIT"));
   final DTOList vl = (DTOList) request.getAttribute("VS_LIST");

%>
</head><body>
<form name=f action="valueset.ctl" method=POST>
<input type=hidden name=EVENT value="VS_SAVE">
<%=jspUtil.getHiddenText("DelVal",null,null,100)%>
<input type=hidden name=ACTION value="<%=sTitle%>">
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>

<table cellpadding=1 cellspacing=1>
<%=jspUtil.getHeader(sTitle+" VALUE SET ")%>
<tr>
	<td width="30%"> <br></td>
</tr>
<tr>
	<td class="row0">
		Group Name
	</td>
	<td > <%
        if(vl!=null && vl.size()>0){
            final ValueSetView vs1 = (ValueSetView)vl.get(0);
            if (sDisable){

    %>
     <%=jspUtil.getDisplayText("groupname", ValueSetValidator.vfVsgroup, vs1.getStVGroup(), 100, JSPUtil.MANDATORY)%>
    <%} else {%>
    <%=jspUtil.getInputText("groupname", ValueSetValidator.vfVsgroup, vs1.getStVGroup(), 100, JSPUtil.MANDATORY)%>
    <%}}else{%>
    <%=jspUtil.getInputText("groupname", ValueSetValidator.vfVsgroup, null, 100, JSPUtil.MANDATORY)%>
    <%}%>
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

   <td class="header">&nbsp;</td>
</tr>
<%  if(vl.size()>0){
    for(int i=0;i<vl.size();i++){
    final ValueSetView vs = (ValueSetView)vl.get(i);
    %>
<tr>
          <td class="row<%=i%2%>">
              <% if(!vs.isNew()){%>
               <%=jspUtil.print(vs.getStVCode())%>
               <%}else{%>
                <%=jspUtil.getInputText("code"+(i+1),ValueSetValidator.vfVscode,vs.getStVCode(),100,JSPUtil.MANDATORY)%>
               <%}%>
          </td>
          <td class="row<%=i%2%>">
             <%=jspUtil.getInputText("description"+(i+1),ValueSetValidator.vfVsdesc,vs.getStVDesc(),200)%>
          </td>
          <td class="row<%=i%2%>">
             <%=jspUtil.getInputText("order"+(i+1),ValueSetValidator.vfVsorder,vs.getLgVOrder(),100)%>
          </td>
          <td class="row0">
            <%=jspUtil.getButtonNormal("btnDel","Del","delCode("+i+");")%>

          </td>
</tr>
<%}}%>
<tr>
          <td>
               <%=jspUtil.getInputText("code0",ValueSetValidator.vfVscode,null,100)%>
          </td>
          <td>
             <%=jspUtil.getInputText("description0",ValueSetValidator.vfVsdesc,null,200)%>
          </td>
          <td>
             <%=jspUtil.getInputText("order0",ValueSetValidator.vfVsorder,null,100)%>
          </td>
          <td>
          <%=jspUtil.getButtonNormal("btnAdd","Add","addCode();")%>
          </td>
</tr>
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
      <%=jspUtil.getButtonSubmit("bsave","Submit") %>
      <%=jspUtil.getButtonNormal("bcancel","Cancel","window.location='"+jspUtil.getControllerURL("VS_LIST_GO")+"'")%>
	</td>

</tr>
</table>
</form>
</body>
<script>

function addCode(){
   f.EVENT.value='VS_ADD_CODE';
   f.submit();
}
function delCode(n) {
    f.DelVal.value=n;
    f.EVENT.value='VS_DEL_CODE';
   f.submit();

}
</script>

</html>


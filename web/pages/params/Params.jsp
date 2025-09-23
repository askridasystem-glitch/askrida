<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.configure.model.ParameterView,
                 com.crux.util.validation.FieldValidator,
                 com.crux.common.codedecode.Codec,
                 com.crux.common.parameter.Parameter,
                 com.crux.util.LookUpUtil,
                 com.crux.common.parameter.lov.LOG_LEVEL,
                 com.crux.common.parameter.ParameterLOVPool"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
<%
   final DTOList l = (DTOList)request.getAttribute("PARAMS");
   final String grp = (String)request.getAttribute("GRP");
%>
   <body>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="CONFIGURE_SAVE">
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader2(grp+" - SETUP")%>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td>Key</td>
                        <td>Seq</td>
                        <td>Description</td>
                        <td>Value</td>
                     </tr>
<%
   for (int i = 0; i < l.size(); i++) {
      ParameterView p = (ParameterView) l.get(i);

      final LookUpUtil lu = ParameterLOVPool.getLOV(p.getStParamID());
      //final LookUpUtil lu = new LOG_LEVEL();
%>
                     <tr class=row<%=i%2%>>
                        <td><%=jspUtil.print(p.getStParamID())%></td>
                        <td><%=jspUtil.print(p.getLgParamSeq())%></td>
                        <td><%=jspUtil.print(p.getStParamDesc())%></td>
<% if (Codec.ParameterType.STRING.equalsIgnoreCase(p.getStParamType())) {%>
<% if (lu==null) {%>
                        <td><%=jspUtil.getInputText("vs"+i, new FieldValidator("v","Value","string",1500),p.getStValueString(),450)%></td>
<% } else {%>
                        <td><%=jspUtil.getInputSelect("vs"+i, new FieldValidator("v","Value","string",255),lu.getComboContent(p.getStValueString()),200)%></td>
<% } %>
<% } else if (Codec.ParameterType.DATE.equalsIgnoreCase(p.getStParamType())) {%>
                        <td><%=jspUtil.getInputText("vd"+i, new FieldValidator("v","Value","date",-1),p.getDtValueDate(),50)%></td>
<% } else if (Codec.ParameterType.INTEGER.equalsIgnoreCase(p.getStParamType())) {%>
<% if (lu==null) {%>
                        <td><%=jspUtil.getInputText("vi"+i, new FieldValidator("v","Value","integer",15),p.getLgValueNumber(),100)%></td>
<% } else {%>
                        <td><%=jspUtil.getInputSelect("vi"+i, new FieldValidator("v","Value","integer",15),lu.getComboContent(String.valueOf(p.getLgValueNumber())),100)%></td>
<% } %>
<% } else if (Codec.ParameterType.BOOLEAN.equalsIgnoreCase(p.getStParamType())) {

   final boolean b = (p.getLgValueNumber()!=null) && (p.getLgValueNumber().intValue()==1);
%>
                        <td><input type=checkbox name=vb<%=i%> <%=b?"checked":""%>></td>
<% } %>
                     </tr>
<% }%>
                  </table>
               </td>
            </tr>
            <tr>
               <td align=center>
                  <%=jspUtil.getButtonSubmit("bsave","SAVE")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
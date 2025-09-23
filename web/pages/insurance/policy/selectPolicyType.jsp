<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.LOV"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
   <body>
<%
   final LOV instypes = (LOV) request.getAttribute("INS_TYPE_LIST");
   final LOV inssubtypes = (LOV) request.getAttribute("INS_SUBTYPE_LIST");
   final boolean hasSubtype = inssubtypes!=null;
%>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="INS_POL_CREATE2">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("SELECT POLICY TYPE")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr><td>Policy Type</td><td>:</td><td><%=jspUtil.getInputSelect("poltype|Policy Type|string||onChange:INS_POL_CREATE_CHGTYPE",null,instypes,400,JSPUtil.MANDATORY)%></td></tr>
<% if (hasSubtype) {%>
                     <tr><td>Sub Type</td><td>:</td><td><%=jspUtil.getInputSelect("polsubtype|Policy Sub Type|string",null,inssubtypes,400,0)%></td></tr>
<% } %>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td><%=jspUtil.getButtonSubmit("b","Select")%></td>
                        <td><%=jspUtil.getButtonSubmit("b","Cancel","f.EVENT.value='INS_POL'")%></td>
                     </tr>
                  </table>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
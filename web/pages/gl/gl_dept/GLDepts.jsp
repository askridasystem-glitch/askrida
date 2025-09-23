<%@ page import="com.crux.util.JSPUtil,
                 com.webfin.gl.model.JournalMasterView,
                 com.crux.util.validation.FieldValidator,
                 com.webfin.gl.codes.GLCodes,
                 com.crux.util.LOV,
                 com.webfin.gl.model.GLCostCenterView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   final GLCostCenterView dept = (GLCostCenterView )request.getAttribute("DEPT");

   final char jflags = dept.isModified()?0:JSPUtil.READONLY;
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="GL_DEPT_SAVE">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("COST CENTER")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr><td>Cost Center Code</td><td>:</td><td><%=jspUtil.getInputText("deptcode",new FieldValidator("","Department Code","string",5),dept.getStCostCenterCode(),100,JSPUtil.MANDATORY|jflags)%></td></tr>
                     <tr><td>Description</td><td>:</td><td><%=jspUtil.getInputText("desc",new FieldValidator("","Description","string",128),dept.getStDescription(),200,JSPUtil.MANDATORY|jflags)%></td></tr>
                  </table>
               </td>
            </tr>

            <tr>
               <td>
                  <%=dept.isModified()?jspUtil.getButtonSubmit("bsave","Save"):""%>
                  <%=jspUtil.getButtonNormal("bcancel",dept.isModified()?"Cancel":"Back","submitEvent('GL_DEPT_LIST')")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
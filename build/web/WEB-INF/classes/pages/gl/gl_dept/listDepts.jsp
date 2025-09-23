<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.gl.model.GLCostCenterView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
<%
   final DTOList list = (DTOList) request.getAttribute("LIST");
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <input type=hidden name=deptid value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("COST CENTER")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>CODE</td>
                        <td>DESCRIPTION</td>
                     </tr>
<%
   for (int i = 0; i < list.size(); i++) {
      GLCostCenterView dept = (GLCostCenterView) list.get(i);

%>
                     <tr class=row<%=i%2%>>
                        <td><input type=radio name=x onclick="f.deptid.value='<%=jspUtil.print(dept.getStCostCenterCode())%>'"></td>
                        <td><%=jspUtil.print(dept.getStCostCenterCode())%></td>
                        <td><%=jspUtil.print(dept.getStDescription())%></td>
                     </tr>
<% } %>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <%=jspUtil.getButtonSubmit("bcreate","Create","f.EVENT.value='GL_DEPT_ADD'")%>
                  <%=jspUtil.getButtonSubmit("bedit","Edit","f.EVENT.value='GL_DEPT_EDIT'")%>
                  <%=jspUtil.getButtonSubmit("bview","View","f.EVENT.value='GL_DEPT_VIEW'")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
<%@ page import="com.crux.util.JSPUtil,
                 com.webfin.gl.model.GLReportView,
                 com.crux.util.DTOList,
                 com.webfin.gl.model.GLReportLineView,
                 com.webfin.gl.model.GLReportColumnView,
                 com.crux.util.BDUtil,
                 java.util.ArrayList"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
<%
   final GLReportView glrpt = (GLReportView) request.getAttribute("RPT");

   final DTOList columns = glrpt.getColumns();

%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("RPT")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
<%
   for (int i = 0; i < columns.size(); i++) {
      GLReportColumnView col = (GLReportColumnView) columns.get(i);

%>
                        <td><%=jspUtil.print(col.getStColumnHeader())%></td>
<% } %>
                     </tr>
<%
   final ArrayList lines = glrpt.getResult();

   for (int i = 0; i < lines.size(); i++) {
      Object[] line = (Object []) lines.get(i);
%>
                     <tr class=row<%=i%2%>>
<%
   for (int j = 0; j < columns.size(); j++) {
      GLReportColumnView col = (GLReportColumnView) columns.get(j);

      Object val = line[j+1];

      if (val==null) val=BDUtil.zero;

%>
                        <td align=right><%=jspUtil.print(val)%></td>
<% } %>

                     </tr>
<% } %>
                  </table>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
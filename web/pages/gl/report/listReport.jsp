<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.gl.model.GLReportView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
   <body>
<%
   final DTOList list = (DTOList) request.getAttribute("LIST");
%>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="GL_RPT_GENERATE">
         <input type=hidden name=glreportid value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("GL REPORTS")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>REPORT ID</td>
                        <td>Title</td>
                     </tr>
<%
   for (int i = 0; i < list.size(); i++) {
      GLReportView glr = (GLReportView) list.get(i);
%>
                     <tr class=row<%=i%2%>>
                        <td><input type=radio name=x onclick="f.glreportid.value='<%=jspUtil.print(glr.getStReportID())%>'"></td>
                        <td><%=jspUtil.print(glr.getStReportID())%></td>
                        <td><%=jspUtil.print(glr.getStReportTitle())%></td>
                     </tr>
<% }%>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <%--<%=jspUtil.getButtonSubmit("bgenerate","Generate")%>--%>
                  <%=jspUtil.getButtonEvent("Generate 2","GL_RPT_GENERATE2")%>
                  <%=jspUtil.getButtonNormal("b","Generate 3","w1('gl_rpt_1.pdf.rpt?glreportid='+f.glreportid.value+'&antic='+(new Date().getTime()));")%>
                  <%=jspUtil.getButtonEvent("New Report","glrptdsgnform.newReport.crux")%>
                  <%=jspUtil.getButtonEvent("Edit Report","glrptdsgnform.editReport.crux")%>
                  <%=jspUtil.getButtonEvent("View Report","glrptdsgnform.viewReport.crux")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
<script>
   function w1(x) {
      //w = window.open();
      document.location=x;
   }
</script>
<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.gl.model.PeriodHeaderView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   final DTOList list = (DTOList) request.getAttribute("LIST");
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="GL_PERIODS">
         <input type=hidden name=periodid value="">
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("PERIODS")%>
            <tr>
               <td>
                  <%=jspUtil.getPager(request,list)%>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>FISCAL YEAR</td>
                        <td>PERIOD NUM</td>
                     </tr>
<%
   for (int i = 0; i < list.size(); i++) {
      PeriodHeaderView pd = (PeriodHeaderView) list.get(i);
%>
                     <tr class=row<%=i%2%>>
                        <td><input type=radio name=sel onclick="f.periodid.value='<%=jspUtil.print(pd.getLgPeriodID())%>'"></td>
                        <td><%=jspUtil.print(pd.getStFiscalYear())%></td>
                        <td><%=jspUtil.print(pd.getLgPeriodNum())%></td>
                     </tr>
<% }%>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <%=jspUtil.getButtonNormal("crt","Create","openDialog('so.ctl?EVENT=GL_PERIODS_ADD', 500,400,refreshcb);")%>
                  <%=jspUtil.getButtonNormal("edt","Edit","openDialog('so.ctl?EVENT=GL_PERIODS_EDIT&periodid='+f.periodid.value, 500,400,refreshcb);")%>
                  <%=jspUtil.getButtonNormal("v","View","openDialog('so.ctl?EVENT=GL_PERIODS_VIEW&periodid='+f.periodid.value, 500,400,refreshcb);")%>
                  <%=jspUtil.getButtonNormal("v","Open/Close","openDialog('so.ctl?EVENT=GL_PERIODS_OPEN_CLOSE&periodid='+f.periodid.value, 500,400,refreshcb);")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
<script>
   function refreshcb(o) {
      if (o!=null) f.submit();
   }
</script>
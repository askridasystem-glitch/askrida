<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.jobs.model.JobStatusView,
                 com.crux.util.DateUtil"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
<%

   final DTOList l = (DTOList)request.getAttribute("LIST");
%>
   <body>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <table cellpadding=2 cellspacing=1 width="100%">
            <%=jspUtil.getHeader("JOB LOG")%>
            <tr>
               <td>
                  <%=jspUtil.getPager(request, l)%>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td>LOG ID</td>
                        <td>JOB ID</td>
                        <td>STATUS</td>
                        <td>DESCRIPTION</td>
                        <td>DATE/TIME</td>
                     </tr>
<%
   for (int i = 0; i < l.size(); i++) {
      JobStatusView jl = (JobStatusView) l.get(i);

%>
                     <tr class=row<%=i%2%>>
                        <td><%=jspUtil.print(jl.getStJobLogID())%></td>
                        <td><%=jspUtil.print(jl.getStJobID())%></td>
                        <td><%=jspUtil.print(jl.getStJobStatus())%></td>
                        <td><%=jspUtil.print(jl.getStStatusMessage())%></td>
                        <td><%=DateUtil.getDateTimeStr(jl.getDtJobDate())%></td>
                     </tr>
<% } %>
                  </table>
                  <%=jspUtil.getPager(request, l)%>
               </td>
            </tr>
            <tr>
               <td align=center>
                  <%=jspUtil.getButtonNormal("brefresh","REFRESH","window.location='"+jspUtil.getControllerURL("JOB_LOG_VIEW")+"'")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
<script>
   function changePageList() {
      f.EVENT.value='JOB_LOG_VIEW';
      f.submit();
   }
</script>
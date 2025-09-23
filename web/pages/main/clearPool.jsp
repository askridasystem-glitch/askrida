<%@ page import="com.crux.util.JSPUtil,
                 com.crux.pool.DTOPool"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
   <body>
   <%
      DTOPool.getInstance().clear();
   %>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("POOL SWEEPER")%>
               </td>
            </tr>
            <tr>
               <td>
                  Pool Cleared.
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
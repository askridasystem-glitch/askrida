<%@ page import="com.crux.util.JSPUtil"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("TITLE")%>
            <tr>
               <td>
                  invoke success
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
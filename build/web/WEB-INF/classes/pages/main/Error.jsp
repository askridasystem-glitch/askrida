<%@ page import="com.crux.util.JSPUtil,
                 java.io.StringWriter,
                 java.io.PrintWriter"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
<%
   final Throwable e = (Throwable) request.getAttribute("ERROR_MESSAGE");

%>   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("FATAL ERROR")%>
            <tr>
               <td>
                  <br><font color=red><b>Fatal Error has been occured : <%=e.getMessage()%></b></font><br>
                  <br>
                  Detailed Info:<br>
                  <br>
<pre>
<%e.printStackTrace(new PrintWriter(out));%>
</pre>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
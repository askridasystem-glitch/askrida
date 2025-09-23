<%@ page import="com.crux.util.JSPUtil"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("FILE LIST")%>
            <tr>
               <td>

               </td>
            </tr>

            <tr>
               <td>
                  <%=jspUtil.getButtonSubmit("bUpload","Upload File","document.getElementById('EVENT').value='FILEMAN_UPLOAD'")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
<%@ page import="com.crux.util.JSPUtil,
                 com.webfin.gl.model.AccountView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
   <body>

   </body>
</html>
<script>
<%
   final AccountView ac = (AccountView)request.getAttribute("AC");
%>
     dialogReturn(
         {
          acid:'<%=jspUtil.print(ac.getLgAccountID())%>',
          acno:'<%=jspUtil.print(ac.getStAccountNo())%>',
          desc:'<%=jspUtil.print(ac.getStDescription())%>',
          acdesc:'<%=jspUtil.print(ac.getStDescriptionLong())%>'
         }
      );
      window.close();
</script>
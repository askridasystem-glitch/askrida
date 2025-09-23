<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.util.LOV,
                 java.util.Iterator,
                 com.crux.util.LookUpUtil,
                 com.crux.common.model.DTO,
                 java.util.Enumeration"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%

%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden id=EVENT name=EVENT value="LOVPOP">
         <table>
            <tr><td>{L-ENGPassword-L}{L-INAPassword-L} </td><td>:</td><td><input type="password" name="policy.stPassword"></td></tr>
         </table>
      </form>
   </body>
</html>

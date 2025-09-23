<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.gl.model.AccountTypeView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
<%

    final DTOList list = (DTOList) request.getAttribute("LIST");
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("ACCOUNT TYPES")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td>ACCT CODE</td>
                        <td>DESCRIPTION</td>
                        <td>BAL TYPE</td>
                     </tr>
<% for (int i = 0; i < list.size(); i++) {
   AccountTypeView act = (AccountTypeView) list.get(i);
 %>
                     <tr class=row<%=i%2%>>
                        <td><%=jspUtil.print(act.getStAccountType())%></td>
                        <td><%=jspUtil.print(act.getStDescription())%></td>
                        <td><%=jspUtil.print(act.getStBalanceType())%></td>
                     </tr>
<% } %>                     
                  </table>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
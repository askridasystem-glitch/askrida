<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.ar.model.ARReceiptView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
<%
   final DTOList list = (DTOList) request.getAttribute("LIST");
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <input type=hidden name=rcid value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("RECEIPTS")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>ID</td>
                        <td>RECEIPT NO</td>
                        <td>AMOUNT</td>
                        <td>CURRENCY</td>
                     </tr>
<%
   for (int i = 0; i < list.size(); i++) {
      ARReceiptView rc = (ARReceiptView) list.get(i);
%>
                     <tr class=row<%=i%2%>>
                        <td><input type=radio name=x onclick="f.rcid.value='<%=jspUtil.print(rc.getStARReceiptID())%>'"></td>
                        <td><%=jspUtil.print(rc.getStARReceiptID())%></td>
                        <td><%=jspUtil.print(rc.getStReceiptNo())%></td>
                        <td><%=jspUtil.print(rc.getDbAmount())%></td>
                        <td><%=jspUtil.print(rc.getStCurrencyCode())%></td>
                     </tr>
<% } %>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <%=jspUtil.getButtonSubmit("b","CREATE","f.EVENT.value='AR_RECEIPT_CREATE'")%>
                  <%=jspUtil.getButtonSubmit("b","EDIT","f.EVENT.value='AR_RECEIPT_EDIT'")%>
                  <%=jspUtil.getButtonSubmit("b","VIEW","f.EVENT.value='AR_RECEIPT_VIEW'")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
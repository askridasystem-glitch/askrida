<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.ar.model.ARInvoiceView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   final DTOList list = (DTOList) request.getAttribute("LIST");
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <input type=hidden name=invoiceid value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("INVOICES")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>ID</td>
                        <td>CUSTOMER</td>
                        <td>TYPE</td>
                        <td>INVOICE NUMBER</td>
                        <td>DATE</td>
                        <td>DUE DATE</td>
                        <td>CCY</td>
                        <td>AMOUNT</td>
                        <td>SETTLED</td>
                     </tr>
<%
   for (int i = 0; i < list.size(); i++) {
      ARInvoiceView inv = (ARInvoiceView) list.get(i);
%>
                     <tr class=row<%=i%2%>>
                        <td><input type=radio name=n onclick="f.invoiceid.value='<%=jspUtil.print(inv.getStARInvoiceID())%>'"></td>
                        <td><%=jspUtil.print(inv.getStARInvoiceID())%></td>
                        <td><%=jspUtil.print(inv.getStEntityName())%></td>
                        <td><%=jspUtil.print(inv.getStInvoiceType())%></td>
                        <td><%=jspUtil.print(inv.getStInvoiceNo())%></td>
                        <td><%=jspUtil.print(inv.getDtInvoiceDate())%></td>
                        <td><%=jspUtil.print(inv.getDtDueDate())%></td>
                        <td><%=jspUtil.print(inv.getStCurrencyCode())%></td>
                        <td><%=jspUtil.print(inv.getDbAmount())%></td>
                        <td><%=jspUtil.print(inv.getDbAmountSettled())%></td>
                     </tr>
<% }%>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td>
                           <%=jspUtil.getButtonEvent("Create","AR_INVOICE_CREATE")%>
                           <%=jspUtil.getButtonEvent("Edit","AR_INVOICE_EDIT")%>
                           <%=jspUtil.getButtonEvent("View","AR_INVOICE_VIEW")%>
                        </td>
                     </tr>
                  </table>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
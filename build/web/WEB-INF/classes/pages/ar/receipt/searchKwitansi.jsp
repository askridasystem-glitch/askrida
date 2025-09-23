<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.validation.FieldValidator,
                 com.crux.util.DTOList,
                 com.webfin.ar.model.ARInvoiceView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   final String key = request.getParameter("key");
   
   final String arsid = request.getParameter("arsid");

   final DTOList list = (DTOList) request.getAttribute("LIST");
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="AR_RECEIPT_SEARCH_KWITANSI">
         <input type=hidden name=ccy value="">
         <input type=hidden name=ivtype value="">
         <input type=hidden name=arsid value="<%=arsid%>">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("SEARCH INVOICE")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td>Invoice NO/DESC</td>
                        <td>:</td>
                        <td><%=jspUtil.getInputText("key",new FieldValidator("Search Key","string",50),key,200,JSPUtil.MANDATORY)%></td>
                     </tr>
                  </table>
               </td>
            </tr>

            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>ID</td>
                        <td>INVOICE NO</td>
                        <td>Date</td>
                        <td>CCY</td>
                        <td align=right>Amount</td>
                        <td align=right>Outstanding</td>
                     </tr>
<%
   for (int i = 0; i < list.size(); i++) {
      ARInvoiceView iv = (ARInvoiceView) list.get(i);

%>
                     <tr class=row<%=i%2%>>
                        <td>
                           <%=jspUtil.getButtonNormal("b","*","selectInvoice('"+iv.getStARInvoiceID()+"')")%>
                        </td>
                        <td><%=jspUtil.print(iv.getStARInvoiceID())%></td>
                        <td><%=jspUtil.print(iv.getStInvoiceNo())%></td>
                        <td><%=jspUtil.print(iv.getDtInvoiceDate())%></td>
                        <td><%=jspUtil.print(iv.getStCurrencyCode())%></td>
                        <td align=right><%=jspUtil.printX(iv.getDbAmount(),2)%></td>
                        <td align=right><%=jspUtil.printX(iv.getDbOutstandingAmount(),2)%></td>
                     </tr>
<% } %>
                  </table>
               </td>
            </tr>

         </table>
      </form>
   </body>
</html>
<script>
   function selectInvoice(o) {
      dialogReturn({INVOICE_ID:o});
      window.close();
   }
</script>
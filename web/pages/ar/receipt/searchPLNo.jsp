<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.validation.FieldValidator,
                 com.crux.util.DTOList,
                 com.webfin.ar.model.ARInvoiceView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   final String norek = request.getParameter("norek");
   
   final String arsid = request.getParameter("arsid");

   final DTOList list = (DTOList) request.getAttribute("LIST");
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="AR_RECEIPT_SEARCH_PLNO">
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
                        <td><%=jspUtil.getInputText("norek",new FieldValidator("Search No","string",50),norek,200,JSPUtil.MANDATORY)%></td>
                     </tr>
                  </table>
               </td>
            </tr>

            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>No. MONITORING</td>
                        <td>KETERANGAN</td>
                        <td>TOTAL</td>
                     </tr>
<%
   for (int i = 0; i < list.size(); i++) {
      ARInvoiceView iv = (ARInvoiceView) list.get(i);

%>
                     <tr class=row<%=i%2%>>
                        <td>
                           <%=jspUtil.getButtonNormal("b","*","selectInvoice('"+iv.getStClaimNo()+"')")%>
                        </td>
                        <td id=CLAIMNO><%=jspUtil.print(iv.getStClaimNo())%></td>
                        <td id=CLAIMNAME><%=jspUtil.print(iv.getStClaimName())%></td>
                        <td id=POLID><%=jspUtil.print(iv.getStAttrPolicyID())%></td>
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
      dialogReturn(
         {
          CLAIMNO:o,
          CLAIMNAME:document.getElementById('CLAIMNAME').innerText
         }
      );
      window.close();
   }
</script>
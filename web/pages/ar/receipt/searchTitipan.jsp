<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.validation.FieldValidator,
                 com.crux.util.DTOList,
                 com.webfin.ar.model.ARInvoiceView,
                 com.webfin.gl.model.TitipanPremiView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   final String key = request.getParameter("key");
   
   final String arsid = request.getParameter("arsid");

   final DTOList list = (DTOList) request.getAttribute("LIST");
   
   //final String cc_code = request.getAttribute("CC_CODE");
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="AR_RECEIPT_SEARCH_TITIPAN">
         <input type=hidden name=ccy value="">
         <input type=hidden name=ivtype value="">
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
                        <td>NO BUKTI TITIPAN</td>
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
                        <td>ID Titipan</td>
                        <td>No Transaksi</td>
                        <td>Keterangan</td>
                        <td>No Polis</td>
                        <td>Jumlah</td>
                     </tr>
<%
   for (int i = 0; i < list.size(); i++) {
      TitipanPremiView iv = (TitipanPremiView) list.get(i);

%>
                     <tr class=row<%=i%2%>>
                        <td>
                           <%=jspUtil.getButtonNormal("b","*","selectInvoice('"+iv.getStTransactionID()+"')")%>
                        </td>
                        <td><%=jspUtil.print(iv.getStTransactionID())%></td>
                        <td><%=jspUtil.print(iv.getStTransactionNo())%></td>
                        <td><%=jspUtil.print(iv.getStDescription())%></td>
                        <td><%=jspUtil.print(iv.getStPolicyNo())%></td>
                        <td><%=jspUtil.print(iv.getDbBalance(),2)%></td>
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
      dialogReturn({TITIPAN_ID:o});
      window.close();
   }
</script>
<%@ page import="com.webfin.ar.model.ARInvoiceView,
                 com.webfin.ar.model.ARInvoiceDetailView,
                 com.webfin.gl.ejb.CurrencyManager,
                 com.webfin.FinCodec,
                 com.crux.util.*"%>
<% final JSPUtil jspUtil = new JSPUtil(request, response,"INVOICE");

   final ARInvoiceView invoice = (ARInvoiceView)request.getAttribute("INVOICE");

   final int defRO = invoice.isModified()?0:JSPUtil.READONLY;

   final LOV lovCCY = (LOV) request.getAttribute("CCY");

   final LookUpUtil luInvoiceType = FinCodec.InvoiceType.getLookUp();

   final boolean isMasterCcy = CurrencyManager.getInstance().isMasterCurrency(invoice.getStCurrencyCode());

   int rateflags=isMasterCcy?JSPUtil.READONLY:0;

   final boolean ro = invoice.isUnModified();
%>
         <input type=hidden name=delindex value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr><td>INVOICE NO</td><td>:</td><td><%=jspUtil.getInputText("invoiceno|Invoice Number|string|64",null,invoice.getStInvoiceNo(),200,JSPUtil.MANDATORY|defRO)%></td></tr>
                     <tr><td>INVOICE TYPE</td><td>:</td><td><%=jspUtil.getInputSelect("invoicetype|Invoice Type|string",null,luInvoiceType.setLOValue(invoice.getStInvoiceType()),100,JSPUtil.MANDATORY|defRO)%></td></tr>
                     <tr><td>INVOICE DATE</td><td>:</td><td><%=jspUtil.getInputText("invoicedate|Invoice Date|date",null,invoice.getDtInvoiceDate(),200,JSPUtil.MANDATORY|defRO)%></td></tr>
                     <tr><td>DUE DATE</td><td>:</td><td><%=jspUtil.getInputText("duedate|Due Date|date",null,invoice.getDtDueDate(),200,JSPUtil.MANDATORY|defRO)%></td></tr>

                     <tr><td>CURRENCY</td><td>:</td><td><%=jspUtil.getInputSelect("ccy|Currency|string||onChange:AR_INVOICE_CHG_CCY",null,lovCCY.setLOValue(invoice.getStCurrencyCode()),80,JSPUtil.MANDATORY|defRO)%></td></tr>
                     <tr><td>RATE</td><td>:</td><td><%=jspUtil.getInputText("rate|Rate|money16.2",null,invoice.getDbCurrencyRate(), 80, JSPUtil.MANDATORY|defRO|rateflags|JSPUtil.NOTEXTMODE)%></td></tr>

                     <tr><td>AMOUNT</td><td>:</td><td><%=jspUtil.getInputText("amount|Amount|money16.2",null,invoice.getDbAmount(),100,JSPUtil.READONLY|defRO)%></td></tr>
                     <tr><td>SETTLED</td><td>:</td><td><%=jspUtil.getInputText("settled|Settled|money16.2",null,invoice.getDbAmountSettled(),100,JSPUtil.READONLY|defRO)%></td></tr>
                     <tr><td>ACCOUNT</td><td>:</td><td><input type=hidden name=acid value="<%=jspUtil.print(invoice.getStGLARAccountID())%>">
                                 <%=jspUtil.getInputText("accdesc|Account|string",null,invoice.getStGLARAccountDesc(),300,JSPUtil.READONLY|JSPUtil.NOTEXTMODE|defRO)%>
                                 <%=jspUtil.getButtonNormal("b","...","openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT', 400,400,selectARAccount);")%>
                     </td></tr>
                     <tr><td>CUSTOMER</td><td>:</td><td><%=jspUtil.getInputText("custname|Customer Name|string",null,invoice.getStEntityID(),200,JSPUtil.MANDATORY|defRO)%></td></tr>
                     <tr><td>DESCRIPTION</td><td>:</td><td><%=jspUtil.getInputText("description|Description|string|255",null,invoice.getStDescription(),400,JSPUtil.MANDATORY|defRO)%></td></tr>

                     <tr><td>POSTED</td><td>:</td><td><%=jspUtil.getInputCheck("posted",null,Tools.isYes(invoice.getStPostedFlag()), null, 0)%></td></tr>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td><%=ro?"":jspUtil.getButtonNormal("b","+","addnewitem()")%></td>
                        <td>ACCOUNT</td>
                        <td></td>
                        <td>DESCRIPTION</td>
                        <td>AMOUNT</td>
                     </tr>
<%
   final DTOList details = invoice.getDetails();

   for (int i = 0; i < details.size(); i++) {
      ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);
%>
                     <tr class=row<%=i%2%>>
                        <td><%=ro?"":jspUtil. getButtonNormal("brecalc","-","f.delindex.value="+i+";f.EVENT.value='AR_INVOICE_DEL_ITEM';f.submit();")%></td>
                        <td><%=jspUtil.getInputText("iac"+i+"|Account|string",null,det.getStAccountDesc(),200,JSPUtil.READONLY|JSPUtil.NOTEXTMODE)%></td>
                        <td><%=jspUtil.getButtonNormal("b","...","idx="+i+";openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT', 400,400,selectARAccountDet);")%>
                            <input type=hidden name=iacid<%=i%> value="<%=jspUtil.print(det.getStGLAccountID())%>"></td>
                        <td><%=jspUtil.getInputText("idesc"+i+"|Description|string|255",null,det.getStDescription(),200,defRO|JSPUtil.MANDATORY)%></td>
                        <td><%=jspUtil.getInputText("iamt"+i+"|Amount|money16.2",null,det.getDbAmount(),100,defRO|JSPUtil.MANDATORY)%></td>
                     </tr>
<% } %>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <%=jspUtil.getButtonEvent("Save","AR_INVOICE_SAVE")%>
                  <%=jspUtil.getButtonEvent("Cancel","AR_INVOICE_LIST")%>
               </td>
            </tr>
         </table>
<script>

   function addnewitem() {
      f.EVENT.value='AR_INVOICE_ADD_ITEM';
      f.submit();
   }

   var idx;
   function selectARAccount(o) {
      if (o!=null) {
         document.getElementById('acid').value=o.acid;
         document.getElementById('accdesc').value=o.acdesc;
      }
   }

   function selectARAccountDet(o) {
      if (o!=null) {
         document.getElementById('iacid'+idx).value=o.acid;
         document.getElementById('iac'+idx).value=o.acdesc;
      }
   }

</script>
<%=jspUtil.release()%>
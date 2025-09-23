<%@ page import="com.crux.util.JSPUtil,
                 com.webfin.ar.validator.ARReceiptValidator,
                 com.webfin.ar.model.ARReceiptView,
                 com.crux.util.LOV,
                 com.crux.util.DTOList,
                 com.webfin.ar.model.ARReceiptLinesView,
                 com.crux.util.Tools,
                 com.webfin.gl.ejb.CostCenterManager,
                 com.webfin.gl.ejb.CurrencyManager"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>

   </head>
<%
   final ARReceiptView rcpt = (ARReceiptView) request.getAttribute("RCP");

   int cf = !rcpt.isModified()?JSPUtil.READONLY:0;

   final LOV lovRC = (LOV) request.getAttribute("RC");
   final LOV lovPMT = (LOV) request.getAttribute("PMT");
   final LOV lovCCY = (LOV) request.getAttribute("CCY");

   final DTOList details = rcpt.getDetails();
   final DTOList notes = rcpt.getNotes();

   final boolean ro = rcpt.isUnModified();

   final boolean methodSelected = rcpt.getStReceiptClassID() != null;

   final boolean isBank = rcpt.isBank();
   final boolean isNote = rcpt.isNote();

   final boolean isAP = rcpt.isAP();
   final boolean isAR = rcpt.isAR();

   int amountflags = JSPUtil.MANDATORY|cf;

   if (isNote) amountflags |= JSPUtil.READONLY;

   final boolean isMasterCcy = CurrencyManager.getInstance().isMasterCurrency(rcpt.getStCurrencyCode());

   int rateflags=isMasterCcy?JSPUtil.READONLY:0;

   /*
   {"stARSettlementID","ar_settlement_id"},
      {"stEntityID","entity_id"},
   */

%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <input type=hidden name=addinvoiceid value="">
         <input type=hidden name=addnoteid value="">
         <input type=hidden name=delindex value="">
         <input type=hidden name=ivtype value="<%=jspUtil.print(rcpt.getStInvoiceType())%>">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("SETTLEMENT")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr><td>PAYMENT NO</td><td>:</td><td><%=jspUtil.getInputText("receiptno",ARReceiptValidator.receiptNo,rcpt.getStReceiptNo(),200,JSPUtil.MANDATORY|cf)%></td></tr>
                     <tr><td>COST CENTER</td><td>:</td><td><%=jspUtil.getInputSelect("costcenter|Cost Center|string",null,CostCenterManager.getInstance().getCostCenterLOV().setLOValue(rcpt.getStCostCenterCode()),200,JSPUtil.MANDATORY)%></td></tr>
                     <tr><td>DATE</td><td>:</td><td><%=jspUtil.getInputText("rcptdate|Receipt Date|date",null,rcpt.getDtReceiptDate(),80,JSPUtil.MANDATORY|cf)%></td></tr>
                     <tr><td>CURRENCY</td><td>:</td><td><%=jspUtil.getInputSelect("ccy|Currency|string||onChange:AR_RCP_CHG_CCY",null,lovCCY.setLOValue(rcpt.getStCurrencyCode()),80,JSPUtil.MANDATORY|cf)%></td></tr>
                     <tr><td>RATE</td><td>:</td><td><%=jspUtil.getInputText("rate|Rate|money16.2",null,rcpt.getDbCurrencyRate(), 80, JSPUtil.MANDATORY|cf|rateflags|JSPUtil.NOTEXTMODE)%></td></tr>
                     <tr><td>AMOUNT</td><td>:</td><td><%=jspUtil.getInputText("amount",ARReceiptValidator.amount,rcpt.getDbAmount(),100,amountflags)%></td></tr>
                     <tr><td>METHOD</td><td>:</td><td><%=jspUtil.getInputSelect("rc",ARReceiptValidator.rc,lovRC.setLOValue(rcpt.getStReceiptClassID()),150,JSPUtil.MANDATORY|cf)%></td></tr>
                     <% if (methodSelected) {%>
                     <% if (isBank) {%>
                     <tr><td>ACCOUNT</td><td>:</td><td><%=jspUtil.getInputSelect("method",ARReceiptValidator.method,lovPMT.setLOValue(rcpt.getStPaymentMethodID()),400,JSPUtil.MANDATORY|cf)%></td></tr>
                     <% } %>
                     <tr><td>INVOICE TYPE</td><td>:</td><td><%=jspUtil.print(rcpt.getStInvoiceTypeDesc())%></td></tr>
                     <tr><td>APPLIED</td><td>:</td><td><%=jspUtil.getInputText("applied||money16.2",null,rcpt.getDbAmountApplied(),100,JSPUtil.READONLY|JSPUtil.NOTEXTMODE)%></td></tr>
                     <tr><td>REMAINING</td><td>:</td><td><%=jspUtil.getInputText("applied||money16.2",null,rcpt.getDbAmountRemain(),100,JSPUtil.READONLY|JSPUtil.NOTEXTMODE)%></td></tr>
                     <% } %>
                     <tr><td>POSTED</td><td>:</td><td><%=jspUtil.getInputCheck("posted",null,Tools.isYes(rcpt.getStPostedFlag()), null, 0)%></td></tr>
                  </table>
               </td>
            </tr>

<script>
   f.rc.VE_onchange = function () {
      f.EVENT.value='AR_RECEIPT_RECALC';
      f.submit();
   }
</script>
<% if (methodSelected && isNote) { %>
            <tr>
               <td>
                  <%=isAP?"Receivable":"Payable"%>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td><%=ro?"":jspUtil.getButtonNormal("b","+","addnewnote()")%></td>
                        <td>INVOICE NO</td>
                        <td>TOTAL</td>
                        <td>AMOUNT</td>
                     </tr>
<%
   for (int i = 0; i < notes.size(); i++) {
      ARReceiptLinesView rcl = (ARReceiptLinesView) notes.get(i);
%>
                     <tr class=row<%=i%2%>>
                        <td><%=ro?"":jspUtil. getButtonNormal("brecalc","-","f.delindex.value="+i+";f.EVENT.value='AR_RCP_DEL_NOTE';f.submit();")%></td>
                        <td><%=jspUtil.print(rcl.getStInvoiceNo())%></td>
                        <td><%=jspUtil.print(rcl.getDbInvoiceAmount())%></td>
                        <td><%=jspUtil.getInputText("n_amount"+i,ARReceiptValidator.lineAmount,rcl.getDbAmount(),100,JSPUtil.MANDATORY|cf)%></td>
                     </tr>
<% } %>
                  </table>
               </td>
            </tr>
<% } %>
<% if (methodSelected) { %>
            <tr>
               <td>
                  <%=isAR?"Receivable":"Payable"%>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td><%=ro?"":jspUtil.getButtonNormal("b","+","addnewinvoice()")%></td>
                        <td>INVOICE NO</td>
                        <td>TOTAL</td>
                        <td>AMOUNT</td>
                     </tr>
<%
   for (int i = 0; i < details.size(); i++) {
      ARReceiptLinesView rcl = (ARReceiptLinesView) details.get(i);
%>
                     <tr class=row<%=i%2%>>
                        <td><%=ro?"":jspUtil. getButtonNormal("brecalc","-","f.delindex.value="+i+";f.EVENT.value='AR_RCP_DEL_INVOICE';f.submit();")%></td>
                        <td><%=jspUtil.print(rcl.getStInvoiceNo())%></td>
                        <td><%=jspUtil.print(rcl.getDbInvoiceAmount())%></td>
                        <td><%=jspUtil.getInputText("i_amount"+i,ARReceiptValidator.lineAmount,rcl.getDbAmount(),100,JSPUtil.MANDATORY|cf)%></td>
                     </tr>
<% } %>
                  </table>
               </td>
            </tr>
<% } %>
            <tr>
               <td>
                  <%=ro?"":jspUtil.getButtonSubmit("b","Recalculate","f.EVENT.value='AR_RECEIPT_RECALC'")%>
                  <%=ro?"":jspUtil.getButtonSubmit("b","Save","f.EVENT.value='AR_RECEIPT_SAVE'")%>
                  <%=jspUtil.getButtonSubmit("b",ro?"Back":"Cancel","f.EVENT.value='AR_RECEIPT_LIST'")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
<script>
   function addnewinvoice() {
      openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_INVOICE&ccy='+f.ccy.value+'&type='+f.ivtype.value, 500,400,
         function (o) {
            if (o!=null) {
               f.addinvoiceid.value=o.INVOICE_ID;
               f.EVENT.value='AR_RECEIPT_ADD_INVOICE';
               f.submit();
            }
         }
      );
   }

   function addnewnote() {
      openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_INVOICE&ccy='+f.ccy.value+'&type='+f.ivtype.value, 500,400,
         function (o) {
            if (o!=null) {
               f.addnoteid.value=o.INVOICE_ID;
               f.EVENT.value='AR_RECEIPT_ADD_NOTE';
               f.submit();
            }
         }
      );
   }
</script>
<%@ page import="com.webfin.ar.model.ARReceiptView,
                 com.webfin.ar.forms.ReceiptForm,
                 com.webfin.ar.model.ARReceiptLinesView,
                 com.crux.util.DTOList,
                 com.crux.util.Tools,
                 com.webfin.ar.model.ARAPSettlementView,
                 com.crux.util.JSPUtil,
                 com.crux.lov.LOVManager,
                 com.crux.common.config.Config"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Surat Pengantar Gabungan">
<%
   boolean showNotes = true;

   final ReceiptForm form = (ReceiptForm) request.getAttribute("FORM");

   final ARReceiptView receipt = form.getReceipt();
   
   //receipt.getStARSettlementID();

   final boolean masterCurrency = receipt.isMasterCurrency();

   final boolean isAP = receipt.isAP();
   final boolean isAR = receipt.isAR();

   final boolean isNote = receipt.isNote();

   final boolean hasReceiptClass = receipt.getStReceiptClassID()!=null;
   
   final boolean hasBankType = receipt.getStBankType()!=null;
   

%>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <c:field name="parinvoiceid" type="string" hidden="true"/>
         <c:field name="letterno" type="string" hidden="true"/>


      </td>
   </tr>

      <c:evaluate when="true">
         <tr>
            <td>
               
               <table cellpadding=2 cellspacing=1>
               <tr>
            <td>
               
               <table cellpadding=2 cellspacing=1>
                
                  <tr> 
                     <td colspan="2">
                        <c:field name="invoicesindex" hidden="true" type="string"/>
                        <c:field name="invoicecomissionindex" hidden="true" type="string"/>
                        <c:listbox name="listInvoices" selectable="true" >
                           
                           <c:listcol title="" columnClass="header" >
                              <c:button text="+" clientEvent="addnewinvoice()" validate="false" enabled="true"/>
                           </c:listcol>
                          
                           <c:listcol title="" columnClass="detail" >
                              <c:button text="-" event="onDeleteInvoiceItem" clientEvent="f.invoicesindex.value='$index$';" validate="false" enabled="true" />
   
                           </c:listcol>
                           <c:listcol name="invoice.stARInvoiceID" title="ID" />
                           <c:listcol name="invoice.stInvoiceNo" title="NO" />
                           <c:listcol name="invoice.dbAttrPolicyTSI" align="right" title="INSURED" ></c:listcol>
                     <%-- 
                           <c:evaluate when="<%=currentLine!=null && currentLine.getDetails().size()>0%>" >

                                    <%
                                       final DTOList commissions = currentLine.getDetails();

                                       for (int i = 0; i < commissions.size(); i++) {
                                          ARReceiptLinesView rcl = (ARReceiptLinesView) commissions.get(i);

                                          %>
                                          </tr><tr>
          									<td class=row1></td>
                                          <td class=row1></td>
                                          
                                          <td class=row1><%=jspUtil.print(rcl.getStDescription())%></td>
                                          <td align="right" class=row1><%=jspUtil.print(rcl.getDbInvoiceAmount(),2)%></td>
                                          <%
                                       }
                                    %>
                              </tr>
                              <tr>
                                 <td colspan=7>
                                    &nbsp;
                                 </td>
                           </c:evaluate>
                          --%>
                        </c:listbox>
                     </td>
                  </tr>
               </table>
            </td>
         </tr>
      </c:evaluate>
     
     <%--   end of tes 
      
   </c:evaluate>--%>
   <tr>
   <td>
   <table>
   <tr>
         <td align=center>
         <c:button text="Print" event="clickPrintLetter" />
         <c:button text="Clear" event="doClose"/>
      </td>
   </tr>
   </table>
   </td>
      
      

   </tr>
</table>

</c:frame>
<script>
   function addnewinvoice() {
      openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_LETTER',
                  550,400,
         function (o) {
            if (o!=null) {
               f.parinvoiceid.value=o.INVOICE_ID;
               f.action_event.value='onNewInvoice';
               f.submit();
            }
         }
      );
   }
</script>
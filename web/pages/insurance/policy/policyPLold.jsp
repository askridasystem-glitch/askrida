<%@ page import="com.webfin.ar.model.ARReceiptView,
                 com.webfin.ar.forms.ReceiptForm,
                 com.webfin.ar.model.ARReceiptLinesView,
                 com.crux.util.DTOList,
                 com.crux.util.Tools,
                 com.webfin.ar.model.ARAPSettlementView,
                 com.crux.util.JSPUtil,
                 com.crux.common.config.Config"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="SETTLEMENT" >
<%

   final ReceiptForm form = (ReceiptForm) request.getAttribute("FORM");

   final ARReceiptView receipt = form.getReceipt();
   
   int phase=0;

   boolean readOnly = form.isReadOnly();


%>
<table cellpadding=2 cellspacing=1>

		<%--  
         <tr>
            <td>
               <%="CARI POLIS"%>
               <table cellpadding=2 cellspacing=1>
                  <tr>
                     <td>
                     <c:field name="parinvoiceid" type="string" hidden="false"/>
                        <c:listbox name="listInvoices" selectable="true" >
                        <%
                           final ARReceiptLinesView currentLine = (ARReceiptLinesView)form.getListInvoices().get(index.intValue());
                        %> 
                           <c:listcol title="" columnClass="header" >
                              <c:button text="+" clientEvent="addnewinvoice()" validate="false" enabled="<%=!readOnly%>"/>
                           </c:listcol>

                           <c:listcol title="" columnClass="detail" >
                              <c:button text="-" event="onDeleteInvoiceItem" clientEvent="f.invoicesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>" />
                                 <c:button text="V" event="onExpandInvoiceItem" clientEvent="f.invoicesindex.value='$index$';" validate="false" />
                           </c:listcol>
                           <c:listcol name="stPolicyID" title="ID" />
                           <c:listcol name="stPolicyNo" title="Policy No" />
                        </c:listbox>
                     </td>
                  </tr>
               </table>
            </td>
         </tr>
         --%>
      
         <tr>
            <td>
               <%="CARI POLIS"%>
               <table cellpadding=2 cellspacing=1>
                  <tr>
                     <td>
                        <c:field name="invoicesindex" hidden="true" type="string"/>
                        <c:field name="invoicecomissionindex" hidden="true" type="string"/>
                        <c:listbox name="listInvoices" selectable="true" >
                        <%
                           final ARReceiptLinesView currentLine = (ARReceiptLinesView)form.getListInvoices().get(index.intValue());

                           final boolean expanded = currentLine!=null && Tools.isYes(currentLine.getStExpandedFlag());
                        %> 
           
          
                           <c:listcol title="" columnClass="header" >
                              <c:button text="+" clientEvent="addnewinvoice()" validate="false" enabled="<%=!readOnly%>"/>
                           </c:listcol>

                           <c:listcol title="" columnClass="detail" >
                              <c:button text="-" event="onDeleteInvoiceItem" clientEvent="f.invoicesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>" />
                              <c:evaluate when="<%=!expanded%>" >
                                 <c:button text="V" event="onExpandInvoiceItem" clientEvent="f.invoicesindex.value='$index$';" validate="false" />
                              </c:evaluate>
                              <c:evaluate when="<%=expanded%>" >
                                 <c:button text="^" event="onShrinkInvoiceItem" clientEvent="f.invoicesindex.value='$index$';" validate="false" />
                              </c:evaluate>
                           </c:listcol>
                           <c:listcol name="invoice.stARInvoiceID" title="ID" />
                           <c:listcol name="stInvoiceNo" title="NO" />
                           <c:listcol align="right" title="TOTAL(ORG)" ></c:listcol>
                           <c:listcol align="right" title="Outstanding (ORG)" ></c:listcol>
                           <c:listcol align="right" title="SETTLED(ORG)" ></c:listcol>
                           <%--<c:listcol name="dbActInvOutstandingAmountIDR" title="AMOUNT(IDR)" />--%>
                           <c:listcol align="right" title="Payment(ORG)" >
                              
                              
                           </c:listcol>
                           <c:listcol align="right" title="<%="Payment("+ccy+")"%>" />
                           <%--  <c:listcol  title="O/S" />--%>
                           <c:evaluate when="<%=currentLine!=null && currentLine.getDetails().size()>0%>" >

                                    <%
                                       final DTOList commissions = currentLine.getDetails();

                                       for (int i = 0; i < commissions.size(); i++) {
                                          ARReceiptLinesView rcl = (ARReceiptLinesView) commissions.get(i);

                                          %>
                                          </tr><tr>
                                            <%--  <td class=row1><c:button text="-" event="onDeleteInvoiceComissionItem" clientEvent="<%="f.invoicesindex.value='$index$';f.invoicecomissionindex.value='"+i+"';"%>" validate="false" enabled="<%=!readOnly%>"/></td>
                                          --%><td class=row1></td>
                                          <td class=row1></td>
                                          
                                          <td class=row1><%=jspUtil.print(rcl.getStDescription())%></td>
                                          <td align="right" class=row1><%=jspUtil.print(rcl.getDbInvoiceAmount(),2)%></td>
                                          <td align="right" class=row1><%=jspUtil.print(rcl.getDbOutstandingAmountAct(),2)%></td>
                                          <td align="right" class=row1><%=jspUtil.print(rcl.getDbAmountSettled(),2)%></td>
                                          <td class=row1><c:field name="<%="listInvoices.[$index$].details.["+i+"].dbEnteredAmount"%>" caption="Amount" type="money16.2" /></td>
                                          <td align="right" class=row1><%=jspUtil.print(rcl.getDbAmount(),2)%></td>
                                          <%--  <td class=row1><c:field name="<%="listInvoices.[$index$].details.["+i+"].stCheck"%>" caption="Check" type="check" /></td>--%>
                                          <%
                                       }
                                    %>
                              </tr>
                              <tr>
                                 <td colspan=7>
                                    &nbsp;
                                 </td>
                           </c:evaluate>
                        </c:listbox>
                     </td>
                  </tr>
               </table>
            </td>
         </tr>

     
     <%--   end of tes --%>
      
   <tr>
      <td align=center>
          <c:button text="Print" event="doRecalculate"/>
      </td>
   </tr>
</table>

</c:frame>
<script>
   function addnewinvoice() {
      openDialog('c.ctl?EVENT=INS_POL_SEARCH',
                  500,400,
         function (o) {
            if (o!=null) {
               f.parinvoiceid.value=o.POL_ID;
               f.action_event.value='onNewInvoice';
               f.submit();
            }
         }
      );
   }

</script>
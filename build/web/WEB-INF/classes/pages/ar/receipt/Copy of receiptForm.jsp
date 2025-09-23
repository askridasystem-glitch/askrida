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
   

   showNotes = isNote && hasReceiptClass;
   boolean showInvoices = true && hasReceiptClass;
   boolean showXC = false;
   boolean suratHutang = false;

   final ARAPSettlementView settlement = receipt.getSettlement();

   if (settlement!=null) {
      showXC = settlement.checkProperty("EN_XC","Y");
   }


   showInvoices &= settlement.checkProperty("EN_SL_IV","Y");
   showNotes &= settlement.isNote();
   suratHutang = settlement.checkProperty("SH","Y");

   final String ccy = receipt.getStCurrencyCode();

   /*
   stEntityID
   stARSettlementID
   */

   int phase=0;

   if (receipt.getStCostCenterCode()!=null) phase=1;
   if (receipt.getStReceiptClassID()!=null) phase=2;
   //if (receipt.getStPaymentMethodID()!=null) phase=3;
   if (receipt.getStBankType()!=null) phase=3;
   //if(receipt.getStBankType()==null&&receipt.getStReceiptClassID()!=null) phase=3;
   
   if(phase==2 && receipt.getStReceiptClassID()!=null) phase=3;

   if (phase==2 && showNotes && receipt.getStCostCenterCode()!=null) phase=3;

   final boolean developmentMode = Config.isDevelopmentMode();

   boolean readOnly = form.isReadOnly();
   
   boolean titipan=false;
   if (receipt.getStReceiptClassID()!=null)
   		if(receipt.getStReceiptClassID().equals("8")) titipan=true; 

%>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <c:field name="parinvoiceid" type="string" hidden="true"/>
         <c:field name="nosurathutang" type="string" hidden="true"/>
         <c:field name="artitipanid" type="string" hidden="true"/>
         
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
               <table cellpadding=2 cellspacing=1>
                  <c:field name="receipt.stReceiptNo" width="130" mandatory="true" caption="Payment No" type="string|64" presentation="standard" readonly="true" show="<%=receipt.getStReceiptNo()!=null%>" />
                  <c:field lov="LOV_CostCenter" width="200" name="receipt.stCostCenterCode" mandatory="true" caption="Branch" type="string" presentation="standard" readonly="<%=receipt.getStCostCenterCode()!=null%>" changeaction="changeCostCenter" />
                  <c:field readonly="<%=hasReceiptClass%>" lov="LOV_ReceiptClass" width="150" changeaction="changeReceiptClass" name="receipt.stReceiptClassID" mandatory="true" caption="Method" type="string" presentation="standard">
                     <c:param name="invoice_type" value="<%=receipt.getStInvoiceType()%>" />
                  </c:field>
                  
                  <c:evaluate when="<%=titipan%>">
                  <c:evaluate when="<%=phase>=2%>">
                  <c:field readonly="<%=hasBankType%>" lov="LOV_BankType" width="150" changeaction="changeBankType" name="receipt.stBankType" mandatory="true" caption="Bank Type" type="string" presentation="standard"/> 
                  </c:evaluate>
                  </c:evaluate>
                  
                  <c:evaluate when="<%=phase>=3%>" >
                     <c:evaluate when="<%=!isNote%>">
                        <c:field lov="LOV_PaymentMethod" width="400" name="receipt.stPaymentMethodID" mandatory="true" caption="Account" type="string"  presentation="standard" readonly="<%=receipt.getStPaymentMethodID()!=null%>" changeaction="changemethod" >
                           <c:lovLink name="rc" link="receipt.stReceiptClassID" clientLink="false"/>
                           <c:param name="ccbanktype" value="<%=receipt.getStCostCenterCode()+"|"+receipt.getStBankType()%>" />
                        </c:field>
                     </c:evaluate>
                  </c:evaluate>
                  <c:evaluate when="<%=phase>=3%>">
                     <c:field name="receipt.dtReceiptDate" mandatory="true" caption="Date" type="date" presentation="standard"/>
                     <c:field lov="LOV_Currency" changeaction="chgCurrency" name="receipt.stCurrencyCode" caption="Currency" type="string" presentation="standard"/>
                     <c:field readonly="<%=masterCurrency%>" mandatory="true" name="receipt.dbCurrencyRate" caption="Rate" type="money16.2" presentation="standard"/>
                     <%--<c:evaluate when="<%=!isNote%>">--%>
                        <c:field readonly="<%=isNote%>" name="receipt.dbEnteredAmount" mandatory="true" caption="Amount(ORG)" type="money16.2" presentation="standard" width="200"/>
                        <c:field include="<%=!masterCurrency%>"  name="receipt.dbAmount" readonly="true" caption="Amount(IDR)" type="money16.2" presentation="standard" width="200"/>
                     <%--</c:evaluate>--%>

                     <c:field readonly="<%=receipt.getStEntityID()!=null%>" changeaction="onChangeCust" name="receipt.stEntityID" type="string" width="200" popuplov="true"  lov="LOV_Entity" mandatory="true" caption="Customer" presentation="standard" />

                  </c:evaluate>
                  	
                  </c:evaluate>

            
               </table>
               </td>
               <td>
                  <c:evaluate when="<%=phase>=3%>">
                     <table cellpadding=2 cellspacing=1>
                        <c:field name="receipt.stInvoiceType" type="string" hidden="true" />
                        <c:field name="receipt.stNoteType" type="string" hidden="true" />
                        <c:field name="receipt.stInvoiceTypeDesc" readonly="true" caption="Invoice Type" type="string" presentation="standard"/>
                        <c:evaluate when="<%=!isNote%>">
                           <c:field name="receipt.dbAmountApplied" readonly="true" caption="Applied" type="money16.2"  width="200" presentation="standard"/>
                           <c:field name="receipt.dbRateDiffAmount" readonly="true" caption="Rate Diff" type="money16.2" width="200" presentation="standard"/>
                        </c:evaluate>
                        <c:field name="receipt.dbAmountRemain" readonly="true" caption="Remaining" type="money16.2" width="200" presentation="standard"/>
                        <c:field name="receipt.stPostedFlag" caption="Posted" type="check" presentation="standard" readonly="true"/>
                     
                     </table>
                     <table>
                     <c:evaluate when="<%=titipan%>">
                     <tr>
                     <td>
                     	Titipan Premi : <c:field readonly="false" name="receipt.stARTitipanID" type="string" width="150" mandatory="true" caption="Titipan Premi" />
                     </td>
                     <td>
                     	<c:button text="..." clientEvent="searchTitipan()" validate="false" enabled="<%=!readOnly%>"/>
                     </td>
                     </tr>
                     </c:evaluate>
                     </table>
                  </c:evaluate>
               </td>
               
               <tr>
               
            </tr>
               
            </tr>
            <tr>
               <td colspan=2>
                  <c:evaluate when="<%=phase>=3%>">
                     <table cellpadding=2 cellspacing=1>
                        <c:field name="receipt.stShortDescription" mandatory="false" caption="Description" type="string|128" width="400" presentation="standard" />
                        <c:field name="receipt.stDescription" mandatory="false" caption="Remarks" type="string|255" rows="2" width="400" presentation="standard" />
                    	<c:field name="receipt.stARSettlementID" hidden="true" caption="stARSettlementID" type="string" presentation="standard"/>
           			</table>
                  </c:evaluate>
               </td>
            </tr>
    
         </table>

      </td>
   </tr>
   <c:evaluate when="<%=phase>=3%>">
      <c:evaluate when="<%=showNotes%>">
         <tr>
            <td>
               <%=isAP?"Receivable":"Payable"%>
               <table cellpadding=2 cellspacing=1>
                  <tr>
                     <td>
                        <c:field name="notesindex" hidden="true" type="string"/>
                        <c:listbox name="listNotes" >
                           <c:listcol title="" columnClass="header" >
                              <c:button text="+" clientEvent="addnewnote()" validate="false" enabled="<%=!readOnly%>" />
                           </c:listcol>
                            <c:listcol title="" columnClass="detail" >
                              <c:button text="-" event="onDeleteNoteItem" clientEvent="f.notesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                           </c:listcol>
                           <c:listcol name="stInvoiceNo" title="INVOICE NO" />
                           <c:listcol name="dbActInvAmount" title="TOTAL(ORG)" />
                           <c:listcol name="dbEnteredInvoiceAmount" title="AMOUNT(ORG)" />
                           <c:listcol name="dbInvoiceAmount" title="AMOUNT(IDR)" />
                           <c:listcol title="APPLY(ORG)" >
                              <c:field name="listNotes.[$index$].dbEnteredAmount" caption="Amount" type="money16.2" />
                           </c:listcol>
                           <c:listcol name="dbAmount" title="APPLY(IDR)" />
                        </c:listbox>
                     </td>
                  </tr>
               </table>
            </td>
         </tr>
      </c:evaluate>
      
      <%--  
      <c:evaluate when="<%=showInvoices%>">
         <tr>
            <td>
               <%=isAR?"Receivable":"Payable"%>
               <table cellpadding=2 cellspacing=1>
                  <tr>
                     <td>
                        
                        <c:field name="invoicecomissionindex" hidden="true" type="string"/>
                        <c:listbox name="listInvoices" selectable="true" >
                        <%
                           final ARReceiptLinesView currentLine = (ARReceiptLinesView)form.getListInvoices().get(index.intValue());

                           final boolean expanded = currentLine!=null && Tools.isYes(currentLine.getStExpandedFlag());
                        %> 
                        
                         <%
                             final DTOList commissions = currentLine.getDetails();

                             for (int i = 0; i < commissions.size(); i++) {
                                 ARReceiptLinesView rcl = (ARReceiptLinesView) commissions.get(i);

                          %>
                        	<c:evaluate when="<%=suratHutang%>" >
                           <c:listcol title="" columnClass="header" >
                              <c:button text="+ SH" clientEvent="addnewsurathutang()" validate="false" enabled="<%=!readOnly%>"/>
                           </c:listcol>
                           </c:evaluate>
                           <c:evaluate when="<%=!suratHutang%>" >
                           <c:listcol title="" columnClass="header" >
                              <c:button text="+" clientEvent="addnewinvoice()" validate="false" enabled="<%=!readOnly%>"/>
                           </c:listcol>
                           </c:evaluate>
                           <c:listcol title="" columnClass="detail" >
                             
                           </c:listcol>
                           <c:listcol title="ID" />
                           <c:listcol title="Description" ><%=jspUtil.print(rcl.getStDescription())%></c:listcol>
                           <c:listcol align="right" title="TOTAL(ORG)" ><%=jspUtil.print(rcl.getDbInvoiceAmount(),2)%></c:listcol>
                           <c:listcol align="right" title="Outstanding (ORG)" ><%=jspUtil.print(rcl.getDbOutstandingAmountAct(),2)%></c:listcol>
                           <c:listcol align="right" title="SETTLED(ORG)" ><%=jspUtil.print(rcl.getDbAmountSettled(),2)%></c:listcol>
                           <c:listcol align="right" title="Payment(ORG)" ><c:field name="<%="listInvoices.[$index$].details.["+i+"].dbEnteredAmount"%>" caption="Amount" type="money16.2" /></c:listcol>
                           <c:listcol align="right" name="dbAmount2" title="<%="Payment("+ccy+")"%>" ><%=jspUtil.print(rcl.getDbAmount(),2)%></c:listcol>
                           <%
                            }
                           %>
                           
                        </c:listbox>
                     </td>
                  </tr>
               </table>
            </td>
         </tr>
      </c:evaluate>
      --%>
      
      <%-- tes --%>
      
      <c:evaluate when="<%=showInvoices%>">
         <tr>
            <td>
               <%=isAR?"Receivable":"Payable"%>
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
                        	<c:evaluate when="<%=suratHutang%>" >
                           <c:listcol title="" columnClass="header" >
                              <c:button text="+ SH" clientEvent="addnewsurathutang()" validate="false" enabled="<%=!readOnly%>"/>
                           </c:listcol>
                           </c:evaluate>
                           <c:evaluate when="<%=!suratHutang%>" >
                           <c:listcol title="" columnClass="header" >
                              <c:button text="+" clientEvent="addnewinvoice()" validate="false" enabled="<%=!readOnly%>"/>
                           </c:listcol>
                           </c:evaluate>
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
                           <c:listcol align="right" title="TOTAL(ORG)" ><%=currentLine.getStCurrencyCode()+" "+JSPUtil.print(currentLine.getDbActInvAmount(),2)%></c:listcol>
                           <c:listcol align="right" title="Outstanding (ORG)" ><%=currentLine.getStCurrencyCode()+" "+JSPUtil.print(currentLine.getDbOutstandingAmount(),2)%></c:listcol>
                           <c:listcol align="right" title="SETTLED(ORG)" ><%=currentLine.getStCurrencyCode()+" "+JSPUtil.print(currentLine.getDbAmountSettled(),2)%></c:listcol>
                           <%--<c:listcol name="dbActInvOutstandingAmountIDR" title="AMOUNT(IDR)" />--%>
                           <c:listcol align="right" title="Payment(ORG)" >
                              <%=currentLine.getStCurrencyCode()%>
                              <c:field name="listInvoices.[$index$].dbEnteredAmount" caption="Amount" type="money16.2" />
                           </c:listcol>
                           <c:listcol align="right" name="dbAmount" title="<%="Payment("+ccy+")"%>" />
                           <c:listcol  title="O/S" />
                           <c:evaluate when="<%=currentLine!=null && currentLine.getDetails().size()>0%>" >

                                    <%
                                       final DTOList commissions = currentLine.getDetails();

                                       for (int i = 0; i < commissions.size(); i++) {
                                          ARReceiptLinesView rcl = (ARReceiptLinesView) commissions.get(i);

                                          %>
                                          </tr><tr>
                                            <td class=row1><c:button text="-" event="onDeleteInvoiceComissionItem" clientEvent="<%="f.invoicesindex.value='$index$';f.invoicecomissionindex.value='"+i+"';"%>" validate="false" enabled="<%=!readOnly%>"/></td>
                                          <td class=row1></td>
                                          
                                          <td class=row1><%=jspUtil.print(rcl.getStDescription())%></td>
                                          <td align="right" class=row1><%=jspUtil.print(rcl.getDbInvoiceAmount(),2)%></td>
                                          <td align="right" class=row1><%=jspUtil.print(rcl.getDbOutstandingAmountAct(),2)%></td>
                                          <td align="right" class=row1><%=jspUtil.print(rcl.getDbAmountSettled(),2)%></td>
                                          <td class=row1><c:field name="<%="listInvoices.[$index$].details.["+i+"].dbEnteredAmount"%>" caption="Amount" type="money16.2" /></td>
                                          <td align="right" class=row1><%=jspUtil.print(rcl.getDbAmount(),2)%></td>
                                          <td class=row1><c:field name="<%="listInvoices.[$index$].details.["+i+"].stCheck"%>" caption="Check" type="check" /></td>
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
      </c:evaluate>
     
     <%--   end of tes --%>
      
      <c:evaluate when="<%=showXC%>">
         <tr>
            <td>
               Miscellaneous :
               <c:field name="stGLSelect" width="150" type="string" caption="Item" lov="LOV_SettlementXC" >
                  <c:param name="arsettlementid" value="<%=receipt.getStARSettlementID()%>" />
               </c:field>
               <c:button text="+" event="addGL" validate="false" enabled="<%=!readOnly%>"/>
               <table cellpadding=2 cellspacing=1>
                  <tr>
                     <td>
                        <c:field name="glindex" hidden="true" type="string"/>
                        <c:listbox name="listGLs" >
                           <c:listcol title="" columnClass="header" >

                           </c:listcol>
                           <c:listcol title="" columnClass="detail" >
                              <c:button text="-" event="onDeleteGLItem" clientEvent="f.glindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                           </c:listcol>
                           <c:listcol name="stInvoiceNo" title="Description" />
                           <c:listcol title="APPLY(ORG)" >
                              <c:field name="listGLs.[$index$].dbEnteredAmount" caption="Amount" type="money16.2" />
                           </c:listcol>
                           <c:listcol name="dbAmount" title="APPLY(IDR)" />
                        </c:listbox>
                     </td>
                  </tr>
               </table>
            </td>
         </tr>

      </c:evaluate>
   </c:evaluate>
   <tr>
      <td align=center>
         <c:evaluate when="<%=!readOnly%>" >
            <c:evaluate when="<%=phase>=3%>">
               <c:button text="Recalculate" event="doRecalculate"/>
               <c:button text="Save" event="doSave" validate="true" confirm="Do you want to save ?" />
            </c:evaluate>
            <c:button text="Cancel" event="doCancel" confirm="Do you want to cancel ?" />
            <c:button text="Generate Payment No" event="generatRNo" show="<%=developmentMode%>" />
         </c:evaluate>
         <c:evaluate when="<%=readOnly%>" >
            <c:button text="Close" event="doClose"/>
         </c:evaluate>
         <c:button text="Approve" event="doApprove" confirm="Do you want to approve ?" />

      </td>
   </tr>
</table>

</c:frame>
<script>
   function addnewinvoice() {
      openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_INVOICE'
                  +'&ccy='+docEl('receipt.stCurrencyCode').value
                  +'&cust='+docEl('receipt.stEntityID').value
                  +'&arsid='+docEl('receipt.stARSettlementID').value
                  +'&cc_code='+docEl('receipt.stCostCenterCode').value
                  +'&type='+docEl('receipt.stInvoiceType').value,
                  500,400,
         function (o) {
            if (o!=null) {
               f.parinvoiceid.value=o.INVOICE_ID;
               f.action_event.value='onNewInvoice';
               f.submit();
            }
         }
      );
   }

   function addnewnote() {
      openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_INVOICE'
                     +'&ccy='+docEl('receipt.stCurrencyCode').value
                     +'&cust='+docEl('receipt.stEntityID').value
                     +'&type='+docEl('receipt.stNoteType').value,
                     500,400,
         function (o) {
            if (o!=null) {
               f.parinvoiceid.value=o.INVOICE_ID;
               f.action_event.value='onNewNote';
               f.submit();
            }
         }
      );
   }
   
   function addnewsurathutang() {
      openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_SURAT_HUTANG'
                  +'&ccy='+docEl('receipt.stCurrencyCode').value
                  +'&cust='+docEl('receipt.stEntityID').value
                  +'&arsid='+docEl('receipt.stARSettlementID').value
                  +'&cc_code='+docEl('receipt.stCostCenterCode').value
                  +'&type='+docEl('receipt.stInvoiceType').value,
                  500,400,
         function (o) {
            if (o!=null) {
               f.nosurathutang.value=o.NO_SURAT_HUTANG;
               f.action_event.value='onNewSuratHutang';
               f.submit();
            }
         }
      );
   }
   
      function searchTitipan() {
      openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_TITIPAN'
                  +'&ccy='+docEl('receipt.stCurrencyCode').value
                  +'&cust='+docEl('receipt.stEntityID').value
                  +'&paymentmethodid='+docEl('receipt.stPaymentMethodID').value
                  +'&arsid='+docEl('receipt.stARSettlementID').value
                  +'&cc_code='+docEl('receipt.stCostCenterCode').value
                  +'&type='+docEl('receipt.stInvoiceType').value,
                  500,400,
         function (o) {
            if (o!=null) {
               f.artitipanid.value=o.TITIPAN_ID;
               document.getElementById('receipt.stARTitipanID').value=o.TITIPAN_ID;
               //f.action_event.value='onNewSuratHutang';
               //f.submit();
            }
         }
      );
   }
</script>
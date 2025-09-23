<%@ page import="com.webfin.ar.model.ARReceiptView,
com.webfin.ar.forms.ReceiptForm,
com.webfin.ar.model.ARReceiptLinesView,
com.crux.util.DTOList,
com.crux.util.Tools,
com.webfin.ar.model.ARAPSettlementView,
com.crux.util.JSPUtil,
com.crux.common.config.Config"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Cetak Kwitansi" >
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
        </td>
    </tr>
    
    <c:evaluate when="true">
        
        <tr>
        <td>
        
        <table cellpadding=2 cellspacing=1>
        <tr>
            <td style="width:100">
                No. Kwitansi
            </td>
            <td>
                <c:field name="stReceiptNo" type="string" width="100" mandatory="false" caption="No Kwitansi" />
            </td>
        </tr>
        <tr>
            <td style="width:100">
                Tanggal Pembayaran
            </td>
            <td>
                <c:field name="dtReceipt" type="date" caption="Tanggal Pembayaran Kwitansi"  />
            </td>
        </tr>
        <tr> 
            <td colspan="2">
            <c:field name="invoicesindex" hidden="true" type="string"/>
            <c:field name="invoicecomissionindex" hidden="true" type="string"/>
            <c:listbox name="listInvoices" selectable="true" >
            <%
            final ARReceiptLinesView currentLine = (ARReceiptLinesView)form.getListInvoices().get(index.intValue());
            
            final boolean expanded = currentLine!=null && Tools.isYes(currentLine.getStExpandedFlag());
            %> 
            
            
            <c:listcol title="" columnClass="header" >
                <c:button text="+" clientEvent="addnewinvoice()" validate="false" enabled="true"/>
            </c:listcol>
            
            <c:listcol title="" columnClass="detail" >
                <c:button text="-" event="onDeleteInvoiceItem" clientEvent="f.invoicesindex.value='$index$';" validate="false" enabled="true" />
                
            </c:listcol>
            <c:listcol name="invoice.stARInvoiceID" title="ID" />
            <c:listcol name="stInvoiceNo" title="NO" />
            <c:listcol align="right" title="TOTAL(ORG)" ></c:listcol>
            <c:listcol align="right" title="Outstanding (ORG)" ></c:listcol>
            <c:listcol align="right" title="SETTLED(ORG)" ></c:listcol>
            <%--<c:listcol name="dbActInvOutstandingAmountIDR" title="AMOUNT(IDR)" />--%>
            <%--  <c:listcol  title="O/S" />--%>
                           
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
            <td align="right" class=row1><%=jspUtil.print(rcl.getDbOutstandingAmountAct(),2)%></td>
            <td align="right" class=row1><%=jspUtil.print(rcl.getDbAmountSettled(),2)%></td>
            
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
</c:evaluate>

<%--   end of tes --%>

<tr>
    <td>
        <table>
            <tr>
                <td>{L-ENGReceipt Type-L}{L-INAPrint Kwitansi-L}</td>
                <td>:</td>
                <td align=center>
                    <c:field width="200" lov="LOV_RECEIPTTYPE" caption="{L-ENGReport Type-L}{L-INAJenis Laporan-L}" name="stReceiptType" type="string" mandatory="true" />
                    <c:button text="Print" clientEvent="dynPrintClick();" />
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
      openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_KWITANSI',
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
   
   function dynPrintClick() {

      if (f.stReceiptType.value=='') {
         alert('Pilih Jenis Laporan');
         f.stReceiptType.focus();
         return;
      }
	 f.action_event.value='clickPrintReceipt';
         f.submit();
   }

</script>
<%@ page import="com.webfin.ar.model.ARReceiptView,
com.webfin.ar.forms.ReceiptForm,
com.webfin.ar.model.ARReceiptLinesView,
com.crux.util.DTOList,
com.crux.util.Tools,
com.webfin.ar.model.ARAPSettlementView,
com.crux.util.JSPUtil,
com.crux.web.controller.SessionManager,
com.crux.lov.LOVManager,
com.crux.common.config.Config"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Cetak Pengajuan Klaim">
<%
final ReceiptForm form = (ReceiptForm) request.getAttribute("FORM");

final ARReceiptView receipt = form.getReceipt();

boolean admin = true;

if (SessionManager.getInstance().getSession().getStBranch()!=null){
    admin = false;
}
%>
<table cellpadding=2 cellspacing=1>
    <tr>
        <td>
            <c:field name="parinvoiceid" type="string" hidden="true"/>
            <c:field name="claimno" type="string" hidden="true"/>
        </td>
    </tr>
    
    <c:evaluate when="true">
        <tr>
        <td>        
        <table cellpadding=2 cellspacing=1>
        <tr>            
            <table cellpadding=2 cellspacing=1>
            <tr>
                <td style="width:100">
                    No. Konversi
                </td>
                <td>
                    <c:field name="stReceiptNo" type="string" width="200" mandatory="false" caption="No Kwitansi"/><c:button text="Search" clientEvent="searchno()" validate="false" enabled="true"/>
                </td>
            </tr>
            <tr>
                <td style="width:100">
                    Rekening Bank
                </td>
                <td>
                    <c:field clientchangeaction="selectProducer2()" name="stEntityID" descfield="stEntityName" type="string" width="200" caption="Name" popuplov="true" lov="LOV_Account"/>
                </td>
            </tr>
            <tr>
                <td style="width:100">
                    Nama Bank
                </td>
                <td>
                    <c:field name="stAddress" type="string|255" rows="2" width="200" mandatory="false"/>
                </td>
            </tr>
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
            
            <c:listcol name="invoice.stTransactionHeaderID" title="ID" />
            <c:listcol name="invoice.stTransactionHeaderNo" title="NO" />
            <c:listcol name="invoice.dtApplyDate" title="DATE" />
            <c:listcol name="invoice.dbAmount" align="right" title="AMOUNT(ORG)" ></c:listcol>
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

<%--   end of tes --%>

<tr>
    <td>
        <table>
            <tr>
                <td align=center>
                    <c:button text="Print" event="clickPrintClaim" />
                    <c:button text="Clear" event="doClose"/>
                </td>
            </tr>
        </table>
    </td>
    
    
    
</tr>
</table>

</c:frame>
<script>
    
   function searchno() {
      openDialog('c.ctl?EVENT=AR_RECEIPT_BUDEP',
                  700,400,
         function (o) {
            if (o!=null) {
               f.claimno.value=o.CLAIMNO;
               f.stReceiptNo.value=o.CLAIMNO;
               f.stEntityID.value=o.COINSID;
               f.stEntityName.value=o.CLAIMNAME;
               f.stAddress.value=o.COINSADDRESS;
               f.action_event.value='onNewBudepNo';
               f.submit();
            }
         }
      );
   }
   
   function selectProducer2() {
        var o = window.lovPopResult;
        document.getElementById('accountno').value = o.address;
    }
</script>
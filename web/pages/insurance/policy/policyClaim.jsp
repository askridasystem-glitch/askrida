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
boolean showNotes = true;

final ReceiptForm form = (ReceiptForm) request.getAttribute("FORM");

final ARReceiptView receipt = form.getReceipt();

boolean admin = true;

if (SessionManager.getInstance().getSession().getStBranch()!=null){
    admin = false;
}

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
                    No.
                </td>
                <td>
                    <c:field name="stReceiptNo" type="string" width="200" mandatory="false" caption="No Kwitansi"/><c:button text="Search" clientEvent="searchno()" validate="false" enabled="true"/>
                </td>
            </tr>
            <tr>
                <td style="width:100">
                    Name
                </td>
                <td>
                    <c:field name="stName" type="string" width="200" caption="Name"/>
                </td>
            </tr>
            <tr>
                <td style="width:100">
                    Coinsurer
                </td>
                <td>
                    <c:field clientchangeaction="selectProducer2()" name="stEntityID" descfield="stEntityName" type="string" width="200" caption="Name" popuplov="true" lov="LOV_Entity"/>
                </td>
            </tr>
            <tr>
                <td style="width:100">
                    Address
                </td>
                <td>
                    <c:field name="stAddress" type="string|255" rows="2" width="200" mandatory="false"/>
                </td>
            </tr>
            <tr>
                <td style="width:100">
                    Authorized
                </td>
                <td>
                    <c:field clientchangeaction="selectProfil()" name="stUserID" descfield="stUserName" type="string" width="200" popuplov="true" lov="LOV_Profil" mandatory="false"/>
                </td>
            </tr>
            <tr>
                <td style="width:100">
                    Division
                </td>
                <td>
                    <c:field name="stDivision" type="string" width="200" mandatory="false"/>
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
            <c:listcol name="invoice.stARInvoiceID" title="ID" />
            <c:listcol name="stInvoiceNo" title="NO" />
            <c:listcol align="right" title="AMOUNT(ORG)" ></c:listcol>
            
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
                    <c:evaluate when="<%=admin%>" >
                        <c:button text="Report Akuntansi" clientEvent="dynPrintClick();" />
                    </c:evaluate>
                    <c:button text="Export To Excel" event="clickPrintExcel" />
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
      openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_CLAIM',
                  700,400,
         function (o) {
            if (o!=null) {
               f.parinvoiceid.value=o.INVOICE_ID;
               f.action_event.value='onNewInvoice';
               f.submit();
            }
         }
      );
   }
   function searchno() {
      openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_NO',
                  700,400,
         function (o) {
            if (o!=null) {
               f.claimno.value=o.CLAIMNO;
               f.stReceiptNo.value=o.CLAIMNO;
               f.stName.value=o.CLAIMNAME;
               f.stEntityID.value=o.COINSID;
               f.stEntityName.value=o.COINSNAME;
               f.stAddress.value=o.COINSADDRESS;
               f.action_event.value='onNewClaimNo';
               f.submit();
            }
         }
      );
   }
   
   function selectProducer2() {
        var o = window.lovPopResult;
        document.getElementById('stAddress').value = o.address;
    }
    
    function selectProfil() {
        var o = window.lovPopResult;
        document.getElementById('stDivision').value = o.division;
    }
   
   function dynPrintClick() {
	 f.action_event.value='clickPrintForAccounting';
         f.submit();
   }
</script>
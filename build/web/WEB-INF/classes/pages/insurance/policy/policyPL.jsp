<%@ page import="com.webfin.ar.model.ARReceiptView,
         com.webfin.ar.forms.ReceiptForm,
         com.webfin.ar.model.ARReceiptLinesView,
         com.crux.util.DTOList,
         com.crux.util.Tools,
         com.webfin.ar.model.ARAPSettlementView,
         com.crux.util.JSPUtil,
         com.crux.common.config.Config"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Cetak Monitoring Pembayaran" >
    <%
                boolean showNotes = true;

                final ReceiptForm form = (ReceiptForm) request.getAttribute("FORM");

                final ARReceiptView receipt = form.getReceipt();

                final boolean masterCurrency = receipt.isMasterCurrency();

                final boolean isAP = receipt.isAP();
                final boolean isAR = receipt.isAR();

                final boolean isNote = receipt.isNote();

                final boolean hasReceiptClass = receipt.getStReceiptClassID() != null;

                final boolean hasBankType = receipt.getStBankType() != null;


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
                                    No. Rekap Monitoring
                                </td>
                                <td>
                                    <c:field name="stReceiptNo" type="string" width="200" mandatory="false" caption="No Rekap"/><c:button text="Search" clientEvent="searchno()" validate="false" enabled="true"/>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:100">
                                    Keterangan
                                </td>
                                <td>
                                    <c:field name="stName" type="string" width="200" caption="Name"/>
                                </td>
                            </tr>

                            <tr>
                                <td style="width:100">
                                    Upload Excel
                                </td>
                                <td>
                                    <c:field name="receipt.stFilePhysic" caption="Upload Excel" type="file" thumbnail="true"/>
                                </td>
                            </tr>
                            <tr>
                                <td><c:button show="true" text="Konversi" event="uploadExcelMonitoring"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <c:field name="invoicesindex" hidden="true" type="string"/>
                                    <c:field name="invoicecomissionindex" hidden="true" type="string"/>
                                    <c:listbox name="listInvoices" selectable="true" >
                                        <%
                                                    final ARReceiptLinesView currentLine = (ARReceiptLinesView) form.getListInvoices().get(index.intValue());

                                                    final boolean expanded = currentLine != null && Tools.isYes(currentLine.getStExpandedFlag());
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

                                        <c:evaluate when="<%=currentLine != null && currentLine.getDetails().size() > 0%>" >

                                            <%
                                                        final DTOList commissions = currentLine.getDetails();

                                                        for (int i = 0; i < commissions.size(); i++) {
                                                            ARReceiptLinesView rcl = (ARReceiptLinesView) commissions.get(i);

                                            %>
                                    </tr><tr>
                                        <td class=row1></td>
                                        <td class=row1></td>

                                        <td class=row1><%=jspUtil.print(rcl.getStDescription())%></td>
                                        <td align="right" class=row1><%=jspUtil.print(rcl.getDbInvoiceAmount(), 2)%></td>
                                        <td align="right" class=row1><%=jspUtil.print(rcl.getDbOutstandingAmountAct(), 2)%></td>
                                        <td align="right" class=row1><%=jspUtil.print(rcl.getDbAmountSettled(), 2)%></td>

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
                        <td>{L-ENGReport Type-L}{L-INAJenis Report-L}</td>
                        <td>:</td>
                        <td align=center>
                            <c:field width="150" lov="LOV_PLTYPE" caption="{L-ENGReport Type-L}{L-INAJenis Laporan-L}" name="stReportType" type="string" mandatory="true" />
                            Font <c:field name="stFontSize" width="60" type="string" lov="LOV_FONTSIZE" />
                        </td>
                    </tr>
                    <tr>
                        <td>Kurs</td>
                        <td></td>
                        <td align=start>
                            <c:field name="stCurrency" width="150" type="string" lov="LOV_CURRENCY2" />
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
        600,400,
        function (o) {
            if (o!=null) {
                f.parinvoiceid.value=o.INVOICE_ID;
                f.action_event.value='onNewInvoiceAllInstallment';
                f.submit();
            }
        }
    );
    }
   
    function searchno() {
        openDialog('c.ctl?EVENT=AR_RECEIPT_SEARCH_PLNO',
        700,400,
        function (o) {
            if (o!=null) {
                f.claimno.value=o.CLAIMNO;
                f.stReceiptNo.value=o.CLAIMNO;
                f.stName.value=o.CLAIMNAME;
                f.action_event.value='onNewClaimNo';
                f.submit();
            }
        }
    );
    }
   
    function dynPrintClick() {

        if (f.stReportType.value=='') {
            alert('Pilih Jenis Laporan');
            f.stReportType.focus();
            return;
        }
        f.action_event.value='btnPrint';
        f.submit();
    }
</script>
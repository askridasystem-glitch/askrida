<%@ page import="com.webfin.ar.model.ARReceiptView,
         com.webfin.ar.forms.ReceiptPembayaranKlaimForm,
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

                final ReceiptPembayaranKlaimForm form = (ReceiptPembayaranKlaimForm) request.getAttribute("FORM");

                final ARReceiptView receipt = form.getReceipt();

                boolean admin = true;

                if (SessionManager.getInstance().getSession().getStBranch() != null) {
                    admin = false;
                }

                boolean cabang = true;

                if (form.getCcCode() != null) {
                    if (form.getCcCode().equalsIgnoreCase("00")) {
                        cabang = false;
                    }
                }

                if (form.getCcCode() == null) {
                    cabang = false;
                }

                boolean readOnly = form.isReadOnly();
                boolean canAdd = readOnly;

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <c:field name="parinvoiceid" type="string" hidden="true"/>
                <c:field name="invoiceid" type="string" hidden="true"/>
            </td>
        </tr>

        <c:evaluate when="true">
            <tr>
                <td>
                    <table cellpadding=2 cellspacing=1>
                        <tr>

                        <table cellpadding=2 cellspacing=1>
                            <tr>
                                <td>
                                    Cabang
                                </td>
                                <td>
                                    <c:field name="stBranch" descfield="stBranchDesc" type="string" caption="Branch" lov="LOV_Branch" mandatory="false" readonly="<%=form.getCcCode() != null%>"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Tanggal LKS
                                </td>
                                <td>
                                    <c:field name="dtApplyDateFrom" type="date" mandatory="false" caption="Apply From"/>
                                    To <c:field name="dtApplyDateTo" type="date" mandatory="false" caption="Apply To" />
                                </td>
                            </tr>

                            </tr>

                            <tr>
                                <td colspan="2">
                                    <c:field name="invoicesindex" hidden="true" type="string"/>
                                    <c:listbox name="listInvoices" selectable="true" >
                                        <%
                                                    final ARReceiptLinesView currentLine = (ARReceiptLinesView) form.getListInvoices().get(index.intValue());

                                                    final boolean expanded = currentLine != null && Tools.isYes(currentLine.getStExpandedFlag());
                                        %>

                                        <c:listcol title="" columnClass="header" >
                                            <c:button text="+ Klaim" clientEvent="selectPolicy()" validate="false" enabled="<%=!canAdd%>"/>
                                        </c:listcol>

                                        <c:listcol title="" columnClass="detail" >
                                            <c:button text="-" event="onDeleteInvoiceItem" clientEvent="f.invoicesindex.value='$index$';" validate="false" enabled="true" />
                                        </c:listcol>
                                        <c:listcol name="invoice.stARInvoiceID" title="ID" />
                                        <c:listcol name="stPolicyID" title="POL ID" />
                                        <c:listcol name="stInvoiceNo" title="NO POLIS" />
                                        <c:listcol name="stArInvoiceClaim" title="DLA NO" />
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
                            <c:button text="Validasi" event="clickValidate" />
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>

</c:frame>
<script>
    function selectPolicy(o){

        var cabang = document.getElementById('stBranch').value;
        var date1 = document.getElementById('dtApplyDateFrom').value;
        var date2 = document.getElementById('dtApplyDateTo').value;

        openDialog('so.ctl?EVENT=INS_CLAIM_SEARCH_VLD&costcenter='+cabang+'&dateFrom='+date1+'&dateTo='+date2+'', 700,450,function (o) {
            if (o!=null) {
                f.parinvoiceid.value=o.polid;
                f.invoiceid.value=o.invoiceid;
                f.action_event.value='onNewKlaimByPolID';
                f.submit();
            }
        });
    }
</script>
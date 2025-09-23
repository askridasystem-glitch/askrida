<%@ page import="com.crux.util.JSPUtil,
         com.crux.util.validation.FieldValidator,
         com.crux.util.DTOList,
         com.crux.util.DateUtil,
         java.util.Date,
         org.joda.time.DateTime,
         com.webfin.ar.model.ARInvoiceView"%>
<%@ taglib prefix="c" uri="crux" %>                 
<html><% final JSPUtil jspUtil = new JSPUtil(request, response);%>
    <head>
        <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
        <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
    </head>
    <%
                final String key = request.getParameter("key");

                final String key2 = request.getParameter("key2");

                final String arsid = request.getParameter("arsid");

                final String cc_code = request.getParameter("cc_code");

                final DTOList list = (DTOList) request.getAttribute("LIST");

                final String rcpdate = request.getParameter("rcpdate");

                final String cust = request.getParameter("cust");

                boolean hasClaim = false;

                if (arsid.equalsIgnoreCase("10") || arsid.equalsIgnoreCase("34") || arsid.equalsIgnoreCase("14")) {
                    hasClaim = true;
                }

                final Date tglPembukuan = DateUtil.getDate(rcpdate);

                final String journaltype = request.getParameter("journaltype");
    %>
    <body>
        <form name=f method=POST action="ctl.ctl">
            <input type=hidden name=EVENT value="AR_RECEIPT_SEARCH_INVOICE_RI">
            <input type=hidden name=ccy value="">
            <input type=hidden name=ivtype value="">
            <input type=hidden name=arsid value="<%=arsid%>">
            <input type=hidden name=cc_code value="<%=cc_code%>">
            <input type=hidden name=rcpdate value="<%=rcpdate%>">
            <input type=hidden name=cust value="<%=cust%>">
            <input type=hidden name=journaltype value="<%=journaltype%>">
            <table cellpadding=2 cellspacing=1>
                <tr>
                    <td>
                        <%=jspUtil.getHeader2("CARI HUTANG/PIUTANG")%>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table cellpadding=2 cellspacing=1>
                            <tr>
                                <td>NO POLIS / NO BUKTI <%if (hasClaim) {%> / NO LKS/LKP<%}%></td>
                                <td>:</td>
                                <td><%=jspUtil.getInputText("key", new FieldValidator("Search Key", "string", 50), key, 200, JSPUtil.MANDATORY)%></td>
                            </tr>
                            <tr>
                                <td>NO SLIP RI <%if (hasClaim) {%> / NO LKS/LKP<%}%></td>
                                <td>:</td>
                                <td><%=jspUtil.getInputText("key2", new FieldValidator("Search Key", "string", 50), key2, 200, JSPUtil.MANDATORY)%></td>
                            </tr>
                            <tr>
                                <td align="left">
                                    <%=jspUtil.getButtonEvent("Search", "AR_RECEIPT_SEARCH_INVOICE_RI")%>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td>
                        <table cellpadding=2 cellspacing=1>
                            <tr class=header>
                                <td></td>
                                <td>ID</td>
                                <c:evaluate when="<%=hasClaim%>">
                                    <td>No LKP</td>
                                </c:evaluate>
                                <td>No Invoice</td>
                                <td>Tanggal Approval</td>
                                <td>Tanggal Cicilan</td>
                                <td>Tanggal Bayar</td>
                                <td>No. Bukti</td>
                                <td>Sumber Bisnis</td>
                                <td>CCY</td>
                                <td>RI Slip</td>
                                <td align=right>Jumlah</td>
                                <td align=right>Outstanding</td>
                            </tr>
                            <%
                                        for (int i = 0; i < list.size(); i++) {
                                            ARInvoiceView iv = (ARInvoiceView) list.get(i);

                                            //cek jika tgl approved > tgl bayar
                                            DateTime tglApproved = new DateTime();
                                            DateTime tglBayar = new DateTime();
                                            DateTime tglAkhir = new DateTime();

                                            if (iv.getPolicy() != null) {
                                                tglApproved = new DateTime(iv.getPolicy().getDtApprovedDate());
                                            } else {
                                                tglApproved = new DateTime(iv.getDtDueDate());
                                            }

                                            tglBayar = new DateTime(tglPembukuan);
                                            tglAkhir = tglBayar.dayOfMonth().withMaximumValue();

                                            tglApproved = tglApproved.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

                                            boolean showButton = true;

                                            /*if (tglApproved.isAfter(tglAkhir)) {
                                            showButton = false;
                                            }*/

                                            if (iv.getDtReceipt() != null) {
                                                showButton = false;
                                            }

                                            if (iv.getStUsedFlag() != null) {
                                                if (iv.getStUsedFlag().equalsIgnoreCase("Y")) {
                                                    showButton = false;
                                                }
                                            }
                            %>
                            <tr class=row<%=i % 2%>>

                                <td>
                                    <%if (showButton) {%>
                                    <%=jspUtil.getButtonNormal("b", "*", "selectInvoice('" + iv.getStARInvoiceID() + "')")%>
                                    <%}%>
                                </td>
                                <td><%=jspUtil.print(iv.getStARInvoiceID())%></td>
                                <c:evaluate when="<%=hasClaim%>">
                                    <td><%=jspUtil.print(iv.getStRefID2())%></td>
                                </c:evaluate>
                                <td><%=jspUtil.print(iv.getStInvoiceNo())%></td>
                                <td><font color="red"><%=jspUtil.print(iv.getPolicy() != null ? iv.getPolicy().getDtApprovedDate() : "")%></font></td>
                                <td><font color="red"><%=jspUtil.print(iv.getDtDueDate())%></font></td>
                                <td><font color="red"><%=jspUtil.print(iv.getDtReceipt())%></font></td>
                                <td><font color="red"><%=jspUtil.print(iv.getStReceiptNo())%></font></td>
                                <td><%=jspUtil.print(iv.getStEntityName())%></td>
                                <td><%=jspUtil.print(iv.getStCurrencyCode())%></td>
                                <td><%=jspUtil.print(iv.getStReferenceA1())%></td>
                                <td align=right><%=jspUtil.printX(iv.getDbAmount(), 2)%></td>
                                <td align=right><%=jspUtil.printX(iv.getDbOutstandingAmount(), 2)%></td>
                            </tr>
                            <% }%>
                        </table>
                    </td>
                </tr>

            </table>
        </form>
    </body>
</html>
<script>
    function selectInvoice(o) {
        dialogReturn({INVOICE_ID:o});

        window.close();
    }
</script>
<%@page import="java.util.Date"%>
<%@ page import="com.crux.util.JSPUtil,
         com.crux.util.validation.FieldValidator,
         com.crux.util.DTOList,
         java.util.Date,
com.webfin.ar.model.ARInvoiceView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response);%>
    <head>
        <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
        <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
    </head>
    <%
                final String key = request.getParameter("key");

                final String arsid = request.getParameter("arsid");

                final String cc_code = request.getParameter("cc_code");

                final String tax_code = request.getParameter("tax_code");

                final Date start_date = (Date) request.getAttribute("start_date");

                final Date end_date = (Date) request.getAttribute("end_date");

                final DTOList list = (DTOList) request.getAttribute("LIST");

                final String type = request.getParameter("type");

                final String cust = request.getParameter("cust");

                final String jenas = request.getParameter("jenas");

                final String treaty = request.getParameter("treaty");

                final Date start_per_date = (Date) request.getAttribute("start_per_date");

                final Date end_per_date = (Date) request.getAttribute("end_per_date");
    %>
    <body>
        <form name=f method=POST action="ctl.ctl">
            <input type=hidden name=EVENT value="AR_RECEIPT_SURAT_HUTANG_RI">
            <input type=hidden name=ccy value="">
            <input type=hidden name=ivtype value="">
            <input type=hidden name=cc_code value="<%=cc_code%>">
            <input type=hidden name=arsid value="<%=arsid%>">
            <input type=hidden name=tax_code value="<%=tax_code%>">
            <input type=hidden name=type value="<%=type%>">
            <input type=hidden name=cust value="<%=cust%>">
            <input type=hidden name=jenas value="<%=jenas%>">
            <input type=hidden name=treaty value="<%=treaty%>">
            <table cellpadding=2 cellspacing=1>
                <tr>
                    <td>
                        <%=jspUtil.getHeader("SEARCH INVOICE")%>
                    </td>
                </tr>

                <%
                            String label = type.equalsIgnoreCase("AR") ? "Surat Piutang" : "Surat Hutang";
                %>

                <tr>
                    <td>
                        <table cellpadding=2 cellspacing=1>
                            <tr>
                                <td>TANGGAL PRODUKSI</td>
                                <td>:</td>
                                <td>
                                    <table cellpadding=2 cellspacing=1>
                                        <tr>
                                            <td><%=jspUtil.getInputText("start_date", new FieldValidator("", "Start Date", "date", -1), start_date, 200, JSPUtil.NOTEXTMODE)%></td>
                                            <td>s/d</td>
                                            <td><%=jspUtil.getInputText("end_date", new FieldValidator("", "End Date", "date", -1), end_date, 200, JSPUtil.NOTEXTMODE)%></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td>UY YEAR</td>
                                <td>:</td>
                                <td>
                                    <table cellpadding=2 cellspacing=1>
                                        <tr>
                                            <td><%=jspUtil.getInputText("start_per_date", new FieldValidator("", "Period Start Date", "date", -1), start_per_date, 200, JSPUtil.NOTEXTMODE)%></td>
                                            <td>s/d</td>
                                            <td><%=jspUtil.getInputText("end_per_date", new FieldValidator("", "Period End Date", "date", -1), end_per_date, 200, JSPUtil.NOTEXTMODE)%></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td><%=label.toUpperCase()%></td>
                                <td>:</td>
                                <td><%=jspUtil.getInputText("key", new FieldValidator("Search Key", "string", 50), key, 200, JSPUtil.NOTEXTMODE)%></td>
                            </tr>
                            <tr>
                                <td align="left">
                                    <%=jspUtil.getButtonEvent("Search", "AR_RECEIPT_SURAT_HUTANG_RI")%>
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
                                <td>No. <%=label%></td>
                                <td>Sumber Bisnis</td>
                                <td>Jumlah</td>
                                <td>Total</td>
                                <td>Tanggal Produksi</td>
                                <td>Tanggal Produksi S/D</td>
                                <td>Periode Awal</td>
                                <td>Periode Akhir</td>
                            </tr>
                            <%
                                        for (int i = 0; i < list.size(); i++) {
                                            ARInvoiceView iv = (ARInvoiceView) list.get(i);

                            %>
                            <tr class=row<%=i % 2%>>
                                <td>
                                    <%=jspUtil.getButtonNormal("b", "*", "selectInvoice('" + iv.getStNoSuratHutang() + "','" + iv.getDtSuratHutangPeriodFrom() + "','" + iv.getDtSuratHutangPeriodTo() + "','" + iv.getDtAttrPolicyPeriodStart() + "','" + iv.getDtAttrPolicyPeriodEnd() + "')")%>
                                </td>
                                <td><%=jspUtil.print(iv.getStNoSuratHutang())%></td>
                                <td><%=jspUtil.print(iv.getStReferenceA0())%></td>
                                <td><%=jspUtil.print(iv.getStReferenceA1())%></td>
                                <td><%=jspUtil.print(iv.getDbAmount(), 2)%></td>
                                <td><%=jspUtil.print(iv.getDtSuratHutangPeriodFrom())%></td>
                                <td><%=jspUtil.print(iv.getDtSuratHutangPeriodTo())%></td>
                                <td><%=jspUtil.print(iv.getDtAttrPolicyPeriodStart())%></td>
                                <td><%=jspUtil.print(iv.getDtAttrPolicyPeriodEnd())%></td>
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
    function selectInvoice(o,p,q,r,s) {
        dialogReturn({NO_SURAT_HUTANG:o,TGL_MULAI:p,TGL_AKHIR:q,PER_TGL_MULAI:r,PER_TGL_AKHIR:s});
        window.close();
    }
</script>
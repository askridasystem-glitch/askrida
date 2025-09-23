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

                final DTOList list = (DTOList) request.getAttribute("LIST");
    %>
    <body>
        <form name=f method=POST action="ctl.ctl">
            <input type=hidden name=EVENT value="AR_RECEIPT_SURAT_HUTANG_RI_REKAP">
            <table cellpadding=2 cellspacing=1>
                <tr>
                    <td>
                        <%=jspUtil.getHeader("SEARCH INVOICE")%>
                    </td>
                </tr>

                <%
                            String label = "Surat Hutang Rekap";
                %>

                <tr>
                    <td>
                        <table cellpadding=2 cellspacing=1>                            
                            <tr>
                                <td><%=label.toUpperCase()%></td>
                                <td>:</td>
                                <td><%=jspUtil.getInputText("key", new FieldValidator("Search Key", "string", 50), key, 200, JSPUtil.NOTEXTMODE)%></td>
                            </tr>
                            <tr>
                                <td align="left">
                                    <%=jspUtil.getButtonEvent("Search", "AR_RECEIPT_SURAT_HUTANG_RI_REKAP")%>
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
                                <td>Description</td>
                            </tr>
                            <%
                                        for (int i = 0; i < list.size(); i++) {
                                            ARInvoiceView iv = (ARInvoiceView) list.get(i);

                            %>
                            <tr class=row<%=i % 2%>>
                                <td>
                                    <%=jspUtil.getButtonNormal("b", "*", "selectInvoice('" + iv.getStNoSuratHutang() + "')")%>
                                </td>
                                <td><%=jspUtil.print(iv.getStNoSuratHutang())%></td>
                                <td><%=jspUtil.print(iv.getStDescription())%></td>
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
        dialogReturn({NO_SURAT_HUTANG_REKAP:o});
        window.close();
    }
</script>
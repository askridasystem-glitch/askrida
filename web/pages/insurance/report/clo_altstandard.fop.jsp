<%@ page import="com.crux.web.controller.SessionManager,
         com.webfin.insurance.model.InsuranceClosingView,
         com.crux.util.fop.FOPUtil,
         com.webfin.ar.model.ARInvoiceView,
         java.math.BigDecimal,
         com.crux.util.*,
         com.crux.common.parameter.Parameter,
         com.crux.lang.LanguageManager,
         java.util.Date"%><?xml version="1.0" ?>

<%
            final InsuranceClosingView pol = (InsuranceClosingView) request.getAttribute("CLOSING");
%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="21cm"
                               page-width="29cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="3cm"
                               margin-right="3cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0.5cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <fo:flow flow-name="xsl-region-body">

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block font-family="serif" font-weight="bold" font-size="12pt" text-align="center">
                                    PRODUKSI KLAIM REASURANSI PER REASURADUR
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block font-family="serif" font-weight="bold">
                                    No Surat Hutang : <%=JSPUtil.xmlEscape(pol.getStNoSuratHutang())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">NO</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">REINSURER</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">JENIS</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">TREATY</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">KURS</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">MATA UANG</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">ORIGINAL</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" border-right-style="solid" padding="2pt" ><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                    BigDecimal[] t = new BigDecimal[1];

                                    final DTOList invoice = pol.getInvoice();
                                    for (int i = 0; i < invoice.size(); i++) {
                                        ARInvoiceView inv = (ARInvoiceView) invoice.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], inv.getDbAmount());

                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(inv.getEntity().getStShortName())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(inv.getPolicyType().getStShortDescription().toUpperCase())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(inv.getStReferenceZ1().toUpperCase())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(inv.getDbCurrencyRate(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(inv.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(inv.getDbEnteredAmount(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(inv.getDbAmount(), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7" border-left-style="solid"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" border-right-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(t[0], 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>

                </fo:table>
            </fo:block>

            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
            </fo:block>

            <fo:block font-size="8pt">
                {L-ENG Print User-L}{L-INA Print User-L} : <%=SessionManager.getInstance().getCreateUser().getStShortName()%>
            </fo:block>

            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>" orientation="0">
                        <barcode:datamatrix>
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>
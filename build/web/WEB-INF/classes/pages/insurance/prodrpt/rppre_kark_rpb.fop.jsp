<%@ page import="com.webfin.insurance.model.*,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.form.ProductionReinsuranceReportForm,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>

<%
            final DTOList l = (DTOList) request.getAttribute("RPT");

            final ProductionReinsuranceReportForm form = (ProductionReinsuranceReportForm) SessionManager.getInstance().getCurrentForm();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-width="30cm"
                               page-height="21cm"
                               margin-top="0.5cm"
                               margin-bottom="1cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">
            <fo:region-body margin-top="2cm" margin-bottom="1cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="1cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <fo:static-content flow-name="xsl-region-after">
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block text-align="start" font-weight="bold">Kepada</fo:block>
                                <fo:block text-align="start" font-weight="bold">Administrator</fo:block>
                                <fo:block text-align="start" font-weight="bold">KONSORSIUM ASURANSI RISIKO KHUSUS (KARK)</fo:block>
                                <fo:block text-align="start" font-weight="bold">d.a PT. Tugu Reasuransi Indonesia</fo:block>
                                <fo:block text-align="start" font-weight="bold">Jl. Raden Saleh 50</fo:block>
                                <fo:block text-align="start" font-weight="bold">Jakarta 10330</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block font-size="12pt" text-align="center" space-before.optimum="5pt" space-after.optimum="5pt">
                                    REKAPITULASI PRODUKSI BULANAN (RPB)
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="start">No. RPB</fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align="start"><%=JSPUtil.printX(form.getStNoUrut())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="start">Bulan / Tahun</fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align="start"><%=DateUtil.getMonth(form.getPolicyDateFrom())%>/<%=DateUtil.getYear(form.getPolicyDateFrom())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="start">U/Y</fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align="start">
                                    <% if (form.getPeriodFrom() != null) {%>
                                    <%=DateUtil.getYear(form.getPeriodFrom())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="start">Kantor Cabang</fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align="start">
                                    <% if (form.getStBranch() != null) {%>
                                    <%=JSPUtil.printX(form.getStBranchDesc())%>
                                    <% } else {%>
                                    Seluruh Cabang
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">No.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">No. LPH</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">No. Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Zona EQ</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Zona Banjir</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Jangka Waktu</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Curr</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">Jumlah</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                    BigDecimal komisi = new BigDecimal(0);
                                    BigDecimal[] t = new BigDecimal[2];
                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());

                                        komisi = BDUtil.add(pol.getDbNDComm1(), pol.getDbNDComm2());
                                        t[n] = BDUtil.add(t[n++], komisi);

                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(String.valueOf(i + 1))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="start" line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="start" line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getDtPeriodStart())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbPremiTotal(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" border-width="0.5pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[0], 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" border-width="0.5pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">KOMISI SESI</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[1], 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" border-width="0.5pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">NET PREMI</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.sub(t[0], t[1]), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
            </fo:block>

            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[0], 0)%>" orientation="0">
                        <barcode:datamatrix>
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>
            </fo:block>

            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                        <svg xmlns="http://www.w3.org/2000/svg" width="15cm" height="1cm" xml:space="preserve">
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>

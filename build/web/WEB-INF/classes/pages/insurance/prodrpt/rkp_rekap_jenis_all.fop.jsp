<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         java.util.Date,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.form.ProductionClaimReportForm,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final DTOList l = (DTOList) request.getAttribute("RPT");

            final ProductionClaimReportForm form = (ProductionClaimReportForm) SessionManager.getInstance().getCurrentForm();

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="first"
                               page-height="21cm"
                               page-width="44cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">

            <fo:region-body margin-top="0cm" margin-bottom="2cm"/>
            <fo:region-before extent="0.5cm"/>
            <fo:region-after extent="1cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="first">

        <fo:static-content flow-name="xsl-region-after">
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">

            <!-- defines text title level 1-->

            <!-- GARIS  -->

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="50mm"/>
                    <% for (int t = 0; t < 10; t++) {%>
                    <fo:table-column column-width="30mm"/>
                    <%}%>
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">
                                    REKAPITULASI PRODUKSI KLAIM PER JENIS KESELURUHAN
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block text-align="center">
                                    <% if (form.getAppDateFrom() != null || form.getAppDateTo() != null) {%>
                                    Tanggal Disetujui : <%=JSPUtil.printX(form.getAppDateFrom())%> S/D <%=JSPUtil.printX(form.getAppDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block text-align="center">
                                    <% if (form.getPeriodFrom() != null || form.getPeriodTo() != null) {%>
                                    Periode Awal : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">No.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Jenis Asuransi</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Aceh</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Medan</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Padang</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Riau</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Jambi</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Palembang</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Bengkulu</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Lampung</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Bangka Belitung</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Batam</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>
                    <fo:table-body>
                        <!-- GARIS DALAM KOLOM -->
                        <%
                                    BigDecimal[] t = new BigDecimal[10];

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], pol.getDbTsiBPDAN());//10
                                        t[n] = BDUtil.add(t[n++], pol.getDbTsiOR());//11
                                        t[n] = BDUtil.add(t[n++], pol.getDbTsiQS());//12
                                        t[n] = BDUtil.add(t[n++], pol.getDbTsiSPL());//13
                                        t[n] = BDUtil.add(t[n++], pol.getDbTsiFAC());//14
                                        t[n] = BDUtil.add(t[n++], pol.getDbTsiPARK());//15
                                        t[n] = BDUtil.add(t[n++], pol.getDbTsiFACO());//16
                                        t[n] = BDUtil.add(t[n++], pol.getDbTsiXOL1());//17
                                        t[n] = BDUtil.add(t[n++], pol.getDbTsiXOL2());//18
                                        t[n] = BDUtil.add(t[n++], pol.getDbTsiXOL3());//19


                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(pol.getPolicyType().getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiBPDAN(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiOR(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiQS(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiSPL(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiFAC(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiPARK(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiFACO(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiXOL1(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiXOL2(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiXOL3(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                    }
                        %>


                        <!-- GARIS DALAM KOLOM -->


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[4], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[5], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[6], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[7], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[8], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[9], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
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
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[0], 0)%>" orientation="0">
                        <barcode:datamatrix>
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>

    <fo:page-sequence master-reference="first">

        <fo:static-content flow-name="xsl-region-after">
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">

            <!-- defines text title level 1-->

            <!-- GARIS  -->

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="50mm"/>
                    <% for (int y = 0; y < 10; y++) {%>
                    <fo:table-column column-width="30mm"/>
                    <%}%>
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">
                                    REKAPITULASI PRODUKSI KLAIM PER JENIS KESELURUHAN
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block text-align="center">
                                    <% if (form.getAppDateFrom() != null || form.getAppDateTo() != null) {%>
                                    Tanggal Disetujui : <%=JSPUtil.printX(form.getAppDateFrom())%> S/D <%=JSPUtil.printX(form.getAppDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block text-align="center">
                                    <% if (form.getPeriodFrom() != null || form.getPeriodTo() != null) {%>
                                    Periode Awal : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">No.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Jenis Asuransi</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Jakarta</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Bandung</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Semarang</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Yogyakarta</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Surabaya</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Serang </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Pontianak</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Palangkaraya</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Banjarmasin</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Samarinda</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>
                    <fo:table-body>
                        <!-- GARIS DALAM KOLOM -->
                        <%
                                    BigDecimal[] y = new BigDecimal[10];

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        y[n] = BDUtil.add(y[n++], pol.getDbTsiXOL4());//20
                                        y[n] = BDUtil.add(y[n++], pol.getDbTsiXOL5());//21
                                        y[n] = BDUtil.add(y[n++], pol.getDbPremiBPDAN());//22
                                        y[n] = BDUtil.add(y[n++], pol.getDbPremiOR());//23
                                        y[n] = BDUtil.add(y[n++], pol.getDbPremiQS());//24
                                        y[n] = BDUtil.add(y[n++], pol.getDbPremiSPL());//25
                                        y[n] = BDUtil.add(y[n++], pol.getDbPremiFAC());//30
                                        y[n] = BDUtil.add(y[n++], pol.getDbPremiPARK());//31
                                        y[n] = BDUtil.add(y[n++], pol.getDbPremiFACO());//32
                                        y[n] = BDUtil.add(y[n++], pol.getDbPremiXOL1());//33


                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(pol.getPolicyType().getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiXOL4(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiXOL5(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiBPDAN(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiOR(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiQS(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiSPL(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiFAC(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiPARK(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiFACO(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiXOL1(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                    }
                        %>


                        <!-- GARIS DALAM KOLOM -->


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(y[0], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(y[1], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(y[2], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(y[3], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(y[4], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(y[5], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(y[6], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(y[7], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(y[8], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(y[9], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
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
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[0], 0)%>" orientation="0">
                        <barcode:datamatrix>
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>

    <fo:page-sequence master-reference="first">

        <fo:static-content flow-name="xsl-region-after">
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">

            <!-- defines text title level 1-->

            <!-- GARIS  -->

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="50mm"/>
                    <% for (int o = 0; o < 12; o++) {%>
                    <fo:table-column column-width="30mm"/>
                    <%}%>
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="14">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">
                                    REKAPITULASI PRODUKSI KLAIM PER JENIS KESELURUHAN
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="14">
                                <fo:block text-align="center">
                                    <% if (form.getAppDateFrom() != null || form.getAppDateTo() != null) {%>
                                    Tanggal Disetujui : <%=JSPUtil.printX(form.getAppDateFrom())%> S/D <%=JSPUtil.printX(form.getAppDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="14">
                                <fo:block text-align="center">
                                    <% if (form.getPeriodFrom() != null || form.getPeriodTo() != null) {%>
                                    Periode Awal : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="14">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">No.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Jenis Asuransi</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Manado</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Palu</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Kendari</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Makasar</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Mamuju</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Gorontalo</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Denpasar</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Mataram</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Kupang</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Ambon</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Ternate</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Klaim Papua</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="14">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>
                    <fo:table-body>
                        <!-- GARIS DALAM KOLOM -->
                        <%
                                    BigDecimal[] o = new BigDecimal[12];

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        o[n] = BDUtil.add(o[n++], pol.getDbPremiXOL2());//40
                                        o[n] = BDUtil.add(o[n++], pol.getDbPremiXOL3());//41
                                        o[n] = BDUtil.add(o[n++], pol.getDbPremiXOL4());//42
                                        o[n] = BDUtil.add(o[n++], pol.getDbPremiXOL5());//43
                                        o[n] = BDUtil.add(o[n++], pol.getDbCommBPDAN());//44
                                        o[n] = BDUtil.add(o[n++], pol.getDbCommOR());//45
                                        o[n] = BDUtil.add(o[n++], pol.getDbCommQS());//50
                                        o[n] = BDUtil.add(o[n++], pol.getDbCommSPL());//51
                                        o[n] = BDUtil.add(o[n++], pol.getDbCommFAC());//52
                                        o[n] = BDUtil.add(o[n++], pol.getDbCommPARK());//60
                                        o[n] = BDUtil.add(o[n++], pol.getDbCommFACO());//61
                                        o[n] = BDUtil.add(o[n++], pol.getDbCommXOL1());//70


                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(pol.getPolicyType().getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiXOL2(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiXOL3(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiXOL4(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiXOL5(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCommBPDAN(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCommOR(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCommQS(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCommSPL(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCommFAC(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCommPARK(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCommFACO(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCommXOL1(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                    }
                        %>


                        <!-- GARIS DALAM KOLOM -->


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="14">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(o[0], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(o[1], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(o[2], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(o[3], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(o[4], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(o[5], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(o[6], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(o[7], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(o[8], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(o[9], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(o[10], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(o[11], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="14">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
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
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[0], 0)%>" orientation="0">
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
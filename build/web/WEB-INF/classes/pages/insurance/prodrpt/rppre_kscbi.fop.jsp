<%@ page import="com.webfin.insurance.model.*, 
         com.crux.util.*,
         com.crux.util.Tools,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         com.crux.common.parameter.Parameter,
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
                               page-width="49cm"
                               page-height="21cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only">

        <fo:flow flow-name="xsl-region-body">
            <!-- usage of page layout -->
            <!-- HEADER -->

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="10pt" text-align="center">
                                    KERJASAMA SURETY DAN CUSTOMS BOND INDONESIA
                                </fo:block>
                                <fo:block font-family="tahoma" font-weight="bold" font-size="10pt" text-align="center">
                                    REKAPITULASI LAPORAN PRODUKSI BULANAN
                                </fo:block>
                                <fo:block font-family="tahoma" font-weight="bold" font-size="10pt" text-align="center">
                                    SURETY BOND DAN KONTRA BANK GARANSI
                                </fo:block>
                                <fo:block font-family="tahoma" font-weight="bold" font-size="10pt" text-align="center">
                                    (10% DARI NILAI JAMINAN, MAKSIMAL RP. 500 JUTA)
                                </fo:block>
                                <fo:block font-family="tahoma" font-weight="bold" font-size="10pt" text-align="center">
                                    BULAN <%=DateUtil.getMonth(form.getPolicyDateFrom())%> TAHUN <%=DateUtil.getYear(form.getPolicyDateTo())%>
                                </fo:block>
                                <fo:block font-family="tahoma" font-weight="bold" font-size="10pt" text-align="center">
                                    PT. ASURANSI BANGUN ASKRIDA
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17">
                                <fo:block font-family="tahoma" font-size="8pt" text-align="center" space-after.optimum="10pt">
                                    REFERENSI NO :
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">No.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">No. Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Principal</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">No. NPWP Principal</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Obligee</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Bank Penerbit</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">No. Penjaminan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Periode Awal</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Periode Akhir</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Jenis Produk</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Nilai Jaminan (100%)</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Nilai Jaminan (10%)/Max. 500 Jt</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Rate</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Gross Biaya Jasa (A)</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Handling Fee (B = 35% x A)</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Total untuk Pool (C = A - B)</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm" text-align="center">Keterangan</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>
                    <fo:table-body>

                        <%
                                    BigDecimal[] t = new BigDecimal[5];

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());
                                        t[n] = BDUtil.add(t[n++], pol.getDbAmount());
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiBase());
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiNetto());
                                        t[n] = BDUtil.add(t[n++], BDUtil.sub(pol.getDbPremiBase(),pol.getDbPremiNetto()));

                                        String jenis = null;
                                        if (pol.getStPolicyTypeID().equalsIgnoreCase("51")) {
                                            jenis = "A1";
                                        } else if (pol.getStPolicyTypeID().equalsIgnoreCase("52")) {
                                            jenis = "A2";
                                        } else if (pol.getStPolicyTypeID().equalsIgnoreCase("53")) {
                                            jenis = "A3";
                                        } else if (pol.getStPolicyTypeID().equalsIgnoreCase("54")) {
                                            jenis = "A4";
                                        } else if (pol.getStPolicyTypeID().equalsIgnoreCase("55")) {
                                            jenis = "C1";
                                        } else if (pol.getStPolicyTypeID().equalsIgnoreCase("56")) {
                                            jenis = "C2";
                                        } else if (pol.getStPolicyTypeID().equalsIgnoreCase("57")) {
                                            jenis = "C3";
                                        } else if (pol.getStPolicyTypeID().equalsIgnoreCase("58")) {
                                            jenis = "C4";
                                        }

                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center"><%= JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="start"><%= JSPUtil.xmlEscape(pol.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="start"><%= JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="start"><%if (pol.getStReference2() != null) {%><%= JSPUtil.xmlEscape(pol.getStReference2())%><% }%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center"><%if (pol.getStReference3() != null) {%><%= JSPUtil.xmlEscape(pol.getStReference3())%><% }%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center"><%= JSPUtil.printX(pol.getDtPeriodStart())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center"><%= JSPUtil.printX(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center"><%= JSPUtil.printX(jenis)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="end"><%= JSPUtil.printX(pol.getDbInsuredAmount(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="end"><%= JSPUtil.printX(pol.getDbAmount(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="end"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="end"><%= JSPUtil.printX(pol.getDbPremiBase(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="end"><%= JSPUtil.printX(pol.getDbPremiNetto(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="end"><%= JSPUtil.printX(BDUtil.sub(pol.getDbPremiBase(),pol.getDbPremiNetto()), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm" text-align="start"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" number-columns-spanned="10"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="end"><%= JSPUtil.printX(t[0], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="end"><%= JSPUtil.printX(t[1], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="end"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="end"><%= JSPUtil.printX(t[2], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="end"><%= JSPUtil.printX(t[3], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm" text-align="end"><%= JSPUtil.printX(t[4], 0)%></fo:block></fo:table-cell>
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

        </fo:flow>
    </fo:page-sequence>
</fo:root>
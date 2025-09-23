<%@ page import="com.webfin.insurance.model.*,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.form.ProductionMarketingReportForm,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>

<%
            final DTOList l = (DTOList) request.getAttribute("RPT");

            final ProductionMarketingReportForm form = (ProductionMarketingReportForm) SessionManager.getInstance().getCurrentForm();
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="21cm"
                               page-width="30cm"
                               margin-top="0.5cm"
                               margin-bottom="0.5cm"
                               margin-left="2cm"
                               margin-right="2cm">
            <fo:region-body margin-top="2cm" margin-bottom="1cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <fo:flow flow-name="xsl-region-body">

            <fo:block text-align="center"
                      font-size="18pt"
                      font-family="TAHOMA"
                      line-height="12pt"
                      font-style="bold" space-after.optimum="50pt"><fo:inline text-decoration="underline">DATA YANG BELUM MENDAPAT PERSETUJUAN</fo:inline></fo:block>

            <fo:block font-size="12pt" text-align="start" space-after.optimum="20pt"><%=JSPUtil.printX(form.getStBranchDesc())%>, <%=DateUtil.getDateStr(new Date(), "d ^^ yyyy")%></fo:block>

            <fo:block font-size="12pt" text-align="start" space-after.optimum="10pt">Kepada Yth.</fo:block>

            <fo:block font-size="12pt" text-align="start">Bp. Direktur Tehnik</fo:block>
            <fo:block font-size="12pt" text-align="start">Kadiv. Underwriting</fo:block>
            <fo:block font-size="12pt" text-align="start" space-after.optimum="20pt">Kadiv. Reasuransi</fo:block>

            <fo:block font-size="12pt" text-align="justify" space-after.optimum="15pt">
                Bersama ini kami sampaikan, bahwa polis-polis dibawah ini masih <fo:inline text-decoration="underline">belum mendapatkan persetujuan dari Divisi Underwriting atau Reasuransi Kantor Pusat</fo:inline>.
            </fo:block>

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-size="10pt" line-height="12pt" text-align="center" space-after.optimum="5pt">
                                    VALIDASI APPROVAL
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-size="10pt" line-height="12pt" text-align="center" space-after.optimum="5pt">
                                    <% if (form.getPolicyDateFrom() != null || form.getPolicyDateTo() != null) {%>
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> s/d <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block font-size="10pt"><% if (form.getStBranch() != null) {%>
                                    Cabang : <%= JSPUtil.printX(form.getStBranchDesc())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block font-size="10pt" text-align="end">
                                    (dalam rupiah)
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">No </fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Tgl. Polis}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Nomor Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Nama Tertanggung</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Objek</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Eff Flag</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">RI Flag</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">TSI</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">Premi</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                    BigDecimal[] t = new BigDecimal[2];

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());

                                        String custName = "";
                                        if (pol.getStCustomerName().length() > 27) {
                                            custName = pol.getStCustomerName().substring(0, 27);
                                        } else {
                                            custName = pol.getStCustomerName().substring(0, pol.getStCustomerName().length());
                                        }

                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(String.valueOf(i + 1))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="start" line-height="5mm"><%=JSPUtil.printX(custName)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="start" line-height="5mm"><%=JSPUtil.xmlEscape(pol.getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getStEffectiveFlag())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getStRIFinishFlag())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 0)%><fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbPremiTotal(), 0)%><fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[0], 0)%><fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[1], 0)%><fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="12pt" text-align="justify">
                Mohon dapat segera diselesaikan.
            </fo:block>

            <fo:block font-size="12pt" text-align="justify" space-after.optimum="30pt">
                Atas perhatian dan kerjasamanya diucapkan banyak terima kasih.
            </fo:block>


            <fo:block font-size="12pt" text-align="center">
                Mengetahui :
            </fo:block>

            <fo:block font-size="12pt" text-align="center">
                PT. ASURANSI BANGUN ASKRIDA
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
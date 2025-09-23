<%@ page import="com.webfin.ar.model.*,
         com.webfin.ar.forms.FRRPTrptArAPDetailForm,
         com.webfin.insurance.model.InsurancePolicyInwardView,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.util.Tools,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final DTOList l = (DTOList) request.getAttribute("RPT");

            final FRRPTrptArAPDetailForm form = (FRRPTrptArAPDetailForm) SessionManager.getInstance().getCurrentForm();

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="first"
                               page-height="21.5cm"
                               page-width="42cm"
                               margin-top="1.5cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="first">

        <!-- usage of page layout -->
        <fo:flow flow-name="xsl-region-body">

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="20mm"/><!-- Entry Date -->
                    <fo:table-column column-width="10mm"/><!-- Entry Date -->
                    <fo:table-column column-width="20mm"/><!-- Policy Date-->
                    <fo:table-column column-width="40mm"/><!-- Policy No -->
                    <fo:table-column column-width="10mm"/><!-- Policy Date-->
                    <fo:table-column column-width="30mm"/><!-- The Insured -->
                    <fo:table-column column-width="60mm"/><!-- Premium -->
                    <fo:table-column column-width="10mm"/><!-- Biaya Polis -->
                    <fo:table-column column-width="10mm"/><!-- Biaya Materai -->
                    <fo:table-column column-width="30mm"/><!-- Biaya Polis -->
                    <fo:table-column column-width="20mm"/><!-- Biaya Polis -->
                    <fo:table-column column-width="30mm"/><!-- Discount-->
                    <fo:table-column column-width="30mm"/><!-- Discount-->
                    <fo:table-column column-width="30mm"/><!-- Discount-->
                    <fo:table-column column-width="30mm"/><!-- Discount-->
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block font-weight="bold">
                                    REKAPITULASI KLAIM INWARD CASH LOSS
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block  font-weight="bold">
                                    <% if (form.getDLADateFrom() != null || form.getDLADateTo() != null) {%>
                                    Tgl Mutasi Slip : <%=JSPUtil.printX(form.getDLADateFrom())%> S/D <%=JSPUtil.printX(form.getDLADateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block  font-weight="bold">
                                    <% if (form.getStPolicyTypeID() != null) {%>
                                    JENIS PENUTUPAN : <%=JSPUtil.printX(form.getStPolicyTypeName().toUpperCase())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block border-width="0.25pt" border-style="solid" line-height="0.25pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">No</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Date</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">U/Y</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">COB</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">DLA</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Treaty</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Asuradur</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Tertanggung</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Curr.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Kurs</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">TSI</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">D.O.L</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Nilai Klaim 100% R/I</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Bagian Askrida</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">MinDep</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Premi Reinstatement</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>

                    <fo:table-body>

                        <%

                                    BigDecimal[] t = new BigDecimal[4];
                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyInwardView invoic = (InsurancePolicyInwardView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], BDUtil.mul(invoic.getDbAttrPolicyTSITotal(),invoic.getDbCurrencyRate()));
                                        t[n] = BDUtil.add(t[n++], BDUtil.mul(invoic.getDbAttrPolicyTSI(),invoic.getDbCurrencyRate()));
                                        t[n] = BDUtil.add(t[n++], BDUtil.mul(invoic.getDbClaimAmountTotal(),invoic.getDbCurrencyRate()));
                                        t[n] = BDUtil.add(t[n++], BDUtil.mul(invoic.getDbEnteredAmount(),invoic.getDbCurrencyRate()));

                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>    <!-- No --><!-- No -->
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getDtDueDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=DateUtil.getYear(invoic.getDtAttrPolicyPeriodStart())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getPolicyType().getStShortDescription().toUpperCase())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStDLANo())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStRefID0())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getEntity().getStShortName().toUpperCase())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(invoic.getStAttrPolicyName())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getDbCurrencyRate())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.mul(invoic.getDbAttrPolicyTSITotal(),invoic.getDbCurrencyRate()), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getDtReference2())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.mul(invoic.getDbClaimAmountTotal(),invoic.getDbCurrencyRate()), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.mul(invoic.getDbEnteredAmount(),invoic.getDbCurrencyRate()), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.mul(invoic.getDbAttrPolicyTSI(),invoic.getDbCurrencyRate()), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.zero, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%

                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[0], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[2], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[3], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[1], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.zero, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block border-width="0.25pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
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

        </fo:flow>
    </fo:page-sequence>
</fo:root>   
<%@ page import="com.webfin.insurance.model.*, 
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.form.ProductionReportForm,
				 com.webfin.entity.model.EntityView,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final DTOList l = (DTOList) request.getAttribute("RPT");

            final ProductionReportForm form = (ProductionReportForm) SessionManager.getInstance().getCurrentForm();

            BigDecimal accident = new BigDecimal(0);
            BigDecimal phk = new BigDecimal(0);
            BigDecimal total1 = new BigDecimal(0);
            BigDecimal total2 = new BigDecimal(0);
            //BigDecimal rate1 = new BigDecimal(75);
            //BigDecimal rate2 = new BigDecimal(45);

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="29.5cm"
                               page-width="21cm"
                               margin-top="3cm"
                               margin-bottom="0.5cm"
                               margin-left="3cm"
                               margin-right="3cm">

            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <!-- usage of page layout -->
        <!-- header -->


        <fo:flow flow-name="xsl-region-body">

            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block font-weight="bold">Kepada YTH,</fo:block>
                                <fo:block font-weight="bold">REASURANSI INTERNASIONAL INDONESIA</fo:block>
                                <fo:block font-weight="bold">Jl. Salemba Raya No. 30</fo:block>
                                <fo:block font-weight="bold">Jakarta Pusat 10430</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>

            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/><!-- No -->
                    <fo:table-column column-width="5mm"/><!-- Entry Date -->
                    <fo:table-column column-width="40mm"/><!-- Entry Date -->
                    <fo:table-column column-width="15mm"/><!-- Police Date -->
                    <fo:table-column column-width="5mm"/><!-- Entry Date -->
                    <fo:table-column column-width="35mm"/><!-- Policy No. -->
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="20pt" text-align="center">
                                    AUTOMATIC FACULTATIVE
                                </fo:block>
                                <fo:block font-family="tahoma" font-weight="bold" font-size="20pt" text-align="center">
                                    PA KREASI + PHK
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6"><fo:block space-before.optimum="10pt" space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block font-weight="bold">
                                    <% if (form.getStNoUrut() != null) {%>
                                    Note Claim : <%=JSPUtil.printX(form.getStNoUrut())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block font-weight="bold">
                                    <% if (form.getPolicyDateFrom() != null || form.getPolicyDateTo() != null) {%>
                                    Month : <%=DateUtil.getMonth(form.getPolicyDateFrom())%> - <%=DateUtil.getYear2Digit(form.getPolicyDateFrom())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center">{L-ENG TYPE -L}{L-INA TYPE -L}</fo:block></fo:table-cell><!-- No -->
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end">{L-ENG ORIGINAL NET CLAIM -L}{L-INA ORIGINAL NET CLAIM -L}</fo:block></fo:table-cell><!-- Entry Date -->
                            <fo:table-cell><fo:block text-align="center">{L-ENG % -L}{L-INA % -L}</fo:block></fo:table-cell><!-- Police Date -->
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end">{L-ENG NET CLAIM -L}{L-INA NET CLAIM -L}</fo:block></fo:table-cell><!-- Policy No. -->
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header> 

                    <fo:table-body>

                        <%
                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        //rate1 = BDUtil.getRateFromPct(rate1);
                                        //rate2 = BDUtil.getRateFromPct(rate2);

                                        if (pol.getStClaimCauseID().equalsIgnoreCase("1576")) {
                                            accident = BDUtil.add(accident, pol.getDbClaimAmount());
                                            total1 = BDUtil.mul(accident, new BigDecimal(0.075));
                                        } else {
                                            phk = BDUtil.add(phk, pol.getDbClaimAmount());
                                            total2 = BDUtil.mul(phk, new BigDecimal(0.45));
                                        }
                        %>

                        <% }%>

                        <fo:table-row>
                            <fo:table-cell><fo:block>PA KREASI (Accident)</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Rp. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(accident, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">7.5%</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Rp. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(total1, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>PA KREASI (PHK)</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Rp. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(phk, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">45%</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Rp. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(total2, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block space-before.optimum="30pt" space-after.optimum="30pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">TOTAL KLAIM </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >Rp. </fo:block></fo:table-cell><!-- Name of Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(accident, phk), 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell><!-- Claim Estimated -->
                            <fo:table-cell ><fo:block >Rp. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(total1, total2), 2)%></fo:block></fo:table-cell><!-- Claim Approved -->
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
            </fo:block>

            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(new Date(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">Bagian Klaim</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>


            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                        <svg xmlns="http://www.w3.org/2000/svg" width="5cm" height="1cm" xml:space="preserve">
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
        </fo:flow>

    </fo:page-sequence>
</fo:root>   

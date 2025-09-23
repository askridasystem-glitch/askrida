<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.math.BigDecimal,
         com.crux.common.parameter.Parameter,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String attached = (String) request.getAttribute("attached");
            boolean tanpaTanggal = attached.equalsIgnoreCase("3") ? false : true;

            //if (true) throw new NullPointerException();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="21cm"
                               page-width="33cm"
                               margin-top="1cm"
                               margin-bottom="0.5cm"
                               margin-left="1cm"
                               margin-right="1cm">
            <fo:region-body margin-top="0.5cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="2cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <!-- usage of page layout -->
        <!-- header -->
        <fo:static-content flow-name="xsl-region-before">
            <fo:block text-align="end"
                      font-size="6pt"
                      font-family="serif"
                      line-height="12pt" >
                PT. Asuransi Bangun Askrida
            </fo:block>

            <fo:block-container height="1cm" width="1cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="1cm" width="1cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="term"
                                        retrieve-boundary="page"
                                        retrieve-position="last-ending-within-page"/>
                </fo:block>
            </fo:block-container>
        </fo:static-content>

        <fo:static-content flow-name="xsl-region-after">
            <fo:block
                font-size="6pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">

            <!-- defines text title level 1-->

            <fo:block font-size="14pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                {L-ENG List of Attachement -L}{L-INA Lampiran Polis Asuransi -L} <%=pol.getStPolicyTypeDesc2()%> {L-ENG Insurance -L}{L-INA -L}
            </fo:block>

            <!-- Normal text -->

            <!-- defines text title level 1-->

            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                {L-ENG On Behalf of : -L}{L-INA Atas Nama : -L}<%=pol.getStCustomerName()%>
            </fo:block>

            <!-- Normal text -->

            <!-- defines text title level 1-->

            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="5pt" space-after.optimum="20pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                {L-ENG Policy No. : -L}{L-INA Nomor Polis : -L}<%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block>


            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-after.optimum="3pt"></fo:block>

            <!-- Normal text -->

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="65mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG NO. -L}{L-INA NO. -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG CODE -L}{L-INA KODE -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG BRANCH NAME -L}{L-INA NAMA CABANG/CAPEM -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG DEBIT STRATA 1-L}{L-INA STRATA SALDO 1-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG DEBIT STRATA 2-L}{L-INA STRATA SALDO 2-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG DEBIT STRATA 3-L}{L-INA STRATA SALDO 3-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG DEBIT STRATA 4-L}{L-INA STRATA SALDO 4-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG DEBIT STRATA 5-L}{L-INA STRATA SALDO 5-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG DEBIT STRATA 6-L}{L-INA STRATA SALDO 6-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG DEBIT STRATA 7-L}{L-INA STRATA SALDO 7-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG RATE (%o) -L}{L-INA RATE (%o) -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG PREMIUM -L}{L-INA PREMI -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <% 	BigDecimal[] t = new BigDecimal[8];

                                    BigDecimal[] r = new BigDecimal[1];

                                    DTOList objects = pol.getObjects();

                                    for (int i = 0; i < objects.size(); i++) {
                                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], obj.getDbReference1());
                                        t[n] = BDUtil.add(t[n++], obj.getDbReference2());
                                        t[n] = BDUtil.add(t[n++], obj.getDbReference3());
                                        t[n] = BDUtil.add(t[n++], obj.getDbReference4());
                                        t[n] = BDUtil.add(t[n++], obj.getDbReference5());
                                        t[n] = BDUtil.add(t[n++], obj.getDbReference6());
                                        t[n] = BDUtil.add(t[n++], obj.getDbReference7());

                        %>

                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(String.valueOf(i + 1))%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block ><%=JSPUtil.printX(obj.getStReference2())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbReference1(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbReference2(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbReference3(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbReference4(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbReference5(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbReference6(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbReference7(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="15mm"/>
                                            <fo:table-column column-width="30mm"/>
                                            <fo:table-body>

                                                <% DTOList cover = obj.getCoverage();
                                                       for (int j = 0; j < cover.size(); j++) {
                                                           InsurancePolicyCoverView cov = (InsurancePolicyCoverView) cover.get(j);

                                                           int m = 0;
                                                           r[m] = BDUtil.add(r[m++], cov.getDbPremi());
                                                %>

                                                <fo:table-row>
                                                    <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(cov.getDbRate(), 3)%></fo:block></fo:table-cell>
                                                    <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(cov.getDbPremi(), 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>

                                                <% }%>

                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                                </fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="end" line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="end" line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="center" line-height="5mm">JUMLAH</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[0], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[1], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[2], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[3], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[4], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[5], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[6], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="end" line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(r[0], 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3">
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if (tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% } else {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    <%= JSPUtil.printX(pol.getCostCenter(pol.getCostCenter(pol.getStCostCenterCode()).getStSubCostCenterCode()).getStDescription())%><%if (tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% }%>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                        <svg xmlns="http://www.w3.org/2000/svg" width="1cm" height="1cm" xml:space="preserve">
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>

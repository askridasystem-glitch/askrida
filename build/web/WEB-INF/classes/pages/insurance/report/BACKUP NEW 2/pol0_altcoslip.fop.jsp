<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.webfin.ar.model.ARTaxView,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         com.webfin.entity.model.EntityView,
         java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            boolean effective = pol.isEffective();

            BigDecimal PremiNett = new BigDecimal(0);
            BigDecimal instPremiNett = new BigDecimal(0);
            BigDecimal instCoins = new BigDecimal(pol.getStInstallmentPeriods());

            BigDecimal CommMember = new BigDecimal(0);
            BigDecimal BfeeMember = new BigDecimal(0);
            BigDecimal HfeeMember = new BigDecimal(0);

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="first"
                               page-height="14cm"
                               page-width="21.5cm"
                               margin-top="2.5cm"
                               margin-bottom="0.5cm"
                               margin-left="1cm"
                               margin-right="1cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="first" initial-page-number="1">

        <!-- usage of page layout -->
        <!-- HEADER -->

        <fo:static-content flow-name="xsl-region-before">
            <fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="term"
                                        retrieve-boundary="page"
                                        retrieve-position="last-ending-within-page"/>
                </fo:block>
            </fo:block-container>
        </fo:static-content>

        <fo:static-content flow-name="xsl-region-after">
            <fo:block text-align="end"
                      font-size="6pt"
                      font-family="TAHOMA"
                      line-height="12pt" >
                COSLIP - PT. Asuransi Bangun Askrida
            </fo:block>

            <fo:block text-align="end"
                      font-size="6pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">


            <%          int pn = 0;
                        String tax = "";
                        BigDecimal tax_amount = new BigDecimal(0);

                        final DTOList det = pol.getDetails();
                        for (int i = 0; i < det.size(); i++) {
                            InsurancePolicyItemsView dt = (InsurancePolicyItemsView) det.get(i);

                            final ARTaxView tx = dt.getTax();
                            if (tx == null) {
                                continue;
                            }

                            tax = tx.getStDescription();
                            tax_amount = dt.getDbTaxRate();

                            /*
                            if (tx == null) {
                            tax = "";
                            tax_amount = BDUtil.zero;
                            } else if (tx != null) {
                            tax = tx.getStDescription();
                            tax_amount = dt.getDbTaxRate();
                            }
                             */

                            //System.out.println("@@@@@@@@@@@@@@@@@@@@ : " + tax_amount);
                            //System.out.println("#################### : " + dt.getStTaxCode());
                        }

                        final DTOList objects = pol.getObjects();

                        for (int i = 0; i < objects.size(); i++) {
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                            DTOList coinz = pol.getCoins();

                            for (int j = 0; j < coinz.size(); j++) {
                                InsurancePolicyCoinsView ci = (InsurancePolicyCoinsView) coinz.get(j);

                                if (ci.isHoldingCompany()) {
                                    continue;
                                }

                                EntityView reasuradur = ci.getEntity();

                                String ciSlipNo = pol.getStPolicyNo() + j;

                                //BigDecimal premiRate = BDUtil.div(ci.getDbPremiAmount(), ci.getDbAmount(), 10);

                                PremiNett = BDUtil.sub(ci.getDbPremiAmount(), ci.getDbCommissionAmount());
                                PremiNett = BDUtil.sub(PremiNett, ci.getDbDiscountAmount());
                                PremiNett = BDUtil.sub(PremiNett, ci.getDbHandlingFeeAmount());
                                PremiNett = BDUtil.sub(PremiNett, ci.getDbBrokerageAmount());
                                instPremiNett = BDUtil.div(PremiNett, instCoins);

                                CommMember = BDUtil.mul(ci.getDbCommissionAmount(), BDUtil.getRateFromPct(tax_amount));
                                BfeeMember = BDUtil.mul(ci.getDbBrokerageAmount(), BDUtil.getRateFromPct(tax_amount));
                                HfeeMember = BDUtil.mul(ci.getDbHandlingFeeAmount(), BDUtil.getRateFromPct(tax_amount));

                                pn++;

            %>
            <%if (pn > 1) {%>
            <fo:block break-after="page"></fo:block>
            <% }%>

            <% if (!effective) {%>
            <fo:block font-size="14pt"
                      line-height="16pt" space-after.optimum="10pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="136mm"/>

                    <fo:table-column />
                    <fo:table-body>


                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG TO-L}{L-INA KEPADA-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= JSPUtil.xmlEscape(reasuradur.getStEntityName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG ADDRESS-L}{L-INA ALAMAT-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"><%= JSPUtil.xmlEscape(reasuradur.getStAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="108mm"/>
                    <fo:table-column />
                    <fo:table-body>


                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Address-L}{L-INA Alamat-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>


                        <%
                                                        final FlexFieldHeaderView objectMap = pol.getPolicyType().getObjectMap();

                                                        final DTOList objectMapDetails = objectMap == null ? null : objectMap.getDetails();

                                                        final InsurancePolicyObjDefaultView io = obj;

                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy") + " {L-ENG Up To-L}{L-INA Sampai-L} ") + DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Total Sum Insured-L}{L-INA Jumlah Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="64mm"/>


                                        <fo:table-column />
                                        <fo:table-body>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Your Share-L}{L-INA Saham Saudara-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="64mm"/>
                                        <fo:table-column />
                                        <fo:table-body>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(ci.getDbAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Conditions-L}{L-INA Kondisi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table>
                                        <fo:table-column column-width="25mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="25mm"/>
                                        <fo:table-body>

                                            <%
                                                                            final DTOList covers = obj.getCoverage();

                                                                            for (int m = 0; m < covers.size(); m++) {
                                                                                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covers.get(m);
                                            %>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block ><%=cov.getStInsuranceCoverDesc()%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printPct(cov.getDbRatePct(), 4)%> %</fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <%
                                            %>
                                            <% }%>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <%--<%if (!BDUtil.isZero(premiRate)) {%>
                                   <fo:table-row>
                                     <fo:table-cell ><fo:block >{L-ENG Rate of Premium-L}{L-INA Rate Premi-L}</fo:block></fo:table-cell>
                                     <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                     <fo:table-cell ><fo:block><%=JSPUtil.printPct(premiRate,3)%>%</fo:block></fo:table-cell>
                                   </fo:table-row>
                        <% } %>--%>

                        <%if (!BDUtil.isZero(ci.getDbHandlingFeeRate())) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENGHandling Fee-L}{L-INA Handling Fee-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.print(ci.getDbHandlingFeeRate(), 2)%>%</fo:block></fo:table-cell>
                        </fo:table-row>
                        <%}%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!-- defines text title level 1-->

            <fo:block font-size="14pt" font-family="TAHOMA" line-height="16pt" color="black" text-align="center">
                {L-ENG DETAILS OF CO-INSURANCE NOTE-L}{L-INA PERINCIAN NOTA KO-ASURANSI-L}
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="153mm"/>
                    <fo:table-column />
                    <fo:table-body>


                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="1" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="30mm"/>


                                        <fo:table-column />
                                        <fo:table-body>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Premium-L}{L-INA Premi-L}</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ci.getDbPremiAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <%if (!BDUtil.isZeroOrNull(ci.getDbDiscountAmount())) {%>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Disc Fee-L}{L-INA Diskon Fee-L} <%=JSPUtil.print(ci.getDbDiscountRate(), 2)%>%</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ci.getDbDiscountAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% }%>

                                            <%if (!BDUtil.isZeroOrNull(ci.getDbCommissionAmount())) {%>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Comm Fee-L}{L-INA Komisi Fee-L} <%=JSPUtil.printX(ci.getDbCommissionRate(), 2)%>%</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(BDUtil.sub(ci.getDbCommissionAmount(), CommMember), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Tax Fee-L}{L-INA Pajak-L} <%=JSPUtil.print(tax)%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(CommMember, 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% }%>

                                            <%if (!BDUtil.isZeroOrNull(ci.getDbBrokerageAmount())) {%>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Brokerage Fee-L}{L-INA Brokerage Fee-L} <%=JSPUtil.printX(ci.getDbBrokerageRate(), 2)%>%</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(BDUtil.sub(ci.getDbBrokerageAmount(), BfeeMember), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Tax Fee-L}{L-INA Pajak-L} <%=JSPUtil.print(tax)%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(BfeeMember, 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% }%>

                                            <%if (!BDUtil.isZeroOrNull(ci.getDbHandlingFeeAmount())) {%>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Handling Fee-L}{L-INA Handling Fee-L} <%=JSPUtil.print(ci.getDbHandlingFeeRate(), 2)%>%</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(BDUtil.sub(ci.getDbHandlingFeeAmount(), HfeeMember), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Tax Fee-L}{L-INA Pajak-L} <%=JSPUtil.print(tax)%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(HfeeMember, 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% }%>

                                            <fo:table-row>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell number-columns-spanned="2" >
                                                    <fo:block border-width="0.50pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="0.75pt"></fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Nett Premium-L}{L-INA Premi Netto-L}</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>

                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(PremiNett, 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell number-columns-spanned="2" >
                                                    <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="0.75pt"></fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>


                                        </fo:table-body>
                                    </fo:table>

                                </fo:block></fo:table-cell>
                        </fo:table-row>


                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>

            <%
                                            DTOList installment = pol.getInstallment();

                                            if (installment.size() > 1) {

            %>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <!-- INTEREST START -->

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Tanggal</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Jumlah</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="4"><fo:block font-size="<%=fontsize%>" space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%

                                                                        for (int k = 0; k < installment.size(); k++) {
                                                                            InsurancePolicyInstallmentView ins = (InsurancePolicyInstallmentView) installment.get(k);

                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Installment <%=(k + 1)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(ins.getDtDueDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(instPremiNett, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% }%>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="150mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>

                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>


            <%
                            }

                        }

            %>



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




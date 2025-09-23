<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.math.BigDecimal,
         com.crux.util.fop.FOPUtil,
         com.crux.lang.LanguageManager,
         com.crux.common.parameter.Parameter,
         com.webfin.entity.model.EntityView"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");

            boolean effective = pol.isEffective();
            String askridaLogoPath = Parameter.readString("DIGITAL_POLIS_LOGO_PIC");


%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="33cm"
                               page-width="21cm"
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
    <fo:page-sequence master-reference="only" initial-page-number="1">

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
                      line-height="12pt"
                      font-style="bold">
                FACRE - PT. Asuransi Bangun Askrida
            </fo:block>
            <fo:block text-align="end"
                      font-size="6pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>

        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">

            <fo:block text-align = "center">
                <fo:external-graphic
                    content-height="scale-to-fit"
                    height="1.00in" content-width="1.00in"
                    scaling="non-uniform" src="url(file:///<%=askridaLogoPath%>)"  />
            </fo:block>

            <%
                        int pn = 0;

                        final DTOList objects = pol.getObjects();

                        for (int i = 0; i < objects.size(); i++) {
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                            final DTOList treatyDetails = obj.getTreatyDetails();

                            for (int j = 0; j < treatyDetails.size(); j++) {
                                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                                final boolean nonProportional = trd.getTreatyDetail().getTreatyType().isNonProportional();

                                if (!nonProportional) {
                                    continue;
                                }

                                final DTOList shares = trd.getShares();

                                for (int k = 0; k < shares.size(); k++) {
                                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                                    final EntityView reasuradur = ri.getEntity();

                                    pn++;

            %>
            <%if (pn > 1) {%>
            <fo:block break-after="page"></fo:block>
            <% }%>

            <!-- defines text title level 1-->
            <% if (!effective) {%>
            <fo:block font-size="14pt"
                      line-height="16pt" space-after.optimum="10pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block>
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-left-style="solid" padding="1pt" number-columns-spanned="4">
                                <fo:block text-align="center" font-size="9pt"><fo:inline text-decoration="underline">PRELIMINARY LOSS ADVICE</fo:inline></fo:block>
                                <fo:block text-align="center" font-size="9pt">LAPORAN KERUGIAN SEMENTARA</fo:block>
                            </fo:table-cell>
                            <fo:table-cell border-left-style="solid" border-right-style="solid" padding="1pt" number-rows-spanned="2" display-align="center"><fo:block text-align="center" font-size="12pt">To : <%= reasuradur.getStEntityName().toUpperCase()%></fo:block></fo:table-cell><!-- Entry Date -->
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-left-style="solid" padding="1pt"></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="start" font-size="9pt">Bussiness</fo:block>
                                <fo:block text-align="start" font-size="9pt">Number</fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center" font-size="9pt">:</fo:block>
                                <fo:block text-align="center" font-size="9pt">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell border-right-style="solid" padding="1pt">
                                <fo:block text-align="start" font-size="9pt"><%=pol.getStPolicyTypeDesc2().toUpperCase()%></fo:block>
                                <fo:block text-align="start" font-size="9pt"><%=JSPUtil.printX(pol.getStPLANo())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="45mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">1.</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline">Insured</fo:inline></fo:block>
                                <fo:block >Tertanggung</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">2.</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline">Policy Number</fo:inline></fo:block>
                                <fo:block >Nomor Polis</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">3.</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline">Insurance Periode</fo:inline></fo:block>
                                <fo:block >Periode Asuransi</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><%=JSPUtil.printX(DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy") + " {L-ENG Up To-L}{L-INA Sampai-L} ") + DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">4.</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline">Insured Object</fo:inline></fo:block>
                                <fo:block >Obyek Pertanggungan</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><%=JSPUtil.printX(obj.getStReference1())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">5.</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline">Reinsurance Type</fo:inline></fo:block>
                                <fo:block >Jenis Reasuransi</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block >FACULTATIVE</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">6.</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline">Sum Insured</fo:inline></fo:block>
                                <fo:block >Harga Pertanggungan</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="100mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column />
                                            <fo:table-body>
                                                <%
                                                                                    final DTOList suminsureds = obj.getSuminsureds();

                                                                                    for (int z = 0; z < suminsureds.size(); z++) {
                                                                                        InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(z);
                                                %>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(tsi.getStDescriptionAuto())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(tsi.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>


                                                <%

                                                                                    }
                                                %>
                                                <fo:table-row>
                                                    <fo:table-cell ></fo:table-cell>
                                                    <fo:table-cell number-columns-spanned="2" >
                                                        <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>


                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block>Jumlah</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbObjectInsuredAmount(), 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>

                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>

                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">7.</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline">Allocation Risk</fo:inline></fo:block>
                                <fo:block >Alokasi Risiko</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="100mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column />
                                            <fo:table-body>
                                                <%
                                                                                    BigDecimal insured = new BigDecimal(0);
                                                                                    DTOList treaty = obj.getTreatyDetails();

                                                                                    for (int x = 0; x < treaty.size(); x++) {
                                                                                        InsurancePolicyTreatyDetailView trdx = (InsurancePolicyTreatyDetailView) treaty.get(x);

                                                                                        insured = BDUtil.add(insured, trdx.getDbTSIAmount());

                                                %>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(trdx.getStTreatyClassDesc())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(trdx.getDbTSIAmount(), 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>
                                                <%

                                                                                    }
                                                %>
                                                <fo:table-row>
                                                    <fo:table-cell ></fo:table-cell>
                                                    <fo:table-cell ></fo:table-cell>
                                                    <fo:table-cell number-columns-spanned="2" >
                                                        <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>


                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block>TOTAL</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(insured, 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">8.</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline">Date and Location of Loss</fo:inline></fo:block>
                                <fo:block >Tanggal dan Lokasi Kejadian</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><%=JSPUtil.printX(DateUtil.getDateStr(pol.getDtClaimDate(), "d ^^ yyyy"))%></fo:block>
                                <fo:block ><%=JSPUtil.printX(pol.getStClaimEventLocation())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <% DTOList cause = pol.getClaimCause();
                                                            for (int v = 0; v < cause.size(); v++) {
                                                                InsuranceClaimCauseView cau = (InsuranceClaimCauseView) cause.get(v);
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">9.</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline">Cause of Loss</fo:inline></fo:block>
                                <fo:block >Sebab Kerugian</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><%=JSPUtil.printX(cau.getStDescription())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">10.</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline">Estimated Claim Amount (100%)</fo:inline></fo:block>
                                <fo:block >Jumlah Kerugian Sementara (100%)</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >                                    
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="100mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column />
                                            <fo:table-body>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getClaimLossDesc())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmountEstimate(), 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>

                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">11.</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline">Allocation of Claim</fo:inline></fo:block>
                                <fo:block >Alokasi Klaim</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="100mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column />
                                            <fo:table-body>
                                                <%
                                                                                    //final DTOList deductibles = io.getDeductibles();


                                                                                    BigDecimal claimri = new BigDecimal(0);
                                                                                    for (int q = 0; q < treaty.size(); q++) {
                                                                                        InsurancePolicyTreatyDetailView trdq = (InsurancePolicyTreatyDetailView) treaty.get(q);

                                                                                        claimri = BDUtil.add(claimri, trdq.getDbClaimAmount());

                                                %>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(trdq.getStTreatyClassDesc())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(trdq.getDbClaimAmount(), 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>
                                                <%

                                                                                    }
                                                %>
                                                <fo:table-row>
                                                    <fo:table-cell ></fo:table-cell>
                                                    <fo:table-cell number-columns-spanned="2" >
                                                        <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block>TOTAL</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(claimri, 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>

                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">12.</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline">Your Share in Facultative (100%)</fo:inline></fo:block>
                                <fo:block >Bagian Saudara Facultative (100%)</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="100mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column />
                                            <fo:table-body>

                                                <fo:table-row>
                                                    <fo:table-cell ></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(ri.getDbClaimAmount(), 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>

                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">13.</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline">Remarks</fo:inline></fo:block>
                                <fo:block >Keterangan</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><%=JSPUtil.printX(ri.getStNotes())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>            

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="150mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center" space-before.optimum="20pt">JAKARTA, <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>

                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="KANTOR_ASKRIDA_<%=pol.getStCostCenterCode()%>_<%=pol.getStPolicyNo()%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>

                                </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>


            <%
                                }
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




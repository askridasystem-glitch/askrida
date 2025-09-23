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
                               page-height="21cm"
                               page-width="31cm"
                               margin-top="0.5cm"
                               margin-bottom="0.5cm"                                                                                                                                                                           
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <!-- usage of page layout -->
        <fo:static-content flow-name="xsl-region-before"> 
            <fo:block-container height="0cm" width="0cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="0cm" width="0cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="term"
                                        retrieve-boundary="page"
                                        retrieve-position="last-ending-within-page"/>
                </fo:block>
            </fo:block-container>

            <fo:block text-align="end"
                      font-size="6pt"
                      font-family="TAHOMA"
                      line-height="12pt" 
                      font-style="bold">
                rppre_premi_reas_jenis - PT. Asuransi Bangun Askrida
            </fo:block>
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">

            <!-- defines text title level 1-->
            <fo:block font-size="7pt" line-height="8pt"></fo:block>

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="25mm"/> 
                    <fo:table-column column-width="25mm"/> 
                    <fo:table-column column-width="40mm"/> 
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="14pt" text-align="center">  
                                    {L-ENG REINSURANCE PREMIUM PER REINSURANCE -L}{L-INA PRODUKSI PREMI REASURANSI PER REASURADUR -L}
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="10pt" text-align="center">
                                    <% if (form.getPolicyDateFrom() != null || form.getPolicyDateTo() != null) {%>
                                    {L-ENG Period -L}{L-INA Tanggal Polis -L} : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13"><fo:block space-after.optimum="20pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block>{L-ENG Branch -L}{L-INA Cabang -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold"> : 
                                    <% if (form.getStBranch() != null) {%>
                                    <%=JSPUtil.printX(form.getStBranchDesc())%>  
                                    <% } else {%>
                                    {L-ENG All Branch -L}{L-INA Seluruh Cabang -L}
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>      

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block>{L-ENG Reinsurance Type -L}{L-INA Treaty -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold"> : 
                                    <% if (form.getStFltTreatyType() != null) {%>
                                    <%=JSPUtil.printX(form.getStFltTreatyType())%>
                                    <% }%> 
                                    <% if (form.getPeriodFrom() != null) {%>
                                    <%=DateUtil.getYear(form.getPeriodFrom())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block>{L-ENG Class of Bussiness -L}{L-INA Jenis Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold"> : 
                                    <% if (form.getStPolicyTypeGroupID() != null) {%>
                                    <%= JSPUtil.printX(form.getStPolicyTypeGroupDesc())%>
                                    <% } else if (form.getStPolicyTypeID() != null) {%>
                                    - <%= JSPUtil.printX(form.getStPolicyTypeDesc())%>
                                    <% } else {%>
                                    {L-ENG All Class Of Bussiness -L}{L-INA Semua Jenis -L}
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block>{L-ENG Reinsurer -L}{L-INA Reasuradur -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold"> :
                                    <% if (form.getStCoinsurerID() != null) {%>
                                    <%=JSPUtil.printX(form.getStCoinsurerName())%>
                                    <% } else {%>
                                    {L-ENG All Reinsurer -L}{L-INA Seluruh Reasuradur -L}
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>   

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13"><fo:block text-align="end" space-before.optimum="10pt">(dalam Rupiah)</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">NO</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">REINSURER</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG COB -L}{L-INA JENIS -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG POLICY -L}{L-INA POLIS -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">SLIP NO</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">KURS</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG CURR -L}{L-INA MATA UANG -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-column-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG PREMIUM -L}{L-INA PREMI -L}</fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell> 
                            <fo:table-cell number-column-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">R/I COMM</fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell> 
                            <fo:table-cell number-column-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG NETT -L}{L-INA NETTO -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-right-style="solid"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">ORG</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">IDR</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">ORG</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">IDR</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">ORG</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"  border-right-style="solid"><fo:block text-align="center" line-height="5mm">IDR</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>
                    <fo:table-body>

                        <%
                                    int pn = 0;
                                    int norut = 0;

                                    BigDecimal subTotalPremi = null;
                                    BigDecimal subTotalKomisi = null;
                                    BigDecimal subTotalPremiIDR = null;
                                    BigDecimal subTotalKomisiIDR = null;
                                    BigDecimal subTotalPremiUSD = null;
                                    BigDecimal subTotalKomisiUSD = null;

                                    BigDecimal TotalPremi = null;
                                    BigDecimal TotalKomisi = null;
                                    BigDecimal TotalPremiIDR = null;
                                    BigDecimal TotalKomisiIDR = null;
                                    BigDecimal TotalPremiUSD = null;
                                    BigDecimal TotalKomisiUSD = null;

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        norut++;

                                        TotalPremi = BDUtil.add(TotalPremi, pol.getDbPremiNetto());
                                        TotalKomisi = BDUtil.add(TotalKomisi, pol.getDbNDComm2());

                                        if (pol.getStCurrencyCode().equalsIgnoreCase("IDR")) {
                                            TotalPremiIDR = BDUtil.add(TotalPremiIDR, pol.getDbPremiTotal());
                                            TotalKomisiIDR = BDUtil.add(TotalKomisiIDR, pol.getDbNDComm1());
                                        } else if (!pol.getStCurrencyCode().equalsIgnoreCase("IDR")) {
                                            TotalPremiUSD = BDUtil.add(TotalPremiUSD, pol.getDbPremiTotal());
                                            TotalKomisiUSD = BDUtil.add(TotalKomisiUSD, pol.getDbNDComm1());
                                        }

                                        if (i > 0) {
                                            InsurancePolicyView pol2 = (InsurancePolicyView) l.get(i - 1);
                                            String inward = pol.getStCustomerName();
                                            String inward2 = pol2.getStCustomerName();
                                            if (!inward.equalsIgnoreCase(inward2)) {
                                                pn++;

                                                norut = 1;
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">IDR</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiIDR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremi, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalKomisiIDR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalKomisi, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.sub(subTotalPremiIDR, subTotalKomisiIDR), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.sub(subTotalPremi, subTotalKomisi), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%--<% if (Tools.compare(subTotalPremiUSD, BDUtil.zero) > 0) {%>--%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" border-width="0.5pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">NON IDR</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiUSD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalKomisiUSD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.sub(subTotalPremiUSD, subTotalKomisiUSD), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"></fo:table-cell>
                        </fo:table-row>
                        <%--<% }%>--%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <%--
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        --%>
                        <%
                                                            subTotalPremi = null;
                                                            subTotalKomisi = null;
                                                            subTotalPremiIDR = null;
                                                            subTotalKomisiIDR = null;
                                                            subTotalPremiUSD = null;
                                                            subTotalKomisiUSD = null;

                                                        }
                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(norut)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell> 
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getStReference2())%></fo:block></fo:table-cell> 
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCurrencyRate(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiNetto(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm1(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm2(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(pol.getDbPremiTotal(), pol.getDbNDComm1()), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(pol.getDbPremiNetto(), pol.getDbNDComm2()), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                        subTotalPremi = BDUtil.add(subTotalPremi, pol.getDbPremiNetto());
                                        subTotalKomisi = BDUtil.add(subTotalKomisi, pol.getDbNDComm2());

                                        if (pol.getStCurrencyCode().equalsIgnoreCase("IDR")) {
                                            subTotalPremiIDR = BDUtil.add(subTotalPremiIDR, pol.getDbPremiTotal());
                                            subTotalKomisiIDR = BDUtil.add(subTotalKomisiIDR, pol.getDbNDComm1());
                                        } else if (pol.getStCurrencyCode().equalsIgnoreCase("USD")) {
                                            subTotalPremiUSD = BDUtil.add(subTotalPremiUSD, pol.getDbPremiTotal());
                                            subTotalKomisiUSD = BDUtil.add(subTotalKomisiUSD, pol.getDbNDComm1());
                                        }

                                    }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">IDR</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiIDR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremi, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalKomisiIDR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalKomisi, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.sub(subTotalPremiIDR, subTotalKomisiIDR), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.sub(subTotalPremi, subTotalKomisi), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%--<% if (Tools.compare(subTotalPremiUSD, BDUtil.zero) > 0) {%>--%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" border-width="0.5pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">NON IDR</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiUSD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalKomisiUSD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.sub(subTotalPremiUSD, subTotalKomisiUSD), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"></fo:table-cell>
                        </fo:table-row>
                        <%--<% }%>--%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">IDR</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(TotalPremiIDR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(TotalPremi, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(TotalKomisiIDR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(TotalKomisi, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.sub(TotalPremiIDR, TotalKomisiIDR), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.sub(TotalPremi, TotalKomisi), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%--<% if (Tools.compare(TotalPremiUSD, BDUtil.zero) > 0) {%>--%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" border-width="0.5pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">NON IDR</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(TotalPremiUSD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(TotalKomisiUSD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.sub(TotalPremiUSD, TotalKomisiUSD), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"></fo:table-cell>
                        </fo:table-row>
                        <%--<% }%>--%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
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
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(TotalPremi, 2)%>" orientation="0">
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
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>     

        </fo:flow>
    </fo:page-sequence>
</fo:root>

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

            BigDecimal PremiIDR = null;
            BigDecimal PremiReasIDR = null;
            BigDecimal CommReasIDR = null;

            BigDecimal PremiUSD = null;
            BigDecimal PremiReasUSD = null;
            BigDecimal CommReasUSD = null;

            BigDecimal PremiGBP = null;
            BigDecimal PremiReasGBP = null;
            BigDecimal CommReasGBP = null;

            BigDecimal PremiJPY = null;
            BigDecimal PremiReasJPY = null;
            BigDecimal CommReasJPY = null;

            BigDecimal PremiSGD = null;
            BigDecimal PremiReasSGD = null;
            BigDecimal CommReasSGD = null;

            BigDecimal PremiEUR = null;
            BigDecimal PremiReasEUR = null;
            BigDecimal CommReasEUR = null;
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-width="21cm"
                               page-height="30cm"
                               margin-top="0.5cm"
                               margin-bottom="1cm"
                               margin-left="2cm"
                               margin-right="3cm">
            <fo:region-body margin-top="4cm" margin-bottom="1cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">


        <fo:flow flow-name="xsl-region-body">
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="155mm"/>
                    <fo:table-body>


                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-size="12pt" line-height="12pt" text-align="center" space-after.optimum="5pt">	 
                                    REKAP LAPORAN KHUSUS MAIPARK
                                    <% if (form.getStPolicyTypeDesc() != null) {%>
                                    <%=JSPUtil.printX(form.getStPolicyTypeDesc())%><% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-size="12pt" line-height="12pt" text-align="center" space-after.optimum="5pt">	 
                                    BULAN : <%=DateUtil.getDateStr(form.getPolicyDateFrom(), "^^ yyyy")%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!-- GARIS  -->
            <fo:block space-after.optimum="10pt"></fo:block>

            <!-- Normal text -->


            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center">{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Code -L}{L-INA Kode -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Branch -L}{L-INA Daerah -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Curr-L}{L-INA Kurs -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Premium 100%-L}{L-INA Premi 100% -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Premium -L}{L-INA Premi -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Comm -L}{L-INA Komisi -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Netto R/I -L}{L-INA Netto -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%

                                    BigDecimal[] t = new BigDecimal[4];
                                    BigDecimal netto = new BigDecimal(0);
                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], BDUtil.mul(pol.getDbPremiTotal(), pol.getDbCurrencyRate()));
                                        t[n] = BDUtil.add(t[n++], BDUtil.mul(pol.getDbPremiBPDAN(), pol.getDbCurrencyRate()));
                                        t[n] = BDUtil.add(t[n++], BDUtil.mul(pol.getDbCommBPDAN(), pol.getDbCurrencyRate()));

                                        netto = BDUtil.sub(pol.getDbPremiBPDAN(), pol.getDbCommBPDAN());
                                        t[n] = BDUtil.add(t[n++], BDUtil.mul(netto, pol.getDbCurrencyRate()));

                                        if (pol.getStCurrencyCode().equalsIgnoreCase("IDR")) {
                                            PremiIDR = BDUtil.add(PremiIDR, pol.getDbPremiTotal());
                                            PremiReasIDR = BDUtil.add(PremiReasIDR, pol.getDbPremiBPDAN());
                                            CommReasIDR = BDUtil.add(CommReasIDR, pol.getDbCommBPDAN());
                                        } else if (pol.getStCurrencyCode().equalsIgnoreCase("USD")) {
                                            PremiUSD = BDUtil.add(PremiUSD, pol.getDbPremiTotal());
                                            PremiReasUSD = BDUtil.add(PremiReasUSD, pol.getDbPremiBPDAN());
                                            CommReasUSD = BDUtil.add(CommReasUSD, pol.getDbCommBPDAN());
                                        } else if (pol.getStCurrencyCode().equalsIgnoreCase("SGD")) {
                                            PremiSGD = BDUtil.add(PremiSGD, pol.getDbPremiTotal());
                                            PremiReasSGD = BDUtil.add(PremiReasSGD, pol.getDbPremiBPDAN());
                                            CommReasSGD = BDUtil.add(CommReasSGD, pol.getDbCommBPDAN());
                                        } else if (pol.getStCurrencyCode().equalsIgnoreCase("EUR")) {
                                            PremiEUR = BDUtil.add(PremiEUR, pol.getDbPremiTotal());
                                            PremiReasEUR = BDUtil.add(PremiReasEUR, pol.getDbPremiBPDAN());
                                            CommReasEUR = BDUtil.add(CommReasEUR, pol.getDbCommBPDAN());
                                        } else if (pol.getStCurrencyCode().equalsIgnoreCase("GBP")) {
                                            PremiGBP = BDUtil.add(PremiGBP, pol.getDbPremiTotal());
                                            PremiReasGBP = BDUtil.add(PremiReasGBP, pol.getDbPremiBPDAN());
                                            CommReasGBP = BDUtil.add(CommReasGBP, pol.getDbCommBPDAN());
                                        } else if (pol.getStCurrencyCode().equalsIgnoreCase("JPY")) {
                                            PremiJPY = BDUtil.add(PremiJPY, pol.getDbPremiTotal());
                                            PremiReasJPY = BDUtil.add(PremiReasJPY, pol.getDbPremiBPDAN());
                                            CommReasJPY = BDUtil.add(CommReasJPY, pol.getDbCommBPDAN());
                                        }

                                        //	if (Tools.compare(pol.getDbPremiTotal(), BDUtil.zero)>0) continue;
                        %>
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(String.valueOf(i + 1))%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCostCenterCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block ><%=JSPUtil.printX(pol.getCostCenter(pol.getStCostCenterCode()).getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiBPDAN(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCommBPDAN(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(pol.getDbPremiBPDAN(), pol.getDbCommBPDAN()), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(PremiIDR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(PremiReasIDR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(CommReasIDR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(PremiReasIDR, CommReasIDR), 2)%></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <% if (!Tools.isEqual(PremiReasUSD, BDUtil.zero)) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block text-align="center">USD</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(PremiUSD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(PremiReasUSD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(CommReasUSD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(PremiReasUSD, CommReasUSD), 2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        <% }%>

                        <% if (Tools.compare(PremiReasEUR, BDUtil.zero) > 0) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block text-align="center">EUR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(PremiEUR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(PremiReasEUR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(CommReasEUR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(PremiReasEUR, CommReasEUR), 2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        <% }%>

                        <% if (Tools.compare(PremiReasGBP, BDUtil.zero) > 0) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block text-align="center">GBP</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(PremiGBP, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(PremiReasGBP, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(CommReasGBP, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(PremiReasGBP, CommReasGBP), 2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        <% }%>

                        <% if (Tools.compare(PremiReasSGD, BDUtil.zero) > 0) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block text-align="center">SGD</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(PremiSGD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(PremiReasSGD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(CommReasSGD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(PremiReasSGD, CommReasSGD), 2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        <% }%>

                        <% if (Tools.compare(PremiReasJPY, BDUtil.zero) > 0) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block text-align="center">JPY</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(PremiJPY, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(PremiReasJPY, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(CommReasJPY, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(PremiReasJPY, CommReasJPY), 2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block text-align="center">TOTAL IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[0], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[1], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[2], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[3], 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="10pt"></fo:block>
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
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(PremiIDR, 0)%>" orientation="0">
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

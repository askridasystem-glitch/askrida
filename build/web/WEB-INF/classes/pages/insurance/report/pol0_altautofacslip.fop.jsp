<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.util.Date,
         java.math.BigDecimal,
         com.crux.util.fop.FOPUtil,
         com.crux.lang.LanguageManager,
         com.webfin.entity.model.EntityView"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");

            boolean effective = pol.isEffective();

            BigDecimal CIT = new BigDecimal(0);
            BigDecimal CIS = new BigDecimal(0);
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="33cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="0.5cm"
                               margin-left="1.5cm"
                               margin-right="1.5cm">

            <fo:region-body margin-top="3.5cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="0.5cm"/>
            <fo:region-after extent="1cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <fo:static-content flow-name="xsl-region-after"> 
            <fo:block text-align="start"
                      font-size="6pt"                      
                      line-height="12pt" 
                      font-style="bold">
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%> PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%>
            </fo:block>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">            

            <%
                        int pn = 0;

                        final DTOList reins = pol.getReinsuranceReins();

                        for (int i = 0; i < reins.size(); i++) {
                            InsurancePolicyReinsView rei = (InsurancePolicyReinsView) reins.get(i);

                            final EntityView reasuradur = rei.getEntity();

                            pn++;

                            BigDecimal premi = BDUtil.add(rei.getDbPremiumCover1(), rei.getDbPremiumCover2());
                            BigDecimal comm = BDUtil.add(rei.getDbPremiAmount(), rei.getDbPremiRate());

            %>

            <%if (pn > 1) {%>
            <fo:block break-after="page"></fo:block>
            <% }%>

            <!-- defines text title level 1-->
            <% if (!effective) {%>
            <fo:block font-size="16pt"
                      line-height="16pt" space-after.optimum="3pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block font-size="16pt" line-height="16pt" color="black" text-align="center">
                {L-INA ASURANSI -L}<%=pol.getStPolicyTypeDesc2().toUpperCase()%>{L-ENG INSURANCE -L}
            </fo:block>

            <fo:block font-size="16pt" line-height="16pt" color="black" text-align="center">
                AUTOMATIC FACULTATIVE
            </fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="16pt" space-after.optimum="10pt" color="black" text-align="center"> 
                Nr. <% if (rei.getStRISlipNo() != null) {%> <%=JSPUtil.printX(rei.getStRISlipNo())%> <% }%>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG The Insurer -L}{L-INA Penanggung -L}</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%= pol.getHoldingCompany().getStEntityName()%></fo:block></fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG The Reinsurer -L}{L-INA Reasuradur -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= reasuradur.getStEntityName()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Policy No.-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Type of Insurance -L}{L-INA Jenis Asuransi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Type of Cover -L}{L-INA Type of Cover -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Cash Management Insurance Package + Open Cover (LSW 630)</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Name of Insured -L}{L-INA Nama Tertanggung -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%> as Cash Management for their respective rights and interest</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Coverage -L}{L-INA Jaminan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="30mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column column-width="30mm"/>
                                            <fo:table-body>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block>Basic Coverage</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end">: -</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>Cash In Transit</fo:block></fo:table-cell>
                                                </fo:table-row>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end">-</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>Cash In Premises</fo:block></fo:table-cell>
                                                </fo:table-row>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block>Extended Coverage</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end">: -</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>Fidelity Guarantee</fo:block></fo:table-cell>
                                                </fo:table-row>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end">-</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>Loss of Bank Interest</fo:block></fo:table-cell>
                                                </fo:table-row>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end">-</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>Personal Accident</fo:block></fo:table-cell>
                                                </fo:table-row>

                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>                                
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt" space-after.optimum="10pt">{L-ENG Period -L}{L-INA Jangka Waktu -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-before.optimum="10pt">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"><%=JSPUtil.printX(DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy") + " {L-ENG Up To-L}{L-INA Sampai-L} ") + DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Total Sum Insured -L}{L-INA Jumlah Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-body>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%> </fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Geographical Limit -L}{L-INA Batas Wilayah -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Anywhere in Indonesia -L}{L-INA Di Wilayah Indonesia -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Condition -L}{L-INA Kondisi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG As Original Policy -L}{L-INA Polis Asli -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Rate -L}{L-INA Persen -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column column-width="20mm"/>
                                        <fo:table-column column-width="15mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column column-width="20mm"/>
                                        <fo:table-body>
                                            <%--
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>CIT</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(rei.getDbRateCover1(),5)%> %</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>;</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>CIS</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(rei.getDbRateCover2(),5)%> %</fo:block></fo:table-cell>
                                            </fo:table-row>
                                            --%>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>CIS</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end">0.035 %</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="start">;</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>CIT</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end">0.00825 %</fo:block></fo:table-cell>
                                            </fo:table-row>

                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>R/I Comm : <%=JSPUtil.printX(rei.getDbRICommRate(), 2)%> %</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt" space-after.optimum="10pt">{L-ENG Reinsurances Share -L}{L-INA Saham Reasuradur -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-before.optimum="10pt" space-after.optimum="10pt">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-before.optimum="10pt" space-after.optimum="10pt">
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="25mm"/>
                                        <fo:table-body>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>CIT</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%> </fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(rei.getDbTSICover1(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>CIS</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%> </fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(rei.getDbTSICover2(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Reinsurance Premium -L}{L-INA Premi Reasuransi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="35mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="25mm"/>

                                        <fo:table-column />
                                        <fo:table-body>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Premium-L}{L-INA Premi-L}</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(BDUtil.add(rei.getDbPremiumCover1(), rei.getDbPremiumCover2()), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <% if (Tools.compare(rei.getDbRICommRate(), new BigDecimal(0)) > 0) {%>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Commission-L}{L-INA Komisi-L} <%=JSPUtil.print(rei.getDbRICommRate(), 2)%>%</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(BDUtil.add(rei.getDbPremiAmount(), rei.getDbPremiRate()), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <% } else {%>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Commission-L}{L-INA Komisi-L}</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end">--</fo:block></fo:table-cell>
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

                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(BDUtil.sub(premi, comm), 2)%></fo:block></fo:table-cell>
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

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
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
                                <fo:block text-align="center" space-before.optimum="20pt">JAKARTA, <%=DateUtil.getDateStr(new Date(), "d ^^ yyyy")%></fo:block>
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

            <% }%>

        </fo:flow>
    </fo:page-sequence>
</fo:root>




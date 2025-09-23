<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.util.Date,     
         com.crux.util.fop.FOPUtil,
         com.webfin.entity.model.EntityView"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            boolean effective = pol.isEffective();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="first"
                               page-height="14cm"
                               page-width="21.5cm"
                               margin-top="3cm"
                               margin-bottom="0.5cm"
                               margin-left="1cm"
                               margin-right="1cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="2cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->


    <!-- actual layout -->

    <fo:page-sequence master-reference="first"> 

        <fo:flow flow-name="xsl-region-body">  

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

            <% if (!effective) {%>
            <fo:block font-size="16pt"
                      line-height="16pt" space-after.optimum="10pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                {L-INA SPESIMEN -L}{L-ENG SPECIMEN -L}
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
                            <fo:table-cell ><fo:block>NO.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(ri.getStRISlipNo())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG TO-L}{L-INA KEPADA-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= JSPUtil.xmlEscape(reasuradur.getStEntityName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% if (reasuradur.getStAddress() != null) {%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG ADDRESS-L}{L-INA ALAMAT-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= JSPUtil.xmlEscape(reasuradur.getStAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% } else {%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG ADDRESS-L}{L-INA ALAMAT-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>
                        <%-- 
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="3" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                        --%>
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="3pt"></fo:block>


            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="108mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG R/I Slip No.-L}{L-INA No. Slip R/A -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(ri.getStRISlipNo())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Period-L}{L-INA Awal Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d MMM yyyy")%> {L-ENG up to -L}{L-INA s/d -L} <%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d MMM yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="64mm"/>

                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Sum Insured -L}{L-INA Harga Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell> <fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbObjectInsuredAmount(), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Your Share-L}{L-INA Share Saudara-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell> <fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(ri.getDbTSIAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%-- 
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="5" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                        --%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="3pt"></fo:block>


            <!-- defines text title level 1-->

            <fo:block font-size="16pt" line-height="16pt" color="black" text-align="center">
                {L-ENG DETAILS OF CREDIT NOTE-L}{L-INA PERINCIAN NOTA KREDIT-L}
            </fo:block>

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="3pt"></fo:block>


            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="161mm"/>
                    <fo:table-column />
                    <fo:table-body>


                        <!-- GARIS DALAM KOLOM -->
                        <%-- 
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="1" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="10pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                        --%>

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

                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ri.getDbPremiAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Commission-L}{L-INA Komisi-L} <%=JSPUtil.print(ri.getDbRICommRate(), 2)%>%</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>

                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ri.getDbRICommAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell number-columns-spanned="2" >
                                                    <fo:block border-width="0.50pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="0.75pt"></fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Due To You-L}{L-INA Netto Untuk Saudara-L}</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>

                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ri.getDbPremiNet(), 2)%></fo:block></fo:table-cell>
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

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="150mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center" space-before.optimum="10pt">JAKARTA, <%=DateUtil.getDateStr(new Date(), "d ^^ yyyy")%></fo:block>
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

            <fo:block space-after.optimum="150pt"></fo:block>

            <%
                                }
                            }
                        }

            %>


        </fo:flow>
    </fo:page-sequence>
</fo:root>
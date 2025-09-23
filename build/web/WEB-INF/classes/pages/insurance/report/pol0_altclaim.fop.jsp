<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");

            boolean effective = pol.isEffective();

            //if (true) throw new NullPointerException();

            Calculator calculator = new Calculator();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="29.7cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="2cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">

            <fo:region-body margin-top="1cm" margin-bottom="1.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <!-- usage of page layout -->
        <!-- HEADER -->

        <fo:flow flow-name="xsl-region-body">

            <% if (!effective) {%>
            <fo:block font-size="16pt" font-family="TAHOMA"
                      line-height="16pt" space-after.optimum="10pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="10pt"></fo:block>

            <!-- defines text title level 1-->

            <fo:block font-size="16pt" font-family="TAHOMA" line-height="16pt" color="black" text-align="center">
                {L-INA Klaim Sementara Asuransi-L}<%=pol.getStPolicyTypeDesc2()%>{L-ENG Preliminary Loss Advice-L}
            </fo:block>

            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="16pt" space-after.optimum="10pt" color="black" text-align="center"> NO. <%=pol.getStPLANo()%>
            </fo:block>

            <!-- Normal text -->

            <!-- DYNAMIC HEADER WORDING -->


            <!-- GARIS -->
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>

            <!-- Normal text -->

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStCustomerName()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Address-L}{L-INA Alamat-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStCustomerAddress()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPeriodLength()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Inception Date-L}{L-INA Awal Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Expiry Date-L}{L-INA Akhir Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>DLA No</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStDLANo()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>DLA Remark</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStDLARemark()%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>


            <%
                        final FlexFieldHeaderView polClaimMap = pol.getClaimFF();

                        final DTOList polClaimMapDetails = polClaimMap == null ? null : polClaimMap.getDetails();

                        if (polClaimMapDetails != null) {

            %>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <%


                               for (int j = 0; j < polClaimMapDetails.size(); j++) {
                                   FlexFieldDetailView iomd = (FlexFieldDetailView) polClaimMapDetails.get(j);

                                   final Object desc = iomd.getDesc(pol);

                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(iomd.getStFieldDesc())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                               }
                        %>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% }%>

            <%
                        final DTOList claimItems = pol.getClaimItems();


            %>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <%


                                    for (int i = 0; i < claimItems.size(); i++) {
                                        InsurancePolicyItemsView ci = (InsurancePolicyItemsView) claimItems.get(i);

                                        final boolean negative = ci.getInsItem().getARTrxLine().isNegative();

                                        String amt = JSPUtil.printX(ci.getDbAmount(), 2);

                                        if (negative) {
                                            amt = "(" + amt + ")";
                                        }

                                        calculator.add("totci", ci.getDbAmount());

                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(ci.getStDescription2())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=amt%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                                    }
                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX("TOTAL")%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbClaimAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>





            <fo:block font-size="<%=fontsize%>" space-before.optimum="5pt">
                {L-ENG INTEREST INSURED-L}{L-INA OBJEK PERTANGGUNGAN-L}
            </fo:block>

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="2pt" text-align="justify" > </fo:block>

            <%
                        final DTOList objects = pol.getObjects();

                        for (int i = 0; i < objects.size(); i++) {
                            InsurancePolicyObjectView io = (InsurancePolicyObjectView) objects.get(i);

                            final FlexFieldHeaderView objectMap = io.getClaimFF();

                            final DTOList objectMapDetails = objectMap == null ? null : objectMap.getDetails();

            %>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <%

                               if (objectMapDetails != null)
                                   for (int j = 0; j < objectMapDetails.size(); j++) {
                                       FlexFieldDetailView iomd = (FlexFieldDetailView) objectMapDetails.get(j);

                                       final Object desc = iomd.getDesc(io);

                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(iomd.getStFieldDesc())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                                  }
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="50mm"/>
                                            <fo:table-column column-width="2mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column column-width="30mm"/>
                                            <fo:table-body>
                                                <%
                                                       final DTOList suminsureds = io.getSuminsureds();

                                                       for (int j = 0; j < suminsureds.size(); j++) {
                                                           InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);
                                                %>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(tsi.getStInsuranceTSIDesc())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(tsi.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>
                                                <%

                                                       }
                                                %>

                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Coverage -L}{L-INA Jaminan-L} </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="50mm"/>
                                            <fo:table-column column-width="2mm"/>
                                            <fo:table-column column-width="10mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column column-width="20mm"/>
                                            <fo:table-body>
                                                <%
                                                       final DTOList coverage = io.getCoverage();

                                                       for (int j = 0; j < coverage.size(); j++) {
                                                           InsurancePolicyCoverView cover = (InsurancePolicyCoverView) coverage.get(j);

                                                %>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(cover.getStInsuranceCoverDesc())%></fo:block></fo:table-cell>
                                                </fo:table-row>
                                                <%

                                                       }
                                                %>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>

                                    <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

                                </fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Deductible-L}{L-INA Risiko Sendiri-L} </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="50mm"/>
                                            <fo:table-column column-width="2mm"/>
                                            <fo:table-column />
                                            <fo:table-body>
                                                <%
                                                       final DTOList deductibles = io.getDeductibles();

                                                       for (int j = 0; j < deductibles.size(); j++) {
                                                           InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);

                                                %>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(ded.getStClaimCauseDesc())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(ded.getStAutoDesc())%></fo:block></fo:table-cell>
                                                    <%--<fo:table-cell ><fo:block text-align="center">x</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(cover.getDbPremi(),2)%></fo:block></fo:table-cell>--%>
                                                </fo:table-row>
                                                <%

                                                       }
                                                %>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>TSI Spreading </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="35mm"/>
                                            <fo:table-column column-width="2mm"/>
                                            <fo:table-column />
                                            <fo:table-body>
                                                <%
                                                       //final DTOList deductibles = io.getDeductibles();

                                                       {
                                                           DTOList treatyDetails = io.getTreatyDetails();

                                                           for (int j = 0; j < treatyDetails.size(); j++) {
                                                               InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                                                               calculator.add("TSISPTOT", trd.getDbTSIAmount());

                                                %>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(trd.getStTreatyClassDesc())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(trd.getDbTSIAmount(), 0)%></fo:block></fo:table-cell>
                                                    <%--<fo:table-cell ><fo:block text-align="center">x</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(cover.getDbPremi(),2)%></fo:block></fo:table-cell>--%>
                                                </fo:table-row>
                                                <%

                                                           }
                                                       }
                                                %>
                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block>TOTAL</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end" space-after.optimum="10pt"><%=JSPUtil.printX(calculator.getBD("TSISPTOT"), 0)%></fo:block></fo:table-cell>
                                                    <%--<fo:table-cell ><fo:block text-align="center">x</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(cover.getDbPremi(),2)%></fo:block></fo:table-cell>--%>
                                                </fo:table-row>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Liability Spreading </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="35mm"/>
                                            <fo:table-column column-width="2mm"/>
                                            <fo:table-column />
                                            <fo:table-body>
                                                <%
                                                       //final DTOList deductibles = io.getDeductibles();

                                                       {
                                                           DTOList treatyDetails = io.getTreatyDetails();

                                                           for (int j = 0; j < treatyDetails.size(); j++) {
                                                               InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                                                               calculator.add("LISPTOT", trd.getDbClaimAmount());
                                                %>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(trd.getStTreatyClassDesc())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(trd.getDbClaimAmount(), 0)%></fo:block></fo:table-cell>
                                                    <%--<fo:table-cell ><fo:block text-align="center">x</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(cover.getDbPremi(),2)%></fo:block></fo:table-cell>--%>
                                                </fo:table-row>
                                                <%

                                                           }
                                                       }
                                                %>
                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block>TOTAL</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(calculator.getBD("LISPTOT"), 0)%></fo:block></fo:table-cell>
                                                    <%--<fo:table-cell ><fo:block text-align="center">x</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(cover.getDbPremi(),2)%></fo:block></fo:table-cell>--%>
                                                </fo:table-row>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                                </fo:block></fo:table-cell>
                        </fo:table-row>


                    </fo:table-body>
                </fo:table>
            </fo:block>


            <fo:block font-size="<%=fontsize%>" space-after.optimum="10pt"> </fo:block>
            <%}%>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="-2mm"/>
                                        <fo:table-column column-width="71mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="20mm"/>
                                        <fo:table-body>
                                            <%

                                                        final DTOList details = pol.getDetails();

                                            %>


                                            <%

                                                        for (int i = 0; i < details.size(); i++) {
                                                            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);
                                                            if (!item.isDiscount()) {
                                                                continue;
                                                            }

                                                            String desc = item.getStDescription2();

                                                            if (item.isEntryByRate()) {
                                                                desc += JSPUtil.printX(item.getDbRate() + " %");
                                                            }

                                            %>
                                            <% }%>



                                            <%

                                                        for (int i = 0; i < details.size(); i++) {
                                                            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

                                                            if (!item.isFee()) {
                                                                continue;
                                                            }
                                            %>
                                            <% }%>




                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" space-after.optimum="10pt"></fo:block>

            <fo:block font-size="<%=fontsize%>">

                <!-- GARIS  -->
                <fo:block border-width="0.25pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="10pt"></fo:block>

                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Claim Date</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtClaimDate())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Premium payment date</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtPremiPayDate())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Claim Cause</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimCauseDesc())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Claim Event Location</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimEventLocation())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Claim Person Name</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimPersonName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Claim Person Address</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimPersonAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Estimated Amount</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbClaimAmountEstimate())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Amount Currency</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimCurrency())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Loss Status</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimLossStatusDesc())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Claim Benefit</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimBenefitDesc())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Claim Documents</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimDocuments())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>

                </fo:table>
            </fo:block>


            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>


            <fo:block font-size="6pt"
                      font-family="sans-serif"
                      line-height="10pt"
                      space-after.optimum="10pt"
                      text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%> PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>]

            </fo:block>

            <fo:block font-size="<%=fontsize%>" space-after.optimum="30pt"></fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (pol.getStClaimStatus().equalsIgnoreCase("PLA")) {%>
                                <fo:block text-align="center"><%= pol.getStCostCenterDesc()%>, <%=DateUtil.getDateStr(pol.getDtPLADate(), "dd ^^ yyyy")%></fo:block>
                                <% } else {%>
                                <fo:block text-align="center"><%= pol.getStCostCenterDesc()%>, <%=DateUtil.getDateStr(pol.getDtDLADate(), "dd ^^ yyyy")%></fo:block>
                                <% }%>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>


        </fo:flow>
    </fo:page-sequence>
</fo:root>




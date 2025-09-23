<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.math.BigDecimal,
         com.crux.lov.LOVManager,
         com.crux.common.parameter.Parameter,
         com.crux.util.fop.FOPUtil,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String attached = (String) request.getAttribute("attached");

            final String preview = (String) request.getAttribute("preview");
            boolean isPreview = false;
            if (preview != null) {
                if (preview.equalsIgnoreCase("Preview")) {
                    isPreview = true;
                }
            }

            boolean effective = pol.isEffective();

            String jenis_pol = pol.getStPolicyTypeDesc2().toUpperCase();

            String coverDesc = null;
            final DTOList objects2 = pol.getCoverage2();
            //InsurancePolicyCoverView cover2 = (InsurancePolicyCoverView) objects2.get(0);
            //coverDesc = cover2.getStInsuranceCoverDesc().toUpperCase();

            boolean coverCIS = false;
            boolean coverFire = false;
            boolean coverCICB = false;

            for (int p = 0; p < objects2.size(); p++) {
                InsurancePolicyCoverView cover2 = (InsurancePolicyCoverView) objects2.get(p);

                if (cover2.getStInsuranceCoverID().equalsIgnoreCase("8")) {
                    coverCIS = true;
                }
                if (cover2.getStInsuranceCoverID().equalsIgnoreCase("27")) {
                    coverFire = true;
                }
                if (cover2.getStInsuranceCoverID().equalsIgnoreCase("237")) {
                    coverCICB = true;
                }
            }

            System.out.print("@@@@@@@@@@@@@@@ :" + coverCIS);
            System.out.print("@@@@@@@@@@@@@@@ :" + coverFire);
            System.out.print("@@@@@@@@@@@@@@@ :" + coverCICB);

            if (coverCIS && coverCICB) {
                coverDesc = "CASH IN CASHIER BOX";
            } else if (coverCIS && coverFire) {
                coverDesc = "CASH IN SAFE";
            } else if (coverFire && coverCICB) {
                coverDesc = "CASH IN SAFE";
            } else if (coverCICB) {
                coverDesc = "CASH IN CASHIER BOX";
            } else if (coverFire) {
                coverDesc = "CASH IN SAFE";
            } else if (coverCIS) {
                coverDesc = "CASH IN SAFE";
            }
            System.out.print("@@@@@@@@@@@@@@@ :" + coverDesc);
            
            String nopol = pol.getStParentPolicy().getStPolicyNo();
            if (nopol.length() == 16) {
                nopol = nopol.substring(0, 4) + "-" + nopol.substring(4, 8) + "-" + nopol.substring(8, 12) + "-" + nopol.substring(12, 16);
            } else if (nopol.length() == 18) {
                nopol = nopol.substring(0, 4) + "-" + nopol.substring(4, 8) + "-" + nopol.substring(8, 12) + "-" + nopol.substring(12, 16) + "-" + nopol.substring(16, 18);
            }

            final String digitalsign = (String) request.getAttribute("digitalsign");
            boolean usingDigitalSign = false;
            if (digitalsign != null) {
                if (digitalsign.equalsIgnoreCase("Y")) {
                    usingDigitalSign = true;
                }
            }

            boolean isUsingBarcode = Parameter.readString("REPORT_SIGN_TYPE").equalsIgnoreCase("BARCODE");

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="3cm"
                               margin-bottom="0.5cm"
                               margin-left="2cm"
                               margin-right="2cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">


        <fo:flow flow-name="xsl-region-body">

            <% if (!effective) {%>
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>

            <fo:block font-size="16pt"
                      text-align="center">
                {L-ENG Policy No.-L}{L-INA No. Polis Induk-L} <%=nopol%>
            </fo:block>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
            <% }%>

            <% if (isPreview) {%>
            <fo:block font-size="20pt"
                      line-height="16pt"
                      color="blue"
                      text-align="center"
                      padding-top="10pt">
                PREVIEW MODE
            </fo:block>
            <% }%>            

            <% if (!effective) {%>
            <fo:block font-size="16pt"
                      line-height="16pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block font-size="16pt"
                      line-height="16pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                {L-INA IKHTISAR PERTANGGUNGAN -L}{L-ENG POLICY SCHEDULE -L}
            </fo:block>

            <fo:block font-size="16pt"
                      line-height="16pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="0pt">
                {L-INA ASURANSI -L}<%=coverDesc%>{L-ENG INSURANCE -L}
            </fo:block>

            <!-- GARIS -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify"></fo:block>

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

            <%
                        BigDecimal[] t = new BigDecimal[2];

                        final DTOList objects = pol.getParentPolicy3();
                        for (int i = 0; i < objects.size(); i++) {
                            InsurancePolicyObjDefaultView io = (InsurancePolicyObjDefaultView) objects.get(i);

                            int n = 0;
                            t[n] = BDUtil.add(t[n++], io.getDbObjectInsuredAmount());
                            t[n] = BDUtil.add(t[n++], io.getDbObjectPremiTotalAmount());

                        }
            %>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="8mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Master Insurance Policy -L}{L-INA Nomor Polis Induk-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=nopol%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><% if (pol.getStStatus().equals("RENEWAL")) {%>
                                    {L-ENG Renewal of -L}{L-INA Perpanjangan ke- -L} <%= pol.getStRenewalCounter()%>
                                    <% } else {%>
                                    {L-ENG New-L}{L-INA Baru -L}
                                    <%}%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Certificate No.-L}{L-INA No. Sertifikat -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=objects.size()%> Sertifikat</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>  

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>   
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>          
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block><%=JSPUtil.xmlEscape(pol.getStParentPolicy().getStCustomerName())%></fo:block></fo:table-cell>   
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Address-L}{L-INA Alamat-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block><%=JSPUtil.xmlEscape(pol.getStParentPolicy().getStCustomerAddress())%></fo:block></fo:table-cell>   
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="justify">Sesuai Sertifikat</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% if (Integer.parseInt(pol.getStPeriodLength()) > 0) {%>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG (both dates at 12.00 noon local time where the insured object is located)-L}{L-INA (Jam 12.00 siang waktu setempat dimana obyek pertanggungan berada) -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>
            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" space-before.optimum="5pt">                
                {L-ENG OBJECT DETAILS-L}{L-INA KETERANGAN OBJEK-L} :                
            </fo:block>

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>"
                      line-height="10pt" space-after.optimum="2pt"
                      text-align="justify" > 
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Sum Insured -L}{L-INA Harga Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=objects.size()%> Sertifikat</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=pol.getStCurrencyCode()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0], 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Coverage / Rate -L}{L-INA Coverage / Rate  -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=objects.size()%> Sertifikat</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=pol.getStCurrencyCode()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1], 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Deductible -L}{L-INA Deductible  -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="start"><%=objects.size()%> Sertifikat</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block> 

            <fo:block font-size="<%=fontsize%>" space-before.optimum="10pt">
                {L-ENG ENDORSEMENT/CLAUSES/ADDITIONAL TERMS-L}{L-INA ENDORSEMENT/KLAUSUL/SYARAT TAMBAHAN-L}
            </fo:block>

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>"
                      line-height="10pt" space-after.optimum="2pt"
                      text-align="justify" > </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="140mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell >
                                <fo:list-block space-after.optimum="5pt">
                                    <%
                                                final DTOList clausules = pol.getReportClausules();
                                                for (int ii = 0; ii < clausules.size(); ii++) {
                                                    InsurancePolicyClausulesView cl = (InsurancePolicyClausulesView) clausules.get(ii);

                                                    if (!cl.isSelected()) {
                                                        continue;
                                                    }

                                                    String desc = cl.getStDescription().replaceAll("%S%", pol.getEntity().getStEntityName());

                                                    if (cl.getStKeterangan() != null) {
                                                        desc = desc + " " + cl.getStKeterangan();
                                                    }
                                    %>
                                    <fo:list-item>
                                        <fo:list-item-label end-indent="label-end()">
                                            <fo:block>&#x2022;</fo:block>
                                        </fo:list-item-label>
                                        <fo:list-item-body start-indent="body-start()">
                                            <fo:block font-size="<%=fontsize%>">
                                                <fo:inline text-decoration="none"><%=JSPUtil.xmlEscape(desc)%></fo:inline>
                                            </fo:block>
                                        </fo:list-item-body>
                                    </fo:list-item>
                                    <% }%>
                                </fo:list-block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <%--            
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

            <fo:block font-size="<%=fontsize%>" >
                {L-ENG PREMIUM CALCULATION-L}{L-INA PERHITUNGAN PREMI-L}
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="18mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="20mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="20mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="25mm"/>
                                        <fo:table-body>

                                            <%

                                            final DTOList coverage2 = pol.getCoverageRecap();

                                            for (int p = 0; p < coverage2.size(); p++) {
                                                InsurancePolicyCoverView cover = (InsurancePolicyCoverView) coverage2.get(p);

                                                final boolean entryRate = cover.isEntryRate();

                                                if (Tools.compare(cover.getDbPremi(),BDUtil.zero)<=0) continue;

                                            %>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center">-</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                                                <fo:table-cell  number-columns-spanned="5"><fo:block  text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%> <%=JSPUtil.printX(cover.getDbInsuredAmount(),0)%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(cover.getDbPremi(),2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% } %>

                                        </fo:table-body>
                                    </fo:table>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="1pt" space-after.optimum="10pt" space-before.optimum="10pt"></fo:block>

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
                                        <fo:table-column column-width="60mm"/>
                                        <fo:table-column column-width="27mm"/>
                                        <fo:table-column column-width="7mm"/>
                                        <fo:table-column column-width="7mm"/>
                                        <fo:table-column column-width="27mm"/>
                                        <fo:table-body>
                                            <%

                                            final DTOList details = pol.getDetails();

                                            %>

                                            <fo:table-row>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell number-columns-spanned="2" >
                                                </fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="left"></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="left">Subtotal</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <%

                                            for (int i = 0; i < details.size(); i++) {
                                                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

                                                if (!item.isFee()||item.isPPN()) continue;
                                            %>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="left"></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="left"><%=JSPUtil.printX(item.getStDescription2())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(item.getDbAmount(),2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% } %>

                                            <fo:table-row>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell number-columns-spanned="2" >
                                                    <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>


                                            <fo:table-row>
                                                <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="left"></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="left">{L-ENG Total Premium-L}{L-INA Total Premi-L}</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(BDUtil.add(pol.getDbPremiTotal(), BDUtil.add(pol.getDbNDPCost(), pol.getDbNDSFee())),2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell ></fo:table-cell>
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
            --%>
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="1pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>

            <% if (usingDigitalSign) {%>
            <fo:block font-size="6pt"
                      space-after.optimum="3pt"
                      text-align="justify" >
                Dokumen ini dicetak secara sistem dan dinyatakan sah.
            </fo:block>
            <% }%>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="3pt"></fo:block>

            <fo:block font-size="6pt" space-after.optimum="10pt">
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%>

                <% if (effective) {%>
                PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Cetakan : <%=pol.getStPrintCounter()%>
                <% }%>   
                <% if (usingDigitalSign) {%>
                <%if (!isUsingBarcode) {%>  [<%=JSPUtil.print(pol.getStSignCode())%>] <%}%>
                <% }%>   
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="90mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L} 
                                    <%= JSPUtil.printX(pol.getStCostCenterDesc())%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%></fo:block>
                                    <% } else {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L} 
                                    Jakarta, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%></fo:block>
                                    <% }%>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <%if (!isUsingBarcode) {%><fo:block text-align="center">{L-ENG Authorized-L}{L-INA Tanda Tangan Yang Berwenang-L}</fo:block><%}%>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-body>

                        <% if (usingDigitalSign) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="KANTOR_ASKRIDA_<%=pol.getCostCenter(pol.getStCostCenterCode()).getTypeDescription().toUpperCase()%>_<%=pol.getCostCenter(pol.getStCostCenterCode()).getStDescription().toUpperCase()%>_<%=pol.getStPolicyNo()%>" orientation="0">
                                            <barcode:datamatrix> 
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                                <%--<human-readable>none</human-readable>--%>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>

                                </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <%--<% if (pol.getUserApproved().getFile().getStFilePath()!=null) { %>--%>
                            <fo:table-cell>
                                <%if (!isUsingBarcode) {%>
                                <%if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUser(pol.getUserIDSign()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% } else {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% }%>
                                <% }%>

                                <%if (isUsingBarcode) {%>
                                <fo:block text-align = "center" space-before.optimum="5pt">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=pol.getEncryptedApprovedWho()%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                                <%--<human-readable>none</human-readable>--%>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>

                                </fo:block>
                                <fo:block font-size="6pt"
                                          font-family="sans-serif"
                                          line-height="10pt"
                                          space-after.optimum="10pt"
                                          text-align="center">
                                    <%=pol.getStSignCode()%>
                                </fo:block>
                                <%}%>

                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>                         

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <%--<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getUserApproved().getStUserName().toUpperCase()%></fo:block></fo:table-cell>--%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%if (pol.getParaf() != null) {%><fo:leader leader-length="10mm"/><%}%><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%>                                     <%if (pol.getParaf() != null) {%>                                     <fo:external-graphic                                         content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform"                                         overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"  />                                     <%}%>                             </fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%> 

                    </fo:table-body>
                </fo:table> 
            </fo:block>  

        </fo:flow>
    </fo:page-sequence>
</fo:root>
<%@ page import="com.webfin.insurance.model.*,
         com.webfin.entity.model.EntityAddressView,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.common.parameter.Parameter,
         com.crux.util.fop.FOPUtil,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String attached = (String) request.getAttribute("attached");

            String param[] = attached.split("[\\|]");

            boolean isAttached = param[0].equalsIgnoreCase("2") ? true : false;
            boolean tanpaTanggal = param[0].equalsIgnoreCase("3") ? true : false;
            boolean tanpaNama = param[0].equalsIgnoreCase("5") ? true : false;
            boolean tanpaNamaTanggal = param[0].equalsIgnoreCase("6") ? true : false;
            boolean tanpaRate = param[0].equalsIgnoreCase("7") ? true : false;
            boolean klausulaTerlampir = param[0].equalsIgnoreCase("8") ? true : false;

            if (attached.length() > 1) {
                if (param[1] != null) {
                    isAttached = !isAttached ? param[1].equalsIgnoreCase("2") ? true : false : isAttached;
                    tanpaTanggal = !tanpaTanggal ? param[1].equalsIgnoreCase("3") ? true : false : tanpaTanggal;
                    tanpaRate = !tanpaRate ? param[1].equalsIgnoreCase("7") ? true : false : tanpaRate;
                }
            }

            final String preview = (String) request.getAttribute("preview");
            boolean isPreview = false;
            if (preview != null) {
                if (preview.equalsIgnoreCase("Preview")) {
                    isPreview = true;
                }
            }

            boolean effective = pol.isEffective();
            boolean coMember = pol.isCoMember();

            String nopol = pol.getStPolicyNo();

            String jenis_pol = pol.getStPolicyTypeDesc2().toUpperCase();

            final String digitalsign = (String) request.getAttribute("digitalsign");
            boolean usingDigitalSign = false;
            if (digitalsign != null) {
                if (digitalsign.equalsIgnoreCase("Y")) {
                    usingDigitalSign = true;
                }
            }

            boolean isUsingBarcode = Parameter.readString("REPORT_SIGN_TYPE").equalsIgnoreCase("BARCODE");
            String askridaLogoPath = Parameter.readString("DIGITAL_POLIS_LOGO_PIC");

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="36cm"
                               page-width="21cm"
                               margin-top="1cm"
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

        <!-- usage of page layout -->
        <!-- HEADER -->

        <%--    <fo:static-content flow-name="xsl-region-before">
      <fo:block text-align="end"
            font-size="6pt"
            font-family="TAHOMA"
            line-height="12pt" >
        Reminder letter - PT. Asuransi Bangun Askrida Page:<fo:page-number/>
      </fo:block>
    </fo:static-content> --%>

        <fo:flow flow-name="xsl-region-body">

            <fo:block text-align = "center">
                <fo:external-graphic
                    content-height="scale-to-fit"
                    height="1.00in" content-width="1.00in"
                    scaling="non-uniform" src="url(file:///<%=askridaLogoPath%>)"  />
            </fo:block>

            <% if (!effective) {%>
            <fo:block font-size="16pt" font-family="TAHOMA"
                      line-height="16pt" space-after.optimum="0pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block font-size="16pt" font-family="TAHOMA"
                      line-height="16pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt"
                      space-after.optimum="10pt">
                {L-INA SURAT PERPANJANGAN -L}{L-ENG RENEWAL NOTICE -L}
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>No: <%=JSPUtil.printX(pol.getStCostCenterCode())%>/<%=JSPUtil.printX(pol.getStPolicyNo().substring(2, 4))%>/<%=JSPUtil.printX(pol.getStPolicyNo().substring(12, 16))%>/<%=DateUtil.getMonth2Digit(pol.getDtPeriodEnd())%>/<%=DateUtil.getYear2Digit(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCostCenterDesc())%>, <%=DateUtil.getDateStr(new Date(), "dd ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="15pt" space-after.optimum="10pt">
            </fo:block>

            <%--  

<%
   for (int n = 0; n < l.size(); n++) {
      InsurancePolicyView pol = (InsurancePolicyView) l.get(n);

      final boolean lastPage = n==l.size()-1;
%>

            --%>

            <!-- GARIS  -->
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="1pt"></fo:block>

            <!-- defines text title level 1-->

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG To -L}{L-INA Kepada Yth. -L}</fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Nama</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.xmlEscape(pol.getStProducerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% final DTOList address = pol.getAddress();
                                    for (int i = 0; i < address.size(); i++) {
                                        EntityAddressView adr = (EntityAddressView) address.get(i);
                        %>

                        <fo:table-row>
                            <fo:table-cell><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Telepon</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(adr.getStPhone())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Email</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(adr.getStEmail())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% if (pol.getStMarketingOfficerWho() != null) {%>
                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Marketing Officer</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getEntity2(pol.getStMarketingOfficerWho()).getStEntityName())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <% }%>     

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!-- Normal text -->

            <!-- DYNAMIC HEADER WORDING -->


            <!-- GARIS -->
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="1pt" space-after.optimum="10pt"></fo:block>

            <!-- Normal text -->

            <fo:block font-size="<%=fontsize%>" line-height="12pt" space-after.optimum="10pt" text-align="justify">
                {L-ENG Dear Sir / Madam, -L}{L-INA Dengan hormat, -L}
            </fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="12pt" space-after.optimum="10pt" text-align="justify">
                {L-ENG We herewith, PT. Asuransi Bangun Askrida notify that, as per our notification  your policy No. -L}{L-INA Bersama surat ini, kami dari PT. Asuransi Bangun Askrida memberitahukan, bahwa sesuai dengan catatan kami, Polis Asuransi Bapak / Ibu / Saudara No. -L}<fo:inline font-weight="bold" ><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:inline>{L-ENG would be expired on -L}{L-INA akan jatuh tempo pada tanggal -L}<fo:inline font-weight="bold" ><%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:inline>, {L-ENG with data as follows -L}{L-INA dengan data sebagai berikut : -L}
            </fo:block>

            <!-- GARIS -->
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="1pt" space-after.optimum="1pt"></fo:block>

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="1pt" text-align="justify" > </fo:block>

            <fo:block font-size="<%=fontsize%>">

                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell >
                                <fo:list-block space-after.optimum="5pt">

                                </fo:list-block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="2pt" text-align="justify" > </fo:block>

            <%
                        if (isAttached) {%>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Sum Insured -L}{L-INA Harga Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG See Attached-L}{L-INA Lihat Lampiran -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Coverage / Rate-L}{L-INA Jaminan / Suku Premi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG See Attached-L}{L-INA Lihat Lampiran -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <%}%>

            <% if (!isAttached) {%>
            <%
                 final DTOList objects = pol.getObjects();

                 final FlexFieldHeaderView objectMap = pol.getPolicyType().getObjectMap();

                 final DTOList objectMapDetails = objectMap == null ? null : objectMap.getReportHidden();

                 for (int i = 0; i < objects.size(); i++) {
                     InsurancePolicyObjectView io = (InsurancePolicyObjectView) objects.get(i);
                     InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) io;

            %>

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
                    <fo:table-column />
                    <fo:table-body>

                        <!-- INTEREST START -->

                        <%

                                             if (objectMapDetails != null)
                                                 for (int j = 0; j < objectMapDetails.size(); j++) {
                                                     FlexFieldDetailView iomd = (FlexFieldDetailView) objectMapDetails.get(j);

                                                     final Object desc = iomd.getDesc(io);

                                                     if (desc == null) {
                                                         continue;
                                                     }

                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(iomd.getStFieldDesc())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                                                                         }
                                                                     else {

                        %>
                        <%                                        }

                        %>

                        <!-- INTEREST END -->
                        <% if (objx.getStRiskCategoryCode1() != null) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Risk Code-L}{L-INA Kode Resiko-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <%=JSPUtil.printX(objx.getStRiskCategoryCode1())%>
                                    <% if (objx.getStRiskCategoryCode2() != null) {%>
                                    / <%=JSPUtil.printX(objx.getStRiskCategoryCode2())%>
                                    <% }%>
                                    <% if (objx.getStRiskCategoryCode3() != null) {%>
                                    / <%=JSPUtil.printX(objx.getStRiskCategoryCode3())%>
                                    <% }%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="40mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column column-width="35mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column column-width="35mm"/>
                                            <fo:table-body>
                                                <%
                                                                     final DTOList suminsureds = io.getSuminsureds();

                                                                     for (int j = 0; j < suminsureds.size(); j++) {
                                                                         InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);
                                                %>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(tsi.getStDescriptionAuto())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(tsi.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>


                                                <%

                                                                     }
                                                %>
                                                <fo:table-row>
                                                    <fo:table-cell ></fo:table-cell>
                                                    <fo:table-cell ></fo:table-cell>
                                                    <fo:table-cell ></fo:table-cell>
                                                    <fo:table-cell number-columns-spanned="2" >
                                                        <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>


                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block>{L-ENG Total-L}{L-INA Jumlah-L}</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(io.getDbObjectInsuredAmount(), 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>

                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>

                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Coverage / Rate-L}{L-INA Jaminan / Suku Premi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="40mm"/>
                                            <fo:table-column column-width="25mm"/>
                                            <fo:table-column column-width="10mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column column-width="10mm"/>
                                            <fo:table-column column-width="30mm"/>
                                            <fo:table-body>

                                                <%
                                                                     final DTOList coverage = io.getCoverage();%>

                                                <%

                                                                     for (int j = 0; j < coverage.size(); j++) {
                                                                         InsurancePolicyCoverView cover = (InsurancePolicyCoverView) coverage.get(j);

                                                                         //  final boolean hasRate = (Tools.compare(cover.getDbRate(),BDUtil.zero)>0);
%>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(cover.getStInsuranceCoverDesc())%>
                                                            <% if (cover.getStDescription() != null) {%>
                                                            <%=JSPUtil.xmlEscape(cover.getStDescription())%>
                                                            <% }%>
                                                        </fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <% if (tanpaRate) {%>
                                                    <% if (cover.getDbRate() != null) {%>
                                                    <%--<% if(Tools.compare(cover.getDbRate(),BDUtil.zero)>0) { %>--%>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printPct(cover.getDbRatePct(), 5)%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block><%=pol.getStRateMethodDesc()%></fo:block></fo:table-cell>
                                                    <% } else {%>
                                                    <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                    <% }%>
                                                    <%--<% } %>--%>
                                                    <% } else {%>
                                                    <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                    <% }%>
                                                    <fo:table-cell ><fo:block><%=pol.getStCurrencyCode()%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(cover.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>

                                                <% }%>

                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>


                                    <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

                                </fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Deductible-L}{L-INA Risiko Sendiri-L} </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="40mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column column-width="2mm"/>
                                            <fo:table-column />
                                            <fo:table-body>

                                                <%
                                                                     final DTOList deductibles = io.getDeductibles();

                                                                     for (int j = 0; j < deductibles.size(); j++) {
                                                                         InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);

                                                                         String desc = "";
                                                                         if (ded.getStDescription() != null) {
                                                                             desc = ded.getStDescription();
                                                                         }

                                                %>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(ded.getStClaimCauseDesc())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(ded.getStAutoDesc().replace(".000.00 null", ".00"))%> <%=JSPUtil.xmlEscape(desc)%></fo:block></fo:table-cell>
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

            <% if (klausulaTerlampir) {%>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="140mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell >
                                <fo:list-block space-after.optimum="5pt">
                                    <fo:list-item>
                                        <fo:list-item-label end-indent="label-end()">
                                            <fo:block>&#x2022;</fo:block>
                                        </fo:list-item-label>
                                        <fo:list-item-body start-indent="body-start()">
                                            <fo:block font-size="<%=fontsize%>">
                                                <fo:inline text-decoration="none">Lihat Lampiran</fo:inline>
                                            </fo:block>
                                        </fo:list-item-body>
                                    </fo:list-item>
                                </fo:list-block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% } else {%>

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
                                    <%}%>
                                </fo:list-block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% }%>

            <% }%>

            <% if (klausulaTerlampir) {%>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <% if (pol.getStWarranty() != null) {%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Warranty/Under Condition-L}{L-INA Syarat Tambahan/Kondisi Lainnya-L} </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Lihat Lampiran</fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% } else {%>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <% if (pol.getStWarranty() != null) {%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Warranty/Under Condition-L}{L-INA Syarat Tambahan/Kondisi Lainnya-L} </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block wrap-option="yes-wrap" linefeed-treatment="preserve"
                                                      white-space-treatment="preserve" white-space-collapse="false"><%= JSPUtil.xmlEscape(pol.getStWarranty())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

            <fo:block font-size="<%=fontsize%>" >
                {L-ENG PREMIUM CALCULATION-L}{L-INA PERHITUNGAN PREMI-L}
            </fo:block>

            <% if (!tanpaRate) {%>
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

                                                 final DTOList coverage = pol.getCoverage2();

                                                 for (int j = 0; j < coverage.size(); j++) {
                                                     InsurancePolicyCoverView cover = (InsurancePolicyCoverView) coverage.get(j);

                                                     final boolean entryRate = cover.isEntryRate();

                                                     if (Tools.compare(cover.getDbPremi(), BDUtil.zero) <= 0) {
                                                         continue;
                                                     }

                                            %>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center">-</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                                                <fo:table-cell  number-columns-spanned="5"><fo:block  text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%> <%=JSPUtil.printX(cover.getStCalculationDesc())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(cover.getDbPremi(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% }%>

                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% } else {%>

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

                                                     if (Tools.compare(cover.getDbPremi(), BDUtil.zero) <= 0) {
                                                         continue;
                                                     }

                                            %>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center">-</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                                                <fo:table-cell  number-columns-spanned="5"><fo:block  text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%> <%=JSPUtil.printX(cover.getDbInsuredAmount(), 0)%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(cover.getDbPremi(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% }%>

                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

            <% }%>

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
                                                <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <%

                                                        for (int i = 0; i < details.size(); i++) {
                                                            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

                                                            if (!item.isFee() || item.isPPN()) {
                                                                continue;
                                                            }
                                            %>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="left"></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="left"><%=JSPUtil.printX(item.getStDescription2())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(item.getDbAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% }%>

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
                                                <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(BDUtil.add(pol.getDbPremiTotal(), BDUtil.add(pol.getDbNDPCost(), pol.getDbNDSFee())), 2)%></fo:block></fo:table-cell>
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
                                    <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if (!tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% } else {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    <%= JSPUtil.printX(pol.getCostCenter(pol.getCostCenter(pol.getStCostCenterCode()).getStSubCostCenterCode()).getStDescription())%><%if (!tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
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
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>

                                </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
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

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="1pt" space-after.optimum="10pt" space-before.optimum="10pt"></fo:block>

            <fo:block font-size="<%=fontsize%>" space-after.optimum="10pt"></fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="12pt">
                <fo:inline text-decoration="underline" font-weight="bold">Diisi oleh Tertanggung :</fo:inline>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="12pt">
                Mohon dapat diperpanjang Polis tersebut dengan ketentuan sebagai berikut : 
            </fo:block>

            <fo:block font-size="6pt" line-height="12pt" space-after.optimum="10pt">
                <fo:inline font-style="italic">Beri tanda (X) untuk pilihan Saudara *)</fo:inline>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>(</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>...</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>)</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Tetap, tanpa perubahan</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>(</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>...</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>)</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Dengan perubahan sebagai berikut: ( mengisi formulir terlampir )</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>(</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>...</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>)</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Tidak Diperpanjang</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" space-after.optimum="10pt"></fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>* Polis dapat dikirim ke</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>................................................</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>* Nomor telp yang dapat dihubungi</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>................................................</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" space-after.optimum="10pt"></fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="12pt" space-after.optimum="10pt" text-align="justify">
                <fo:inline font-style="italic">Apabila disetujui mohon di fax kembali pemberitahuan ini dan menandatanganinya atau 
                    untuk informasi lebih lanjut dapat menghubungi kami. Terima kasih telah memberikan kepercayaan Asuransi <%=JSPUtil.printX(pol.getStPolicyTypeDesc2())%>
                    anda kepada perusahaan kami, dan sangat senang apabila diberi kepercayaan kembali atas perpanjangan penutupan polis tersebut.</fo:inline>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">Menyetujui,</fo:block>
            <fo:block font-size="<%=fontsize%>">Tanda Tangan Tertanggung</fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>





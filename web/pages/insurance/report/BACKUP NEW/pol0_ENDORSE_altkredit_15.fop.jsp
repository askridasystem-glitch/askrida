<%@ page import="com.webfin.insurance.model.*,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*,
java.util.Date,
com.crux.util.Tools,
com.crux.util.fop.FOPUtil,
com.webfin.entity.model.EntityView,
com.crux.common.parameter.Parameter,
com.webfin.ar.model.ARTaxView,
java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
<%

final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
final String fontsize = (String) request.getAttribute("FONTSIZE");
final String attached = (String) request.getAttribute("attached");
boolean tanpaTanggal = attached.equalsIgnoreCase("3")?false:true;

boolean effective = pol.isEffective();

final String preview = (String) request.getAttribute("preview");
boolean isPreview = false;
if (preview!=null)
    if (preview.equalsIgnoreCase("Preview")) isPreview = true;

//if (true) throw new NullPointerException();
final String digitalsign = (String) request.getAttribute("digitalsign");
boolean usingDigitalSign = false;
if (digitalsign!=null)
    if (digitalsign.equalsIgnoreCase("Y")) usingDigitalSign = true;

boolean isUsingBarcode = Parameter.readString("REPORT_SIGN_TYPE").equalsIgnoreCase("BARCODE");

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="first"
                               page-height="14cm"
                               page-width="21.5cm"
                               margin-top="3cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="2cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->


    <!-- actual layout -->
    <fo:page-sequence master-reference="first" initial-page-number="1">


        <fo:flow flow-name="xsl-region-body">
            <!-- ROW -->

            <fo:block text-align="start"
                      font-size="16pt"

                      line-height="12pt"
                      font-weight="bold">
                NOTA KREDIT
            </fo:block>

            <fo:block space-after.optimum="20pt"></fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="100mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG TO-L}{L-INA KEPADA-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block-container height="5cm" width="2cm" top="0cm" left="13cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1.5cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="<%=fontsize%>">

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start">{L-ENG Number-L}{L-INA Nomor-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start">{L-ENG Dates-L}{L-INA Tanggal-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start">{L-ENG Branch-L}{L-INA Cabang-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCostCenterDesc())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block-container>



            <% if (isPreview) { %>
            <fo:block font-size="20pt"
                      line-height="16pt" space-after.optimum="0pt"
                      color="blue"
                      text-align="center"
                      padding-top="10pt">
                PREVIEW MODE
            </fo:block>
            <% }%>

            <% if (!effective) {%>
            <fo:block font-size="16pt"
                      line-height="16pt" space-after.optimum="10pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="3pt"></fo:block>


            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
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
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%> {L-ENG up to-L}{L-INA s/d-L} <%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmountEndorse(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>

            <!-- ROW -->

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="37mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Details-L}{L-INA Detil-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Gross Premium-L}{L-INA Premi Bruto-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                        final DTOList details = pol.getDetails();

                        for (int i = 0; i < details.size(); i++) {
                            InsurancePolicyItemsView det = (InsurancePolicyItemsView) details.get(i);

                            if (!det.isDiscount()) continue;

                            String rate = "";

                            if (det.isEntryByRate()) rate = JSPUtil.printX(det.getDbRate(),2) + "%";
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Discount-L}{L-INA Diskon-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=rate%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(det.getDbAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        <!-- GARIS DALAM KOLOM -->

                        <%
                        for (int i = 0; i < details.size(); i++) {
                            InsurancePolicyItemsView det = (InsurancePolicyItemsView) details.get(i);

                            if (!det.isFee()||det.isPPN()) continue;
                        /*
                        String rate = "";

                        rate = JSPUtil.printX(det.getDbRate(),2) + "%";
                         */
                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(det.getStDescription2())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(det.getDbAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } %>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" ><fo:block border-width="0.35pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Due To Us-L}{L-INA Total Tagihan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(pol.getDbTotalDue(),pol.getDbNDPPN()),2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->



                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" space-after.optimum="5pt" line-height="0.15pt"></fo:block>

            <fo:block font-size="<%=fontsize%>" font-weight="bold">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="180mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="left">{L-ENG SAY :-L}{L-INA TERBILANG :-L} <%=NumberSpell.readNumber(JSPUtil.printX(BDUtil.sub(pol.getDbTotalDue(),pol.getDbNDPPN()),2), pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->


                    </fo:table-body>
                </fo:table>
            </fo:block>


            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>"
                      line-height="10pt" space-after.optimum="2pt"
                      text-align="justify" > </fo:block>

            <% //if(pol.getStInstallmentPeriods()>=2)
            {

            DTOList installment = pol.getInstallment();

            if (installment.size()>1) {

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

                        for (int i = 0; i < installment.size(); i++) {
                            InsurancePolicyInstallmentView ins = (InsurancePolicyInstallmentView) installment.get(i);

                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Installment <%=(i+1)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(ins.getDtDueDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(ins.getDbAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } %>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% } %>
            <% } %>
            <!-- ROW -->

           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" space-before.optimum="3pt" space-after.optimum="3pt" line-height="0.15pt"></fo:block>

            <fo:block font-size="6pt"
                      font-family="sans-serif"
                      line-height="10pt"
                      space-after.optimum="5pt"
                      text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%> PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>]
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="130mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <!-- GARIS DALAM KOLOM -->

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
                    <fo:table-column column-width="150mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell ><fo:block>
                                    <% if (usingDigitalSign) { %>
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="KANTOR_ASKRIDA_<%=pol.getCostCenter(pol.getStCostCenterCode()).getTypeDescription().toUpperCase()%>_<%=pol.getCostCenter(pol.getStCostCenterCode()).getStDescription().toUpperCase()%>_<%=pol.getStPolicyNo()%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>17pt</barcode:height>
                                                <barcode:module-width>2pt</barcode:module-width>
                                                <%--<human-readable>none</human-readable>--%>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                    <%}%>

                            </fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (usingDigitalSign) { %>
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="0.75in" content-width="0.75in"
                                        scaling="non-uniform" src="url(<%=pol.getUser(pol.getUserIDSign()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% } else { %>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit" height="0.75in"
                                        content-width="0.75in" scaling="non-uniform"
                                        src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% } %>
                                <% } %>
                            </fo:table-cell>
                        </fo:table-row>

                        <% if (usingDigitalSign) { %>
                        <fo:table-row>

                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
                            <%--<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getUserApproved().getStUserName().toUpperCase()%></fo:block></fo:table-cell>--%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else { %>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% } %>

                        </fo:table-row>
                        <%}%>

                        <fo:table-row>

                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%></fo:block></fo:table-cell>
                            <% } else { %>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% } %>

                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>
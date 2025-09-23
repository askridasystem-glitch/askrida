<%@ page import="com.webfin.insurance.model.*,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.form.ProductionClaimReportForm,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>

<%
            final DTOList l = (DTOList) request.getAttribute("RPT");

            final ProductionClaimReportForm form = (ProductionClaimReportForm) SessionManager.getInstance().getCurrentForm();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-width="35cm"
                               page-height="21.cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence id="N2528" master-reference="only">

        <fo:flow flow-name="xsl-region-body">

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="33mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block >
                                    <fo:external-graphic <%--content-width="scale-to-fit"--%>
                                        content-height="100%"
                                        width="100%"
                                        scaling="uniform" src="url(D:\jboss-fin\server\default\deploy\fin.ear\fin.war\pages\main\img\burung.jpg)"/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="16"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block space-after.optimum="20pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block font-weight="bold" font-size="12pt">
                                    Reinsurance Type
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="14">
                                <fo:block font-weight="bold" font-size="12pt">
                                    :  <%=form.getStFltTreatyTypeDesc().toUpperCase()%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block font-weight="bold" font-size="12pt">
                                    Reinsurance
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="14">
                                <fo:block font-weight="bold" font-size="12pt">
                                    :  <%=form.getMarketer().getStEntityName().toUpperCase()%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block font-weight="bold" font-size="12pt">
                                    Claim Period
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="14">
                                <fo:block font-weight="bold" font-size="12pt">
                                    : <%=DateUtil.getDateStr(form.getAppDateFrom(), "MMMM yyyy")%> Up To <%=DateUtil.getDateStr(form.getAppDateTo(), "MMMM yyyy")%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block font-weight="bold" font-size="12pt">
                                    Class of Bussiness
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="14">
                                <fo:block font-weight="bold" font-size="12pt">
                                    :
                                    <% if (form.getStPolicyTypeID() != null) {%>
                                    <%=JSPUtil.printX(form.getPolicyType().getStDescription().toUpperCase())%>
                                    <% } else {%>
                                    <%=JSPUtil.printX(form.getPolicyTypeGroup().getStGroupName().toUpperCase())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">No.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Policy No</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">The Insured</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Debitur</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Period Start</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Period End</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Curr</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Insured Amount</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Claim Amount</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Claim Share</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">D.O.L</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Cause of Claim</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm" text-align="center">Remark</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>
                    <fo:table-body>

                        <%
                                    //int counter = 30;

                                    BigDecimal[] t = new BigDecimal[3];

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimAmountApproved());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimAmount());

                                        //if (i==counter) {
                                        //    counter = counter + 30;
%>
                        <%--
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>

                        <% } %>
                        --%>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(String.valueOf(i + 1))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="start"><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="start"><%=JSPUtil.printX(pol.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPeriodStart())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmountApproved(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmount(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtClaimDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getClaimCauses().getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block text-align="center" line-height="5mm">Total</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[0], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[1], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[2], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="14"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" ><fo:block>Demikian disampaikan, terima kasih atas kerjasamanya</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="14"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" ><fo:block>Jakarta, <%=DateUtil.getDateStr(new Date(), "dd MMMM yyyy")%> </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block space-before.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="14"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" ><fo:block>PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
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
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[0], 0)%>" orientation="0">
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
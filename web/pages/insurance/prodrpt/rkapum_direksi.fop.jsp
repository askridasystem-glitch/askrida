<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         java.util.Date,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.form.ProductionFinanceReportForm,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final DTOList l = (DTOList) request.getAttribute("RPT");

            final ProductionFinanceReportForm form = (ProductionFinanceReportForm) SessionManager.getInstance().getCurrentForm();

            InsurancePolicyView policy = (InsurancePolicyView) l.get(0);

            BigDecimal amountRKAP = new BigDecimal(0);
            if (form.getStPolicyTypeGroupID() != null) {
                amountRKAP = policy.getDbInsuredAmount();
            } else {
                amountRKAP = BDUtil.mul(new BigDecimal(184810), new BigDecimal(1000000));
            }
%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="first"
                               page-height="29cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">

            <fo:region-body margin-top="0cm" margin-bottom="2cm"/>
            <fo:region-before extent="0.5cm"/>
            <fo:region-after extent="1cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="first">

        <fo:static-content flow-name="xsl-region-after">
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">

            <!-- defines text title level 1-->

            <!-- GARIS  -->

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">
                                    LAPORAN BIAYA OPERASIONAL (RKAP)
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">
                                    <%=JSPUtil.printX(form.getStReportDesc().replace("4. RKAP", " ").toUpperCase())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">
                                    <% if (form.getPolicyDateFrom() != null || form.getPolicyDateTo() != null) {%>
                                    Tanggal Mutasi : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <% if (form.getStBranch() != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-weight="bold">
                                    Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <% if (form.getStRegion() != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-weight="bold">
                                    Outlet : <%=JSPUtil.printX(form.getStRegionDesc())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-weight="bold">
                                    <% if (form.getStPolicyTypeGroupID() != null) {%>
                                    Group Operasional : <%=JSPUtil.printX(form.getStPolicyTypeGroupDesc())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-weight="bold">
                                    <% if (form.getStPolicyTypeID() != null) {%>
                                    Jenis Operasional : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">No.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Tanggal</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Cabang</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">No Bukti</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Akun</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Keterangan</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Nilai</fo:block></fo:table-cell>
                            <%--<fo:table-cell ><fo:block text-align="center">Saldo</fo:block></fo:table-cell>--%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>
                    <fo:table-body>

                        <%if (form.getStBranch() == null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center" font-weight="bold">RKAP Nasional 2014</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(amountRKAP, 2)%></fo:block></fo:table-cell>
                            <%--<fo:table-cell ></fo:table-cell>--%>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->
                        <%
                                    BigDecimal[] t = new BigDecimal[1];

                                    BigDecimal saldo = null;
                                    BigDecimal amount = null;

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], pol.getDbAmount());

                                        String description = null;
                                        if (pol.getStDescription().length() > 55) {
                                            description = pol.getStDescription().substring(0, 55);
                                        } else {
                                            description = pol.getStDescription().substring(0, pol.getStDescription().length());
                                        }

                                        amount = BDUtil.add(amount, pol.getDbAmount());

                                        //if (i == 0) {
                                        //    saldo = BDUtil.sub(amountRKAP, saldo);
%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getCostCenter(pol.getStCostCenterCode()).getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStAccountno())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.xmlEscape(description)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbAmount(), 2)%></fo:block></fo:table-cell>
                            <%--<fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(saldo, pol.getDbAmount()), 2)%></fo:block></fo:table-cell>--%>
                        </fo:table-row>
                        <%--
                        <% } else if (i > 0) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getCostCenter(pol.getStCostCenterCode()).getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(pol.getStAccountno())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.xmlEscape(description)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbAmount(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(saldo, amount), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        --%>
                        <%
                                        //     }
                                    }
                        %>


                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0], 2)%></fo:block></fo:table-cell>
                            <%--<fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(amountRKAP, t[0]), 2)%></fo:block></fo:table-cell>--%>
                        </fo:table-row>

                        <%if (form.getStBranch() == null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center" font-weight="bold">SISA RKAP</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(BDUtil.sub(amountRKAP, t[0]), 2)%></fo:block></fo:table-cell>
                            <%--<fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(amountRKAP, t[0]), 2)%></fo:block></fo:table-cell>--%>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
            </fo:block>
            <fo:block font-size="8pt">
                {L-ENG Print User-L}{L-INA Print User-L} : <%=SessionManager.getInstance().getCreateUser().getStShortName()%>
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
                        <svg xmlns="http://www.w3.org/2000/svg" width="0cm" height="0cm" xml:space="preserve">
                            <rect style="fill:white;stroke:white" x="0" y="0" width="0cm" height="0cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>
<%@ page import="com.webfin.ar.model.ARRequestFee,
         com.webfin.ar.model.ARRequestFeeObj,
         com.crux.util.*,
         java.math.BigDecimal,
         com.crux.lang.LanguageManager,
         com.crux.web.controller.SessionManager,
         com.crux.common.parameter.Parameter,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%
            final ARRequestFee req = (ARRequestFee) request.getAttribute("REQUEST");

            boolean effective = req.isEffective();
            boolean cashier = req.isCashierFlag();

            boolean isStatusApproval = req.isStatusApproval();
            boolean isStatusCashback = req.isStatusCashback();

            String status = null;
            BigDecimal nominal = null;
            if (isStatusApproval) {
                status = "SLIP PERMINTAAN BIAYA";
                nominal = req.getDbNominal();
            }
            if (isStatusCashback) {
                status = "SLIP REALISASI BIAYA";
                nominal = req.getDbNominalUsed();
            }

            String NIPPimpinan = null;
            String sub_cccode = req.getCostCenter().getStSubCostCenterCode();
            if (!sub_cccode.equalsIgnoreCase("00")) {
                NIPPimpinan = Parameter.readString("BRANCH_" + sub_cccode);
            }

            String originalWatermarkPath = Parameter.readString("DIGITAL_NOTA_ORI_PIC");
            String duplicateWatermarkPath = Parameter.readString("DIGITAL_NOTA_DUPLICATE_PIC");
            String copyWatermarkPath = Parameter.readString("DIGITAL_NOTA_COPY_PIC");
            String askridaLogoPath = Parameter.readString("DIGITAL_POLIS_LOGO_PIC");
            String askridaLogoBlackWhitePath = Parameter.readString("DIGITAL_POLIS_LOGOBW_PIC");

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fox="http://xmlgraphics.apache.org/fop/extensions">

    <%
                String watermarkPath = null;
                String logoAskridaPath = null;

                for (int x = 0; x < 3; x++) {

                    if (x == 0) {
                        watermarkPath = originalWatermarkPath;
                        logoAskridaPath = askridaLogoPath;
                    } else if (x == 1) {
                        watermarkPath = copyWatermarkPath;
                        logoAskridaPath = askridaLogoBlackWhitePath;
                    } else if (x == 2) {
                        watermarkPath = duplicateWatermarkPath;
                        logoAskridaPath = askridaLogoBlackWhitePath;
                    }

    %>

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="first"
                               page-height="14cm"
                               page-width="21.5cm"
                               margin-top="1cm"
                               margin-left="1cm"
                               margin-right="1cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm" background-image="file:///<%=watermarkPath%>"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->


    <!-- actual layout -->
    <fo:page-sequence master-reference="first" initial-page-number="1" force-page-count="no-force">


        <fo:flow flow-name="xsl-region-body">

            <!-- ROW -->

            <fo:block text-align = "start">
                <fo:external-graphic
                    content-height="scale-to-fit"
                    height="0.75in" content-width="0.75in"
                    scaling="non-uniform" src="url(file:///<%=logoAskridaPath%>)"  />
            </fo:block>

            <% if (!effective) {%>
            <fo:block font-size="16pt" font-family="TAHOMA"
                      color="red"
                      text-align="center">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block text-align="center"
                      font-size="16pt"
                      font-family="TAHOMA"
                      font-weight="bold">
                <%= JSPUtil.printX(status)%>
            </fo:block>

            <fo:block-container height="3.5cm" width="6.5cm" top="0cm" left="11.5cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="2cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="4cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal">

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt">
                                <fo:block font-size="10pt" text-align="start" font-weight="bold">No. Bukti</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(req.getStTransactionNo())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt">
                                <fo:block font-size="10pt" text-align="start" font-weight="bold">Tanggal</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start"><%=DateUtil.getDateStr(req.getDtTglRequest(), "d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt">
                                <fo:block font-size="10pt" text-align="start" font-weight="bold">KaOps.</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start"><%=JSPUtil.printX(req.getCostCenter().getStDescription().toUpperCase())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <fo:block-container height="3.5cm" width="18.5cm" top="3.5cm" left="0cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="4cm"/>
                    <fo:table-column column-width="11.5cm"/>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%if (isStatusApproval) {%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2" ><fo:block text-align="center" font-weight="bold">U R A I A N</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">Jumlah</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2" ><fo:block text-align="start">Estimasi <%=JSPUtil.xmlEscape(req.getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(nominal, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                        <% if (isStatusCashback) {%>

                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">No. Rekening</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">U R A I A N</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">Jumlah</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2" ><fo:block text-align="start">Estimasi <%=JSPUtil.xmlEscape(req.getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(req.getDbNominal(), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="start">Realisasi :</fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row>

                        <%
                             DTOList reqObject = req.getReqObject();
                             for (int i = 0; i < reqObject.size(); i++) {
                                 ARRequestFeeObj reqobj = (ARRequestFeeObj) reqObject.get(i);
                        %>

                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center">- <%= JSPUtil.printX(reqobj.getStAccountNo())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="start"><%=JSPUtil.xmlEscape(reqobj.getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(reqobj.getDbNominal(), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>
                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <!-- ROW -->

            <fo:block-container height="7cm" width="18.5cm" top="7cm" left="0cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-column column-width="3.5cm"/>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-body>

                        <% if (isStatusCashback) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6"><fo:block text-align="start" font-weight="bold">TOTAL : <%=JSPUtil.printX(nominal, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6"><fo:block text-align="start" font-weight="bold">TERBILANG : <%=NumberSpell.readNumber(JSPUtil.printX(nominal, 2), req.getStCurrency())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block font-size="6pt" space-after.optimum="10pt">
                                    Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <%if (req.getUserApproved().getStBranch() != null) {%>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Setujui</fo:block></fo:table-cell>
                            <% } else {%>
                            <%if (!sub_cccode.equalsIgnoreCase("00")) {%>
                            <fo:table-cell><fo:block text-align="center">Setujui KP</fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Mengetahui</fo:block></fo:table-cell>
                            <%} else {%>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Setujui</fo:block></fo:table-cell>
                            <% }%>
                            <% }%>
                            <fo:table-cell><fo:block text-align="center">Entry</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Kasir</fo:block></fo:table-cell>
                        </fo:table-row>

                        <%if (effective) {%>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block>
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="KANTOR_ASKRIDA_<%=req.getStTransactionNo()%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>20pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                </fo:block>
                            </fo:table-cell>
                            <%if (req.getUserApproved().getStBranch() != null) {%>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="0.75in" content-width="0.75in"
                                        scaling="non-uniform" src="url(<%=req.getUser(NIPPimpinan).getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                            <% } else {%>
                            <%if (!sub_cccode.equalsIgnoreCase("00")) {%>
                            <fo:table-cell>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="0.75in" content-width="0.75in"
                                        scaling="non-uniform" src="url(<%=req.getUser(req.getStApprovedWho()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="0.75in" content-width="0.75in"
                                        scaling="non-uniform" src="url(<%=req.getUser(NIPPimpinan).getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                            <% } else {%>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="0.75in" content-width="0.75in"
                                        scaling="non-uniform" src="url(<%=req.getUser(req.getStApprovedWho()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                            <% }%>
                            <% }%>
                            <fo:table-cell>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="0.75in" content-width="0.75in"
                                        scaling="non-uniform" src="url(<%=req.getUser(req.getStCreateWho()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                            <%if (cashier) {%>
                            <fo:table-cell>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="0.75in" content-width="0.75in"
                                        scaling="non-uniform" src="url(<%=req.getUser(req.getStCashierWho()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                            <%} else {%>
                            <fo:table-cell></fo:table-cell>
                            <%}%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <%if (req.getUserApproved().getStBranch() != null) {%>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-align = "center"><%=req.getUser(NIPPimpinan).getStShortName().toUpperCase()%></fo:block></fo:table-cell>
                            <% } else {%>
                            <%if (!sub_cccode.equalsIgnoreCase("00")) {%>
                            <fo:table-cell><fo:block text-align = "center"><%=req.getUser(req.getStApprovedWho()).getStShortName().toUpperCase()%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-align = "center"><%=req.getUser(NIPPimpinan).getStShortName().toUpperCase()%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-align = "center"><%=req.getUser(req.getStApprovedWho()).getStShortName().toUpperCase()%></fo:block></fo:table-cell>
                            <% }%>
                            <% }%>
                            <fo:table-cell><fo:block text-align = "center"><%=req.getUser(req.getStCreateWho()).getStShortName().toUpperCase()%></fo:block></fo:table-cell>
                            <%if (cashier) {%>
                            <fo:table-cell><fo:block text-align = "center"><%=req.getUser(req.getStCashierWho()).getStShortName().toUpperCase()%></fo:block></fo:table-cell>
                            <%} else {%>
                            <fo:table-cell></fo:table-cell>
                            <%}%>
                        </fo:table-row>

                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

        </fo:flow>
    </fo:page-sequence>
    <%
                }
    %>
</fo:root>
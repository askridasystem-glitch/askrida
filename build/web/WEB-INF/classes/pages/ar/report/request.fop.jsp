<%@ page import="com.webfin.ar.model.ARRequestFee,
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
            String keterangan = null;
            BigDecimal nominal = null;
            if (isStatusApproval) {
                status = "SLIP PENGELUARAN BIAYA";
                keterangan = req.getStDescription();
                nominal = req.getDbNominal();
            }
            if (isStatusCashback) {
                status = "SLIP PENGEMBALIAN BIAYA";
                keterangan = req.getStDescriptionApproved();
                nominal = req.getDbNominalUsed();
            }

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="first"
                               page-height="16cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="0.5cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->


    <!-- actual layout -->
    <fo:page-sequence master-reference="first" initial-page-number="1">


        <fo:flow flow-name="xsl-region-body">
            <!-- ROW -->

            <fo:block text-align = "start">
                <fo:external-graphic
                    content-height="scale-to-fit"
                    height="0.75in" content-width="0.75in"
                    scaling="non-uniform" src="url(file:///<%=Parameter.readString("DIGITAL_POLIS_LOGO_PIC")%>)"  />
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

            <fo:block-container height="5cm" width="7cm" top="0cm" left="13cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="2cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal">

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start" font-weight="bold">No. Bukti</fo:block>
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
                                <fo:block text-align="start" font-weight="bold">Tanggal</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start"><%=DateUtil.getDateStr(req.getDtTglRequest(), "d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <fo:block-container height="5cm" width="20cm" top="3.5cm" left="0cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="130mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center" font-weight="bold">No. Rekening</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center" font-weight="bold">U R A I A N</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center" font-weight="bold">Jumlah</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center"><%= JSPUtil.printX(req.getStAccountNo())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.xmlEscape(keterangan)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(nominal, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <!-- ROW -->

            <fo:block-container height="5cm" width="20cm" top="10cm" left="0cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="start" font-weight="bold">TERBILANG : <%=NumberSpell.readNumber(JSPUtil.printX(req.getDbNominal(), 2), req.getStCurrency())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Diperiksa</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Dibukukan</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Kasir</fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
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
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                                <%--<human-readable>none</human-readable>--%>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="0.75in" content-width="0.75in"
                                        scaling="non-uniform" src="url(<%=req.getUser(req.getStApprovedWho()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="0.75in" content-width="0.75in"
                                        scaling="non-uniform" src="url(<%=req.getUser(req.getStCreateWho()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                            <%if (cashier) {%>
                            <fo:table-cell >
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
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

        </fo:flow>
    </fo:page-sequence>
</fo:root>
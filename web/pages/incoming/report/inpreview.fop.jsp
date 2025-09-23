<%@ page import="com.webfin.incoming.model.*,
         com.crux.util.*,
         java.math.BigDecimal,
         com.crux.web.controller.SessionManager,
         com.webfin.incoming.forms.ApprovalBODMasterForm,
         com.crux.common.parameter.Parameter,
         com.crux.lang.LanguageManager,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%
            final ApprovalBODMasterForm form = (ApprovalBODMasterForm) SessionManager.getInstance().getCurrentForm();

            final ApprovalBODView entity = form.getEntity();
%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-width="21cm"
                               page-height="30cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="3cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only">

        <fo:static-content flow-name="xsl-region-after">
            <fo:block>
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="KANTOR_ASKRIDA_<%=entity.getStLetterNo()%>" orientation="0">
                        <barcode:datamatrix>
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>
            </fo:block>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">

            <fo:block-container height="5cm" width="15cm" top="20cm" left="0cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="5cm"/>
                    <fo:table-column column-width="10cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt" border-width="0.5pt" border-left-style="solid">
                                <fo:block line-height="5mm" text-align="center">NAMA</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt" border-width="0.5pt" border-left-style="solid" border-right-style="solid">
                                <fo:block line-height="5mm" text-align="center">CATATAN</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt" border-width="0.5pt" border-left-style="solid">
                                <fo:block line-height="5mm" text-align="start">
                                    <%=JSPUtil.printX(entity.getStReceiverName()).toUpperCase()%>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt" border-width="0.5pt" border-left-style="solid" border-right-style="solid">
                                <fo:block line-height="5mm" text-align="start"><%=JSPUtil.xmlEscape(entity.getStReplyNote())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <fo:block text-align = "center">
                <fo:external-graphic
                    content-height="scale-to-fit"
                    height="1.00in" content-width="1.00in"
                    scaling="non-uniform" src="url(file:///<%=Parameter.readString("DIGITAL_POLIS_LOGO_PIC")%>)"  />
            </fo:block>

            <fo:block space-before.optimum="10pt" font-size="12pt" text-align="center" text-decoration="underline">
                MEMO INTERN
            </fo:block>

            <fo:block font-size="12pt" text-align="center">
                No. <%=JSPUtil.xmlEscape(entity.getStLetterNo())%>
            </fo:block>

            <fo:block space-before.optimum="10pt" font-size="12pt" text-align="start" >
                Kepada Yth.
            </fo:block>

            <%
                        String nama = null;
                        String entid[] = entity.getStCCID().split("[\\;]");
                        for (int k = 0; k < entid.length; k++) {

                            nama = entid[k];
            %>

            <fo:block font-size="12pt" text-align="start" >
                <%=entity.getUser(nama).getStJobPosition().toUpperCase()%>
            </fo:block>
            <%
                        }
            %>

            <fo:block font-size="12pt" text-align="start" >
                PT. Asuransi Bangun Askrida
            </fo:block>

            <fo:block space-before.optimum="10pt" space-after.optimum="10pt" font-size="12pt" text-align="center">
                PERIHAL : <%=JSPUtil.xmlEscape(entity.getStSubject())%>
            </fo:block>

            <fo:block space-after.optimum="10pt" font-size="12pt" text-align="justify" >
                Dengan hormat,
            </fo:block>

            <fo:block space-after.optimum="5pt" font-size="12pt" wrap-option="yes-wrap" linefeed-treatment="preserve"
                      white-space-treatment="preserve" white-space-collapse="false"
                      ><%=JSPUtil.xmlEscape(entity.getStNote())%></fo:block>

            <fo:block font-size="12pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="100mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align = "center">Jakarta, <%=LanguageManager.getInstance().translate(DateUtil.getDateStr(entity.getDtLetterDate(), "d ^^ yyyy"))%></fo:block>
                                <fo:block text-align = "center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=entity.getUser(entity.getStSender()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-align = "center"><fo:inline text-decoration="underline" font-weight="bold"><%= entity.getUser(entity.getStSender()).getStUserName()%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-align = "center" ><%= entity.getUser(entity.getStSender()).getStJobPosition()%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>
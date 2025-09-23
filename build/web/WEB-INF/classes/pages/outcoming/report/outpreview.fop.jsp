<%@ page import="com.webfin.outcoming.model.*,
         com.crux.util.*,
         java.math.BigDecimal,
         com.crux.lang.LanguageManager,
         com.crux.web.controller.SessionManager,
         com.webfin.outcoming.forms.UploadBODMasterForm,
         com.crux.common.parameter.Parameter,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final UploadBODMasterForm form = (UploadBODMasterForm) SessionManager.getInstance().getCurrentForm();

            final UploadBODView entity = form.getEntity();

            String sender = null;
            if (entity.getStOutID() != null) {
                sender = entity.getStSender();
            } else {
                sender = SessionManager.getInstance().getSession().getStUserID();
            }

            String nama = null;
            DTOList l = entity.getDistributions();
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


                        <%
                                    for (int k = 0; k < l.size(); k++) {
                                        UploadBODDistributionView dist = (UploadBODDistributionView) l.get(k);

                                        if (Tools.isNo(dist.getStApprovalFlag())) {
                                            continue;
                                        }
                        %>

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
                                    <%=dist.getUser(dist.getStReceiver()).getStUserName().toUpperCase()%>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt" border-width="0.5pt" border-left-style="solid" border-right-style="solid">
                                <fo:block line-height="5mm" text-align="start"><%=JSPUtil.xmlEscape(dist.getStReplyNote())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <% }%>

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
                        for (int i = 0; i < l.size(); i++) {
                            UploadBODDistributionView dist = (UploadBODDistributionView) l.get(i);

                            nama = dist.getStReceiver();
            %>

            <fo:block font-size="12pt" text-align="start" >
                <%=dist.getUser(nama).getStJobPosition().toUpperCase()%>
            </fo:block>

            <%}%>

            <fo:block font-size="12pt" text-align="start" >
                PT. Asuransi Bangun Askrida
            </fo:block>

            <fo:block space-before.optimum="10pt" space-after.optimum="10pt" font-size="12pt" text-align="center">
                PERIHAL : <%=JSPUtil.xmlEscape(entity.getStSubject())%>
                <%=JSPUtil.printX(entity.getPolicy(entity.getStPolicyID()).getStPolicyNo())%>
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
                                        scaling="non-uniform" src="url(<%=entity.getUser(sender).getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-align = "center"><fo:inline text-decoration="underline" font-weight="bold"><%= entity.getUser(sender).getStUserName()%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-align = "center" ><%= entity.getUser(sender).getStJobPosition()%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>
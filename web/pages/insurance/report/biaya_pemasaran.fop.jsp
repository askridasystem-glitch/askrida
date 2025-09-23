<%@ page import="com.webfin.insurance.form.InsuranceUploadForm,
         com.crux.web.controller.SessionManager,
         java.util.ArrayList,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         com.webfin.insurance.model.BiayaPemasaranView,
         com.webfin.insurance.model.BiayaPemasaranDetailView,
         java.math.BigDecimal,
         com.crux.common.parameter.Parameter,
         com.crux.lang.LanguageManager,
         java.util.Date"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-width="21cm"
                               page-height="30cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>

    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">

        <%
                    BiayaPemasaranView receipt = (BiayaPemasaranView) request.getAttribute("PEMASARAN");

                    ArrayList colW = new ArrayList();

                    colW.add(new Integer(5));
                    colW.add(new Integer(20));
                    colW.add(new Integer(40));
                    colW.add(new Integer(15));

                    boolean isPosted = receipt.isStatus1Flag();
        %>


        <fo:flow flow-name="xsl-region-body">

            <%
                        String bw = "0.2pt";
            %>

            <% if (!isPosted) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="5mm" color="red">SPECIMEN</fo:block>
            <% }%>

            <fo:block font-family="Helvetica" font-weight="bold" font-size="10pt" line-height="5mm">Lampiran Biaya Pemasaran</fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" font-size="8pt" line-height="5mm">No. SBP : <%=JSPUtil.print(receipt.getStNoSPP())%></fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" font-size="8pt" line-height="5mm">No. Surat Pemasaran : <%=JSPUtil.print(receipt.getStKeterangan())%></fo:block>

            <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>" >
                <fo:table-header>

                    <fo:table-row>
                        <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No</fo:block></fo:table-cell>
                        <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No Akun</fo:block></fo:table-cell>
                        <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Deskripsi</fo:block></fo:table-cell>
                        <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Nilai</fo:block></fo:table-cell>
                    </fo:table-row>

                </fo:table-header>

                <%=FOPUtil.printColumnWidth(colW, 19, 1, "cm")%>
                <fo:table-body>

                    <%
                                BigDecimal[] t = new BigDecimal[1];
                                DTOList objects = receipt.getPmsDetail();

                                for (int i = 0; i < objects.size(); i++) {
                                    BiayaPemasaranDetailView obj = (BiayaPemasaranDetailView) objects.get(i);

                                    int n = 0;
                                    t[n] = BDUtil.add(t[n++], obj.getDbNilai());
                    %>

                    <fo:table-row>
                        <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(String.valueOf(i + 1))%></fo:block></fo:table-cell>
                        <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(obj.getStAccountNo())%></fo:block></fo:table-cell>
                        <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="start" font-size="8pt"><%=JSPUtil.xmlEscape(obj.getStDescription())%></fo:block></fo:table-cell>
                        <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(obj.getDbNilai(), 0)%></fo:block></fo:table-cell>
                    </fo:table-row>

                    <%
                                }
                    %>

                    <fo:table-row>
                        <fo:table-cell number-columns-spanned="4">
                            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block>
                        </fo:table-cell>
                    </fo:table-row>

                    <fo:table-row>
                        <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="3"><fo:block text-align="center" font-size="8pt" >SUBTOTAL</fo:block></fo:table-cell>
                        <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(t[0], 0)%></fo:block></fo:table-cell>
                    </fo:table-row>

                </fo:table-body>
            </fo:table>

            <fo:block font-size="6pt" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>
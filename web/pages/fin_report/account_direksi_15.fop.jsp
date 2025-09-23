<%@ page import="com.webfin.gl.util.GLUtil,
         com.webfin.gl.ejb.GLReportEngine2,
         com.webfin.gl.model.AccountView,
         com.crux.util.JSPUtil,
         com.webfin.gl.report2.form.FinReportForm,
         com.crux.util.DateUtil,
         java.util.Date,
         com.crux.util.BDUtil,
         com.crux.util.SQLAssembler,
         java.math.BigDecimal"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="32cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">
            <fo:region-body margin-top="2cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>

    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">

        <%
                    GLReportEngine2 glr = new GLReportEngine2();

                    FinReportForm form = (FinReportForm) request.getAttribute("FORM");

                    long lPeriodFrom = form.getLPeriodFrom();
                    long lPeriodTo = form.getLPeriodTo();
                    long lYearFrom = form.getLYearFrom();
                    long lYearTo = form.getLYearFrom();

                    long lPeriodFromLastYear = new Long(1);
                    long lPeriodToLastYear = new Long(12);
                    long lYearLastYear = form.getLYearFrom() - 1;

                    glr.setBranch(form.getBranch());
                    glr.setStFlag("Y");

                    BigDecimal kali = new BigDecimal(-1);

                    BigDecimal laba_rugi_berjalan_kon = BDUtil.roundUp(BDUtil.roundUp(glr.getSummaryRangedOnePeriod("BAL|NEG|ADD=2", "51611", "51611", lPeriodTo, lYearFrom, lYearTo)));
                    BigDecimal laba_rugi_berjalan_sya = BDUtil.roundUp(BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|NEG|ADD=2", "51611", "51611", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                    BigDecimal laba_rugi_berjalan = BDUtil.sub(laba_rugi_berjalan_kon, laba_rugi_berjalan_sya);

                    BigDecimal kerugian_penurunan_kon = BDUtil.roundUp(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2", "514", "514", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                    BigDecimal kerugian_penurunan_sya = BDUtil.roundUp(BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|NEG|ADD=2", "514", "514", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                    BigDecimal kerugian_penurunan = BDUtil.negate(BDUtil.add(kerugian_penurunan_kon, kerugian_penurunan_sya));

                    BigDecimal kerugian_penurunan_tahun_lalu_kon = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2", "514", "514", lPeriodFromLastYear, lPeriodToLastYear, lYearLastYear, lYearLastYear));
                    BigDecimal kerugian_penurunan_tahun_lalu_sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|NEG|ADD=2", "514", "514", lPeriodFromLastYear, lPeriodToLastYear, lYearLastYear, lYearLastYear));
                    BigDecimal kerugian_penurunan_tahun_lalu = BDUtil.negate(BDUtil.add(kerugian_penurunan_tahun_lalu_kon, kerugian_penurunan_tahun_lalu_sya));
                    //penghasilanBeban = BDUtil.add(penghasilanBeban, selisih_nilai);

                    BigDecimal pendapatan = BDUtil.sub(kerugian_penurunan, kerugian_penurunan_tahun_lalu);
                    String amt = null;
                    BigDecimal amt1 = null;

                    if (BDUtil.lesserThanZero(pendapatan)) {
                        amt1 = BDUtil.negate(pendapatan);
                        amt = "(" + JSPUtil.printX(amt1, 0) + ")";
                    } else {
                        amt1 = pendapatan;
                        amt = JSPUtil.printX(amt1, 0);
                    }

                    BigDecimal total_laba = BDUtil.add(laba_rugi_berjalan, amt1);


        %>

        <fo:flow flow-name="xsl-region-body">

            <%
                        String bw = "0.5pt";
            %>

            <%if (form.getBranch() != null) {
                            if (!form.isPosted()) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" color="red">DRAFT</fo:block>
            <% }
                        }%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >PT. ASURANSI BANGUN ASKRIDA</fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >LABA RUGI KOMPREHENSIF</fo:block>
            <%--<fo:block font-family="Helvetica" font-weight="bold" text-align="center" >KONSOLIDASI</fo:block>--%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getPeriodTitleDescription())%></fo:block>
            <% if (form.getStKeterangan() != null) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >(<%=JSPUtil.printX(form.getStKeteranganDesc())%>)</fo:block>
            <% }%>
            <% if (form.getBranch() != null) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getCostCenter().getStDescription())%></fo:block>
            <% }%>
            <fo:block space-after.optimum="14pt"/>


            <fo:block font-family="Helvetica" font-size="10pt" display-align="center" border-width="0.1pt">
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>">
                    <fo:table-column column-width="130mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" ><fo:block line-height="10mm" background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm" background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">NILAI</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block font-weight="bold">LABA BERSIH PERIODE BERJALAN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(laba_rugi_berjalan), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row height="3mm">
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block >Pendapatan (beban) komprehensif lain tahun berjalan setelah pajak</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row height="3mm">
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block font-weight="bold">TOTAL LABA KOMPREHENSIF TAHUN BERJALAN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(total_laba), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row height="3mm">
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block >Laba Bersih yang dapat diatribusikan kepada :</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="3mm">Pemilik entitas induk</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(total_laba), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="3mm">Kepentingan nonpengendali</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.zero, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" ><fo:block line-height="10mm" background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm" background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(BDUtil.negate(total_laba), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>

                <fo:block font-size="6pt"
                          font-family="sans-serif"
                          line-height="10pt"
                          space-after.optimum="15pt"
                          text-align="left" >
                    Print Stamp : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "hhmmssyyyyMMdd "))%>
                </fo:block>

                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <fo:table-row>

                            <fo:table-cell>
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(BDUtil.zero, 0)%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                </fo:block>
                            </fo:table-cell>

                            <fo:table-cell>
                                <fo:table table-layout="fixed">
                                    <fo:table-column column-width="70mm"/>
                                    <fo:table-column />
                                    <fo:table-body>
                                        <fo:table-row height="10mm"/>

                                        <fo:table-row>
                                            <fo:table-cell number-columns-spanned="2" >
                                                <fo:block text-align="center">S.E. &amp; O.</fo:block>
                                                <fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(form.getDtPrintDate(), "d ^^ yyyy")%></fo:block>
                                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                                <fo:block text-align="center">DIREKSI</fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>

                                        <fo:table-row height="20mm">
                                        </fo:table-row>

                                        <fo:table-row>
                                            <fo:table-cell>
                                                <fo:block text-align="center" text-decoration="underline"><%=JSPUtil.printX(form.getPerson1Name())%></fo:block>
                                                <fo:block text-align="center"><%=JSPUtil.printX(form.getPerson1Title())%></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell>
                                                <fo:block text-align="center" text-decoration="underline"><%=JSPUtil.printX(form.getPerson2Name())%></fo:block>
                                                <fo:block text-align="center"><%=JSPUtil.printX(form.getPerson2Title())%></fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>

                                    </fo:table-body>
                                </fo:table>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>

            </fo:block>
        </fo:flow>
    </fo:page-sequence>
</fo:root>
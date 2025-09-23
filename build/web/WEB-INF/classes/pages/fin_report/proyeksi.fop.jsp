<%@ page import="com.webfin.gl.util.GLUtil,
         com.webfin.gl.ejb.GLReportEngine2,
         com.webfin.gl.report2.form.FinReportForm,
         com.crux.ff.FlexTableManager,
         com.crux.ff.model.FlexTableView,
         com.webfin.gl.model.JournalView,
         com.crux.util.DateUtil,
         java.util.Date,
         java.math.BigDecimal,
         com.crux.util.JSPUtil,
         com.crux.util.BDUtil,
         java.util.HashMap,
         com.crux.util.*,
         java.util.ArrayList,
         com.crux.util.fop.FOPUtil"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="21cm"
                               page-width="30cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>

    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">

        <%
                    GLReportEngine2 glr = new GLReportEngine2();

                    FinReportForm form = (FinReportForm) request.getAttribute("FORM");

                    glr.setBranch(form.getBranch());
                    glr.setRegion(form.getRegion());
                    glr.setTahun(form.getYear());

                    long lTahun = Long.parseLong(form.getYear());
                    long lTahunLalu = lTahun - 1;

                    HashMap refmap = form.getRptRef();

                    DTOList ftl;
                    if (form.getBranch() != null) {
                        if (form.getRegion() != null) {
                            ftl = FlexTableManager.getInstance().getFlexTableProd3("PROD", form.getBranch(), form.getRegion());
                        } else {
                            ftl = FlexTableManager.getInstance().getFlexTableProd2("PROD", form.getBranch());
                        }
                    } else {
                        ftl = FlexTableManager.getInstance().getFlexTableProd("PROD");
                    }

                    ArrayList cmap = new ArrayList();

                    cmap.add(new Integer(45));
                    cmap.add(new Integer(35));
                    cmap.add(new Integer(35));
                    cmap.add(new Integer(35));
                    cmap.add(new Integer(35));
                    cmap.add(new Integer(35));
                    cmap.add(new Integer(35));

                    ArrayList cw = FOPUtil.computeColumnWidth(cmap, 29, 2, "cm");

        %>

        <fo:flow flow-name="xsl-region-body">

            <%
                        String bw = "0.5pt";
            %>

            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >PROYEKSI PRODUKSI DAN REALISASI TAHUN <%=JSPUtil.printX(form.getYear())%></fo:block>
            <%if (form.getBranch() != null) {
                            if (form.getRegion() != null) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >CABANG <%=JSPUtil.printX(form.getRegionDesc().toUpperCase())%></fo:block>
            <% } else {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >CABANG <%=JSPUtil.printX(form.getBranchDesc().toUpperCase())%></fo:block>
            <% }%>
            <% } else {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >(KONSOLIDASI)</fo:block>
            <% }%>
            <fo:block font-family="Helvetica" font-size="10pt" >
                <fo:table table-layout="fixed">
                    <%
                                for (int i = 0; i < cw.size(); i++) {
                                    String cwx = (String) cw.get(i);
                    %>
                    <fo:table-column column-width="<%=cwx%>"/>
                    <%
                                }
                    %>

                    <fo:table-header>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell number-rows-spanned="2" padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">BULAN</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(lTahunLalu)%></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" display-align="center" number-columns-spanned="3"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">TARGET <%=JSPUtil.printX(form.getYear())%></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">(+/-)</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center" border-right-style="solid"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">(+/-)</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">GOAL SETTING</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">RKAP</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">REALISASI</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">GOAL SETTING</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center" border-right-style="solid"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">RKAP</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                    </fo:table-header>
                    <fo:table-body>

                        <%
                                    BigDecimal bulanTotalLastYear = new BigDecimal(0);
                                    BigDecimal bulanTotalGoalset = new BigDecimal(0);
                                    BigDecimal bulanTotalRkap = new BigDecimal(0);
                                    BigDecimal bulanTotalReal = new BigDecimal(0);
                                    BigDecimal bulanTotalSelGoalset = new BigDecimal(0);
                                    BigDecimal bulanTotalSelRkap = new BigDecimal(0);

                                    BigDecimal triwulanTotalLastYear = new BigDecimal(0);
                                    BigDecimal triwulanTotalGoalset = new BigDecimal(0);
                                    BigDecimal triwulanTotalRkap = new BigDecimal(0);
                                    BigDecimal triwulanTotalReal = new BigDecimal(0);
                                    BigDecimal triwulanTotalSelGoalset = new BigDecimal(0);
                                    BigDecimal triwulanTotalSelRkap = new BigDecimal(0);

                                    for (int i = 0; i < ftl.size(); i++) {

                                        FlexTableView ft = (FlexTableView) ftl.get(i);

                                        String opCode = ft.getStReference1();
                                        String bulan = ft.getStReference2();
                                        String bulanDesc = ft.getStReference4();
                                        BigDecimal goalset = ft.getDbReference1();
                                        BigDecimal rkap = ft.getDbReference2();

                                        boolean isDESC = "DESC".equalsIgnoreCase(opCode);

                                        if (isDESC) {

                                            BigDecimal tahunLalu = glr.getProduksi(bulan, String.valueOf(lTahunLalu));
                                            BigDecimal tahunBerjalan = glr.getProduksi(bulan, String.valueOf(lTahun));

                                            BigDecimal sub1 = BDUtil.sub(tahunBerjalan, goalset);
                                            BigDecimal sub2 = BDUtil.sub(tahunBerjalan, rkap);

                                            String amt1 = null;
                                            if (Tools.compare(sub1, BDUtil.zero) < 0) {
                                                amt1 = "(" + JSPUtil.printX(BDUtil.negate(sub1), 0) + ")";
                                            } else {
                                                amt1 = JSPUtil.printX(sub1, 0);
                                            }

                                            String amt2 = null;
                                            if (Tools.compare(sub2, BDUtil.zero) < 0) {
                                                amt2 = "(" + JSPUtil.printX(BDUtil.negate(sub2), 0) + ")";
                                            } else {
                                                amt2 = JSPUtil.printX(sub2, 0);
                                            }

                                            bulanTotalLastYear = BDUtil.add(bulanTotalLastYear, tahunLalu);
                                            bulanTotalGoalset = BDUtil.add(bulanTotalGoalset, goalset);
                                            bulanTotalRkap = BDUtil.add(bulanTotalRkap, rkap);
                                            bulanTotalReal = BDUtil.add(bulanTotalReal, tahunBerjalan);
                                            bulanTotalSelGoalset = BDUtil.add(bulanTotalSelGoalset, sub1);
                                            bulanTotalSelRkap = BDUtil.add(bulanTotalSelRkap, sub2);
                        %>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(bulanDesc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(tahunLalu, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(goalset, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(rkap, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(tahunBerjalan, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(amt1)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(amt2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                        }

                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" font-weight="bold">Jumlah</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(bulanTotalLastYear, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(bulanTotalGoalset, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(bulanTotalRkap, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(bulanTotalReal, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(bulanTotalSelGoalset, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(bulanTotalSelRkap, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block space-after.optimum="20pt"></fo:block>

            <fo:block font-family="Helvetica" font-size="10pt" >
                <fo:table table-layout="fixed">
                    <%
                        for (int i = 0; i < cw.size(); i++) {
                            String cwx = (String) cw.get(i);
                    %>
                    <fo:table-column column-width="<%=cwx%>"/>
                    <%
                        }
                    %>

                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-rows-spanned="2" padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">TRIWULAN</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(lTahunLalu)%></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" display-align="center" number-columns-spanned="3"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">TARGET <%=JSPUtil.printX(form.getYear())%></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">(+/-)</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center" border-right-style="solid"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">(+/-)</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">GOAL SETTING</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">RKAP</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">REALISASI</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">GOAL SETTING</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center" border-right-style="solid"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">RKAP</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>
                    <fo:table-body>

                        <%
                            for (int i = 0; i < ftl.size(); i++) {

                                FlexTableView ft = (FlexTableView) ftl.get(i);

                                String opCode = ft.getStReference1();
                                String bulanDesc = ft.getStReference4();
                                String bulan[] = ft.getStReference2().split("[\\|]");

                                boolean isTRIWULAN = "TRIWULAN".equalsIgnoreCase(opCode);

                                if (isTRIWULAN) {

                                    BigDecimal tahunLalu = glr.getProduksiTriwulan(bulan[0], bulan[2], String.valueOf(lTahunLalu));
                                    BigDecimal tahunBerjalan = glr.getProduksiTriwulan(bulan[0], bulan[2], String.valueOf(lTahun));
                                    BigDecimal goalset = new BigDecimal(0);
                                    BigDecimal rkap = new BigDecimal(0);

                                    DTOList prodTriwulan = glr.getProduksiTriwulanInfo2(bulan[0], bulan[2]);
                                    for (int j = 0; j < prodTriwulan.size(); j++) {
                                        JournalView gli = (JournalView) prodTriwulan.get(j);

                                        goalset = gli.getDbDebit();
                                        rkap = gli.getDbCredit();

                                    }


                                    //BigDecimal goalset = glr.getProduksiTriwulanInfo2(bulan[0], bulan[2]);
                                    //BigDecimal rkap = glr.getProduksiTriwulanInfo2(bulan[0], bulan[2]);

                                    BigDecimal sub1 = BDUtil.sub(tahunBerjalan, goalset);
                                    BigDecimal sub2 = BDUtil.sub(tahunBerjalan, rkap);

                                    String amt1 = null;
                                    if (Tools.compare(sub1, BDUtil.zero) < 0) {
                                        amt1 = "(" + JSPUtil.printX(BDUtil.negate(sub1), 0) + ")";
                                    } else {
                                        amt1 = JSPUtil.printX(sub1, 0);
                                    }

                                    String amt2 = null;
                                    if (Tools.compare(sub2, BDUtil.zero) < 0) {
                                        amt2 = "(" + JSPUtil.printX(BDUtil.negate(sub2), 0) + ")";
                                    } else {
                                        amt2 = JSPUtil.printX(sub2, 0);
                                    }

                                    triwulanTotalLastYear = BDUtil.add(triwulanTotalLastYear, tahunLalu);
                                    triwulanTotalGoalset = BDUtil.add(triwulanTotalGoalset, goalset);
                                    triwulanTotalRkap = BDUtil.add(triwulanTotalRkap, rkap);
                                    triwulanTotalReal = BDUtil.add(triwulanTotalReal, tahunBerjalan);
                                    triwulanTotalSelGoalset = BDUtil.add(triwulanTotalSelGoalset, sub1);
                                    triwulanTotalSelRkap = BDUtil.add(triwulanTotalSelRkap, sub2);
                        %>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(bulanDesc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(tahunLalu, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(goalset, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(rkap, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(tahunBerjalan, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(amt1)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(amt2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                }

                            }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" font-weight="bold">Jumlah</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(triwulanTotalLastYear, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(triwulanTotalGoalset, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(triwulanTotalRkap, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(triwulanTotalReal, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(triwulanTotalSelGoalset, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(triwulanTotalSelRkap, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="6pt"
                      font-family="sans-serif"
                      line-height="10pt"
                      space-after.optimum="15pt"
                      text-align="left" >
                Print Stamp : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "hhmmssyyyyMMdd "))%>
            </fo:block>

            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>" orientation="0">
                        <barcode:datamatrix>
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>
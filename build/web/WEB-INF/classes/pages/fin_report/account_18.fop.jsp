<%@ page import="com.webfin.gl.util.GLUtil,
         com.webfin.gl.ejb.GLReportEngine2,
         com.webfin.gl.report2.form.FinReportForm,
         com.crux.ff.FlexTableManager,
         com.crux.ff.model.FlexTableView,
         com.webfin.gl.model.GLInfoView,
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
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="2cm" margin-bottom="1cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
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

                    glr.setBranch(form.getBranch());
                    glr.setStFlag("Y");

                    HashMap refmap = form.getRptRef();

                    String ftGroup = (String) refmap.get("FT");

                    //DTOList ftl = FlexTableManager.getInstance().getFlexTable2("RKAPPEM");
                    DTOList ftl = FlexTableManager.getInstance().getFlexTable3("RKAPPEM", form.getPeriodTo(), form.getYearFrom());

                    ArrayList cmap = new ArrayList();

                    cmap.add(new Integer(5));
                    cmap.add(new Integer(75));
                    cmap.add(new Integer(35));
                    cmap.add(new Integer(35));
                    cmap.add(new Integer(35));
                    cmap.add(new Integer(15));

                    ArrayList cw = FOPUtil.computeColumnWidth(cmap, 20, 2, "cm");

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
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >EVALUASI PELAKSANAAN RKAP TH. <%=JSPUtil.printX(form.getYearFrom())%></fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >PT. ASURANSI BANGUN ASKRIDA</fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >S/D BULAN <%=JSPUtil.printX(form.getMonthTitleDescription().toUpperCase())%></fo:block>
            <% if (form.getStKeterangan() != null) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >(<%=JSPUtil.printX(form.getStKeteranganDesc())%>)</fo:block>
            <% }%>
            <% if (form.getBranch() != null) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getCostCenter().getStDescription())%></fo:block>
            <% }%>

            <%
                        for (int i = 0; i < ftl.size(); i++) {
                            FlexTableView ft = (FlexTableView) ftl.get(i);

                            String opCode = ft.getStReference2();
                            String desc = ft.getStReference3();

                            boolean isTITLE = "TITLE".equalsIgnoreCase(opCode);

                            if (isTITLE) {
            %>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center"><%=desc%></fo:block>
            <%
                            }
                        }
            %>

            <fo:block font-family="Helvetica" font-weight="bold" >Konsolidasi</fo:block>

            <fo:block font-family="Helvetica" font-size="6pt" >
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
                            <fo:table-cell number-columns-spanned="6">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">NO</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">RKAP <%=JSPUtil.printX(form.getYearFrom())%> KONS.</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(form.getPeriodTitleDescription())%> Konsolidasi</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center" border-right-style="solid"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">Sisa Anggaran Th. <%=JSPUtil.printX(form.getYearFrom())%></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center" border-right-style="solid"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">Tercapai (%)</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="6">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                    </fo:table-header>
                    <fo:table-body>

                        <%
                                    String[] indents = null;

                                    BigDecimal A8111 = new BigDecimal(0);
                                    BigDecimal A8112 = new BigDecimal(0);
                                    BigDecimal A8113 = new BigDecimal(0);
                                    BigDecimal A8114 = new BigDecimal(0);
                                    BigDecimal A81190 = new BigDecimal(0);
                                    BigDecimal A81192 = new BigDecimal(0);
                                    BigDecimal A8190 = new BigDecimal(0);
                                    BigDecimal A8191 = new BigDecimal(0);
                                    BigDecimal promosi = new BigDecimal(0);
                                    BigDecimal sarana = new BigDecimal(0);
                                    BigDecimal dinas = new BigDecimal(0);
                                    BigDecimal A81011Sya = new BigDecimal(0);
                                    BigDecimal A811Sya = new BigDecimal(0);

                                    for (int i = 0; i < ftl.size(); i++) {
                                        FlexTableView ft = (FlexTableView) ftl.get(i);

                                        String style = ft.getStReference1();
                                        String opCode = ft.getStReference2();
                                        String desc = ft.getStReference3();
                                        String acctFrom = ft.getStReference4();
                                        String acctTo = ft.getStReference5();
                                        int iindent = ft.getStReference6() == null ? 0 : Integer.parseInt(ft.getStReference6());
                                        String flags = ft.getStReference7();
                                        BigDecimal kali = ft.getDbReference1();
                                        BigDecimal rkap = ft.getDbReference2();
                                        BigDecimal rkapsya = ft.getDbReference3();
                                        BigDecimal amtsya = ft.getDbReference4();

                                        String indent = (indents != null && indents.length > iindent) ? indents[iindent] : null;

                                        if (style == null) {
                                            style = "";
                                        }

                                        String[] styles = style.split("[\\|]");

                                        style = styles[0];
                                        String style1 = styles.length > 1 ? styles[1] : "";

                                        if (indent != null) {
                                            style += " start-indent=\"" + indent + "\"";
                                        }

                                        if (opCode == null) {
                                            continue;
                                        }

                                        boolean isINDENT = "INDENT".equalsIgnoreCase(opCode);
                                        boolean isACCT = "ACCT".equalsIgnoreCase(opCode);
                                        boolean isACTT = "ACTT".equalsIgnoreCase(opCode);
                                        boolean isDESC = "DESC".equalsIgnoreCase(opCode);
                                        boolean isNL = "NL".equalsIgnoreCase(opCode);
                                        boolean isTOT = "TOT".equalsIgnoreCase(opCode);
                                        boolean isATOT1 = "ATOT1".equalsIgnoreCase(opCode);
                                        boolean isATOT2 = "ATOT2".equalsIgnoreCase(opCode);
                                        boolean isATOT3 = "ATOT3".equalsIgnoreCase(opCode);
                                        boolean isATOT = opCode.indexOf("ATOT") == 0;

                                        if (isATOT) {
                                            A8111 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "8111", "8111", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8112 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "8112", "8112", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8113 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "8113", "8113", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8114 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "8114", "8114", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A81190 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "81190", "81193", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8190 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "8190", "8190", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8191 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "8191", "8191", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            sarana = BDUtil.add(A8111, A8112);
                                            sarana = BDUtil.add(sarana, A8114);
                                            sarana = BDUtil.add(sarana, A81190);
                                            sarana = BDUtil.add(sarana, A8190);

                                            dinas = BDUtil.add(A81192, A8191);

                                            A81011Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "811", "811", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A811Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "811", "814", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            promosi = BDUtil.add(sarana, A8113);
                                            promosi = BDUtil.add(promosi, A81011Sya);
                                        }

                                        if (isINDENT) {
                                            indents = desc.split("[\\|]");
                                        }

                                        if (isACCT) {

                                            if (acctFrom == null) {
                                                continue;
                                            }
                                            if (acctTo == null) {
                                                acctTo = acctFrom;
                                            }

                                            final boolean negative = Tools.isYes(ft.getStReference7());

                                            BigDecimal amtkon = new BigDecimal(0);
                                            if (negative) {
                                                amtkon = BDUtil.sub(amtkon, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                            } else {
                                                amtkon = BDUtil.add(amtkon, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                            }

                                            BigDecimal amt3 = BDUtil.add(amtkon, amtsya);

                                            String amt = JSPUtil.printX(amt3, 0);
                                            if (negative) {
                                                amt = "(" + amt + ")";
                                            }

                                            BigDecimal amount = BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali);

                                            BigDecimal pct3 = BDUtil.div(amt3, rkapsya, 5);

                                            String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                            if (negative) {
                                                pct = "(" + pct + ")";
                                            }

                        %>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amount), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isACTT) {

                                                                    if (acctFrom == null) {
                                                                        continue;
                                                                    }
                                                                    if (acctTo == null) {
                                                                        acctTo = acctFrom;
                                                                    }

                                                                    final boolean negative = Tools.isYes(ft.getStReference7());

                                                                    BigDecimal amtkon = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amtkon = BDUtil.sub(amtkon, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                                                    } else {
                                                                        amtkon = BDUtil.add(amtkon, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                                                    }

                                                                    BigDecimal amt3 = BDUtil.add(amtkon, amtsya);

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkapsya, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amt3), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isNL) {
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <%

                                                                }

                                                                if (isDESC) {
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX(form.getLineNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT1) {

                                                                    final boolean negative = Tools.isYes(ft.getStReference7());

                                                                    BigDecimal amtkon = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amtkon = BDUtil.sub(amtkon, promosi);
                                                                    } else {
                                                                        amtkon = BDUtil.add(amtkon, BDUtil.mul(promosi, kali));
                                                                    }

                                                                    BigDecimal amt3 = BDUtil.add(amtkon, amtsya);

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal amount = BDUtil.mul(amt3, kali);

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkapsya, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amount), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT2) {

                                                                    final boolean negative = Tools.isYes(ft.getStReference7());

                                                                    BigDecimal amtkon = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amtkon = BDUtil.sub(amtkon, sarana);
                                                                    } else {
                                                                        amtkon = BDUtil.add(amtkon, BDUtil.mul(sarana, kali));
                                                                    }

                                                                    BigDecimal amt3 = BDUtil.add(amtkon, amtsya);

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal amount = BDUtil.mul(amt3, kali);

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkapsya, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amount), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT3) {

                                                                    final boolean negative = Tools.isYes(ft.getStReference7());

                                                                    if (acctFrom == null) {
                                                                        continue;
                                                                    }
                                                                    if (acctTo == null) {
                                                                        acctTo = acctFrom;
                                                                    }

                                                                    BigDecimal amtkon = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amtkon = BDUtil.sub(amtkon, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                                                    } else {
                                                                        amtkon = BDUtil.add(amtkon, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                                                    }

                                                                    BigDecimal amt3 = BDUtil.add(amtkon, A811Sya);

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkapsya, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amt3), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                        }

                                    }
                        %>

                    </fo:table-body>
                </fo:table>

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

            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>
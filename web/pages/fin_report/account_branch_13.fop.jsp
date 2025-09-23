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
                               margin-left="2.5cm"
                               margin-right="2.5cm">
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

                    DTOList ftl = FlexTableManager.getInstance().getFlexTable2("RKAPDPT");

                    ArrayList cmap = new ArrayList();

                    cmap.add(new Integer(5));
                    cmap.add(new Integer(75));
                    cmap.add(new Integer(35));
                    //cmap.add(new Integer(35));
                    //cmap.add(new Integer(35));
                    //cmap.add(new Integer(15));

                    ArrayList cw = FOPUtil.computeColumnWidth(cmap, 16, 2, "cm");

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
            <%if (form.getPeriodFrom().equalsIgnoreCase(form.getPeriodTo())) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >BULAN <%=JSPUtil.printX(form.getMonthTitleDescription().toUpperCase())%></fo:block>
            <% } else {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >BULAN <%=JSPUtil.printX(form.getMonthFromTitleDescription().toUpperCase())%> S/D <%=JSPUtil.printX(form.getMonthTitleDescription().toUpperCase())%></fo:block>
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

            <% if (form.getStKeterangan() != null) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >(<%=JSPUtil.printX(form.getStKeteranganDesc())%>)</fo:block>
            <% }%>
            <% if (form.getBranch() != null) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getCostCenter().getStDescription().toUpperCase())%></fo:block>
            <% }%>

            <fo:block font-family="Helvetica" font-weight="bold" >Konvensional</fo:block>

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
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">NO</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
                            <%--<fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">RKAP <%=JSPUtil.printX(form.getYearFrom())%> KONV.</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(form.getPeriodTitleDescription())%> Konvensional</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">Sisa Anggaran Th. <%=JSPUtil.printX(form.getYearFrom())%></fo:block></fo:table-cell>--%>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center" border-right-style="solid"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(form.getPeriodTitleDescription())%> Konvensional</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                    </fo:table-header>
                    <fo:table-body>

                        <%
                                    String[] indents = null;

                                    BigDecimal A6931 = new BigDecimal(0);
                                    BigDecimal A6932 = new BigDecimal(0);
                                    BigDecimal A8911 = new BigDecimal(0);
                                    BigDecimal A8912 = new BigDecimal(0);
                                    BigDecimal A8914 = new BigDecimal(0);
                                    BigDecimal A8917 = new BigDecimal(0);
                                    BigDecimal A8921 = new BigDecimal(0);
                                    BigDecimal A8923 = new BigDecimal(0);
                                    BigDecimal A8925 = new BigDecimal(0);
                                    BigDecimal A8928 = new BigDecimal(0);
                                    BigDecimal A8927 = new BigDecimal(0);
                                    BigDecimal A892327 = new BigDecimal(0);
                                    BigDecimal A51611 = new BigDecimal(0);
                                    BigDecimal A8922 = new BigDecimal(0);
                                    BigDecimal aktiva_tetap = new BigDecimal(0);
                                    BigDecimal lain_lain = new BigDecimal(0);
                                    BigDecimal hasil_lain = new BigDecimal(0);
                                    BigDecimal beban_lain = new BigDecimal(0);
                                    BigDecimal jumlah_lain = new BigDecimal(0);
                                    BigDecimal A90 = new BigDecimal(0);
                                    BigDecimal A91 = new BigDecimal(0);
                                    BigDecimal labaSblmPajak = new BigDecimal(0);

                                    for (int i = 0; i < ftl.size(); i++) {
                                        FlexTableView ft = (FlexTableView) ftl.get(i);

                                        String style = ft.getStReference1();
                                        String opCode = ft.getStReference2();
                                        String desc = ft.getStReference3();
                                        String acctFrom = ft.getStReference4();
                                        String acctTo = ft.getStReference5();
                                        int iindent = ft.getStReference6() == null ? 0 : Integer.parseInt(ft.getStReference6());
                                        String groupType = ft.getStReferenceID1();
                                        String flags = ft.getStReference7();
                                        BigDecimal kali = ft.getDbReference1();
                                        BigDecimal rkap = ft.getDbReference2();

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
                                        boolean isDESC = "DESC".equalsIgnoreCase(opCode);
                                        boolean isACCT = "ACCT".equalsIgnoreCase(opCode);
                                        boolean isACTT = "ACTT".equalsIgnoreCase(opCode);
                                        boolean isATTC = "ATTC".equalsIgnoreCase(opCode);
                                        boolean isNL = "NL".equalsIgnoreCase(opCode);
                                        boolean isTOT = "TOT".equalsIgnoreCase(opCode);
                                        boolean isATOT1 = "ATOT1".equalsIgnoreCase(opCode);
                                        boolean isATOT2 = "ATOT2".equalsIgnoreCase(opCode);
                                        boolean isATOT3 = "ATOT3".equalsIgnoreCase(opCode);
                                        boolean isATOT4 = "ATOT4".equalsIgnoreCase(opCode);
                                        boolean isATOT5 = "ATOT5".equalsIgnoreCase(opCode);
                                        boolean isATOT6 = "ATOT6".equalsIgnoreCase(opCode);
                                        boolean isATOT = opCode.indexOf("ATOT") == 0;

                                        if (isATOT) {
                                            A6931 = BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", "6931", "6931", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A6932 = BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", "6932", "6932", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8911 = BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", "8911", "8911", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8912 = BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", "8912", "8912", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8914 = BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", "8914", "8914", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8917 = BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", "8917", "8917", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8921 = BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", "8921", "8921", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8928 = BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", "8928", "8928", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8925 = BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", "8925", "8925", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8923 = BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", "8923", "8923", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8927 = BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", "8927", "8927", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8922 = BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", "8922", "8922", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A51611 = BDUtil.roundUp(glr.getSummaryRangedOnePeriod("BAL|ADD=0", "51611", "51611", lPeriodTo, lYearFrom, lYearTo));
                                            A90 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A91 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            labaSblmPajak = BDUtil.sub(A51611, A90);
                                            labaSblmPajak = BDUtil.sub(labaSblmPajak, A91);

                                            aktiva_tetap = BDUtil.add(A8912, A8922);

                                            lain_lain = BDUtil.add(A8914, A8923);
                                            lain_lain = BDUtil.add(lain_lain, A8927);

                                            hasil_lain = BDUtil.add(A6931, A6932);
                                            hasil_lain = BDUtil.add(hasil_lain, A8911);
                                            hasil_lain = BDUtil.add(hasil_lain, aktiva_tetap);
                                            //hasil_lain = BDUtil.add(hasil_lain, lain_lain);

                                            beban_lain = BDUtil.add(A8921, A8928);
                                            beban_lain = BDUtil.add(beban_lain, A8925);
                                            beban_lain = BDUtil.add(beban_lain, lain_lain);

                                            jumlah_lain = BDUtil.add(hasil_lain, beban_lain);
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

                                            final boolean negative = Tools.isYes(flags);

                                            BigDecimal amt3 = new BigDecimal(0);
                                            if (negative) {
                                                amt3 = BDUtil.sub(amt3, BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                            } else {
                                                amt3 = BDUtil.add(amt3, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                            }

                                            String amt = JSPUtil.printX(amt3, 0);
                                            if (negative) {
                                                amt = "(" + amt + ")";
                                            }

                                            BigDecimal amount = BDUtil.mul(BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali);

                                            BigDecimal pct3 = BDUtil.div(amt3, rkap, 5);

                                            String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                            if (negative) {
                                                pct = "(" + pct + ")";
                                            }

                        %>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <%--<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, amount), 0)%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATTC) {

                                                                    if (acctFrom == null) {
                                                                        continue;
                                                                    }
                                                                    if (acctTo == null) {
                                                                        acctTo = acctFrom;
                                                                    }

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    String amt = JSPUtil.printX(BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    String rkap2 = JSPUtil.printX(rkap);
                                                                    if (negative) {
                                                                        rkap2 = "(" + rkap2 + ")";
                                                                    }

                                                                    BigDecimal amount = BDUtil.mul(BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali);

                                                                    BigDecimal sisa = BDUtil.sub(rkap, amount);
                                                                    String sisa2 = JSPUtil.printX(sisa, 2);
                                                                    if (negative) {
                                                                        sisa2 = "(" + sisa2 + ")";
                                                                    }

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }

                        %>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <%--<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(sisa2)%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
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

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal amt3 = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amt3 = BDUtil.sub(amt3, BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                                                    } else {
                                                                        amt3 = BDUtil.add(amt3, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                                                    }

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal amount = BDUtil.mul(BDUtil.roundUp(glr.getSummaryRangedBranch("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali);

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <%--<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, amount), 0)%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isDESC) {
                        %>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX(form.getLineNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <%--<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isNL) {
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <%--<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <%
                                                                }

                                                                if (isATOT1) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal amt3 = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amt3 = BDUtil.add(amt3, BDUtil.mul(hasil_lain, kali));
                                                                    } else {
                                                                        amt3 = BDUtil.sub(amt3, hasil_lain);
                                                                    }

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal amount = BDUtil.mul(hasil_lain, kali);

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }

                                                                    /*
                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal amt = BDUtil.div(BDUtil.mul(hasil_lain,kali), rkap);
                                                                    String amt3 = JSPUtil.printX(BDUtil.div(BDUtil.mul(hasil_lain,kali), rkap));
                                                                    if (negative) amt3="("+amt3+")";

                                                                    String pct = JSPUtil.printX(BDUtil.mul(amt,new BigDecimal(100)),2);
                                                                    if (negative) pct="("+pct+")";
                                                                     */
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <%--<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(hasil_lain, kali)), 0)%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT2) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    String rkap2 = JSPUtil.printX(rkap);
                                                                    if (negative) {
                                                                        rkap2 = "(" + rkap2 + ")";
                                                                    }

                                                                    BigDecimal amount = BDUtil.mul(aktiva_tetap, kali);

                                                                    String amt = JSPUtil.printX(amount, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal sisa = BDUtil.sub(rkap, amount);
                                                                    String sisa2 = JSPUtil.printX(sisa, 0);
                                                                    if (negative) {
                                                                        sisa2 = "(" + sisa2 + ")";
                                                                    }

                                                                    BigDecimal pct3 = BDUtil.div(amount, rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <%--<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(sisa2)%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT3) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    String amt = JSPUtil.printX(lain_lain, 0);

                                                                    String rkap2 = JSPUtil.printX(rkap);

                                                                    BigDecimal sisa = BDUtil.sub(rkap, lain_lain);
                                                                    sisa = BDUtil.negate(sisa);
                                                                    String sisa2 = JSPUtil.printX(sisa, 2);
                                                                    if (negative) {
                                                                        sisa2 = "(" + sisa2 + ")";
                                                                    }

                                                                    BigDecimal pct3 = BDUtil.div(lain_lain, rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <%--<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(sisa2)%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT4) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    String rkap2 = JSPUtil.printX(rkap);
                                                                    if (negative) {
                                                                        rkap2 = "(" + rkap2 + ")";
                                                                    }

                                                                    String amt = JSPUtil.printX(beban_lain, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal amount = BDUtil.mul(beban_lain, kali);

                                                                    BigDecimal sisa = BDUtil.sub(rkap, amount);
                                                                    String sisa2 = JSPUtil.printX(sisa, 2);
                                                                    if (negative) {
                                                                        sisa2 = "(" + sisa2 + ")";
                                                                    }

                                                                    BigDecimal pct3 = BDUtil.div(beban_lain, rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <%--<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(sisa2)%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT5) {

                                                                    final boolean negative = Tools.compare(jumlah_lain, BDUtil.zero) < 0;

                                                                    BigDecimal amt3 = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amt3 = BDUtil.negate(BDUtil.add(amt3, jumlah_lain));
                                                                    } else {
                                                                        amt3 = BDUtil.add(amt3, jumlah_lain);
                                                                    }

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (!negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }


                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (!negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <%--<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.add(rkap, amt3), 0)%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT6) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal amt3 = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amt3 = BDUtil.sub(amt3, labaSblmPajak);
                                                                    } else {
                                                                        amt3 = BDUtil.add(amt3, BDUtil.mul(labaSblmPajak, kali));
                                                                    }

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal amount = BDUtil.mul(labaSblmPajak, kali);

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <%--<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amount)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, amount), 0)%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amount)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
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
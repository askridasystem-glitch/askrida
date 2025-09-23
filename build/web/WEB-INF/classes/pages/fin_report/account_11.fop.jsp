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

                    DTOList ftl = FlexTableManager.getInstance().getFlexTable4("RKAPUM", form.getYearFrom());

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
                            <fo:table-cell number-columns-spanned="6">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">NO</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">RKAP <%=JSPUtil.printX(form.getYearFrom())%> KONV.</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(form.getPeriodTitleDescription())%> Konvensional</fo:block></fo:table-cell>
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

                                    BigDecimal A821111 = new BigDecimal(0);
                                    BigDecimal A821113 = new BigDecimal(0);
                                    BigDecimal A82153 = new BigDecimal(0);
                                    BigDecimal A82155 = new BigDecimal(0);
                                    BigDecimal A82157 = new BigDecimal(0);
                                    BigDecimal A821511 = new BigDecimal(0);
                                    BigDecimal A821121 = new BigDecimal(0);
                                    BigDecimal A821124 = new BigDecimal(0);
                                    BigDecimal A821126 = new BigDecimal(0);
                                    BigDecimal A821127 = new BigDecimal(0);
                                    BigDecimal komisaris = new BigDecimal(0);
                                    BigDecimal tht = new BigDecimal(0);
                                    BigDecimal audit = new BigDecimal(0);

                                    BigDecimal A82022 = new BigDecimal(0);
                                    BigDecimal A82140 = new BigDecimal(0);
                                    BigDecimal A82152 = new BigDecimal(0);
                                    BigDecimal A82156 = new BigDecimal(0);
                                    BigDecimal A82158 = new BigDecimal(0);
                                    BigDecimal A82169 = new BigDecimal(0);
                                    BigDecimal gaji = new BigDecimal(0);

                                    BigDecimal A821115 = new BigDecimal(0);
                                    BigDecimal A821125 = new BigDecimal(0);
                                    BigDecimal A821510 = new BigDecimal(0);
                                    BigDecimal A821512 = new BigDecimal(0);
                                    BigDecimal bonus = new BigDecimal(0);
                                    BigDecimal bulan = new BigDecimal(form.getLPeriodTo());

                                    BigDecimal A82159 = new BigDecimal(0);
                                    BigDecimal A82163 = new BigDecimal(0);
                                    BigDecimal A821513 = new BigDecimal(0);
                                    BigDecimal A82130 = new BigDecimal(0);

                                    BigDecimal A82161 = new BigDecimal(0);
                                    BigDecimal A82165 = new BigDecimal(0);
                                    BigDecimal A82190 = new BigDecimal(0);

                                    BigDecimal A82171 = new BigDecimal(0);
                                    BigDecimal A82180 = new BigDecimal(0);
                                    BigDecimal A82174 = new BigDecimal(0);

                                    BigDecimal A82113 = new BigDecimal(0);
                                    BigDecimal A824 = new BigDecimal(0);

                                    BigDecimal A82164 = new BigDecimal(0);
                                    BigDecimal A82166 = new BigDecimal(0);
                                    BigDecimal A8294 = new BigDecimal(0);
                                    BigDecimal A82990 = new BigDecimal(0);

                                    BigDecimal header1 = new BigDecimal(0);
                                    BigDecimal header2 = new BigDecimal(0);
                                    BigDecimal header4 = new BigDecimal(0);
                                    BigDecimal header5 = new BigDecimal(0);
                                    BigDecimal header7 = new BigDecimal(0);
                                    BigDecimal header10 = new BigDecimal(0);

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
                                        boolean isDESC1 = "DESC1".equalsIgnoreCase(opCode);
                                        boolean isACCT = "ACCT".equalsIgnoreCase(opCode);
                                        boolean isACTT = "ACTT".equalsIgnoreCase(opCode);
                                        boolean isPAGE = "PAGE".equalsIgnoreCase(opCode);
                                        boolean isNL = "NL".equalsIgnoreCase(opCode);
                                        boolean isTOT = "TOT".equalsIgnoreCase(opCode);
                                        boolean isATOT1 = "ATOT1".equalsIgnoreCase(opCode);
                                        boolean isATOT2 = "ATOT2".equalsIgnoreCase(opCode);
                                        boolean isATOT3 = "ATOT3".equalsIgnoreCase(opCode);
                                        boolean isATOT4 = "ATOT4".equalsIgnoreCase(opCode);
                                        boolean isATOT5 = "ATOT5".equalsIgnoreCase(opCode);
                                        boolean isHEAD1 = "ATOT6".equalsIgnoreCase(opCode);
                                        boolean isHEAD2 = "ATOT7".equalsIgnoreCase(opCode);
                                        boolean isHEAD3 = "ATOT8".equalsIgnoreCase(opCode);
                                        boolean isHEAD4 = "ATOT9".equalsIgnoreCase(opCode);
                                        boolean isHEAD5 = "ATOT10".equalsIgnoreCase(opCode);
                                        boolean isHEAD6 = "ATOT11".equalsIgnoreCase(opCode);
                                        boolean isHEAD7 = "ATOT12".equalsIgnoreCase(opCode);
                                        boolean isHEAD8 = "ATOT13".equalsIgnoreCase(opCode);
                                        boolean isHEAD9 = "ATOT14".equalsIgnoreCase(opCode);
                                        boolean isHEAD10 = "ATOT15".equalsIgnoreCase(opCode);
                                        boolean isHEAD11 = "ATOT16".equalsIgnoreCase(opCode);
                                        boolean isATOT = opCode.indexOf("ATOT") == 0;

                                        if (isATOT) {

                                            A821111 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "821111", "821113", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82153 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82153", "82153", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82155 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82155", "82155", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82157 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82157", "82157", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A821511 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "821511", "821511", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            A821121 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "821121", "821121", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A821124 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "821124", "821124", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A821126 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "821126", "821126", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A821127 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "821127", "821127", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            komisaris = BDUtil.add(A821121, A821124);
                                            komisaris = BDUtil.add(komisaris, A821126);
                                            komisaris = BDUtil.add(komisaris, A821127);

                                            A821115 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "821115", "821115", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A821125 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "821125", "821125", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            tht = BDUtil.add(A821115, A821125);

                                            audit = BDUtil.mul(new BigDecimal(27500000), bulan);

                                            A82022 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82120", "82126", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82140 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82140", "82140", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82152 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82152", "82152", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82156 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82156", "82156", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82158 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82158", "82158", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            //A82169 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82169", "82169", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            gaji = BDUtil.add(A82022, A82140);
                                            gaji = BDUtil.add(gaji, A82152);
                                            gaji = BDUtil.add(gaji, A82156);
                                            gaji = BDUtil.add(gaji, A82158);
                                            //gaji = BDUtil.add(gaji, A82169);

                                            header1 = BDUtil.add(A821111, A82153);
                                            header1 = BDUtil.add(header1, A82155);
                                            header1 = BDUtil.add(header1, A82157);
                                            header1 = BDUtil.add(header1, A821511);
                                            header1 = BDUtil.add(header1, komisaris);
                                            header1 = BDUtil.add(header1, tht);
                                            header1 = BDUtil.add(header1, gaji);

                                            A821510 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "821510", "821510", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A821512 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "821512", "821512", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            bonus = BDUtil.add(A821510, A821512);

                                            A82159 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82159", "82159", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82163 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82163", "82163", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A821513 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "821513", "821513", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82130 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82130", "82130", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            header2 = BDUtil.add(A82159, A82163);
                                            header2 = BDUtil.add(header2, A821513);
                                            header2 = BDUtil.add(header2, A82130);
                                            header2 = BDUtil.add(header2, bonus);

                                            A82161 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82161", "82162", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82165 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82165", "82165", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82190 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82190", "82190", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            header4 = BDUtil.add(A82161, A82165);
                                            header4 = BDUtil.add(header4, A82190);

                                            A82171 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82171", "82173", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82180 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82180", "82180", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82174 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82174", "82174", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            header5 = BDUtil.add(A82171, A82180);
                                            header5 = BDUtil.add(header5, A82174);

                                            A82113 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82113", "82113", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A824 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "824", "824", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            header7 = BDUtil.add(A82113, A824);

                                            A82164 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82164", "82164", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82166 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82166", "82167", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8294 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "8294", "8296", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82990 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82990", "82990", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            header10 = BDUtil.add(A82164, A82166);
                                            header10 = BDUtil.add(header10, A8294);
                                            header10 = BDUtil.add(header10, A82990);
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
                                                amt3 = BDUtil.sub(amt3, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                            } else {
                                                amt3 = BDUtil.add(amt3, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                            }

                                            String amt = JSPUtil.printX(amt3, 0);
                                            if (negative) {
                                                amt = "(" + amt + ")";
                                            }

                                            BigDecimal amount = BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali);

                                            BigDecimal pct3 = BDUtil.div(amt3, rkap, 5);

                                            String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                            if (negative) {
                                                pct = "(" + pct + ")";
                                            }

                        %>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, amount), 0)%></fo:block></fo:table-cell>
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

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal amt3 = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amt3 = BDUtil.sub(amt3, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                                                    } else {
                                                                        amt3 = BDUtil.add(amt3, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                                                    }

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal amount = BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali);

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkap, 5);

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
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, amount), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
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
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%

                                                                }

                                                                if (isDESC1) {
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>></fo:block></fo:table-cell>
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

                                                                if (isATOT1) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(bonus, kali), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(bonus, kali), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(bonus, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT2) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(audit, kali), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(audit, kali), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(audit, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT3) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(tht, kali), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(tht, kali), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(tht, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT4) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(gaji, kali), rkap, 5);

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
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(gaji, kali), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(gaji, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT5) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    komisaris = BDUtil.sub(komisaris, audit);

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(komisaris, kali), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(komisaris, kali), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(komisaris, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD1) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(header1, kali), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>1</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(header1, kali), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(header1, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD2) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(header2, kali), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>2</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(header2, kali), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(header2, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD3) {

                                                                    if (acctFrom == null) {
                                                                        continue;
                                                                    }
                                                                    if (acctTo == null) {
                                                                        acctTo = acctFrom;
                                                                    }

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal amt3 = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amt3 = BDUtil.sub(amt3, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                                                    } else {
                                                                        amt3 = BDUtil.add(amt3, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                                                    }

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(amt3, kali), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>3</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(amt3, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD4) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(header4, kali), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>4</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(header4, kali), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(header4, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD5) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(header5, kali), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>5</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(header5, kali), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(header5, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD6) {

                                                                    if (acctFrom == null) {
                                                                        continue;
                                                                    }
                                                                    if (acctTo == null) {
                                                                        acctTo = acctFrom;
                                                                    }

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal amt3 = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amt3 = BDUtil.sub(amt3, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                                                    } else {
                                                                        amt3 = BDUtil.add(amt3, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                                                    }

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(amt3, kali), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>6</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(amt3, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD7) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(header7, kali), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>7</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(header7, kali), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(header7, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD8) {

                                                                    if (acctFrom == null) {
                                                                        continue;
                                                                    }
                                                                    if (acctTo == null) {
                                                                        acctTo = acctFrom;
                                                                    }

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal amt3 = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amt3 = BDUtil.sub(amt3, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                                                    } else {
                                                                        amt3 = BDUtil.add(amt3, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                                                    }

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(amt3, kali), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>8</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(amt3, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD9) {

                                                                    if (acctFrom == null) {
                                                                        continue;
                                                                    }
                                                                    if (acctTo == null) {
                                                                        acctTo = acctFrom;
                                                                    }

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal amt3 = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amt3 = BDUtil.sub(amt3, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                                                    } else {
                                                                        amt3 = BDUtil.add(amt3, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                                                    }

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(amt3, kali), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>9</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(amt3, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD10) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(header10, kali), rkap, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>10</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(header10, kali), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, BDUtil.mul(header10, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD11) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    if (acctFrom == null) {
                                                                        continue;
                                                                    }
                                                                    if (acctTo == null) {
                                                                        acctTo = acctFrom;
                                                                    }

                                                                    BigDecimal amt3 = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amt3 = BDUtil.sub(amt3, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                                                    } else {
                                                                        amt3 = BDUtil.add(amt3, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                                                    }

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkap, 5);

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
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, amt3), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isTOT) {

                                                                    if (acctFrom == null) {
                                                                        continue;
                                                                    }
                                                                    if (acctTo == null) {
                                                                        acctTo = acctFrom;
                                                                    }

                                                                    BigDecimal amount = BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali);

                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkap)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amount, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkap, amount), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX(BDUtil.mul(BDUtil.div(amount, rkap), new BigDecimal(100)), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isPAGE) {
                        %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block font-size="6pt" text-align="left" >
                                    Print Stamp : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "hhmmssyyyyMMdd "))%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row break-after="page" <%=style%>>
                            <fo:table-cell />
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
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

                    //DTOList ftl = FlexTableManager.getInstance().getFlexTable2("RKAPUM");
                    DTOList ftl = FlexTableManager.getInstance().getFlexTable3("RKAPUM", form.getPeriodTo(), form.getYearFrom());

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
                                    BigDecimal header8 = new BigDecimal(0);
                                    BigDecimal header9 = new BigDecimal(0);
                                    BigDecimal header10 = new BigDecimal(0);
                                    BigDecimal header11 = new BigDecimal(0);

                                    BigDecimal A821111Sya = new BigDecimal(0);
                                    BigDecimal A821121Sya = new BigDecimal(0);
                                    BigDecimal A821124Sya = new BigDecimal(0);
                                    BigDecimal A821128Sya = new BigDecimal(0);
                                    BigDecimal A821115Sya = new BigDecimal(0);
                                    BigDecimal A82120Sya = new BigDecimal(0);
                                    BigDecimal A82153Sya = new BigDecimal(0);
                                    BigDecimal A82155Sya = new BigDecimal(0);
                                    BigDecimal A82157Sya = new BigDecimal(0);

                                    BigDecimal A82159Sya = new BigDecimal(0);
                                    BigDecimal A82163Sya = new BigDecimal(0);
                                    BigDecimal A821512Sya = new BigDecimal(0);
                                    BigDecimal A82130Sya = new BigDecimal(0);

                                    BigDecimal A82165Sya = new BigDecimal(0);
                                    BigDecimal A82161Sya = new BigDecimal(0);
                                    BigDecimal A82190Sya = new BigDecimal(0);

                                    BigDecimal A8217Sya = new BigDecimal(0);
                                    BigDecimal A824Sya = new BigDecimal(0);
                                    BigDecimal A8260Sya = new BigDecimal(0);
                                    BigDecimal A8263Sya = new BigDecimal(0);
                                    BigDecimal A829Sya = new BigDecimal(0);

                                    BigDecimal A82164Sya = new BigDecimal(0);
                                    BigDecimal A82166Sya = new BigDecimal(0);
                                    BigDecimal A82950Sya = new BigDecimal(0);
                                    BigDecimal A82960Sya = new BigDecimal(0);
                                    BigDecimal A8290Sya = new BigDecimal(0);

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

                                            A82022 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82120", "82122", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82140 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82140", "82140", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82152 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82152", "82152", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82156 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82156", "82156", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82158 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82158", "82158", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82169 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "82169", "82169", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            gaji = BDUtil.add(A82022, A82140);
                                            gaji = BDUtil.add(gaji, A82152);
                                            gaji = BDUtil.add(gaji, A82156);
                                            gaji = BDUtil.add(gaji, A82158);
                                            gaji = BDUtil.add(gaji, A82169);

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

                                            A821111Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "821111", "821111", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A821115Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "821115", "821115", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A821121Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "821121", "821121", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A821124Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "821124", "821124", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A821128Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "821128", "821128", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82120Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "82120", "82120", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82153Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "82153", "82153", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82155Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "82155", "82155", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82157Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "82157", "82157", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            header1 = BDUtil.add(header1, A821111Sya);
                                            header1 = BDUtil.add(header1, A821115Sya);
                                            header1 = BDUtil.add(header1, A821121Sya);
                                            header1 = BDUtil.add(header1, A821124Sya);
                                            header1 = BDUtil.add(header1, A821128Sya);
                                            header1 = BDUtil.add(header1, A82120Sya);
                                            header1 = BDUtil.add(header1, A82153Sya);
                                            header1 = BDUtil.add(header1, A82155Sya);
                                            header1 = BDUtil.add(header1, A82157Sya);

                                            A82159Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "82159", "82159", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82163Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "82163", "82163", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A821512Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "821512", "821513", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82130Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "82130", "82130", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            header2 = BDUtil.add(header2, A82159Sya);
                                            header2 = BDUtil.add(header2, A82163Sya);
                                            header2 = BDUtil.add(header2, A821512Sya);
                                            header2 = BDUtil.add(header2, A82130Sya);

                                            A82165Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "821650", "821651", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82161Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "82161", "82161", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82190Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "82190", "82190", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            header4 = BDUtil.add(header4, A82165Sya);
                                            header4 = BDUtil.add(header4, A82161Sya);
                                            header4 = BDUtil.add(header4, A82190Sya);

                                            A8217Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "8217", "8218", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            header5 = BDUtil.add(header5, A8217Sya);

                                            A824Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "8241", "8242", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            header7 = BDUtil.add(header7, A824Sya);

                                            A8260Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "8260", "8260", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8263Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "8263", "8263", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            header8 = BDUtil.add(header8, A8260Sya);
                                            header8 = BDUtil.add(header8, A8263Sya);

                                            header9 = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "8291", "8292", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            A82164Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "82164", "82164", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82166Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "82166", "82167", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82950Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "82950", "82950", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A82960Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "82960", "82960", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                            A8290Sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "82990", "82990", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                                            header10 = BDUtil.add(header10, A82164Sya);
                                            header10 = BDUtil.add(header10, A82166Sya);
                                            header10 = BDUtil.add(header10, A82950Sya);
                                            header10 = BDUtil.add(header10, A82960Sya);
                                            header10 = BDUtil.add(header10, A8290Sya);

                                            header11 = BDUtil.roundUp(glr.getSummaryRangedExcludedSyariah("BAL|ADD=0", "82", "82", "8290", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

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
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
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

                                                                    final boolean negative = Tools.isYes(flags);

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
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amount), 0)%></fo:block></fo:table-cell>
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

                                                                    //bonus = BDUtil.sub(bonus, audit);

                                                                    BigDecimal amt3 = BDUtil.add(BDUtil.mul(bonus, kali), amtsya);

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
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amt3), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT2) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal amt3 = BDUtil.add(BDUtil.mul(audit, kali), amtsya);

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
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amt3), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT3) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal amt3 = BDUtil.add(BDUtil.mul(tht, kali), amtsya);

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
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amt3), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT4) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal amt3 = BDUtil.add(BDUtil.mul(gaji, kali), amtsya);

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
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amt3), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isATOT5) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    komisaris = BDUtil.sub(komisaris, audit);

                                                                    BigDecimal amt3 = BDUtil.add(BDUtil.mul(komisaris, kali), amtsya);

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
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amt3), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD1) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    //bonus = BDUtil.sub(bonus, audit);

                                                                    BigDecimal amt3 = BDUtil.mul(header1, kali);

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkapsya, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>1</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amt3), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD2) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    //bonus = BDUtil.sub(bonus, audit);
                                                                    BigDecimal amt3 = BDUtil.mul(header2, kali);

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkapsya, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>2</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amt3), 0)%></fo:block></fo:table-cell>
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

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(amt3, kali), rkapsya, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>3</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, BDUtil.mul(amt3, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD4) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    //bonus = BDUtil.sub(bonus, audit);
                                                                    BigDecimal amt3 = BDUtil.mul(header4, kali);

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkapsya, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>4</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amt3), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD5) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    //bonus = BDUtil.sub(bonus, audit);

                                                                    BigDecimal amt3 = BDUtil.mul(header5, kali);

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkapsya, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>5</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amt3), 0)%></fo:block></fo:table-cell>
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

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(amt3, kali), rkapsya, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>6</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, BDUtil.mul(amt3, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD7) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    //bonus = BDUtil.sub(bonus, audit);

                                                                    BigDecimal amt3 = BDUtil.mul(header7, kali);

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkapsya, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>7</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amt3), 0)%></fo:block></fo:table-cell>
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

                                                                    BigDecimal amtkon = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amtkon = BDUtil.sub(amtkon, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                                                    } else {
                                                                        amtkon = BDUtil.add(amtkon, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                                                    }

                                                                    BigDecimal amt3 = BDUtil.add(amtkon, A8260Sya);
                                                                    amt3 = BDUtil.add(amt3, A8263Sya);

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(amt3, kali), rkapsya, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>8</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, BDUtil.mul(amt3, kali)), 0)%></fo:block></fo:table-cell>
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

                                                                    BigDecimal amtkon = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amtkon = BDUtil.sub(amtkon, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                                                    } else {
                                                                        amtkon = BDUtil.add(amtkon, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                                                    }

                                                                    BigDecimal amt3 = BDUtil.add(amtkon, header9);

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(amt3, kali), rkapsya, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>9</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, BDUtil.mul(amt3, kali)), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                }

                                                                if (isHEAD10) {

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    //bonus = BDUtil.sub(bonus, audit);

                                                                    BigDecimal amt3 = BDUtil.mul(header10, kali);

                                                                    BigDecimal pct3 = BDUtil.div(amt3, rkapsya, 5);

                                                                    String pct = JSPUtil.printX(BDUtil.mul(pct3, new BigDecimal(100)), 2);
                                                                    if (negative) {
                                                                        pct = "(" + pct + ")";
                                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>10</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.sub(rkapsya, amt3), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(pct)%></fo:block></fo:table-cell>
                        </fo:table-row>



                        <%
                                                                }

                                                                if (isHEAD11) {

                                                                    if (acctFrom == null) {
                                                                        continue;
                                                                    }
                                                                    if (acctTo == null) {
                                                                        acctTo = acctFrom;
                                                                    }

                                                                    final boolean negative = Tools.isYes(flags);

                                                                    BigDecimal amtkon = new BigDecimal(0);
                                                                    if (negative) {
                                                                        amtkon = BDUtil.sub(amtkon, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                                                                    } else {
                                                                        amtkon = BDUtil.add(amtkon, BDUtil.mul(BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali));
                                                                    }

                                                                    BigDecimal amt3 = BDUtil.add(amtkon, header11);

                                                                    String amt = JSPUtil.printX(amt3, 0);
                                                                    if (negative) {
                                                                        amt = "(" + amt + ")";
                                                                    }

                                                                    BigDecimal pct3 = BDUtil.div(BDUtil.mul(amt3, kali), rkapsya, 5);

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
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>>10</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(rkapsya)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(amt3, 0)%></fo:block></fo:table-cell>
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

                        <fo:table-row <%=style%>>
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
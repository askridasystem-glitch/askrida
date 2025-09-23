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
                               page-height="31cm"
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
        long lYearTo = form.getLYearTo();
        
        glr.setBranch(form.getBranch());
        glr.setStFlag(form.getStFlag());
        
        HashMap refmap = form.getRptRef();
        
        String ftGroup = (String) refmap.get("FT");
        
        DTOList ftl = FlexTableManager.getInstance().getFlexTable(ftGroup);
        
        ArrayList cmap = new ArrayList();
        
        cmap.add(new Integer(5));
        cmap.add(new Integer(90));
        cmap.add(new Integer(35));
        cmap.add(new Integer(35));
        cmap.add(new Integer(35));
        
        ArrayList cw = FOPUtil.computeColumnWidth(cmap,20,2,"cm");
        
        %>
        
        <fo:flow flow-name="xsl-region-body">
            
            <%
            String bw = "0.5pt";
            %>
            
            <%
            for (int i = 0; i < ftl.size(); i++) {
                FlexTableView ft = (FlexTableView) ftl.get(i);
                
                String opCode = ft.getStReference2();
                String desc = ft.getStReference3();
                
                boolean isTITLE = "TITLE".equalsIgnoreCase(opCode);
                
                if (isTITLE) {
            %>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm"><%=desc%></fo:block>
            <%
                }
                
            }
            %>
            
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getPeriodTitleDescription())%></fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getBranchDesc())%></fo:block>
            <fo:block space-after.optimum="14pt"/>
            
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
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block font-size="8pt" text-align="end" font-style="italic">(Dalam Rupiah)</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">NO</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" ><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">SALDO per <%=JSPUtil.printX(form.getMonthBeforeTitleDescription().toUpperCase())%></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" ><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">MUTASI <%=JSPUtil.printX(form.getMonthTitleDescription().toUpperCase())%></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" border-right-style="solid"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">SALDO s/d <%=JSPUtil.printX(form.getMonthTitleDescription().toUpperCase())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                    </fo:table-header>
                    <fo:table-body>
                        
                        <%
                        String[] indents=null;
                        
                        BigDecimal atot1 = new BigDecimal(0);
                        BigDecimal atot2 = new BigDecimal(0);
                        BigDecimal atot3 = new BigDecimal(0);
                        BigDecimal atot4 = new BigDecimal(0);
                        BigDecimal atot5 = new BigDecimal(0);
                        BigDecimal atot6 = new BigDecimal(0);
                        BigDecimal atot7 = new BigDecimal(0);
                        BigDecimal atot8 = new BigDecimal(0);
                        BigDecimal atot9 = new BigDecimal(0);
                        BigDecimal atot10 = new BigDecimal(0);
                        BigDecimal atot11 = new BigDecimal(0);
                        BigDecimal atot12 = new BigDecimal(0);
                        BigDecimal atot13 = new BigDecimal(0);
                        BigDecimal atot14 = new BigDecimal(0);
                        BigDecimal atot15 = new BigDecimal(0);
                        BigDecimal akun69A = new BigDecimal(0);
                        BigDecimal akun69B = new BigDecimal(0);
                        BigDecimal akun69C = new BigDecimal(0);
                        BigDecimal akun8911A = new BigDecimal(0);
                        BigDecimal akun8911B = new BigDecimal(0);
                        BigDecimal akun8911C = new BigDecimal(0);
                        BigDecimal akun8921A = new BigDecimal(0);
                        BigDecimal akun8921B = new BigDecimal(0);
                        BigDecimal akun8921C = new BigDecimal(0);
                        BigDecimal akun65A = new BigDecimal(0);
                        BigDecimal akun65B = new BigDecimal(0);
                        BigDecimal akun65C = new BigDecimal(0);
                        BigDecimal akun8183A = new BigDecimal(0);
                        BigDecimal akun8183B = new BigDecimal(0);
                        BigDecimal akun8183C = new BigDecimal(0);
                        BigDecimal akun811A = new BigDecimal(0);
                        BigDecimal akun811B = new BigDecimal(0);
                        BigDecimal akun811C = new BigDecimal(0);
                        
                        for (int i = 0; i < ftl.size(); i++) {
                            FlexTableView ft = (FlexTableView) ftl.get(i);
                            
                            String style = ft.getStReference1();
                            String opCode = ft.getStReference2();
                            String desc = ft.getStReference3();
                            String acctFrom = ft.getStReference4();
                            String acctTo = ft.getStReference5();
                            int iindent = ft.getStReference6()==null?0:Integer.parseInt(ft.getStReference6());
                            String groupType = ft.getStReferenceID1();
                            String flags = ft.getStReference7();
                            BigDecimal kali = ft.getDbReference1();
                            
                            String indent=(indents!=null && indents.length>iindent)?indents[iindent]:null;
                            
                            if (style==null) style="";
                            
                            String[] styles = style.split("[\\|]");
                            
                            style=styles[0];
                            String style1 = styles.length>1?styles[1]:"";
                            
                            if (indent!=null) style+=" start-indent=\""+indent+"\"";
                            
                            if (opCode==null ) continue;
                            
                            boolean isINDENT = "INDENT".equalsIgnoreCase(opCode);
                            boolean isDESC = "DESC".equalsIgnoreCase(opCode);
                            boolean isDESC1 = "DESC1".equalsIgnoreCase(opCode);
                            boolean isACCT = "ACCT".equalsIgnoreCase(opCode);
                            boolean isACTT = "ACTT".equalsIgnoreCase(opCode);
                            boolean isNL = "NL".equalsIgnoreCase(opCode);
                            boolean isPAGE = "PAGE".equalsIgnoreCase(opCode);
                            boolean isTOT = "TOT".equalsIgnoreCase(opCode);
                            boolean isATOT1 = "ATOT1".equalsIgnoreCase(opCode);
                            boolean isATOT2 = "ATOT2".equalsIgnoreCase(opCode);
                            boolean isATOT3 = "ATOT3".equalsIgnoreCase(opCode);
                            boolean isATOT4 = "ATOT4".equalsIgnoreCase(opCode);
                            boolean isATOT5 = "ATOT5".equalsIgnoreCase(opCode);
                            boolean isATOT6 = "ATOT6".equalsIgnoreCase(opCode);
                            boolean isATOT7 = "ATOT7".equalsIgnoreCase(opCode);
                            boolean isATOT8 = "ATOT8".equalsIgnoreCase(opCode);
                            boolean isATOT = opCode.indexOf("ATOT")==0;
                            
                            if (isATOT) {
                                atot1 = BDUtil.add(glr.getSummaryRanged("BAL|ADD=0","61","64",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo), glr.getSummaryRanged("BAL|ADD=0","71","79",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo));
                                atot2 = BDUtil.add(glr.getSummaryRangedOnePeriod("BAL|ADD=0","61","64",lPeriodTo,lYearFrom,lYearTo), glr.getSummaryRangedOnePeriod("BAL|ADD=0","71","79",lPeriodTo,lYearFrom,lYearTo));
                                atot3 = BDUtil.add(glr.getSummaryRanged("BAL|ADD=0","61","64",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo), glr.getSummaryRanged("BAL|ADD=0","71","79",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
                                atot4 = glr.getSummaryRanged("BAL|ADD=0","65","65",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo);
                                atot5 = glr.getSummaryRangedOnePeriod("BAL|ADD=0","65","65",lPeriodTo,lYearFrom,lYearTo);
                                atot6 = glr.getSummaryRanged("BAL|ADD=0","65","65",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
                                atot7 = glr.getSummaryRanged("BAL|ADD=0","81","83",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo);
                                atot8 = glr.getSummaryRangedOnePeriod("BAL|ADD=0","81","83",lPeriodTo,lYearFrom,lYearTo);
                                atot9 = glr.getSummaryRanged("BAL|ADD=0","81","83",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
                                atot10 = BDUtil.add(glr.getSummaryRanged("BAL|ADD=1","69","69",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo), glr.getSummaryRanged("BAL|ADD=1","89","89",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo));
                                atot11 = BDUtil.add(glr.getSummaryRangedOnePeriod("BAL|ADD=1","69","69",lPeriodTo,lYearFrom,lYearTo), glr.getSummaryRangedOnePeriod("BAL|ADD=1","89","89",lPeriodTo,lYearFrom,lYearTo));
                                atot12 = BDUtil.add(glr.getSummaryRanged("BAL|ADD=1","69","69",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo), glr.getSummaryRanged("BAL|ADD=1","89","89",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
                                atot13 = BDUtil.add(glr.getSummaryRanged("BAL|ADD=0","90","90",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo), glr.getSummaryRanged("BAL|ADD=0","91","91",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo));
                                atot14 = BDUtil.add(glr.getSummaryRangedOnePeriod("BAL|ADD=0","90","90",lPeriodTo,lYearFrom,lYearTo), glr.getSummaryRangedOnePeriod("BAL|ADD=0","91","91",lPeriodTo,lYearFrom,lYearTo));
                                atot15 = BDUtil.add(glr.getSummaryRanged("BAL|ADD=0","90","90",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo), glr.getSummaryRanged("BAL|ADD=0","91","91",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
                                akun69A =glr.getSummaryRanged("BAL|ADD=0","69","69",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo);
                                akun69B =glr.getSummaryRangedOnePeriod("BAL|ADD=0","69","69",lPeriodTo,lYearFrom,lYearTo);
                                akun69C =glr.getSummaryRanged("BAL|ADD=0","69","69",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
                                akun8911A =glr.getSummaryRanged("BAL|ADD=0","8911","8911",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo);
                                akun8911B =glr.getSummaryRangedOnePeriod("BAL|ADD=0","8911","8911",lPeriodTo,lYearFrom,lYearTo);
                                akun8911C =glr.getSummaryRanged("BAL|ADD=0","8911","8911",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
                                akun8921A =glr.getSummaryRanged("BAL|ADD=0","8921","8921",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo);
                                akun8921B =glr.getSummaryRangedOnePeriod("BAL|ADD=0","8921","8921",lPeriodTo,lYearFrom,lYearTo);
                                akun8921C =glr.getSummaryRanged("BAL|ADD=0","8921","8921",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);                                
                                akun65A = BDUtil.sub(glr.getSummaryRanged("BAL|ADD=0","65","65",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo), glr.getSummaryRanged("BAL|ADD=0","65111","65112",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo));
                                akun65B = BDUtil.sub(glr.getSummaryRangedOnePeriod("BAL|ADD=0","65","65",lPeriodTo,lYearFrom,lYearTo), glr.getSummaryRangedOnePeriod("BAL|ADD=0","65111","65112",lPeriodTo,lYearFrom,lYearTo));
                                akun65C = BDUtil.sub(glr.getSummaryRanged("BAL|ADD=0","65","65",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo), glr.getSummaryRanged("BAL|ADD=0","65111","65112",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
                                akun8183A = glr.getSummaryRanged("BAL|ADD=0","81","83",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo);
                                akun8183B = glr.getSummaryRangedOnePeriod("BAL|ADD=0","81","83",lPeriodTo,lYearFrom,lYearTo);
                                akun8183C = glr.getSummaryRanged("BAL|ADD=0","81","83",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
                                akun811A = BDUtil.add(glr.getSummaryRanged("BAL|ADD=0","811","829",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo), glr.getSummaryRanged("BAL|ADD=0","833","834",lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo));
                                akun811B = BDUtil.add(glr.getSummaryRangedOnePeriod("BAL|ADD=0","811","829",lPeriodTo,lYearFrom,lYearTo), glr.getSummaryRangedOnePeriod("BAL|ADD=0","833","834",lPeriodTo,lYearFrom,lYearTo));
                                akun811C = BDUtil.add(glr.getSummaryRanged("BAL|ADD=0","811","829",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo), glr.getSummaryRanged("BAL|ADD=0","833","834",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
                            }
                            
                            if (isINDENT) {
                                indents = desc.split("[\\|]");
                            }
                            
                            if (isACCT) {
                                
                                if (acctFrom == null) continue;
                                if (acctTo == null) acctTo = acctFrom;
                        
                        %>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedWithPolType("BAL|ADD=0",acctFrom,acctTo,groupType,lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo), kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedWithPolTypeOnePeriod("BAL|ADD=0",acctFrom,acctTo,groupType,lPeriodTo,lYearFrom,lYearTo), kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedWithPolType("BAL|ADD=0",acctFrom,acctTo,groupType,lPeriodFrom,lPeriodTo,lYearFrom,lYearTo), kali),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <%                        
                            }
                            
                            if (isACTT) {
                                
                                if (acctFrom == null) continue;
                                if (acctTo == null) acctTo = acctFrom;
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0",acctFrom,acctTo,lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo), kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedOnePeriod("BAL|ADD=0",acctFrom,acctTo,lPeriodTo,lYearFrom,lYearTo), kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0",acctFrom,acctTo,lPeriodFrom,lPeriodTo,lYearFrom,lYearTo), kali),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
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
                        </fo:table-row>
                        
                        <%
                            }
                            
                            if (isPAGE) {
                        %>
                        
                        <fo:table-row <%=style%>>
                            <fo:table-cell />
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
                        </fo:table-row> 
                        
                        <%                        
                            }
                            
                            if (isTOT) {
                                
                                if (acctFrom == null) continue;
                                if (acctTo == null) acctTo = acctFrom;
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0",acctFrom,acctTo,lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo), kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedOnePeriod("BAL|ADD=0",acctFrom,acctTo,lPeriodTo,lYearFrom,lYearTo), kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0",acctFrom,acctTo,lPeriodFrom,lPeriodTo,lYearFrom,lYearTo), kali),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <%                        
                            }
                            
                            if (isATOT1) {
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atot1, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atot2, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atot3, kali),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <%                        
                            }
                            
                            if (isATOT2) {
                                
                                BigDecimal atotA = akun65A;
                                BigDecimal atotB = akun65B;;
                                BigDecimal atotC = akun65C;;
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotA, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotB, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotC, kali),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <%                        
                            }
                            
                            if (isATOT3) {
                                
                                BigDecimal atotA = BDUtil.sub(akun8183A, akun811A);
                                BigDecimal atotB = BDUtil.sub(akun8183B, akun811B);
                                BigDecimal atotC = BDUtil.sub(akun8183C, akun811C);
                        
                        %>
                                                
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotA, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotB, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotC, kali),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <%                        
                            }
                            
                            if (isATOT4) {
                                
                                BigDecimal atotA = BDUtil.add(BDUtil.add(atot1, atot4),atot7);
                                BigDecimal atotB = BDUtil.add(BDUtil.add(atot2, atot5),atot8);
                                BigDecimal atotC = BDUtil.add(BDUtil.add(atot3, atot6),atot9);
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotA, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotB, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotC, kali),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <%                        
                            }
                            
                            if (isATOT5) {
                                
                                BigDecimal atotA = BDUtil.add(akun69A, BDUtil.add(akun8911A,akun8921A));
                                atotA = BDUtil.sub(atotA, atot10);
                                BigDecimal atotB = BDUtil.add(akun69B, BDUtil.add(akun8911B,akun8921B));
                                atotB = BDUtil.sub(atotB, atot11);
                                BigDecimal atotC = BDUtil.add(akun69C, BDUtil.add(akun8911C,akun8921C));
                                atotC = BDUtil.sub(atotC, atot12);
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotA, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotB, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotC, kali),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <%                        
                            }
                            
                            if (isATOT6) {
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atot10, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atot11, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atot12, kali),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <%                        
                            }
                            
                            if (isATOT7) {
                                
                                BigDecimal atotA = BDUtil.add(atot1,BDUtil.add(atot4, atot7));
                                atotA = BDUtil.add(atotA, atot10);
                                BigDecimal atotB = BDUtil.add(atot2,BDUtil.add(atot5, atot8));
                                atotB = BDUtil.add(atotB, atot11);
                                BigDecimal atotC = BDUtil.add(atot3,BDUtil.add(atot6, atot9));
                                atotC = BDUtil.add(atotC, atot12);
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotA, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotB, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotC, kali),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <%                        
                            }
                            
                            if (isATOT8) {
                                
                                BigDecimal atotA = BDUtil.add(atot1,BDUtil.add(atot4, atot7));
                                atotA = BDUtil.add(atotA, atot10);
                                atotA = BDUtil.add(atotA, atot13);
                                BigDecimal atotB = BDUtil.add(atot2,BDUtil.add(atot5, atot8));
                                atotB = BDUtil.add(atotB, atot11);
                                atotB = BDUtil.add(atotB, atot14);
                                BigDecimal atotC = BDUtil.add(atot3,BDUtil.add(atot6, atot9));
                                atotC = BDUtil.add(atotC, atot12);
                                atotC = BDUtil.add(atotC, atot15);                             
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotA, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotB, kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotC, kali),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
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
                    Print Stamp : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"hhmmssyyyyMMdd "))%>                    
                </fo:block>
                
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="90mm"/>
                    <fo:table-column column-width="90mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" >
                                <fo:block text-align="center">S.E. &amp; O.</fo:block>
                                <fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">DIREKSI</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row height="20mm"></fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
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
            </fo:block>
        </fo:flow>
    </fo:page-sequence>
</fo:root>
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
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center"><%=desc%></fo:block>
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
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0",acctFrom,acctTo,lPeriodFrom,lPeriodTo-1,lYearFrom,lYearTo), kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedOnePeriod("BAL|ADD=0",acctFrom,acctTo,lPeriodTo,lYearFrom,lYearTo), kali),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0",acctFrom,acctTo,lPeriodFrom,lPeriodTo,lYearFrom,lYearTo), kali),2)%></fo:block></fo:table-cell>
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
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
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
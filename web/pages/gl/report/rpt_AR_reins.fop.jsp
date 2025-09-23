<%@ page import="com.webfin.gl.form.GLListForm,
com.crux.web.controller.SessionManager,
java.util.ArrayList,
com.crux.util.fop.FOPUtil,
com.crux.util.SQLAssembler,
com.webfin.gl.model.JournalView,
com.crux.util.DTOList,
com.crux.util.BDUtil,
com.crux.util.JSPUtil,
com.crux.util.DateUtil,
java.math.BigDecimal,
com.crux.lang.LanguageManager,
java.util.Date"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="30cm"
                               page-width="23cm"
                               margin-top="2.5cm"
                               margin-bottom="0.5cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="1cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>
    
    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">
        
        <%
        final DTOList l = (DTOList)request.getAttribute("RPT");
        
        final GLListForm form = (GLListForm)SessionManager.getInstance().getCurrentForm();
        
        %>
        
        <fo:flow flow-name="xsl-region-body">
            
            <%
            String bw = "0.5pt";
            %>  
            
            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="30mm"/><!-- Entry Date -->
                    <fo:table-column column-width="25mm"/><!-- Policy Date--> 
                    <fo:table-column column-width="25mm"/><!-- Policy No -->
                    <fo:table-column column-width="25mm"/><!-- Policy No -->
                    <fo:table-column column-width="25mm"/><!-- Policy No --> 
                    <fo:table-column column-width="25mm"/><!-- Policy No -->
                    <fo:table-column column-width="25mm"/><!-- Policy No -->
                    <fo:table-column column-width="25mm"/><!-- Policy No -->
                    <fo:table-header>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">             
                                    <fo:inline text-decoration="underline">PIUTANG REASURANSI</fo:inline>   
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-family="serif" font-weight="bold" font-size="12pt" text-align="center">             
                                    <%=JSPUtil.printX(form.getPeriodTitleDescription().toUpperCase())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-family="serif" font-weight="bold" font-size="12pt">             
                                    <% if (form.getBranchDesc()!=null) { %>
                                    {L-ENG Branch-L}{L-INA Cabang-L} : <%=JSPUtil.printX(form.getBranchDesc())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="9" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" number-rows-spanned="2" display-align="center"><fo:block text-align="center">{L-ENG NO. -L}{L-INA NO. -L}</fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" number-rows-spanned="2" display-align="center"><fo:block text-align="center">{L-ENG REASURADUR -L}{L-INA REASURADUR -L}</fo:block></fo:table-cell><!-- Entry Date --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" number-columns-spanned="3" ><fo:block text-align="center">{L-ENG PREMIUM -L}{L-INA PREMI -L}</fo:block></fo:table-cell><!-- Police Date --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" number-columns-spanned="3" ><fo:block text-align="center">{L-ENG CLAIM -L}{L-INA KLAIM -L}</fo:block></fo:table-cell><!-- Police Date --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" padding="2pt" number-rows-spanned="2" display-align="center" ><fo:block text-align="center">{L-ENG TOTAL -L}{L-INA JUMLAH -L}</fo:block></fo:table-cell><!-- Policy No. --> 
                        </fo:table-row>   
                        
                        <fo:table-row> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" padding="2pt" ><fo:block text-align="center">TREATY</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" padding="2pt" ><fo:block text-align="center">FACULTATIF</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" padding="2pt" ><fo:block text-align="center">X/L</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" padding="2pt" ><fo:block text-align="center">CASH LOSS</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" padding="2pt" ><fo:block text-align="center">FACULTATIF</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-right-style="solid" padding="2pt" ><fo:block text-align="center">X/L</fo:block></fo:table-cell> 
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="9" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <%
                        int counter = 50;
                        BigDecimal subTotalPremiTreaty = null;
                        BigDecimal subTotalPremiFac = null;
                        BigDecimal subTotalPremiXL = null;
                        BigDecimal subTotalClaimCash = null;
                        BigDecimal subTotalClaimFac = null;
                        BigDecimal subTotalClaimXL = null;
                        BigDecimal subTotalDebit = null;
                        
                        BigDecimal [] t = new BigDecimal[7];
                        
                        for (int i = 0; i < l.size(); i++) {
                            JournalView pol = (JournalView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiTreaty());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiFac());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiXL());
                            t[n] = BDUtil.add(t[n++], pol.getDbClaimCash());
                            t[n] = BDUtil.add(t[n++], pol.getDbClaimFac());
                            t[n] = BDUtil.add(t[n++], pol.getDbClaimXL());
                            t[n] = BDUtil.add(t[n++], pol.getDbDebit());
                            
                            if (i==counter) {
                                counter = counter + 50;
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" number-columns-spanned="2"><fo:block text-align="center">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(subTotalPremiTreaty,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(subTotalPremiFac,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(subTotalPremiXL,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(subTotalClaimCash,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(subTotalClaimFac,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(subTotalClaimXL,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(subTotalDebit,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        
                        <% } %>
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block ><%=JSPUtil.printX(pol.getStTransactionNo())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTreaty(),0)%></fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiFac(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiXL(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimCash(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimFac(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimXL(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbDebit(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                        </fo:table-row>
                        
                        <% 
                        
                        subTotalPremiTreaty = BDUtil.add(subTotalPremiTreaty,pol.getDbPremiTreaty());
                        subTotalPremiFac = BDUtil.add(subTotalPremiFac,pol.getDbPremiFac());
                        subTotalPremiXL = BDUtil.add(subTotalPremiXL,pol.getDbPremiXL());
                        subTotalClaimCash = BDUtil.add(subTotalClaimCash,pol.getDbClaimCash());
                        subTotalClaimFac = BDUtil.add(subTotalClaimFac,pol.getDbClaimFac());
                        subTotalClaimXL = BDUtil.add(subTotalClaimXL,pol.getDbClaimXL());
                        subTotalDebit = BDUtil.add(subTotalDebit,pol.getDbDebit());
                        
                        } %>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="9">  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center"> TOTAL </fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(t[0],0)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(t[1],0)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(t[2],0)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(t[3],0)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(t[4],0)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(t[5],0)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(t[6],0)%></fo:block></fo:table-cell><!-- Total Premi --> 
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="9" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-after.optimum="3pt"></fo:block>   
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
                Print Stamp : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"hhmmssyyyyMMdd "))%>                    
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>


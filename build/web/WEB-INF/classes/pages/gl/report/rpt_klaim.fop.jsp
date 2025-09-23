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
                               page-width="21cm"
                               margin-top="3cm"
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
                    <fo:table-column column-width="100mm"/><!-- Entry Date -->
                    <fo:table-column column-width="40mm"/><!-- Policy Date--> 
                    <fo:table-column /><!-- The Insured -->
                    <fo:table-header>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">             
                                    <fo:inline text-decoration="underline">HUTANG KLAIM</fo:inline>   
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block font-family="serif" font-weight="bold" font-size="12pt" text-align="center">             
                                    <%=JSPUtil.printX(form.getPeriodTitleDescription())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block font-family="serif" font-weight="bold" font-size="12pt">             
                                    <% if (form.getBranchDesc()!=null) { %>
                                    {L-ENG Branch-L}{L-INA Cabang-L} : <%=JSPUtil.printX(form.getBranchDesc())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="4" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Description -L}{L-INA Keterangan-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Rekening-L}{L-INA Rekening-L}</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" border-right-style="solid"><fo:block text-align="center">{L-ENG Amount-L}{L-INA Jumlah-L}</fo:block></fo:table-cell>  
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="4" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <%
                        int counter = 45;
                        BigDecimal [] t = new BigDecimal[1];
                        
                        for (int i = 0; i < l.size(); i++) {
                            JournalView pol = (JournalView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbBalance());
                            
                            if (i==counter) {
                                counter = counter + 45;
                        
                        %>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="4" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        
                        <% } %>
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block ><%=JSPUtil.printX(pol.getStTransactionNo())%> <%=pol.getCostCenter().getStDescription().toUpperCase()%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStHeaderAccountNo())%></fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" border-right-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.negate(pol.getDbBalance()),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% } %>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="4">  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="3" border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center"> TOTAL : </fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" border-right-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.negate(t[0]),0)%></fo:block></fo:table-cell> 
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="4" >  
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
            
            <fo:block font-size="10pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="100mm"/><!-- Entry Date -->
                    <fo:table-column column-width="40mm"/><!-- Policy Date--> 
                    <fo:table-column /><!-- The Insured -->
                    <fo:table-body> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="4" >  
                                <fo:block text-align="end">Jakarta, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                    </fo:table-body>   
                </fo:table>   
            </fo:block> 
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>
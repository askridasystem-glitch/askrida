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
                               page-height="21cm"
                               page-width="30cm"
                               margin-top="1cm"
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
            
            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="30mm"/><!-- Entry Date -->
                    <fo:table-column column-width="70mm"/><!-- Policy Date--> 
                    <fo:table-column column-width="30mm"/><!-- Policy No -->
                    <fo:table-column column-width="30mm"/><!-- Policy No -->
                    <fo:table-column column-width="30mm"/><!-- Policy No -->
                    <fo:table-column column-width="30mm"/><!-- Policy No -->
                    <fo:table-column column-width="30mm"/><!-- Policy No -->
                    <fo:table-column column-width="30mm"/><!-- Policy No -->
                    <fo:table-header>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">             
                                    <fo:inline text-decoration="underline">NERACA LAJUR</fo:inline>   
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-family="serif" font-weight="bold" font-size="12pt" text-align="center">             
                                    <%=JSPUtil.printX(form.getPeriodTitleDescription())%>
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
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block line-height="10mm" text-align="center" font-size="10pt">NO.</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block line-height="10mm" text-align="center" font-size="10pt">REKENING</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block line-height="10mm" text-align="center" font-size="10pt">URAIAN</fo:block></fo:table-cell>  
                            <% if (form.getLPeriodTo()!=1) { %>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block line-height="10mm" text-align="center" font-size="10pt">SALDO <%=JSPUtil.printX(form.getMonthBeforeTitleDescription().toUpperCase())%></fo:block></fo:table-cell>
                            <% } else { %>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block line-height="10mm" text-align="center" font-size="10pt">SALDO JANUARI <%=JSPUtil.printX(form.getYearFrom())%></fo:block></fo:table-cell>
                            <% } %>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block line-height="10mm" text-align="center" font-size="10pt">MUTASI <%=JSPUtil.printX(form.getMonthTitleDescription().toUpperCase())%></fo:block></fo:table-cell>  
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid" padding="2pt" border-right-style="solid"><fo:block line-height="10mm" text-align="center" font-size="10pt">SALDO <%=JSPUtil.printX(form.getMonthTitleDescription().toUpperCase())%></fo:block></fo:table-cell>  
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="3" border-width="0.5pt" border-left-style="solid" padding="2pt" ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="6" border-width="0.5pt" border-left-style="solid" padding="2pt" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                        <fo:table-row> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center" font-size="10pt">Debit</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center" font-size="10pt">Credit</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center" font-size="10pt">Debit</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center" font-size="10pt">Credit</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center" font-size="10pt">Debit</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" padding="2pt" ><fo:block text-align="center" font-size="10pt">Credit</fo:block></fo:table-cell> 
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="9" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <%
                        BigDecimal [] t = new BigDecimal[8];
                        
                        BigDecimal bx = new BigDecimal(0);
                        BigDecimal saldo = new BigDecimal(0);
                        BigDecimal bal = new BigDecimal(0);
                        BigDecimal credit_saldo = new BigDecimal(0);
                        BigDecimal debit_saldo = new BigDecimal(0);
                        BigDecimal credit_bal = new BigDecimal(0);
                        BigDecimal debit_bal = new BigDecimal(0);
                        
                        for (int i = 0; i < l.size(); i++) {
                            JournalView pol = (JournalView) l.get(i);
                            
                            saldo = pol.getDbSaldo();
                            bal = BDUtil.add(pol.getDbSaldo(), pol.getDbBalance());
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbSaldo());
                            t[n] = BDUtil.add(t[n++], pol.getDbDebit());
                            t[n] = BDUtil.add(t[n++], pol.getDbCredit());
                            t[n] = BDUtil.add(t[n++], pol.getDbBalance());
                            
                            if(BDUtil.isNegative(pol.getDbSaldo())) credit_saldo = BDUtil.sub(bx, pol.getDbSaldo());
                            else debit_saldo = BDUtil.add(bx, pol.getDbSaldo());
                            t[n] = BDUtil.add(t[n++], credit_saldo);
                            t[n] = BDUtil.add(t[n++], debit_saldo);
                            
                            if(BDUtil.isNegative(bal)) credit_bal = BDUtil.sub(bx, bal);
                            else debit_bal = BDUtil.add(bx, bal);
                            t[n] = BDUtil.add(t[n++], credit_bal);
                            t[n] = BDUtil.add(t[n++], debit_bal);
                        %>
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStAccountNo())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="start" font-size="8pt"><%=JSPUtil.printX(pol.getStDescription())%></fo:block></fo:table-cell>   <!-- No --><!-- Policy No --> 
                            <% if (BDUtil.isNegative(saldo)) { %>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(bx,2)%></fo:block></fo:table-cell>  <!-- No --><!-- Policy No --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(credit_saldo,2)%></fo:block></fo:table-cell>  <!-- No --><!-- Policy No --> 
                            <% } else { %>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(debit_saldo,2)%></fo:block></fo:table-cell>  <!-- No --><!-- Policy No --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(bx,2)%></fo:block></fo:table-cell>  <!-- No --><!-- Policy No --> 
                            <% } %>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbDebit(),2)%></fo:block></fo:table-cell>  <!-- No --><!-- Policy No --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCredit(),2)%></fo:block></fo:table-cell>  <!-- No --><!-- Policy No --> 
                            <% if (BDUtil.isNegative(bal)) { %>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(bx,2)%></fo:block></fo:table-cell>  <!-- No --><!-- Policy No --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" border-right-style="solid" ><fo:block text-align="end"><%=JSPUtil.printX(credit_bal,2)%></fo:block></fo:table-cell>
                            <% } else { %>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(debit_bal,2)%></fo:block></fo:table-cell>  <!-- No --><!-- Policy No --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" border-right-style="solid" ><fo:block text-align="end"><%=JSPUtil.printX(bx,2)%></fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                        <% } %>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="9">  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="3" border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="center"> TOTAL : </fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(t[5],2)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(t[4],2)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(t[2],2)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(t[7],2)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" padding="2pt" border-right-style="solid" ><fo:block text-align="end"><%=JSPUtil.printX(t[6],2)%></fo:block></fo:table-cell><!-- Total Premi --> 
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
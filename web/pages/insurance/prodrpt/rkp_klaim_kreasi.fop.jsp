<%@ page import="com.webfin.insurance.model.*, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.Tools, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionClaimReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionClaimReportForm form = (ProductionClaimReportForm)SessionManager.getInstance().getCurrentForm();

//if (true) throw new NullPointerException();

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-height="28cm"
                               page-width="35cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">
            
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
            <fo:region-before extent="0.5cm"/> 
            <fo:region-after extent="0cm"/> 
        </fo:simple-page-master> 
        
    </fo:layout-master-set> 
    <!-- end: defines page layout --> 
 
    <!-- actual layout --> 
    <fo:page-sequence master-reference="first"> 
        
        <!-- usage of page layout --> 
        <!-- header --> 
    
        <fo:flow flow-name="xsl-region-body"> 
            
            <fo:block font-size="10pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="35mm"/><!-- No -->
                    <fo:table-column column-width="5mm"/><!-- No -->
                    <fo:table-column column-width="35mm"/><!-- No -->
                    <fo:table-column column-width="15mm"/><!-- Daerah -->
                    <fo:table-column column-width="35mm"/><!-- Pol_No-->
                    <fo:table-column column-width="15mm"/><!-- Daerah -->
                    <fo:table-column column-width="35mm"/><!-- Pol_No--> 
                    <fo:table-column column-width="15mm"/><!-- Daerah -->
                    <fo:table-column column-width="35mm"/><!-- Pol_No--> 
                    <fo:table-column column-width="15mm"/><!-- Daerah -->
                    <fo:table-column column-width="35mm"/><!-- Pol_No--> 
                    <fo:table-column column-width="15mm"/><!-- Daerah -->
                    <fo:table-column column-width="35mm"/><!-- Pol_No--> 
                    <fo:table-header>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block text-align="center" font-family="tahoma" font-weight="bold" font-size="14pt">             
                                    STATISTIK KLAIM PA KREASI
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block text-align="center" font-family="tahoma" font-weight="bold" font-size="14pt">             
                                    <% if (form.getStBranch()!=null){ %>
                                    <%=JSPUtil.printX(form.getStBranchDesc())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block font-weight="bold">TANGGAL</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9"><fo:block>: <%=DateUtil.getDateStr(form.getAppDateFrom(),"d ^^ yyyy")%> s/d <%=DateUtil.getDateStr(form.getAppDateTo(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="13"><fo:block text-align="end" font-weight="bold">{L-ENG (In IDR)-L}{L-INA (dalam rupiah)-L}</fo:block></fo:table-cell>  
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="13" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">NILAI KLAIM</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">JUMLAH LKP</fo:block></fo:table-cell>  
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">SAKIT</fo:block></fo:table-cell>  
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">JUMLAH LKP</fo:block></fo:table-cell>  
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">KECELAKAAN</fo:block></fo:table-cell>  
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">JUMLAH LKP</fo:block></fo:table-cell>  
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">P H K</fo:block></fo:table-cell>  
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">JUMLAH LKP</fo:block></fo:table-cell>  
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">RECALL</fo:block></fo:table-cell>  
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">JUMLAH LKP</fo:block></fo:table-cell>  
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">LAIN-LAIN</fo:block></fo:table-cell> 
                        </fo:table-row>
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="13" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <!-- GARIS  -->  
                        <!--   \<fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt"></fo:block> -->  
   
                        
                        <%   
                        
                        BigDecimal [] t = new BigDecimal[10];
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm1());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm2());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm3());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm4());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDBrok1());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiBase());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiTotalAfterDisc());
                            t[n] = BDUtil.add(t[n++], pol.getDbTotalDue());
                            t[n] = BDUtil.add(t[n++], pol.getDbTotalFee()); 
                        
                        %> 
                        
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getStReference1())%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">-</fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="start"><%= JSPUtil.printX(pol.getStReference2()) %></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDbNDComm1(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiBase(),2)%></fo:block></fo:table-cell>   <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDbNDComm2(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>   <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDbNDComm3(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotalAfterDisc(),2)%></fo:block></fo:table-cell>   <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDbNDComm4(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalDue(),2)%></fo:block></fo:table-cell>   <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDbNDBrok1(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalFee(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% } %>   
                        
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="13">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="3" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"> TOTAL : </fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(t[0],0)%></fo:block></fo:table-cell><!-- Total Premi -->  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[5],2)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(t[1],0)%></fo:block></fo:table-cell><!-- Total Premi -->  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[6],2)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(t[2],0)%></fo:block></fo:table-cell><!-- Total Premi -->  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[7],2)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(t[3],0)%></fo:block></fo:table-cell><!-- Total Premi -->  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[8],2)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(t[4],0)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[9],2)%></fo:block></fo:table-cell>
                        </fo:table-row>   
                        
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="13" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="13"></fo:table-cell>   
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center">Bagian Klaim</fo:block></fo:table-cell>   
                        </fo:table-row>
                        
                    </fo:table-body>   
                </fo:table>   
            </fo:block> 
            
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block>    
            
        </fo:flow>
    </fo:page-sequence>   
</fo:root>   
<%@ page import="com.webfin.insurance.model.*,
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionReinsuranceReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 


<%
final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionReinsuranceReportForm form = (ProductionReinsuranceReportForm)SessionManager.getInstance().getCurrentForm(); 
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="21cm"
                               page-width="39cm"
                               margin-top="0.5cm"
                               margin-bottom="0.5cm"                                                                                                                                                                           
                               margin-left="1.5cm"
                               margin-right="1.5cm">
            <fo:region-body margin-top="2cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
        <fo:static-content flow-name="xsl-region-before"> 
            <fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="term"
                                        retrieve-boundary="page"
                                        retrieve-position="last-ending-within-page"/>
                </fo:block>
            </fo:block-container>
            
            
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>
        
        
        
        <fo:flow flow-name="xsl-region-body">
            
            <!-- defines text title level 1-->
            <fo:block font-size="7pt" line-height="8pt"></fo:block>
            
            
            <!-- Normal text -->

 <!-- bikin kolom headernye -->
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="50mm"/>  
                    <fo:table-column column-width="15mm"/> 
                    <fo:table-column column-width="15mm"/>  
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="20mm"/>        	 
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-header>
                        
                        <!-- judul laporan -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="14pt" text-align="center">             
                                    RINCIAN REASURANSI KELUAR
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="10pt" text-align="center">
                                    <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%>
                                    Periode : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% } %>

                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17"><fo:block space-after.optimum="20pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                       
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block font-weight="bold">Cabang Asuransi :
                                   <% if(form.getStPolicyTypeDesc()!=null) {%>
                                    <%= JSPUtil.printX(form.getStPolicyTypeGroupDesc())%>
                                    <% }else{ %>
                                    <%= JSPUtil.printX(form.getStPolicyTypeGroupDesc())%><% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17"><fo:block text-align="end" space-before.optimum="10pt">(dalam Rupiah)</fo:block></fo:table-cell>
                        </fo:table-row>
                        <!-- garis horizontal -->
                        
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <!-- garisnye sampe sini -->
        
                        
                        
                        <!-- sampe sini neh -->
                        
                        <!-- yang ini bikin kolom" header -->
                        <!-- bikin row pertama -->
                        <fo:table-row>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">NO</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Nama Reasuradur</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">Rating</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="6" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Premi Asuransi</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Komisi Reasuransi</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="6" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Beban Klaim Reasuradur</fo:block></fo:table-cell>
                             <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">Dana Reasuransi yang Ditahan</fo:block></fo:table-cell>
                        </fo:table-row>
                     
                                             
                           <!-- ini bikin row yg ke dua -->
                        <fo:table-row> 
                                                       
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid"><fo:block text-align="center">QS</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid"><fo:block text-align="center">Surplus</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid"><fo:block text-align="center">Working X/L</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid"><fo:block text-align="center">Stop Loss</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid"><fo:block text-align="center">Catastrophe X/L</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid"><fo:block text-align="center">Fakulatif</fo:block></fo:table-cell> 
                            
                            
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid"><fo:block text-align="center">QS</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid"><fo:block text-align="center">Surplus</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid"><fo:block text-align="center">Working X/L</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid"><fo:block text-align="center">Stop Loss</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid"><fo:block text-align="center">Catastrophe X/L</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-right-style="solid"><fo:block text-align="center">Fakulatif</fo:block></fo:table-cell>
                            
                            
                        </fo:table-row> 
                        
                         
                         <fo:table-row>
                            <fo:table-cell number-columns-spanned="17" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" ></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <!-- bikin lagi row yang ke tiga -->
                        
                         <fo:table-row> 
                                                       
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(1)</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(2)</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(3)</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(4)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(5)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(6)</fo:block></fo:table-cell> 
                            
                                                       
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(7)</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(8)</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(9)</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(10)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(11)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(12)</fo:block></fo:table-cell> 
                            
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(13)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(14)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(15)</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(16)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center">(17)</fo:block></fo:table-cell> 
                            
                          
                        </fo:table-row> 
                        
                         <fo:table-row>
                            <fo:table-cell number-columns-spanned="17" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" ></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        
                         <fo:table-row> 
                                                       
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block font-weight="bold">Dalam Negeri :</fo:block></fo:table-cell> 
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block></fo:block></fo:table-cell>
                            
                                   
                        </fo:table-row> 
                        
                        
                         <fo:table-row> 
                                                       
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell  display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block space-after.optimum="5pt"> </fo:block></fo:table-cell> 
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block></fo:block></fo:table-cell>
                            
                                   
                        </fo:table-row> 
                                                    
                        <!-- sampe sini -->
                                         
                                         
                       
                    </fo:table-header>
                    <fo:table-body>
                        <!-- sampe sini -->
                        
          
                        <% 
                        int counter = 40;

                        BigDecimal subTotalPremiQS = BigDecimal.ZERO;
                        BigDecimal subTotalPremiSPL = BigDecimal.ZERO;
                        BigDecimal subTotalPremiXOL1 = BigDecimal.ZERO;
                        BigDecimal subTotalPremiFAC = BigDecimal.ZERO;
                        BigDecimal subTotalCommisi = BigDecimal.ZERO;


                        BigDecimal subTotalClaimQS = BigDecimal.ZERO;
                        BigDecimal subTotalClaimSPL = BigDecimal.ZERO;
                        BigDecimal subTotalClaimXOL1 = BigDecimal.ZERO;
                        BigDecimal subTotalClaimFAC = BigDecimal.ZERO;
                        BigDecimal subTotalDanaReas = BigDecimal.ZERO;
                       
                        
                        
                        BigDecimal [] t = new BigDecimal[14];
                        int x = 0;
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                              if (pol.getStReference1() != null && !pol.getStReference1().equalsIgnoreCase("88"))  {
                               x++;
                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiQS());
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiSPL());
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiXOL1());
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiFAC());


                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimQS());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimSPL());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimXOL1());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimFAC());

                                         if (i==counter) {
                                            counter = counter + 40;
                                    %>


                                    <%

                                 subTotalPremiQS = BigDecimal.ZERO;
                                 subTotalPremiSPL = BigDecimal.ZERO;
                                 subTotalPremiXOL1 = BigDecimal.ZERO;
                                 subTotalPremiFAC = BigDecimal.ZERO;
                                 subTotalCommisi  = BigDecimal.ZERO;

                                 subTotalClaimQS = BigDecimal.ZERO;
                                 subTotalClaimSPL = BigDecimal.ZERO;
                                 subTotalClaimXOL1 = BigDecimal.ZERO;
                                 subTotalClaimFAC = BigDecimal.ZERO;
                                 subTotalDanaReas = BigDecimal.ZERO;
                                        }%>



                                    <!-- sampe sini -->
                                    <!-- ini bikin isi nye masing" row -->

                                            <fo:table-row>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="center" font-size="6pt" ><%=JSPUtil.printX(String.valueOf(x))%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block font-size="6pt" ><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                                         <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="center" font-size="6pt"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbPremiQS(),0)%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbPremiSPL(),0)%></fo:block></fo:table-cell>
                                         <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbPremiXOL1(),0)%></fo:block></fo:table-cell>
                                          <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="center" font-size="6pt"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                                           <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="center" font-size="6pt"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbPremiFAC(),0)%></fo:block></fo:table-cell>

                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbCommQS().add(pol.getDbCommSPL()).add(pol.getDbCommFAC()).add(pol.getDbCommXOL1()),0)%></fo:block></fo:table-cell>

                                         <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbClaimQS(),0)%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbClaimSPL(),0)%></fo:block></fo:table-cell>
                                         <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbClaimXOL1(),0)%></fo:block></fo:table-cell>
                                          <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="center" font-size="6pt"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                                           <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="center" font-size="6pt"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbClaimFAC(),0)%></fo:block></fo:table-cell>
                                         <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbDanaReas(),0)%></fo:block></fo:table-cell>

                                    </fo:table-row>



                                    <%

                                             subTotalPremiQS = BDUtil.add(subTotalPremiQS, pol.getDbPremiQS());
                                             subTotalPremiSPL = BDUtil.add(subTotalPremiSPL, pol.getDbPremiSPL());
                                             subTotalPremiXOL1 = BDUtil.add(subTotalPremiXOL1, pol.getDbPremiXOL1());
                                             subTotalPremiFAC = BDUtil.add(subTotalPremiFAC, pol.getDbPremiFAC());
                                             subTotalCommisi = BDUtil.add(subTotalCommisi, pol.getDbCommQS().add(pol.getDbCommSPL()).add(pol.getDbCommFAC()).add(pol.getDbCommXOL1()));

                                             subTotalClaimQS = BDUtil.add(subTotalClaimQS, pol.getDbClaimQS());
                                             subTotalClaimSPL = BDUtil.add(subTotalClaimSPL, pol.getDbClaimSPL());
                                             subTotalClaimXOL1 = BDUtil.add(subTotalClaimXOL1, pol.getDbClaimXOL1());
                                             subTotalClaimFAC =  BDUtil.add(subTotalClaimFAC, pol.getDbClaimFAC());
                                             subTotalDanaReas =  BDUtil.add(subTotalDanaReas, pol.getDbDanaReas());
                               }
                         } %>	
                        
                        <!-- sampe sini neh perulangannye -->
                        
                         <!-- untuk sub jumlah -->
                    
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Sub Jumlah</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block></fo:block></fo:table-cell>



                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiQS,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiSPL,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiXOL1,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiFAC ,0)%></fo:block></fo:table-cell>
                            
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalCommisi,0)%></fo:block></fo:table-cell>
                            
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalClaimQS,0)%></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalClaimSPL,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalClaimXOL1,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                        
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalClaimFAC,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalDanaReas,0)%></fo:block></fo:table-cell>
                        
                        </fo:table-row>
                         
                        <!-- luar negeri -->
                        
                        
                         <fo:table-row> 
                                                       
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell  display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block space-after.optimum="5pt"> </fo:block></fo:table-cell> 
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block></fo:block></fo:table-cell>
                            
                                   
                        </fo:table-row> 
                        
                        
                         <fo:table-row> 
                                                       
                            <fo:table-cell  border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block>Luar Negeri :</fo:block></fo:table-cell> 
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block></fo:block></fo:table-cell>
                            
                                   
                        </fo:table-row> 
                        
                        
                        <!-- table kosong -->
                        <fo:table-row> 
                                                       
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell  display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block space-after.optimum="5pt"> </fo:block></fo:table-cell> 
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block></fo:block></fo:table-cell>
                            
                                   
                        </fo:table-row> 
                        
                         <%

                        BigDecimal subTotalPremiQSx = BigDecimal.ZERO;
                        BigDecimal subTotalPremiSPLx = BigDecimal.ZERO;
                        BigDecimal subTotalPremiXOL1x = BigDecimal.ZERO;
                        BigDecimal subTotalPremiFACx = BigDecimal.ZERO;
                        BigDecimal subTotalCommisix = BigDecimal.ZERO;

                        BigDecimal subTotalClaimQSx = BigDecimal.ZERO;
                        BigDecimal subTotalClaimSPLx = BigDecimal.ZERO;
                        BigDecimal subTotalClaimXOL1x = BigDecimal.ZERO;
                        BigDecimal subTotalClaimFACx = BigDecimal.ZERO;
                        BigDecimal subTotalDanaReasx = BigDecimal.ZERO;



                        BigDecimal [] tx = new BigDecimal[14];
                        int y = 0;
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                              if (pol.getStReference1() != null && pol.getStReference1().equalsIgnoreCase("88"))  {
                               y++;
                                        int n = 0;
                                        tx[n] = BDUtil.add(t[n++], pol.getDbPremiQS());
                                        tx[n] = BDUtil.add(t[n++], pol.getDbPremiSPL());
                                        tx[n] = BDUtil.add(t[n++], pol.getDbPremiXOL1());
                                        tx[n] = BDUtil.add(t[n++], pol.getDbPremiFAC());


                                        tx[n] = BDUtil.add(t[n++], pol.getDbClaimQS());
                                        tx[n] = BDUtil.add(t[n++], pol.getDbClaimSPL());
                                        tx[n] = BDUtil.add(t[n++], pol.getDbClaimXOL1());
                                        tx[n] = BDUtil.add(t[n++], pol.getDbClaimFAC());

                                         if (i==counter) {
                                            counter = counter + 40;
                                    %>


                                    <%

                                 subTotalPremiQSx = BigDecimal.ZERO;
                                 subTotalPremiSPLx = BigDecimal.ZERO;
                                 subTotalPremiXOL1x = BigDecimal.ZERO;
                                 subTotalPremiFACx = BigDecimal.ZERO;
                                 subTotalCommisix = BigDecimal.ZERO;

                                 subTotalClaimQSx = BigDecimal.ZERO;
                                 subTotalClaimSPLx = BigDecimal.ZERO;
                                 subTotalClaimXOL1x = BigDecimal.ZERO;
                                 subTotalClaimFACx = BigDecimal.ZERO;
                                 subTotalDanaReasx = BigDecimal.ZERO;
                                 
                                        }%>


                                    <!-- sampe sini -->
                                    <!-- ini bikin isi nye masing" row -->

                                    <fo:table-row>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="center" font-size="6pt" ><%=JSPUtil.printX(String.valueOf(y))%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block font-size="6pt" ><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="center" font-size="6pt"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbPremiQS(),0)%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbPremiSPL(),0)%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbPremiXOL1(),0)%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="center" font-size="6pt"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="center" font-size="6pt"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbPremiFAC(),0)%></fo:block></fo:table-cell>

                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbCommQS().add(pol.getDbCommSPL()).add(pol.getDbCommFAC()).add(pol.getDbCommXOL1()),0)%></fo:block></fo:table-cell>

                                         <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbClaimQS(),0)%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbClaimSPL(),0)%></fo:block></fo:table-cell>
                                         <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbClaimXOL1(),0)%></fo:block></fo:table-cell>
                                          <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="center" font-size="6pt"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                                           <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="center" font-size="6pt"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                                        <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbClaimFAC(),0)%></fo:block></fo:table-cell>
                                         <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(pol.getDbDanaReas(),0)%></fo:block></fo:table-cell>

                                    </fo:table-row>



                                    <% 
                                             subTotalPremiQSx = BDUtil.add(subTotalPremiQSx, pol.getDbPremiQS());
                                             subTotalPremiSPLx = BDUtil.add(subTotalPremiSPLx, pol.getDbPremiSPL());
                                             subTotalPremiXOL1x = BDUtil.add(subTotalPremiXOL1x, pol.getDbPremiXOL1());
                                             subTotalPremiFAC = BDUtil.add(subTotalPremiFACx, pol.getDbPremiFAC());
                                             subTotalCommisix = BDUtil.add(subTotalCommisix, pol.getDbCommQS().add(pol.getDbCommSPL()).add(pol.getDbCommFAC()).add(pol.getDbCommXOL1()));

                                             subTotalClaimQSx = BDUtil.add(subTotalClaimQSx, pol.getDbClaimQS());
                                             subTotalClaimSPLx = BDUtil.add(subTotalClaimSPLx, pol.getDbClaimSPL());
                                             subTotalClaimXOL1x = BDUtil.add(subTotalClaimXOL1x, pol.getDbClaimXOL1());
                                             subTotalClaimFACx =  BDUtil.add(subTotalClaimFACx, pol.getDbClaimFAC());
                                             subTotalDanaReasx =  BDUtil.add(subTotalDanaReasx, pol.getDbDanaReas());
                               }
                         } %>	
                         } %>
                        
                        

                        
                        <!-- table kosong -->
                        <fo:table-row> 
                                                       
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell  display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block space-after.optimum="5pt"> </fo:block></fo:table-cell> 
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block></fo:block></fo:table-cell>
                            
                                   
                        </fo:table-row> 
                        
                         <!-- table kosong -->
                        <fo:table-row> 
                                                       
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell  display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block space-after.optimum="5pt"> </fo:block></fo:table-cell> 
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block></fo:block></fo:table-cell>
                            
                                   
                        </fo:table-row> 
                        
                        
                        <!-- sub jumlah untuk luar negri -->
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Sub Jumlah</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block></fo:block></fo:table-cell>
                           
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiQSx,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiSPLx,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiXOL1x,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiFACx,0)%></fo:block></fo:table-cell>
                            
                            
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalCommisix,0)%></fo:block></fo:table-cell>
                            
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalClaimQSx,0)%></fo:block></fo:table-cell>
                                                   
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalClaimSPLx,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalClaimXOL1x,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                        
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalClaimFACx,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-bottom-style="solid" border-top-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalDanaReasx,0)%></fo:block></fo:table-cell>
                        
                        </fo:table-row>
                         
                         <fo:table-row> 
                                                       
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"  border-bottom-style="solid"><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                             <fo:table-cell border-width="0.5pt" border-left-style="solid"  border-right-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                            
                                   
                        </fo:table-row>     
                        
                        <!-- untuk bikin total -->
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Jumlah keseluruhan</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiQS.add(subTotalPremiQSx),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiSPL.add(subTotalPremiSPLx),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiXOL1.add(subTotalPremiXOL1x),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiFAC.add(subTotalPremiFACx),0)%></fo:block></fo:table-cell>
                            
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalCommisi.add(subTotalCommisix))%></fo:block></fo:table-cell>
                            
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalClaimQS.add(subTotalClaimQSx),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalClaimSPL.add(subTotalClaimSPLx),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalClaimXOL1.add(subTotalClaimXOL1x),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX("-")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalClaimFAC.add(subTotalClaimFACx),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalDanaReas.add(subTotalDanaReasx),0)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>    
            
            <fo:block font-size="6pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block> 

            <fo:block 
                      font-size="6pt"
                      font-family="TAHOMA"
                      line-height="12pt"
                      font-style="bold">
                rppre_rincian_reas_keluar - PT. Asuransi Bangun Askrida
            </fo:block>

            
            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                        <svg xmlns="http://www.w3.org/2000/svg" width="15cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block> 
            
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>

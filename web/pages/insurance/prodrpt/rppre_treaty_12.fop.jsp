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

BigDecimal PremiReasIDR = null;

BigDecimal PremiReasUSD = null;

BigDecimal PremiReasGBP = null;

BigDecimal PremiReasJPY = null;

BigDecimal PremiReasSGD = null;

BigDecimal PremiReasEUR = null;
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="31cm"
                               page-width="22cm"
                               margin-top="4cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="1.5cm" margin-bottom="1cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->
    
    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
        <fo:static-content flow-name="xsl-region-before"> 
            <fo:block-container height="1cm" width="2cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="1cm" width="2cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="term"
                                        retrieve-boundary="page"
                                        retrieve-position="last-ending-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block text-align="end"
                      font-size="8pt" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>
        
        <fo:flow flow-name="xsl-region-body">
            
            <fo:block font-size="<%=form.getStFontSize()%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="13mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column column-width="41mm"/>
                    <fo:table-header>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-size="12pt" font-weight="bold" text-align="center">	 
                                    REKAPITULASI LAPORAN PRODUKSI HARIAN
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block font-size="10pt" text-align="start">	 
                                    Kepada Yth,
                                </fo:block>
                                <fo:block font-size="10pt" text-align="start">	
                                    Manager Konsorsium Asuransi
                                </fo:block>
                                <fo:block font-size="10pt" text-align="start">	
                                    Risiko Khusus
                                </fo:block>
                                <fo:block font-size="10pt" text-align="start">	 
                                    d/a. PT. Tugu Jasatama Reasuransi Indonesia
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="5"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block font-size="10pt" text-align="start">	 
                                    No. Rekap : 
                                </fo:block>
                                <fo:block font-size="10pt" text-align="start">
                                    U. Year   : <% if (form.getPeriodFrom()!=null) { %>
                                    <%=DateUtil.getDateStr(form.getPeriodFrom(),"YYYY")%> / 
                                    <% } %>
                                    <% if (form.getPeriodTo()!=null) { %>
                                    <%=DateUtil.getDateStr(form.getPeriodTo(),"YYYY")%>
                                    <% } %>
                                </fo:block>
                                <fo:block text-align="start">	 
                                    No. Urut : <% if (form.getStNoUrut()!=null) { %><%=form.getStNoUrut()%> <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-rows-spanned="2" border-width="0.5pt" border-left-style="solid" display-align="center"><fo:block text-align="center">No. Urut</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" border-width="0.5pt" border-left-style="solid" display-align="center"></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" border-width="0.5pt" display-align="center"><fo:block text-align="center">Kelompok Resiko</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" border-width="0.5pt" border-left-style="solid" display-align="center"><fo:block text-align="center">No. Polis</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" border-width="0.5pt" border-left-style="solid" display-align="center"></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" border-width="0.5pt" display-align="center"><fo:block text-align="center">Premi Bruto</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="4" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center">Keterangan</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-left-style="solid"><fo:block text-align="center">Kelas</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-left-style="solid"><fo:block text-align="center">Periode</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-left-style="solid" ></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-right-style="solid"><fo:block text-align="center">Kode</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                          
                            <fo:table-cell border-width="0.5pt" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                             
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                                                    
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                            
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>   
                            <fo:table-cell border-width="0.5pt" border-right-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <% 
                        int counter = 20;
                        BigDecimal [] t = new BigDecimal[4];
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            if (pol.getStCurrencyCode().equalsIgnoreCase("IDR")) {
                                PremiReasIDR = BDUtil.add(PremiReasIDR, pol.getDbPremiTotal());
                            } else if (pol.getStCurrencyCode().equalsIgnoreCase("USD")) {
                                PremiReasUSD = BDUtil.add(PremiReasUSD, pol.getDbPremiTotal());
                            } else if (pol.getStCurrencyCode().equalsIgnoreCase("SGD")) {
                                PremiReasSGD = BDUtil.add(PremiReasSGD, pol.getDbPremiTotal());
                            } else if (pol.getStCurrencyCode().equalsIgnoreCase("EUR")) {
                                PremiReasEUR = BDUtil.add(PremiReasEUR, pol.getDbPremiTotal());
                            } else if (pol.getStCurrencyCode().equalsIgnoreCase("GBP")) {
                                PremiReasGBP = BDUtil.add(PremiReasGBP, pol.getDbPremiTotal());
                            } else if (pol.getStCurrencyCode().equalsIgnoreCase("JPY")) {
                                PremiReasJPY = BDUtil.add(PremiReasJPY, pol.getDbPremiTotal());
                            }
                            
                            if (i==counter) {
                                counter = counter + 20;
                                
                                //	if (Tools.compare(pol.getDbPremiTotal(), BDUtil.zero)>0) continue;
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                          
                            <fo:table-cell border-width="0.5pt" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                             
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                                                    
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                            
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>   
                            <fo:table-cell border-width="0.5pt" border-right-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        
                        <% } %>                        
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(String.valueOf(i+1))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" ><fo:block text-align="start"><%=JSPUtil.printX(pol.getStReference5())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><fo:inline color="white">.</fo:inline><%=JSPUtil.printX(pol.getStPolicyNo())%><fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="start"><fo:inline color="white">.</fo:inline>Rp.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%><fo:inline color="white"> . </fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">
                                    Kelas <%=JSPUtil.printX(pol.getStReference2())%> <%=JSPUtil.printX(pol.getDbReference1(),2)%> %o                                
                            </fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">
                                    <%=DateUtil.getDateStr(pol.getDtPeriodStart(),"dd MMM yyyy")%> / <%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"dd MMM yyyy")%>
                            </fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-right-style="solid"><fo:block text-align="start">                            
                                    <%=JSPUtil.printX(pol.getStReference7())%>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                          
                            <fo:table-cell border-width="0.5pt" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                             
                            <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                                                    
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                            
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>   
                            <fo:table-cell border-width="0.5pt" border-right-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                          
                            <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                             
                            <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                                                    
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                            
                            <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>   
                            <fo:table-cell border-width="0.5pt" border-right-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">TOTAL IDR</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="start"><fo:inline color="white">.</fo:inline>Rp.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" ><fo:block text-align="end"><%=JSPUtil.printX(PremiReasIDR,2)%><fo:inline color="white"> . </fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="4" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="start"></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">TOTAL USD</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="start"><fo:inline color="white">.</fo:inline>Rp.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" ><fo:block text-align="end"><%=JSPUtil.printX(PremiReasUSD,2)%><fo:inline color="white"> . </fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="4" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="start"></fo:block></fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">TOTAL EUR</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="start"><fo:inline color="white">.</fo:inline>Rp.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" ><fo:block text-align="end"><%=JSPUtil.printX(PremiReasEUR,2)%><fo:inline color="white"> . </fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="4" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="start"></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">TOTAL GBP</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="start"><fo:inline color="white">.</fo:inline>Rp.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" ><fo:block text-align="end"><%=JSPUtil.printX(PremiReasGBP,2)%><fo:inline color="white"> . </fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="4" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="start"></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">TOTAL SGD</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="start"><fo:inline color="white">.</fo:inline>Rp.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" ><fo:block text-align="end"><%=JSPUtil.printX(PremiReasSGD,2)%><fo:inline color="white"> . </fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="4" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="start"></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">TOTAL JPY</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="start"><fo:inline color="white">.</fo:inline>Rp.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" ><fo:block text-align="end"><%=JSPUtil.printX(PremiReasJPY,2)%><fo:inline color="white"> . </fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="4" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="start"></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                          
                            <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                             
                            <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                                                    
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>                            
                            <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>   
                            <fo:table-cell border-width="0.5pt" border-right-style="solid" ><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                    
                </fo:table>
            </fo:block>    
            
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block>    
            
            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[0],0)%>" orientation="0">
                        <barcode:datamatrix> 
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>                                    
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

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
                               page-height="23cm"
                               page-width="52cm"
                               margin-top="0.5cm"
                               margin-bottom="0.5cm"                                                                                                                                                                           
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="2cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
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
                      font-size="6pt"
                      font-family="TAHOMA"
                      line-height="12pt" 
                      font-style="bold">
                rppre_all - PT. Asuransi Bangun Askrida
            </fo:block>
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

 
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="47mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>         
                    <fo:table-column column-width="18mm"/>        
                    <fo:table-column column-width="18mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-header>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="24">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="20pt" text-align="center">             
                                    DETIL LAPORAN PRODUKSI REASURANSI    
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="24">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="18pt" text-align="center">
                                    <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block>Jenis Polis</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="21">
                                <fo:block font-weight="bold">: <% if(form.getStPolicyTypeDesc()!=null) {%>
                                    <%= JSPUtil.printX(form.getStPolicyTypeGroupDesc())%>
                                    <% }else{ %>
                                    <%= JSPUtil.printX(form.getStPolicyTypeGroupDesc())%><% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block>Cabang</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="21">
                                <fo:block font-weight="bold">: <% if (form.getStBranchDesc()!=null) {%> 
                                    <%=JSPUtil.printX(form.getStBranchDesc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block>No. Polis</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="21">
                                <fo:block font-weight="bold">: <% if (form.getStPolicyNo()!=null) {%> 
                                    <%=JSPUtil.printX(form.getStPolicyNo())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="24" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Policy No. -L}{L-INA No. Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Customer -L}{L-INA Nama -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Periode -L}{L-INA Period -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Curr -L}{L-INA Kurs -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Insured Amount -L}{L-INA Harga Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Rate -L}{L-INA Rate -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Co-member Share -L}{L-INA Co-member Share -L}</fo:block></fo:table-cell>   
                            <fo:table-cell number-column-spanned="3" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG BPPDAN -L}{L-INA BPPDAN -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-column-spanned="3" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG OWN RISK -L}{L-INA OWN RISK -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-column-spanned="3" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG SURPLUS -L}{L-INA SURPLUS -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-column-spanned="3" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG FACULTATIF -L}{L-INA FACULTATIF -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-column-spanned="3" display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG PARK -L}{L-INA PARK -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-right-style="solid"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Sum Insured</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Premium</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Commission</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Sum Insured</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Premium</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Commission</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Sum Insured</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Premium</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Commission</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Sum Insured</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Premium</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Commission</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Sum Insured</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="center" line-height="5mm">Premium</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">Commission</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="24" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-header>
                    <fo:table-body>
                        
                        <% 
                        int counter = 40;
                        BigDecimal subTotalTsiBPDAN = null;
                        BigDecimal subTotalPremiBPDAN = null;
                        BigDecimal subTotalCommBPDAN = null;
                        BigDecimal subTotalTsiOR = null;
                        BigDecimal subTotalPremiOR = null;
                        BigDecimal subTotalCommOR = null;
                        BigDecimal subTotalTsiSPL = null;
                        BigDecimal subTotalPremiSPL = null;
                        BigDecimal subTotalCommSPL = null;
                        BigDecimal subTotalTsiFAC = null;
                        BigDecimal subTotalPremiFAC = null;
                        BigDecimal subTotalCommFAC = null;
                        BigDecimal subTotalTsiPARK = null;
                        BigDecimal subTotalPremiPARK = null;
                        BigDecimal subTotalCommPARK = null;
                        
                        
                        BigDecimal [] t = new BigDecimal[15];
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n = 0;
                            t[n] = BDUtil.add(t[n++], pol.getDbTsiBPDAN());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiBPDAN());
                            t[n] = BDUtil.add(t[n++], pol.getDbTsiOR());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiOR());
                            t[n] = BDUtil.add(t[n++], pol.getDbTsiSPL());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiSPL());
                            t[n] = BDUtil.add(t[n++], pol.getDbTsiFAC());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiFAC());
                            t[n] = BDUtil.add(t[n++], pol.getDbTsiPARK());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiPARK());
                            t[n] = BDUtil.add(t[n++], pol.getDbCommBPDAN());
                            t[n] = BDUtil.add(t[n++], pol.getDbCommOR());
                            t[n] = BDUtil.add(t[n++], pol.getDbCommSPL());
                            t[n] = BDUtil.add(t[n++], pol.getDbCommFAC());
                            t[n] = BDUtil.add(t[n++], pol.getDbCommPARK());
                            
                            if (i==counter) {
                                counter = counter + 40;
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="24" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell number-columns-spanned="9" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalTsiBPDAN,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiBPDAN,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalCommBPDAN,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalTsiOR,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiOR,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalCommOR,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalTsiSPL,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiSPL,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalCommSPL,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalTsiFAC,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiFAC,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalCommFAC,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalTsiPARK,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiPARK,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalCommPARK,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="24" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                        subTotalTsiBPDAN = null;
                        subTotalPremiBPDAN = null;
                        subTotalCommBPDAN = null;
                        subTotalTsiOR = null;
                        subTotalPremiOR = null;
                        subTotalCommOR = null;
                        subTotalTsiSPL = null;
                        subTotalPremiSPL = null;
                        subTotalCommSPL = null;
                        subTotalTsiFAC = null;
                        subTotalPremiFAC = null;
                        subTotalCommFAC = null;
                        subTotalTsiPARK = null;
                        subTotalPremiPARK = null;
                        subTotalCommPARK = null;
                            }%>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(String.valueOf(i+1))%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStPolicyTypeDesc())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPeriodStart())%> s/d <%=JSPUtil.printX(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getStRateMethodDesc())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getStRateMethod())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiBPDAN(),0)%></fo:block></fo:table-cell> 
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiBPDAN(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCommBPDAN(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiOR(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiOR(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCommOR(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiSPL(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiSPL(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCommSPL(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiFAC(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiFAC(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCommFAC(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiPARK(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiPARK(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCommPARK(),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% 
                        subTotalTsiBPDAN = BDUtil.add(subTotalTsiBPDAN, pol.getDbTsiBPDAN());
                        subTotalPremiBPDAN = BDUtil.add(subTotalPremiBPDAN, pol.getDbPremiBPDAN());
                        subTotalCommBPDAN = BDUtil.add(subTotalCommBPDAN, pol.getDbCommBPDAN());
                        subTotalTsiOR = BDUtil.add(subTotalTsiOR, pol.getDbTsiOR());
                        subTotalPremiOR = BDUtil.add(subTotalPremiOR, pol.getDbPremiOR());
                        subTotalCommOR = BDUtil.add(subTotalCommOR, pol.getDbCommOR());
                        subTotalTsiSPL = BDUtil.add(subTotalTsiSPL, pol.getDbTsiSPL());
                        subTotalPremiSPL = BDUtil.add(subTotalPremiSPL, pol.getDbPremiSPL());
                        subTotalCommSPL = BDUtil.add(subTotalCommSPL, pol.getDbCommSPL());
                        subTotalTsiFAC = BDUtil.add(subTotalTsiFAC, pol.getDbTsiFAC());
                        subTotalPremiFAC = BDUtil.add(subTotalPremiFAC, pol.getDbPremiFAC());
                        subTotalCommFAC = BDUtil.add(subTotalCommFAC, pol.getDbCommFAC());
                        subTotalTsiPARK = BDUtil.add(subTotalTsiPARK, pol.getDbTsiPARK());
                        subTotalPremiPARK = BDUtil.add(subTotalPremiPARK, pol.getDbPremiPARK());
                        subTotalCommPARK = BDUtil.add(subTotalCommPARK, pol.getDbCommPARK());
                        } %>			
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="24">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row> 			
                        
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[0],0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[1],0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[10],0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[2],0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[3],0)%></fo:block></fo:table-cell> 
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[11],0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[4],0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[5],0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[12],0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[6],0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[7],0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[13],0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[8],0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[9],0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-right-style="solid" ><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[14],0)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="24" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>    
            
            <fo:block font-size="6pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
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
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
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
                               page-width="30cm"
                               page-height="21cm"
                               margin-top="0.5cm"
                               margin-bottom="0.5cm"                                                                                                                                                                           
                               margin-left="1.5cm"
                               margin-right="1.5cm">
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
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>
        
        
        
        <fo:flow flow-name="xsl-region-body">
            
            
            <fo:block font-size="13pt" line-height="12pt" text-align="center">{L-ENG Register of Premium List Reinsurance-L}{L-INA Register Produksi Premi Reasuransi-L}</fo:block>
            
            
            <fo:block font-size="14pt" text-align="center">
                <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                <% } %>
            </fo:block> 
            
            <fo:block font-size="14pt" text-align="center">
                <% if (form.getPeriodFrom()!=null || form.getPeriodTo()!=null) {%> 
                Periode : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
                <% } %>
            </fo:block>
            
            <fo:block font-size="14pt" text-align="center">
                <% if (form.getEntryDateFrom()!=null || form.getEntryDateTo()!=null) {%> 
                Tanggal Entry : <%=JSPUtil.printX(form.getEntryDateFrom())%> S/D <%=JSPUtil.printX(form.getEntryDateTo())%>
                <% } %>
            </fo:block>  
            
            <fo:block font-size="14pt">
                <% if (form.getStBranchDesc()!=null) {%> 
                Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>  
                <% } %>
            </fo:block>
            
            <fo:block font-size="14pt">
                <% if (form.getStPolicyTypeDesc()!=null) {%> 
                Jenis Polis : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>  
                <% } %>
            </fo:block>  
            
            <fo:block font-size="14pt">
                <% if (form.getStFltCoverTypeDesc()!=null) {%> 
                Jenis Penutupan  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>  
                <% } %>
            </fo:block>  
            
            <fo:block font-size="14pt">
                <% if (form.getStCustCategory1Desc()!=null) {%> 
                Sumber Bisnis : <%=JSPUtil.printX(form.getStCustCategory1Desc())%>  
                <% } %>
            </fo:block>    
            
            <fo:block font-size="14pt">
                <% if (form.getStEntityName()!=null) {%> 
                Customer Name :<%=JSPUtil.printX(form.getStEntityName())%>  
                <% } %>
            </fo:block>  
            
            <fo:block font-size="14pt">
                <% if (form.getStMarketerName()!=null) {%> 
                Marketer Name :<%=JSPUtil.printX(form.getStMarketerName())%>  
                <% } %>
            </fo:block>
            
            <fo:block font-size="14pt">
                <% if (form.getStRiskLocation()!=null) {%> 
                Risk Location :<%=JSPUtil.printX(form.getStRiskLocation())%>  
                <% } %>
            </fo:block>  
            
            <fo:block font-size="14pt">
                <% if (form.getStPostCode()!=null) {%> 
                Post Code :<%=JSPUtil.printX(form.getStPostCode())%>  
                <% } %>
            </fo:block>  
            
            
            <fo:block font-size="14pt">
                <% if (form.getStRiskCardNo()!=null) {%> 
                Risk Card No. :<%=JSPUtil.printX(form.getStRiskCardNo())%>  
                <% } %>
            </fo:block>  
            
            <fo:block font-size="14pt">
                <% if (form.getStRiskCode()!=null) {%> 
                Risk Code :<%=JSPUtil.printX(form.getStRiskCode())%>  
                <% } %>
            </fo:block> 
            
            <!-- defines text title level 1-->
            <fo:block font-size="7pt" line-height="8pt"></fo:block>
            
            
            
            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-after.optimum="10pt"></fo:block>
            
            <!-- Normal text -->

 
            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center">{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Policy Date -L}{L-INA Tanggal Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Policy No. -L}{L-INA No. Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Customer -L}{L-INA Nama -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG TSI -L}{L-INA TSI -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG TSI R/I -L}{L-INA TSI R/I -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Premi R/I -L}{L-INA Premi R/I -L}</fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>             
                            <fo:table-cell><fo:block text-align="center">{L-ENG Type R/I -L}{L-INA Type R/I -L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <% 
                        BigDecimal [] t = new BigDecimal[1];
                        BigDecimal [] y = new BigDecimal[2];
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                        %>
                        <fo:table-row>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(String.valueOf(i+1))%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>
                                    <fo:block font-size="10pt">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="40mm"/>
                                            <fo:table-column />
                                            <fo:table-body>
                                                
                                                <% 
                                                DTOList objects = pol.getObjects();
                                                
                                                for (int j = 0; j < objects.size(); j++) {
                                                    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);
                                                    
                                                    int n = 0;
                                                    t[n] = BDUtil.add(t[n++], obj.getDbObjectInsuredAmount());
                                                %>
                                                <fo:table-row>
                                                    <%--  <fo:table-cell><fo:block><%=JSPUtil.printX(String.valueOf(i+1))%>.<%=JSPUtil.printX(String.valueOf(j+1))%></fo:block></fo:table-cell>--%> 
                                                    <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbObjectInsuredAmount(),2)%></fo:block></fo:table-cell> 
                                                    <fo:table-cell><fo:block>
                                                            <fo:block font-size="10pt">
                                                                <fo:table table-layout="fixed">
                                                                    <fo:table-column column-width="40mm"/>
                                                                    <fo:table-column column-width="40mm"/>
                                                                    <fo:table-column column-width="5mm"/>
                                                                    <fo:table-column column-width="20mm"/>
                                                                    <fo:table-body>
                                                                        
                                                                        <% 
                                                                        DTOList poltreaty = obj.getTreatyDetails();
                                                                        
                                                                        for(int k = 0; k < poltreaty.size(); k++) {
                                                                        InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) poltreaty.get(k);
                                                                        
                                                                        int m = 0;
                                                                        y[m] = BDUtil.add(y[m++], trd.getDbTSIAmount());
                                                                        y[m] = BDUtil.add(y[m++], trd.getDbPremiAmount());
                                                                        %>			 
                                                                        <fo:table-row>
                                                                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(trd.getDbTSIAmount(),2)%></fo:block></fo:table-cell>
                                                                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(trd.getDbPremiAmount(),2)%></fo:block></fo:table-cell>
                                                                            <fo:table-cell></fo:table-cell>
                                                                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(trd.getStTreatyType())%></fo:block></fo:table-cell>
                                                                        </fo:table-row> 
                                                                        
                                                                        <% } %>        
                                                                        
                                                                    </fo:table-body>
                                                                </fo:table>
                                                            </fo:block>  	
                                                    </fo:block></fo:table-cell>
                                                </fo:table-row>
                                                
                                                <fo:table-row>
                                                    <fo:table-cell number-columns-spanned="12" >
                                                        <fo:block space-before.optimum="5pt"></fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                                
                                                <% } %>
                                                
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>  	
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row> 			
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Total</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(y[0],2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(y[1],2)%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>    
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
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

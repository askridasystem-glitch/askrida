<%@ page import="com.webfin.insurance.model.*,
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionFinanceReportForm, 
com.crux.common.parameter.Parameter,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 

<%
final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionFinanceReportForm form = (ProductionFinanceReportForm)SessionManager.getInstance().getCurrentForm(); 
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-width="21cm"
                               page-height="33cm"
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
        </fo:static-content>
        
        <fo:static-content flow-name="xsl-region-after">
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>
        
        <fo:flow flow-name="xsl-region-body">
            
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-header>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block text-align="center">TECHNICAL ACCOUNT STATEMENT</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block text-align="center">Account Period : <%= Parameter.readString("MONTH_"+DateUtil.getMonth(form.getPolicyDateFrom()))%> / <%=DateUtil.getYear(form.getPolicyDateFrom())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block text-align="center" space-after.optimum="20pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9"><fo:block space-after.optimum="3pt"><%=JSPUtil.printX(form.getStEntityName())%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">No.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">C.O.B</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">CCY</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Month</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Type of Treaty</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Premium</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Commission</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Claim</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Balance</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-header>
                    <fo:table-body>
                        <% 
                        int counter = 50;
                        BigDecimal subTotalPremium = null;
                        BigDecimal subTotalCommission = null;
                        BigDecimal subTotalClaim = null;
                        BigDecimal subTotalNetto = null;
                        
                        BigDecimal TotalPremium = null;
                        BigDecimal TotalCommission = null;
                        BigDecimal TotalClaim = null;
                        BigDecimal TotalNetto = null;
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            TotalPremium = BDUtil.add(TotalPremium,pol.getDbPremiAmt());
                            TotalCommission = BDUtil.add(TotalCommission,pol.getDbNDBrok2Pct());
                            TotalClaim = BDUtil.add(TotalClaim,pol.getDbClaimAmount());
                            TotalNetto = BDUtil.sub(TotalPremium,TotalCommission);
                            TotalNetto = BDUtil.sub(TotalNetto,TotalClaim);
                            
                            if (i==counter) {
                                counter = counter + 50;
                        %>      
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="center">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalPremium,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalCommission,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalClaim,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalNetto,2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                        subTotalPremium = null;
                        subTotalCommission = null;
                        subTotalClaim = null;
                        subTotalNetto = null;
                        
                            }%>  
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(pol.getStCoverTypeCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(pol.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCoTreatyID())%> <%=JSPUtil.printX(pol.getStReference2())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiAmt(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDBrok2Pct(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmount(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalNetto,2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% 
                        subTotalPremium = BDUtil.add(subTotalPremium,pol.getDbPremiAmt());
                        subTotalCommission = BDUtil.add(subTotalCommission,pol.getDbNDBrok2Pct());
                        subTotalClaim = BDUtil.add(subTotalClaim,pol.getDbClaimAmount());
                        subTotalNetto = BDUtil.sub(subTotalPremium,subTotalCommission);
                        subTotalNetto = BDUtil.sub(subTotalNetto,pol.getDbClaimAmount());
                        } %>          
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalPremium,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalCommission,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalClaim,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalNetto,2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
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

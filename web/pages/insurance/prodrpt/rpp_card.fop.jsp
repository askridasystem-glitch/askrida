<%@ page import="com.webfin.insurance.model.*, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionMarketingReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionMarketingReportForm form = (ProductionMarketingReportForm)SessionManager.getInstance().getCurrentForm(); 

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="all" 
                               page-height="29.5cm"
                               page-width="21cm"
                               margin-top="2cm"
                               margin-bottom="2cm"
                               margin-left="1.5cm"
                               margin-right="1.5cm">
            
            <fo:region-body margin-top="0cm" margin-bottom="1cm"
                            column-count="2" column-gap="0.75in"/> 
            <fo:region-before extent="0cm"/> 
            <fo:region-after extent="0cm"/> 
        </fo:simple-page-master> 
        
    </fo:layout-master-set> 
    <!-- end: defines page layout --> 
 
    <!-- actual layout --> 
    <fo:page-sequence master-reference="all"> 
        
        <fo:flow flow-name="xsl-region-body"> 
            
            <fo:block font-size="10pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="20mm"/><!-- No -->
                    <fo:table-column column-width="5mm"/><!-- Entry Date --> 
                    <fo:table-column column-width="50mm"/><!-- Police Date -->
                    <fo:table-body>
                        
                        <% 
                        for (int i = 0; i < l.size(); i++) {
    InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
    
    String no_polis = pol.getStPolicyNo();
    
    String no_polis_cetak = no_polis.substring(0,4)+"-"+no_polis.substring(4,8)+"-"+no_polis.substring(8,12)+"-"+no_polis.substring(12,16)+"-00";
                        %>   
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="12pt" text-align="center">
                                    KARTU PESERTA ASURANSI
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="12pt" text-align="center">
                                    PA. KREASI
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block space-after.optimum="10pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block font-weight="bold">NO. POLIS</fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>    <!-- No --><!-- Entry Date -->
                            <fo:table-cell ><fo:block ><%= JSPUtil.printX(no_polis_cetak) %></fo:block></fo:table-cell>    <!-- No --><!-- Police Date --> 
                        </fo:table-row>   
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block font-weight="bold">NO. URUT</fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>    <!-- No --><!-- Entry Date -->
                            <fo:table-cell ><fo:block ><%= JSPUtil.printX(pol.getStEntityID()) %></fo:block></fo:table-cell>    <!-- No --><!-- Police Date --> 
                        </fo:table-row>   
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block font-weight="bold">NAMA</fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>    <!-- No --><!-- Entry Date -->
                            <fo:table-cell ><fo:block ><%= JSPUtil.printX(pol.getStCustomerName()) %></fo:block></fo:table-cell>    <!-- No --><!-- Police Date --> 
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block font-weight="bold">PERIODE</fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>    <!-- No --><!-- Entry Date -->
                            <fo:table-cell ><fo:block ><%= JSPUtil.printX(pol.getDtPeriodStart()) %> s/d <%= JSPUtil.printX(pol.getDtPeriodEnd()) %></fo:block></fo:table-cell>    <!-- No --><!-- Police Date --> 
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block space-after.optimum="60pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>  
                        
                        <%  } %>   
                        
                    </fo:table-body>   
                </fo:table>   
            </fo:block> 
            
        </fo:flow>   
    </fo:page-sequence>   
</fo:root>   

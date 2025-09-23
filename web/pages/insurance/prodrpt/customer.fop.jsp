<%@ page import="com.webfin.insurance.model.*, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.Tools, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.lang.LanguageManager,
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
        <fo:simple-page-master master-name="first" 
                               page-height="21cm"
                               page-width="33cm"
                               margin-top="0.75cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">
            
            <fo:region-body margin-top="1cm" margin-bottom="2cm"/> 
            <fo:region-before extent="2cm"/> 
            <fo:region-after extent="0.5cm"/> 
        </fo:simple-page-master> 
        
    </fo:layout-master-set> 
    <!-- end: defines page layout --> 
 
    <!-- actual layout --> 
    <fo:page-sequence master-reference="first">     
        
        <fo:flow flow-name="xsl-region-body"> 
            
            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="20mm"/><!-- Entry Date -->
                    <fo:table-column column-width="100mm"/><!-- Policy Date--> 
                    <fo:table-column column-width="140mm"/><!-- Period End--> 
                    <fo:table-column column-width="20mm"/><!-- Policy No -->
                    <fo:table-header>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block font-weight="bold" font-size="20pt" text-align="center">             
                                    DATA CUSTOMER
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block font-weight="bold">
                                    <% if (form.getStBranchName()!=null) { %>
                                    Cabang : <%=JSPUtil.printX(form.getStBranchName())%>
                                    <% } %>
                                    <% if (form.getStBranchDesc()!=null) { %>
                                    Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCustStatus()!=null) {%> 
                                    Status : <%=JSPUtil.printX(form.getStCustStatus())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG ID -L}{L-INA ID -L}</fo:block></fo:table-cell>   
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Customer Name-L}{L-INA Nama Customer -L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Customer Address-L}{L-INA Alamat Customer -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Category -L}{L-INA Kategori -L}</fo:block></fo:table-cell>
                        </fo:table-row>  
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <!-- GARIS  -->  
                        
                        <% 
                        for (int i = 0; i < l.size(); i++) {
                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                        
                        String custName = "";
                        if(pol.getStCustomerName().length()>60)
                        custName = pol.getStCustomerName().substring(0,60);
                        else
                        custName = pol.getStCustomerName().substring(0, pol.getStCustomerName().length());
                        
                        String custAddress = "";
                        if(pol.getStCustomerAddress().length()>100)
                        custAddress = pol.getStCustomerAddress().substring(0,100);
                        else
                        custAddress = pol.getStCustomerAddress().substring(0, pol.getStCustomerAddress().length());
                        %>   
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStPolicyID())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(custName)%></fo:block></fo:table-cell>    <!-- No --><!-- The Insured --> 
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(custAddress)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(LanguageManager.getInstance().translate(pol.getStPrintCode()))%></fo:block></fo:table-cell>    
                        </fo:table-row>
                        
                        <% } %>
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
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
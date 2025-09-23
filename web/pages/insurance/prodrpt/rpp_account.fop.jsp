<%@ page import="com.webfin.insurance.model.*,
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionMarketingReportForm,
com.webfin.entity.model.EntityView, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 
final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionMarketingReportForm form = (ProductionMarketingReportForm)SessionManager.getInstance().getCurrentForm(); 
%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <fo:simple-page-master master-name="only" 
                               page-height="27cm"
                               page-width="33cm"
                               margin-top="2cm"
                               margin-bottom="1cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">
            <fo:region-body margin-top="1cm" margin-bottom="1.5cm"/> 
            <fo:region-before extent="3cm"/> 
            <fo:region-after extent="1.5cm"/> 
        </fo:simple-page-master> 
        
    </fo:layout-master-set> 
    <!-- end: defines page layout --> 
 
    <!-- actual layout --> 
    <fo:page-sequence master-reference="only" initial-page-number="1"> 
        
        <!-- usage of page layout --> 
        <!-- header --> 
        <fo:static-content flow-name="xsl-region-before"> 
            
            <fo:block text-align="center" font-size="16pt" font-family="tahoma" line-height="16pt" > 
                {L-ENG STATEMENT OF ACCOUNT-L}{L-INA STATEMENT OF ACCOUNT-L}
            </fo:block> 
            
            <%--    <fo:block text-align="center" font-size="9pt" font-family="tahoma" line-height="16pt" > 
        {L-ENG PER TYPE-L}{L-INA PER JENIS-L}
      </fo:block>  --%>
 
        </fo:static-content> 
        
        <fo:flow flow-name="xsl-region-body"> 
            
            <fo:block font-size="9pt" text-align="center">
                <% if (form.getPeriodFrom()!=null || form.getPeriodTo()!=null) {%> 
                Periode : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
                <% } %>
            </fo:block> 
            
            <fo:block font-size="9pt">
                <% if (form.getStBranchDesc()!=null) {%> 
                Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>  
                <% } %>
            </fo:block>
            
            <fo:block font-size="9pt">
                <% if (form.getStFltCoverTypeDesc()!=null) {%> 
                Jenis Penutupan  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>  
                <% } %>
            </fo:block>
            
            <fo:block font-size="9pt">
                <% if (form.getStEntityName()!=null) {%> 
                Customer Name :<%=JSPUtil.printX(form.getStEntityName())%>  
                <% } %>
            </fo:block>
            
            <fo:block font-size="9pt"> 
                <fo:table table-layout="fixed"> 
                    <fo:table-column column-width="10mm"/> 
                    <fo:table-column column-width="40mm"/> 
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="40mm"/> 
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="40mm"/>
                    <%--       <fo:table-column column-width="40mm"/>--%>
                    <fo:table-body> 
                        
                        <!-- GARIS DALAM KOLOM --> 
 
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="8" > 
                                <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row> 
                        
                        <fo:table-row> 
                            <fo:table-cell ><fo:block text-align="center">No</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Class Of Business</fo:block></fo:table-cell>           
                            <fo:table-cell ><fo:block text-align="center">Curr</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Gross Premium</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">R/I Comm</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Net Premium</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Claims Paid</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Balance Due</fo:block></fo:table-cell>  
                        </fo:table-row> 
                        
                        
                        <!-- GARIS DALAM KOLOM --> 
 
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="8" > 
                                <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row> 
                        
                        <fo:table-row> 
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ><fo:block>FIRE SURPLUS</fo:block></fo:table-cell>
                            <fo:table-cell columns-rows-spanned="5"></fo:table-cell>
                        </fo:table-row> 
                        
                        <%
                        for (int i = 0; i < l.size(); i++) {
    InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                        %>        
                        
                        <fo:table-row> 
                            <fo:table-cell ><fo:block text-align="center"><%= String.valueOf(i+1) %></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= JSPUtil.printX(pol.getStCoverDesc()) %></fo:block></fo:table-cell>           
                            <fo:table-cell><fo:block>
                                    <fo:block font-size="10pt">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="10mm"/>
                                            <fo:table-column column-width="40mm"/>
                                            <fo:table-column column-width="40mm"/>
                                            <fo:table-column column-width="40mm"/>
                                            <fo:table-column />
                                            <fo:table-body>
                                                <%
                                                DTOList cover = pol.getCoverage();
                                                
                                                for (int j = 0; j < cover.size(); j++) {
                                                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) cover.get(j);
                                                %>	    
                                                
                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block text-align="end"><%= JSPUtil.printX(cov.getStCCY()) %></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%= JSPUtil.printX(cov.getDbPremiTotal(),0) %></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%= JSPUtil.printX(cov.getDbPremiNetto(),0) %></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%= JSPUtil.printX(cov.getDbClaimAmt(),0) %></fo:block></fo:table-cell>
                                                </fo:table-row>
                                                <% } %>           
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>  	
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% } %>
                        
                        
                    </fo:table-body> 
                </fo:table> 
            </fo:block>
            <fo:block> </fo:block> 
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block> 
        </fo:flow> 
    </fo:page-sequence> 
</fo:root> 

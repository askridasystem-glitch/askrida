<%@ page import="com.webfin.pks.model.PerjanjianKerjasamaView, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.pks.form.PKSProductionReportForm, 
com.webfin.gl.model.GLCostCenterView,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final PKSProductionReportForm form = (PKSProductionReportForm)SessionManager.getInstance().getCurrentForm();

PerjanjianKerjasamaView policy = (PerjanjianKerjasamaView) l.get(0);

final GLCostCenterView cc_code = policy.getCostCenter(form.getStBranch());

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="only" 
                               page-height="21cm"
                               page-width="32cm"
                               margin-top="1cm"
                               margin-bottom="0.5cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            
            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/> 
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
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="30mm"/><!-- Entry Date --> 
                    <fo:table-column column-width="50mm"/><!-- Police Date -->
                    <fo:table-column column-width="40mm"/><!-- Policy No. -->
                    <fo:table-column column-width="40mm"/><!-- PLA No. --> 
                    <fo:table-column column-width="30mm"/><!-- DLA No. --> 
                    <fo:table-column column-width="30mm"/><!-- Name of Insured --> 
                    <fo:table-column column-width="30mm"/><!-- Object --> 
                    <fo:table-column column-width="30mm"/><!-- Total Sum Insured --> 
                    <fo:table-column column-width="20mm"/><!-- Claim Estimated --> 
                    <fo:table-header>   
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">             
                                    DAFTAR PERJANJIAN KERJASAMA
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-family="serif" font-size="16pt" text-align="center">             
                                    CABANG <%=JSPUtil.printX(cc_code.getStDescription().toUpperCase())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-family="serif" font-weight="bold" font-size="18pt" text-align="center">
                                    <% if (form.getReceiveDateFrom()!=null || form.getReceiveDateFrom()!=null) {%> 
                                    Tanggal Penerimaan : <%=JSPUtil.printX(form.getReceiveDateFrom())%> S/D <%=JSPUtil.printX(form.getReceiveDateFrom())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-family="serif" font-weight="bold" font-size="18pt" text-align="center">
                                    <% if (form.getRemarkDateFrom()!=null || form.getRemarkDateTo()!=null) {%> 
                                    Tanggal Penandatangan : <%=JSPUtil.printX(form.getRemarkDateFrom())%> S/D <%=JSPUtil.printX(form.getRemarkDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold" text-align="center">
                                    <% if (form.getPeriodFrom()!=null || form.getPeriodTo()!=null) {%> 
                                    Periode : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStBranchDesc()!=null) { %>
                                    Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStPolicyTypeDesc()!=null) {%> 
                                    Jenis Polis : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center">{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Branch -L}{L-INA Cabang -L}</fo:block></fo:table-cell><!-- Entry Date --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Type -L}{L-INA Jenis Polis -L}</fo:block></fo:table-cell><!-- Police Date --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG BPD No. -L}{L-INA No. BPD -L}</fo:block></fo:table-cell><!-- Policy No. --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG ASKRIDA No. -L}{L-INA No. ASKRIDA -L}</fo:block></fo:table-cell><!-- PLA No. --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Remark Date -L}{L-INA Tanggal Penandatangan -L}</fo:block></fo:table-cell><!-- DLA No. --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Receipt Date  -L}{L-INA Tanggal Penerimaan -L}</fo:block></fo:table-cell><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Period Start -L}{L-INA Periode Awal -L}</fo:block></fo:table-cell><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Period End -L}{L-INA Periode Akhir -L}</fo:block></fo:table-cell><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Desc -L}{L-INA Ket -L}</fo:block></fo:table-cell><!-- Total Sum Insured --> 
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header> 
                    
                    <fo:table-body>
                        
                        <% 
                        
                        for (int i = 0; i < l.size(); i++) {
                            PerjanjianKerjasamaView pol = (PerjanjianKerjasamaView) l.get(i);
                               
                        %>   
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=i+1%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(cc_code.getStDescription())%></fo:block></fo:table-cell>    <!-- No --><!-- Entry Date -->
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStPolicyTypeDesc2())%></fo:block></fo:table-cell>    <!-- No --><!-- Police Date --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStBankNo())%></fo:block></fo:table-cell>    <!-- No --><!-- PLA No. --> 
                            <fo:table-cell ><fo:block text-align="center"><%=DateUtil.getDateStr(pol.getDtPolicyDate())%></fo:block></fo:table-cell>    <!-- No --><!-- DLA No. --> 
                            <fo:table-cell ><fo:block text-align="center"><%=DateUtil.getDateStr(pol.getDtReceiveDate())%></fo:block></fo:table-cell>    <!-- No --><!-- DLA No. --> 
                            <fo:table-cell ><fo:block text-align="center"><%=DateUtil.getDateStr(pol.getDtPeriodStart())%></fo:block></fo:table-cell>    <!-- No --><!-- DLA No. --> 
                            <fo:table-cell ><fo:block text-align="center"><%=DateUtil.getDateStr(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>    <!-- No --><!-- DLA No. --> 
                            <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>    <!-- No --><!-- Claim Approved -->   
                        </fo:table-row>   
                        
                        <% } %>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>   
                        
                    </fo:table-body>   
                </fo:table>   
            </fo:block> 
            
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block>    
            
            
            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                        <svg xmlns="http://www.w3.org/2000/svg" width="5cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
        </fo:flow>   
        
    </fo:page-sequence>   
</fo:root>   

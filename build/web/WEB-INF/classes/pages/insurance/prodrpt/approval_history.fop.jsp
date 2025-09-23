<%@ page import="com.webfin.insurance.model.*, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.Tools, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionMarketingReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionMarketingReportForm form = (ProductionMarketingReportForm)SessionManager.getInstance().getCurrentForm();

//if (true) throw new NullPointerException();

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-height="28cm"
                               page-width="31.5cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
            <fo:region-before extent="0cm"/> 
            <fo:region-after extent="0.5cm"/> 
        </fo:simple-page-master> 
        
    </fo:layout-master-set> 
    <!-- end: defines page layout --> 
 
    <!-- actual layout --> 
    <fo:page-sequence master-reference="first"> 
        
        <!-- usage of page layout --> 
        <!-- header --> 
        <fo:static-content flow-name="xsl-region-before"> 
            
            <fo:block-container height="0cm" width="0cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="0cm" width="0cm" top="0cm" left="0cm" position="absolute">    <fo:block>
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
                    <fo:table-column column-width="35mm"/><!-- Policy No -->
                    <fo:table-column column-width="31mm"/><!-- tgl ubah -->
                    <fo:table-column column-width="15mm"/><!-- user id--> 
                    <fo:table-column column-width="31mm"/><!-- tgl approve-->
                    <fo:table-column column-width="20mm"/><!-- user id-->
                    <fo:table-column column-width="35mm"/><!-- user name-->
                    <fo:table-column column-width="22mm"/><!-- client ip-->
                    <fo:table-column column-width="53mm"/><!-- password-->
                    <fo:table-column column-width="53mm"/><!-- password master-->
                    <fo:table-header>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">             
                                    LAPORAN PERSETUJUAN    
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block  font-weight="bold" font-size="18pt" text-align="center">
                                    <% if (form.getAppDateFrom()!=null || form.getAppDateTo()!=null) {%> 
                                    Tanggal Persetujuan : <%=JSPUtil.printX(form.getAppDateFrom())%> S/D <%=JSPUtil.printX(form.getAppDateTo())%>
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
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStPolicyTypeGroupDesc()!=null) {%> 
                                    Jenis Group : <%=JSPUtil.printX(form.getStPolicyTypeGroupDesc())%>  
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
                                <fo:block font-weight="bold">
                                    <% if (form.getStFltCoverTypeDesc()!=null) {%> 
                                    Jenis Penutupan  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCustCategory1Desc()!=null) {%> 
                                    Sumber Bisnis : <%=JSPUtil.printX(form.getStCustCategory1Desc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStEntityName()!=null) {%> 
                                    Customer Name :<%=JSPUtil.printX(form.getStEntityName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStMarketerName()!=null) {%> 
                                    Marketer Name :<%=JSPUtil.printX(form.getStMarketerName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCompanyName()!=null) {%> 
                                    Company Name :<%=JSPUtil.printX(form.getStCompanyName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCoinsurerName()!=null) {%> 
                                    Coinsurer Name :<%=JSPUtil.printX(form.getStCoinsurerName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCustStatus()!=null) {%> 
                                    Customer Status :<%=JSPUtil.printX(form.getStCustStatus())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>                        
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10"><fo:block text-align="end" font-weight="bold"></fo:block></fo:table-cell>  
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Number -L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Change Date-L}{L-INA Tanggal Ubah -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG User ID-L}{L-INA User ID-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Approval Date-L}{L-INA Tanggal Approve -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Approved Who-L}{L-INA User Approve-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Approved Who-L}{L-INA User Approve-L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Client IP-L}{L-INA IP Address-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Password Approval-L}{L-INA Password Approval-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Password User-L}{L-INA Password User-L}</fo:block></fo:table-cell>                        
                        </fo:table-row>  
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <!-- GARIS  -->  
                        <!--   \<fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt"></fo:block> -->  
     
                        
                        <%   
                        int counter = 70;
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n=0;
                            
                            String no_polis = pol.getStPolicyNo();
                            
                            String no_polis_cetak = no_polis.substring(0,4)+"-"+no_polis.substring(4,8)+"-"+no_polis.substring(8,12)+"-"+no_polis.substring(12,16)+"-"+no_polis.substring(16,18);
                            
                            if (i==counter) {
                                counter = counter + 70;
                        
                        %>   
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                        
                            }%>
                        
                        
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtChangeDate(),"d-MMM-yyyy hh:mm:ss")%> </fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStChangeWho())%></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtApprovedDate(),"d-MMM-yyyy hh:mm:ss")%> </fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStApprovedWho())%></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStReference3())%></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClientIP())%></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStPassword())%></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStReference4())%></fo:block></fo:table-cell>                         
                        </fo:table-row>
                        
                        <% 
                        
                        } %>   
                        
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10">  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                      
                        
                    </fo:table-body>   
                </fo:table>   
            </fo:block> 
            
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block>    
            
            
            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
            
            
        </fo:flow> 
    </fo:page-sequence>   
</fo:root>   
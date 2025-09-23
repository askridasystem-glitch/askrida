<%@ page import="com.webfin.insurance.model.*, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal,
java.util.Date, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionFinanceReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionFinanceReportForm form = (ProductionFinanceReportForm)SessionManager.getInstance().getCurrentForm();

BigDecimal TotalComm = new BigDecimal(0);
BigDecimal TotalTax = new BigDecimal(0);
BigDecimal TotalPremiBruto = new BigDecimal(0);
BigDecimal TotalPremiNetto = new BigDecimal(0);

String cabang = null;

if (SessionManager.getInstance().getSession().getStBranch()!=null) {
    cabang = form.getStBranchName();
} else {
    cabang = form.getStBranchDesc();
}

String region = null;

if (SessionManager.getInstance().getSession().getStRegion()!=null) {
    region = form.getStRegionName();
} else {
    region = form.getStRegionDesc();
}

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="5cm"
                               margin-bottom="2cm"
                               margin-left="1cm"
                               margin-right="1cm">
            
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
            <fo:region-before extent="0.5cm"/> 
            <fo:region-after extent="0.5cm"/> 
        </fo:simple-page-master> 
        
    </fo:layout-master-set> 
    <!-- end: defines page layout --> 
 
    <!-- actual layout --> 
    <fo:page-sequence master-reference="first">  
        
        <!-- usage of page layout --> 
        <!-- header --> 
        <fo:static-content flow-name="xsl-region-before"> 
            
            <fo:block-container height="1cm" width="0cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="1cm" width="0cm" top="0cm" left="0cm" position="absolute">    <fo:block>
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
            
            <!-- defines text title level 1--> 
  
            <!-- GARIS  -->  
       
            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="35mm"/>
                   
                    <fo:table-header> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">             
                                    UMUR PIUTANG PREMI
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">
                                    <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                         <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">
                                    Per : <%=JSPUtil.printX(form.getPerDateFrom())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG DAERAH -L}{L-INA DAERAH -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG OUTSTANDING PREMI -L}{L-INA OUTSTANDING PREMI -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG OUTSTANDING PREMI -L}{L-INA OUTSTANDING PREMI -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" border-right-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG JUMLAH-L}{L-INA JUMLAH-L}</fo:block></fo:table-cell>
                        </fo:table-row>

                         <fo:table-row>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">1-60 hari</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center"> lebih dari 60 hari</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" border-right-style="solid" padding="2pt" ><fo:block text-align="center"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>    
                        
                    </fo:table-header>             
                    <fo:table-body>      
                        <!-- GARIS DALAM KOLOM -->  
                        <%   
                        
                        BigDecimal premi = BigDecimal.ZERO;
                        BigDecimal premi160 = BigDecimal.ZERO;
                        BigDecimal totalpremi = BigDecimal.ZERO;
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                       
                            premi = BDUtil.add(premi, pol.getDbPremiTotal());
                            premi160 = BDUtil.add(premi160, pol.getDbPremiNetto());
                             totalpremi = BDUtil.add(premi160, premi);

                        
                        %>   
                      
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=i+1%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="left"><%=JSPUtil.printX(pol.getCostCenter(pol.getStCostCenterCode()).getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiNetto(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(pol.getDbPremiTotal(),pol.getDbPremiNetto()),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <%
                            }
                        %>
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="2"  border-left-style="solid" padding="2pt"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(premi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(premi160,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" border-right-style="solid" padding="2pt" ><fo:block text-align="end"><%=JSPUtil.printX(totalpremi,0)%></fo:block></fo:table-cell>
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>   
                        
                    </fo:table-body>   
                </fo:table>   
            </fo:block>
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block>   
            <fo:block font-size="8pt">
                {L-ENG Print User-L}{L-INA Print User-L} : <%=SessionManager.getInstance().getCreateUser().getStShortName()%>  
            </fo:block>     
            
            
            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(totalpremi,0)%>" orientation="0">
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
                        <svg xmlns="http://www.w3.org/2000/svg" width="0cm" height="1cm" xml:space="preserve">
                            <rect style="fill:white;stroke:white" x="0" y="0" width="0cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
            
        </fo:flow>   
    </fo:page-sequence>   
</fo:root> 
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

BigDecimal outstandingClaim = new BigDecimal(0);

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
                               page-height="21cm"
                               page-width="30cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
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
            
            <!-- defines text title level 1--> 
  
            <!-- GARIS  -->  
       
            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-header> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">             
                                    LAPORAN OUTSTANDING KLAIM REAS
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">
                                    <% if (form.getClaimDateFrom()!=null || form.getClaimDateTo()!=null) {%> 
                                    Tanggal Klaim : <%=JSPUtil.printX(form.getClaimDateFrom())%> S/D <%=JSPUtil.printX(form.getClaimDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">
                                    <% if (form.getAppDateFrom()!=null || form.getAppDateTo()!=null) {%> 
                                    Tanggal Setujui Klaim : <%=JSPUtil.printX(form.getAppDateFrom())%> S/D <%=JSPUtil.printX(form.getAppDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <% if (cabang!=null) { %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    Cabang : <%=JSPUtil.printX(cabang)%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <% if (form.getStRegion()!=null) { %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    Outlet : <%=JSPUtil.printX(region)%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    <% if (form.getStPolicyTypeDesc()!=null) {%> 
                                    Jenis Polis : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    <% if (form.getStEntityName()!=null) {%> 
                                    Customer Name :<%=JSPUtil.printX(form.getStEntityName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Policy Date-L}{L-INA Tgl. Polis-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Policy No.-L}{L-INA No. Polis -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Surat Hutang-L}{L-INA Surat Hutang -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG DLA Date-L}{L-INA Tgl. LKP-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG DLA No.-L}{L-INA No. LKP -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Claim Total-L}{L-INA Total Klaim-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Claim Reins-L}{L-INA Klaim Reas-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" border-right-style="solid" padding="2pt"><fo:block text-align="center">{L-ENG O/S Claim Reins-L}{L-INA Hutang Klaim Reas-L}</fo:block></fo:table-cell>  	
                        </fo:table-row>   
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>    
                        
                    </fo:table-header>             
                    <fo:table-body>      
                        <!-- GARIS DALAM KOLOM -->  
                        <%   
                        BigDecimal [] t = new BigDecimal[4];
                        
                        int counter = 30;
                        BigDecimal subTotalInsured = null;
                        BigDecimal subTotalClaim = null;
                        BigDecimal subTotalClaimCoins = null;
                        BigDecimal subTotalOutsClaim = null;
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());
                            t[n] = BDUtil.add(t[n++], pol.getDbClaimAmountApproved());
                            t[n] = BDUtil.add(t[n++], pol.getDbTotalDue());
                            
                            outstandingClaim = BDUtil.sub(pol.getDbTotalDue(),pol.getDbAPComisP());
                            t[n] = BDUtil.add(t[n++], outstandingClaim); //total tagihan komisi
                            
                            String no_polis = pol.getStPolicyNo();
                            String no_polis_cetak = no_polis.substring(0,4)+"-"+no_polis.substring(4,8)+"-"+no_polis.substring(8,12)+"-"+no_polis.substring(12,16)+"-"+no_polis.substring(16,18);
                            
                            if (i==counter) {
                                counter = counter + 30;
                        %>   
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell number-columns-spanned="7"><fo:block text-align="center">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalInsured,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalClaim,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalClaimCoins,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalOutsClaim,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                        subTotalInsured 	= null;
                        subTotalClaim 		= null;
                        subTotalClaimCoins	= null;
                        subTotalOutsClaim 	= null;
                            }%>
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=i+1%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getEntity2(pol.getStEntityID()).getStShortName())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtDLADate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStDLANo())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmountApproved(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalDue(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(outstandingClaim,0)%></fo:block></fo:table-cell>
                        </fo:table-row>     
                        <% 
                        subTotalInsured     = BDUtil.add(subTotalInsured,pol.getDbInsuredAmount());
                        subTotalClaim       = BDUtil.add(subTotalClaim,pol.getDbClaimAmountApproved());
                        subTotalClaimCoins  = BDUtil.add(subTotalClaimCoins,pol.getDbTotalDue());
                        subTotalOutsClaim   = BDUtil.add(subTotalOutsClaim,pol.getDbAPComisP());
                        } %>    
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0],0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1],0)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2],0)%></fo:block></fo:table-cell>   
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3],0)%></fo:block></fo:table-cell>  
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
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
                        <svg xmlns="http://www.w3.org/2000/svg" width="0cm" height="0cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="0cm" height="0cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
            
        </fo:flow>   
    </fo:page-sequence>   
</fo:root> 
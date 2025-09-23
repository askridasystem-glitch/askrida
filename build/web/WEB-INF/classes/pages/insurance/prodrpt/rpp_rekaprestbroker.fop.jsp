<%@ page import="com.webfin.insurance.model.*, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.crux.common.parameter.Parameter,
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
                               page-height="21.5cm"
                               page-width="30cm"
                               margin-top="1.5cm"
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
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block>   
            
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
                    <fo:table-column column-width="65mm"/>
                    <fo:table-column column-width="65mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-header>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-size="12pt" font-weight="bold">      
                                    Data Rekapitulasi Restitusi 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-size="12pt" font-weight="bold"> 
                                    <%=JSPUtil.printX(form.getStEntityName())%>  
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-size="12pt" font-weight="bold">      
                                    Pasca Broker
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-size="12pt"> 
                                    Nomor Surat : <%=JSPUtil.printX(form.getStNoUrut())%>  
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Branch Name-L}{L-INA Nama Cabang/KCP BJB-L}</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Name of Member-L}{L-INA Nama Peserta-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Time Length-L}{L-INA Jangka Waktu -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Liquidity Start-L}{L-INA Tanggal Realisasi-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Restitution Date -L}{L-INA Tanggal Restitusi-L}</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Sum Insured -L}{L-INA Harga Pertanggungan -L}</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Contract-L}{L-INA Sisa Kontrak-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Premium -L}{L-INA Premi -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Premium -L}{L-INA Pengembalian Premi -L}</fo:block></fo:table-cell>  
                        </fo:table-row>  
                        
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <!-- GARIS  -->  
                        <!--   \<fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt"></fo:block> -->  
     
                        
                        <%   
                        int counter = 25;
                        BigDecimal subTotalInsured = null;
                        BigDecimal subTotalPremi = null;
                        BigDecimal subTotalPremium = null;
                        
                        BigDecimal [] t = new BigDecimal[3];
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());
                            t[n] = BDUtil.add(t[n++], pol.getDbReference5());
                            t[n] = BDUtil.add(t[n++], pol.getDbReference4());
                            
                            if (i==counter) {
                                counter = counter + 25;
                                
                        /*   	  int p=-1;
 
                        final DTOList objects = pol.getObjects();
 
                        while (true) {
                        p++;
                        if (p>=objects.size()) break;
 
                        final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(p);
                         */
                        %>   
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell number-columns-spanned="9" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm" font-weight="bold"><%=JSPUtil.printX(BDUtil.negate(subTotalPremium),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                        subTotalPremium = null;
                            }%>
                        
                        
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm"><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm"><%=JSPUtil.printX(pol.getStReference1())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getStReference6())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getDtReference2())%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getDtReference5())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.negate(pol.getDbInsuredAmount()),0)%></fo:block></fo:table-cell>    <!-- No --><!-- The Insured --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getStReference2())%></fo:block></fo:table-cell>    <!-- No --><!-- Premium --> 
                            <% if (pol.getDbReference3()!=null) { %>             
                            <fo:table-cell  border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm" ><%=JSPUtil.printX(pol.getDbReference3(),0)%></fo:block></fo:table-cell>
                            <% }  else { %> 
                            <fo:table-cell  border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm" ><%=JSPUtil.printX(BDUtil.negate(BDUtil.mul(pol.getDbInsuredAmount(),BDUtil.getRateFromMile(pol.getDbReference5()))),0)%></fo:block></fo:table-cell>
                            <% } %>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.negate(pol.getDbReference4()),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Discount --> 
                        </fo:table-row>
                        
                        <% 
                        subTotalPremium = BDUtil.add(subTotalPremium,pol.getDbReference4());
                        
                        } %>   
                        
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="9" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm" font-weight="bold"> TOTAL : </fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.negate(t[2]),0)%></fo:block></fo:table-cell><!-- Total Fee --> 
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="20pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                    </fo:table-body>   
                </fo:table>   
            </fo:block> 
            
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block >
                                    <% if (form.getStBranchName()!=null) { %>
                                    <%=JSPUtil.printX(form.getStBranchName())%>,
                                    <% } %>
                                    <% if (form.getStBranchDesc()!=null) { %>
                                    <%=JSPUtil.printX(form.getStBranchDesc())%>,
                                    <% } %> 
                                <%=DateUtil.getDateStr(new Date(),"dd ^^ yyyy")%></fo:block>
                                <fo:block >PT. Asuransi Bangun Askrida</fo:block>
                                <fo:block >
                                    <% if (form.getStBranchName()!=null) { %>
                                    Cabang <%=JSPUtil.printX(form.getStBranchName())%>
                                    <% } %>
                                    <% if (form.getStBranchDesc()!=null) { %>
                                    Cabang <%=JSPUtil.printX(form.getStBranchDesc())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <%       if(SessionManager.getInstance().getSession().getStBranch()!=null){
                            if(SessionManager.getInstance().getSession().getStBranch().equalsIgnoreCase("00")){
                        %>          
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline">GATOT TRI SUSILO</fo:inline></fo:block>
                                <fo:block >Kabag. Underwriting</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% 						}if(!SessionManager.getInstance().getSession().getStBranch().equalsIgnoreCase("00")){%>
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block ><fo:inline text-decoration="underline"><%= Parameter.readString("BRANCH_SIGN_"+SessionManager.getInstance().getSession().getStBranch())%></fo:inline></fo:block>
                                <fo:block >Kepala Cabang</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }} %>
                        
                    </fo:table-body>
                </fo:table>
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
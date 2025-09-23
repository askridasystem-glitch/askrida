<%@ page import="com.webfin.insurance.model.*, 
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionMarketingReportForm, 
com.webfin.system.region.model.RegionView,
com.webfin.entity.model.EntityView,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionMarketingReportForm form = (ProductionMarketingReportForm)SessionManager.getInstance().getCurrentForm();

//if (true) throw new NullPointerException();

BigDecimal [] t = new BigDecimal[4];

InsurancePolicyView policy = (InsurancePolicyView) l.get(0);

final EntityView entity = policy.getEntity2(form.getStEntityID());

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="only" 
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="2cm"
                               margin-right="0.5cm">
            
            <fo:region-body margin-top="2cm" margin-bottom="0cm"/> 
            <fo:region-before extent="0cm"/> 
            <fo:region-after extent="0.5cm"/> 
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
            
            <fo:block font-size="10pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="150mm"/>
                    <fo:table-header>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block font-family="tahoma" font-weight="bold"> REKAPITULASI DAFTAR PESERTA ASURANSI <%= JSPUtil.printX(form.getStPolicyTypeDesc().toUpperCase()) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block font-family="tahoma" font-weight="bold">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block font-family="tahoma" font-weight="bold">BULAN</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-family="tahoma" font-weight="bold">:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-family="tahoma" font-weight="bold"><%=DateUtil.getDateStr(form.getPolicyDateTo(),"^^ yyyy")%> ( <%=DateUtil.getDateStr(form.getPolicyDateFrom(),"d ^^ yyyy")%> s/d <%=DateUtil.getDateStr(form.getPolicyDateTo(),"d ^^ yyyy")%> )</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% if (form.getStNoUrut()!=null) { %>   
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block font-family="tahoma" font-weight="bold">NO. REK</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-family="tahoma" font-weight="bold">:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-family="tahoma" font-weight="bold"><%= JSPUtil.printX(form.getStNoUrut()) %>/<%= JSPUtil.printX(entity.getStShortName()) %>.<%= JSPUtil.printX(entity.getStRefEntityID()) %>/AK.21/<%=DateUtil.getMonthRomawi(form.getPolicyDateTo())%>/<%=DateUtil.getYear2Digit(form.getPolicyDateTo())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% } else { %>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block font-family="tahoma" font-weight="bold">NO. REK</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-family="tahoma" font-weight="bold">:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-family="tahoma" font-weight="bold"><%= JSPUtil.printX(form.getStRekapNo()) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% } %>      
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>  
                        
                        
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block space-after.optimum="3pt">
                                    <% if (form.getStEntityName()!=null) {%>    
                                    UNTUK <%= JSPUtil.printX(form.getStEntityName()) %>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-header>
                    <fo:table-body>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            
            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="40mm"/><!-- Policy No -->
                    <fo:table-column column-width="40mm"/><!-- No. Reg --> 
                    <fo:table-column column-width="15mm"/><!-- Jumlah --> 
                    <fo:table-column column-width="30mm"/><!-- Insured --> 
                    <fo:table-column column-width="30mm"/><!-- Premi Koas -->         
                    <fo:table-column column-width="15mm"/><!-- Ket -->
                    <fo:table-header>      
                        
                        <fo:table-row>   
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>  
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell> 
                            <fo:table-cell ></fo:table-cell> 
                            <fo:table-cell number-columns-spanned="2" ><fo:block text-align="end" font-weight="bold">{L-ENG (In IDR)-L}{L-INA (dalam rupiah)-L}</fo:block></fo:table-cell>  
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">No.</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">NOMOR POLIS</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">NOMOR REGISTRASI</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">JUMLAH PESERTA</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">UANG PERTANGGUNGAN</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">PREMI</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">KET</fo:block></fo:table-cell> 
                        </fo:table-row>  
                        
                        
                        
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <!-- GARIS  -->  
     
                        
                        <% int counter = 45;
                        BigDecimal subTotalJumlah = null;
                        BigDecimal subTotalTSI = null;
                        BigDecimal subTotalPremiKo = null;
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiKo());
                            t[n] = BDUtil.add(t[n++], pol.getDbJumlah());
                            
                            String no_polis = pol.getStPolicyNo();
                            
                            String no_polis_cetak = no_polis.substring(0,4)+"-"+no_polis.substring(4,8)+"-"+no_polis.substring(8,12)+"-"+no_polis.substring(12,16)+"-"+no_polis.substring(16,18);
                            
                            if (i==counter) {
                                counter = counter + 45;
                                
                        /*   	  int p=-1;
 
                        final DTOList objects = pol.getObjects();
 
                        while (true) {
                        p++;
                        if (p>=objects.size()) break;
 
                        final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(p);
                         */
                        %>   
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" number-columns-spanned="3"><fo:block text-align="center" line-height="5mm">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalJumlah,0)%> <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalTSI,0)%> <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiKo,0)%> <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>   
                        
                        
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                        subTotalJumlah = null;
                        subTotalTSI = null;
                        subTotalPremiKo = null;
                            }%>
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm" line-weight="5mm"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm" line-weight="5mm"><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" line-weight="5mm"><fo:inline color="white">.</fo:inline> <%=String.valueOf(i+1)%>/AK.<%=JSPUtil.printX(pol.getStPolicyTypeID())%>/<%=JSPUtil.printX(pol.getStKreasiTypeID())%>/<%=DateUtil.getMonthRomawi(form.getPolicyDateTo())%>/<%=DateUtil.getYear2Digit(form.getPolicyDateTo())%></fo:block></fo:table-cell>    <!-- No --><!-- The Insured -->     <!-- No --><!-- Policy No --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm" line-weight="5mm"><%=JSPUtil.printX(pol.getDbJumlah(),0)%> <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>    <!-- No --><!-- The Insured --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm" line-weight="5mm"><%=JSPUtil.printX(pol.getDbInsuredAmount(),0)%> <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell> <!-- No --><!-- Premium --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm" line-weight="5mm"><%=JSPUtil.printX(pol.getDbPremiKo(),0)%> <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>    <!-- No --><!-- Discount --> <!-- No --><!-- Premium --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getStDescription())%></fo:block></fo:table-cell>     
                        </fo:table-row> 
                        
                        <%  	
                        subTotalJumlah = BDUtil.add(subTotalJumlah,pol.getDbJumlah());      
                        subTotalTSI = BDUtil.add(subTotalTSI,pol.getDbInsuredAmount());
                        subTotalPremiKo = BDUtil.add(subTotalPremiKo,pol.getDbPremiKo());	
                        }%>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="3" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">TOTAL :</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[2],0)%> <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[0],0)%> <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[1],0)%> <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"></fo:block></fo:table-cell><!-- Total Fee --> 
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>   
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
                        <svg xmlns="http://www.w3.org/2000/svg" width="15cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
        </fo:flow>   
        
    </fo:page-sequence>   
</fo:root>   
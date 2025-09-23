<%@ page import="com.webfin.insurance.model.*, 
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionClaimReportForm, 
com.webfin.system.region.model.RegionView,
com.webfin.entity.model.EntityView,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionClaimReportForm form = (ProductionClaimReportForm)SessionManager.getInstance().getCurrentForm();

InsurancePolicyView policy = (InsurancePolicyView) l.get(0);

final EntityView entity = policy.getEntity2(form.getStEntityID());

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="only" 
                               page-height="29.5cm"
                               page-width="21cm"
                               margin-top="3cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="0.5cm">
            
            <fo:region-body margin-top="1.5cm" margin-bottom="0.5cm"/> 
            <fo:region-before extent="1cm"/> 
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
            
            <fo:block font-size="12pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="100mm" />
                    <fo:table-column />
                    <fo:table-body>    
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >Nomor : <%= JSPUtil.printX(form.getStNoUrut()) %></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="2" >  
                                <fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  number-columns-spanned="2"><fo:block >Kepada Yth.</fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell  number-columns-spanned="2"><fo:block ><%= JSPUtil.printX(form.getStEntityName()) %></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block ><%= JSPUtil.printX(entity.getStAddress()) %></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell >
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="2" >  
                                <fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  number-columns-spanned="2"><fo:block >Up. Yth, <%= JSPUtil.printX(form.getStNama()) %></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="2" >  
                                <fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center" >Perihal : <fo:inline text-decoration="underline">Pengajuan Klaim Asuransi <%= JSPUtil.printX(form.getStPolicyTypeDesc()) %></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="2" >  
                                <fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >Dengan Hormat,</fo:block></fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="2" >  
                                <fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-after.optimum="10pt" text-align="justify">
                                    Bersama ini kami sampaikan kepada bapak LKP-LKP dan berkas-berkas pengajuan klaim <%= JSPUtil.printX(form.getStPolicyTypeDesc()) %>
                                    dengan data-data sebagai berikut : 
                            </fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="9pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="35mm"/><!-- Policy No -->
                    <fo:table-column column-width="40mm"/><!-- No. Reg --> 
                    <fo:table-column column-width="85mm"/><!-- Jumlah --> 
                    <fo:table-column column-width="25mm"/><!-- Insured --> 
                    <fo:table-body>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5" ><fo:block text-align="end" font-weight="bold">{L-ENG (In IDR)-L}{L-INA (dalam rupiah)-L}</fo:block></fo:table-cell>  
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">No.</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">NO. LKP</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">NO. POLIS</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">NAMA TERTANGGUNG</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">KLAIM</fo:block></fo:table-cell> 
                        </fo:table-row>  
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <!-- GARIS  -->  
     
                        
                        <% int counter = 45;
                        BigDecimal subTotalPremiKo = null;
                        
                        BigDecimal [] t = new BigDecimal[1];
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());
                            
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
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" number-columns-spanned="4"><fo:block text-align="center" line-height="5mm">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm"><%=JSPUtil.printX(subTotalPremiKo,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                        subTotalPremiKo = null;
                            }%>
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm" line-weight="5mm"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm" line-weight="5mm"><%=JSPUtil.printX(pol.getStDLANo())%></fo:block></fo:table-cell>   
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm" line-weight="5mm"><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No --> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" line-weight="5mm"><fo:inline color="white">.</fo:inline> <%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbPremiTotal(),0)%> <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>     
                        </fo:table-row> 
                        
                        <%  	
                        subTotalPremiKo = BDUtil.add(subTotalPremiKo,pol.getDbPremiTotal());
                        }%>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" number-columns-spanned="3" ><fo:block line-height="5mm"></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">TOTAL PENGAJUAN KLAIM :</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[0],0)%> <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell><!-- Total Fee --> 
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                    </fo:table-body>   
                </fo:table>   
            </fo:block> 
            
            <fo:block font-size="12pt"> 
                <fo:table table-layout="fixed">    
                    <fo:table-column column-width="100mm" />
                    <fo:table-column />
                    <fo:table-body>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-after.optimum="10pt" text-align="justify">
                                    Demikian pengajuan klaim ini kami sampaikan agar kiranya dapat diterima dengan baik, dan atas perhatian serta kerjasamanya
                                    yang baik selama ini kami ucapkan terima kasih.
                            </fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Hormat Kami,</fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">BAGIAN KLAIM,</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="2" >  
                                <fo:block space-before.optimum="30pt" space-after.optimum="30pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">JATMIKO</fo:inline></fo:block>\
                                <fo:block text-align="center">Kepala Bagian</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>   
                </fo:table>   
            </fo:block>   
            
            
            <%--       
       <fo:block font-size="8pt">
         {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
      </fo:block>    
    --%>
    
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
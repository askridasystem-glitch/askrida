<%@ page import="com.webfin.insurance.model.*, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.Tools, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionClaimReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionClaimReportForm form = (ProductionClaimReportForm)SessionManager.getInstance().getCurrentForm();

String status = "";

//if (true) throw new NullPointerException();

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-height="28cm"
                               page-width="35cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">
            
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
            <fo:region-before extent="0.5cm"/> 
            <fo:region-after extent="0cm"/> 
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
        <%-- 
  <fo:static-content flow-name="xsl-region-after">  
      <fo:block text-align="end"
      font-size="8pt" font-family="serif" line-height="1em + 2pt">
      {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
ref-id="end-of-document"/>
      </fo:block>
    </fo:static-content>
    
  --%>  
        <fo:flow flow-name="xsl-region-body"> 
            
            <fo:block font-size="10pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="25mm"/><!-- Daerah -->
                    <fo:table-column column-width="35mm"/><!-- Pol_No--> 
                    <fo:table-column column-width="20mm"/><!-- Type -->
                    <fo:table-column column-width="45mm"/><!-- Bayar -->
                    <fo:table-column column-width="40mm"/><!-- Bayar -->
                    <fo:table-column column-width="45mm"/><!-- Bayar -->
                    <fo:table-column column-width="40mm"/><!-- Bayar -->
                    <fo:table-column column-width="40mm"/><!-- Bayar -->
                    <fo:table-column column-width="5mm"/><!-- Bayar -->
                    <fo:table-column column-width="20mm"/><!-- Bayar -->
                    <fo:table-header>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="12pt">             
                                    REKAP PRODUKSI KLAIM POS LANGSUNG
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block text-align="end"
                                          font-size="8pt" font-family="serif" line-height="1em + 2pt">
                                    {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                                        ref-id="end-of-document"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="12pt">             
                                    PT. ASURANSI BANGUN ASKRIDA
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                            <fo:block font-weight="bold">JENIS KLAIM</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9"><fo:block>: 
                                    <% if (form.getStPolicyTypeDesc()!=null){ %>
                                    <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>
                                    <% }else{ %>
                                    KESELURUHAN
                                    <% } %>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block font-weight="bold">TANGGAL</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9"><fo:block>: <%=DateUtil.getDateStr(form.getClaimDateFrom(),"d ^^ yyyy")%> s/d <%=DateUtil.getDateStr(form.getClaimDateTo(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11"><fo:block text-align="end" font-weight="bold">{L-ENG (In IDR)-L}{L-INA (dalam rupiah)-L}</fo:block></fo:table-cell>  
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG NO.-L}{L-INA NO.-L}</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG BRANCH -L}{L-INA DAERAH -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG DLA NO. / POLICY NO. -L}{L-INA NO. LKP / NO. POLIS -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG K.O.B -L}{L-INA K.O.B -L}</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG JUMLAH KLAIM LANGSUNG -L}{L-INA JUMLAH KLAIM LANGSUNG -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG KO-ASURANSI DIBAYAR -L}{L-INA KO-ASURANSI DIBAYAR -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG KO-ASURANSI DITERIMA -L}{L-INA KO-ASURANSI DITERIMA -L}</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG HUTANG KLAIM -L}{L-INA HUTANG KLAIM -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG PTG. KLAIM -L}{L-INA PTG. KLAIM -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG DESC. / DATE -L}{L-INA KET / TGL -L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <%   
                        /*   int counter = 55;
                        BigDecimal subTotalKlaim = null;
                        BigDecimal subTotalBayar = null;
                        BigDecimal subTotalTerima = null;
                        BigDecimal subTotalHutang = null;
                        BigDecimal subTotalPiutang = null;
                         */
                        BigDecimal [] t = new BigDecimal[3];
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbClaimAmount());
                            t[n] = BDUtil.add(t[n++], pol.getDbClaimAmountApproved());
                            t[n] = BDUtil.add(t[n++], pol.getDbClaimAmountEndorse());
                            
                            String custName = "";
                            if(pol.getStCustomerName().length()>35)
                                custName = pol.getStCustomerName().substring(0,35);
                            else
                                custName = pol.getStCustomerName().substring(0, pol.getStCustomerName().length());
                            
                            
                        /*
                        if (i==counter) {
                        counter = counter + 55;
 
                        int p=-1;
 
                        final DTOList objects = pol.getObjects();
 
                        while (true) {
                        p++;
                        if (p>=objects.size()) break;
 
                        final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(p);
                         */
                        %> 
                        
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>    
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCostCenterDesc())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStDLANo())%> <%=JSPUtil.printX(pol.getStPolicyNo())%> <%=JSPUtil.printX(custName)%></fo:block></fo:table-cell>    
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStProducerName())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmount(),2)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(new BigDecimal(0),2)%></fo:block></fo:table-cell>   
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmountApproved(),2)%></fo:block></fo:table-cell>    
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmountEndorse(),2)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmountApproved(),2)%></fo:block></fo:table-cell>    
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(pol.getDtDLADate())%> <%=JSPUtil.printX(pol.getStCoinsName())%></fo:block></fo:table-cell>    
                        </fo:table-row>
                        
                        <% 
                        /*
                        subTotalKlaim = BDUtil.add(subTotalKlaim,pol.getDbClaimAmountApproved());
                        subTotalBayar = BDUtil.add(subTotalBayar,pol.getDbNDTaxComm1());
                        subTotalTerima = BDUtil.add(subTotalTerima,pol.getDbNDTaxComm2());
                        subTotalHutang = BDUtil.add(subTotalHutang,pol.getDbNDTaxComm3());
                        subTotalPiutang = BDUtil.add(subTotalPiutang,pol.getDbNDTaxComm4());
                        */
                        } %>   
                        
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="4" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"> TOTAL : </fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(new BigDecimal(0),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[2],2)%></fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="8"></fo:table-cell>   
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center">Bagian Klaim</fo:block></fo:table-cell>   
                        </fo:table-row>
                        
                    </fo:table-body>   
                </fo:table>   
            </fo:block> 
            
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
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
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
        </fo:flow>   
        
    </fo:page-sequence>   
</fo:root>   
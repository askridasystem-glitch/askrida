<%@ page import="com.webfin.insurance.model.*,
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionReinsuranceReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 

<%
final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionReinsuranceReportForm form = (ProductionReinsuranceReportForm)SessionManager.getInstance().getCurrentForm();

String treaty = null;
if(form.getStFltTreatyType().equalsIgnoreCase("SPL")||form.getStFltTreatyType().equalsIgnoreCase("QS"))
    treaty = "SPL QS";
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-width="21cm"
                               page-height="33cm"
                               margin-top="0.5cm"
                               margin-bottom="0.5cm"                                                                                                                                                                           
                               margin-left="1cm"
                               margin-right="1cm">
            <fo:region-body margin-top="2cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
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
                      font-size="6pt"
                      font-family="TAHOMA"
                      line-height="12pt" 
                      font-style="bold">
                rppre_notapremi - PT. Asuransi Bangun Askrida
            </fo:block>
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>
        
        <fo:flow flow-name="xsl-region-body">
            
            <fo:block font-size="12pt" text-align="left">
                {L-ENG Attention To -L}{L-INA Kepada Yth.-L}
            </fo:block> 
            <fo:block font-size="12pt" text-align="left" space-after.optimum="10pt">
                <%=JSPUtil.printX(form.getStEntityName())%>
            </fo:block> 
            
            <fo:block font-size="10pt" space-after.optimum="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="center">{L-ENG REINSURANCE PREMIUM NOTE -L}{L-INA NOTA PREMI REASURANSI-L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG NOTE-L}{L-INA NOTA-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>
                                    <%=DateUtil.getMonth2Digit(form.getPolicyDateFrom())%>
                                    /04R/NPR/<%=DateUtil.getMonthRomawi(form.getPolicyDateFrom())%>/<%=DateUtil.getYear2Digit(form.getPolicyDateFrom())%>
                            </fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG MONTH-L}{L-INA BULAN-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=DateUtil.getDateStr(form.getPolicyDateFrom(),"^^ yyyy")%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>TREATY</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>
                                    <% if(form.getPeriodFrom()!=null){ %>
                                    <%=DateUtil.getYear(form.getPeriodFrom())%>
                                    <% } %>
                                    <%-- - 
                                    <% if(form.getStFltTreatyType()!=null) { %>
                                    <% if(!form.getStFltTreatyType().equalsIgnoreCase("SPL")||!form.getStFltTreatyType().equalsIgnoreCase("QS")) { %>
                                    <%=JSPUtil.printX(form.getStFltTreatyType())%>
                                    <% } %>
                                    <% } %>
                                    --%>
                            </fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="center" space-after.optimum="20pt"></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                    </fo:table-body>
                </fo:table>
            </fo:block> 
            
            <!-- defines text title level 1-->
            <fo:block font-size="7pt" line-height="8pt"></fo:block>
            
            
            <!-- GARIS  -->
            <!-- Normal text -->

 
            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="end" font-weight="bold">{L-ENG (In IDR)-L}{L-INA (dalam rupiah)-L}</fo:block></fo:table-cell>  
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Class Of Bussiness -L}{L-INA Jenis Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Treaty Type-L}{L-INA Jenis Treaty-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Gross Premium-L}{L-INA Premi Gross-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG RI Comm-L}{L-INA Komisi Reas-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Nett-L}{L-INA Netto-L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <% 
                        BigDecimal [] t = new BigDecimal[2];
                        String captive = null;
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiAmt());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDBrok2Pct());
                            
                            /*
                            if (pol.getStReference1().equalsIgnoreCase("1")||
                                    pol.getStReference1().equalsIgnoreCase("4")||
                                    pol.getStReference1().equalsIgnoreCase("6")) {
                                captive = "CAPTIVE";
                            } else if (pol.getStReference1().equalsIgnoreCase("2")||
                                    pol.getStReference1().equalsIgnoreCase("5")||
                                    pol.getStReference1().equalsIgnoreCase("7")) {
                                captive = "NON CAPTIVE";
                            } else if (pol.getStReference1().equalsIgnoreCase("")) {
                                captive = " ";
                            }
                            */
                        %> 
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm"><%=JSPUtil.printX(pol.getStCoverTypeCode())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getStCoTreatyID())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbPremiAmt(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbNDBrok2Pct(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.sub(pol.getDbPremiAmt(), pol.getDbNDBrok2Pct()),2)%></fo:block></fo:table-cell>
                        </fo:table-row>                        
                        
                        <% } %>             
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.sub(t[0], t[1]),2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(new Date(),"d MMM yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center">{L-ENG REINSURANCE DEPT.-L}{L-INA BAGIAN REASURANSI-L}</fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center">S. E. &#x26; O.</fo:block></fo:table-cell>
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
                        <svg xmlns="http://www.w3.org/2000/svg" width="15cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>

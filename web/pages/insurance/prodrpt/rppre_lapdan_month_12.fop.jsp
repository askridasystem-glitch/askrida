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

BigDecimal IDRPremiReas = null;
BigDecimal IDRCommReas = null;
BigDecimal IDRNettReas = null;
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="29cm"
                               page-width="21cm"
                               margin-top="4cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="2cm" margin-bottom="1cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->
    
    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <fo:flow flow-name="xsl-region-body">
            
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column />
                    <fo:table-header>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block font-size="12pt" font-weight="bold" text-align="center">	 
                                    REKAPITULASI LAPORAN PRODUKSI BULANAN
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block text-align="start">	 
                                    Kepada Yth,
                                </fo:block>
                                <fo:block text-align="start">	
                                    Manager Konsorsium Asuransi
                                </fo:block>
                                <fo:block text-align="start">	
                                    Risiko Khusus
                                </fo:block>
                                <fo:block text-align="start">	 
                                    d/a. PT. Tugu Jasatama Reasuransi Indonesia
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="start">	 
                                    No. Rekap : 
                                </fo:block>
                                <fo:block text-align="start">
                                    U. Year   : <% if (form.getPeriodFrom()!=null) { %>
                                    <%=DateUtil.getDateStr(form.getPeriodFrom(),"YYYY")%> / 
                                    <% } %>
                                    <% if (form.getPeriodTo()!=null) { %>
                                    <%=DateUtil.getDateStr(form.getPeriodTo(),"YYYY")%>
                                    <% } %>
                                </fo:block>
                                <fo:block text-align="start">	 
                                    No. Urut : <% if (form.getStNoUrut()!=null) { %><%=form.getStNoUrut()%> <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="center">Tanggal Terima Premi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" ><fo:block text-align="center">Premi Bruto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" ><fo:block text-align="center">Komisi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center">Keterangan</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>                           
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                                                                           
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>                                                
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <%                         
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            IDRPremiReas = BDUtil.add(IDRPremiReas, pol.getDbPremiTotal());
                            IDRCommReas = BDUtil.add(IDRCommReas, pol.getDbNDComm1());
                            IDRNettReas = BDUtil.sub(IDRPremiReas, IDRCommReas);
                            
                            //	if (Tools.compare(pol.getDbPremiTotal(), BDUtil.zero)>0) continue;
                        %>
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="center"><%=DateUtil.getDateStr(form.getPolicyDateTo(),"dd ^^ yyyy")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="center">Rp. </fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%><fo:inline color="white"> . </fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDbAmount(),0)%> %</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm1(),2)%><fo:inline color="white"> . </fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block text-align="center"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="100pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="100pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell ><fo:block space-after.optimum="100pt"></fo:block></fo:table-cell>                                           
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" ><fo:block space-after.optimum="100pt"></fo:block></fo:table-cell>                           
                            <fo:table-cell ><fo:block space-after.optimum="100pt"></fo:block></fo:table-cell>                                     
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid" ><fo:block space-after.optimum="100pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="start">Total</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Rp. </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRPremiReas,2)%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="start">Imbalan jasa</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Rp. </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRCommReas,2)%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="start">Net Premi</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Rp. </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRNettReas,2)%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
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
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(IDRPremiReas,0)%>" orientation="0">
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

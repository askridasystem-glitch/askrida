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

BigDecimal IDRPremi = null;
BigDecimal IDRComm = null;
BigDecimal IDRNetto = null;

BigDecimal nonIDRPremi = null;
BigDecimal nonIDRComm = null;
BigDecimal nonIDRNetto = null;
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-width="34cm"
                               page-height="21cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
        <fo:static-content flow-name="xsl-region-after"> 
            <fo:block-container height="1cm" width="2cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="1cm" width="2cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="term"
                                        retrieve-boundary="page"
                                        retrieve-position="last-ending-within-page"/>
                </fo:block>
            </fo:block-container>
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
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>	
                    <fo:table-header>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-size="10pt" line-height="12pt" text-align="center" space-after.optimum="5pt">	 
                                    LAPORAN KHUSUS <% if(form.getStFltTreatyTypeDesc()!=null) { %>
                                    <%=JSPUtil.printX(form.getStFltTreatyTypeDesc())%><% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-size="10pt" line-height="12pt" text-align="center" space-after.optimum="5pt">	 
                                    BULAN : <%=DateUtil.getDateStr(form.getPolicyDateFrom(),"^^ yyyy")%> s/d <%=DateUtil.getDateStr(form.getPolicyDateTo(),"^^ yyyy")%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-size="10pt" line-height="12pt" space-after.optimum="5pt">	 
                                    TREATY : <% if(form.getPeriodFrom()!=null){ %>
                                    <%=DateUtil.getYear(form.getPeriodFrom())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-size="10pt" line-height="12pt" space-after.optimum="5pt">	 
                                    <% if(form.getStCustCategory1Desc()!=null) { %>
                                    <%=JSPUtil.printX(form.getStCustCategory1Desc())%><% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-size="10pt" line-height="12pt" space-after.optimum="5pt">	 
                                    <% if(form.getStEntityName()!=null) { %>
                                    <%=JSPUtil.printX(form.getStEntityName())%><% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Reins -L}{L-INA Reins -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG R/I Slip-L}{L-INA R/I Slip-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Policy No. -L}{L-INA No. Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Description -L}{L-INA Keterangan -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Period Start -L}{L-INA Periode Awal -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Period End -L}{L-INA Periode Akhir -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Policy Date -L}{L-INA Tanggal Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Curr -L}{L-INA Kurs -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Conv. -L}{L-INA Conv. -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Premium -L}{L-INA Premi -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Comm -L}{L-INA Komisi -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Netto R/I -L}{L-INA Netto -L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <% 
                        
                        int pn = 0;
                        int norut = 0;
                        
                        BigDecimal jumlahIDRPremi = null;
                        BigDecimal jumlahIDRComm = null;
                        BigDecimal jumlahIDRNetto = null;
                        
                        BigDecimal jumlahnonIDRPremi = null;
                        BigDecimal jumlahnonIDRComm = null;
                        BigDecimal jumlahnonIDRNetto = null;
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            if (Tools.isEqual(pol.getDbPremiTotal(), new BigDecimal(0))) continue;
                            
                            norut++;
                            
                            if (pol.getStCurrencyCode().equalsIgnoreCase("IDR")){
                                IDRPremi = BDUtil.add(IDRPremi, pol.getDbPremiTotal());
                                IDRComm = BDUtil.add(IDRComm, pol.getDbNDComm1());
                                IDRNetto = BDUtil.add(IDRNetto, pol.getDbPremiTotalAfterDisc());
                            } else {
                                nonIDRPremi = BDUtil.add(nonIDRPremi, pol.getDbPremiTotal());
                                nonIDRComm = BDUtil.add(nonIDRComm, pol.getDbNDComm1());
                                nonIDRNetto = BDUtil.add(nonIDRNetto, pol.getDbPremiTotalAfterDisc());
                            }
                            
                            String custname = null;
                            if(pol.getStCustomerName().length()>32) {
                                custname = pol.getStCustomerName().substring(0,32);
                            } else {
                                custname = pol.getStCustomerName();
                            }
                            
                            if(i>0){
                                InsurancePolicyView inv2 = (InsurancePolicyView) l.get(i-1);
                                String inward = pol.getStProducerName();
                                String inward2 = inv2.getStProducerName();
                                if(!inward.equalsIgnoreCase(inward2)){
                                    pn++;
                                    
                                    norut = 1;
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahIDRComm,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahIDRNetto,2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">NON IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahnonIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahnonIDRComm,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahnonIDRNetto,2)%></fo:block></fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>   
                        
                        <%--
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        --%>
                        <%
                        jumlahIDRPremi = null;
                        jumlahIDRComm = null;
                        jumlahIDRNetto = null;
                        
                        jumlahnonIDRPremi = null;
                        jumlahnonIDRComm = null;
                        jumlahnonIDRNetto = null;
                                }
                            }  
                        %>
                        
                        
                        <fo:table-row>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(norut)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getStProducerName())%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getStRIFinishFlag())%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="start" line-height="5mm"><%=JSPUtil.printX(custname)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getDtPeriodStart())%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(pol.getDbCurrencyRate(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%>  <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbNDComm1(),2)%>  <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbPremiTotalAfterDisc(),2)%>  <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%                       
                        
                        if (pol.getStCurrencyCode().equalsIgnoreCase("IDR")){
                                jumlahIDRPremi = BDUtil.add(jumlahIDRPremi, pol.getDbPremiTotal());
                                jumlahIDRComm = BDUtil.add(jumlahIDRComm, pol.getDbNDComm1());
                                jumlahIDRNetto = BDUtil.add(jumlahIDRNetto, pol.getDbPremiTotalAfterDisc());
                        } else {
                                jumlahnonIDRPremi = BDUtil.add(jumlahnonIDRPremi, pol.getDbPremiTotal());
                                jumlahnonIDRComm = BDUtil.add(jumlahnonIDRComm, pol.getDbNDComm1());
                                jumlahnonIDRNetto = BDUtil.add(jumlahnonIDRNetto, pol.getDbPremiTotalAfterDisc());
                        }
                        } 
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahIDRComm,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahIDRNetto,2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">NON IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahnonIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahnonIDRComm,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahnonIDRNetto,2)%></fo:block></fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>   
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(IDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(IDRComm,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(IDRNetto,2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">NON IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(nonIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(nonIDRComm,2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(nonIDRNetto,2)%></fo:block></fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
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

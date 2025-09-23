<%@ page import="com.webfin.insurance.model.*, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionClaimReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionClaimReportForm form = (ProductionClaimReportForm)SessionManager.getInstance().getCurrentForm();

//if (true) throw new NullPointerException();

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="only" 
                               page-height="21cm"
                               page-width="35cm"
                               margin-top="1cm"
                               margin-bottom="0.5cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            
            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/> 
            <fo:region-before extent="3cm"/> 
            <fo:region-after extent="1.5cm"/> 
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
            
            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="18mm"/><!-- Entry Date --> 
                    <fo:table-column column-width="18mm"/><!-- Police Date -->
                    <fo:table-column column-width="33mm"/><!-- Policy No. -->
                    <fo:table-column column-width="30mm"/><!-- PLA No. --> 
                    <fo:table-column column-width="30mm"/><!-- DLA No. --> 
                    <fo:table-column column-width="85mm"/><!-- Name of Insured --> 
                    <fo:table-column column-width="30mm"/><!-- Object --> 
                    <fo:table-column column-width="25mm"/><!-- Total Sum Insured --> 
                    <fo:table-column column-width="25mm"/><!-- Claim Estimated --> 
                    <fo:table-column column-width="25mm"/><!-- Claim Approved --> 
                    <fo:table-header>   
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">             
                                    {L-ENG DLA (Definite Loss Advice)-L}{L-INA LKP (Laporan Klaim Pasti)-L}
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-family="serif" font-weight="bold" font-size="18pt" text-align="center">
                                    <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold" text-align="center">
                                    <% if (form.getPeriodFrom()!=null || form.getPeriodTo()!=null) {%> 
                                    Periode : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    <% if (form.getStBranchDesc()!=null) { %>
                                    Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
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
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center">{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG DLA Date -L}{L-INA Tanggal LKP -L}</fo:block></fo:table-cell><!-- Entry Date --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Date -L}{L-INA Tanggal Polis -L}</fo:block></fo:table-cell><!-- Police Date --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy No. -L}{L-INA No. Polis -L}</fo:block></fo:table-cell><!-- Policy No. --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG PLA No. -L}{L-INA No. LKS -L}</fo:block></fo:table-cell><!-- PLA No. --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG DLA No. -L}{L-INA No. LKP -L}</fo:block></fo:table-cell><!-- DLA No. --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Name of Insured -L}{L-INA Nama Tertanggung -L}</fo:block></fo:table-cell><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Object -L}{L-INA Objek -L}</fo:block></fo:table-cell><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Sum Insured -L}{L-INA Harga Tertanggung -L}</fo:block></fo:table-cell><!-- Total Sum Insured --> 
                            <fo:table-cell ><fo:block text-align="center">Claim Estimated</fo:block></fo:table-cell><!-- Claim Estimated --> 
                            <fo:table-cell ><fo:block text-align="center">Claim Approved</fo:block></fo:table-cell><!-- Claim Approved --> 
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header> 
                    
                    <fo:table-body>
                        
                        <% 
                        int counter = 40;
                        BigDecimal subTotalTSI = null;
                        BigDecimal subTotalEst = null;
                        BigDecimal subTotalApp = null;
                        
                        BigDecimal [] t = new BigDecimal[4];
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());
                            t[n] = BDUtil.add(t[n++], pol.getDbClaimAmountEstimate());
                            t[n] = BDUtil.add(t[n++], pol.getDbClaimAmountApproved());
                            
                            String no_polis = pol.getStPolicyNo();
                            
                            String no_polis_cetak = no_polis.substring(0,4)+"-"+no_polis.substring(4,8)+"-"+no_polis.substring(8,12)+"-"+no_polis.substring(12,16)+"-"+no_polis.substring(16,18);
                            
                            String custName = "";
                            if(pol.getStCustomerName().length()>25)
                                custName = pol.getStCustomerName().substring(0,25);
                            else
                                custName = pol.getStCustomerName().substring(0, pol.getStCustomerName().length());
                            
                            if (i==counter) {
                                counter = counter + 40;            
                        %>   
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell number-columns-spanned="8"><fo:block text-align="center">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalTSI,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalEst,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalApp,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                        subTotalTSI = null;
                        subTotalEst = null;
                        subTotalApp = null;
                            }%>
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=i+1%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtDLADate())%></fo:block></fo:table-cell>    <!-- No --><!-- Entry Date -->
                            <fo:table-cell ><fo:block text-align="center"><%=DateUtil.getDateStr(pol.getDtPolicyDate())%></fo:block></fo:table-cell>    <!-- No --><!-- Police Date --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStPLANo())%></fo:block></fo:table-cell>    <!-- No --><!-- PLA No. --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStDLANo())%></fo:block></fo:table-cell>    <!-- No --><!-- DLA No. --> 
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(custName)%></fo:block></fo:table-cell>    <!-- No --><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStProducerName())%></fo:block></fo:table-cell>    <!-- No --><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Total Sum Insured --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmountEstimate(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Claim Estimated --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmountApproved(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Claim Approved -->   
                        </fo:table-row>   
                        
                        <%   	
                        subTotalTSI = BDUtil.add(subTotalTSI,pol.getDbInsuredAmount());
                        subTotalEst = BDUtil.add(subTotalEst,pol.getDbClaimAmountEstimate());
                        subTotalApp = BDUtil.add(subTotalApp,pol.getDbClaimAmountApproved());
                        
                        } %>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>   
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- Entry Date --> 
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- Police Date --> 
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- Policy No. --> 
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- PLA No. --> 
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- DLA No. --> 
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- DLA No. --> 
                            <fo:table-cell ><fo:block> </fo:block>TOTAL</fo:table-cell><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell><!-- Total Sum Insured --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell><!-- Claim Estimated --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2],2)%></fo:block></fo:table-cell><!-- Claim Approved -->  
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
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
                        <svg xmlns="http://www.w3.org/2000/svg" width="5cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
        </fo:flow>   
        
    </fo:page-sequence>   
</fo:root>   

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

BigDecimal outstandingCommBfeeHfeeFbase = new BigDecimal(0);
BigDecimal TotalCommBfeeHfeeFbase = new BigDecimal(0);
BigDecimal TotalTaxCommBfeeHfeeFbase = new BigDecimal(0);
BigDecimal TotalBiapolmat = new BigDecimal(0);
BigDecimal TotalHfeeBfee = new BigDecimal(0);

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
                               page-height="21.7cm"
                               page-width="33cm"
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
       
            <fo:block font-size="6pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column />
                    <fo:table-header> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">             
                                    LAPORAN REALISASI TITIPAN KOMISI   
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">
                                    <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">
                                    <% if (form.getPaymentDateFrom()!=null || form.getPaymentDateTo()!=null) {%> 
                                    Tanggal Bayar : <%=JSPUtil.printX(form.getPaymentDateFrom())%> S/D <%=JSPUtil.printX(form.getPaymentDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <% if (cabang!=null) { %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block font-weight="bold">
                                    Cabang : <%=JSPUtil.printX(cabang)%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <% if (form.getStRegion()!=null) { %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block font-weight="bold">
                                    Outlet : <%=JSPUtil.printX(region)%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block font-weight="bold">
                                    <% if (form.getStPolicyTypeDesc()!=null) {%> 
                                    Jenis Polis : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block font-weight="bold">
                                    <% if (form.getStEntityName()!=null) {%> 
                                    Customer Name : <%=JSPUtil.printX(form.getStEntityName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="16">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>  
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">{L-ENG Receipt Date-L}{L-INA Tgl. Bayar-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">{L-ENG Receipt No-L}{L-INA No. Receipt -L}</fo:block></fo:table-cell> 
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">Tanggal Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">No Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">Nama Tertanggung</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">Premi Bruto</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">Biaya Polis Materai</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">Diskon</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">Komisi</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">Bfee Hfee</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">Total Pajak</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">Fee Base</fo:block></fo:table-cell> 
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">Premi Netto</fo:block></fo:table-cell> 
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center">Komisi Dibayar</fo:block></fo:table-cell> 
                            <fo:table-cell border-left-style="solid" border-right-style="solid" padding="2pt"><fo:block text-align="center">Hutang Komisi</fo:block></fo:table-cell>  	
                        </fo:table-row>   
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="16">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>    
                        
                    </fo:table-header>             
                    <fo:table-body>      
                        <!-- GARIS DALAM KOLOM -->  
                        <%   
                        BigDecimal [] t = new BigDecimal[13];
                        
                        int counter = 45;
                        BigDecimal subTotalPremi = null;
                        BigDecimal subTotalBPol = null;
                        BigDecimal subTotalBMat = null;
                        BigDecimal subTotalDisc = null;
                        BigDecimal subTotalBfee = null;
                        BigDecimal subTotalHfee = null;
                        BigDecimal subTotalFbase = null;
                        BigDecimal subTotalComm = null;
                        BigDecimal subTotalTaxComm = null;
                        BigDecimal subTotalNetto = null;
                        BigDecimal subTotalCommBfeeHfeeFbasePaid = null;
                        BigDecimal subTotalOutsCommBfeeHfeeFbase = null;
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDDisc1());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiNetto());
                            t[n] = BDUtil.add(t[n++], pol.getDbAPComisP());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm1());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDBrok1());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDHFee());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDFeeBase1());
                            
                            TotalBiapolmat = BDUtil.add(pol.getDbNDPCost(), pol.getDbNDSFee());
                            t[n] = BDUtil.add(t[n++], TotalBiapolmat); //total tagihan komisi
                            
                            TotalTaxCommBfeeHfeeFbase = BDUtil.add(pol.getDbNDTaxComm1(), pol.getDbNDTaxBrok1());
                            TotalTaxCommBfeeHfeeFbase = BDUtil.add(TotalTaxCommBfeeHfeeFbase, pol.getDbNDTaxHFee());
                            t[n] = BDUtil.add(t[n++], TotalTaxCommBfeeHfeeFbase); //total tagihan pajak komisi
                            
                            TotalCommBfeeHfeeFbase = BDUtil.add(pol.getDbNDComm1(), pol.getDbNDBrok1());
                            TotalCommBfeeHfeeFbase = BDUtil.add(TotalCommBfeeHfeeFbase, pol.getDbNDHFee());
                            TotalCommBfeeHfeeFbase = BDUtil.add(TotalCommBfeeHfeeFbase, pol.getDbNDFeeBase1());
                            t[n] = BDUtil.add(t[n++], TotalCommBfeeHfeeFbase); //total tagihan komisi
                            
                            outstandingCommBfeeHfeeFbase = BDUtil.sub(TotalCommBfeeHfeeFbase,pol.getDbAPComisP());
                            t[n] = BDUtil.add(t[n++], outstandingCommBfeeHfeeFbase); //total tagihan komisi
                            
                            TotalHfeeBfee = BDUtil.add(pol.getDbNDBrok1(), pol.getDbNDHFee());
                            t[n] = BDUtil.add(t[n++], TotalHfeeBfee); //total hfee bfee
                            
                            String custName = "";
                            if(pol.getStCustomerName().length()>25)
                                custName = pol.getStCustomerName().substring(0,25);
                            else
                                custName = pol.getStCustomerName().substring(0, pol.getStCustomerName().length());
                            
                            String no_polis = pol.getStPolicyNo();
                            String no_polis_cetak = no_polis.substring(0,4)+"-"+no_polis.substring(4,8)+"-"+no_polis.substring(8,12)+"-"+no_polis.substring(12,16)+"-"+no_polis.substring(16,18);
                            
                            if (i==counter) {
                                counter = counter + 45;
                        %>   
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell number-columns-spanned="6"><fo:block text-align="center">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(subTotalBPol,subTotalBMat),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalDisc,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalComm,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(subTotalBfee,subTotalHfee),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalTaxComm,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalFbase,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalNetto,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalCommBfeeHfeeFbasePaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalOutsCommBfeeHfeeFbase,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                        subTotalPremi 		= null;
                        subTotalBPol 		= null;
                        subTotalBMat 		= null;
                        subTotalDisc 		= null;
                        subTotalBfee 		= null;
                        subTotalHfee 		= null;
                        subTotalFbase 		= null;
                        subTotalComm 		= null;
                        subTotalTaxComm 	= null;
                        subTotalNetto 		= null;
                        subTotalCommBfeeHfeeFbasePaid 	= null;
                        subTotalOutsCommBfeeHfeeFbase 	= null;
                        
                            }
                        %>
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=i+1%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPeriodStart())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(custName)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(pol.getDbNDPCost(),pol.getDbNDSFee()),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDDisc1(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm1(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(pol.getDbNDBrok1(),pol.getDbNDHFee()),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalTaxCommBfeeHfeeFbase,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDFeeBase1(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiNetto(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbAPComisP(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(outstandingCommBfeeHfeeFbase,0)%></fo:block></fo:table-cell>
                        </fo:table-row>   
                        
                        <% 
                        subTotalPremi 	= BDUtil.add(subTotalPremi,pol.getDbPremiTotal());
                        subTotalBPol 	= BDUtil.add(subTotalBPol,pol.getDbNDPCost());
                        subTotalBMat 	= BDUtil.add(subTotalBMat,pol.getDbNDSFee());
                        subTotalDisc 	= BDUtil.add(subTotalDisc,pol.getDbNDDisc1());
                        subTotalBfee 	= BDUtil.add(subTotalBfee,pol.getDbNDBrok1());
                        subTotalHfee 	= BDUtil.add(subTotalHfee,pol.getDbNDHFee());
                        subTotalFbase 	= BDUtil.add(subTotalFbase,pol.getDbNDFeeBase1());
                        subTotalComm 	= BDUtil.add(subTotalComm,pol.getDbNDComm1());
                        subTotalTaxComm	= BDUtil.add(pol.getDbNDTaxComm1(),pol.getDbNDTaxBrok1());
                        subTotalTaxComm	= BDUtil.add(subTotalTaxComm,pol.getDbNDTaxHFee());
                        subTotalNetto 	= BDUtil.add(subTotalNetto,pol.getDbPremiNetto());
                        subTotalCommBfeeHfeeFbasePaid 	= BDUtil.add(subTotalCommBfeeHfeeFbasePaid,pol.getDbAPComisP());
                        subTotalOutsCommBfeeHfeeFbase 	= BDUtil.add(subTotalOutsCommBfeeHfeeFbase,outstandingCommBfeeHfeeFbase);
                        } %>    
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="16">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="6"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0],0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[8],0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1],0)%> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[4],0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[12],0)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[9],0)%></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[7],0)%></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2],0)%></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3],0)%></fo:block></fo:table-cell>    
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[11],0)%></fo:block></fo:table-cell>  
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="16">  
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
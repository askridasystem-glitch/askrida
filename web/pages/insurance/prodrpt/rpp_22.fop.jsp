<%@ page import="com.webfin.insurance.model.*, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.Tools, 
com.crux.util.fop.FOPUtil, 
com.crux.util.BDUtil,
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionMarketingReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionMarketingReportForm form = (ProductionMarketingReportForm)SessionManager.getInstance().getCurrentForm();

boolean tanpaKomisi = true;

//if(form.getStCustCategory1().equalsIgnoreCase("2") || form.getStCustCategory1().equalsIgnoreCase("3") || form.getStCustCategory1().equalsIgnoreCase("4"))
    tanpaKomisi = false;

String width = tanpaKomisi?"43cm":"28cm";

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-height="28cm"
                               page-width="<%=width%>"
                               margin-top="1cm"
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
                    <fo:table-column column-width="17mm"/><!-- Policy Date-->
                    <fo:table-column column-width="33mm"/><!-- Policy No -->
                    <fo:table-column column-width="10mm"/><!-- Policy No -->
                    <fo:table-column column-width="90mm"/><!-- The Insured -->
                    <fo:table-column column-width="30mm"/><!-- Premium -->
                    <fo:table-column column-width="23mm"/><!-- Premium --> 
                    <fo:table-column column-width="15mm"/><!-- Biaya Polis --> 
                    <fo:table-column column-width="15mm"/><!-- Biaya Materai -->
                    <fo:table-column column-width="20mm"/><!-- Entry Date -->
                    <%if(tanpaKomisi){%>
                        <fo:table-column column-width="20mm"/><!-- Discount-->
                        <fo:table-column column-width="20mm"/><!-- Komisi-->
                        <fo:table-column column-width="20mm"/><!-- Handling Fee -->
                        <fo:table-column column-width="20mm"/><!-- Broker Fee -->
                        <fo:table-column column-width="20mm"/><!-- Pajak Komisi/Broker Fee -->
                        <fo:table-column column-width="20mm"/><!-- Tagihan Netto -->
                    <%}%>

                    <fo:table-header>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">             
                                    LAPORAN PRODUKSI    
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block  font-weight="bold" font-size="18pt" text-align="center">
                                    <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-weight="bold" text-align="center">
                                    <% if (form.getPeriodFrom()!=null || form.getPeriodTo()!=null) {%> 
                                    Periode : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <% if (form.getStBranch()!=null) { %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-weight="bold">
                                    Cabang : <%=JSPUtil.printX(form.getCostCenter().getStDescription())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <% if (form.getStRegion()!=null) { %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-weight="bold">
                                    Outlet : <%=JSPUtil.printX(form.getRegion().getStDescription())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-weight="bold">
                                    <% if (form.getStPolicyTypeGroupID()!=null) {%> 
                                    Jenis Group : <%=JSPUtil.printX(form.getStPolicyTypeGroupDesc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-weight="bold">
                                    <% if (form.getStPolicyTypeID()!=null) {%> 
                                    Jenis Polis : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>                        
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-weight="bold">
                                    <% if (form.getStFltCoverType()!=null) {%> 
                                    Jenis Penutupan  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCustCategory1()!=null) {%> 
                                    Sumber Bisnis : <%=JSPUtil.printX(form.getStCustCategory1Desc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-weight="bold">
                                    <% if (form.getStEntityID()!=null) {%> 
                                    Customer Name :<%=JSPUtil.printX(form.getStEntityName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-weight="bold">
                                    <% if (form.getStMarketerID()!=null) {%> 
                                    Marketer Name :<%=JSPUtil.printX(form.getStMarketerName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCompanyID()!=null) {%> 
                                    Company Name :<%=JSPUtil.printX(form.getStCompanyName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCoinsurerID()!=null) {%> 
                                    Coinsurer Name :<%=JSPUtil.printX(form.getStCoinsurerName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCustStatus()!=null) {%> 
                                    Customer Status :<%=JSPUtil.printX(form.getStCustStatus())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="18"><fo:block text-align="end" font-weight="bold">{L-ENG (In IDR)-L}{L-INA (dalam rupiah)-L}</fo:block></fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="18" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Date-L}{L-INA Tgl. Polis-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Number -L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Insured-L}{L-INA TSI-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Bruto Premium-L}{L-INA Premi Bruto-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Cost-L}{L-INA Biaya Polis-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Stamp Duty -L}{L-INA Biaya Materai-L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Fee Base-L}{L-INA Fee Base -L}</fo:block></fo:table-cell>  

                            <%if(tanpaKomisi){%>
                                <fo:table-cell ><fo:block text-align="center">{L-ENG Discount-L}{L-INA Diskon Premi-L}</fo:block></fo:table-cell>
                                <fo:table-cell ><fo:block text-align="center">{L-ENG Commission-L}{L-INA Komisi-L}</fo:block></fo:table-cell>
                                <fo:table-cell ><fo:block text-align="center">{L-ENG Handling Fee-L}{L-INA Handling Fee-L}</fo:block></fo:table-cell>
                                <fo:table-cell ><fo:block text-align="center">{L-ENG Brokerage Fee-L}{L-INA Brokerage Fee-L}</fo:block></fo:table-cell>
                                <fo:table-cell ><fo:block text-align="center">{L-ENG Tax Comm/Brok Fee-L}{L-INA Pajak Komisi/Broker Fee-L}</fo:block></fo:table-cell>
                                <fo:table-cell ><fo:block text-align="center">{L-ENG Premi Nett-L}{L-INA Tagihan Netto-L}</fo:block></fo:table-cell>
                            <%}%>

                            
                        </fo:table-row>  
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="18" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <!-- GARIS  -->  
                        <!--   \<fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt"></fo:block> -->  
     
                        
                        <%   
                        int counter = 60;
                        BigDecimal subTotalInsured = new BigDecimal(0);
                        BigDecimal subTotalPremi = new BigDecimal(0);
                        BigDecimal subTotalBPol = new BigDecimal(0);
                        BigDecimal subTotalBMat = new BigDecimal(0);
                        BigDecimal subTotalFeeBase = new BigDecimal(0);
                        BigDecimal subTotalDisc = new BigDecimal(0);
                        BigDecimal subTotalKomisi = new BigDecimal(0);
                        BigDecimal subTotalHFee = new BigDecimal(0);
                        BigDecimal subTotalBrok = new BigDecimal(0);
                        BigDecimal subTotalTax = new BigDecimal(0);
                        BigDecimal subTotalNetto = new BigDecimal(0);
                        
                        BigDecimal TotalComm = new BigDecimal(0);
                        BigDecimal TotalCommTax = new BigDecimal(0);
                        BigDecimal DiscComm = new BigDecimal(0);
                        BigDecimal HfeeBfee = new BigDecimal(0);
                        BigDecimal TotalMin = new BigDecimal(0);
                        BigDecimal PremiGross = new BigDecimal(0);
                        BigDecimal PlusTax = new BigDecimal(0);
                        BigDecimal TagihanNetto = new BigDecimal(0);
                        
                        BigDecimal [] t = new BigDecimal[11];
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDPCost());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDSFee());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDFeeBase1());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDDisc1());
                            
                            TotalComm = BDUtil.add(pol.getDbNDComm1(), pol.getDbNDComm3());
                            t[n] = BDUtil.add(t[n++], TotalComm);
                            
                            t[n] = BDUtil.add(t[n++], pol.getDbNDHFee());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDBrok1());
                            
                            TotalCommTax = BDUtil.add(pol.getDbNDTaxComm1(), pol.getDbNDTaxComm3());
                            TotalCommTax = BDUtil.add(TotalCommTax, pol.getDbNDTaxBrok1());
                            TotalCommTax = BDUtil.add(TotalCommTax, pol.getDbNDTaxHFee());
                            if(pol.getStEntityID().equalsIgnoreCase("1"))
                                t[n] = BDUtil.add(t[n++], TotalCommTax);
                            else
                                t[n] = BDUtil.add(t[n++], new BigDecimal(0));
                            
                            DiscComm = BDUtil.add(pol.getDbNDDisc1(),TotalComm);
                            HfeeBfee = BDUtil.add(pol.getDbNDHFee(),pol.getDbNDBrok1());
                            HfeeBfee = BDUtil.add(HfeeBfee,pol.getDbNDFeeBase1());
                            TotalMin = BDUtil.add(HfeeBfee,DiscComm);
                            PremiGross = BDUtil.add(BDUtil.add(pol.getDbNDPCost(),pol.getDbNDSFee()),pol.getDbPremiTotal());
                            
                            if(pol.getStEntityID().equalsIgnoreCase("1"))
                                PlusTax = BDUtil.add(TotalCommTax,PremiGross);
                            else
                                PlusTax = BDUtil.add(new BigDecimal(0),PremiGross);
                            
                            TagihanNetto = BDUtil.sub(PlusTax,TotalMin);
                            t[n] = BDUtil.add(t[n++], TagihanNetto);
                            t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());
                            
                            String custName = "";
                            if(pol.getStCustomerName().length()>50)
                                custName = pol.getStCustomerName().substring(0,50);
                            else
                                custName = pol.getStCustomerName().substring(0, pol.getStCustomerName().length());
                            
                            String no_polis = "";
                            String no_polis_cetak = "";
                            
                            if (pol.getStPolicyNo()!=null) {
                                no_polis = pol.getStPolicyNo();
                                no_polis_cetak = no_polis.substring(0,4)+"-"+no_polis.substring(4,8)+"-"+no_polis.substring(8,12)+"-"+no_polis.substring(12,16)+"-"+no_polis.substring(16,18);
                            } else {
                                no_polis = pol.getStReference1();
                                no_polis_cetak = no_polis.substring(0,2)+"-"+no_polis.substring(2,6)+"-"+no_polis.substring(6,10)+"-"+no_polis.substring(10,14)+"-"+no_polis.substring(14,16);
                            }
                            
                            if (i==counter) {
                                counter = counter + 60;
                                
                        /*   	  int p=-1;
 
                        final DTOList objects = pol.getObjects();
 
                        while (true) {
                        p++;
                        if (p>=objects.size()) break;
 
                        final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(p);
                         */
                        %>   
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                        subTotalInsured = null;
                        subTotalPremi = null;
                        subTotalBPol = null;
                        subTotalBMat = null;
                        subTotalFeeBase = null;
                        subTotalDisc = null;
                        subTotalKomisi = null;
                        subTotalHFee = null;
                        subTotalBrok = null;
                        subTotalTax = null;
                        subTotalNetto = null;
                            }%>
                        
                        
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPolicyDate())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStEntityID())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(custName)%></fo:block></fo:table-cell>    <!-- No --><!-- The Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDPCost(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDSFee(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Discount --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDFeeBase1(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Total Comm --> 
                            
                            <%if(tanpaKomisi){%>
                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDDisc1(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Total Comm -->
                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalComm,0)%></fo:block></fo:table-cell>
                                <!-- No --><!-- HFee 1 -->
                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDHFee(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- HFee 1 -->
                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDBrok1(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Broker 1 -->
                                <% if(pol.getStEntityID().equalsIgnoreCase("1")){ %>
                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalCommTax,0)%></fo:block></fo:table-cell>    <!-- No --><!-- HFee 1 -->
                                <% }else{ %>
                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(new BigDecimal(0))%></fo:block></fo:table-cell>    <!-- No --><!-- HFee 1 -->
                                <% } %>
                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TagihanNetto,0)%></fo:block></fo:table-cell>    <!-- No --><!-- Broker 1 -->
                            <%} %>
                           
                        </fo:table-row>
                        
                        <%
                        subTotalInsured = BDUtil.add(subTotalInsured,pol.getDbInsuredAmount());
                        subTotalPremi = BDUtil.add(subTotalPremi,pol.getDbPremiTotal());
                        subTotalBPol = BDUtil.add(subTotalBPol,pol.getDbNDPCost());
                        subTotalBMat = BDUtil.add(subTotalBMat,pol.getDbNDSFee());
                        subTotalFeeBase = BDUtil.add(subTotalFeeBase,pol.getDbNDFeeBase1());
                        subTotalDisc = BDUtil.add(subTotalDisc,pol.getDbNDDisc1());
                        subTotalKomisi = BDUtil.add(subTotalKomisi,TotalComm);
                        subTotalHFee = BDUtil.add(subTotalHFee,pol.getDbNDHFee());
                        subTotalBrok = BDUtil.add(subTotalBrok,pol.getDbNDBrok1());
                        subTotalTax = BDUtil.add(subTotalTax,TotalCommTax);
                        subTotalNetto = BDUtil.add(subTotalNetto,TagihanNetto);
                        
                        } %>   
                        
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block> TOTAL : </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[10],0)%></fo:block></fo:table-cell><!-- Total Premi -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0],0)%></fo:block></fo:table-cell><!-- Total Premi -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1],0)%></fo:block></fo:table-cell><!-- Total Premi -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2],0)%></fo:block></fo:table-cell><!-- Total Fee -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3],0)%></fo:block></fo:table-cell><!-- Total Fee -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[4],0)%></fo:block></fo:table-cell><!-- Total Fee -->
                            
                        </fo:table-row>
                        
                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="18" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        
                        
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
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>            
            
        </fo:flow> 
    </fo:page-sequence>   
</fo:root>   
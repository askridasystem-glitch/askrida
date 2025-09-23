<%@ page import="com.webfin.insurance.model.*, 
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.util.Tools,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.form.ProductionMarketingReportForm,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final DTOList l = (DTOList) request.getAttribute("RPT");

            final ProductionMarketingReportForm form = (ProductionMarketingReportForm) SessionManager.getInstance().getCurrentForm();

//if (true) throw new NullPointerException();

            BigDecimal TagihanNetto = new BigDecimal(0);
            BigDecimal Polis = new BigDecimal(0);

            String cabang = null;

            if (SessionManager.getInstance().getSession().getStBranch() != null) {
                cabang = form.getStBranchName();
            } else {
                cabang = form.getStBranchDesc();
            }

            String region = null;

            if (SessionManager.getInstance().getSession().getStRegion() != null) {
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
                               page-height="21cm"
                               page-width="30cm"
                               margin-top="0.75cm"
                               margin-bottom="0.5cm"
                               margin-left="3cm"
                               margin-right="3cm">

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

            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="8mm"/><!-- No -->
                    <fo:table-column column-width="30mm"/><!-- Policy Type -->
                    <fo:table-column column-width="35mm"/><!-- Premium --> 
                    <fo:table-column column-width="30mm"/><!-- Biaya Polis -->
                    <fo:table-column column-width="30mm"/><!-- Biaya Materai -->
                    <fo:table-column column-width="30mm"/><!-- Discount-->
                    <fo:table-column column-width="30mm"/><!-- Handling Fee -->
                    <fo:table-column column-width="15mm"/><!-- Jumlah Polis--> 
                    <fo:table-column column-width="15mm"/><!-- Jumlah Endorse--> 
                    <fo:table-header>  

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="20pt" text-align="center">             
                                    REKAP PRODUKSI PER JENIS ASURANSI
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <% if (form.getPolicyDateFrom() != null || form.getPolicyDateTo() != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="18pt" text-align="center">
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row> 
                        <% }%>                                

                        <% if (cabang != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-weight="bold">
                                    Cabang : <%=JSPUtil.printX(cabang)%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <% if (form.getStRegion() != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-weight="bold">
                                    Outlet : <%=JSPUtil.printX(region)%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <% if (form.getStPolicyTypeDesc() != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-weight="bold">
                                    Jenis Polis : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>  
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <% if (form.getStFltCoverTypeDesc() != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-weight="bold">
                                    Jenis Penutupan  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>  
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <% if (form.getStCustCategory1Desc() != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-weight="bold">
                                    Sumber Bisnis : <%=JSPUtil.printX(form.getStCustCategory1Desc())%>  
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <% if (form.getStMarketerName() != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-weight="bold">
                                    Marketer Name :<%=JSPUtil.printX(form.getStMarketerName())%> 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row> 
                        <% }%>

                        <% if (form.getStCompanyName() != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-weight="bold">
                                    Company Name :<%=JSPUtil.printX(form.getStCompanyName())%>  
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>                        
                        <% }%>                        

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="9"><fo:block text-align="end" font-weight="bold">{L-ENG (In IDR)-L}{L-INA (dalam rupiah)-L}</fo:block></fo:table-cell>
                        </fo:table-row>  

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Type-L}{L-INA Jenis Polis -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Bruto Premium-L}{L-INA Premi Bruto-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Cost-L}{L-INA Biaya Polis-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Stamp Duty -L}{L-INA Biaya Materai-L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Fee Base-L}{L-INA Fee Base-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Premi Nett-L}{L-INA Tagihan Netto-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Total-L}{L-INA Jumlah Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Endorse Total-L}{L-INA Jumlah Endorse -L}</fo:block></fo:table-cell>
                        </fo:table-row>    

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                    </fo:table-header>

                    <fo:table-body>

                        <!-- GARIS  -->  
                        <!--   \<fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt"></fo:block> -->  


                        <%   int counter = 30;
                                    BigDecimal subTotalPremi = null;
                                    BigDecimal subTotalBPol = null;
                                    BigDecimal subTotalBMat = null;
                                    BigDecimal subTotalFeebase = null;
                                    BigDecimal subTotalNetto = null;
                                    BigDecimal subTotalPolis = null;
                                    BigDecimal subTotalEndorse = null;

                                    BigDecimal TotalPremi = new BigDecimal(0);

                                    BigDecimal[] t = new BigDecimal[7];

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        TotalPremi = BDUtil.sub(pol.getDbPremiAmt(), pol.getDbDiscPremi());
                                        t[n] = BDUtil.add(t[n++], TotalPremi);
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDPCost());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDSFee());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDFeeBase1());


                                        TagihanNetto = BDUtil.sub(TotalPremi, pol.getDbNDFeeBase1());
                                        TagihanNetto = BDUtil.add(TagihanNetto, pol.getDbNDPCost());
                                        TagihanNetto = BDUtil.add(TagihanNetto, pol.getDbNDSFee());
                                        t[n] = BDUtil.add(t[n++], TagihanNetto);

                                        t[n] = BDUtil.add(t[n++], pol.getDbPolis());
                                        t[n] = BDUtil.add(t[n++], pol.getDbJumlah());


                                        if (i == counter) {
                                            counter = counter + 30;

                                            /*   	  int p=-1;

                                            final DTOList objects = pol.getObjects();

                                            while (true) {
                                            p++;
                                            if (p>=objects.size()) break;

                                            final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(p);
                                             */
                        %>   
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalPremi, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalBPol, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalBMat, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalFeebase, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalNetto, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalPolis, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalEndorse, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                                                                    subTotalPremi = null;
                                                                    subTotalBPol = null;
                                                                    subTotalBMat = null;
                                                                    subTotalFeebase = null;
                                                                    subTotalNetto = null;
                                                                    subTotalPolis = null;
                                                                    subTotalEndorse = null;
                                                                }%>


                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>    <!-- No --><!-- No -->
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStPolicyTypeDesc2())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalPremi, 0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDPCost(), 0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDSFee(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDFeeBase1(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TagihanNetto, 0)%></fo:block></fo:table-cell>    <!-- No --><!-- Broker 1 -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPolis(), 0)%></fo:block></fo:table-cell>    <!-- No --><!-- Period -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbJumlah(), 0)%></fo:block></fo:table-cell>    <!-- No --><!-- Period -->
                        </fo:table-row>

                        <%
                                        subTotalPremi = BDUtil.add(subTotalPremi, TotalPremi);
                                        subTotalBPol = BDUtil.add(subTotalBPol, pol.getDbNDPCost());
                                        subTotalBMat = BDUtil.add(subTotalBMat, pol.getDbNDSFee());
                                        subTotalFeebase = BDUtil.add(subTotalFeebase, pol.getDbNDFeeBase1());
                                        subTotalNetto = BDUtil.add(subTotalNetto, TagihanNetto);
                                        subTotalPolis = BDUtil.add(subTotalPolis, pol.getDbPolis());
                                        subTotalEndorse = BDUtil.add(subTotalEndorse, pol.getDbJumlah());

                                    }%>

                        <!-- GARIS DALAM KOLOM -->   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center"> TOTAL : </fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0], 0)%></fo:block></fo:table-cell><!-- Total Premi -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1], 0)%></fo:block></fo:table-cell><!-- Total Premi -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2], 0)%></fo:block></fo:table-cell><!-- Total Fee -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3], 0)%></fo:block></fo:table-cell><!-- Total Fee -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[4], 0)%></fo:block></fo:table-cell><!-- Total Disc -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[5], 0)%></fo:block></fo:table-cell><!-- Total Disc -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[6], 0)%></fo:block></fo:table-cell><!-- Total Disc -->
                        </fo:table-row>   

                        <!-- GARIS DALAM KOLOM -->   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                        <!-- GARIS DALAM KOLOM -->   

                    </fo:table-body>   
                </fo:table>   
            </fo:block> 

            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
            </fo:block>      

            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[0], 0)%>" orientation="0">
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
                        <svg xmlns="http://www.w3.org/2000/svg" width="1cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="1cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
        </fo:flow>   

    </fo:page-sequence>   
</fo:root>   
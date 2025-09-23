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
%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 

    <!-- defines page layout --> 
    <fo:layout-master-set> 

        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-height="21cm"
                               page-width="33cm"
                               margin-top="0.75cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">

            <fo:region-body margin-top="1cm" margin-bottom="2cm"/> 
            <fo:region-before extent="2cm"/> 
            <fo:region-after extent="0.5cm"/> 
        </fo:simple-page-master> 

    </fo:layout-master-set> 
    <!-- end: defines page layout --> 

    <!-- actual layout --> 
    <fo:page-sequence master-reference="first"> 

        <!-- usage of page layout --> 
        <!-- header --> 
        <fo:static-content flow-name="xsl-region-before"> 

            <fo:block-container height="1cm" width="5cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="1cm" width="5cm" top="0cm" left="0cm" position="absolute">    <fo:block>
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
                rpp_expired_date - PT. Asuransi Bangun Askrida
            </fo:block>
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
                    <fo:table-column column-width="20mm"/><!-- Entry Date -->
                    <fo:table-column column-width="20mm"/><!-- Policy Date--> 
                    <fo:table-column column-width="20mm"/><!-- Period End--> 
                    <fo:table-column column-width="40mm"/><!-- Policy No -->
                    <fo:table-column column-width="3mm"/><!-- Policy No -->
                    <fo:table-column column-width="110mm"/><!-- The Insured -->
                    <fo:table-column column-width="50mm"/><!-- Premium --> 
                    <fo:table-column column-width="20mm"/><!-- Biaya Polis --> 
                    <fo:table-column column-width="20mm"/><!-- Biaya Materai -->
                    <fo:table-header>  

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold" font-size="20pt" text-align="center">             
                                    LAPORAN POLIS JATUH TEMPO
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold" font-size="18pt" text-align="center">
                                    <% if (form.getPolicyDateFrom() != null || form.getPolicyDateTo() != null) {%>
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold" font-size="18pt" text-align="center">
                                    <% if (form.getExpirePeriodFrom() != null || form.getExpirePeriodTo() != null) {%>
                                    Jatuh Tempo : <%=JSPUtil.printX(form.getExpirePeriodFrom())%> S/D <%=JSPUtil.printX(form.getExpirePeriodTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold" font-size="18pt" text-align="center">
                                    <% if (form.getPeriodFrom() != null) {%>
                                    Treaty : <%=DateUtil.getYear(form.getPeriodFrom())%>      
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStBranchName() != null) {%>
                                    Cabang : <%=JSPUtil.printX(form.getStBranchName())%>
                                    <% }%>
                                    <% if (form.getStBranchDesc() != null) {%>
                                    Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStPolicyTypeDesc() != null) {%>
                                    Jenis Polis : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>  
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStFltCoverTypeDesc() != null) {%>
                                    Jenis Penutupan  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>  
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCustCategory1Desc() != null) {%>
                                    Sumber Bisnis : <%=JSPUtil.printX(form.getStCustCategory1Desc())%>  
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStEntityName() != null) {%>
                                    Customer Name :<%=JSPUtil.printX(form.getStEntityName())%>  
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStMarketerName() != null) {%>
                                    Marketer Name :<%=JSPUtil.printX(form.getStMarketerName())%>  
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10"><fo:block text-align="end" font-weight="bold">{L-ENG (In IDR)-L}{L-INA (dalam rupiah)-L}</fo:block></fo:table-cell>  
                        </fo:table-row>  

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Date-L}{L-INA Tgl. Polis-L}</fo:block></fo:table-cell>   
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Period Start-L}{L-INA Periode Mulai-L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Period End-L}{L-INA Periode Akhir-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Number -L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Customer-L}{L-INA Customer-L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Insured Amount-L}{L-INA Harga Pertanggungan-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Total Premium-L}{L-INA Premi Total-L}</fo:block></fo:table-cell> 
                        </fo:table-row>  




                        <!-- GARIS DALAM KOLOM -->   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                    </fo:table-header>

                    <fo:table-body>

                        <!-- GARIS  -->  
                        <!--   \<fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt"></fo:block> -->  


                        <%
                                    int counter = 35;
                                    BigDecimal subTotalTSI = null;
                                    BigDecimal subTotalPremi = null;

                                    BigDecimal[] t = new BigDecimal[10];

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());

                                        String no_polis = pol.getStPolicyNo();

                                        String no_polis_cetak = no_polis.substring(0, 4) + "-" + no_polis.substring(4, 8) + "-" + no_polis.substring(8, 12) + "-" + no_polis.substring(12, 16) + "-" + no_polis.substring(16, 18);

                                        String custName = "";
                                        if (pol.getStCustomerName().length() > 60) {
                                            custName = pol.getStCustomerName().substring(0, 60);
                                        } else {
                                            custName = pol.getStCustomerName().substring(0, pol.getStCustomerName().length());
                                        }

                                        String prodName = "";
                                        if (pol.getStProducerName() != null) {
                                            if (pol.getStProducerName().length() > 30) {
                                                prodName = pol.getStProducerName().substring(0, 30);
                                            } else {
                                                prodName = pol.getStProducerName().substring(0, pol.getStProducerName().length());
                                            }
                                        }

                                        if (i == counter) {
                                            counter = counter + 35;

                                            /*   	  int p=-1;

                                            final DTOList objects = pol.getObjects();

                                            while (true) {
                                            p++;
                                            if (p>=objects.size()) break;

                                            final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(p);
                                             */
                        %>   
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell number-columns-spanned="8"><fo:block text-align="center">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalTSI, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalPremi, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                                                                    subTotalTSI = null;
                                                                    subTotalPremi = null;
                                                                }%>



                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>    <!-- No --><!-- No -->
                            <fo:table-cell ><fo:block text-align="center"><%=DateUtil.getDateStr(pol.getDtPolicyDate())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell ><fo:block text-align="center"><%=DateUtil.getDateStr(pol.getDtPeriodStart())%></fo:block></fo:table-cell>   <!-- No --><!-- Period --> 
                            <fo:table-cell ><fo:block text-align="center"><%=DateUtil.getDateStr(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No --> 
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(custName)%></fo:block></fo:table-cell>    <!-- No --><!-- The Insured --> 
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(prodName)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                        subTotalTSI = BDUtil.add(subTotalTSI, pol.getDbInsuredAmount());
                                        subTotalPremi = BDUtil.add(subTotalPremi, pol.getDbPremiTotal());
                                    }
                        %>   


                        <!-- GARIS DALAM KOLOM -->   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>  
                            <fo:table-cell number-columns-spanned="7"></fo:table-cell> 
                            <fo:table-cell ><fo:block> TOTAL : </fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0], 0)%></fo:block></fo:table-cell><!-- Total Premi -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1], 0)%></fo:block></fo:table-cell><!-- Total Premi -->
                        </fo:table-row>   

                        <!-- GARIS DALAM KOLOM -->   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                    </fo:table-body>   
                </fo:table>   
            </fo:block> 

            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
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
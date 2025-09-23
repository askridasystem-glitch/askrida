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

            final DTOList l = (DTOList) request.getAttribute("RPT");

            final ProductionMarketingReportForm form = (ProductionMarketingReportForm) SessionManager.getInstance().getCurrentForm();

            String fontsize = form.getStFontSize();

            String cabang = form.getStBranch();
            String region = form.getStRegion();

            BigDecimal TagihanNetto = new BigDecimal(0);
%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 

    <!-- defines page layout --> 
    <fo:layout-master-set> 

        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-height="28cm"
                               page-width="32cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
            <fo:region-before extent="2cm"/> 
            <fo:region-after extent="0.5cm"/> 
        </fo:simple-page-master> 

    </fo:layout-master-set> 
    <!-- end: defines page layout --> 

    <!-- actual layout --> 
    <fo:page-sequence master-reference="first"> 
        <!-- usage of page layout --> 

        <fo:static-content flow-name="xsl-region-after">

            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>


        <fo:flow flow-name="xsl-region-body"> 

            <fo:block font-size="<%=fontsize%>"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="20mm"/><!-- Policy Date--> 
                    <fo:table-column column-width="33mm"/><!-- Policy No -->
                    <fo:table-column column-width="10mm"/><!-- Policy No -->
                    <fo:table-column column-width="84mm"/><!-- The Insured -->
                    <fo:table-column column-width="30mm"/><!-- Premium -->
                    <fo:table-column column-width="30mm"/><!-- Entry Date -->
                    <fo:table-column column-width="30mm"/><!-- Premium -->
                    <fo:table-column column-width="30mm"/><!-- Entry Date -->
                    <fo:table-column column-width="30mm"/><!-- Discount-->
                    <fo:table-header>  

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">             
                                    LAPORAN PRODUKSI DETAIL 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-family="serif" font-weight="bold" font-size="18pt" text-align="center">
                                    <% if (form.getPolicyDateFrom() != null || form.getPolicyDateTo() != null) {%>
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold" text-align="center">
                                    <% if (form.getPeriodFrom() != null || form.getPeriodTo() != null) {%>
                                    Periode : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <% if (cabang != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    Cabang : <%=JSPUtil.printX(cabang)%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <% if (region != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    Outlet : <%=JSPUtil.printX(region)%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStPolicyTypeGroupDesc() != null) {%>
                                    Jenis Group : <%=JSPUtil.printX(form.getStPolicyTypeGroupDesc())%>  
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
                                    Customer Name : <%=JSPUtil.printX(form.getStEntityName())%>  
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStMarketerName() != null) {%>
                                    Marketer Name : <%=JSPUtil.printX(form.getStMarketerName())%>  
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCompanyName() != null) {%>
                                    Company Name : <%=JSPUtil.printX(form.getStCompanyName())%>  
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCreateName() != null) {%>
                                    Create Who : <%=JSPUtil.printX(form.getStCreateName())%>  
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>                        

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10"><fo:block text-align="end" font-weight="bold">{L-ENG (In IDR)-L}{L-INA (dalam rupiah)-L}</fo:block></fo:table-cell>
                        </fo:table-row>  

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Date-L}{L-INA Tgl. Polis-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Number -L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Bruto Premium -L}{L-INA Premi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG PCost -L}{L-INA Bpol -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG SFee -L}{L-INA Bmat -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Fee Base-L}{L-INA Fee Base -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Netto 2-L}{L-INA Tagihan Netto-L}</fo:block></fo:table-cell>
                        </fo:table-row> 

                        <!-- GARIS DALAM KOLOM -->   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                    </fo:table-header>

                    <fo:table-body>

                        <%
                                    int counter = 60;

                                    BigDecimal subTotalPremi = null;
                                    BigDecimal subTotalBPol = null;
                                    BigDecimal subTotalBMat = null;
                                    BigDecimal subTotalFeeBase = null;
                                    BigDecimal subTotalNetto = null;

                                    BigDecimal TotalPremi = new BigDecimal(0);

                                    BigDecimal[] t = new BigDecimal[5];

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        TotalPremi = BDUtil.sub(pol.getDbPremiTotal(), pol.getDbNDDisc1());
                                        t[n] = BDUtil.add(t[n++], TotalPremi);
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDPCost());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDSFee());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDFeeBase1());

                                        TagihanNetto = BDUtil.sub(TotalPremi, pol.getDbNDFeeBase1());
                                        TagihanNetto = BDUtil.add(TagihanNetto, pol.getDbNDPCost());
                                        TagihanNetto = BDUtil.add(TagihanNetto, pol.getDbNDSFee());
                                        t[n] = BDUtil.add(t[n++], TagihanNetto);

                                        String custName = "";
                                        if (pol.getStCustomerName().length() > 45) {
                                            custName = pol.getStCustomerName().substring(0, 45);
                                        } else {
                                            custName = pol.getStCustomerName().substring(0, pol.getStCustomerName().length());
                                        }

                                        String no_polis = pol.getStPolicyNo();

                                        String no_polis_cetak = no_polis.substring(0, 4) + "-" + no_polis.substring(4, 8) + "-" + no_polis.substring(8, 12) + "-" + no_polis.substring(12, 16) + "-" + no_polis.substring(16, 18);

                                        if (i == counter) {
                                            counter = counter + 60;

                        %>   

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="center">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalPremi, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalBPol, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalBMat, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalFeeBase, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalNetto, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>

                        <%
                                                                    subTotalPremi = null;
                                                                    subTotalBPol = null;
                                                                    subTotalBMat = null;
                                                                    subTotalFeeBase = null;
                                                                    subTotalNetto = null;
                                                                }%>

                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>    <!-- No --><!-- No -->
                            <fo:table-cell ><fo:block text-align="center"><%=DateUtil.getDateStr(pol.getDtPolicyDate())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStEntityID())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(custName)%></fo:block></fo:table-cell>    <!-- No --><!-- The Insured --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalPremi, 0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDPCost(), 0)%></fo:block></fo:table-cell>    <!-- No --><!-- Total Comm -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDSFee(), 0)%></fo:block></fo:table-cell>    <!-- No --><!-- Total Comm -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDFeeBase1(), 0)%></fo:block></fo:table-cell>    <!-- No --><!-- Total Comm -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TagihanNetto, 0)%></fo:block></fo:table-cell>    <!-- No --><!-- Broker 1 -->
                        </fo:table-row>

                        <%
                                        subTotalPremi = BDUtil.add(subTotalPremi, TotalPremi);
                                        subTotalBPol = BDUtil.add(subTotalBPol, pol.getDbNDPCost());
                                        subTotalBMat = BDUtil.add(subTotalBMat, pol.getDbNDSFee());
                                        subTotalFeeBase = BDUtil.add(subTotalFeeBase, pol.getDbNDFeeBase1());
                                        subTotalNetto = BDUtil.add(subTotalNetto, TagihanNetto);

                                    }%>


                        <!-- GARIS DALAM KOLOM -->   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5"><fo:block> TOTAL : </fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0], 0)%></fo:block></fo:table-cell><!-- Total Premi -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1], 0)%></fo:block></fo:table-cell><!-- Total Premi -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2], 0)%></fo:block></fo:table-cell><!-- Total Fee -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3], 0)%></fo:block></fo:table-cell><!-- Total Premi -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[4], 0)%></fo:block></fo:table-cell><!-- Total Fee -->
                        </fo:table-row>   

                        <!-- GARIS DALAM KOLOM -->   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

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
                        <svg xmlns="http://www.w3.org/2000/svg" width="0cm" height="0cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>

        </fo:flow> 
    </fo:page-sequence>   
</fo:root>   
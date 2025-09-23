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

            final DTOList l = (DTOList) request.getAttribute("RPT");

            final ProductionFinanceReportForm form = (ProductionFinanceReportForm) SessionManager.getInstance().getCurrentForm();

            BigDecimal outstandingPremi = new BigDecimal(0);
            BigDecimal outstandingComm = new BigDecimal(0);
            BigDecimal outstandingFeebase = new BigDecimal(0);
            BigDecimal outstandingTaxcomm = new BigDecimal(0);
            BigDecimal TotalBmatPol = new BigDecimal(0);
            BigDecimal TotalPremiPaid = new BigDecimal(0);
            BigDecimal TotalComm = new BigDecimal(0);
            BigDecimal TotalTax = new BigDecimal(0);

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 

    <!-- defines page layout --> 
    <fo:layout-master-set> 

        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first"
                               page-height="21.6cm"
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
                      font-size="6pt" font-family="serif" line-height="1em + 2pt">
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
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-header> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">             
                                    LAPORAN PENERIMAAN PREMI   
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">
                                    <% if (form.getPolicyDateFrom() != null || form.getPolicyDateTo() != null) {%>
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">
                                    <% if (form.getPaymentDateFrom() != null || form.getPaymentDateTo() != null) {%>
                                    Tanggal Bayar : <%=JSPUtil.printX(form.getPaymentDateFrom())%> S/D <%=JSPUtil.printX(form.getPaymentDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">
                                    <% if (form.getCreateDateFrom() != null || form.getCreateDateTo() != null) {%>
                                    Tanggal Entry : <%=JSPUtil.printX(form.getCreateDateFrom())%> S/D <%=JSPUtil.printX(form.getCreateDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <% if (form.getStBranch() != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block font-weight="bold">
                                    Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <% if (form.getStRegion() != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block font-weight="bold">
                                    Outlet : <%=JSPUtil.printX(form.getStRegionDesc())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block font-weight="bold">
                                    <% if (form.getStPolicyTypeID() != null) {%>
                                    Jenis Polis : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>  
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block font-weight="bold">
                                    <% if (form.getStEntityID() != null) {%>
                                    Customer Name :<%=JSPUtil.printX(form.getStEntityName())%>  
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell border-left-style="solid" padding="2pt" number-columns-spanned="19" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>   
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  	
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Paid Date-L}{L-INA Tanggal Bayar-L}</fo:block></fo:table-cell> 		
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Receipt No.-L}{L-INA No. Bukti-L}</fo:block></fo:table-cell> 		
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Policy Date-L}{L-INA Tgl. Polis-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG No. Policy.-L}{L-INA No. Polis -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Premium-L}{L-INA Premi -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Adm Cost -L}{L-INA Biaya Adm-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Discount-L}{L-INA Discount-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Gross Balance-L}{L-INA Tagihan Bruto-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Total Comm-L}{L-INA Total Komisi-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Comm Tax-L}{L-INA Pajak Komisi-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Fee Base-L}{L-INA Fee Base-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Nett Balance-L}{L-INA Tagihan Netto-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Premium Paid-L}{L-INA Premi Dibayar-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Comm Paid-L}{L-INA Komisi Dibayar-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG O/S Comm-L}{L-INA O/S Komisi-L}</fo:block></fo:table-cell> 
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG O/S Tax Comm-L}{L-INA O/S Pajak Komisi-L}</fo:block></fo:table-cell> 
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG O/S Fee Base-L}{L-INA O/S Fee Base-L}</fo:block></fo:table-cell> 
                            <fo:table-cell border-left-style="solid" border-right-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Create User-L}{L-INA Create User-L}</fo:block></fo:table-cell>  
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="19">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>    

                    </fo:table-header>             
                    <fo:table-body>      
                        <!-- GARIS DALAM KOLOM -->  
                        <%
                                    BigDecimal[] t = new BigDecimal[14];

                                    int counter = 60;
                                    BigDecimal subTotalPremi = null;
                                    BigDecimal subTotalBmatPol = null;
                                    BigDecimal subTotalDisc = null;
                                    BigDecimal subTotalBruto = null;
                                    BigDecimal subTotalComm = null;
                                    BigDecimal subTotalFeebase = null;
                                    BigDecimal subTotalTaxComm = null;
                                    BigDecimal subTotalNetto = null;
                                    BigDecimal subTotalPremiPaid = null;
                                    BigDecimal subTotalCommPaid = null;
                                    BigDecimal subTotalTaxPaid = null;
                                    BigDecimal subTotalOutsPremi = null;
                                    BigDecimal subTotalOutsComm = null;
                                    BigDecimal subTotalOutsTax = null;
                                    BigDecimal subTotalOutsFeebase = null;

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;

                                        //OS
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());
                                        t[n] = BDUtil.add(t[n++], pol.getDbTotalDue());
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiNetto());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDDisc1());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDFeeBase1());

                                        TotalBmatPol = BDUtil.add(pol.getDbNDPCost(), pol.getDbNDSFee());
                                        t[n] = BDUtil.add(t[n++], TotalBmatPol);

                                        TotalTax = BDUtil.add(pol.getDbNDTaxComm1(), BDUtil.add(pol.getDbNDTaxBrok1(), pol.getDbNDTaxHFee()));
                                        t[n] = BDUtil.add(t[n++], TotalTax); //total tagihan komisi

                                        TotalComm = BDUtil.add(pol.getDbNDComm1(), BDUtil.add(pol.getDbNDBrok1(), pol.getDbNDHFee()));
                                        TotalComm = BDUtil.sub(TotalComm, TotalTax);
                                        t[n] = BDUtil.add(t[n++], TotalComm); //total tagihan komisi

                                        //PAID
                                        TotalPremiPaid = BDUtil.add(pol.getDbPremiPaid(), pol.getDbAPPCostP());
                                        TotalPremiPaid = BDUtil.add(TotalPremiPaid, pol.getDbAPSFeeP());
                                        //TotalPremiPaid = BDUtil.sub(TotalPremiPaid, pol.getDbAPDiscP());
                                        //TotalPremiPaid = BDUtil.sub(TotalPremiPaid, pol.getDbAPHFeeP());
                                        //TotalPremiPaid = BDUtil.sub(TotalPremiPaid, pol.getDbAPBFeeP());
                                        //TotalPremiPaid = BDUtil.sub(TotalPremiPaid, pol.getDbAPFBaseP());
                                        //TotalPremiPaid = BDUtil.sub(TotalPremiPaid, pol.getDbAPComisP());
                                        // = BDUtil.add(TotalPremiPaid, pol.getDbAPTaxP());
                                        t[n] = BDUtil.add(t[n++], TotalPremiPaid);

                                        outstandingPremi = BDUtil.sub(pol.getDbPremiTotal(), pol.getDbPremiPaid());
                                        t[n] = BDUtil.add(t[n++], outstandingPremi); //13

                                        outstandingComm = BDUtil.sub(TotalComm, pol.getDbAPComisP());
                                        t[n] = BDUtil.add(t[n++], outstandingComm); //13

                                        outstandingTaxcomm = BDUtil.sub(TotalTax, pol.getDbAPTaxP());
                                        t[n] = BDUtil.add(t[n++], outstandingTaxcomm); //13

                                        outstandingFeebase = BDUtil.sub(pol.getDbNDFeeBase1(), pol.getDbAPFBaseP());
                                        t[n] = BDUtil.add(t[n++], outstandingFeebase); //13

                                        t[n] = BDUtil.add(t[n++], pol.getDbAPComisP());

                                        String custName = "";
                                        if (pol.getStCustomerName().length() > 25) {
                                            custName = pol.getStCustomerName().substring(0, 25);
                                        } else {
                                            custName = pol.getStCustomerName().substring(0, pol.getStCustomerName().length());
                                        }

                                        String no_polis = pol.getStPolicyNo();
                                        String no_polis_cetak = no_polis.substring(0, 4) + "-" + no_polis.substring(4, 8) + "-" + no_polis.substring(8, 12) + "-" + no_polis.substring(12, 16) + "-" + no_polis.substring(16, 18);

                                        if (i == counter) {
                                            counter = counter + 60;
                        %>   
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="center">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalPremi, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalBmatPol, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalDisc, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalBruto, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(subTotalComm, subTotalTaxComm), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalTaxComm, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalFeebase, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalNetto, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalPremiPaid, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalCommPaid, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalOutsComm, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalOutsTax, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalOutsFeebase, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>

                        <%

                                                                    subTotalPremi = null;
                                                                    subTotalBmatPol = null;
                                                                    subTotalDisc = null;
                                                                    subTotalBruto = null;
                                                                    subTotalComm = null;
                                                                    subTotalFeebase = null;
                                                                    subTotalTaxComm = null;
                                                                    subTotalNetto = null;
                                                                    subTotalPremiPaid = null;
                                                                    subTotalCommPaid = null;
                                                                    subTotalOutsPremi = null;
                                                                    subTotalOutsComm = null;
                                                                    subTotalOutsTax = null;
                                                                    subTotalOutsFeebase = null;

                                                                }%>

                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=i + 1%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPeriodStart())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalBmatPol, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDDisc1(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalDue(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(TotalComm, TotalTax), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalTax, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDFeeBase1(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiNetto(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalPremiPaid, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbAPComisP(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(outstandingComm, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(outstandingTaxcomm, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(outstandingFeebase, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getCreateUser().getStShortName())%></fo:block></fo:table-cell>
                        </fo:table-row>     
                        <%
                                        subTotalPremi = BDUtil.add(subTotalPremi, pol.getDbPremiTotal());
                                        subTotalBmatPol = BDUtil.add(subTotalBmatPol, pol.getDbNDSFee());
                                        subTotalBmatPol = BDUtil.add(subTotalBmatPol, pol.getDbNDPCost());
                                        subTotalBruto = BDUtil.add(subTotalNetto, pol.getDbTotalDue());
                                        subTotalDisc = BDUtil.add(subTotalDisc, pol.getDbNDDisc1());
                                        subTotalTaxComm = BDUtil.add(pol.getDbNDTaxBrok1(), pol.getDbNDTaxHFee());
                                        subTotalTaxComm = BDUtil.add(subTotalTaxComm, pol.getDbNDTaxComm1());
                                        subTotalComm = BDUtil.add(pol.getDbNDBrok1(), pol.getDbNDHFee());
                                        subTotalComm = BDUtil.add(subTotalComm, pol.getDbNDComm1());
                                        subTotalComm = BDUtil.sub(subTotalComm, subTotalTaxComm);
                                        subTotalFeebase = BDUtil.add(subTotalFeebase, pol.getDbNDFeeBase1());
                                        subTotalNetto = BDUtil.add(subTotalNetto, pol.getDbPremiNetto());
                                        subTotalPremiPaid = BDUtil.add(subTotalPremiPaid, TotalPremiPaid);
                                        subTotalCommPaid = BDUtil.add(subTotalCommPaid, pol.getDbAPComisP());
                                        subTotalOutsPremi = BDUtil.add(subTotalOutsPremi, outstandingPremi);
                                        subTotalOutsComm = BDUtil.add(subTotalOutsComm, outstandingComm);
                                        subTotalOutsTax = BDUtil.add(subTotalOutsTax, outstandingTaxcomm);
                                        subTotalOutsFeebase = BDUtil.add(subTotalOutsFeebase, outstandingFeebase);

                                    }%>

                        <!-- GARIS DALAM KOLOM -->   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="19">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[5], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3], 0)%>  </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(t[6], t[7]), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[6], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[4], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[8], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[13], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[10], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[11], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[12], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>   
                        </fo:table-row>   

                        <!-- GARIS DALAM KOLOM -->   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="19" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>   
                    </fo:table-body>   
                </fo:table>   
            </fo:block>

            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
            </fo:block>   
            <fo:block font-size="8pt">
                {L-ENG Print User-L}{L-INA Print User-L} : <%=SessionManager.getInstance().getCreateUser().getStShortName()%>  
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
                            <rect style="fill:white;stroke:white" x="0" y="0" width="0cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>

        </fo:flow>   
    </fo:page-sequence>   
</fo:root> 
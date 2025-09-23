<%@ page import="com.webfin.insurance.model.*,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.form.ProductionReinsuranceReportForm,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>

<%
            final DTOList l = (DTOList) request.getAttribute("RPT");

            final ProductionReinsuranceReportForm form = (ProductionReinsuranceReportForm) SessionManager.getInstance().getCurrentForm();

            InsurancePolicyView policy = (InsurancePolicyView) l.get(0);
            BigDecimal comm = policy.getDbSharePct();
            String bordero = policy.getStCustomerAddress();
            String jenpol = policy.getStProducerAddress();

            BigDecimal InsuredIDR = null;
            BigDecimal InsuredReasIDR = null;
            BigDecimal PremiReasIDR = null;
            BigDecimal CommReasIDR = null;
            BigDecimal NettoReasIDR = null;

            BigDecimal InsuredUSD = null;
            BigDecimal InsuredReasUSD = null;
            BigDecimal PremiReasUSD = null;
            BigDecimal CommReasUSD = null;
            BigDecimal NettoReasUSD = null;
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-width="33cm"
                               page-height="21.5cm"                                                                                                                                                                        
                               margin-left="0.5cm"
                               margin-right="2cm">
            <fo:region-body margin-top="1cm" margin-bottom="2cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set> 
    <!-- end: defines page layout --> 

    <!-- actual layout --> 
    <fo:page-sequence id="N2528" master-reference="only">         

        <fo:flow flow-name="xsl-region-body">

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="33mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block >  	
                                    <fo:external-graphic <%--content-width="scale-to-fit"--%>
                                        content-height="100%"
                                        width="100%"
                                        scaling="uniform" src="url(D:\jboss-fin\server\default\deploy\fin.ear\fin.war\pages\main\img\burung.jpg)"/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="13"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="17" >
                                <fo:block space-after.optimum="20pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block font-weight="bold" font-size="12pt">  	 
                                    {L-ENGBordereaux No-L}{L-INANo. Bordero-L}
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="15">
                                <fo:block font-weight="bold" font-size="12pt">  	 
                                    : <%=bordero.toUpperCase()%>
                                    <%-- <%= DateUtil.getMonth2Digit(form.getPolicyDateTo())%><%= DateUtil.getYear2Digit(form.getPolicyDateTo())%>
                                    / AUTOFAC / CM 
                                    <%= JSPUtil.printX(form.getStPolicyTypeGroupID())%>
                                    /04R/<%=DateUtil.getMonth2Digit(form.getPolicyDateFrom())%>-
                                    <%=DateUtil.getYear2Digit(form.getPolicyDateFrom())%>--%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block font-weight="bold" font-size="12pt">  	 
                                    {L-ENGCompany-L}{L-INAReasuradur-L} 
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="15">
                                <fo:block font-weight="bold" font-size="12pt">  	 
                                    : <%= JSPUtil.printX(form.getStMarketerName())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block font-weight="bold" font-size="12pt">  	 
                                    {L-ENGCession For Month -L}{L-INASesi Bulan-L}
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="15">
                                <fo:block font-weight="bold" font-size="12pt">  	 
                                    : <%=DateUtil.getDateStr(form.getPolicyDateTo(), "MMMM yyyy")%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block font-weight="bold" font-size="12pt">  	 
                                    {L-ENGClass of Bussiness-L}{L-INAJenis Polis-L} 
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="15">
                                <fo:block font-weight="bold" font-size="12pt">  	 
                                    : 
                                    <% if (form.getStPolicyTypeID() != null) {%>
                                    <%=JSPUtil.printX(form.getStPolicyTypeDesc().toUpperCase())%>
                                    <% } else {%>
                                    <%=JSPUtil.printX(form.getStPolicyTypeGroupDesc().toUpperCase())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="17">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">No.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">No. Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Name of Insured</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Circumstance of Situation</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Date</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Curr</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Kurs</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Sum Insured</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Reinsurance</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Gross Premium</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Commission</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm" text-align="center">Nett Premium</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>
                    <fo:table-body>

                        <%
                                    //int counter = 30;
                                    BigDecimal totalTSI = null;
                                    BigDecimal totalTSISHARE = null;
                                    BigDecimal totalPREMI = null;
                                    BigDecimal totalPREMI_RI = null;
                                    BigDecimal totalKOMISI = null;
                                    BigDecimal totalNETTO = null;

                                    BigDecimal[] t = new BigDecimal[7];

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());
                                        t[n] = BDUtil.add(t[n++], pol.getDbTsiReas());
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiNetto());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDComm1());
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiBase());

                                        totalNETTO = BDUtil.sub(pol.getDbPremiNetto(), pol.getDbNDComm1());
                                        t[n] = BDUtil.add(t[n++], totalNETTO);

                                        if (pol.getStCurrencyCode().equalsIgnoreCase("IDR")) {
                                            InsuredIDR = BDUtil.add(InsuredIDR, pol.getDbInsuredAmount());
                                            InsuredReasIDR = BDUtil.add(InsuredReasIDR, pol.getDbTsiReas());
                                            PremiReasIDR = BDUtil.add(PremiReasIDR, pol.getDbPremiNetto());
                                            CommReasIDR = BDUtil.add(CommReasIDR, pol.getDbNDComm1());
                                            NettoReasIDR = BDUtil.add(NettoReasIDR, pol.getDbPremiBase());
                                        } else if (pol.getStCurrencyCode().equalsIgnoreCase("USD")) {
                                            InsuredUSD = BDUtil.add(InsuredUSD, pol.getDbInsuredAmount());
                                            InsuredReasUSD = BDUtil.add(InsuredReasUSD, pol.getDbTsiReas());
                                            PremiReasUSD = BDUtil.add(PremiReasUSD, pol.getDbPremiNetto());
                                            CommReasUSD = BDUtil.add(CommReasUSD, pol.getDbNDComm1());
                                            NettoReasUSD = BDUtil.add(NettoReasUSD, pol.getDbPremiBase());
                                        }

                                        //if (i==counter) {
                                        //    counter = counter + 30;
%>        
                        <%--
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>

                        <% } %>
                        --%>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(String.valueOf(i + 1))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="start"><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getStReference4())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtChangeDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getDbCurrencyRate(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiReas(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiNetto(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm1(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(totalNETTO, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-right-style="solid"><fo:block color="white">.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="17">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">IDR</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(InsuredIDR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(InsuredReasIDR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(PremiReasIDR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(CommReasIDR, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(NettoReasIDR, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% if (Tools.compare(PremiReasUSD, BDUtil.zero) > 0) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">USD</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(InsuredUSD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(InsuredReasUSD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(PremiReasUSD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(CommReasUSD, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(NettoReasUSD, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="17">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt"></fo:block>   
                            </fo:table-cell>  
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[0], 2)%>" orientation="0">
                                            <barcode:datamatrix> 
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>                                    
                                </fo:block> 
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block text-align="center" line-height="5mm">Jakarta, <%=DateUtil.getDateStr(new Date(), "dd MMMM yyyy")%></fo:block>
                                <fo:block text-align="center" line-height="5mm">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>  

        </fo:flow>
    </fo:page-sequence>
</fo:root>

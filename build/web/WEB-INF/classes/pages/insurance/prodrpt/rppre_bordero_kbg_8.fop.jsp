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
            String bordero = null;

            if (policy.getStCustomerAddress() != null) {
                bordero = policy.getStCustomerAddress();
            }
//String slipNo = policy.getStReference2();
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-width="34cm"
                               page-height="21.05cm"                                                                                                                                                                        
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/>
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
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
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
                    <fo:table-column />
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
                            <fo:table-cell number-columns-spanned="16"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="20">
                                <fo:block space-after.optimum="20pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block font-weight="bold" font-size="12pt">  	 
                                    {L-ENGBordereaux No-L}{L-INANo. Bordero-L}
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-weight="bold" font-size="12pt">
                                    <% if (bordero != null) {%>
                                    : <%= JSPUtil.printX(bordero.toUpperCase())%>
                                    <% } else {%>
                                    : <%= DateUtil.getMonth2Digit(form.getPolicyDateTo())%><%= DateUtil.getYear2Digit(form.getPolicyDateTo())%>
                                    / AUTOFAC / CM
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block font-weight="bold" font-size="12pt">  	 
                                    {L-ENGCompany-L}{L-INAReasuradur-L} 
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="18">
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
                            <fo:table-cell number-columns-spanned="18">
                                <fo:block font-weight="bold" font-size="12pt">  	 
                                    : <%=DateUtil.getDateStr(form.getPolicyDateFrom(), "MMMM yyyy")%> - <%=DateUtil.getDateStr(form.getPolicyDateTo(), "MMMM yyyy")%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="20">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">No.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Policy No.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Principal</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">COB</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Inception</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Expiry</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Curr</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Kurs</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Penal Sum</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Reins Share</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Service Charge</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">R/I Comm</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" text-align="center">Net Premium</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm" text-align="center">Remarks</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="20">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>
                    <fo:table-body>

                        <%
                                    //int counter = 30;

                                    BigDecimal jumlahIDRTSI = null;
                                    BigDecimal jumlahUSDTSI = null;
                                    BigDecimal jumlahIDRTSIReas = null;
                                    BigDecimal jumlahUSDTSIReas = null;
                                    BigDecimal jumlahIDRPremi = null;
                                    BigDecimal jumlahUSDPremi = null;
                                    BigDecimal jumlahIDRComm = null;
                                    BigDecimal jumlahUSDComm = null;
                                    BigDecimal jumlahIDRNetto = null;
                                    BigDecimal jumlahUSDNetto = null;

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        if (pol.getStCurrencyCode().equalsIgnoreCase("IDR")) {
                                            jumlahIDRTSI = BDUtil.add(jumlahIDRTSI, pol.getDbInsuredAmount());
                                            jumlahIDRTSIReas = BDUtil.add(jumlahIDRTSIReas, pol.getDbTsiReas());
                                            jumlahIDRPremi = BDUtil.add(jumlahIDRPremi, pol.getDbPremiNetto());
                                            jumlahIDRComm = BDUtil.add(jumlahIDRComm, pol.getDbNDComm1());
                                            jumlahIDRNetto = BDUtil.add(jumlahIDRNetto, pol.getDbPremiBase());
                                        } else if (pol.getStCurrencyCode().equalsIgnoreCase("USD")) {
                                            jumlahUSDTSI = BDUtil.add(jumlahUSDTSI, pol.getDbInsuredAmount());
                                            jumlahUSDTSIReas = BDUtil.add(jumlahUSDTSIReas, pol.getDbTsiReas());
                                            jumlahUSDPremi = BDUtil.add(jumlahUSDPremi, pol.getDbPremiNetto());
                                            jumlahUSDComm = BDUtil.add(jumlahUSDComm, pol.getDbNDComm1());
                                            jumlahUSDNetto = BDUtil.add(jumlahUSDNetto, pol.getDbPremiBase());
                                        }

                                        //if (i==counter) {
                                        //    counter = counter + 30;
                        %>
                        <%--
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="20">
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
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getStReference6())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPeriodStart())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getDbCurrencyRate(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiReas(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiNetto(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm1(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiBase(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="20">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRTSI, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRTSIReas, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRPremi, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRComm, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRNetto, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">USD</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahUSDTSI, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahUSDTSIReas, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahUSDPremi, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahUSDComm, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahUSDNetto, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="20">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="14" ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="6" ><fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(new Date(), "dd MMMM yyyy")%> </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="14" ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="6" ><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>       

            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(jumlahIDRTSI, 2)%>" orientation="0">
                        <barcode:datamatrix> 
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>                                    
            </fo:block> 

        </fo:flow>
    </fo:page-sequence>
</fo:root>

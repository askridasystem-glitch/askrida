<%@ page import="com.webfin.insurance.model.*, 
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         java.text.SimpleDateFormat,
         java.util.Calendar,
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
        <fo:simple-page-master master-name="only" 
                               page-height="33cm"
                               page-width="25cm"
                               margin-top="2cm"
                               margin-bottom="2cm"
                               margin-left="2cm"
                               margin-right="2cm">
            <fo:region-body margin-top="2cm" margin-bottom="1.5cm"/> 
            <fo:region-before extent="3cm"/> 
            <fo:region-after extent="1.5cm"/> 
        </fo:simple-page-master> 

    </fo:layout-master-set>


    <fo:page-sequence master-reference="only"> 

        <fo:flow flow-name="xsl-region-body"> 

            <fo:block font-size="10pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-header>   

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block font-weight="bold" font-size="20pt" text-align="center">             
                                    {L-ENG RECAPITULATION PRODUCTION PREMIUM PER BRANCH-L}{L-INA PERBANDINGAN PRODUKSI DAERAH -L}
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block space-before.optimum="10pt" text-align="center">
                                    <% if (form.getPolicyDateFrom() != null || form.getPolicyDateTo() != null) {%>
                                    <% if (Tools.isEqual(DateUtil.getDateStr(form.getPolicyDateFrom(), "^^ yyyy"), DateUtil.getDateStr(form.getPolicyDateTo(), "^^ yyyy"))) {%>
                                    BULAN : <%=JSPUtil.print(DateUtil.getMonth(form.getPolicyDateFrom()).toUpperCase())%>
                                    <% } else {%>
                                    BULAN : <%=JSPUtil.print(DateUtil.getMonth(form.getPolicyDateFrom()).toUpperCase())%> S/D <%=JSPUtil.print(DateUtil.getMonth(form.getPolicyDateTo()).toUpperCase())%>
                                    <%}
                                                }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block space-before.optimum="10pt" text-align="start">
                                    <% if (form.getPolicyDateFrom() != null || form.getPolicyDateTo() != null) {%>
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid"  border-top-style="solid" border-left-style="solid" border-right-style="solid" number-rows-spanned="2" display-align="center"><fo:block text-align="center">{L-ENG NO. -L}{L-INA NO. -L}</fo:block></fo:table-cell><!-- No -->
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid"  border-top-style="solid" border-left-style="solid" border-right-style="solid" number-rows-spanned="2" display-align="center"><fo:block text-align="center">{L-ENG BRANCH -L}{L-INA DAERAH -L}</fo:block></fo:table-cell><!-- Entry Date -->
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid"  border-top-style="solid" border-left-style="solid" border-right-style="solid" number-columns-spanned="2" ><fo:block text-align="center">{L-ENG PRODUKSI -L}{L-INA PRODUKSI -L}</fo:block></fo:table-cell><!-- Police Date -->
                            <fo:table-cell  border-width="0.5pt" border-bottom-style="solid"  border-top-style="solid" border-left-style="solid" border-right-style="solid" display-align="center"><fo:block text-align="center">{L-ENG SELISIH -L}{L-INA SELISIH -L}</fo:block></fo:table-cell><!-- Police Date -->
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid"  border-top-style="solid" border-left-style="solid" border-right-style="solid" display-align="center"><fo:block text-align="center">{L-ENG PERTUMBUHAN -L}{L-INA PERTUMBUHAN -L}</fo:block></fo:table-cell><!-- Policy No. -->
                        </fo:table-row>



                        <%
                                    Calendar policyDateFroms = Calendar.getInstance();
                                    policyDateFroms.setTime(form.getPolicyDateFrom() != null ? form.getPolicyDateFrom() : new Date());
                                    policyDateFroms.set(Calendar.YEAR, policyDateFroms.get(Calendar.YEAR) - 1);
                        %>



                        <fo:table-row> 
                            <fo:table-cell border-width="0.5pt"  border-bottom-style="solid"  border-top-style="solid"  border-left-style="solid" border-right-style="solid"><fo:block  text-align="center"><%=JSPUtil.printX(new SimpleDateFormat("yyyy").format(policyDateFroms.getTime()))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt"  border-bottom-style="solid"  border-top-style="solid" border-left-style="solid" border-right-style="solid"><fo:block  text-align="center"><%=JSPUtil.printX(new SimpleDateFormat("yyyy").format(form.getPolicyDateFrom()))%></fo:block></fo:table-cell>
                            <fo:table-cell  display-align="center"  border-bottom-style="solid"  border-top-style="solid" border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block  text-align="center">{L-ENG (+/-) -L}{L-INA (+/-) -L}</fo:block></fo:table-cell><!-- Police Date -->
                            <fo:table-cell  display-align="center"  border-bottom-style="solid"  border-top-style="solid" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block  text-align="center">{L-ENG (%) -L}{L-INA (%) -L}</fo:block></fo:table-cell><!-- Policy No. -->
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->   


                    </fo:table-header> 

                    <fo:table-body>

                        <%
                                    BigDecimal[] t = new BigDecimal[5];
                                    BigDecimal prosentasess = BigDecimal.ZERO;
                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDComm1() != null ? pol.getDbNDComm1() : BigDecimal.ZERO);
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDComm2() != null ? pol.getDbNDComm2() : BigDecimal.ZERO);
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDComm2().subtract(pol.getDbNDComm1()));
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDComm1() != null ? pol.getDbNDComm1() : BigDecimal.ZERO);


                                        BigDecimal selisih = BDUtil.sub(pol.getDbNDComm2(), pol.getDbNDComm1());
                                        BigDecimal d = BDUtil.div(selisih, pol.getDbNDComm1());
                                        BigDecimal prosentase = BDUtil.mul(d, new BigDecimal(100));

                                        BigDecimal a = BDUtil.div(t[2], t[0]);
                                        prosentasess = BDUtil.mul(a, new BigDecimal(100));


                        %>   

                        <fo:table-row>   
                            <fo:table-cell  border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid"><fo:block text-align="center"><%=i + 1%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid"><fo:block ><%=JSPUtil.printX(pol.getCostCenter(pol.getStCostCenterCode()).getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid"><fo:block text-align="end"> <%=JSPUtil.printX(pol.getDbNDComm1(), 0)%> </fo:block></fo:table-cell>
                            <fo:table-cell  border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid"><fo:block text-align="end"> <%=JSPUtil.printX(pol.getDbNDComm2(), 0)%> </fo:block></fo:table-cell>
                            <fo:table-cell  border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid" ><fo:block text-align="end"> <%= BDUtil.lesserThanZero(pol.getDbNDComm2().subtract(pol.getDbNDComm1())) == true ? BDUtil.setMinusToKurung(pol.getDbNDComm2().subtract(pol.getDbNDComm1())) : JSPUtil.printX(pol.getDbNDComm2().subtract(pol.getDbNDComm1()), 0)%> </fo:block></fo:table-cell>
                            <%if (Tools.isEqual(pol.getDbNDComm2(), new BigDecimal(0))) {%>
                            <fo:table-cell  border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid" border-right-style="solid" ><fo:block text-align="end"><%=JSPUtil.printX(new BigDecimal(0), 0)%> </fo:block></fo:table-cell>
                            <%} else {%>
                            <fo:table-cell  border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid" border-right-style="solid" ><fo:block text-align="end"><%=BDUtil.lesserThanZero(prosentase) == true ? BDUtil.setMinusToKurung(prosentase) : JSPUtil.printX(prosentase, 0)%> </fo:block></fo:table-cell>
                            <%}%>
                        </fo:table-row>
                        <%

                                    }%>

                        <!-- GARIS DALAM KOLOM -->   


                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid"><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid"><fo:block> TOTAL </fo:block></fo:table-cell><!-- Name of Insured -->
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(t[0], 0)%> </fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(t[1], 0)%> </fo:block></fo:table-cell><!-- Claim Approved -->
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid"><fo:block text-align="end"><%=BDUtil.lesserThanZero(t[2]) == true ? BDUtil.setMinusToKurung(t[2]) : JSPUtil.printX(t[2], 0)%> </fo:block></fo:table-cell><!-- Claim Estimated -->
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-right-style="solid" border-bottom-style="solid"><fo:block text-align="end"><%=BDUtil.lesserThanZero(prosentasess) == true ? BDUtil.setMinusToKurung(prosentasess) : JSPUtil.printX(prosentasess, 0)%> </fo:block></fo:table-cell><!-- Claim Approved -->
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
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[1], 0)%>" orientation="0">
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

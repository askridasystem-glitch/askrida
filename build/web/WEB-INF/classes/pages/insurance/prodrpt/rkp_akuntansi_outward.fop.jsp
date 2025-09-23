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

            final DTOList l = (DTOList) request.getAttribute("RPT");

            final ProductionClaimReportForm form = (ProductionClaimReportForm) SessionManager.getInstance().getCurrentForm();

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 

    <!-- defines page layout --> 
    <fo:layout-master-set> 

        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="only" 
                               page-height="28cm"
                               page-width="73cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-after extent="0cm"/> 
        </fo:simple-page-master> 

    </fo:layout-master-set> 

    <fo:page-sequence master-reference="only" initial-page-number="1"> 

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
                    <fo:table-column column-width="10mm"/><!-- 1 -->
                    <fo:table-column column-width="20mm"/><!-- Entry Date -->
                    <fo:table-column column-width="20mm"/><!-- Police Date -->
                    <fo:table-column column-width="10mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="10mm"/><!-- Police Date -->
                    <fo:table-column column-width="10mm"/><!-- Police Date -->
                    <fo:table-column column-width="40mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- 10 -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- 20 -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Policy No. -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- 27 -->
                    <fo:table-header>   

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="27">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">             
                                    LAPORAN KLAIM UNTUK AKUNTANSI OUTWARD
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="27">
                                <fo:block font-family="serif" font-weight="bold" font-size="18pt" text-align="center">
                                    <% if (form.getAppDateFrom() != null || form.getAppDateTo() != null) {%>
                                    Tanggal : <%=JSPUtil.printX(form.getAppDateFrom())%> S/D <%=JSPUtil.printX(form.getAppDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="27">
                                <fo:block font-weight="bold">
                                    <% if (form.getStBranch() != null) {%>
                                    Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="27">
                                <fo:block font-weight="bold">
                                    <% if (form.getStGroupID() != null) {%>
                                    Group Polis : <%=JSPUtil.printX(form.getStGroupName())%>  
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="27">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">no</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">koda</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">cob</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">u/y</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">no polis</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">no dla</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">curr</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">kurs</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">tsi</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">nilai klaim</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">recovery klaim</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">uwlain</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">adjfee</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">jasabengkel</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">pph jasabengkel</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">total klaim</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">reasurasur</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">claim or</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">claim bppdan</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">claim spl</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">claim qs</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">claim fac</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">claim park</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">claim faco</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">claim jp</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">claim qskr</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">claim xol</fo:block></fo:table-cell>
                        </fo:table-row>   

                        <!-- GARIS DALAM KOLOM -->   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="27">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                    </fo:table-header> 

                    <fo:table-body>

                        <%
                                    BigDecimal totalKlaim = new BigDecimal(0);

                                    BigDecimal[] t = new BigDecimal[18];
                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDComm1());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDComm2());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDComm3());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDComm4());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDBrok1());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDBrok2());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimOR());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimBPDAN());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimSPL());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimQS());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimFAC());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimPARK());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimFACO());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimJP());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimQSKR());
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimXOL1());

                                        totalKlaim = BDUtil.add(pol.getDbNDComm1(), pol.getDbNDComm2());
                                        totalKlaim = BDUtil.add(totalKlaim, pol.getDbNDComm3());
                                        totalKlaim = BDUtil.add(totalKlaim, pol.getDbNDComm4());
                                        totalKlaim = BDUtil.add(totalKlaim, pol.getDbNDBrok1());
                                        totalKlaim = BDUtil.add(totalKlaim, pol.getDbNDBrok2());
                                        t[n] = BDUtil.add(t[n++], totalKlaim);


                        %>   

                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=i + 1%></fo:block></fo:table-cell>    <!-- No --><!-- No -->
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCostCenterCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStReference5())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=DateUtil.getYear(pol.getDtPeriodStart())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStDLANo())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDbCurrencyRate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm1(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm2(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm3(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm4(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDBrok1(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDBrok2(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(totalKlaim, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStReference2())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimOR(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimBPDAN(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimSPL(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimQS(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimFAC(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimPARK(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimFACO(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimJP(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimQSKR(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimXOL1(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>   

                        <%
                                    }
                        %>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="27">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="8"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[4], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[5], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[6], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[17], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ></fo:table-cell><!-- Name of Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[7], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[8], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[9], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[10], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[11], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[12], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[13], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[14], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[15], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[16], 2)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                        </fo:table-row>   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="27">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>   

                    </fo:table-body>   
                </fo:table>   
            </fo:block> 

            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "dd-MMM-yyyy hh:mm:ss")%>
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
                        <svg xmlns="http://www.w3.org/2000/svg" width="5cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
        </fo:flow>   

    </fo:page-sequence>   
</fo:root>   

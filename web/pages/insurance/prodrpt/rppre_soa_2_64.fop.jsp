<%@ page import="com.webfin.insurance.model.*,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.form.ProductionReinsuranceReportForm,
         com.crux.common.parameter.Parameter,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>

<%
            final DTOList l = (DTOList) request.getAttribute("RPT");
            //final DTOList m = (DTOList) request.getAttribute("RPS");

            final ProductionReinsuranceReportForm form = (ProductionReinsuranceReportForm) SessionManager.getInstance().getCurrentForm();

            BigDecimal totalGrossPremi = null;
            BigDecimal totalKomisi = null;
            BigDecimal totalClaim = null;
            BigDecimal totalNetto = null;
            BigDecimal share = null;
            String captive = null;

            String treaty = null;
            if (form.getStFltTreatyType() != null) {
                if (form.getStFltTreatyTypeDesc().equalsIgnoreCase("AutoFac1")) {
                    treaty = "AUTOMATIC FACULTATIVE";
                } else {
                    treaty = form.getStFltTreatyTypeDesc();
                }
            }
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-width="21cm"
                               page-height="33cm"
                               margin-top="4cm"
                               margin-bottom="0.5cm"                                                                                                                                                                           
                               margin-left="1.5cm"
                               margin-right="1.5cm">
            <fo:region-body margin-top="2cm" margin-bottom="1.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <!-- usage of page layout -->

        <fo:flow flow-name="xsl-region-body">

            <fo:block text-align="center"
                      font-size="18pt"
                      font-family="TAHOMA"
                      line-height="12pt" 
                      font-weight="bold">
                PT. ASURANSI BANGUN ASKRIDA
            </fo:block>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="20pt" space-after.optimum="25pt"></fo:block>

            <fo:block text-align="center"
                      font-size="20pt"
                      font-family="Arial"
                      line-height="12pt" 
                      font-weight="bold"
                      font-style="italic">
                Statement Of Account
            </fo:block> 

            <fo:block font-size="10pt" space-after.optimum="20pt"></fo:block>

            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="170mm"/>
                    <fo:table-body>

                        <fo:table-row>   
                            <fo:table-cell ><fo:block font-weight="bold">To :  </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ><%= JSPUtil.printX(form.getStEntityName())%></fo:block></fo:table-cell>  
                        </fo:table-row> 

                    </fo:table-body>
                </fo:table>
            </fo:block>   

            <fo:block font-size="10pt" space-after.optimum="20pt"></fo:block>

            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="125mm"/>
                    <fo:table-body>

                        <fo:table-row>   
                            <fo:table-cell ><fo:block font-weight="bold">Treaty</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >:</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block >
                                    <% if (form.getPeriodFrom() != null) {%>
                                    <%=DateUtil.getYear(form.getPeriodFrom())%>
                                    <% }%>   
                                    <% if (form.getStPolicyTypeGroupID() != null) {%>
                                    <%= Parameter.readString("JENAS_" + form.getStPolicyTypeGroupID())%>
                                    <% }%>      		 
                                    <% if (form.getStFltTreatyType() != null) {%>
                                    <%=JSPUtil.printX(treaty)%>
                                    <% }%> 
                                </fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>   
                            <fo:table-cell ><fo:block font-weight="bold">Accounting Period</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >:</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block >
                                    <% if (form.getPolicyDateFrom() != null) {%>
                                    <%= Parameter.readString("MONTH_" + DateUtil.getMonth(form.getPolicyDateFrom()))%>
                                    <% }%>   
                                    <% if (form.getPolicyDateFrom() != null) {%>
                                    <%=DateUtil.getYear(form.getPolicyDateFrom())%>
                                    <% }%> (
                                    <% if (form.getPolicyDateFrom() != null) {%>
                                    <%= Parameter.readString("PERIODE_" + DateUtil.getMonth(form.getPolicyDateFrom()))%>
                                    <% }%>
                                    <% if (form.getPolicyDateFrom() != null) {%>
                                    <%=DateUtil.getYear(form.getPolicyDateFrom())%>
                                    <% }%> )
                                </fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>   
                            <fo:table-cell ><fo:block font-weight="bold">Accounting System</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >:</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block >Run-off</fo:block></fo:table-cell>  
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell ><fo:block font-weight="bold">U/Y</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >:</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block >
                                    <% if (form.getPeriodFrom() != null) {%>
                                    <%=DateUtil.getYear(form.getPeriodFrom())%>
                                    <% }%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="end" font-weight="bold">Currency: Indonesian Rupiah</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>   

            <!-- defines text title level 1-->
            <fo:block font-size="7pt" line-height="8pt"></fo:block>


            <!-- GARIS  -->
            <!-- Normal text -->


            <fo:block font-size="12pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-body>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="3" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">TECHINAL ACCOUNT</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">DEBIT</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm">CREDIT</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" font-weight="bold">Premium</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        totalGrossPremi = BDUtil.add(totalGrossPremi, pol.getDbPremiAmt());

                                        if (pol.getStCustomerName().equalsIgnoreCase("1")
                                                || pol.getStCustomerName().equalsIgnoreCase("4")
                                                || pol.getStCustomerName().equalsIgnoreCase("6")) {
                                            captive = "CAPTIVE";
                                        } else if (pol.getStCustomerName().equalsIgnoreCase("2")
                                                || pol.getStCustomerName().equalsIgnoreCase("5")
                                                || pol.getStCustomerName().equalsIgnoreCase("7")) {
                                            captive = "NON CAPTIVE";
                                        }
                        %>        

                        <fo:table-row>
                            <% if (captive != null) {%>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm"><%=JSPUtil.printX(pol.getStCoverTypeCode())%> (<%=JSPUtil.printX(captive)%>)</fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm"><%=JSPUtil.printX(pol.getStCoverTypeCode())%></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbPremiAmt(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>      

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" font-weight="bold">Commission</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <%
                                    for (int j = 0; j < l.size(); j++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(j);

                                        totalKomisi = BDUtil.add(totalKomisi, pol.getDbNDBrok2Pct());

                                        if (form.getStEntityID().equalsIgnoreCase("214")) {
                                            share = new BigDecimal(20);
                                        } else if (form.getStEntityID().equalsIgnoreCase("215")) {
                                            share = new BigDecimal(60);
                                        } else if (form.getStEntityID().equalsIgnoreCase("217")) {
                                            share = new BigDecimal(20);
                                        }

                        %>

                        <fo:table-row>
                            <% if (captive != null) {%>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm"><%=JSPUtil.printX(pol.getStCoverTypeCode())%> (<%=JSPUtil.printX(captive)%>)</fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm"><%=JSPUtil.printX(pol.getStCoverTypeCode())%></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbNDBrok2Pct(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>        

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm" font-weight="bold">Paid Loss</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <%
                                    for (int k = 0; k < l.size(); k++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(k);

                                        if (Tools.isEqual(pol.getDbClaimAmount(), new BigDecimal(0))) {
                                            continue;
                                        }

                                        totalClaim = BDUtil.add(totalClaim, pol.getDbClaimAmount());
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm"><%=JSPUtil.printX(pol.getStCoverTypeCode())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbClaimAmount(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>    

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm">100% Total</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(BDUtil.add(totalKomisi, totalClaim), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(totalGrossPremi, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="2pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                    totalNetto = BDUtil.sub(totalGrossPremi, BDUtil.add(totalKomisi, totalClaim));
                        %>                    

                        <fo:table-row>
                            <fo:table-cell ><fo:block >100% Balance</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(totalNetto, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                    BigDecimal netto = BDUtil.div(BDUtil.mul(share, totalNetto), new BigDecimal(100));
                                    String sharedesc = null;

                                    if (Tools.compare(netto, new BigDecimal(0)) < 0) {
                                        sharedesc = "Due To Us";
                                    } else {
                                        sharedesc = "Due To You";
                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block font-weight="bold">Your Share <%= JSPUtil.printX(share)%>% (<%=JSPUtil.printX(sharedesc)%>)</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.div(BDUtil.mul(share, totalNetto), new BigDecimal(100)), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block  space-after.optimum="30pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(new Date(), "d MMM yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">Reinsurance Division</fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">S. E. &#x26; O.</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block> 

        </fo:flow>
    </fo:page-sequence>
</fo:root>

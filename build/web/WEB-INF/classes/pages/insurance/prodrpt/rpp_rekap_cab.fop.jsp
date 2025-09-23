<%@ page import="com.webfin.insurance.model.*, 
         com.crux.util.*,
         java.math.BigDecimal,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.form.ProductionMarketingReportForm,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final DTOList l = (DTOList) request.getAttribute("RPT");

            final ProductionMarketingReportForm form = (ProductionMarketingReportForm) SessionManager.getInstance().getCurrentForm();

            BigDecimal inward = new BigDecimal(0);
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
                               margin-left="1cm"
                               margin-right="1cm">
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
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <% if (!form.isClosed()) {%>
                                <fo:block font-family="Helvetica" font-weight="bold" text-align="center" color="red">DRAFT</fo:block>
                                <% }%>

                                <fo:block font-weight="bold" font-size="20pt" text-align="center">             
                                    {L-ENG RECAPITULATION PRODUCTION PREMIUM PER BRANCH-L}{L-INA REKAPITULASI PRODUKSI PREMI PER CABANG-L}
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <%--
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block text-align="center" space-after.optimum="20pt">             
                                    {L-ENG (After Disc)-L}{L-INA (Setelah Diskon)-L}
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        --%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block text-align="center">
                                    <% if (form.getPolicyDateFrom() != null || form.getPolicyDateTo() != null) {%>
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block text-align="center">
                                    <% if (form.getAppDateFrom() != null || form.getAppDateTo() != null) {%>
                                    Tanggal Setujui : <%=JSPUtil.printX(form.getAppDateFrom())%> S/D <%=JSPUtil.printX(form.getAppDateTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block text-align="center">
                                    <% if (form.getPeriodFrom() != null || form.getPeriodTo() != null) {%>
                                    Periode : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block>
                                    <% if (form.getStPolicyTypeGroupDesc() != null) {%>
                                    <% if (!form.getStPolicyTypeGroupDesc().equalsIgnoreCase("[ALL]")) {%> 
                                    Group Jenis Asuransi : <%=JSPUtil.printX(form.getStPolicyTypeGroupDesc())%>
                                    <% }%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block>
                                    <% if (form.getStPolicyTypeDesc() != null) {%>
                                    <% if (!form.getStPolicyTypeDesc().equalsIgnoreCase("[select one]")) {%> 
                                    Jenis Asuransi : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>
                                    <% }%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block>
                                    <% if (form.getStEntityName() != null) {%>
                                    Customer : <%=JSPUtil.printX(form.getStEntityName())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block>
                                    <% if (form.getStMarketerName() != null) {%>
                                    Marketer : <%=JSPUtil.printX(form.getStMarketerName())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block>
                                    <% if (form.getStCreditName() != null) {%>
                                    Jenis Kredit : <%=JSPUtil.printX(form.getStCreditName())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block>
                                    <% if (form.getStCompanyName() != null) {%>
                                    Company Group : <%=JSPUtil.printX(form.getStCompanyName())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block>
                                    <% if (form.getStCompanyProdName() != null) {%>
                                    Marketer Group : <%=JSPUtil.printX(form.getStCompanyProdName())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block>
                                    <% if (form.getStStatus() != null) {%>
                                    Status : <%=JSPUtil.printX(form.getStStatus())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block>
                                    <% if (form.getStCreateID() != null) {%>
                                    Create : <%=JSPUtil.printX(form.getStCreateName())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <% if (form.getStBussinessPolType() != null) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="start" font-weight="bold">Unit Non AKS</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="end" font-weight="bold">{L-ENG (In IDR)-L}{L-INA (dalam rupiah)-L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } else {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block text-align="end" font-weight="bold">{L-ENG (In IDR)-L}{L-INA (dalam rupiah)-L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                        <fo:table-row>   
                            <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block text-align="center">{L-ENG NO. -L}{L-INA NO. -L}</fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block text-align="center">{L-ENG BRANCH -L}{L-INA DAERAH -L}</fo:block></fo:table-cell><!-- Entry Date --> 
                            <fo:table-cell number-columns-spanned="4" ><fo:block text-align="center">{L-ENG CATEGORY -L}{L-INA SUMBER BISNIS -L}</fo:block></fo:table-cell><!-- Police Date --> 
                            <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block text-align="center">{L-ENG SUBTOTAL -L}{L-INA JUMLAH -L}</fo:block></fo:table-cell><!-- Policy No. --> 
                        </fo:table-row>   

                        <fo:table-row> 
                            <fo:table-cell ><fo:block text-align="end">UMUM</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end">PEMDA</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end">PERUSDA</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end">BPD</fo:block></fo:table-cell> 
                        </fo:table-row> 

                        <!-- GARIS DALAM KOLOM -->   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                    </fo:table-header> 

                    <fo:table-body>

                        <%
                                    BigDecimal[] t = new BigDecimal[5];
                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDComm1());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDComm2());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDComm3());
                                        t[n] = BDUtil.add(t[n++], pol.getDbNDComm4());
                                        t[n] = BDUtil.add(t[n++], pol.getDbPremiTotalAfterDisc());

                                        inward = pol.getDbPremiBase();
                        %>   

                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=i + 1%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(pol.getCostCenter(pol.getStCostCenterCode()).getStDescription())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm1(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm2(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm3(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm4(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotalAfterDisc(), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>   

                        <% }%>   

                        <!-- GARIS DALAM KOLOM -->   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                        <%
                                    if (SessionManager.getInstance().getSession().getStBranch() != null) {
                        %> 

                        <fo:table-row>   
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>TOTAL </fo:block></fo:table-cell><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0], 0)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1], 0)%></fo:block></fo:table-cell><!-- Claim Estimated -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2], 0)%></fo:block></fo:table-cell><!-- Claim Approved -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3], 0)%></fo:block></fo:table-cell><!-- Claim Estimated -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[4], 0)%></fo:block></fo:table-cell><!-- Claim Approved -->
                        </fo:table-row> 

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>

                        <% } else {%>

                        <fo:table-row>   
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>SUB-TOTAL </fo:block></fo:table-cell><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0], 0)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1], 0)%></fo:block></fo:table-cell><!-- Claim Estimated -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2], 0)%></fo:block></fo:table-cell><!-- Claim Approved -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3], 0)%></fo:block></fo:table-cell><!-- Claim Estimated -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[4], 0)%></fo:block></fo:table-cell><!-- Claim Approved -->
                        </fo:table-row> 

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>INWARD </fo:block></fo:table-cell><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(inward, 0)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(new BigDecimal(0), 0)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(new BigDecimal(0), 0)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(new BigDecimal(0), 0)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(inward, 0)%></fo:block></fo:table-cell><!-- Claim Approved -->
                        </fo:table-row> 

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>   

                        <fo:table-row>   
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>TOTAL </fo:block></fo:table-cell><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(t[0], inward), 0)%></fo:block></fo:table-cell><!-- Total Sum Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1], 0)%></fo:block></fo:table-cell><!-- Claim Estimated -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2], 0)%></fo:block></fo:table-cell><!-- Claim Approved -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3], 0)%></fo:block></fo:table-cell><!-- Claim Estimated -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(t[4], inward), 0)%></fo:block></fo:table-cell><!-- Claim Approved -->
                        </fo:table-row>   

                        <!-- GARIS DALAM KOLOM -->   

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                        <% }%>

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
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(BDUtil.add(t[4], inward), 0)%>" orientation="0">
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

<%@ page import="com.webfin.ar.model.*,  
         com.webfin.ar.forms.FRRPTrptArAPDetailForm,
         com.crux.ff.model.FlexFieldHeaderView,
         com.webfin.insurance.model.InsurancePolicyInwardView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.util.Tools,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%
            final DTOList l = (DTOList) request.getAttribute("RPT");

            final FRRPTrptArAPDetailForm form = (FRRPTrptArAPDetailForm) SessionManager.getInstance().getCurrentForm();

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 

    <!-- defines page layout --> 
    <fo:layout-master-set> 

        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="5cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
            <fo:region-before extent="0cm"/> 
            <fo:region-after extent="0.5cm"/> 
        </fo:simple-page-master> 

    </fo:layout-master-set> 
    <!-- end: defines page layout --> 

    <!-- actual layout --> 
    <fo:page-sequence master-reference="first"> 

        <!-- usage of page layout --> 
        <fo:flow flow-name="xsl-region-body"> 

            <fo:block font-size="12pt">
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="30mm"/><!-- Entry Date -->
                    <fo:table-column column-width="40mm"/><!-- Policy No -->
                    <fo:table-column column-width="40mm"/><!-- Premium -->
                    <fo:table-column column-width="40mm"/><!-- Biaya Polis -->
                    <fo:table-column column-width="40mm"/><!-- Biaya Polis -->
                    <fo:table-header>          

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block font-weight="bold" text-align="center">
                                    PRODUKSI INWARD PER BULAN
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block font-weight="bold" text-align="center">
                                    <% if (form.getPolicyDateFrom() != null || form.getPolicyDateTo() != null) {%>
                                    TAHUN : <%=DateUtil.getYear(form.getPolicyDateFrom())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.25pt" border-style="solid" line-height="0.25pt" space-before.optimum="3pt"></fo:block>
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid"><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid"><fo:block text-align="center">{L-ENG Month -L}{L-INA Bulan -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid"><fo:block text-align="center">{L-ENG Facultatif -L}{L-INA Facultatif -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid"><fo:block text-align="center">{L-ENG Treaty -L}{L-INA Treaty -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid"><fo:block text-align="center">{L-ENG XOL -L}{L-INA XOL -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center">Total</fo:block></fo:table-cell>
                        </fo:table-row>    

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>   

                    </fo:table-header>   

                    <fo:table-body>   
                        <%

                                    BigDecimal[] t = new BigDecimal[5];
                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyInwardView invoic = (InsurancePolicyInwardView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], invoic.getDbPremi());
                                        t[n] = BDUtil.add(t[n++], invoic.getDbPcost());
                                        t[n] = BDUtil.add(t[n++], invoic.getDbStampDuty());
                                        t[n] = BDUtil.add(t[n++], invoic.getDbFee());

                        %>

                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid"><fo:block text-align="center"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>    <!-- No --><!-- No -->
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStReferenceX1())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbPremi(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbPcost(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbStampDuty(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbFee(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                    }
                        %>     
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2" border-width="0.5pt" padding="2pt" border-left-style="solid"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(t[0], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(t[1], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(t[2], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(t[3], 2)%></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block border-width="0.25pt" border-style="solid" line-height="0.25pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                    </fo:table-body>
                </fo:table>   
            </fo:block> 

            <fo:block font-size="6pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
            </fo:block> 

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[4], 0)%>" orientation="0">
                                            <barcode:datamatrix> 
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>                                    
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(new Date(), "dd ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">S. E. &#x26; O</fo:block>
                                <fo:block text-align="center">Reinsurance Dept.,</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block> 

        </fo:flow> 
    </fo:page-sequence>   
</fo:root>   
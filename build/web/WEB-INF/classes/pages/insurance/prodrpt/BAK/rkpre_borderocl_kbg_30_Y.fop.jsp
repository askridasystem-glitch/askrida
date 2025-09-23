<%@ page import="com.webfin.insurance.model.*,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.form.ProductionClaimReportForm,
         org.joda.time.DateTime,
         org.joda.time.Months,
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
                               page-width="21cm"
                               page-height="30cm"
                               margin-top="4cm"
                               margin-bottom="0.5cm"                                                                                                                                                                           
                               margin-left="2cm"
                               margin-right="2cm">
            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set> 
    <!-- end: defines page layout --> 

    <!-- actual layout --> 
    <fo:page-sequence id="N2528" master-reference="only">         

        <fo:flow flow-name="xsl-region-body">

            <fo:block >
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>    

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block font-size="12pt">  	 
                                    <%=form.getMarketer().getStEntityName().toUpperCase()%>
                                </fo:block>
                            </fo:table-cell >
                            <fo:table-cell ></fo:table-cell >
                            <fo:table-cell >
                                <fo:block font-weight="bold" font-size="12pt">  	 
                                    DEBIT NOTE
                                </fo:block>
                            </fo:table-cell >
                            <fo:table-cell ></fo:table-cell >
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell >
                            <fo:table-cell ></fo:table-cell >
                            <fo:table-cell >
                                <fo:block font-weight="bold" font-size="8pt">  	 
                                    Date
                                </fo:block>
                            </fo:table-cell >
                            <fo:table-cell > 
                                <fo:block font-size="8pt">  	 
                                    : {L-ENG <%=DateUtil.getDateStr(new Date(), "MMMM dd yyyy")%>-L}{L-INA <%=DateUtil.getDateStr(new Date(), "dd MMMM yyyy")%>-L}
                                </fo:block>
                            </fo:table-cell >
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>  

            <fo:block space-before.optimum="100pt"></fo:block>   

            <fo:block font-size="12pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column />
                    <fo:table-body>    

                        <%
                                    BigDecimal[] t = new BigDecimal[1];

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], pol.getDbClaimAmount());

                                    }
                        %> 

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="start">CLAIM PERIOD</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start">
                                    : <%=DateUtil.getDateStr(form.getAppDateFrom(), "MMMM yyyy")%> up to <%=DateUtil.getDateStr(form.getAppDateTo(), "MMMM yyyy")%>
                                </fo:block></fo:table-cell>
                                <%--<fo:table-cell ><fo:block text-align="start">: <%= DateUtil.getMonth2Digit(form.getPolicyDateTo())%><%= DateUtil.getYear2Digit(form.getPolicyDateTo())%>
                                / AUTOFAC / CM</fo:block></fo:table-cell>
                                --%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="start">CLASS OF BUSINESS</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start">: 
                                    <% if (form.getStPolicyTypeID() != null) {%>
                                    <%=JSPUtil.printX(form.getStPolicyTypeDesc().toUpperCase())%>
                                    <% } else {%>
                                    <%=JSPUtil.printX(form.getStPolicyTypeGroupDesc().toUpperCase())%>
                                    <% }%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="start">CURR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start">: IDR</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>  

            <fo:block space-before.optimum="100pt"></fo:block>   

            <fo:block font-size="12pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>    

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="start">Net Claim Due To You</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0], 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="150pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>  
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block text-align="start">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="start">{L-ENG REINSURANCE DEPARTMENT -L}{L-INA BAGIAN REASURANSI -L}</fo:block>
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
                        <svg xmlns="http://www.w3.org/2000/svg" width="15cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>

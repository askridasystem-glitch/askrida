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

            String share = null;
            if (form.getStCompanyID().equalsIgnoreCase("166")) {
                share = "5";
            } else if (form.getStCompanyID().equalsIgnoreCase("1005838")) {
                share = "5";
            } else if (form.getStCompanyID().equalsIgnoreCase("113")) {
                share = "90";
            }

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 

    <!-- defines page layout --> 
    <fo:layout-master-set> 

        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-width="21cm"
                               page-height="29.5cm"
                               margin-top="5cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
            <fo:region-before extent="0cm"/> 
            <fo:region-after extent="0cm"/> 
        </fo:simple-page-master> 

    </fo:layout-master-set> 
    <!-- end: defines page layout --> 

    <!-- actual layout --> 
    <fo:page-sequence master-reference="first"> 

        <!-- usage of page layout --> 
        <fo:flow flow-name="xsl-region-body">

            <%
                        int pn = 0;
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyInwardView inw = (InsurancePolicyInwardView) l.get(i);

                            pn++;
            %>

            <%if (pn > 1) {%>
            <fo:block break-after="page"></fo:block>
            <% }%>

            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="25mm"/><!-- No -->
                    <fo:table-column column-width="3mm"/><!-- Entry Date -->
                    <fo:table-column column-width="5mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Premium -->
                    <fo:table-column column-width="3mm"/><!-- Entry Date -->
                    <fo:table-column column-width="5mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Premium -->
                    <fo:table-column column-width="3mm"/><!-- Entry Date -->
                    <fo:table-column column-width="5mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Premium -->
                    <fo:table-column column-width="3mm"/><!-- Entry Date -->
                    <fo:table-column column-width="5mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Premium -->
                    <fo:table-column column-width="3mm"/><!-- Entry Date -->
                    <fo:table-column column-width="5mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Premium -->
                    <fo:table-column column-width="3mm"/><!-- Entry Date -->
                    <fo:table-column column-width="5mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Premium -->
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block font-size="12pt" font-weight="bold" text-align="center">
                                    CREDIT NOTE
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block space-after.optimum="20pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Reference No.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="17"><fo:block text-align="start"><%=JSPUtil.xmlEscape(inw.getStTransactionNoReference())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block space-after.optimum="20pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>To</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="17">
                                <fo:block text-align="start" font-weight="bold"><%=JSPUtil.xmlEscape(inw.getEntity().getStEntityName())%></fo:block>
                                <fo:block text-align="start"><%=JSPUtil.xmlEscape(inw.getEntity().getStAddress())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block space-after.optimum="20pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Reinsured</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="17"><fo:block text-align="start" font-weight="bold">PT. Asuransi Bangun Askrida</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block space-after.optimum="20pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Kind Of Treaty</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="17"><fo:block text-align="start" font-weight="bold">WHOLE ACCOUNT EXCESS OF LOSS</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block space-after.optimum="20pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Period</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="17"><fo:block text-align="start"><%=DateUtil.getDateStr(form.getPolicyDateFrom(), "d ^^ yyyy")%> to <%=DateUtil.getDateStr(form.getPolicyDateTo(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block space-after.optimum="20pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Minded Premium</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">Sub Layer</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">1st Layer</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">2nd Layer</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">3rd Layer</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">4th Layer</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">5th Layer</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block space-after.optimum="20pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(inw.getDbReference6(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(inw.getDbReference1(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(inw.getDbReference2(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(inw.getDbReference3(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(inw.getDbReference4(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(inw.getDbReference5(), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block space-after.optimum="20pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                    BigDecimal share1 = new BigDecimal(0);
                                                    BigDecimal share2 = new BigDecimal(0);
                                                    BigDecimal share3 = new BigDecimal(0);
                                                    BigDecimal share4 = new BigDecimal(0);
                                                    BigDecimal share5 = new BigDecimal(0);
                                                    BigDecimal share6 = new BigDecimal(0);

                                                    BigDecimal totalPremi = BDUtil.add(inw.getDbReference1(), inw.getDbReference2());
                                                    totalPremi = BDUtil.add(totalPremi, inw.getDbReference3());
                                                    totalPremi = BDUtil.add(totalPremi, inw.getDbReference4());
                                                    totalPremi = BDUtil.add(totalPremi, inw.getDbReference5());
                                                    totalPremi = BDUtil.add(totalPremi, inw.getDbReference6());

                                                    share1 = BDUtil.mul(inw.getDbReference1(), BDUtil.getRateFromPct(new BigDecimal(share)));
                                                    share2 = BDUtil.mul(inw.getDbReference2(), BDUtil.getRateFromPct(new BigDecimal(share)));
                                                    share3 = BDUtil.mul(inw.getDbReference3(), BDUtil.getRateFromPct(new BigDecimal(share)));
                                                    share4 = BDUtil.mul(inw.getDbReference4(), BDUtil.getRateFromPct(new BigDecimal(share)));
                                                    share5 = BDUtil.mul(inw.getDbReference5(), BDUtil.getRateFromPct(new BigDecimal(share)));
                                                    share6 = BDUtil.mul(inw.getDbReference6(), BDUtil.getRateFromPct(new BigDecimal(share)));

                        %>

                        <fo:table-row>
                            <fo:table-cell><fo:block font-weight="bold">Your Share <%=share%>%</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(share6, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(share1, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(share2, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(share3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(share4, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(share5, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block space-after.optimum="20pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block font-weight="bold">Total Premium</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(totalPremi, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="15"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block space-after.optimum="20pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19"><fo:block font-weight="bold">PAYABLE IN FOUR INSTALLMENT</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Premium Due</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">Sub Layer</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">1st Layer</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">2nd Layer</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">3rd Layer</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">4th Layer</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">5th Layer</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block space-after.optimum="10pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                    String installment = null;
                                                    BigDecimal install1 = new BigDecimal(0);
                                                    BigDecimal install2 = new BigDecimal(0);
                                                    BigDecimal install3 = new BigDecimal(0);
                                                    BigDecimal install4 = new BigDecimal(0);
                                                    BigDecimal install5 = new BigDecimal(0);
                                                    BigDecimal install6 = new BigDecimal(0);

                                                    install1 = BDUtil.div(share1, new BigDecimal(4));
                                                    install2 = BDUtil.div(share2, new BigDecimal(4));
                                                    install3 = BDUtil.div(share3, new BigDecimal(4));
                                                    install4 = BDUtil.div(share4, new BigDecimal(4));
                                                    install5 = BDUtil.div(share5, new BigDecimal(4));
                                                    install6 = BDUtil.div(share6, new BigDecimal(4));

                                                    for (int j = 1; j <= 4; j++) {

                                                        if (j == 1) {
                                                            installment = "1st Installment 01/01/";
                                                        } else if (j == 2) {
                                                            installment = "2nd Installment 01/04/";
                                                        } else if (j == 3) {
                                                            installment = "3rd Installment 01/07/";
                                                        } else if (j == 4) {
                                                            installment = "4th Installment 01/10/";
                                                        }
                        %>


                        <fo:table-row>
                            <fo:table-cell><fo:block><%=installment%><%=DateUtil.getYear(form.getPolicyDateFrom())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(install6, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(install1, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(install2, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(install3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(install4, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(install5, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block space-after.optimum="10pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <%
                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell><fo:block font-weight="bold">BALANCE DUE</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(share6, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(share1, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(share2, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(share3, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(share4, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" color="white">.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(share5, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="19">
                                <fo:block space-after.optimum="20pt">
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>   
            </fo:block>

            <fo:block font-size="8pt">
                *)PPW/WPC : 60 days from each Installment
            </fo:block>

            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="75mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="75mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(inw.getDbReference1(), 0)%>" orientation="0">
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
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(new Date(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">S. E. &#x26; O</fo:block>
                                <fo:block text-align="center">Reinsurance Dept.,</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

        </fo:flow> 
    </fo:page-sequence>   
</fo:root>   
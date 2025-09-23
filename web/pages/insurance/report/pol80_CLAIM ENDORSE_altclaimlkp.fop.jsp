<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.util.Date,
         com.crux.util.Tools,
         java.math.BigDecimal,
         com.crux.web.controller.SessionManager,
         com.crux.common.parameter.Parameter,
         com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String otorized = (String) request.getAttribute("authorized");
            String objectName = null;
            String objectID = null;
            String claimLoss = null;
            String coinsName = null;
            Date tglMulaiKreasi = null;
            Date tglAkhirKreasi = null;

            boolean effective = pol.isEffective();


//if (true) throw new NullPointerException();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="29.5cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="0.5cm"
                               margin-left="1.5cm"
                               margin-right="1.5cm">

            <fo:region-body margin-top="4.5cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>


    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <!-- usage of page layout -->
        <!-- HEADER -->

        <fo:flow flow-name="xsl-region-body">

            <% if (!effective) {%>
            <fo:block font-size="16pt"
                      line-height="16pt" space-after.optimum="0pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="10pt"></fo:block>

            <!-- defines text title level 1-->
            <fo:block font-size="16pt" line-height="16pt" color="black" text-align="center">
                {L-ENG ENDORSEMENT -L}{L-INA ENDORSEMEN -L}
            </fo:block>

            <fo:block font-size="16pt" line-height="16pt" color="black" text-align="center">
                {L-INA Klaim Pasti Asuransi-L}<%=pol.getStPolicyTypeDesc2()%>{L-ENG Definitive Loss Advice-L}
            </fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="10pt" color="black" text-align="center"> NO. <%=pol.getStDLANo()%></fo:block>

            <!-- Normal text -->

            <!-- DYNAMIC HEADER WORDING -->


            <!-- GARIS -->
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>

            <!-- Normal text -->

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <%
                                    final DTOList objects3 = pol.getObjectsClaim();
                                    for (int j = 0; j < objects3.size(); j++) {
                                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects3.get(j);

                                        tglMulaiKreasi = obj.getDtReference2();

                                        tglAkhirKreasi = obj.getDtReference3();

                                        objectName = obj.getStReference1();

                                        objectID = obj.getStPolicyObjectID();
                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>1.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Policy Type-L}{L-INA Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%= pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>2.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>3.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%=pol.getStCustomerName()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Claim Object-L}{L-INA Objek Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%=objectName%>		Object ID : <%=objectID%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% DTOList cause = pol.getClaimCause();
                                    for (int k = 0; k < cause.size(); k++) {
                                        InsuranceClaimCauseView cau = (InsuranceClaimCauseView) cause.get(k);
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>4.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Claim Cause-L}{L-INA Objek Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%=JSPUtil.printX(cau.getStDescription())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>


                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>5.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="35mm"/>
                                            <fo:table-column column-width="2mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column column-width="30mm"/>
                                            <fo:table-body>

                                                <%
                                                            final DTOList objects = pol.getObjectsClaim();

                                                            for (int i = 0; i < objects.size(); i++) {
                                                                InsurancePolicyObjectView io = (InsurancePolicyObjectView) objects.get(i);

                                                                claimLoss = io.getClaimLossDesc();

                                                                final DTOList suminsureds = io.getSuminsureds();

                                                                for (int j = 0; j < suminsureds.size(); j++) {
                                                                    InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);
                                                %>

                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block>: <%=JSPUtil.printX(tsi.getStInsuranceTSIDesc())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(tsi.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>

                                                <%
                                                                }
                                                            }
                                                %>

                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                                </fo:block></fo:table-cell>
                        </fo:table-row>    

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>6.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Period Date-L}{L-INA Jangka Waktu Pertanggungan-L}</fo:block></fo:table-cell>
                            <%if (pol.getStPolicyTypeID().equalsIgnoreCase("21")
                                         || pol.getStPolicyTypeID().equalsIgnoreCase("59")
                                         || pol.getStPolicyTypeID().equalsIgnoreCase("80")) { %>
                            <fo:table-cell ><fo:block>: <%=DateUtil.getDateStr(tglMulaiKreasi, "d ^^ yyyy")%> s/d <%=DateUtil.getDateStr(tglAkhirKreasi, "d ^^ yyyy")%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell ><fo:block>: <%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%> s/d <%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>7.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Invoice-L}{L-INA Keterangan Pembayaran Premi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%=JSPUtil.printX(pol.getStClaimBenefitDesc())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>8.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Date/Claim Event Location-L}{L-INA Tanggal/Tempat Kejadian-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%=DateUtil.getDateStr(pol.getDtClaimDate(), "d ^^ yyyy")%> / <%=JSPUtil.printX(pol.getStClaimEventLocation())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>9.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Loss Status-L}{L-INA Sebab Kerugian / Kecelakaan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%=JSPUtil.printX(claimLoss)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>10.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Estimated Amount-L}{L-INA Jumlah Kerugian-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%= pol.getStCurrencyCode()%> <%=JSPUtil.printX(pol.getDbClaimAmountEstimate())%></fo:block></fo:table-cell>
                        </fo:table-row>  

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>  

                        <fo:table-row>
                            <fo:table-cell ><fo:block>11.</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Remark-L}{L-INA Keterangan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%=JSPUtil.xmlEscape(pol.getStEndorseNotes())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>  

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <%
                                    final DTOList claimItems = pol.getClaimItems();
                                    //BigDecimal amt3 = new BigDecimal(0);
                                    BigDecimal tax = new BigDecimal(0);
                                    BigDecimal amtTotal = new BigDecimal(0);
                                    String amtTotalItem = null;
                                    String itemID = null;

                                    BigDecimal amtSubro = new BigDecimal(0);
                                    BigDecimal amtSubroBefore = new BigDecimal(0);
                                    BigDecimal amtOSSubro = new BigDecimal(0);
                                    BigDecimal amtDeduct = new BigDecimal(0);

                                    for (int i = 0; i < claimItems.size(); i++) {
                                        InsurancePolicyItemsView ci = (InsurancePolicyItemsView) claimItems.get(i);

                                        final boolean negative = ci.getInsItem().getARTrxLine().isNegative();

                                        tax = ci.getDbTaxAmount();

                                        itemID = ci.getStInsItemID();
                                        if (itemID.equalsIgnoreCase("61") || itemID.equalsIgnoreCase("62") || itemID.equalsIgnoreCase("63") || itemID.equalsIgnoreCase("46")) {
                                            continue;
                                        }

                                        if (itemID.equalsIgnoreCase("48")) {
                                            amtDeduct = pol.getTotalDeductible();
                                            amtSubroBefore = pol.getKlaimBefore();
                                            amtSubro = BDUtil.add(amtSubro, pol.getTotalSubrogasi());
                                        }

                                        //if (negative) {
                                        //    amt3 = BDUtil.sub(amt3,ci.getDbAmount());
                                        //    amt3 = BDUtil.sub(amt3,tax);
                                        //} else {
                                        //   amt3 = BDUtil.add(amt3,ci.getDbAmount());
                                        //}

                                        String amt = null;
                                        amt = JSPUtil.printX(ci.getDbAmount(), 2);
                                        amtTotalItem = JSPUtil.printX(pol.getDbClaimAmount(), 2);

                                        //if (negative) {
                                        //    amt = "(" + amt + ")";
                                        //    amtTotal = "(" + amtTotal + ")";
                                        //}

                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(ci.getStDescription2())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%= pol.getStClaimCurrency()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(amt)%></fo:block></fo:table-cell>
                        </fo:table-row>                        

                        <% if (ci.getStInsItemID().equalsIgnoreCase("70")) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Pajak Bengkel</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%= pol.getStClaimCurrency()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end">(<%=JSPUtil.printX(tax, 2)%>)</fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>
                        <%
                                    }
                        %>

                        <fo:table-row>		
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Total Item Klaim</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%= pol.getStClaimCurrency()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(amtTotalItem)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>		
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>

                        <% if (itemID.equalsIgnoreCase("48") || itemID.equalsIgnoreCase("73")) {

                                        //amtTotal = BDUtil.add(pol.getDbClaimAmount(), pol.getDbClaimAmountApproved());
                                        amtTotal = BDUtil.sub(pol.getDbClaimAmountEstimate(), amtSubroBefore);
                                        amtOSSubro = BDUtil.sub(pol.getDbClaimAmountEstimate(), amtDeduct);
                                        amtOSSubro = BDUtil.sub(amtOSSubro, amtSubro);
                        %>

                        <%--
                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>{L-ENG Claim Paid-L}{L-INA Klaim yang telah dibayar-L}</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>: <%= pol.getStClaimCurrency()%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmountApproved(), 2)%></fo:block></fo:table-cell>
                                                </fo:table-row>--%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Claim Paid-L}{L-INA Klaim yang telah dibayar-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%= pol.getStClaimCurrency()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(amtTotal, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Claim Due to-L}{L-INA Klaim seharusnya (menjadi)-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%= pol.getStClaimCurrency()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(amtOSSubro, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% } else {%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Total-L}{L-INA Klaim seharusnya-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%= pol.getStClaimCurrency()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmountEndorse(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Total-L}{L-INA Klaim yang telah dibayar-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%= pol.getStClaimCurrency()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmountApproved(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Total-L}{L-INA Jumlah Kekurangan / Kelebihan Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <%= pol.getStClaimCurrency()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(pol.getDbClaimAmountEndorse(), pol.getDbClaimAmountApproved()), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>


            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>


            <fo:block font-size="6pt"
                      font-family="sans-serif"
                      line-height="10pt"
                      space-after.optimum="10pt"
                      text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%> PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>]

            </fo:block>

            <fo:block font-size="<%=fontsize%>" space-after.optimum="15pt"></fo:block>

            <%
                        boolean isSubrogasi = false;
                        String namaKabag = null;
                        String jabatanKabag = null;
                        String divisiKabag = null;
                        for (int i = 0; i < claimItems.size(); i++) {
                            InsurancePolicyItemsView ci = (InsurancePolicyItemsView) claimItems.get(i);

                            if (ci.getInsItem().getStInsuranceItemID().equalsIgnoreCase("48") || ci.getInsItem().getStInsuranceItemID().equalsIgnoreCase("73")) {
                                isSubrogasi = true;
                            }

                            if (isSubrogasi) {
                                namaKabag = Parameter.readString("KABAG_SUBROGRASI");
                                jabatanKabag = "Kabag. Subrogasi";
                                divisiKabag = "SUBROGASI";
                            } else {
                                namaKabag = Parameter.readString("KABAG_KLAIM");
                                jabatanKabag = "Kabag. Klaim";
                                divisiKabag = "KLAIM";
                            }
                        }
            %>

            <%
                        if (SessionManager.getInstance().getSession().getStBranch() != null) {
            %>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><%= JSPUtil.printX(pol.getStCostCenterDesc())%>, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-display="2">
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(pol.getDbClaimAmount(), 2)%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("BRANCH_SIGN_" + SessionManager.getInstance().getSession().getStBranch())%></fo:inline></fo:block>
                                <fo:block text-align="center">Pimpinan</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <%
                                    } else {
            %>


            <% if (otorized.equalsIgnoreCase("kasie")) {%>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">MENYETUJUI,</fo:block>
                                <fo:block text-align="center">BAGIAN KLAIM,</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-display="2">
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(pol.getDbClaimAmount(), 2)%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KASIE_KLAIM2")%></fo:inline></fo:block>
                                <fo:block text-align="center">Kasie. Klaim</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

            <% if (otorized.equalsIgnoreCase("kabag")) {%>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">BAGIAN <%=JSPUtil.printX(divisiKabag)%>,</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-display="2">
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(pol.getDbClaimAmount(), 2)%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(namaKabag)%></fo:inline></fo:block>
                                <fo:block text-align="center"><%=JSPUtil.printX(jabatanKabag)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

            <% if (otorized.equalsIgnoreCase("kadiv")) {%>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">BAGIAN <%=JSPUtil.printX(divisiKabag)%>,</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-display="3">
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(pol.getDbClaimAmount(), 2)%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KADIV_KLAIM")%></fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. Klaim</fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(namaKabag)%></fo:inline></fo:block>
                                <fo:block text-align="center"><%=JSPUtil.printX(jabatanKabag)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

            <% if (otorized.equalsIgnoreCase("dirtek")) {%>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">BAGIAN <%=JSPUtil.printX(divisiKabag)%>,</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="60pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KADIV_KLAIM")%></fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. Klaim</fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("DIRTEK")%></fo:inline></fo:block>
                                <fo:block text-align="center">Direktur Teknik</fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(namaKabag)%></fo:inline></fo:block>
                                <fo:block text-align="center"><%=JSPUtil.printX(jabatanKabag)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(pol.getDbClaimAmount(), 2)%>" orientation="0">
                        <barcode:datamatrix>
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>
            </fo:block>

            <% }%>

            <% }%>

        </fo:flow>
    </fo:page-sequence>
</fo:root>
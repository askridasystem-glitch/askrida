<%@ page import="com.webfin.ar.forms.ReceiptForm,
         com.crux.web.controller.SessionManager,
         com.webfin.ar.model.ARInvoiceView,
         com.webfin.ar.model.ARInvoiceDetailView,
         com.webfin.ar.model.ARReceiptView,
         com.webfin.ar.model.ARReceiptLinesView,
         com.crux.util.*,
         java.math.BigDecimal,
         com.webfin.insurance.model.*,
         java.util.Date"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines the layout master -->
    <fo:layout-master-set>

        <fo:simple-page-master master-name="only"
                               page-height="16.2cm"
                               page-width="21.3cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>


    </fo:layout-master-set>

    <!-- starts actual layout -->
    <fo:page-sequence master-reference="only">
        <%

                    ReceiptForm form = (ReceiptForm) request.getAttribute("FORM");

                    DTOList line = (DTOList) request.getAttribute("RPT");
        %>

        <fo:flow flow-name="xsl-region-body">

            <%
                        BigDecimal premiGrossTotal = null;
                        BigDecimal diskonTotal = null;
                        BigDecimal pcostTotal = null;
                        BigDecimal stampdutyTotal = null;
                        BigDecimal tagihBruto = null;
                        BigDecimal tagihBruto2 = null;

                        String nopol = "";
                        String installment = "";
                        String custName = "";
                        String custName1 = "";
                        String custName2 = "";
                        String addressName = "";
                        String addressName1 = "";
                        String addressName2 = "";
                        String koda = "";
                        String ccy = "";
                        String polType = "";
                        String policyType = "";
                        String ttd = "";
                        String pejabat = "";
                        String jabatan = "";

                        for (int j = 0; j < line.size(); j++) {
                            ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);

                            nopol = view.getInvoice().getStAttrPolicyNo();

                            installment = view.getStInvoiceNo();

                            final DTOList arInvoiceDetail = view.getARInvoiceDetails();

                            for (int k = 0; k < arInvoiceDetail.size(); k++) {
                                ARInvoiceDetailView detil = (ARInvoiceDetailView) arInvoiceDetail.get(k);

                                if (detil.isPremiGross2()) {
                                    premiGrossTotal = BDUtil.add(premiGrossTotal, detil.getDbEnteredAmount());
                                }

                                if (detil.isDiscount2()) {
                                    diskonTotal = BDUtil.add(diskonTotal, detil.getDbEnteredAmount());
                                }

                                if (detil.isPolicyCost2()) {
                                    pcostTotal = BDUtil.add(pcostTotal, detil.getDbEnteredAmount());
                                }

                                if (detil.isStampDuty2()) {
                                    stampdutyTotal = BDUtil.add(stampdutyTotal, detil.getDbEnteredAmount());
                                }

                                tagihBruto = BDUtil.add(BDUtil.add(premiGrossTotal, pcostTotal), stampdutyTotal);
                                tagihBruto2 = BDUtil.sub(tagihBruto, diskonTotal);
                            }
                        }

                        for (int i = 0; i < line.size(); i++) {
                            ARReceiptLinesView rcl = (ARReceiptLinesView) line.get(i);

                            final DTOList arInvoice = rcl.getARInvoice();
                            for (int m = 0; m < arInvoice.size(); m++) {
                                ARInvoiceView invoc = (ARInvoiceView) arInvoice.get(m);

                                final DTOList insPolicyView = invoc.getInsPolicy();
                                for (int d = 0; d < insPolicyView.size(); d++) {
                                    InsurancePolicyView insPol = (InsurancePolicyView) insPolicyView.get(d);

                                    custName1 = insPol.getStCustomerName();

                                    addressName1 = insPol.getStCustomerAddress();

                                    custName2 = insPol.getStReference5();

                                    ccy = insPol.getStCurrencyCode();

                                    polType = insPol.getStPolicyTypeGroupID();

                                    policyType = insPol.getStPolicyTypeDesc2();

                                    koda = insPol.getCostCenter(insPol.getStCostCenterCode()).getStType();

                                    if (koda.equalsIgnoreCase("1")) {

                                        ttd = insPol.getUser(insPol.getUserIDSign()).getFile().getStFilePath();

                                        pejabat = insPol.getUserIDSignName();

                                        jabatan = insPol.getUser(insPol.getUserIDSign()).getStJobPosition();

                                    }

                                    final DTOList insPolObjView = insPol.getObjects();
                                    for (int e = 0; e < insPolObjView.size(); e++) {
                                        InsurancePolicyObjDefaultView insPolObj = (InsurancePolicyObjDefaultView) insPolObjView.get(e);

                                        addressName2 = insPolObj.getStReference3();

                                    }
                                }
                            }
                        }

                        if (polType.equalsIgnoreCase("7") || polType.equalsIgnoreCase("8")) {
                            custName = custName2;
                            addressName = addressName2;
                        } else {
                            custName = custName1;
                            addressName = addressName1;
                        }
            %>

            <fo:block-container height="0.5cm" width="4cm" top="3.3cm" left="16.3cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%= JSPUtil.printX(form.getStReceiptNo())%>
                </fo:block>
            </fo:block-container>

            <fo:block-container height="0.5cm" width="4cm" top="3.8cm" left="17cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    <%= JSPUtil.printX(policyType)%> - <%= JSPUtil.printX(nopol.substring(12, 16))%>
                </fo:block>
            </fo:block-container>

            <fo:block-container height="0.5cm" width="4cm" top="4.3cm" left="17cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">

                </fo:block>
            </fo:block-container>

            <fo:block-container height="1cm" width="14cm" top="6cm" left="5.5cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    <%=JSPUtil.printX(custName)%>
                </fo:block>
            </fo:block-container>

            <fo:block-container height="1cm" width="14cm" top="7cm" left="5.5cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    <%=JSPUtil.printX(addressName)%>
                </fo:block>
            </fo:block-container>

            <fo:block-container height="1cm" width="14cm" top="8cm" left="5.5cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt" font-weight="bold">
                    # <%=JSPUtil.printX(ccy)%>  <%=NumberSpell.readNumber(JSPUtil.printX(tagihBruto2, 2))%> #
                </fo:block>
            </fo:block-container>

            <%
                        String branch = "";

                        for (int a = 0; a < line.size(); a++) {
                            ARReceiptLinesView receipt = (ARReceiptLinesView) line.get(a);

                            final DTOList arInvoiceView = receipt.getARInvoice();
                            for (int s = 0; s < arInvoiceView.size(); s++) {
                                ARInvoiceView invoicView = (ARInvoiceView) arInvoiceView.get(s);

                                final DTOList insPolicyView = invoicView.getInsPolicy();
                                for (int d = 0; d < insPolicyView.size(); d++) {
                                    InsurancePolicyView insPol = (InsurancePolicyView) insPolicyView.get(d);

                                    branch = insPol.getStCostCenterDesc();

                                    final DTOList insPolicyInstal = insPol.getInstallment();
                                    for (int e = 0; e < insPolicyInstal.size(); e++) {
                                        InsurancePolicyInstallmentView insPolInstal = (InsurancePolicyInstallmentView) insPolicyInstal.get(e);
            %>

            <fo:block-container height="1cm" width="5cm" top="9cm" left="16.5cm" padding="1mm" position="absolute">
                <fo:block space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt" text-align="start">
                    <%=DateUtil.getDateStr(insPol.getDtPolicyDate(), "dd ^^ yyyy")%>
                </fo:block>
            </fo:block-container>

            <fo:block-container height="0.5cm" width="14cm" top="10.2cm" left="3.5cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    Pembayaran Polis No. <%=JSPUtil.printX(nopol.substring(0, 4) + "-" + nopol.substring(4, 8) + "-" + nopol.substring(8, 12) + "-" + nopol.substring(12, 16) + "-" + nopol.substring(16, 18))%>
                </fo:block>
            </fo:block-container>

            <% if (insPolicyInstal.size() > 1) {%>

            <fo:block-container height="0.5cm" width="14cm" top="10.8cm" left="3.5cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    Angsuran Ke
                    <% if (installment.length() > 21) {%>
                    <%=JSPUtil.printX(installment.substring(31, 32))%>
                    <% } else {%>
                    <%=JSPUtil.printX(installment.substring(20, 21))%>
                    <% }%>
                </fo:block>
            </fo:block-container>
            <% }%>

            <%
                                    }
                                }
                            }
                        }
            %>


            <% if (Tools.compare(pcostTotal, BDUtil.zero) > 0 && Tools.compare(stampdutyTotal, BDUtil.zero) > 0) {%>

            <fo:block-container height="0.5cm" width="11cm" top="11.3cm" left="3.5cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="2.5cm"/>
                    <fo:table-body font-weight="normal" font-size="10pt">

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block >Premi Bruto</fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><%=JSPUtil.printX(ccy)%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="end"><%=JSPUtil.printX(premiGrossTotal, 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <% if (diskonTotal != null) {%>

            <fo:block-container height="0.5cm" width="11cm" top="11.8cm" left="3.5cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="2.5cm"/>
                    <fo:table-body font-weight="normal" font-size="10pt">

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block >Diskon</fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><%=JSPUtil.printX(ccy)%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="end"><%=JSPUtil.printX(diskonTotal, 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <fo:block-container height="0.5cm" width="11cm" top="12.3cm" left="3.5cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="2.5cm"/>
                    <fo:table-body font-weight="normal" font-size="10pt">

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block >Biaya Polis</fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><%=JSPUtil.printX(ccy)%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="end"><%=JSPUtil.printX(pcostTotal, 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <fo:block-container height="0.5cm" width="11cm" top="12.8cm" left="3.5cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="2.5cm"/>
                    <fo:table-body font-weight="normal" font-size="10pt">

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block >Biaya Materai</fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><%=JSPUtil.printX(ccy)%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="end"><%=JSPUtil.printX(stampdutyTotal, 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <% } else {%>

            <fo:block-container height="0.5cm" width="11cm" top="12.3cm" left="3.5cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="2.5cm"/>
                    <fo:table-body font-weight="normal" font-size="10pt">

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block >Biaya Polis</fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><%=JSPUtil.printX(ccy)%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="end"><%=JSPUtil.printX(pcostTotal, 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <fo:block-container height="0.5cm" width="11cm" top="12.8cm" left="3.5cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="2.5cm"/>
                    <fo:table-body font-weight="normal" font-size="10pt">

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block >Biaya Materai</fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block ><%=JSPUtil.printX(ccy)%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="end"><%=JSPUtil.printX(stampdutyTotal, 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <% }%>
            <% }%>

            <fo:block-container height="0.5cm" width="3cm" top="12cm" left="15.3cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    <%= JSPUtil.printX(branch)%>,
                </fo:block>
            </fo:block-container>

            <fo:block-container height="0.5cm" width="2cm" top="12.7cm" left="15.3cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    #######
                </fo:block>
            </fo:block-container>

            <fo:block-container height="0.5cm" width="3cm" top="12.5cm" left="17.5cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    <% if (form.getDtReceipt() != null) {%>
                    <%=DateUtil.getDateStr(form.getDtReceipt(), "dd ^^")%>
                    <% }%>
                </fo:block>
            </fo:block-container>

            <fo:block-container height="0.5cm" width="0.5cm" top="12.5cm" left="20.5cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    <% if (form.getDtReceipt() != null) {%>
                    <%=DateUtil.getYear2Digit(form.getDtReceipt())%>
                    <% }%>
                </fo:block>
            </fo:block-container>

            <fo:block-container height="0.8cm" width="5cm" top="13.2cm" left="1.5cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt" font-weight="bold">
                    <%=JSPUtil.printX(ccy)%> <%=JSPUtil.printX(tagihBruto2, 2)%>
                </fo:block>
            </fo:block-container>

            <% if (koda.equalsIgnoreCase("1")) {%>
            <fo:block-container height="2.5cm" width="4cm" top="13.5cm" left="16cm" padding="1mm" position="absolute">
                <fo:block text-align="center" line-height="14pt">
                    <fo:external-graphic
                        content-height="scale-to-fit"
                        height="0.5in" content-width="0.5in"
                        scaling="non-uniform" src="url(<%=ttd%>)"  />
                </fo:block>
                <fo:block text-align="center" line-height="14pt" font-size="8pt">
                    <fo:inline text-decoration="underline"><%=pejabat%></fo:inline>
                </fo:block>
                <fo:block text-align="center" line-height="14pt" font-size="8pt">
                    <%=JSPUtil.printX(jabatan).toUpperCase()%>
                </fo:block>
            </fo:block-container>
            <% }%>

        </fo:flow>
    </fo:page-sequence>
</fo:root>
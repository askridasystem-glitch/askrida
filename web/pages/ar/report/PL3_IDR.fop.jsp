<%@ page import="com.webfin.ar.forms.ReceiptForm,
         com.crux.web.controller.SessionManager,
         java.util.ArrayList,
         com.crux.util.fop.FOPUtil,
         com.crux.util.SQLAssembler,
         com.webfin.ar.model.ARInvoiceView,
         com.webfin.ar.model.ARInvoiceDetailView,
         com.webfin.ar.model.ARReceiptView,
         com.webfin.ar.model.ARReceiptLinesView,
         com.crux.util.DTOList,
         com.crux.util.*,
         com.crux.util.BDUtil,
         com.crux.util.JSPUtil,
         java.math.BigDecimal,
         com.crux.lang.LanguageManager,
         java.util.Date"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="21.6cm"
                               page-width="38cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
            <fo:region-before extent="0cm"/> 
            <fo:region-after extent="0cm"/> 
        </fo:simple-page-master>
    </fo:layout-master-set>

    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">

        <%

                    ReceiptForm form = (ReceiptForm) request.getAttribute("FORM");

                    String fontsize = form.getStFontSize();

                    DTOList line = (DTOList) request.getAttribute("RPT");

                    ArrayList colW = new ArrayList();

                    colW.add(new Integer(25));
                    colW.add(new Integer(75));
                    colW.add(new Integer(200));
                    colW.add(new Integer(170));
                    colW.add(new Integer(100));
                    colW.add(new Integer(100));
                    colW.add(new Integer(170));
                    colW.add(new Integer(170));
                    colW.add(new Integer(100));
                    colW.add(new Integer(170));
                    colW.add(new Integer(100));
                    colW.add(new Integer(170));
                    colW.add(new Integer(170));
                    colW.add(new Integer(170));
                    colW.add(new Integer(170));
                    colW.add(new Integer(200));
                    colW.add(new Integer(200));
                    colW.add(new Integer(100));

                    //colW=FOPUtil.computeColumnWidth(colW,16,2," cm");

        %>


        <fo:flow flow-name="xsl-region-body">

            <%
                        String bw = "0.2pt";
            %>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">MONITORING PEMBAYARAN (PL 3)</fo:block>
            <fo:block space-after.optimum="14pt"/>

            <%if (form.getStReceiptNo() != null) {%> 
            <fo:block font-family="Helvetica" text-align="start">
                No. Rekap Monitoring : <%=JSPUtil.printX(form.getStReceiptNo())%> - <%=JSPUtil.printX(form.getStName())%>
            </fo:block>
            <%}%>

            <fo:block font-family="Helvetica" font-size="<%=fontsize%>" display-align="center" border-width="0.1pt">
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>" >
                    <fo:table-header>
                        <fo:table-row> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">No</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Tanggal</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">No Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Premi Bruto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Polis + Materai</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Diskon</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Tagihan Bruto</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Komisi</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Pajak Komisi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Broker Fee</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Pajak Broker</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Handling Fee</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Fee Base</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">PPN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Tagihan Netto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">No Bukti</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Pembayaran</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Tanggal Bayar</fo:block></fo:table-cell>
                        </fo:table-row>
                    </fo:table-header>
                    <%=FOPUtil.printColumnWidth(colW, 37, 2, "cm")%>
                    <fo:table-body> 
                        <%
                                    //final DTOList commissions = currentLine.getDetails();
                                    BigDecimal premiGrossJumlah = null;
                                    BigDecimal commissionJumlah = null;
                                    BigDecimal commissionFeeBaseJumlah = null;
                                    BigDecimal brokerageJumlah = null;
                                    BigDecimal hfeeJumlah = null;
                                    BigDecimal PPNJumlah = null;
                                    BigDecimal pcostJumlah = null;
                                    BigDecimal stampdutyJumlah = null;
                                    BigDecimal taxCommJumlah = null;
                                    BigDecimal tagihBrutoJumlah = null;
                                    BigDecimal tagihNettoJumlah = null;
                                    BigDecimal diskonJumlah = null;
                                    BigDecimal taxBrokJumlah = null;
                                    BigDecimal taxFeeJumlah = null;
                                    BigDecimal commissionPlusTaxJumlah = null;
                                    BigDecimal brokeragePlusTaxJumlah = null;
                                    BigDecimal handlingPlusTaxJumlah = null;
                                    BigDecimal konversi = null;

                                    for (int j = 0; j < line.size(); j++) {
                                        ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);

                                        BigDecimal premiGrossTotal = null;
                                        BigDecimal commissionTotal = null;
                                        BigDecimal commissionFeeBaseTotal = null;
                                        BigDecimal brokerageTotal = null;
                                        BigDecimal hfeeTotal = null;
                                        BigDecimal PPNTotal = null;
                                        BigDecimal pcostTotal = null;
                                        BigDecimal stampdutyTotal = null;
                                        BigDecimal taxCommTotal = null;
                                        BigDecimal tagihBruto = null;
                                        BigDecimal tagihNetto = null;
                                        BigDecimal diskonTotal = null;
                                        BigDecimal taxBrokTotal = null;
                                        BigDecimal taxFeeTotal = null;
                                        BigDecimal commissionPlusTaxTotal = null;
                                        BigDecimal brokeragePlusTaxTotal = null;
                                        BigDecimal handlingPlusTaxTotal = null;
                                        BigDecimal CommBrokHfeeTotal = null;
                                        BigDecimal TaxCommBrokHfeeTotal = null;

                                        final DTOList arInvoiceDetail = view.getARInvoiceDetails();
                                        for (int k = 0; k < arInvoiceDetail.size(); k++) {
                                            ARInvoiceDetailView detil = (ARInvoiceDetailView) arInvoiceDetail.get(k);

                                            BigDecimal amount = detil.getDbAmount();

                                            if (detil.isPremiGross2()) {
                                                premiGrossTotal = BDUtil.add(premiGrossTotal, amount);
                                            }

                                            if (detil.isCommission2() && !detil.isFeeBase3() && !detil.isPPNFeebase()) {
                                                commissionTotal = BDUtil.add(commissionTotal, amount);
                                            }

                                            if (detil.isCommission2() && detil.isFeeBase3()) {
                                                commissionFeeBaseTotal = BDUtil.add(commissionFeeBaseTotal, amount);
                                            }

                                            if (detil.isBrokerage2() && !detil.isPPN()) {
                                                brokerageTotal = BDUtil.add(brokerageTotal, amount);
                                            }

                                            if (detil.isHandlingFee2()) {
                                                hfeeTotal = BDUtil.add(hfeeTotal, amount);
                                            }

                                            if (detil.isPolicyCost2()) {
                                                pcostTotal = BDUtil.add(pcostTotal, amount);
                                            }

                                            if (detil.isStampDuty2()) {
                                                stampdutyTotal = BDUtil.add(stampdutyTotal, amount);
                                            }

                                            if (detil.isTaxComm()) {
                                                taxCommTotal = BDUtil.add(taxCommTotal, amount);
                                            }

                                            if (detil.isDiscount2()) {
                                                diskonTotal = BDUtil.add(diskonTotal, amount);
                                            }

                                            if (detil.isTaxBrok()) {
                                                taxBrokTotal = BDUtil.add(taxBrokTotal, amount);
                                            }

                                            if (detil.isTaxHFee()) {
                                                taxFeeTotal = BDUtil.add(taxFeeTotal, amount);
                                            }

                                            if (detil.isPPN() || detil.isPPNFeebase()) {
                                                PPNTotal = BDUtil.add(PPNTotal, amount);
                                            }

                                            commissionPlusTaxTotal = BDUtil.add(commissionTotal, taxCommTotal);
                                            brokeragePlusTaxTotal = BDUtil.add(brokerageTotal, taxBrokTotal);
                                            handlingPlusTaxTotal = BDUtil.add(hfeeTotal, taxFeeTotal);

                                            CommBrokHfeeTotal = BDUtil.add(commissionPlusTaxTotal, brokeragePlusTaxTotal);
                                            CommBrokHfeeTotal = BDUtil.add(CommBrokHfeeTotal, handlingPlusTaxTotal);
                                            CommBrokHfeeTotal = BDUtil.add(CommBrokHfeeTotal, PPNTotal);

                                            TaxCommBrokHfeeTotal = BDUtil.add(taxCommTotal, taxBrokTotal);
                                            TaxCommBrokHfeeTotal = BDUtil.add(TaxCommBrokHfeeTotal, taxFeeTotal);

                                            tagihBruto = BDUtil.add(BDUtil.add(premiGrossTotal, pcostTotal), stampdutyTotal);

                                            tagihBruto = BDUtil.sub(tagihBruto, diskonTotal);

                                            tagihNetto = BDUtil.sub(tagihBruto, BDUtil.sub(CommBrokHfeeTotal, TaxCommBrokHfeeTotal));
                                            tagihNetto = BDUtil.sub(tagihNetto, commissionFeeBaseTotal);
                                        }

                                        premiGrossJumlah = BDUtil.add(premiGrossJumlah, premiGrossTotal);
                                        commissionJumlah = BDUtil.add(commissionJumlah, commissionTotal);
                                        commissionFeeBaseJumlah = BDUtil.add(commissionFeeBaseJumlah, commissionFeeBaseTotal);
                                        brokerageJumlah = BDUtil.add(brokerageJumlah, brokerageTotal);
                                        hfeeJumlah = BDUtil.add(hfeeJumlah, hfeeTotal);
                                        PPNJumlah = BDUtil.add(PPNJumlah, PPNTotal);
                                        pcostJumlah = BDUtil.add(pcostJumlah, pcostTotal);
                                        stampdutyJumlah = BDUtil.add(stampdutyJumlah, stampdutyTotal);
                                        taxCommJumlah = BDUtil.add(taxCommJumlah, taxCommTotal);
                                        tagihBrutoJumlah = BDUtil.add(tagihBrutoJumlah, tagihBruto);
                                        tagihNettoJumlah = BDUtil.add(tagihNettoJumlah, tagihNetto);
                                        diskonJumlah = BDUtil.add(diskonJumlah, diskonTotal);
                                        taxBrokJumlah = BDUtil.add(taxBrokJumlah, taxBrokTotal);
                                        commissionPlusTaxJumlah = BDUtil.add(commissionPlusTaxJumlah, commissionPlusTaxTotal);
                                        brokeragePlusTaxJumlah = BDUtil.add(brokeragePlusTaxJumlah, brokeragePlusTaxTotal);
                                        handlingPlusTaxJumlah = BDUtil.add(handlingPlusTaxJumlah, handlingPlusTaxTotal);
                        %>

                        <fo:table-row >
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=j + 1%>.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=JSPUtil.print(view.getInvoice().getDtMutationDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="7pt"><%=JSPUtil.print(view.getInvoice().getStAttrPolicyNo().substring(0, 4) + "-" + view.getInvoice().getStAttrPolicyNo().substring(4, 8) + "-" + view.getInvoice().getStAttrPolicyNo().substring(8, 12) + "-" + view.getInvoice().getStAttrPolicyNo().substring(12, 16) + "-" + view.getInvoice().getStAttrPolicyNo().substring(16, 18))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(premiGrossTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(pcostTotal, stampdutyTotal), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(diskonTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(tagihBruto, 2)%></fo:block></fo:table-cell><%--
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(commissionPlusTaxTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(taxCommTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(brokeragePlusTaxTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(taxBrokTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(handlingPlusTaxTotal, 2)%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(commissionTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(taxCommTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(brokerageTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(taxBrokTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(hfeeTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(commissionFeeBaseTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(PPNTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(tagihNetto, 2)%></fo:block></fo:table-cell>
                            <%
                                                                    if (view.getHistoryTrx() != null) {
                            %>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt">
                                <%
                                                                                                        String param = view.getHistoryTrx();
                                                                                                        String[] param2 = param.split("[\\,]");
                                %>

                                <%
                                                                                                        for (int k = 0; k < param2.length; k++) {
                                %>

                                <%
                                                                                                                                            String paramCh = param2[k];
                                                                                                                                            String[] paramCh2 = paramCh.split("[\\_]");
                                %>
                                <fo:block text-align="start"><%=JSPUtil.printX(paramCh2[0])%></fo:block>
                                <% }%>
                            </fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt">
                                <% for (int k = 0; k < param2.length; k++) {%>
                                <%
                                     String paramCh = param2[k];
                                     String[] paramCh2 = paramCh.split("[\\_]");
                                %>
                                <fo:block text-align="start"><%=JSPUtil.printX(paramCh2[1])%></fo:block>
                                <% }%>
                            </fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt">
                                <% for (int k = 0; k < param2.length; k++) {%>
                                <%
                                     String paramCh = param2[k];
                                     String[] paramCh2 = paramCh.split("[\\_]");
                                %>
                                <fo:block text-align="start"><%=JSPUtil.printX(paramCh2[2])%></fo:block>
                                <% }%>
                            </fo:table-cell>
                            <%--<fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=JSPUtil.printX(view.getInvoice().getStReceiptNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=JSPUtil.printX(view.getInvoice().getDtReceipt())%></fo:block></fo:table-cell>--%>
                            <%
                                                                    }
                            %>
                        </fo:table-row >

                        <%
                                    }
                        %> 
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="18"> 
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row> 

                        <fo:table-row >
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">JUMLAH</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(premiGrossJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(BDUtil.add(pcostJumlah, stampdutyJumlah), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(diskonJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(tagihBrutoJumlah, 2)%></fo:block></fo:table-cell><%--
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(commissionPlusTaxJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(taxCommJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(brokeragePlusTaxJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(taxBrokJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(handlingPlusTaxJumlah, 2)%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(commissionJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(taxCommJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(brokerageJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(taxBrokJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(hfeeJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(commissionFeeBaseJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(PPNJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(tagihNettoJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"></fo:block></fo:table-cell>
                        </fo:table-row >

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
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(premiGrossJumlah, 0)%>" orientation="0">
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


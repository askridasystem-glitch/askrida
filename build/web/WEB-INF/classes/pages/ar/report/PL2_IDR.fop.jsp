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
                               page-height="21cm"
                               page-width="38cm"
                               margin-top="1cm"
                               margin-bottom="2cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="1cm" margin-bottom="1.5cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>

    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">
        <%

                    ReceiptForm form = (ReceiptForm) request.getAttribute("FORM");

                    String fontsize = form.getStFontSize();

                    DTOList line = (DTOList) request.getAttribute("RPT");

                    ArrayList colW = new ArrayList();

                    colW.add(new Integer(30));
                    colW.add(new Integer(90));
                    colW.add(new Integer(200));/*
                    colW.add(new Integer(170));
                    colW.add(new Integer(100));
                    colW.add(new Integer(100));*/
                    colW.add(new Integer(170));
                    colW.add(new Integer(170));
                    colW.add(new Integer(100));
                    colW.add(new Integer(170));
                    colW.add(new Integer(100));
                    colW.add(new Integer(170));
                    colW.add(new Integer(100));
                    colW.add(new Integer(170));
                    colW.add(new Integer(100));
                    colW.add(new Integer(170));/*
                    colW.add(new Integer(100));
                    colW.add(new Integer(100));*/
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

            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">MONITORING PEMBAYARAN (PL 2)</fo:block>
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
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">No Polis</fo:block></fo:table-cell><%--
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Premi Bruto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Biaya Polis</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Biaya Materai</fo:block></fo:table-cell> --%>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Tagihan Bruto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Komisi A</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Pajak A</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Komisi B</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Pajak B</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Komisi C</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Pajak C</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Komisi D</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Pajak D</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Diskon + PPN + FeeBase</fo:block></fo:table-cell><%--
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">PPN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Fee Base</fo:block></fo:table-cell> --%>
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
                                    BigDecimal diskonJumlah = null;
                                    BigDecimal feeBaseJumlah = null;
                                    BigDecimal bfeeJumlah = null;
                                    BigDecimal hfeeJumlah = null;
                                    BigDecimal ppnJumlah = null;
                                    BigDecimal biapolJumlah = null;
                                    BigDecimal biamatJumlah = null;
                                    BigDecimal bfeeTaxJumlah = null;
                                    BigDecimal hfeeTaxJumlah = null;
                                    BigDecimal tagihBrutoJumlah = null;
                                    BigDecimal tagihNettoJumlah = null;

                                    BigDecimal[] komisiJumlah = new BigDecimal[4];
                                    BigDecimal[] komisiTaxJumlah = new BigDecimal[4];
                                    BigDecimal[] komisiPlusTaxJumlah = new BigDecimal[4];
                                    BigDecimal bfeePlusTaxJumlah = null;
                                    BigDecimal hfeePlusTaxJumlah = null;

                                    for (int j = 0; j < line.size(); j++) {
                                        ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);

                                        BigDecimal premiGrossTotal = null;
                                        BigDecimal diskonTotal = null;
                                        BigDecimal feeBaseTotal = null;
                                        BigDecimal bfeeTotal = null;
                                        BigDecimal hfeeTotal = null;
                                        BigDecimal ppnTotal = null;
                                        BigDecimal biapolTotal = null;
                                        BigDecimal biamatTotal = null;
                                        BigDecimal bfeeTaxTotal = null;
                                        BigDecimal hfeeTaxTotal = null;
                                        BigDecimal tagihBruto = null;
                                        BigDecimal tagihNetto = null;

                                        BigDecimal[] komisiTotal = new BigDecimal[4];
                                        BigDecimal[] komisiTaxTotal = new BigDecimal[4];

                                        //String plccc = plNobuk.replace("h", "j");

                                        final DTOList arInvoiceDetail = view.getARInvoiceDetails();
                                        for (int k = 0; k < arInvoiceDetail.size(); k++) {
                                            ARInvoiceDetailView detil = (ARInvoiceDetailView) arInvoiceDetail.get(k);

                                            BigDecimal amount = detil.getDbAmount();

                                            //premi
                                            if (detil.isPremiGross2()) {
                                                premiGrossTotal = BDUtil.add(premiGrossTotal, amount);
                                            }

                                            //diskon
                                            if (detil.isDiscount2()) {
                                                diskonTotal = BDUtil.add(diskonTotal, amount);
                                            }

                                            //feebase
                                            if (detil.isCommission2() && detil.isFeeBase3()) {
                                                feeBaseTotal = BDUtil.add(feeBaseTotal, amount);
                                            }

                                            //bfee
                                            if (detil.isBrokerage2() && !detil.isPPN()) {
                                                bfeeTotal = BDUtil.add(bfeeTotal, amount);
                                            }

                                            //bfee tax
                                            if (detil.isTaxBrok()) {
                                                bfeeTaxTotal = BDUtil.add(bfeeTaxTotal, amount);
                                            }

                                            //hfee
                                            if (detil.isHandlingFee2()) {
                                                hfeeTotal = BDUtil.add(hfeeTotal, amount);
                                            }

                                            //hfee tax
                                            if (detil.isTaxHFee()) {
                                                hfeeTaxTotal = BDUtil.add(hfeeTaxTotal, amount);
                                            }

                                            //biapol
                                            if (detil.isPolicyCost2()) {
                                                biapolTotal = BDUtil.add(biapolTotal, amount);
                                            }

                                            //biamat
                                            if (detil.isStampDuty2()) {
                                                biamatTotal = BDUtil.add(biamatTotal, amount);
                                            }

                                            //ppn
                                            if (detil.isPPN() || detil.isPPNFeebase() || detil.isPPNFeebase()) {
                                                ppnTotal = BDUtil.add(ppnTotal, amount);
                                            }

                                            if (detil.isCommission2() && !detil.isFeeBase3() && !detil.isPPNFeebase()) {

                                                //komisi 1
                                                if (komisiTotal[0] == null) {
                                                    komisiTotal[0] = BDUtil.add(komisiTotal[0], amount);
                                                    continue;

                                                }
                                                //komisi 2
                                                if (komisiTotal[1] == null) {
                                                    komisiTotal[1] = BDUtil.add(komisiTotal[1], amount);
                                                    continue;
                                                }
                                                //komisi 3
                                                if (komisiTotal[2] == null) {
                                                    komisiTotal[2] = BDUtil.add(komisiTotal[2], amount);
                                                    continue;
                                                }
                                                //komisi 4
                                                if (komisiTotal[3] == null) {
                                                    komisiTotal[3] = BDUtil.add(komisiTotal[3], amount);
                                                    continue;
                                                }
                                            }

                                            if (detil.isTaxComm()) {

                                                //komisi 1
                                                if (komisiTaxTotal[0] == null) {
                                                    komisiTaxTotal[0] = BDUtil.add(komisiTaxTotal[0], amount);
                                                    continue;
                                                }
                                                //komisi 2
                                                if (komisiTaxTotal[1] == null) {
                                                    komisiTaxTotal[1] = BDUtil.add(komisiTaxTotal[1], amount);
                                                    continue;
                                                }
                                                //komisi 3
                                                if (komisiTaxTotal[2] == null) {
                                                    komisiTaxTotal[2] = BDUtil.add(komisiTaxTotal[2], amount);
                                                    continue;
                                                }
                                                //komisi 4
                                                if (komisiTaxTotal[3] == null) {
                                                    komisiTaxTotal[3] = BDUtil.add(komisiTaxTotal[3], amount);
                                                    continue;
                                                }
                                            }

                                            tagihBruto = BDUtil.add(premiGrossTotal, biamatTotal);
                                            tagihBruto = BDUtil.add(tagihBruto, biapolTotal);

                                            tagihNetto = BDUtil.sub(tagihBruto, diskonTotal);
                                            tagihNetto = BDUtil.sub(tagihNetto, ppnTotal);
                                            tagihNetto = BDUtil.sub(tagihNetto, feeBaseTotal);
                                            tagihNetto = BDUtil.sub(tagihNetto, bfeeTotal);
                                            tagihNetto = BDUtil.sub(tagihNetto, hfeeTotal);
                                            tagihNetto = BDUtil.sub(tagihNetto, komisiTotal[0]);
                                            tagihNetto = BDUtil.sub(tagihNetto, komisiTotal[1]);
                                            tagihNetto = BDUtil.sub(tagihNetto, komisiTotal[2]);
                                            tagihNetto = BDUtil.sub(tagihNetto, komisiTotal[3]);
                                            /*tagihNetto = BDUtil.sub(tagihNetto, BDUtil.add(bfeeTotal, bfeeTaxTotal));
                                            tagihNetto = BDUtil.sub(tagihNetto, BDUtil.add(hfeeTotal, hfeeTaxTotal));
                                            tagihNetto = BDUtil.sub(tagihNetto, BDUtil.add(komisiTotal[0], komisiTaxTotal[0]));
                                            tagihNetto = BDUtil.sub(tagihNetto, BDUtil.add(komisiTotal[1], komisiTaxTotal[1]));
                                            tagihNetto = BDUtil.sub(tagihNetto, BDUtil.add(komisiTotal[2], komisiTaxTotal[2]));
                                            tagihNetto = BDUtil.sub(tagihNetto, BDUtil.add(komisiTotal[3], komisiTaxTotal[3]));*/

                                            /*
                                            BigDecimal amount = detil.getDbAmount();

                                            if (detil.isTaxComm()) {
                                            taxCode = detil.getTrxLine().getStTaxCode();
                                            }
                                            if (detil.isTaxBrok()) {
                                            taxCode2 = detil.getTrxLine().getStTaxCode();
                                            }
                                            if (detil.isTaxHFee()) {
                                            taxCode3 = detil.getTrxLine().getStTaxCode();
                                            }

                                            if (detil.isPremiGross2()) {
                                            premiGrossTotal = BDUtil.add(premiGrossTotal, amount);
                                            }

                                            if (detil.isCommission2() && !detil.isFeeBase3()) {
                                            commissionTotal = BDUtil.add(commissionTotal, amount);
                                            }

                                            if (detil.isCommission2() && detil.isFeeBase3()) {
                                            feeBaseTotal = BDUtil.add(feeBaseTotal, amount);
                                            }

                                            if (detil.isBrokerage2() && !detil.isPPN()) {
                                            bfeeTotal = BDUtil.add(bfeeTotal, amount);
                                            }

                                            if (detil.isHandlingFee2()) {
                                            hfeeTotal = BDUtil.add(hfeeTotal, amount);
                                            }

                                            if (detil.isPolicyCost2()) {
                                            biapolTotal = BDUtil.add(biapolTotal, amount);
                                            }

                                            if (detil.isStampDuty2()) {
                                            biamatTotal = BDUtil.add(biamatTotal, amount);
                                            }

                                            if (detil.isTaxComm()) {
                                            taxTotal = BDUtil.add(taxTotal, amount);
                                            }


                                            if (detil.isCommission2() || detil.isBrokerage2() || detil.isHandlingFee2() && !detil.isFeeBase3()) {
                                            if (komisi1 == null) {
                                            if (detil.getStTaxCode() != null) {
                                            if (detil.getStTaxCode().equalsIgnoreCase("2")
                                            || detil.getStTaxCode().equalsIgnoreCase("5")
                                            || detil.getStTaxCode().equalsIgnoreCase("6")) {
                                            komisi1 = amount;
                                            continue;
                                            }
                                            } else {
                                            komisi1 = amount;
                                            continue;
                                            }
                                            }
                                            }

                                            if (detil.isCommission2() || detil.isBrokerage2() || detil.isHandlingFee2() && !detil.isFeeBase3()) {
                                            if (komisi2 == null) {
                                            if (detil.getStTaxCode() != null) {
                                            if (detil.getStTaxCode().equalsIgnoreCase("1")
                                            || detil.getStTaxCode().equalsIgnoreCase("4")
                                            || detil.getStTaxCode().equalsIgnoreCase("7")) {
                                            komisi2 = amount;
                                            continue;
                                            }
                                            } else {
                                            komisi2 = amount;
                                            continue;
                                            }
                                            }
                                            }

                                            if (detil.isCommission2() || detil.isBrokerage2() || detil.isHandlingFee2() && !detil.isFeeBase3()) {
                                            if (komisi3 == null) {
                                            if (detil.getStTaxCode() != null) {
                                            if (detil.getStTaxCode().equalsIgnoreCase("1")
                                            || detil.getStTaxCode().equalsIgnoreCase("4")
                                            || detil.getStTaxCode().equalsIgnoreCase("7")) {
                                            komisi3 = amount;
                                            continue;
                                            }
                                            } else {
                                            komisi3 = amount;
                                            continue;
                                            }
                                            }
                                            }

                                            if (detil.isCommission2() || detil.isBrokerage2() || detil.isHandlingFee2() && !detil.isFeeBase3()) {
                                            if (komisi4 == null) {
                                            if (detil.getStTaxCode() != null) {
                                            if (detil.getStTaxCode().equalsIgnoreCase("1")
                                            || detil.getStTaxCode().equalsIgnoreCase("4")
                                            || detil.getStTaxCode().equalsIgnoreCase("7")) {
                                            komisi4 = amount;
                                            continue;
                                            }
                                            } else {
                                            komisi4 = amount;
                                            continue;
                                            }
                                            }
                                            }

                                            if (detil.isTaxComm() || detil.isTaxBrok() || detil.isTaxHFee()) {
                                            if (tax1 == null) {
                                            if (detil.getTrxLine().getStTaxCode().equalsIgnoreCase("2")
                                            || detil.getTrxLine().getStTaxCode().equalsIgnoreCase("5")
                                            || detil.getTrxLine().getStTaxCode().equalsIgnoreCase("6")) {
                                            tax1 = amount;
                                            continue;
                                            }
                                            }
                                            }

                                            if (detil.isTaxComm() || detil.isTaxBrok() || detil.isTaxHFee()) {
                                            if (tax2 == null) {
                                            if (detil.getTrxLine().getStTaxCode().equalsIgnoreCase("1")
                                            || detil.getTrxLine().getStTaxCode().equalsIgnoreCase("4")
                                            || detil.getTrxLine().getStTaxCode().equalsIgnoreCase("7")) {
                                            tax2 = amount;
                                            continue;
                                            }
                                            }
                                            }

                                            if (detil.isTaxComm() || detil.isTaxBrok() || detil.isTaxHFee()) {
                                            if (tax3 == null) {
                                            if (detil.getTrxLine().getStTaxCode().equalsIgnoreCase("1")
                                            || detil.getTrxLine().getStTaxCode().equalsIgnoreCase("4")
                                            || detil.getTrxLine().getStTaxCode().equalsIgnoreCase("7")) {
                                            tax3 = amount;
                                            continue;
                                            }
                                            }
                                            }

                                            if (detil.isTaxComm() || detil.isTaxBrok() || detil.isTaxHFee()) {
                                            if (tax4 == null) {
                                            if (detil.getTrxLine().getStTaxCode().equalsIgnoreCase("1")
                                            || detil.getTrxLine().getStTaxCode().equalsIgnoreCase("4")
                                            || detil.getTrxLine().getStTaxCode().equalsIgnoreCase("7")) {
                                            tax4 = amount;
                                            continue;
                                            }
                                            }
                                            }

                                            komisiPlusTax1 = BDUtil.add(komisi1, tax1);
                                            komisiPlusTax2 = BDUtil.add(komisi2, tax2);
                                            komisiPlusTax3 = BDUtil.add(komisi3, tax3);
                                            komisiPlusTax4 = BDUtil.add(komisi4, tax4);
                                            totalKomisiPlusTax = BDUtil.add(totalKomisiPlusTax, komisiPlusTax1);
                                            totalKomisiPlusTax = BDUtil.add(totalKomisiPlusTax, komisiPlusTax2);
                                            totalKomisiPlusTax = BDUtil.add(totalKomisiPlusTax, komisiPlusTax3);
                                            totalKomisiPlusTax = BDUtil.add(totalKomisiPlusTax, komisiPlusTax4);

                                            tagihBruto = BDUtil.add(BDUtil.add(premiGrossTotal, biapolTotal), biamatTotal);

                                            tagihBruto2 = BDUtil.sub(tagihBruto, commissionTotal);
                                            tagihBruto2 = BDUtil.sub(tagihBruto2, feeBaseTotal);

                                            tagihNetto = BDUtil.sub(tagihBruto, totalKomisiPlusTax);
                                             */
                                        }

                                        premiGrossJumlah = BDUtil.add(premiGrossJumlah, premiGrossTotal);
                                        feeBaseJumlah = BDUtil.add(feeBaseJumlah, feeBaseTotal);
                                        bfeeJumlah = BDUtil.add(bfeeJumlah, bfeeTotal);
                                        hfeeJumlah = BDUtil.add(hfeeJumlah, hfeeTotal);
                                        ppnJumlah = BDUtil.add(ppnJumlah, ppnTotal);
                                        diskonJumlah = BDUtil.add(diskonJumlah, diskonTotal);
                                        biapolJumlah = BDUtil.add(biapolJumlah, biapolTotal);
                                        biamatJumlah = BDUtil.add(biamatJumlah, biamatTotal);
                                        bfeeTaxJumlah = BDUtil.add(bfeeTaxJumlah, bfeeTaxTotal);
                                        hfeeTaxJumlah = BDUtil.add(hfeeTaxJumlah, hfeeTaxTotal);
                                        tagihBrutoJumlah = BDUtil.add(tagihBrutoJumlah, tagihBruto);
                                        tagihNettoJumlah = BDUtil.add(tagihNettoJumlah, tagihNetto);

                                        komisiJumlah[0] = BDUtil.add(komisiJumlah[0], komisiTotal[0]);
                                        komisiJumlah[1] = BDUtil.add(komisiJumlah[1], komisiTotal[1]);
                                        komisiJumlah[2] = BDUtil.add(komisiJumlah[2], komisiTotal[2]);
                                        komisiJumlah[3] = BDUtil.add(komisiJumlah[3], komisiTotal[3]);

                                        komisiTaxJumlah[0] = BDUtil.add(komisiTaxJumlah[0], komisiTaxTotal[0]);
                                        komisiTaxJumlah[1] = BDUtil.add(komisiTaxJumlah[1], komisiTaxTotal[1]);
                                        komisiTaxJumlah[2] = BDUtil.add(komisiTaxJumlah[2], komisiTaxTotal[2]);
                                        komisiTaxJumlah[3] = BDUtil.add(komisiTaxJumlah[3], komisiTaxTotal[3]);

                                        komisiPlusTaxJumlah[0] = BDUtil.add(komisiPlusTaxJumlah[0], BDUtil.add(komisiJumlah[0], komisiTaxJumlah[0]));
                                        komisiPlusTaxJumlah[1] = BDUtil.add(komisiPlusTaxJumlah[1], BDUtil.add(komisiJumlah[1], komisiTaxJumlah[1]));
                                        komisiPlusTaxJumlah[2] = BDUtil.add(komisiPlusTaxJumlah[2], BDUtil.add(komisiJumlah[2], komisiTaxJumlah[2]));
                                        komisiPlusTaxJumlah[3] = BDUtil.add(komisiPlusTaxJumlah[3], BDUtil.add(komisiJumlah[3], komisiTaxJumlah[3]));

                                        bfeePlusTaxJumlah = BDUtil.add(bfeePlusTaxJumlah, BDUtil.add(bfeeJumlah, bfeeTaxJumlah));
                                        hfeePlusTaxJumlah = BDUtil.add(hfeePlusTaxJumlah, BDUtil.add(hfeeJumlah, hfeeTaxJumlah));

                        %>

                        <fo:table-row >
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=j + 1%>.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=JSPUtil.print(view.getInvoice().getDtMutationDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=JSPUtil.print(view.getInvoice().getStAttrPolicyNo().substring(0, 4) + "-" + view.getInvoice().getStAttrPolicyNo().substring(4, 8) + "-" + view.getInvoice().getStAttrPolicyNo().substring(8, 12) + "-" + view.getInvoice().getStAttrPolicyNo().substring(12, 16) + "-" + view.getInvoice().getStAttrPolicyNo().substring(16, 18))%></fo:block></fo:table-cell><%--
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(premiGrossTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(biapolTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(biamatTotal, 2)%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(tagihBruto, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(komisiTotal[0], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(komisiTaxTotal[0], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(komisiTotal[1], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(komisiTaxTotal[1], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(bfeeTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(bfeeTaxTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(hfeeTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(hfeeTaxTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(diskonTotal, BDUtil.add(ppnTotal, feeBaseTotal)), 2)%></fo:block></fo:table-cell><%--
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(ppnTotal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(feeBaseTotal, 2)%></fo:block></fo:table-cell>--%>
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
                            <fo:table-cell number-columns-spanned="17">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row>

                        <fo:table-row >
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">JUMLAH</fo:block></fo:table-cell><%--
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(premiGrossJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(biapolJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(biamatJumlah, 2)%></fo:block></fo:table-cell>--%>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(tagihBrutoJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(komisiJumlah[0], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(komisiTaxJumlah[0], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(komisiJumlah[1], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(komisiTaxJumlah[1], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(bfeeJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(bfeeTaxJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(hfeeJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(hfeeTaxJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(BDUtil.add(diskonJumlah, BDUtil.add(ppnJumlah, feeBaseJumlah)), 2)%></fo:block></fo:table-cell><%--
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(ppnJumlah, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(feeBaseJumlah, 2)%></fo:block></fo:table-cell>--%>
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
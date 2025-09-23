<%@ page import="com.webfin.ar.forms.ReceiptForm,
         com.crux.web.controller.SessionManager,
         java.util.ArrayList,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         com.webfin.ar.model.*,
         java.math.BigDecimal,
         com.crux.lang.LanguageManager,
         java.util.Date"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="21cm"
                               page-width="29.7cm"
                               margin-top="0.75cm"
                               margin-bottom="1cm"
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

                    ARReceiptView receipt = (ARReceiptView) request.getAttribute("RECEIPT");

/*
                    String taxDesc = null;

                    DTOList tax = receipt.getDetails();
                    for (int m = 0; m < tax.size(); m++) {
                        ARReceiptLinesView view = (ARReceiptLinesView) tax.get(0);

                        taxDesc = view.getStDescription().substring(1, 5);
                    }*/


                    String no_cetak = receipt.getStReceiptNo().substring(0, 1) + "-" + receipt.getStReceiptNo().substring(1, 5)
                            + "-" + receipt.getStReceiptNo().substring(5, 9) + "-" + receipt.getStReceiptNo().substring(9, 14)
                            + "-" + receipt.getStReceiptNo().substring(14, 16) + "-" + receipt.getStReceiptNo().substring(16, 19);

                    BigDecimal outstandingTax = new BigDecimal(0);

                    boolean isPosted = receipt.isPosted();
        %>


        <fo:flow flow-name="xsl-region-body"> 

            <%
                        String bw = "0.2pt";
            %>
            <% if (!isPosted) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="5mm" color="red">SPECIMEN</fo:block>
            <% }%>            

            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="20mm"/><!-- Entry Date -->
                    <fo:table-column column-width="30mm"/><!-- Policy Date--> 
                    <fo:table-column column-width="20mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Policy No -->
                    <fo:table-column /><!-- Policy No -->
                    <fo:table-header> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-weight="bold" font-size="12pt" text-align="center">             
                                    LAPORAN PEMBAYARAN PAJAK KOMISI <%=JSPUtil.printX(receipt.getStTaxCode().toUpperCase())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-weight="bold" font-size="10pt" text-align="center" >
                                    Periode Bayar : <%=DateUtil.getDateStr(receipt.getDtReceiptDate(), "^^ yyyy")%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-weight="bold" text-align="start">
                                    No Bukti : <%=no_cetak%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-weight="bold" text-align="start">
                                    <%if (receipt.getStAccountEntityID() != null) {%>
                                    Bank : <%=receipt.getPaymentEntity().getStEntityName()%>
                                    <%}%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="13">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>   
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Tanggal Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Premi Bruto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Diskon</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Biaya Polis Materai</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Fee Base</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Total Komisi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Pajak Komisi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Premi Netto</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Pajak Dibayar</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Hutang Pajak</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Keterangan</fo:block></fo:table-cell> 
                        </fo:table-row>

                    </fo:table-header>                    
                    <fo:table-body>    

                        <%
                                    //final DTOList commissions = currentLine.getDetails();
                                    BigDecimal TotalPremi = null;
                                    BigDecimal TotalBPol = null;
                                    BigDecimal TotalBMat = null;
                                    BigDecimal TotalDisc = null;
                                    BigDecimal TotalComm = null;
                                    BigDecimal TotalBfee = null;
                                    BigDecimal TotalHfee = null;
                                    BigDecimal TotalFBase = null;
                                    BigDecimal TotalCommNonFee = null;
                                    BigDecimal TotalTaxComm = null;
                                    BigDecimal TotalBruto = null;
                                    BigDecimal TotalNetto = null;
                                    BigDecimal TotalPaid = null;
                                    BigDecimal TotalPremiPaid = null;
                                    BigDecimal TotalCommPaid = null;
                                    BigDecimal TotalCommDebt = null;
                                    BigDecimal TotalTaxCommPaid = null;
                                    String test = null;

                                    DTOList line = receipt.getDetails();
                                    for (int j = 0; j < line.size(); j++) {
                                        ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);

                                        BigDecimal subTotalPremi = null;
                                        BigDecimal subTotalBPol = null;
                                        BigDecimal subTotalBMat = null;
                                        BigDecimal subTotalDisc = null;
                                        BigDecimal subTotalComm = null;
                                        BigDecimal subTotalFBase = null;
                                        BigDecimal subTotalCommNonFee = null;
                                        BigDecimal subTotalTaxComm = null;
                                        BigDecimal subTotalBruto = null;
                                        BigDecimal subTotalNetto = null;
                                        BigDecimal subTotalPaid = null;
                                        BigDecimal subTotalPremiPaid = null;
                                        BigDecimal subTotalBPolPaid = null;
                                        BigDecimal subTotalBMatPaid = null;
                                        BigDecimal subTotalDiscPaid = null;
                                        BigDecimal subTotalCommPaid = null;
                                        BigDecimal subTotalFBasePaid = null;
                                        BigDecimal subTotalCommNonFeePaid = null;
                                        BigDecimal subTotalTaxCommPaid = null;

                                        final DTOList arInvoiceDetail = view.getARInvoiceDetails();
                                        for (int k = 0; k < arInvoiceDetail.size(); k++) {
                                            ARInvoiceDetailView detil = (ARInvoiceDetailView) arInvoiceDetail.get(k);

                                            if (detil.getStRootID() != null) {
                                                test = detil.getParentReceipt().getStARInvoiceID();
                                            }

                                            if (detil.isTaxComm() || detil.isTaxBrok() || detil.isTaxHFee()) {
                                                subTotalTaxComm = BDUtil.add(subTotalTaxComm, detil.getDbAmount());
                                                subTotalTaxCommPaid = BDUtil.add(subTotalTaxCommPaid, detil.getDbAmountSettled());
                                            }

                                            outstandingTax = BDUtil.sub(subTotalTaxComm, subTotalTaxCommPaid);

                                        }


                                        final DTOList arInvoiceDetail2 = view.getARInvoiceDetailsParentID(test);
                                        for (int l = 0; l < arInvoiceDetail2.size(); l++) {
                                            ARInvoiceDetailView detil2 = (ARInvoiceDetailView) arInvoiceDetail2.get(l);

                                            if (detil2.isPremiGross2()) {
                                                subTotalPremi = BDUtil.add(subTotalPremi, detil2.getDbAmount());
                                                subTotalPremiPaid = BDUtil.add(subTotalPremiPaid, detil2.getDbAmountSettled());
                                            }

                                            if (detil2.isPolicyCost2()) {
                                                subTotalBPol = BDUtil.add(subTotalBPol, detil2.getDbAmount());
                                                subTotalBPolPaid = BDUtil.add(subTotalBPolPaid, detil2.getDbAmountSettled());
                                            }

                                            if (detil2.isStampDuty2()) {
                                                subTotalBMat = BDUtil.add(subTotalBMat, detil2.getDbAmount());
                                                subTotalBMatPaid = BDUtil.add(subTotalBMatPaid, detil2.getDbAmountSettled());
                                            }

                                            if (detil2.isDiscount2()) {
                                                subTotalDisc = BDUtil.add(subTotalDisc, detil2.getDbAmount());
                                                subTotalDiscPaid = BDUtil.add(subTotalDiscPaid, detil2.getDbAmountSettled());
                                            }

                                            if (detil2.isCommission2() || detil2.isBrokerage2() || detil2.isHandlingFee2()) {
                                                subTotalComm = BDUtil.add(subTotalComm, detil2.getDbAmount());
                                                subTotalCommPaid = BDUtil.add(subTotalCommPaid, detil2.getDbAmountSettled());
                                            }

                                            if (detil2.isFeeBase3()) {
                                                subTotalFBase = BDUtil.add(subTotalFBase, detil2.getDbAmount());
                                                subTotalFBasePaid = BDUtil.add(subTotalFBasePaid, detil2.getDbAmountSettled());
                                            }

                                            subTotalCommNonFee = BDUtil.sub(subTotalComm, subTotalFBase);
                                            subTotalCommNonFeePaid = BDUtil.sub(subTotalCommPaid, subTotalFBasePaid);

                                            subTotalPaid = BDUtil.add(subTotalPremiPaid, BDUtil.add(subTotalBMatPaid, subTotalBPolPaid));
                                            subTotalPaid = BDUtil.sub(subTotalPaid, subTotalDiscPaid);
                                            subTotalPaid = BDUtil.sub(subTotalPaid, subTotalCommNonFeePaid);
                                            subTotalPaid = BDUtil.sub(subTotalPaid, subTotalFBasePaid);
                                            subTotalPaid = BDUtil.sub(subTotalPaid, subTotalTaxCommPaid);

                                            subTotalBruto = BDUtil.add(subTotalPremi, BDUtil.add(subTotalBPol, subTotalBMat));
                                            subTotalBruto = BDUtil.sub(subTotalBruto, subTotalDisc);

                                            subTotalNetto = BDUtil.add(subTotalCommNonFee, subTotalTaxComm);
                                            subTotalNetto = BDUtil.add(subTotalNetto, subTotalFBase);
                                            subTotalNetto = BDUtil.sub(subTotalBruto, subTotalNetto);
                                        }


                                        TotalPremi = BDUtil.add(TotalPremi, subTotalPremi);
                                        TotalBPol = BDUtil.add(TotalBPol, subTotalBPol);
                                        TotalBMat = BDUtil.add(TotalBMat, subTotalBMat);
                                        TotalDisc = BDUtil.add(TotalDisc, subTotalDisc);
                                        TotalComm = BDUtil.add(TotalComm, subTotalCommNonFee);
                                        TotalFBase = BDUtil.add(TotalFBase, subTotalFBase);
                                        TotalTaxComm = BDUtil.add(TotalTaxComm, subTotalTaxComm);
                                        TotalNetto = BDUtil.add(TotalNetto, subTotalNetto);
                                        TotalPaid = BDUtil.add(TotalPaid, subTotalPaid);
                                        TotalTaxCommPaid = BDUtil.add(TotalTaxCommPaid, subTotalTaxCommPaid);
                                        TotalCommDebt = BDUtil.add(TotalCommDebt, outstandingTax);

                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="8pt"><%=j + 1%>.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(view.getInvoice().getDtMutationDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(view.getInvoice().getStAttrPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalPremi, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalDisc, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(BDUtil.add(subTotalBPol, subTotalBMat), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalFBase, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalCommNonFee, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalTaxComm, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalNetto, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalTaxCommPaid, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(outstandingTax, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="start" font-size="6pt"><%=JSPUtil.printX(receipt.getEntity().getStEntityName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                    }
                        %> 	

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="13">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt" number-columns-spanned="3"><fo:block text-align="center" font-size="8pt" >JUMLAH</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalPremi, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalDisc, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(BDUtil.add(TotalBPol, TotalBMat), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalFBase, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalComm, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalTaxComm, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalNetto, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalTaxCommPaid, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalCommDebt, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-right-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ></fo:block></fo:table-cell>
                        </fo:table-row>                        

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="6pt" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%>
                PrintCode:<%=receipt.getStPrintCode()%> PrintStamp:<%=receipt.getStPrintStamp()%>
            </fo:block>  

            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(TotalPremi, 0)%>" orientation="0">
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
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
                               page-height="21.5cm"
                               page-width="33cm"
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

                    ARReceiptView receipt = (ARReceiptView) request.getAttribute("RECEIPT");

                    ArrayList colW = new ArrayList();

                    colW.add(new Integer(10));
                    colW.add(new Integer(30));
                    colW.add(new Integer(100));
                    colW.add(new Integer(30));
                    colW.add(new Integer(100));
                    colW.add(new Integer(50));
                    colW.add(new Integer(50));
                    colW.add(new Integer(50));
                    colW.add(new Integer(50));
                    colW.add(new Integer(50));
                    colW.add(new Integer(50));
                    colW.add(new Integer(50));
                    colW.add(new Integer(50));
                    colW.add(new Integer(50));
                    colW.add(new Integer(50));
                    colW.add(new Integer(50));
                    colW.add(new Integer(100));

                    //colW=FOPUtil.computeColumnWidth(colW,16,0," cm");
                    //C041020203506100000
                    //0123456789012345678
                    String no_cetak = receipt.getStReceiptNo().substring(0, 1) + "-" + receipt.getStReceiptNo().substring(1, 5)
                            + "-" + receipt.getStReceiptNo().substring(5, 9) + "-" + receipt.getStReceiptNo().substring(9, 14)
                            + "-" + receipt.getStReceiptNo().substring(14, 16) + "-" + receipt.getStReceiptNo().substring(16, 19);

                    BigDecimal outstandingClaim = null;

                    boolean isPosted = receipt.isPosted();

                    String panjar = null;
                    DTOList line = receipt.getDetails();
                    for (int k = 0; k < line.size(); k++) {
                        ARReceiptLinesView view2 = (ARReceiptLinesView) line.get(k);
                        panjar = view2.getStAdvancePaymentFlag();
                    }
        %>


        <fo:flow flow-name="xsl-region-body">

            <%
                        String bw = "0.2pt";
            %>
            <% if (!isPosted) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="5mm" color="red">SPECIMEN</fo:block>
            <% }%>            
            <% if (panjar != null) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="5mm">DAFTAR PEMBAYARAN PANJAR KLAIM</fo:block>
            <% } else {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="5mm">DAFTAR PEMBAYARAN KLAIM</fo:block>
            <% }%>    
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="10mm">Periode Bayar : <%=DateUtil.getDateStr(receipt.getDtReceiptDate(), "^^ yyyy")%> </fo:block>
            <fo:block font-family="Helvetica" font-size="6pt" text-align="left" line-height="5mm">No Bukti : <%=no_cetak%></fo:block>
            <%if (receipt.getStAccountEntityID() != null) {%><fo:block font-family="Helvetica" font-size="6pt" text-align="left" line-height="5mm">Bank : <%=receipt.getPaymentEntity().getStEntityName()%></fo:block><%}%>
            <fo:block font-family="Helvetica" font-size="6pt" display-align="center" border-width="0.1pt">

                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>" >
                    <fo:table-header>

                        <fo:table-row>   
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >No</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Tgl Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >No Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Tgl LKS/LKP</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >No LKS/LKP</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Jumlah Klaim</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Adjuster</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Deductible</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Subrogasi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Salvage</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Depresiasi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Pinalty</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Pph-23</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Fee Recovery</fo:block></fo:table-cell>
                            <% if (panjar != null) {%>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Panjar Klaim</fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Bayar Klaim</fo:block></fo:table-cell>
                            <% }%>    
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Hutang Klaim</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Keterangan</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>

                    <%=FOPUtil.printColumnWidth(colW, 32, 1, "cm")%>
                    <fo:table-body>    

                        <%
                                    //final DTOList commissions = currentLine.getDetails();
                                    BigDecimal TotalClaim = null;
                                    BigDecimal TotalClaimNetto = null;
                                    BigDecimal TotalDeduct = null;
                                    BigDecimal TotalAdjuster = null;
                                    BigDecimal TotalSubrogasi = null;
                                    BigDecimal TotalSalvage = null;
                                    BigDecimal TotalDepresiasi = null;
                                    BigDecimal TotalPinalty = null;
                                    BigDecimal TotalPPH = null;
                                    BigDecimal TotalFeeRecovery = null;
                                    BigDecimal TotalCashColl = null;

                                    BigDecimal TotalClaimPaid = null;
                                    BigDecimal TotalClaimNettoPaid = null;
                                    BigDecimal TotalDeductPaid = null;
                                    BigDecimal TotalAdjusterPaid = null;
                                    BigDecimal TotalSubrogasiPaid = null;
                                    BigDecimal TotalSalvagePaid = null;
                                    BigDecimal TotalDepresiasiPaid = null;
                                    BigDecimal TotalPinaltyPaid = null;
                                    BigDecimal TotalPPHPaid = null;
                                    BigDecimal TotalFeeRecoveryPaid = null;
                                    BigDecimal TotalCashCollPaid = null;

                                    //DTOList line = receipt.getDetails();
                                    for (int j = 0; j < line.size(); j++) {
                                        ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);

                                        String no_polis_cetak = view.getPolicy().getStPolicyNo().substring(0, 4) + "." + view.getPolicy().getStPolicyNo().substring(4, 8)
                                                + "." + view.getPolicy().getStPolicyNo().substring(8, 12) + "." + view.getPolicy().getStPolicyNo().substring(12, 16)
                                                + "." + view.getPolicy().getStPolicyNo().substring(16, 18);

                                        BigDecimal subTotalClaim = null;
                                        BigDecimal subTotalClaimNetto = null;
                                        BigDecimal subTotalDeduct = null;
                                        BigDecimal subTotalAdjuster = null;
                                        BigDecimal subTotalSubrogasi = null;
                                        BigDecimal subTotalSalvage = null;
                                        BigDecimal subTotalDepresiasi = null;
                                        BigDecimal subTotalPinalty = null;
                                        BigDecimal subTotalPPH = null;
                                        BigDecimal subTotalFeeRecovery = null;
                                        BigDecimal subTotalCashColl = null;

                                        BigDecimal subTotalClaimPaid = null;
                                        BigDecimal subTotalClaimNettoPaid = null;
                                        BigDecimal subTotalDeductPaid = null;
                                        BigDecimal subTotalAdjusterPaid = null;
                                        BigDecimal subTotalSubrogasiPaid = null;
                                        BigDecimal subTotalSalvagePaid = null;
                                        BigDecimal subTotalDepresiasiPaid = null;
                                        BigDecimal subTotalPinaltyPaid = null;
                                        BigDecimal subTotalPPHPaid = null;
                                        BigDecimal subTotalFeeRecoveryPaid = null;
                                        BigDecimal subTotalCashCollPaid = null;

                                        if (view.getStAdvancePaymentFlag() != null) {
                                            subTotalClaim = BDUtil.add(subTotalClaim, view.getDbAmount());
                                            subTotalClaimPaid = BDUtil.add(subTotalClaimPaid, view.getDbEnteredAmount());
                                        } else {
                                            final DTOList arInvoiceDetail = view.getARInvoiceDetails();
                                            for (int k = 0; k < arInvoiceDetail.size(); k++) {
                                                ARInvoiceDetailView detil = (ARInvoiceDetailView) arInvoiceDetail.get(k);

                                                if (detil.isClaimGross()
                                                        || detil.isWreck()
                                                        || detil.isTJH()
                                                        || detil.isTowingCost()
                                                        || detil.isEXGratia()
                                                        || detil.isSparepart()
                                                        || detil.isJBengkel()
                                                        || detil.isEXGratiaUW()
                                                        || detil.isIntPayment()
                                                        || detil.isPPN()
                                                        || detil.isMaterial()
                                                        || detil.isExpenses()
                                                        || detil.isVatFee()
                                                        || detil.isExAccident()
                                                        || detil.isAdminFee()
                                                        || detil.isSurveyAdjFee()
                                                        || detil.isCostSurvey()
                                                        || detil.isBunga()) {
                                                    subTotalClaim = BDUtil.add(subTotalClaim, detil.getDbAmount());
                                                    subTotalClaimPaid = BDUtil.add(subTotalClaimPaid, detil.getDbAmountSettled());
                                                }

                                                if (detil.isDeduct()) {
                                                    subTotalDeduct = BDUtil.add(subTotalDeduct, detil.getDbAmount());
                                                    subTotalDeductPaid = BDUtil.add(subTotalDeductPaid, detil.getDbAmountSettled());
                                                }

                                                if (detil.isAdjusterFee()) {
                                                    subTotalAdjuster = BDUtil.add(subTotalAdjuster, detil.getDbAmount());
                                                    subTotalAdjusterPaid = BDUtil.add(subTotalAdjusterPaid, detil.getDbAmountSettled());
                                                }

                                                if (detil.isSubrogasi()) {
                                                    subTotalSubrogasi = BDUtil.add(subTotalSubrogasi, detil.getDbAmount());
                                                    subTotalSubrogasiPaid = BDUtil.add(subTotalSubrogasiPaid, detil.getDbAmountSettled());
                                                }

                                                if (detil.isSalvage()) {
                                                    subTotalSalvage = BDUtil.add(subTotalSubrogasiPaid, detil.getDbAmount());
                                                    subTotalSalvagePaid = BDUtil.add(subTotalSalvagePaid, detil.getDbAmountSettled());
                                                }

                                                if (detil.isDepresiasi()) {
                                                    subTotalDepresiasi = BDUtil.add(subTotalDepresiasi, detil.getDbAmount());
                                                    subTotalDepresiasiPaid = BDUtil.add(subTotalDepresiasiPaid, detil.getDbAmountSettled());
                                                }

                                                if (detil.isPinalty()) {
                                                    subTotalPinalty = BDUtil.add(subTotalPinalty, detil.getDbAmount());
                                                    subTotalPinaltyPaid = BDUtil.add(subTotalPinaltyPaid, detil.getDbAmountSettled());
                                                }

                                                if (detil.isPPH23()) {
                                                    subTotalPPH = BDUtil.add(subTotalPPH, detil.getDbAmount());
                                                    subTotalPPHPaid = BDUtil.add(subTotalPPHPaid, detil.getDbAmountSettled());
                                                }

                                                if (detil.isFEERCVY()) {
                                                    subTotalFeeRecovery = BDUtil.add(subTotalFeeRecovery, detil.getDbAmount());
                                                    subTotalFeeRecoveryPaid = BDUtil.add(subTotalFeeRecoveryPaid, detil.getDbAmountSettled());
                                                }

                                                if (detil.isCashColl()) {
                                                    subTotalCashColl = BDUtil.add(subTotalCashColl, detil.getDbAmount());
                                                    subTotalCashCollPaid = BDUtil.add(subTotalCashCollPaid, detil.getDbAmountSettled());
                                                }
                                            }
                                        }

                                        subTotalClaimNetto = BDUtil.sub(subTotalClaim, subTotalDeduct);
                                        subTotalClaimNetto = BDUtil.sub(subTotalClaimNetto, subTotalAdjuster);
                                        subTotalClaimNetto = BDUtil.sub(subTotalClaimNetto, subTotalSubrogasi);
                                        subTotalClaimNetto = BDUtil.sub(subTotalClaimNetto, subTotalSalvage);
                                        subTotalClaimNetto = BDUtil.sub(subTotalClaimNetto, subTotalDepresiasi);
                                        subTotalClaimNetto = BDUtil.sub(subTotalClaimNetto, subTotalPinalty);
                                        subTotalClaimNetto = BDUtil.sub(subTotalClaimNetto, subTotalPPH);
                                        subTotalClaimNetto = BDUtil.sub(subTotalClaimNetto, subTotalFeeRecovery);
                                        subTotalClaimNetto = BDUtil.sub(subTotalClaimNetto, subTotalCashColl);

                                        subTotalClaimNettoPaid = BDUtil.sub(subTotalClaimPaid, subTotalDeductPaid);
                                        subTotalClaimNettoPaid = BDUtil.sub(subTotalClaimNettoPaid, subTotalAdjusterPaid);
                                        subTotalClaimNettoPaid = BDUtil.sub(subTotalClaimNettoPaid, subTotalSubrogasiPaid);
                                        subTotalClaimNettoPaid = BDUtil.sub(subTotalClaimNettoPaid, subTotalSalvagePaid);
                                        subTotalClaimNettoPaid = BDUtil.sub(subTotalClaimNettoPaid, subTotalDepresiasiPaid);
                                        subTotalClaimNettoPaid = BDUtil.sub(subTotalClaimNettoPaid, subTotalPinaltyPaid);
                                        subTotalClaimNettoPaid = BDUtil.sub(subTotalClaimNettoPaid, subTotalPPHPaid);
                                        subTotalClaimNettoPaid = BDUtil.sub(subTotalClaimNettoPaid, subTotalFeeRecoveryPaid);
                                        subTotalClaimNettoPaid = BDUtil.sub(subTotalClaimNettoPaid, subTotalCashCollPaid);

                                        outstandingClaim = BDUtil.sub(subTotalClaimNetto, subTotalClaimNettoPaid);

                                        TotalClaim = BDUtil.add(TotalClaim, subTotalClaim);
                                        TotalDeduct = BDUtil.add(TotalDeduct, subTotalDeduct);
                                        TotalAdjuster = BDUtil.add(TotalAdjuster, subTotalAdjuster);
                                        TotalSubrogasi = BDUtil.add(TotalSubrogasi, subTotalSubrogasi);
                                        TotalSalvage = BDUtil.add(TotalSalvage, subTotalSalvage);
                                        TotalDepresiasi = BDUtil.add(TotalDepresiasi, subTotalDepresiasi);
                                        TotalPinalty = BDUtil.add(TotalPinalty, subTotalPinalty);
                                        TotalPPH = BDUtil.add(TotalPPH, subTotalPPH);
                                        TotalFeeRecovery = BDUtil.add(TotalFeeRecovery, subTotalFeeRecovery);
                                        TotalCashColl = BDUtil.add(TotalCashColl, subTotalCashColl);

                                        TotalClaimNetto = BDUtil.sub(TotalClaim, TotalDeduct);
                                        TotalClaimNetto = BDUtil.sub(TotalClaimNetto, TotalAdjuster);
                                        TotalClaimNetto = BDUtil.sub(TotalClaimNetto, TotalSubrogasi);
                                        TotalClaimNetto = BDUtil.sub(TotalClaimNetto, TotalSalvage);
                                        TotalClaimNetto = BDUtil.sub(TotalClaimNetto, TotalDepresiasi);
                                        TotalClaimNetto = BDUtil.sub(TotalClaimNetto, TotalPinalty);
                                        TotalClaimNetto = BDUtil.sub(TotalClaimNetto, TotalPPH);
                                        TotalClaimNetto = BDUtil.sub(TotalClaimNetto, TotalFeeRecovery);
                                        TotalClaimNetto = BDUtil.sub(TotalClaimNetto, TotalCashColl);

                                        TotalClaimPaid = BDUtil.add(TotalClaimPaid, outstandingClaim);

                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=j + 1%>.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="6pt"><%=JSPUtil.print(view.getPolicy().getDtPolicyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="6pt"><%=JSPUtil.print(no_polis_cetak)%></fo:block></fo:table-cell>
                            <% if (panjar != null) {%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="6pt"><%=JSPUtil.print(view.getPolicy().getDtPLADate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="6pt"><%=JSPUtil.print(view.getPolicy().getStPLANo())%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="6pt"><%=JSPUtil.print(view.getPolicy().getDtDLADate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="6pt"><%=JSPUtil.print(view.getPolicy().getStDLANo())%></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalClaim, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalAdjuster, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalDeduct, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalSubrogasi, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalSalvage, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalDepresiasi, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalPinalty, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalPPH, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalFeeRecovery, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalClaimNettoPaid, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(outstandingClaim, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="start" font-size="6pt"><%=JSPUtil.xmlEscape(view.getPolicy().getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                    }
                        %> 	

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="17">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="5"><fo:block text-align="center" font-size="6pt" >JUMLAH</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalClaim, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalAdjuster, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalDeduct, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalSubrogasi, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalSalvage, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalDepresiasi, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalPinalty, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalPPH, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalFeeRecovery, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalClaimNetto, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalClaimPaid, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
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
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(TotalClaim, 0)%>" orientation="0">
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
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
                               page-width="33cm"
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
        
        ReceiptForm form =  (ReceiptForm) request.getAttribute("FORM");
        
        ARReceiptView receipt =  (ARReceiptView) request.getAttribute("RECEIPT");
        
        ArrayList colW = new ArrayList();
        
        colW.add(new Integer(30));
        colW.add(new Integer(80));
        colW.add(new Integer(180));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(90));
        colW.add(new Integer(100));
        colW.add(new Integer(80));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(80));
        colW.add(new Integer(100));
        colW.add(new Integer(140));
        
        //colW=FOPUtil.computeColumnWidth(colW,16,0," cm");
        //C041020203506100000
        //0123456789012345678
        String no_polis_cetak = null;
        String no_cetak = receipt.getStReceiptNo().substring(0,1) +"-"+ receipt.getStReceiptNo().substring(1,5)+
                "-" + receipt.getStReceiptNo().substring(5,9) + "-" + receipt.getStReceiptNo().substring(9,14)+
                "-" + receipt.getStReceiptNo().substring(14,16) +"-" + receipt.getStReceiptNo().substring(16,19);
        
        BigDecimal outstandingPremi = new BigDecimal(0);
        BigDecimal outstandingComm = new BigDecimal(0);
        BigDecimal outstandingCommPlusTax = new BigDecimal(0);
        BigDecimal outstandingPPN = new BigDecimal(0);
        BigDecimal outstandingFBase = new BigDecimal(0);
        BigDecimal outstandingTax = new BigDecimal(0);
        
        boolean isPosted = receipt.isPosted();
        %>
        
        
        <fo:flow flow-name="xsl-region-body">
            
            <%
            String bw = "0.2pt";
            %>
            
            <% if (!isPosted) { %>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="5mm" color="red">SPECIMEN</fo:block>
            <% } %>            
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="5mm">DAFTAR PENERIMAAN PREMI</fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="10mm">Periode Bayar : <%=DateUtil.getDateStr(receipt.getDtReceiptDate(), "^^ yyyy")%> </fo:block>
            <fo:block font-family="Helvetica" font-size="6pt" text-align="left" line-height="5mm">No Bukti : <%=no_cetak%></fo:block>
            <%if(receipt.getStAccountEntityID()!=null){%><fo:block font-family="Helvetica" font-size="6pt" text-align="left" line-height="5mm">Bank : <%=receipt.getPaymentEntity().getStEntityName()%></fo:block><%}%>
            <fo:block font-family="Helvetica" font-size="6pt" display-align="center" border-width="0.1pt">
                
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>" >
                    <fo:table-header>
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >No</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Tanggal</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >No Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Premi Bruto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Diskon</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Biaya Polis Materai</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Tagihan Bruto</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Komisi + Pajak</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Fee Base</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >PPN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Tagihan Netto</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Tgl Byr</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Jumlah</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Komisi Dibayar</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Fee Base Dibayar</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Hutang Komisi</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Hutang Fee Base</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Hutang PPN</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Hutang Pajak</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >O/S Premi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt" >Keterangan</fo:block></fo:table-cell> 
                        </fo:table-row>
                        
                    </fo:table-header>
                    
                    <%=FOPUtil.printColumnWidth(colW,32,1,"cm")%>
                    <fo:table-body>    
                        
                        <%
                        //final DTOList commissions = currentLine.getDetails();
                        BigDecimal TotalPremi = null;
                        BigDecimal TotalBPol = null;
                        BigDecimal TotalBMat = null;
                        BigDecimal TotalDisc = null;
                        BigDecimal TotalPPN = null;
                        BigDecimal TotalComm = null;
                        BigDecimal TotalCommPlusTax = null;
                        BigDecimal TotalFBase = null;
                        BigDecimal TotalTaxComm = null;
                        BigDecimal TotalTaxComm1 = null;
                        BigDecimal TotalTaxComm2 = null;
                        BigDecimal TotalBruto = null;
                        BigDecimal TotalNetto = null;
                        BigDecimal TotalPaid = null;
                        BigDecimal TotalPremiPaid = null;
                        BigDecimal TotalCommPaid = null;
                        BigDecimal TotalCommPlusTaxPaid = null;
                        BigDecimal TotalPPNPaid = null;
                        BigDecimal TotalFBasePaid = null;
                        BigDecimal TotalCommDebt = null;
                        BigDecimal TotalCommPlusTaxDebt = null;
                        BigDecimal TotalFBaseDebt = null;
                        BigDecimal TotalPPNDebt = null;
                        BigDecimal TotalTaxCommPaid = null;
                        
                        DTOList line = receipt.getDetails();
                        for(int j=0;j<line.size();j++){
                            ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);
                            
                            if (view.getInvoice().getStRefID0().equalsIgnoreCase("FOXPRO")) {
                                if (view.getInvoice().getStAttrPolicyNo().length()==17) {
                                    no_polis_cetak = view.getInvoice().getStAttrPolicyNo().substring(0,4)+"."+view.getInvoice().getStAttrPolicyNo().substring(4,8)+
                                            "."+view.getInvoice().getStAttrPolicyNo().substring(8,12)+"."+view.getInvoice().getStAttrPolicyNo().substring(12,16)+
                                            "."+view.getInvoice().getStAttrPolicyNo().substring(16,17);
                                } else if (view.getInvoice().getStAttrPolicyNo().length()==16) {
                                    no_polis_cetak = view.getInvoice().getStAttrPolicyNo().substring(0,4)+"."+view.getInvoice().getStAttrPolicyNo().substring(4,8)+
                                            "."+view.getInvoice().getStAttrPolicyNo().substring(8,12)+"."+view.getInvoice().getStAttrPolicyNo().substring(12,16);
                                }
                            } else {
                                no_polis_cetak = view.getInvoice().getStAttrPolicyNo().substring(0,4)+"."+view.getInvoice().getStAttrPolicyNo().substring(4,8)+
                                        "."+view.getInvoice().getStAttrPolicyNo().substring(8,12)+"."+view.getInvoice().getStAttrPolicyNo().substring(12,16)+
                                        "."+view.getInvoice().getStAttrPolicyNo().substring(16,18);
                            }
                            
                            BigDecimal subTotalPremi = null;
                            BigDecimal subTotalBPol = null;
                            BigDecimal subTotalBMat = null;
                            BigDecimal subTotalDisc = null;
                            BigDecimal subTotalPPN = null;
                            BigDecimal subTotalComm = null;
                            BigDecimal subTotalCommPlusTax = null;
                            BigDecimal subTotalFBase = null;
                            BigDecimal subTotalTaxComm = null;
                            BigDecimal subTotalTaxComm1 = null;
                            BigDecimal subTotalTaxComm2 = null;
                            BigDecimal subTotalBruto = null;
                            BigDecimal subTotalNetto = null;
                            BigDecimal subTotalPaid = null;
                            BigDecimal subTotalPremiPaid = null;
                            BigDecimal subTotalBPolPaid = null;
                            BigDecimal subTotalBMatPaid = null;
                            BigDecimal subTotalDiscPaid = null;
                            BigDecimal subTotalPPNPaid = null;
                            BigDecimal subTotalFBasePaid = null;
                            BigDecimal subTotalCommPaid = null;
                            BigDecimal subTotalCommPlusTaxPaid = null;
                            BigDecimal subTotalTaxCommPaid = null;
                            
                            final DTOList arInvoiceDetail = view.getARInvoiceDetails();
                            for (int k = 0; k < arInvoiceDetail.size(); k++) {
                                ARInvoiceDetailView detil = (ARInvoiceDetailView) arInvoiceDetail.get(k);
                                
                                if(detil.isPremiGross2()) {
                                    subTotalPremi = BDUtil.add(subTotalPremi,detil.getDbAmount());
                                    subTotalPremiPaid = BDUtil.add(subTotalPremiPaid,detil.getDbAmountSettled());
                                }
                                
                                if(detil.isCommission2()||detil.isBrokerage2()||detil.isHandlingFee2()) {
                                    subTotalComm = BDUtil.add(subTotalComm,detil.getDbAmount());
                                    subTotalCommPaid = BDUtil.add(subTotalCommPaid,detil.getDbAmountSettled());
                                }
                                
                                if(detil.isFeeBase3()) {
                                    subTotalFBase = BDUtil.add(subTotalFBase,detil.getDbAmount());
                                    subTotalFBasePaid = BDUtil.add(subTotalFBasePaid,detil.getDbAmountSettled());
                                }
                                
                                if(detil.isPolicyCost2()) {
                                    subTotalBPol = BDUtil.add(subTotalBPol,detil.getDbAmount());
                                    subTotalBPolPaid = BDUtil.add(subTotalBPolPaid,detil.getDbAmountSettled());
                                }
                                
                                if(detil.isStampDuty2()) {
                                    subTotalBMat = BDUtil.add(subTotalBMat,detil.getDbAmount());
                                    subTotalBMatPaid = BDUtil.add(subTotalBMatPaid,detil.getDbAmountSettled());
                                }
                                
                                if(detil.isTaxComm()||detil.isTaxBrok()||detil.isTaxHFee()) {
                                    if (detil.getParentRefInvoiceDetail()!=null) {
                                        if (!Tools.isEqual(detil.getParentRefInvoiceDetail().getDbAmountSettled(), new BigDecimal(0))) {
                                            subTotalTaxComm = BDUtil.add(subTotalTaxComm,detil.getDbAmount());
                                            subTotalTaxCommPaid = BDUtil.add(subTotalTaxCommPaid,detil.getDbAmountSettled());
                                        } else {
                                            subTotalTaxComm = BDUtil.add(subTotalTaxComm,detil.getDbAmount());
                                            subTotalTaxCommPaid = BDUtil.add(subTotalTaxCommPaid,detil.getDbAmountSettled());
                                        }
                                    } else {
                                        subTotalTaxComm = BDUtil.add(subTotalTaxComm,detil.getDbAmount());
                                        subTotalTaxCommPaid = BDUtil.add(subTotalTaxCommPaid,detil.getDbAmountSettled());
                                    }
                                }
                                
                                if(detil.isPPN()) {
                                    subTotalPPN = BDUtil.add(subTotalPPN,detil.getDbAmount());
                                    subTotalPPNPaid = BDUtil.add(subTotalPPNPaid,detil.getDbAmountSettled());
                                }
                                
                                if(detil.isDiscount2()) {
                                    subTotalDisc = BDUtil.add(subTotalDisc,detil.getDbAmount());
                                    subTotalDiscPaid = BDUtil.add(subTotalDiscPaid,detil.getDbAmountSettled());
                                }
                                
                                subTotalCommPlusTax = BDUtil.add(subTotalComm, subTotalTaxComm);
                                subTotalCommPlusTax = BDUtil.sub(subTotalCommPlusTax, subTotalFBase);
                                subTotalCommPlusTax = BDUtil.sub(subTotalCommPlusTax, subTotalPPN);
                                
                                subTotalCommPlusTaxPaid = BDUtil.add(subTotalCommPaid, subTotalTaxCommPaid);
                                subTotalCommPlusTaxPaid = BDUtil.sub(subTotalCommPlusTaxPaid, subTotalFBasePaid);
                                subTotalCommPlusTaxPaid = BDUtil.sub(subTotalCommPlusTaxPaid, subTotalPPNPaid);
                                
                                subTotalPaid = BDUtil.add(subTotalPremiPaid, BDUtil.add(subTotalBMatPaid, subTotalBPolPaid));
                                subTotalPaid = BDUtil.sub(subTotalPaid, subTotalDiscPaid);
                                subTotalPaid = BDUtil.sub(subTotalPaid, subTotalPPNPaid);
                                subTotalPaid = BDUtil.sub(subTotalPaid, subTotalFBasePaid);
                                subTotalPaid = BDUtil.sub(subTotalPaid, subTotalCommPlusTaxPaid);
                                //subTotalPaid = BDUtil.sub(subTotalPaid, subTotalTaxCommPaid);
                                
                                outstandingPremi = BDUtil.sub(subTotalPremi, subTotalPremiPaid);
                                outstandingComm = BDUtil.sub(subTotalComm, subTotalCommPaid);
                                outstandingPPN = BDUtil.sub(subTotalPPN, subTotalPPNPaid);
                                outstandingFBase = BDUtil.sub(subTotalFBase, subTotalFBasePaid);
                                outstandingTax = BDUtil.sub(subTotalTaxComm, subTotalTaxCommPaid);
                                outstandingCommPlusTax = BDUtil.sub(subTotalCommPlusTax, subTotalCommPlusTaxPaid);
                                outstandingCommPlusTax = BDUtil.sub(outstandingCommPlusTax, outstandingTax);
                                
                                subTotalBruto = BDUtil.add(subTotalPremi, BDUtil.add(subTotalBPol, subTotalBMat));
                                subTotalBruto = BDUtil.sub(subTotalBruto, subTotalDisc);
                                
                                subTotalNetto = BDUtil.sub(subTotalBruto, subTotalCommPlusTax);
                                subTotalNetto = BDUtil.sub(subTotalNetto, subTotalPPN);
                                subTotalNetto = BDUtil.sub(subTotalNetto, subTotalFBase);
                                subTotalNetto = BDUtil.add(subTotalNetto, subTotalTaxComm);
                                
                            }
                            
                            TotalPremi =  BDUtil.add(TotalPremi, subTotalPremi);
                            TotalBPol =  BDUtil.add(TotalBPol, subTotalBPol);
                            TotalBMat =  BDUtil.add(TotalBMat, subTotalBMat);
                            TotalDisc =  BDUtil.add(TotalDisc, subTotalDisc);
                            TotalComm =  BDUtil.add(TotalComm, subTotalComm);
                            TotalCommPlusTax =  BDUtil.add(TotalCommPlusTax, subTotalCommPlusTax);
                            TotalPPN =  BDUtil.add(TotalPPN, subTotalPPN);
                            TotalFBase =  BDUtil.add(TotalFBase, subTotalFBase);
                            TotalTaxComm =  BDUtil.add(TotalTaxComm, subTotalTaxComm);
                            TotalTaxComm1 =  BDUtil.add(TotalTaxComm1, subTotalTaxComm1);
                            TotalTaxComm2 =  BDUtil.add(TotalTaxComm2, subTotalTaxComm2);
                            TotalBruto =  BDUtil.add(TotalBruto, subTotalBruto);
                            TotalNetto =  BDUtil.add(TotalNetto, subTotalNetto);
                            TotalPaid = BDUtil.add(TotalPaid, subTotalPaid);
                            TotalPremiPaid = BDUtil.add(TotalPremiPaid, outstandingPremi);
                            TotalCommPaid = BDUtil.add(TotalCommPaid, subTotalCommPaid);
                            TotalCommPlusTaxPaid = BDUtil.add(TotalCommPlusTaxPaid, subTotalCommPlusTaxPaid);
                            TotalFBasePaid = BDUtil.add(TotalFBasePaid, subTotalFBasePaid);
                            TotalPPNPaid = BDUtil.add(TotalPPNPaid, subTotalPPNPaid);
                            TotalCommDebt = BDUtil.add(TotalCommDebt, outstandingComm);
                            TotalCommPlusTaxDebt = BDUtil.add(TotalCommPlusTaxDebt, outstandingCommPlusTax);
                            TotalFBaseDebt = BDUtil.add(TotalFBaseDebt, outstandingFBase);
                            TotalPPNDebt = BDUtil.add(TotalPPNDebt, outstandingPPN);
                            TotalTaxCommPaid = BDUtil.add(TotalTaxCommPaid, outstandingTax);
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=j+1%>.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="6pt"><%=JSPUtil.print(view.getInvoice().getDtMutationDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="6pt"><%=JSPUtil.print(no_polis_cetak)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalDisc,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(BDUtil.add(subTotalBPol,subTotalBMat),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalBruto,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalCommPlusTax,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalFBase,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalPPN,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalNetto,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.print(view.getDtReceiptDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalCommPlusTaxPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(subTotalFBasePaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(outstandingCommPlusTax,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(outstandingFBase,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(outstandingPPN,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(outstandingTax,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(outstandingPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="start" font-size="6pt"><%=JSPUtil.xmlEscape(view.getInvoice().getStAttrPolicyName())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <%
                        }
                        %> 	
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="21">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="3"><fo:block text-align="center" font-size="6pt" >JUMLAH</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalDisc,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(BDUtil.add(TotalBPol,TotalBMat),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalBruto,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalCommPlusTax,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalFBase,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalPPN,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalNetto,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalCommPlusTaxPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalFBasePaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalCommPlusTaxDebt,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalFBaseDebt,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalPPNDebt,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalTaxCommPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(TotalPremiPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="6pt" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%>                 
                PrintCode:<%=receipt.getStPrintCode()%> PrintStamp:<%=receipt.getStPrintStamp()%>
            </fo:block>  
            
            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(TotalPremi,0)%>" orientation="0">
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
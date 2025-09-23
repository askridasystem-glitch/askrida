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
        
        String no_cetak = receipt.getStReceiptNo().substring(0,1) +"-"+ receipt.getStReceiptNo().substring(1,5)+
                "-" + receipt.getStReceiptNo().substring(5,9) + "-" + receipt.getStReceiptNo().substring(9,14)+
                "-" + receipt.getStReceiptNo().substring(14,16) +"-" + receipt.getStReceiptNo().substring(16,19);
        
        BigDecimal outstandingComm = new BigDecimal(0);
        BigDecimal outstandingCommTax = new BigDecimal(0);
        
        boolean isPosted = receipt.isPosted();        
        %>
        
        
        <fo:flow flow-name="xsl-region-body"> 
            
            <%
            String bw = "0.2pt";
            %>
            
            <% if (!isPosted) { %>
            <fo:block  font-weight="bold" text-align="center" line-height="5mm" color="red">SPECIMEN</fo:block>
            <% } %>            
            
            
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
                    <fo:table-column column-width="20mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Policy No -->
                    <fo:table-column /><!-- Policy No -->
                    <fo:table-header> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block font-weight="bold" font-size="12pt" text-align="center">             
                                    LAPORAN PEMBAYARAN HUTANG KOMISI    
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block font-weight="bold" font-size="10pt" text-align="center" >
                                    Periode Bayar : <%=DateUtil.getDateStr(receipt.getDtReceiptDate(), "^^ yyyy")%> 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block font-weight="bold" text-align="start">
                                    No Bukti : <%=no_cetak%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block font-weight="bold" text-align="start">
                                    <%if(receipt.getStAccountEntityID()!=null){%>
                                    Bank : <%=receipt.getPaymentEntity().getStEntityName()%>
                                    <%}%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="16">  
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
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Komisi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Pajak Komisi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Bfee</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Hfee</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Fee Base</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Premi Netto</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Komisi Dibayar</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Hutang Komisi</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Hutang Pajak</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Keterangan</fo:block></fo:table-cell> 
                        </fo:table-row>
                        
                    </fo:table-header>                    
                    <fo:table-body>    
                        
                        <%
                        //final DTOList commissions = currentLine.getDetails();
                        BigDecimal TotalPremi = null;
                        BigDecimal TotalCommRef = null;
                        BigDecimal TotalTaxCommRef = null;
                        BigDecimal TotalBPol = null;
                        BigDecimal TotalBMat = null;
                        BigDecimal TotalDisc = null;
                        BigDecimal TotalBfee = null;
                        BigDecimal TotalHfee = null;
                        BigDecimal TotalBruto = null;
                        BigDecimal TotalNetto = null;
                        BigDecimal TotalPaid = null;
                        BigDecimal TotalPremiPaid = null;
                        BigDecimal TotalComm = null;
                        BigDecimal TotalFeeBase = null;
                        BigDecimal TotalCommPaid = null;
                        BigDecimal TotalCommNonFee = null;
                        BigDecimal TotalCommPaidNonFee = null;
                        BigDecimal TotalTaxComm = null;
                        BigDecimal TotalTaxCommPaid = null;
                        BigDecimal TotalOutstandingComm = null;
                        BigDecimal TotalOutstandingTaxComm = null;
                        String test = null;
                        
                        DTOList line = receipt.getDetails();
                        for(int j=0;j<line.size();j++){
                            ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);
                            
                            String no_polis_cetak = view.getInvoice().getStAttrPolicyNo().substring(0,4)+"."+view.getInvoice().getStAttrPolicyNo().substring(4,8)+
                                    "."+view.getInvoice().getStAttrPolicyNo().substring(8,12)+"."+view.getInvoice().getStAttrPolicyNo().substring(12,16)+
                                    "."+view.getInvoice().getStAttrPolicyNo().substring(16,18);
                            
                            BigDecimal subTotalPremi = null;
                            BigDecimal subTotalBPol = null;
                            BigDecimal subTotalBMat = null;
                            BigDecimal subTotalCommRef = null;
                            BigDecimal subTotalTaxCommRef = null;
                            BigDecimal subTotalDisc = null;
                            BigDecimal subTotalBfee = null;
                            BigDecimal subTotalHfee = null;
                            BigDecimal subTotalBruto = null;
                            BigDecimal subTotalNetto = null;
                            BigDecimal subTotalPaid = null;
                            BigDecimal subTotalPremiPaid = null;
                            BigDecimal subTotalBPolPaid = null;
                            BigDecimal subTotalBMatPaid = null;
                            BigDecimal subTotalDiscPaid = null;
                            BigDecimal subTotalBfeePaid = null;
                            BigDecimal subTotalHfeePaid = null;
                            BigDecimal subTotalFBase = null;
                            BigDecimal subTotalFBasePaid = null;
                            BigDecimal subTotalComm = null;
                            BigDecimal subTotalCommPaid = null;
                            BigDecimal subTotalCommNonFee = null;
                            BigDecimal subTotalCommPaidNonFee = null;
                            BigDecimal subTotalTaxComm = null;
                            BigDecimal subTotalTaxCommPaid = null;
                            BigDecimal subTotalTaxComm2 = null;
                            BigDecimal subTotalTaxCommPaid2 = null;
                            BigDecimal subTotalCommBfeeHfee = null;
                            BigDecimal subTotalCommBfeeHfeePaid = null;
                            
                            final DTOList arInvoiceDetail = view.getARInvoiceDetails();
                            for (int k = 0; k < arInvoiceDetail.size(); k++) {
                                ARInvoiceDetailView detil = (ARInvoiceDetailView) arInvoiceDetail.get(k);
                                
                                test = detil.getParentReceipt().getStARInvoiceID();
                                
                                if(detil.isCommission2()) {
                                    subTotalComm = BDUtil.add(subTotalComm,detil.getDbAmount());
                                    subTotalCommPaid = BDUtil.add(subTotalCommPaid,detil.getDbAmountSettled());
                                }
                                
                                if(detil.isBrokerage2()) {
                                    subTotalBfee = BDUtil.add(subTotalBfee,detil.getDbAmount());
                                    subTotalBfeePaid = BDUtil.add(subTotalBfeePaid,detil.getDbAmountSettled());
                                }
                                
                                if(detil.isHandlingFee2()) {
                                    subTotalHfee = BDUtil.add(subTotalHfee,detil.getDbAmount());
                                    subTotalHfeePaid = BDUtil.add(subTotalHfeePaid,detil.getDbAmountSettled());
                                }
                                
                                if(detil.isFeeBase3()) {
                                    subTotalFBase = BDUtil.add(subTotalFBase,detil.getDbAmount());
                                    subTotalFBasePaid = BDUtil.add(subTotalFBasePaid,detil.getDbAmountSettled());
                                }
                                
                                if(detil.isTaxComm()||detil.isTaxBrok()||detil.isTaxHFee()) {
                                    if (!Tools.isEqual(detil.getParentRefInvoiceDetail().getDbAmountSettled(), new BigDecimal(0))) {
                                        subTotalTaxComm = BDUtil.add(subTotalTaxComm,detil.getDbAmount());
                                        subTotalTaxCommPaid = BDUtil.add(subTotalTaxCommPaid,detil.getDbAmountSettled());
                                    } else {
                                        subTotalTaxComm = BDUtil.add(subTotalTaxComm,detil.getDbAmount());
                                        subTotalTaxCommPaid = BDUtil.add(subTotalTaxCommPaid,detil.getDbAmountSettled());
                                    }
                                }
                                
                                subTotalCommBfeeHfee = BDUtil.add(subTotalComm, subTotalBfee);
                                subTotalCommBfeeHfee = BDUtil.add(subTotalCommBfeeHfee, subTotalHfee);
                                
                                subTotalCommBfeeHfeePaid = BDUtil.add(subTotalCommPaid, subTotalBfeePaid);
                                subTotalCommBfeeHfeePaid = BDUtil.add(subTotalCommBfeeHfeePaid, subTotalHfeePaid);
                                
                                subTotalCommNonFee = BDUtil.sub(subTotalCommBfeeHfee, subTotalFBase);
                                subTotalCommPaidNonFee = BDUtil.sub(subTotalCommBfeeHfeePaid, subTotalFBasePaid);
                                
                                outstandingComm = BDUtil.sub(subTotalCommNonFee, subTotalCommPaidNonFee);
                                outstandingCommTax = BDUtil.sub(subTotalTaxComm, subTotalTaxCommPaid);
                                
                            }
                            
                            final DTOList arInvoiceDetail2 = view.getARInvoiceDetailsParentID(test);
                            for (int l = 0; l < arInvoiceDetail2.size(); l++) {
                                ARInvoiceDetailView detil2 = (ARInvoiceDetailView) arInvoiceDetail2.get(l);
                                
                                if(detil2.isPremiGross2()) {
                                    subTotalPremi = BDUtil.add(subTotalPremi,detil2.getDbAmount());
                                }
                                
                                if(detil2.isPolicyCost2()) {
                                    subTotalBPol = BDUtil.add(subTotalBPol,detil2.getDbAmount());
                                }
                                
                                if(detil2.isStampDuty2()) {
                                    subTotalBMat = BDUtil.add(subTotalBMat,detil2.getDbAmount());
                                }
                                
                                if(detil2.isDiscount2()) {
                                    subTotalDisc = BDUtil.add(subTotalDisc,detil2.getDbAmount());
                                }
                                
                                if(detil2.isCommission2()) {
                                    subTotalCommRef = BDUtil.add(subTotalCommRef,detil2.getDbAmount());
                                }
                                
                                if(detil2.isBrokerage2()) {
                                    subTotalBfee = BDUtil.add(subTotalBfee,detil2.getDbAmount());
                                }
                                
                                if(detil2.isHandlingFee2()) {
                                    subTotalHfee = BDUtil.add(subTotalHfee,detil2.getDbAmount());
                                }
                                
                                if(detil2.isFeeBase3()) {
                                    subTotalFBase = BDUtil.add(subTotalFBase,detil2.getDbAmount());
                                }
                                
                                if(detil2.isTaxComm()||detil2.isTaxBrok()||detil2.isTaxHFee()) {
                                    if (!Tools.isEqual(detil2.getParentRefInvoiceDetail().getDbAmountSettled(), new BigDecimal(0))) {
                                        subTotalTaxCommRef = BDUtil.add(subTotalTaxCommRef,detil2.getDbAmount());
                                    } else {
                                        subTotalTaxCommRef = BDUtil.add(subTotalTaxCommRef,detil2.getDbAmount());
                                    }
                                }
                                
                                subTotalBruto = BDUtil.add(subTotalPremi, BDUtil.add(subTotalBPol, subTotalBMat));
                                subTotalBruto = BDUtil.sub(subTotalBruto, subTotalDisc);
                                
                                //subTotalNetto = BDUtil.sub(subTotalCommRef, subTotalTaxCommRef);
                                subTotalNetto = BDUtil.add(subTotalCommRef, subTotalBfee);
                                subTotalNetto = BDUtil.add(subTotalNetto, subTotalHfee);
                                subTotalNetto = BDUtil.add(subTotalNetto, subTotalFBase);
                                subTotalNetto = BDUtil.sub(subTotalBruto, subTotalNetto);
                            }
                            
                            TotalPremi =  BDUtil.add(TotalPremi, subTotalPremi);
                            TotalBPol =  BDUtil.add(TotalBPol, subTotalBPol);
                            TotalBMat =  BDUtil.add(TotalBMat, subTotalBMat);
                            TotalDisc =  BDUtil.add(TotalDisc, subTotalDisc);
                            TotalComm =  BDUtil.add(TotalComm, subTotalComm);
                            TotalCommRef =  BDUtil.add(TotalCommRef, subTotalCommRef);
                            TotalTaxCommRef =  BDUtil.add(TotalTaxCommRef, subTotalTaxCommRef);
                            TotalFeeBase =  BDUtil.add(TotalFeeBase, subTotalFBase);
                            TotalCommNonFee =  BDUtil.add(TotalCommNonFee, subTotalCommNonFee);
                            TotalBfee =  BDUtil.add(TotalBfee, subTotalBfee);
                            TotalHfee =  BDUtil.add(TotalHfee, subTotalHfee);
                            TotalTaxComm =  BDUtil.add(TotalTaxComm, subTotalTaxComm);
                            TotalBruto =  BDUtil.add(TotalBruto, subTotalBruto);
                            TotalNetto =  BDUtil.add(TotalNetto, subTotalNetto);
                            TotalPaid = BDUtil.add(TotalPaid, subTotalPaid);
                            TotalCommPaid = BDUtil.add(TotalCommPaid, subTotalCommBfeeHfeePaid);
                            TotalTaxCommPaid = BDUtil.add(TotalTaxCommPaid, subTotalTaxCommPaid);
                            TotalOutstandingComm = BDUtil.add(TotalOutstandingComm, outstandingComm);
                            TotalOutstandingTaxComm = BDUtil.add(TotalOutstandingTaxComm, outstandingCommTax);                        
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="8pt"><%=j+1%>.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(view.getInvoice().getDtMutationDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(no_polis_cetak)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalDisc,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(BDUtil.add(subTotalBPol,subTotalBMat),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(BDUtil.add(subTotalCommRef, subTotalTaxCommRef),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalTaxCommRef,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalBfee,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalHfee,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalFBase,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalNetto,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(BDUtil.add(subTotalCommBfeeHfeePaid, subTotalTaxCommPaid),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(outstandingComm,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(outstandingCommTax,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="start" font-size="6pt"><%=JSPUtil.print(view.getInvoice().getEntity().getStEntityName())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                        }
                        %> 	
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="16">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt" number-columns-spanned="3"><fo:block text-align="center" font-size="8pt" >JUMLAH</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalDisc,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(BDUtil.add(TotalBPol,TotalBMat),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(BDUtil.add(TotalCommRef, TotalTaxCommRef),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalTaxCommRef,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalBfee,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalHfee,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalFeeBase,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalNetto,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(BDUtil.add(TotalCommPaid, TotalTaxCommPaid),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalOutstandingComm,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalOutstandingTaxComm,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-right-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="6pt" space-after.optimum="20pt">
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
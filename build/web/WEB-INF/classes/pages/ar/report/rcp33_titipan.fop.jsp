<%@ page import="com.webfin.ar.forms.ReceiptPembayaranRealisasiTitipanForm,
com.webfin.ar.forms.ReceiptForm,
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
                               page-width="35cm"
                               margin-top="0.75cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="1cm" margin-bottom="1.5cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>
    
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
                    <fo:table-column /><!-- Policy No -->
                    <fo:table-header> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-weight="bold" font-size="12pt" text-align="center">             
                                    LAPORAN PEMBAYARAN HUTANG KOMISI    
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
                                    <%if(receipt.getStAccountEntityID()!=null){%>
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
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Total Komisi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Pajak Komisi</fo:block></fo:table-cell>
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
                        BigDecimal TotalCommPaid = null;
                        BigDecimal TotalTaxComm = null;
                        BigDecimal TotalTaxCommPaid = null;
                        BigDecimal TotalOutstandingComm = null;
                        BigDecimal TotalOutstandingTaxComm = null;
                        String test = null;
                        
                        DTOList line = receipt.getDetails();
                        for(int j=0;j<line.size();j++){
                            ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);
                            
                            BigDecimal subTotalPremi = null;
                            BigDecimal subTotalBPol = null;
                            BigDecimal subTotalBMat = null;
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
                            BigDecimal subTotalComm = null;
                            BigDecimal subTotalCommPaid = null;
                            BigDecimal subTotalTaxComm = null;
                            BigDecimal subTotalTaxCommPaid = null;
                            BigDecimal subTotalTaxComm2 = null;
                            BigDecimal subTotalTaxCommPaid2 = null;
                            
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
                                
                                if(detil.isTaxComm()) {
                                    subTotalTaxComm = BDUtil.add(subTotalTaxComm,detil.getDbAmount());
                                    subTotalTaxCommPaid = BDUtil.add(subTotalTaxCommPaid,detil.getDbAmountSettled());
                                }
                                
                                /*
                                if(detil.isTaxComm()) {
                                    final DTOList arInvoiceTax = detil.getRefInvoice();
                                    for (int m = 0; m < arInvoiceTax.size(); m++) {
                                        ARInvoiceView invoiceTax = (ARInvoiceView) arInvoiceTax.get(m);
                                        
                                        subTotalTaxComm2 = BDUtil.add(subTotalTaxComm2,invoiceTax.getDbAmount());
                                        subTotalTaxCommPaid2 = BDUtil.add(subTotalTaxCommPaid2,invoiceTax.getDbAmountSettled());
                                        
                                    }
                                }*/
                                
                                
                                //subTotalComm = BDUtil.add(subTotalComm, subTotalTaxCommPaid);
                                //subTotalCommPaid = BDUtil.add(subTotalCommPaid, subTotalTaxComm);
                                
                                outstandingComm = BDUtil.sub(subTotalComm, subTotalCommPaid);
                                outstandingCommTax = BDUtil.sub(subTotalTaxComm2, subTotalTaxCommPaid2);
                                
                            }
                            
                            final DTOList arInvoiceDetail2 = view.getARInvoiceDetailsParentID(test);
                            for (int l = 0; l < arInvoiceDetail2.size(); l++) {
                                ARInvoiceDetailView detil2 = (ARInvoiceDetailView) arInvoiceDetail2.get(l);
                                
                                if(detil2.isPremiGross2()) {
                                    subTotalPremi = BDUtil.add(subTotalPremi,detil2.getDbAmount());
                                    subTotalPremiPaid = BDUtil.add(subTotalPremiPaid,detil2.getDbAmountSettled());
                                }
                                
                                if(detil2.isPolicyCost2()) {
                                    subTotalBPol = BDUtil.add(subTotalBPol,detil2.getDbAmount());
                                    subTotalBPolPaid = BDUtil.add(subTotalBPolPaid,detil2.getDbAmountSettled());
                                }
                                
                                if(detil2.isStampDuty2()) {
                                    subTotalBMat = BDUtil.add(subTotalBMat,detil2.getDbAmount());
                                    subTotalBMatPaid = BDUtil.add(subTotalBMatPaid,detil2.getDbAmountSettled());
                                }
                                
                                if(detil2.isDiscount2()) {
                                    subTotalDisc = BDUtil.add(subTotalDisc,detil2.getDbAmount());
                                    subTotalDiscPaid = BDUtil.add(subTotalDiscPaid,detil2.getDbAmountSettled());
                                }
                                
                                subTotalPaid = BDUtil.add(subTotalPremiPaid, BDUtil.add(subTotalBMatPaid, subTotalBPolPaid));
                                subTotalPaid = BDUtil.sub(subTotalPaid, subTotalDiscPaid);
                                subTotalPaid = BDUtil.sub(subTotalPaid, subTotalCommPaid);
                                
                                subTotalBruto = BDUtil.add(subTotalPremi, BDUtil.add(subTotalBPol, subTotalBMat));
                                subTotalBruto = BDUtil.sub(subTotalBruto, subTotalDisc);
                                
                                subTotalNetto = BDUtil.add(subTotalComm, subTotalTaxComm);
                                subTotalNetto = BDUtil.sub(subTotalBruto, subTotalNetto);
                            }
                            
                            TotalPremi =  BDUtil.add(TotalPremi, subTotalPremi);
                            TotalBPol =  BDUtil.add(TotalBPol, subTotalBPol);
                            TotalBMat =  BDUtil.add(TotalBMat, subTotalBMat);
                            TotalDisc =  BDUtil.add(TotalDisc, subTotalDisc);
                            TotalComm =  BDUtil.add(TotalComm, subTotalComm);
                            TotalBfee =  BDUtil.add(TotalBfee, subTotalBfee);
                            TotalHfee =  BDUtil.add(TotalHfee, subTotalHfee);
                            TotalTaxComm =  BDUtil.add(TotalTaxComm, subTotalTaxComm);
                            TotalBruto =  BDUtil.add(TotalBruto, subTotalBruto);
                            TotalNetto =  BDUtil.add(TotalNetto, subTotalNetto);
                            TotalPaid = BDUtil.add(TotalPaid, subTotalPaid);
                            TotalCommPaid = BDUtil.add(TotalCommPaid, subTotalCommPaid);
                            TotalTaxCommPaid = BDUtil.add(TotalTaxCommPaid, subTotalTaxCommPaid);
                            TotalOutstandingComm = BDUtil.add(TotalOutstandingComm, outstandingComm);
                            TotalOutstandingTaxComm = BDUtil.add(TotalOutstandingTaxComm, outstandingCommTax);
                        
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="8pt"><%=j+1%>.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(view.getInvoice().getDtMutationDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(view.getInvoice().getStAttrPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalDisc,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(BDUtil.add(subTotalBPol,subTotalBMat),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(BDUtil.add(subTotalComm, subTotalTaxComm),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalTaxComm,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalNetto,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(BDUtil.add(subTotalCommPaid, subTotalTaxCommPaid),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(outstandingComm,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(outstandingCommTax,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="start" font-size="6pt"><%=JSPUtil.xmlEscape(view.getPolicy().getStCustomerName())%></fo:block></fo:table-cell>
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
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalDisc,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(BDUtil.add(TotalBPol,TotalBMat),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(BDUtil.add(TotalComm, TotalTaxComm),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalTaxComm,0)%></fo:block></fo:table-cell>
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
            
            <%--
        </fo:flow>
        
    </fo:page-sequence>
    
    <fo:page-sequence master-reference="first">
    --%>
        
            <%
            
            ReceiptPembayaranRealisasiTitipanForm form2 =  (ReceiptPembayaranRealisasiTitipanForm) request.getAttribute("FORM");
            
            ARReceiptView receipt2 =  (ARReceiptView) request.getAttribute("RECEIPT");
            
            ArrayList colW2 = new ArrayList();
            
            colW2.add(new Integer(10));
            colW2.add(new Integer(50));
            colW2.add(new Integer(30));
            colW2.add(new Integer(40));
            colW2.add(new Integer(10));
            colW2.add(new Integer(40));
            colW2.add(new Integer(40));
            colW2.add(new Integer(30));
            colW2.add(new Integer(30));
            colW2.add(new Integer(90));
            
            //colW=FOPUtil.computeColumnWidth(colW,16,0," cm");
            //C041020203506100000
            //0123456789012345678
            
            String no_cetak2 = null;
            if (receipt2.getStReceiptNo2()!=null) {
                no_cetak2 = receipt2.getStReceiptNo2().substring(0,1) +"-"+ receipt2.getStReceiptNo2().substring(1,5)+
                        "-" + receipt2.getStReceiptNo2().substring(5,9) + "-" + receipt2.getStReceiptNo2().substring(9,14)+
                        "-" + receipt2.getStReceiptNo2().substring(14,16) +"-" + receipt2.getStReceiptNo2().substring(16,19);
            } else {
                no_cetak2 = receipt2.getStReceiptNo().substring(0,1) +"-"+ receipt2.getStReceiptNo().substring(1,5)+
                        "-" + receipt2.getStReceiptNo().substring(5,9) + "-" + receipt2.getStReceiptNo().substring(9,14)+
                        "-" + receipt2.getStReceiptNo().substring(14,16) +"-" + receipt2.getStReceiptNo().substring(16,19);
            }
            
            BigDecimal outstandingPremi2 = new BigDecimal(0);
            %>
            <%--
        <fo:flow flow-name="xsl-region-body">
        --%>
            
            <%
            String bw2 = "0.2pt";
            %>
            <fo:block font-weight="bold" text-align="center" line-height="5mm">REALISASI TITIPAN KOMISI</fo:block>
            <fo:block font-size="8pt" font-weight="bold" text-align="center" line-height="10mm">Tanggal Bayar : <%=JSPUtil.print(receipt2.getDtReceiptDate())%> </fo:block>
            <fo:block font-size="8pt" text-align="left" line-height="5mm" font-weight="bold">No Bukti : <%=no_cetak2%></fo:block>
            <%if(receipt2.getStAccountEntityID()!=null){%><fo:block text-align="left" line-height="5mm" font-weight="bold" font-size="8pt">Bank : <%=receipt2.getPaymentEntity().getStEntityName()%></fo:block><%}%>
            <fo:block  font-size="6pt" display-align="center" border-width="0.1pt">
                
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>" >
                    <fo:table-header>
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="<%=bw2%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw2%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No. Bukti Realisasi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw2%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Tanggal</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw2%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No. Bukti Titipan Premi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw2%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Ktr</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw2%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No. Rekening</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw2%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No. Polis</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw2%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Nilai</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw2%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Tanggal Entry</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw2%>" border-bottom-style="solid" padding="1pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Keterangan</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-header>
                    
                    <%=FOPUtil.printColumnWidth(colW2,31,1,"cm")%>
                    <fo:table-body>    
                        
                        <%
                        //final DTOList commissions = currentLine.getDetails();
                        BigDecimal [] t = new BigDecimal[1];
                        BigDecimal TotalPremi2 = new BigDecimal(0);
                        
                        DTOList line2 = receipt2.getTitipan();
                        for(int j=0;j<line2.size();j++){
                            ARReceiptLinesView view2 = (ARReceiptLinesView) line2.get(j);
                            
                            if (view2.getDbTitipanPremiUsedAmount()!=null) {
                                TotalPremi2 = view2.getDbTitipanPremiUsedAmount();
                            } else {
                                TotalPremi2 = BDUtil.sub(view2.getTitipanPremi().getDbAmount(), view2.getTitipanPremi().getDbBalance());
                            }
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], TotalPremi2);
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw2%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=j+1%>.</fo:block></fo:table-cell>
                            <% if (receipt2.getStReceiptNo2()!=null) { %>
                            <fo:table-cell border-width="<%=bw2%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(receipt2.getStReceiptNo2())%></fo:block></fo:table-cell>
                            <% } else { %>
                            <fo:table-cell border-width="<%=bw2%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(receipt2.getStReceiptNo())%></fo:block></fo:table-cell>
                            <% } %>
                            <fo:table-cell border-width="<%=bw2%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(receipt2.getDtReceiptDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw2%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(view2.getTitipanPremi().getStTransactionNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw2%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(view2.getTitipanPremi().getStCounter())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw2%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(view2.getTitipanPremi().getStHeaderAccountNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw2%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(view2.getTitipanPremi().getStPolicyNo())%></fo:block></fo:table-cell>
                            <% if (view2.getDbTitipanPremiUsedAmount()!=null) { %>
                            <fo:table-cell border-width="<%=bw2%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(view2.getDbTitipanPremiUsedAmount(),2)%></fo:block></fo:table-cell>
                            <% } else { %>
                            <fo:table-cell border-width="<%=bw2%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(BDUtil.sub(view2.getTitipanPremi().getDbAmount(), view2.getTitipanPremi().getDbBalance()),2)%></fo:block></fo:table-cell>
                            <% } %>
                            <fo:table-cell border-width="<%=bw2%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(receipt2.getDtReceiptDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw2%>" padding="2pt"><fo:block text-align="start" font-size="6pt"><%=JSPUtil.xmlEscape(receipt2.getEntity().getStEntityName())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        
                        <%
                        }
                        %> 	
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw2%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="7"><fo:block text-align="center" font-size="8pt" >JUMLAH</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw2%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw2%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="2"><fo:block text-align="end" font-size="8pt" ></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="6pt" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%>                 
                PrintCode:<%=receipt2.getStPrintCode()%> PrintStamp:<%=receipt2.getStPrintStamp()%>
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>
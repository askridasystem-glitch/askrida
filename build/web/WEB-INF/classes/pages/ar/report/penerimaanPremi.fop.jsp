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
    
    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">
        
        
        
        
        <%
        
        ReceiptForm form =  (ReceiptForm) request.getAttribute("FORM");
        
        ARReceiptView receipt =  (ARReceiptView) request.getAttribute("RECEIPT");
        
        ArrayList colW = new ArrayList();
        
        colW.add(new Integer(30));
        colW.add(new Integer(78));
        colW.add(new Integer(145));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(90));
        colW.add(new Integer(90));
        colW.add(new Integer(100));
        colW.add(new Integer(80));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(80));
        colW.add(new Integer(100));
        colW.add(new Integer(120));
        
        
        //colW=FOPUtil.computeColumnWidth(colW,16,0," cm");
        //C041020203506100000
        //0123456789012345678
        String no_cetak = receipt.getStReceiptNo().substring(0,1) +"-"+ receipt.getStReceiptNo().substring(1,5)+
                "-" + receipt.getStReceiptNo().substring(5,9) + "-" + receipt.getStReceiptNo().substring(9,14)+
                "-" + receipt.getStReceiptNo().substring(14,16) +"-" + receipt.getStReceiptNo().substring(16,19);
        
        BigDecimal outstandingPremi = new BigDecimal(0);
        BigDecimal outstandingComm = new BigDecimal(0);
        BigDecimal outstandingTax = new BigDecimal(0);
        %>
        
        
        <fo:flow flow-name="xsl-region-body">
            
            <%
            String bw = "0.2pt";
            %>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="5mm">DAFTAR PENERIMAAN PREMI</fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="10mm">Tanggal Entry : <%=JSPUtil.print(receipt.getDtReceiptDate())%> </fo:block>
            <fo:block font-family="Helvetica" font-size="8pt" text-align="left" line-height="5mm">No Bukti : <%=no_cetak%></fo:block>
            <%if(receipt.getStAccountEntityID()!=null){%><fo:block font-family="Helvetica" font-size="8pt" text-align="left" line-height="5mm">Bank : <%=receipt.getPaymentEntity().getStEntityName()%></fo:block><%}%>
            <fo:block font-family="Helvetica" font-size="6pt" display-align="center" border-width="0.1pt">
                
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>" >
                    <fo:table-header>
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm"   text-align="center" font-size="10pt" >No</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm"   text-align="center" font-size="10pt" >Tanggal</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >No Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >Premi Bruto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >Diskon</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >Biaya Polis Materai</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >Tagihan Bruto</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >Komisi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >H.Fee</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >B. Fee</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >Tagihan Netto</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >Tgl Byr</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >Jumlah</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >Komisi Dibayar</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >Hutang Komisi</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >Hutang Pajak</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >O/S Premi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >Keterangan</fo:block></fo:table-cell> 
                        </fo:table-row>
                        
                    </fo:table-header>
                    
                    <%=FOPUtil.printColumnWidth(colW,34,1,"cm")%>
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
                        BigDecimal TotalTaxComm = null;
                        BigDecimal TotalBruto = null;
                        BigDecimal TotalNetto = null;
                        BigDecimal TotalPaid = null;
                        BigDecimal TotalPremiPaid = null;
                        BigDecimal TotalCommPaid = null;
                        BigDecimal TotalCommDebt = null;
                        BigDecimal TotalTaxCommPaid = null;
                        
                        DTOList line = receipt.getDetails();
                        for(int j=0;j<line.size();j++){
                            ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);
                            
                            BigDecimal subTotalPremi = null;
                            BigDecimal subTotalBPol = null;
                            BigDecimal subTotalBMat = null;
                            BigDecimal subTotalDisc = null;
                            BigDecimal subTotalComm = null;
                            BigDecimal subTotalBfee = null;
                            BigDecimal subTotalHfee = null;
                            BigDecimal subTotalTaxComm = null;
                            BigDecimal subTotalBruto = null;
                            BigDecimal subTotalNetto = null;
                            BigDecimal subTotalPaid = null;
                            BigDecimal subTotalPremiPaid = null;
                            BigDecimal subTotalBPolPaid = null;
                            BigDecimal subTotalBMatPaid = null;
                            BigDecimal subTotalDiscPaid = null;
                            BigDecimal subTotalBfeePaid = null;
                            BigDecimal subTotalHfeePaid = null;
                            BigDecimal subTotalCommPaid = null;
                            BigDecimal subTotalTaxCommPaid = null;
                            
                            final DTOList arInvoiceDetail = view.getARInvoiceDetails();
                            for (int k = 0; k < arInvoiceDetail.size(); k++) {
                                ARInvoiceDetailView detil = (ARInvoiceDetailView) arInvoiceDetail.get(k);
                                
                                if(detil.isPremiGross2()) {
                                    subTotalPremi = BDUtil.add(subTotalPremi,detil.getDbAmount());
                                    subTotalPremiPaid = BDUtil.add(subTotalPremiPaid,detil.getDbAmountSettled());
                                }
                                
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
                                
                                if(detil.isPolicyCost2()) {
                                    subTotalBPol = BDUtil.add(subTotalBPol,detil.getDbAmount());
                                    subTotalBPolPaid = BDUtil.add(subTotalBPolPaid,detil.getDbAmountSettled());
                                }
                                
                                if(detil.isStampDuty2()) {
                                    subTotalBMat = BDUtil.add(subTotalBMat,detil.getDbAmount());
                                    subTotalBMatPaid = BDUtil.add(subTotalBMatPaid,detil.getDbAmountSettled());
                                }
                                
                                if(detil.isTaxComm()) {
                                    subTotalTaxComm = BDUtil.add(subTotalTaxComm,detil.getDbAmount());
                                    subTotalTaxCommPaid = BDUtil.add(subTotalTaxCommPaid,detil.getDbAmountSettled());
                                }
                                
                                if(detil.isDiscount2()) {
                                    subTotalDisc = BDUtil.add(subTotalDisc,detil.getDbAmount());
                                    subTotalDiscPaid = BDUtil.add(subTotalDiscPaid,detil.getDbAmountSettled());
                                }
                                
                                subTotalPaid = BDUtil.add(subTotalPremiPaid, BDUtil.add(subTotalBMatPaid, subTotalBPolPaid));
                                subTotalPaid = BDUtil.sub(subTotalPaid, subTotalDiscPaid);
                                subTotalPaid = BDUtil.sub(subTotalPaid, subTotalBfeePaid);
                                subTotalPaid = BDUtil.sub(subTotalPaid, subTotalHfeePaid);
                                subTotalPaid = BDUtil.sub(subTotalPaid, subTotalCommPaid);
                                subTotalPaid = BDUtil.sub(subTotalPaid, subTotalTaxCommPaid);
                                
                                outstandingPremi = BDUtil.sub(subTotalPremi, subTotalPremiPaid);
                                outstandingComm = BDUtil.sub(subTotalComm, subTotalCommPaid);
                                outstandingTax = BDUtil.sub(subTotalTaxComm, subTotalTaxCommPaid);
                                
                                subTotalBruto = BDUtil.add(subTotalPremi, BDUtil.add(subTotalBPol, subTotalBMat));
                                subTotalBruto = BDUtil.sub(subTotalBruto, subTotalDisc);
                                
                                subTotalNetto = BDUtil.add(subTotalComm, BDUtil.add(subTotalBfee, subTotalHfee));
                                subTotalNetto = BDUtil.add(subTotalNetto, subTotalTaxComm);
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
                            TotalPremiPaid = BDUtil.add(TotalPremiPaid, outstandingPremi);
                            TotalCommPaid = BDUtil.add(TotalCommPaid, subTotalCommPaid);
                            TotalCommDebt = BDUtil.add(TotalCommDebt, outstandingComm);
                            TotalTaxCommPaid = BDUtil.add(TotalTaxCommPaid, outstandingTax); 
                        
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=j+1%>.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="start" font-size="8pt"><%=JSPUtil.print(view.getInvoice().getDtMutationDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.print(view.getInvoice().getStAttrPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalDisc,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(BDUtil.add(subTotalBPol,subTotalBMat),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalBruto,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(BDUtil.add(subTotalComm, subTotalTaxComm),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalHfee,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalBfee,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalNetto,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.print(receipt.getDtReceiptDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalCommPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(outstandingComm,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(outstandingTax,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(outstandingPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="start" font-size="6pt"><%=JSPUtil.print(receipt.getEntity().getStEntityName())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                        }
                        %> 	
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="18">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="3"><fo:block text-align="center" font-size="8pt" >JUMLAH</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalDisc,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(BDUtil.add(TotalBPol,TotalBMat),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalBruto,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(BDUtil.add(TotalComm, TotalTaxComm),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalHfee,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalBfee,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalNetto,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalCommPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalCommDebt,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalTaxCommPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalPremiPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
        </fo:flow>
    </fo:page-sequence>
</fo:root>
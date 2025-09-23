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
                               page-width="36cm"
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
        colW.add(new Integer(165));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(110));
        colW.add(new Integer(100));
        
        //colW=FOPUtil.computeColumnWidth(colW,16,2," cm");
        %>
        
        <fo:flow flow-name="xsl-region-body">
            <%
            String bw = "0.2pt";
            %>
            
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">MONITORING PEMBAYARAN (PL 2)</fo:block>
            <fo:block space-after.optimum="14pt"/>
            <fo:block font-family="Helvetica" font-size="<%=fontsize%>" display-align="center" border-width="0.1pt">
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>" >
                    <fo:table-header>
                        <fo:table-row> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">No</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Tanggal</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">No Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Premi Bruto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Biaya Polis</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Biaya Materai</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Tagihan Bruto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Komisi A</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Pajak A</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Komisi B</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Pajak B</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Komisi C</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Pajak C</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Komisi D</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Pajak D</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Tagihan Netto</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Tanggal Bayar</fo:block></fo:table-cell> 
                        </fo:table-row>
                    </fo:table-header>
                    
                    <%=FOPUtil.printColumnWidth(colW,34,2,"cm")%>
                    <fo:table-body> 
                        
                        <%
                        //final DTOList commissions = currentLine.getDetails();
                        BigDecimal premiGrossJumlah = null;
                        BigDecimal commissionJumlah = null;
                        BigDecimal brokerageJumlah = null;
                        BigDecimal hfeeJumlah = null;
                        BigDecimal pcostJumlah = null;
                        BigDecimal stampdutyJumlah = null;
                        BigDecimal taxJumlah = null;
                        BigDecimal tagihBrutoJumlah = null;
                        BigDecimal tagihNettoJumlah = null;
                        BigDecimal komisi1Jumlah = null;
                        BigDecimal komisi2Jumlah = null;
                        BigDecimal komisi3Jumlah = null;
                        BigDecimal komisi4Jumlah = null;
                        BigDecimal komisiPlusTax1Jumlah = null;
                        BigDecimal komisiPlusTax2Jumlah = null;
                        BigDecimal komisiPlusTax3Jumlah = null;
                        BigDecimal komisiPlusTax4Jumlah = null;
                        BigDecimal tax1Jumlah = null;
                        BigDecimal tax2Jumlah = null;
                        BigDecimal tax3Jumlah = null;
                        BigDecimal tax4Jumlah = null;
                        
                        for(int j=0;j<line.size();j++){
                            ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);
                            
                            String ccy = view.getStCurrencyCode();
                            
                            BigDecimal premiGrossTotal = null;
                            BigDecimal commissionTotal = null;
                            BigDecimal brokerageTotal = null;
                            BigDecimal hfeeTotal = null;
                            BigDecimal pcostTotal = null;
                            BigDecimal stampdutyTotal = null;
                            BigDecimal taxTotal = null;
                            BigDecimal tagihBruto = null;
                            BigDecimal tagihNetto = null;
                            
                            BigDecimal komisiPlusTax1 = null;
                            BigDecimal komisiPlusTax2 = null;
                            BigDecimal komisiPlusTax3 = null;
                            BigDecimal komisiPlusTax4 = null;
                            
                            BigDecimal komisi1 = null;
                            BigDecimal komisi2 = null;
                            BigDecimal komisi3 = null;
                            BigDecimal komisi4 = null;
                            boolean cekKomisi1 = false;
                            boolean cekKomisi2 = false;
                            boolean cekKomisi3 = false;
                            boolean cekKomisi4 = false;
                            BigDecimal tax1 = null;
                            BigDecimal tax2 = null;
                            BigDecimal tax3 = null;
                            BigDecimal tax4 = null;
                            boolean cekTax1 = false;
                            boolean cekTax2 = false;
                            boolean cekTax3 = false;
                            boolean cekTax4 = false;
                            BigDecimal totalKomisiPlusTax = null;
                            
                            String taxCode = null;
                            String taxCode2 = null;
                            String taxCode3 = null;
                            
                            final DTOList arInvoiceDetail = view.getARInvoiceDetails();
                            for (int k = 0; k < arInvoiceDetail.size(); k++) {
                                ARInvoiceDetailView detil = (ARInvoiceDetailView) arInvoiceDetail.get(k);
                                
                                BigDecimal amount = detil.getDbAmount();
                                
                                if (detil.isCommission2())
                                    taxCode = detil.getTrxLine().getStTaxCode();
                                if (detil.isTaxBrok())
                                    taxCode2 = detil.getTrxLine().getStTaxCode();
                                if (detil.isTaxHFee())
                                    taxCode3 = detil.getTrxLine().getStTaxCode();
                                
                                if(detil.isPremiGross2())
                                    premiGrossTotal = BDUtil.add(premiGrossTotal,amount);
                                
                                if(detil.isCommission2())
                                    commissionTotal = BDUtil.add(commissionTotal,amount);
                                
                                if(detil.isBrokerage2())
                                    brokerageTotal = BDUtil.add(brokerageTotal,amount);
                                
                                if(detil.isHandlingFee2())
                                    hfeeTotal = BDUtil.add(hfeeTotal,amount);
                                
                                if(detil.isPolicyCost2())
                                    pcostTotal = BDUtil.add(pcostTotal,amount);
                                
                                if(detil.isStampDuty2())
                                    stampdutyTotal = BDUtil.add(stampdutyTotal,amount);
                                
                                if(detil.isTaxComm())
                                    taxTotal = BDUtil.add(taxTotal,amount);
                                
                                
                                if(detil.isCommission2()||detil.isBrokerage2()||detil.isHandlingFee2()){
                                    if (komisi1==null) {
                                        if(detil.getStTaxCode()!=null){
                                            if(detil.getStTaxCode().equalsIgnoreCase("2")||
                                                    detil.getStTaxCode().equalsIgnoreCase("5")||
                                                    detil.getStTaxCode().equalsIgnoreCase("8")){
                                                komisi1 = amount;
                                                continue;
                                            }
                                        } else {
                                            komisi1 = amount;
                                            continue;
                                        }
                                    }
                                }
                                
                                if(detil.isCommission2()||detil.isBrokerage2()||detil.isHandlingFee2()){
                                    if (komisi2==null) {
                                        if(detil.getStTaxCode()!=null){
                                            if(detil.getStTaxCode().equalsIgnoreCase("1")||
                                                    detil.getStTaxCode().equalsIgnoreCase("4")||
                                                    detil.getStTaxCode().equalsIgnoreCase("7")){
                                                komisi2 = amount;
                                                continue;
                                            }
                                        } else {
                                            komisi2 = amount;
                                            continue;
                                        }
                                    }
                                }
                                
                                if(detil.isCommission2()||detil.isBrokerage2()||detil.isHandlingFee2()){
                                    if (komisi3==null) {
                                        if(detil.getStTaxCode()!=null){
                                            if(detil.getStTaxCode().equalsIgnoreCase("1")||
                                                    detil.getStTaxCode().equalsIgnoreCase("4")||
                                                    detil.getStTaxCode().equalsIgnoreCase("7")){
                                                komisi3 = amount;
                                                continue;
                                            }
                                        } else {
                                            komisi3 = amount;
                                            continue;
                                        }
                                    }
                                }
                                
                                if(detil.isCommission2()||detil.isBrokerage2()||detil.isHandlingFee2()){
                                    if (komisi4==null) {
                                        if(detil.getStTaxCode()!=null){
                                            if(detil.getStTaxCode().equalsIgnoreCase("1")||
                                                    detil.getStTaxCode().equalsIgnoreCase("4")||
                                                    detil.getStTaxCode().equalsIgnoreCase("7")){
                                                komisi4 = amount;
                                                continue;
                                            }
                                        } else {
                                            komisi4 = amount;
                                            continue;
                                        }
                                    }
                                }
                                
                                if(detil.isTaxComm()||detil.isTaxBrok()||detil.isTaxHFee()){
                                    if (tax1==null) {
                                        if(detil.getTrxLine().getStTaxCode().equalsIgnoreCase("2")||
                                                detil.getTrxLine().getStTaxCode().equalsIgnoreCase("5")||
                                                detil.getTrxLine().getStTaxCode().equalsIgnoreCase("8")){
                                            tax1 = amount;
                                            continue;
                                        }
                                    }
                                }
                                
                                if(detil.isTaxComm()||detil.isTaxBrok()||detil.isTaxHFee()){
                                    if (tax2==null) {
                                        if(detil.getTrxLine().getStTaxCode().equalsIgnoreCase("1")||
                                                detil.getTrxLine().getStTaxCode().equalsIgnoreCase("4")||
                                                detil.getTrxLine().getStTaxCode().equalsIgnoreCase("7")){
                                            tax2 = amount;
                                            continue;
                                        }
                                    }
                                }
                                
                                if(detil.isTaxComm()||detil.isTaxBrok()||detil.isTaxHFee()){
                                    if (tax3==null) {
                                        if(detil.getTrxLine().getStTaxCode().equalsIgnoreCase("1")||
                                                detil.getTrxLine().getStTaxCode().equalsIgnoreCase("4")||
                                                detil.getTrxLine().getStTaxCode().equalsIgnoreCase("7")){
                                            tax3 = amount;
                                            continue;
                                        }
                                    }
                                }
                                
                                if(detil.isTaxComm()||detil.isTaxBrok()||detil.isTaxHFee()){
                                    if (tax4==null) {
                                        if(detil.getTrxLine().getStTaxCode().equalsIgnoreCase("1")||
                                                detil.getTrxLine().getStTaxCode().equalsIgnoreCase("4")||
                                                detil.getTrxLine().getStTaxCode().equalsIgnoreCase("7")){
                                            tax4 = amount;
                                            continue;
                                        }
                                    }
                                }
                                
                                komisiPlusTax1 = BDUtil.add(komisi1,tax1);
                                komisiPlusTax2 = BDUtil.add(komisi2,tax2);
                                komisiPlusTax3 = BDUtil.add(komisi3,tax3);
                                komisiPlusTax4 = BDUtil.add(komisi4,tax4);
                                totalKomisiPlusTax = BDUtil.add(totalKomisiPlusTax, komisiPlusTax1);
                                totalKomisiPlusTax = BDUtil.add(totalKomisiPlusTax, komisiPlusTax2);
                                totalKomisiPlusTax = BDUtil.add(totalKomisiPlusTax, komisiPlusTax3);
                                totalKomisiPlusTax = BDUtil.add(totalKomisiPlusTax, komisiPlusTax4);
                                
                                tagihBruto = BDUtil.add(BDUtil.add(premiGrossTotal,pcostTotal),stampdutyTotal);
                                
                                tagihNetto = BDUtil.sub(tagihBruto, totalKomisiPlusTax);
                            }
                            
                            premiGrossJumlah = BDUtil.add(premiGrossJumlah,premiGrossTotal);
                            commissionJumlah = BDUtil.add(commissionJumlah,commissionTotal);
                            brokerageJumlah = BDUtil.add(brokerageJumlah,brokerageTotal);
                            hfeeJumlah = BDUtil.add(hfeeJumlah,hfeeTotal);
                            pcostJumlah = BDUtil.add(pcostJumlah,pcostTotal);
                            stampdutyJumlah = BDUtil.add(stampdutyJumlah,stampdutyTotal);
                            taxJumlah = BDUtil.add(taxJumlah,taxTotal);
                            tagihBrutoJumlah = BDUtil.add(tagihBrutoJumlah,tagihBruto);
                            tagihNettoJumlah = BDUtil.add(tagihNettoJumlah,tagihNetto);
                            
                            komisi1Jumlah = BDUtil.add(komisi1Jumlah,komisi1);
                            komisi2Jumlah = BDUtil.add(komisi2Jumlah,komisi2);
                            komisi3Jumlah = BDUtil.add(komisi3Jumlah,komisi3);
                            komisi4Jumlah = BDUtil.add(komisi4Jumlah,komisi4);
                            
                            tax1Jumlah = BDUtil.add(tax1Jumlah,tax1);
                            tax2Jumlah = BDUtil.add(tax2Jumlah,tax2);
                            tax3Jumlah = BDUtil.add(tax3Jumlah,tax3);
                            tax4Jumlah = BDUtil.add(tax4Jumlah,tax4);
                            
                            komisiPlusTax1Jumlah = BDUtil.add(komisiPlusTax1Jumlah,BDUtil.add(komisi1,tax1));
                            komisiPlusTax2Jumlah = BDUtil.add(komisiPlusTax2Jumlah,BDUtil.add(komisi2,tax2));
                            komisiPlusTax3Jumlah = BDUtil.add(komisiPlusTax3Jumlah,BDUtil.add(komisi3,tax3));
                            komisiPlusTax4Jumlah = BDUtil.add(komisiPlusTax4Jumlah,BDUtil.add(komisi4,tax4));
                        %>
                        <fo:table-row >
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=j+1%>.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=JSPUtil.print(view.getInvoice().getDtMutationDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=JSPUtil.print(view.getInvoice().getStAttrPolicyNo().substring(0,4)+"-"+view.getInvoice().getStAttrPolicyNo().substring(4,8)+"-"+view.getInvoice().getStAttrPolicyNo().substring(8,12)+"-"+view.getInvoice().getStAttrPolicyNo().substring(12,16)+"-"+view.getInvoice().getStAttrPolicyNo().substring(16,18))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(premiGrossTotal,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(pcostTotal,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(stampdutyTotal,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(tagihBruto,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(komisi1, tax1),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(tax1,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(komisi2, tax2),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(tax2,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(komisi3, tax3),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(tax3,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(komisi4, tax4),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(tax4,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(tagihBruto, commissionTotal),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=JSPUtil.printX(view.getInvoice().getDtReceipt())%></fo:block></fo:table-cell>
                        </fo:table-row >
                        <%
                        }
                        %> 
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="17" > 
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row> 
                        <fo:table-row >
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">JUMLAH</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(premiGrossJumlah,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(pcostJumlah,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(stampdutyJumlah,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(tagihBrutoJumlah,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(BDUtil.add(komisi1Jumlah, tax1Jumlah),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(tax1Jumlah,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(BDUtil.add(komisi2Jumlah, tax2Jumlah),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(tax2Jumlah,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(BDUtil.add(komisi3Jumlah, tax3Jumlah),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(tax3Jumlah,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(BDUtil.add(komisi4Jumlah, tax4Jumlah),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(tax4Jumlah,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(BDUtil.sub(tagihBrutoJumlah, commissionJumlah),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"></fo:block></fo:table-cell>
                        </fo:table-row >
                    </fo:table-body>
                    
                </fo:table>
            </fo:block>
        </fo:flow>
    </fo:page-sequence>
</fo:root>


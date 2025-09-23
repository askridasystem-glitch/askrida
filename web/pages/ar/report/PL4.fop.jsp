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
                               page-width="35cm"
                               margin-top="1cm"
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
        
        String fontsize = form.getStFontSize();
        
        DTOList line = (DTOList) request.getAttribute("RPT");
        
        ArrayList colW = new ArrayList();
        
        colW.add(new Integer(25));
        colW.add(new Integer(75));
        colW.add(new Integer(160));
        colW.add(new Integer(120));
        colW.add(new Integer(100));
        colW.add(new Integer(80));
        colW.add(new Integer(80));
        colW.add(new Integer(110));
        colW.add(new Integer(110));
        colW.add(new Integer(100));
        colW.add(new Integer(110));
        colW.add(new Integer(100));
        colW.add(new Integer(100));
        colW.add(new Integer(150));
        colW.add(new Integer(80));
        
        //colW=FOPUtil.computeColumnWidth(colW,16,2," cm");
        
        
        %>
        
        
        <fo:flow flow-name="xsl-region-body">
            
            <%
            String bw = "0.2pt";
            %>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">MONITORING PEMBAYARAN (PL 4)</fo:block>
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
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Diskon</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Biaya Polis</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Biaya Materai</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Tagihan Bruto</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Komisi</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Pajak Komisi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Broker Fee</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Pajak Broker</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Handling Fee</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Tagihan Netto</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Tanggal Bayar</fo:block></fo:table-cell> 
                        </fo:table-row>
                    </fo:table-header>
                    <%=FOPUtil.printColumnWidth(colW,33,2,"cm")%>
                    <fo:table-body> 
                        <%
                        BigDecimal [] t = new BigDecimal[11];
                        
                        for(int j=0;j<line.size();j++){
                            ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);
                            
                            final DTOList arInvoice = view.getARInvoice();
                            for (int l = 0; l < arInvoice.size(); l++) {
                                ARInvoiceView invoice = (ARInvoiceView) arInvoice.get(l);
                                
                                BigDecimal subTotalBruto = null;
                                BigDecimal subTotalNettoTotal = null;
                                BigDecimal subTotalNettoJumlah = null;
                                BigDecimal subTotalCommBfee = null;
                                BigDecimal subTotalTaxCommBfee = null;
                                BigDecimal TotalDueJumlah = null;
                                BigDecimal TotalDueTotal = null;
                                BigDecimal TotalCommJumlah = null;
                                BigDecimal TotalBfeeJumlah = null;
                                
                                final DTOList arInvoiceDet = invoice.getARInvoiceDetailsCoins();
                                for (int m = 0; m < arInvoiceDet.size(); m++) {
                                    ARInvoiceView invoic = (ARInvoiceView) arInvoiceDet.get(m);
                                    
                                    int n=0;
                                    t[n] = BDUtil.add(t[n++], invoic.getDbAmount());
                                    t[n] = BDUtil.add(t[n++], invoic.getDbDisc());
                                    t[n] = BDUtil.add(t[n++], invoic.getDbPcost());
                                    t[n] = BDUtil.add(t[n++], invoic.getDbStampDuty());
                                    t[n] = BDUtil.add(t[n++], invoic.getDbCurrencyRate());
                                    t[n] = BDUtil.add(t[n++], invoic.getDbAttrPolicyTSITotal());
                                    t[n] = BDUtil.add(t[n++], invoic.getDbEnteredAmount());
                                    
                                    TotalDueJumlah = BDUtil.add(invoic.getDbPcost(), invoic.getDbStampDuty());
                                    TotalDueTotal = BDUtil.add(invoic.getDbAmount(), TotalDueJumlah);
                                    subTotalBruto = BDUtil.sub(TotalDueTotal, invoic.getDbDisc());
                                    t[n] = BDUtil.add(t[n++], subTotalBruto);
                                    
                                    TotalCommJumlah = BDUtil.add(invoic.getDbAmountSettled(), invoic.getDbCurrencyRate());
                                    t[n] = BDUtil.add(t[n++], TotalCommJumlah);
                                    
                                    TotalBfeeJumlah = BDUtil.add(invoic.getDbAttrPolicyTSI(), invoic.getDbAttrPolicyTSITotal());
                                    t[n] = BDUtil.add(t[n++], TotalBfeeJumlah);
                                    
                                    subTotalCommBfee = BDUtil.add(TotalCommJumlah, BDUtil.add(TotalBfeeJumlah, invoic.getDbEnteredAmount()));
                                    subTotalTaxCommBfee = BDUtil.add(invoic.getDbCurrencyRate(), invoic.getDbAttrPolicyTSITotal());
                                    
                                    subTotalNettoJumlah = BDUtil.add(subTotalBruto, subTotalTaxCommBfee);
                                    subTotalNettoTotal = BDUtil.sub(subTotalNettoJumlah, subTotalCommBfee);
                                    t[n] = BDUtil.add(t[n++], subTotalNettoTotal);
                                    
                        /*
                        if(j>0){
                        ARReceiptLinesView viewer = (ARReceiptLinesView) line.get(j-1);
                        String nopol = viewer.getInvoice().getStAttrPolicyNo();
                        String nopol2 = view.getInvoice().getStAttrPolicyNo();
                        if(!nopol.equalsIgnoreCase(nopol2)){
 
                        no++;
 
                        }
                        }
                         */
                        
                        
                        %>
                        <fo:table-row >
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=j+1%>.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=JSPUtil.print(view.getInvoice().getDtMutationDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=JSPUtil.print(view.getInvoice().getStAttrPolicyNo().substring(0,4)+"-"+view.getInvoice().getStAttrPolicyNo().substring(4,8)+"-"+view.getInvoice().getStAttrPolicyNo().substring(8,12)+"-"+view.getInvoice().getStAttrPolicyNo().substring(12,16)+"-"+view.getInvoice().getStAttrPolicyNo().substring(16,18))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbAmount(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbDisc(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbPcost(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbStampDuty(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(subTotalBruto,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(TotalCommJumlah,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbCurrencyRate(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(TotalBfeeJumlah,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbAttrPolicyTSITotal(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbEnteredAmount(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(subTotalNettoTotal,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center"><%=JSPUtil.printX(view.getInvoice().getDtReceipt())%></fo:block></fo:table-cell>
                        </fo:table-row >
                        <%
                        
                                }
                            }
                        }
                        %> 
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="15" > 
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row> 
                        
                        <fo:table-row >
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="3"><fo:block text-align="center" font-weight="bold">JUMLAH</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(t[2],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(t[3],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(t[7],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(t[8],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(t[4],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(t[9],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(t[5],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(t[6],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(t[10],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"></fo:block></fo:table-cell>
                        </fo:table-row >
                        
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
        </fo:flow>
    </fo:page-sequence>
</fo:root>


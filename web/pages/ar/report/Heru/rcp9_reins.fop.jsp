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
        colW.add(new Integer(100));
        colW.add(new Integer(80));
        
        //colW=FOPUtil.computeColumnWidth(colW,16,0," cm");
        //C041020203506100000
        //0123456789012345678
        String no_cetak = receipt.getStReceiptNo().substring(0,1) +"-"+ receipt.getStReceiptNo().substring(1,5)+
                "-" + receipt.getStReceiptNo().substring(5,9) + "-" + receipt.getStReceiptNo().substring(9,14)+
                "-" + receipt.getStReceiptNo().substring(14,16) +"-" + receipt.getStReceiptNo().substring(16,19);
        
        BigDecimal outstandingPremi = new BigDecimal(0);
        BigDecimal outstandingComm = new BigDecimal(0);
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
            
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="5mm">DAFTAR PEMBAYARAN RE-ASURANSI</fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="10mm">Periode Bayar : <%=DateUtil.getDateStr(receipt.getDtReceiptDate(), "^^ yyyy")%> </fo:block>
            <fo:block font-family="Helvetica" font-size="8pt" text-align="left" line-height="5mm">No Bukti : <%=no_cetak%></fo:block>
            <%if(receipt.getStAccountEntityID()!=null){%><fo:block font-family="Helvetica" font-size="8pt" text-align="left" line-height="5mm">Bank : <%=receipt.getPaymentEntity().getStEntityName()%></fo:block><%}%>
            <fo:block font-family="Helvetica" font-size="6pt" display-align="center" border-width="0.1pt">
                
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>" >
                    <fo:table-header>
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Tgl Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Premi R/I</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Komisi R/I</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Hfee R/I</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Bfee R/I</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Total Bayar</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Premi R/I Dibayar</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Keterangan</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-header>
                    
                    <%=FOPUtil.printColumnWidth(colW,34,1,"cm")%>
                    <fo:table-body>    
                        
                        <%
                        //final DTOList commissions = currentLine.getDetails();
                        BigDecimal TotalPremi = null;
                        BigDecimal TotalComm = null;
                        BigDecimal TotalBfee = null;
                        BigDecimal TotalHfee = null;
                        BigDecimal TotalPaid = null;
                        BigDecimal TotalPremiPaid = null;
                        
                        DTOList line = receipt.getDetails();
                        for(int j=0;j<line.size();j++){
                            ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);
                            
                            BigDecimal subTotalPremi = null;
                            BigDecimal subTotalComm = null;
                            BigDecimal subTotalBfee = null;
                            BigDecimal subTotalHfee = null;
                            BigDecimal subTotalPaid = null;
                            BigDecimal subTotalPremiPaid = null;
                            BigDecimal subTotalCommPaid = null;
                            BigDecimal subTotalHfeePaid = null;
                            BigDecimal subTotalBfeePaid = null;
                            
                            final DTOList arInvoiceDetail = view.getARInvoiceDetails();
                            for (int k = 0; k < arInvoiceDetail.size(); k++) {
                                ARInvoiceDetailView detil = (ARInvoiceDetailView) arInvoiceDetail.get(k);
                                
                                if(detil.isPremiGrossReas()) {
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
                                
                                subTotalPaid = BDUtil.sub(subTotalPremiPaid, subTotalBfeePaid);
                                subTotalPaid = BDUtil.sub(subTotalPaid, subTotalHfeePaid);
                                subTotalPaid = BDUtil.sub(subTotalPaid, subTotalCommPaid);
                                
                                outstandingPremi = BDUtil.sub(subTotalPremi, subTotalPremiPaid);
                            }
                            
                            TotalPremi =  BDUtil.add(TotalPremi, subTotalPremi);
                            TotalComm =  BDUtil.add(TotalComm, subTotalComm);
                            TotalBfee =  BDUtil.add(TotalBfee, subTotalBfee);
                            TotalHfee =  BDUtil.add(TotalHfee, subTotalHfee);
                            TotalPaid = BDUtil.add(TotalPaid, subTotalPaid);
                            TotalPremiPaid = BDUtil.add(TotalPremiPaid, outstandingPremi);                        
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=j+1%>.</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(view.getInvoice().getDtMutationDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(view.getInvoice().getStAttrPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalComm,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalHfee,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalBfee,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(subTotalPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(outstandingPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="start" font-size="6pt"><%=JSPUtil.print(receipt.getEntity().getStEntityName())%></fo:block></fo:table-cell>
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
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="3"><fo:block text-align="center" font-size="8pt" >JUMLAH</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalComm,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalHfee,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalBfee,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(TotalPremiPaid,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ></fo:block></fo:table-cell>
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
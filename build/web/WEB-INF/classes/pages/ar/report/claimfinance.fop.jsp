<%@ page import="com.webfin.ar.model.*, 
com.crux.util.*, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager,
java.math.BigDecimal, 
com.webfin.ar.forms.ReceiptForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ReceiptForm form = (ReceiptForm)SessionManager.getInstance().getCurrentForm();

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-height="21cm"
                               page-width="30cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
            <fo:region-before extent="0cm"/> 
            <fo:region-after extent="0cm"/> 
        </fo:simple-page-master> 
        
    </fo:layout-master-set> 
    <!-- end: defines page layout --> 
 
    <!-- actual layout --> 
    <fo:page-sequence master-reference="first"> 
        
        <!-- usage of page layout --> 
        <!-- header --> 
        <fo:static-content flow-name="xsl-region-before"> 
            
            <fo:block-container height="0cm" width="0cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="0cm" width="0cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="term"
                                        retrieve-boundary="page"
                                        retrieve-position="last-ending-within-page"/>
                </fo:block>
            </fo:block-container>
        </fo:static-content>
        
        <fo:static-content flow-name="xsl-region-after">
            
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>
        
        
        <fo:flow flow-name="xsl-region-body"> 
            
            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="30mm"/><!-- Policy Date--> 
                    <fo:table-column column-width="30mm"/><!-- Policy No -->
                    <fo:table-column column-width="30mm"/><!-- Policy No -->
                    <fo:table-column column-width="20mm"/><!-- Biaya Polis --> 
                    <fo:table-column column-width="20mm"/><!-- The Insured -->
                    <fo:table-column column-width="50mm"/><!-- Premium --> 
                    <fo:table-column column-width="20mm"/><!-- Biaya Polis --> 
                    <fo:table-column column-width="20mm"/><!-- Biaya Materai -->
                    <fo:table-column /><!-- Entry Date -->
                    <fo:table-header>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">             
                                    LAPORAN PENGAJUAN KLAIM UNTUK AKUNTANSI    
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block  font-weight="bold" font-size="18pt" text-align="center">                                    
                                    <% if (form.getStReceiptNo()!=null) { %>
                                    Nomor : <%= JSPUtil.printX(form.getStReceiptNo()) %>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center">No</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">No. LKP</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">No. Polis</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Nama Tertanggung</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">Tgl Setujui</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">Nilai Klaim</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">No. Rekap</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">Tgl Bayar</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">Jumlah Bayar</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">Keterangan</fo:block></fo:table-cell>  
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <!-- GARIS  -->  
                        <!--   \<fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt"></fo:block> -->  
     
                        
                        <%   
                        //int counter = 20;
                        String ket = null;
                        String nilaiclaimco = null;
                        BigDecimal subTotalClaim = new BigDecimal(0);
                        BigDecimal subTotalBayar = new BigDecimal(0);
                        BigDecimal subTotalClaimCo = new BigDecimal(0);
                        
                        BigDecimal [] t = new BigDecimal[3];
                        
                        for (int i = 0; i < l.size(); i++) {
                            ARInvoiceView pol = (ARInvoiceView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbAmount());
                            t[n] = BDUtil.add(t[n++], pol.getDbAmountSettled());
                            t[n] = BDUtil.add(t[n++], pol.getDbEnteredAmount());
                            
                            if (pol.getDtDueDate()!=null)
                                ket = "Perubahan OR " + DateUtil.getDateStr7(pol.getDtDueDate());
                            else
                                ket = "";
                            
                            if (pol.getDbEnteredAmount()!=null)
                                nilaiclaimco = JSPUtil.printX(pol.getDbEnteredAmount(),2);
                            else
                                nilaiclaimco = "";
                            
                            //if (i==counter) {
                            //    counter = counter + 20;
                        
                        %>  
                        <%--
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell number-columns-spanned="4"><fo:block text-align="center">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalClaim,0)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalBayar,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                        subTotalClaim = null;
                        subTotalBayar = null;
                            }%>
                            --%>
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStRefID2())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStAttrPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStAttrPolicyName())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtMutationDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbAmount(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStReferenceX0())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtReceipt())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbAmountSettled())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(ket)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <%--
                        <% 
                        subTotalClaim = BDUtil.add(subTotalClaim,pol.getDbAmount());
                        subTotalBayar = BDUtil.add(subTotalBayar,pol.getDbAmountSettled());
                        } %>   
                        --%>
                        
                        <% } %>
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10">  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5"><fo:block> TOTAL : </fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0],0)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1],0)%></fo:block></fo:table-cell><!-- Total Fee --> 
                            <fo:table-cell ></fo:table-cell>    
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                    </fo:table-body>   
                </fo:table>   
            </fo:block> 
            
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block>          
            
            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[0],0)%>" orientation="0">
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
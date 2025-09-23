<%@ page import="com.webfin.gl.form.GLListForm,
com.crux.web.controller.SessionManager,
java.util.ArrayList,
com.crux.util.fop.FOPUtil,
com.crux.util.SQLAssembler,
com.webfin.gl.model.JournalView,
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
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            
            <fo:region-body margin-top="1.5cm" margin-bottom="1.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>
    
    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">
        
        <% 
        
        final DTOList l = (DTOList)request.getAttribute("RPT");
        
        final GLListForm form = (GLListForm)SessionManager.getInstance().getCurrentForm();
        
        %> 
        
        <fo:flow flow-name="xsl-region-body">
            
            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-header> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="14pt" text-align="center">             
                                    DAFTAR MUTASI   
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="12pt" text-align="center">             
                                    <%=JSPUtil.printX(form.getStSettlementDesc().toUpperCase())%>   
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="12pt" text-align="center">
                                    <% if (form.getApplyDateFrom()!=null || form.getApplyDateTo()!=null) {%> 
                                    Tanggal : <%=JSPUtil.printX(form.getApplyDateFrom())%> S/D <%=JSPUtil.printX(form.getApplyDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="10pt" text-align="start">
                                    <% if (form.getStReceiptNo()!=null) {%> 
                                    No. Bukti : <%=JSPUtil.printX(form.getStReceiptNo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="10pt" text-align="start">
                                    <% if (form.getStAccountNo()!=null) {%> 
                                    No. Rekening : <%=JSPUtil.printX(form.getStAccountNo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">NO</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">TRANS#</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">TANGGAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">NOREK</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">DEBET</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-bottom-style="solid" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">KREDIT</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <%
                        int pn = 0;
                        int norut = 0;
                        
                        BigDecimal debitJumlah = null;
                        BigDecimal creditJumlah = null;
                        
                        BigDecimal [] t = new BigDecimal[2];
                        
                        for (int i = 0; i < l.size(); i++) {
                            JournalView jou = (JournalView) l.get(i);
                            
                            norut++;
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], jou.getDbDebit());
                            t[n] = BDUtil.add(t[n++], jou.getDbCredit());
                            
                            if(i>0){
                                JournalView jou2 = (JournalView) l.get(i-1);
                                String journal = jou.getStTransactionNo();
                                String journal2 = jou2.getStTransactionNo();
                                if(!journal.equalsIgnoreCase(journal2)){
                                    pn++;
                                    
                                    norut = 1;
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid" padding="2pt" border-left-style="solid" number-columns-spanned="5"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">SUBTOTAL MUTASI </fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block  background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(debitJumlah,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block  background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(creditJumlah,2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                        <%--
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        --%>
                        <%                        
                        debitJumlah = null;
                        creditJumlah = null;
                                }
                            }
                            
                            //if (i==l.size()) {
                        %>
                        
                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block text-align="center"><%=String.valueOf(norut)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid" padding="2pt"><fo:block text-align="center"><%=JSPUtil.printX(jou.getStTransactionNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid" padding="2pt"><fo:block text-align="center"><%=JSPUtil.printX(jou.getDtApplyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid" padding="2pt"><fo:block text-align="center"><%=JSPUtil.printX(jou.getStCreateWho())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid" padding="2pt"><fo:block><%=JSPUtil.printX(jou.getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid" padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(jou.getDbDebit(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid" padding="2pt" border-right-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(jou.getDbCredit(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% 
                        debitJumlah = BDUtil.add(debitJumlah, jou.getDbDebit());
                        creditJumlah = BDUtil.add(creditJumlah, jou.getDbCredit());
                        } 
                        %>  
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid" padding="2pt" border-left-style="solid" number-columns-spanned="5"><fo:block  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">SUBTOTAL MUTASI </fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block  background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(debitJumlah,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-bottom-style="solid" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block  background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(creditJumlah,2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>      
                        
                        <fo:table-row>
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-bottom-style="solid" padding="2pt" border-left-style="solid" number-columns-spanned="5"><fo:block  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block  background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.print(t[0],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-top-style="solid" border-bottom-style="solid" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block  background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.print(t[1],2)%></fo:block></fo:table-cell>
                        </fo:table-row>		 
                        
                    </fo:table-body>
                </fo:table>  
            </fo:block> 
            
            <fo:block font-weight="bold" text-align="start" font-size="8pt">
                IT-08-10-2005 KODASI '98
            </fo:block>
            
            <fo:block font-weight="bold" text-align="start" font-size="8pt">
                Print Date: <%=JSPUtil.printDateTime(new Date())%>
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>
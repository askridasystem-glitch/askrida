<%@ page import="com.webfin.gl.util.GLUtil,
com.webfin.gl.ejb.GLReportEngine2,
com.webfin.gl.report2.form.FinReportForm,
java.util.Date,
java.math.BigDecimal,
com.crux.ff.FlexTableManager,
com.crux.ff.model.FlexTableView,
java.util.HashMap,
com.webfin.gl.model.GLInfoView,
com.crux.util.*,
java.util.ArrayList,
com.crux.lang.LanguageManager,
com.webfin.gl.model.JournalView,
com.crux.util.fop.FOPUtil"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="21cm"
                               page-width="30cm"
                               margin-top="1cm"
                               margin-bottom="0cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="0cm" margin-bottom="2cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="1cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>
    
    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first" initial-page-number="1">
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
        
        <%
        GLReportEngine2 glr = new GLReportEngine2();
        
        FinReportForm form = (FinReportForm) request.getAttribute("FORM");
        
        glr.setBranch(form.getBranch());
        glr.setStAccountNo(form.getStAccountNo());
        glr.setAppDateFrom(form.getDtApplyDateFrom());
        glr.setAppDateTo(form.getDtApplyDateTo());
        
        String periodBalFrom = DateUtil.getMonth2Digit(form.getDtApplyDateFrom()).toString();
        String periodBalTo = DateUtil.getMonth2Digit(form.getDtApplyDateTo()).toString();
        String yearBalPeriod = DateUtil.getYear(form.getDtApplyDateFrom()).toString();
        
        if (periodBalFrom.length()>1) {
            periodBalFrom = periodBalFrom;
        } else {
            periodBalFrom = periodBalFrom.substring(1,1);
        }
        
        if (periodBalTo.length()>1) {
            periodBalTo = periodBalTo;
        } else {
            periodBalTo = periodBalTo.substring(1,1);
        }
        
        long lperiodFrom = Long.parseLong(periodBalFrom);
        long lperiodTo = Long.parseLong(periodBalTo);
        long lYearPeriod = Long.parseLong(yearBalPeriod);
        
        BigDecimal a1 = glr.getCashBankSummaryAcctBal("BAL|ADD=1",form.getStAccountNo().substring(0,5),1,lperiodFrom-1,lYearPeriod);
        
        ArrayList cmap = new ArrayList();
        
        cmap.add(new Integer(10));
        cmap.add(new Integer(20));
        cmap.add(new Integer(40));
        cmap.add(new Integer(30));
        cmap.add(new Integer(90));
        cmap.add(new Integer(30));
        cmap.add(new Integer(30));
        cmap.add(new Integer(30));
        
        ArrayList cw = FOPUtil.computeColumnWidth(cmap,28,7,"cm");        
        
        String lSort = null;
        if (form.isClAccNo()) {
            lSort = "b.accountno,a.trx_no,a.applydate";
        } else if (form.isClDate()) {
            lSort = "a.applydate,a.trx_no,b.accountno";
        } else if (form.isClTrxNo()) {
            lSort = "a.trx_no,b.accountno,a.applydate";
        }
        
        %>
        
        
        <fo:flow flow-name="xsl-region-body">
            
            <%
            String bw = "0.5pt";
            %>
            
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" font-size="10pt">BUKU : <%=JSPUtil.printX(form.getStDescription())%> </fo:block>  
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" font-size="10pt">
                Untuk Bulan : <%=DateUtil.getDateStr(form.getDtApplyDateFrom(),"^^ yyyy")%>
                <% if (!DateUtil.getMonth(form.getDtApplyDateFrom()).equalsIgnoreCase(DateUtil.getMonth(form.getDtApplyDateTo()))) { %>
                s/d <%=DateUtil.getDateStr(form.getDtApplyDateTo(),"^^ yyyy")%>
                <% } %>
            </fo:block>
            <fo:block space-after.optimum="14pt"/>
            <fo:block font-family="Helvetica" font-weight="bold" font-size="10pt"><%=JSPUtil.printX(form.getBranchDesc())%></fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" font-size="10pt"><%=JSPUtil.printX(form.getStAccountNo())%></fo:block>
            
            <fo:block font-family="Helvetica" font-size="8pt" display-align="center" border-width="0.1pt">
                
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>">
                    <%
                    for (int i = 0; i < cw.size(); i++) {
            String cwx = (String) cw.get(i);
                    %>
                    <fo:table-column column-width="<%=cwx%>"/>
                    <%
                    }
                    %>
                    
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">NO</fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">TANGGAL</fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">TRANSAKSI</fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">AKUN</fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">URAIAN</fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">DEBIT</fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">KREDIT</fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">SALDO</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>          
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" font-style="italic" font-weight="bold" start-indent="5mm">Saldo Awal</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block start-indent="10mm">.........</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(a1,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        
                        <fo:table-row>          
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" font-style="italic" font-weight="bold" start-indent="5mm">Keterangan</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end" ></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        
                        <%
                        DTOList b1 = glr.getCashBankGLJeDetails("BAL|ADD=1",form.getStAccountNo().substring(0,5),form.getStAccountNo().substring(0,5),lperiodFrom,lperiodTo,lYearPeriod,lSort);
                        
                        BigDecimal b2 = null;
                        BigDecimal b3 = null;
                        BigDecimal b4 = null;
                        BigDecimal c1 = new BigDecimal(0);
                        BigDecimal c2 = new BigDecimal(0);
                        
                        for (int j = 0; j < b1.size(); j++) {
                            JournalView gli = (JournalView) b1.get(j);
                            
                            if(BDUtil.isZeroOrNull(gli.getDbCredit())) b2 = BDUtil.sub(gli.getDbCredit(), gli.getDbDebit());
                            else b2 = gli.getDbCredit();
                            
                            b3 = BDUtil.add(b3, gli.getDbDebit());
                            b4 = BDUtil.add(b4, gli.getDbCredit());
                            c1 = BDUtil.add(c1, b2);
                            
                            if (j == 0) {
                                c2 = BDUtil.add(c2, a1);
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="center"><%=String.valueOf(j+1)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="center"><%=JSPUtil.printX(gli.getDtApplyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="center"><%=gli.getStTransactionNo()%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="center"><%=gli.getStAccountNo()%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block start-indent="10mm"><%=LanguageManager.getInstance().translate(JSPUtil.xmlEscape(gli.getStDescription()))%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(gli.getDbCredit(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(gli.getDbDebit(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(c2, b2),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } else if (j > 0) { %>
                        <fo:table-row>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="center"><%=String.valueOf(j+1)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="center"><%=JSPUtil.printX(gli.getDtApplyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="center"><%=gli.getStTransactionNo()%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="center"><%=gli.getStAccountNo()%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block start-indent="10mm"><%=LanguageManager.getInstance().translate(JSPUtil.xmlEscape(gli.getStDescription()))%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(gli.getDbCredit(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(gli.getDbDebit(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(c1, c2),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <%
                        }
                        }
                        %>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="8" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" border-left-style="solid"  padding="2pt"><fo:block text-align="center" font-weight="bold">SALDO <% if (form.getDtApplyDateTo()!=null) { %>PER : <% } %> <%=DateUtil.getDateStr(form.getDtApplyDateTo(),"dd ^^ yyyy")%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(BDUtil.add(b4, a1),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(b3,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(BDUtil.add(c1, a1),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>                
            </fo:block> 
            
            <fo:block font-weight="bold" text-align="start" font-size="8pt" space-after.optimum="20pt">
                Print Date: <%=JSPUtil.printDateTime(new Date())%>
            </fo:block>   
            
            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(BDUtil.add(c1, a1),2)%>" orientation="0">
                        <barcode:datamatrix> 
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>                                    
            </fo:block>
            
            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                        <svg xmlns="http://www.w3.org/2000/svg" width="15cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
            
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>
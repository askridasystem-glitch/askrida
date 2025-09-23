<%@ page import="com.webfin.insurance.model.*, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionClaimReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionClaimReportForm form = (ProductionClaimReportForm)SessionManager.getInstance().getCurrentForm();

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="only" 
                               page-height="21cm"
                               page-width="30cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            
            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/> 
            <fo:region-after extent="0.5cm"/> 
        </fo:simple-page-master> 
        
    </fo:layout-master-set> 
    
    <fo:page-sequence master-reference="only" initial-page-number="1"> 
        
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
                    <fo:table-column column-width="20mm"/><!-- Entry Date --> 
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Police Date -->
                    <fo:table-column column-width="20mm"/><!-- Police Date -->
                    <fo:table-column column-width="20mm"/><!-- Police Date -->
                    <fo:table-column column-width="20mm"/><!-- Police Date -->
                    <fo:table-column column-width="40mm"/><!-- Police Date -->
                    <fo:table-column column-width="20mm"/><!-- Police Date -->
                    <fo:table-column column-width="20mm"/><!-- Police Date -->
                    <fo:table-column column-width="30mm"/><!-- Policy No. -->
                    <fo:table-column /><!-- Policy No. -->
                    <fo:table-header>   
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">             
                                    LAPORAN KLAIM UNTUK AKUNTANSI PER JENIS
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block font-family="serif" font-weight="bold" font-size="18pt" text-align="center">
                                    <% if (form.getAppDateFrom()!=null || form.getAppDateTo()!=null) {%> 
                                    Tanggal : <%=JSPUtil.printX(form.getAppDateFrom())%> S/D <%=JSPUtil.printX(form.getAppDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block font-weight="bold">
                                    <% if (form.getStBranch()!=null) { %>
                                    Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block font-weight="bold">
                                    <% if (form.getStGroupID()!=null) {%> 
                                    Group Polis : <%=JSPUtil.printX(form.getStGroupName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center">no</fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">jenpol</fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">beban klaim</fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">recovery klaim</fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">uw lain</fo:block></fo:table-cell><!-- No -->
                            <fo:table-cell ><fo:block text-align="center">ppn</fo:block></fo:table-cell><!-- No -->
                            <fo:table-cell ><fo:block text-align="center">adjuster fee</fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">nama adjuster fee</fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">jasa bengkel</fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">pajak bengkel</fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">nilai koas</fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center">koasuradur</fo:block></fo:table-cell><!-- No --> 
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header> 
                    
                    <fo:table-body>
                        
                        <%     
                        
                        BigDecimal [] t = new BigDecimal[8];
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm1());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm2());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm3());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm4());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDBrok1());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDBrok2());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiBase());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDPPN());
                        
                        %>   
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=i+1%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStReference1())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm1(),2)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm2(),2)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm3(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDPPN(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm4(),2)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStReference3())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDBrok1(),2)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDBrok2(),2)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiBase(),2)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCoinsName())%></fo:block></fo:table-cell> 
                        </fo:table-row>   
                        
                        <%
                        }
                        %>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>   
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell><!-- Total Sum Insured --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell><!-- Claim Estimated --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2],2)%></fo:block></fo:table-cell><!-- Claim Approved -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[7],2)%></fo:block></fo:table-cell><!-- Claim Approved -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3],2)%></fo:block></fo:table-cell><!-- Claim Approved --> 
                            <fo:table-cell ></fo:table-cell><!-- Name of Insured -->   
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[4],2)%></fo:block></fo:table-cell><!-- Claim Approved -->  
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[5],2)%></fo:block></fo:table-cell><!-- Claim Approved -->  
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[6],2)%></fo:block></fo:table-cell><!-- Claim Approved --> 
                            <fo:table-cell ></fo:table-cell><!-- Name of Insured -->  
                        </fo:table-row>   
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>   
                        
                    </fo:table-body>   
                </fo:table>   
            </fo:block> 
            
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"dd-MMM-yyyy hh:mm:ss")%>  
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
            
            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                        <svg xmlns="http://www.w3.org/2000/svg" width="5cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
        </fo:flow>   
        
    </fo:page-sequence>   
</fo:root>   

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
                               page-height="30cm"
                               page-width="25cm"
                               margin-top="2cm"
                               margin-bottom="2cm"
                               margin-left="2cm"
                               margin-right="2.5cm">
            <fo:region-body margin-top="2cm" margin-bottom="1.5cm"/> 
            <fo:region-before extent="3cm"/> 
            <fo:region-after extent="1.5cm"/> 
        </fo:simple-page-master> 
        
    </fo:layout-master-set>
    
    
    <fo:page-sequence master-reference="only"> 
        
        <fo:flow flow-name="xsl-region-body"> 
            
            <fo:block font-size="10pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-header>   
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-weight="bold" font-size="20pt" text-align="center">             
                                    {L-ENG RECAPITULATION PRODUCTION CLAIM PER BRANCH-L}{L-INA REKAPITULASI PRODUKSI KLAIM PER CABANG-L}
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block text-align="center">
                                    <% if (form.getAppDateFrom()!=null || form.getAppDateTo()!=null) { %> 
                                    Tanggal Disetujui : <%=JSPUtil.printX(form.getAppDateFrom())%> S/D <%=JSPUtil.printX(form.getAppDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block text-align="center">
                                    <% if (form.getClaimDateFrom()!=null || form.getClaimDateTo()!=null) { %> 
                                    Tanggal Klaim : <%=JSPUtil.printX(form.getClaimDateFrom())%> S/D <%=JSPUtil.printX(form.getClaimDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block text-align="center">
                                    <% if (form.getDLADateFrom()!=null || form.getDLADateTo()!=null) { %> 
                                    Tanggal LKP : <%=JSPUtil.printX(form.getDLADateFrom())%> S/D <%=JSPUtil.printX(form.getDLADateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block>
                                    <% if (form.getStPolicyTypeGroupDesc()!=null) { %> 
                                    Group Jenis Penutupan : <%=JSPUtil.printX(form.getStPolicyTypeGroupDesc())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block>
                                    <% if (form.getStPolicyTypeDesc()!=null) { %> 
                                    Jenis Penutupan : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block text-align="center">{L-ENG NO. -L}{L-INA NO. -L}</fo:block></fo:table-cell><!-- No --> 
                            <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block text-align="center">{L-ENG BRANCH -L}{L-INA DAERAH -L}</fo:block></fo:table-cell><!-- Entry Date --> 
                            <fo:table-cell number-columns-spanned="4" ><fo:block text-align="center">{L-ENG CATEGORY -L}{L-INA SUMBER BISNIS -L}</fo:block></fo:table-cell><!-- Police Date --> 
                            <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block text-align="center">{L-ENG SUBTOTAL -L}{L-INA JUMLAH -L}</fo:block></fo:table-cell><!-- Policy No. --> 
                        </fo:table-row>   
                        
                        <fo:table-row> 
                            <fo:table-cell ><fo:block text-align="end">UMUM</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end">PEMDA</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end">PERUSDA</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end">BPD</fo:block></fo:table-cell> 
                        </fo:table-row> 
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header> 
                    
                    <fo:table-body>
                        
                        <%                         
                        BigDecimal [] t = new BigDecimal[5];
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm1());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm2());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm3());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm4());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiTotalAfterDisc());
                        %>   
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=i+1%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(pol.getCostCenter(pol.getStCostCenterCode()).getStDescription())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm1(),0)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm2(),0)%></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm3(),0)%></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm4(),0)%></fo:block></fo:table-cell>   
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotalAfterDisc(),0)%></fo:block></fo:table-cell>  
                        </fo:table-row>   
                        
                        <% } %>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>TOTAL </fo:block></fo:table-cell><!-- Name of Insured --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0],0)%></fo:block></fo:table-cell><!-- Total Sum Insured --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1],0)%></fo:block></fo:table-cell><!-- Claim Estimated --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2],0)%></fo:block></fo:table-cell><!-- Claim Approved -->  
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3],0)%></fo:block></fo:table-cell><!-- Claim Estimated --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[4],0)%></fo:block></fo:table-cell><!-- Claim Approved -->  
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="7" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
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

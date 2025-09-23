<%@ page import="com.webfin.insurance.model.*,
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 

<%
final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionReportForm form = (ProductionReportForm)SessionManager.getInstance().getCurrentForm();

BigDecimal IDRPremi = null;
BigDecimal IDRPremiReas = null;
BigDecimal IDRCommReas = null;
BigDecimal IDRNettoReas = null;

BigDecimal nonIDRPremi = null;
BigDecimal nonIDRPremiReas = null;
BigDecimal nonIDRCommReas = null;
BigDecimal nonIDRNettoReas = null;
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-width="30cm"
                               page-height="21cm"
                               margin-top="0.5cm"
                               margin-bottom="1cm"
                               margin-left="3cm"
                               margin-right="4.5cm">
            <fo:region-body margin-top="2cm" margin-bottom="1cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
        <fo:static-content flow-name="xsl-region-before"> 
            <fo:block-container height="1cm" width="2cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="1cm" width="2cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="term"
                                        retrieve-boundary="page"
                                        retrieve-position="last-ending-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>
        
        
        <fo:flow flow-name="xsl-region-body">
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="200mm"/>
                    <fo:table-body>
                        
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-size="12pt" line-height="12pt" text-align="center" space-after.optimum="5pt">	 
                                    LAPORAN KHUSUS <% if(form.getStFltTreatyTypeDesc()!=null) { %>
                                    <%=JSPUtil.printX(form.getStFltTreatyTypeDesc())%><% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-size="12pt" line-height="12pt" text-align="center" space-after.optimum="5pt">	 
                                    BULAN : <%=DateUtil.getDateStr(form.getPolicyDateFrom(),"^^ yyyy")%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-size="12pt" line-height="12pt" text-align="center" space-after.optimum="5pt">	 
                                    TREATY YEAR : <% if(form.getPeriodFrom()!=null){ %>
                                    <%=DateUtil.getYear(form.getPeriodFrom())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-size="12pt" line-height="12pt" text-align="center" space-after.optimum="5pt">	 
                                    <% if(form.getStCustCategory1Desc()!=null) { %>
                                    <%=JSPUtil.printX(form.getStCustCategory1Desc())%><% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            
            
            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-after.optimum="10pt"></fo:block>
            
            <!-- Normal text -->

 
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center">{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Policy No. -L}{L-INA No. Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Period Start -L}{L-INA Periode Awal -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Period End -L}{L-INA Periode Akhir -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Policy Date -L}{L-INA Tanggal Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Curr -L}{L-INA Kurs -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Premium 100%-L}{L-INA Premi 100%-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Premium -L}{L-INA Premi -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Comm -L}{L-INA Komisi -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">{L-ENG Netto R/I -L}{L-INA Netto -L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <% 
                        BigDecimal [] t = new BigDecimal[4];
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            if (pol.getStCurrencyCode().equalsIgnoreCase("IDR")){
                                IDRPremi = BDUtil.add(IDRPremi, pol.getDbPremiBase());
                                IDRPremiReas = BDUtil.add(IDRPremiReas, pol.getDbPremiTotal());
                                IDRCommReas = BDUtil.add(IDRCommReas, pol.getDbNDComm1());
                                IDRNettoReas = BDUtil.add(IDRNettoReas, pol.getDbPremiTotalAfterDisc());
                            }else {
                                nonIDRPremi = BDUtil.add(nonIDRPremi, pol.getDbPremiBase());
                                nonIDRPremiReas = BDUtil.add(nonIDRPremiReas, pol.getDbPremiTotal());
                                nonIDRCommReas = BDUtil.add(nonIDRCommReas, pol.getDbNDComm1());
                                nonIDRNettoReas = BDUtil.add(nonIDRNettoReas, pol.getDbPremiTotalAfterDisc());
                            }
                            
                            //	if (Tools.compare(pol.getDbPremiTotal(), BDUtil.zero)>0) continue;
                        %>
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(String.valueOf(i+1))%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPeriodStart())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbCurrencyRate(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiBase(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDComm1(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotalAfterDisc(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6"><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRPremiReas,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRCommReas,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRNettoReas,2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6"><fo:block text-align="center">non IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(nonIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(nonIDRPremiReas,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(nonIDRCommReas,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(nonIDRNettoReas,2)%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>    
            
            
            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                        <svg xmlns="http://www.w3.org/2000/svg" width="2cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block> 
            
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>

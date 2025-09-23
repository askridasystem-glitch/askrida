<%@ page import="com.webfin.insurance.model.*,
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionMarketingReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 

<%
final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionMarketingReportForm form = (ProductionMarketingReportForm)SessionManager.getInstance().getCurrentForm(); 
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

<!-- defines page layout -->
<fo:layout-master-set>
    
    <!-- layout for the first page -->
    <fo:simple-page-master master-name="only"
                           page-width="21cm"
                           page-height="33cm"
                           margin-top="0.5cm"
                           margin-bottom="0.5cm"                                                                                                                                                                           
                           margin-left="1.5cm"
                           margin-right="1.5cm">
        <fo:region-body margin-top="2cm" margin-bottom="0.5cm"/>
        <fo:region-before extent="3cm"/>
        <fo:region-after extent="1.5cm"/>
    </fo:simple-page-master>
    
</fo:layout-master-set>
<!-- end: defines page layout -->

<!-- actual layout -->
<fo:page-sequence master-reference="only" initial-page-number="1">

<!-- usage of page layout -->
<fo:static-content flow-name="xsl-region-before">
    <fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
            <fo:retrieve-marker retrieve-class-name="message"
                                retrieve-boundary="page"
                                retrieve-position="first-starting-within-page"/>
        </fo:block>
    </fo:block-container>
    <fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
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

<fo:block font-size="10pt" space-after.optimum="10pt">
<fo:table table-layout="fixed">
<fo:table-column column-width="60mm"/>
<fo:table-column column-width="20mm"/>
<fo:table-column column-width="5mm"/>
<fo:table-column column-width="25mm"/>
<fo:table-column column-width="60mm"/>
<fo:table-body>
<fo:table-row>
    <fo:table-cell number-columns-spanned="5"><fo:block text-align="center">{L-ENG STATEMENT OF ACCOUNT-L}{L-STATEMENT OF ACCOUNT-L}</fo:block></fo:table-cell>
</fo:table-row>
<fo:table-row>
    <fo:table-cell number-columns-spanned="5"><fo:block text-align="center">3<fo:inline font-size="6pt" vertical-align="super">rd</fo:inline> Quarterly - <%=DateUtil.getYear(form.getPolicyDateFrom())%></fo:block></fo:table-cell>
</fo:table-row>
<fo:table-row>
    <fo:table-cell number-columns-spanned="5"><fo:block text-align="center">U/Y  <%=DateUtil.getYear(form.getPolicyDateFrom())%></fo:block></fo:table-cell>
</fo:table-row>           
<fo:table-row>
    <fo:table-scell number-columns-spanned="5"><fo:block text-align="center" space-after.optimum="20pt"></fo:block></fo:table-cell>
</fo:table-row>  
<fo:table-row>
    <fo:table-cell></fo:table-cell>
</fo:table-row> 
<fo:table-row>
    <fo:table-cell number-columns-spanned="5"><fo:block space-after.optimum="3pt"><%=JSPUtil.printX(form.getStEntityName())%></fo:block></fo:table-cell>
</fo:table-row> 
</fo:table-body>
</fo:table>
</fo:block> 

<!-- defines text title level 1-->


<!-- Normal text -->

 
<fo:block font-size="10pt">
    <fo:table table-layout="fixed">
        <fo:table-column column-width="50mm"/>
        <fo:table-column column-width="35mm"/>
        <fo:table-column column-width="15mm"/>
        <fo:table-column column-width="35mm"/>
        <fo:table-column column-width="35mm"/>
        <fo:table-body>
            
            <fo:table-row>
                <fo:table-cell number-columns-spanned="5" >
                    <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                </fo:table-cell>
            </fo:table-row>
            
            <fo:table-row>
                <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Class of Bussiness -L}{L-INA Jenis Pertanggungan -L}</fo:block></fo:table-cell>
                <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Gross Premium -L}{L-INA Premi Reas -L}</fo:block></fo:table-cell>
                <fo:table-cell ></fo:table-cell>
                <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG R/I Comm -L}{L-INA Komisi Reas -L}</fo:block></fo:table-cell>
                <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Net Premium -L}{L-INA Premi Netto -L}</fo:block></fo:table-cell>
            </fo:table-row>
            
            <fo:table-row>
                <fo:table-cell number-columns-spanned="5" >
                    <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                </fo:table-cell>
            </fo:table-row>
            <% 
            int counter = 35;
            BigDecimal totalGrossPremi = null;
            BigDecimal totalKomisi = null;
            BigDecimal totalNetto = null;
            
            for (int i = 0; i < l.size(); i++) {
                InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                
                totalGrossPremi = BDUtil.add(totalGrossPremi,pol.getDbPremiAmt());
                totalKomisi = BDUtil.add(totalKomisi,pol.getDbNDBrok2Pct());
                totalNetto = BDUtil.add(totalNetto,pol.getDbPremiNetto());
                
                if (i==counter) {
                    counter = counter + 35;
            %>      
            
            <fo:table-row>
                <fo:table-cell number-columns-spanned="5" >
                    <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                </fo:table-cell>
            </fo:table-row>
            <fo:table-row space-before.optimum="5pt">
                <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">SUBTOTAL</fo:block></fo:table-cell>
                <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(totalGrossPremi,0)%></fo:block></fo:table-cell>
                <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"></fo:block></fo:table-cell>
                <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(totalKomisi,0)%></fo:block></fo:table-cell>
                <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(totalNetto,0)%></fo:block></fo:table-cell>
            </fo:table-row>
            <fo:table-row>
                <fo:table-cell number-columns-spanned="5" >
                    <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                </fo:table-cell>
            </fo:table-row>
            <fo:table-row break-after="page">
                <fo:table-cell />
            </fo:table-row>
            <%
            totalGrossPremi = null;
            totalKomisi = null;
            totalNetto = null;
                }%>  
            
            <fo:table-row>
                <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm"><%=JSPUtil.printX(pol.getStCoverTypeCode())%> - <%=JSPUtil.printX(pol.getStCoTreatyID())%></fo:block></fo:table-cell>
                <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbPremiAmt(),2)%></fo:block></fo:table-cell>
                <fo:table-cell></fo:table-cell>
                <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbNDBrok2Pct(),2)%></fo:block></fo:table-cell>
                <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(pol.getDbPremiNetto(),2)%></fo:block></fo:table-cell>
            </fo:table-row>
            
            <% 
            totalGrossPremi = BDUtil.add(totalGrossPremi,pol.getDbPremiAmt());
            totalKomisi = BDUtil.add(totalKomisi,pol.getDbNDBrok2Pct());
            totalNetto = BDUtil.add(totalNetto,pol.getDbPremiNetto());
            } %>          
            
            <fo:table-row>
                <fo:table-cell number-columns-spanned="5" >
                    <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                </fo:table-cell>
            </fo:table-row>
            
            <fo:table-row>
                <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block line-height="5mm"></fo:block></fo:table-cell>
                <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(totalGrossPremi,2)%></fo:block></fo:table-cell>
                <fo:table-cell></fo:table-cell>
                <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(totalKomisi,2)%></fo:block></fo:table-cell>
                <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(totalNetto,2)%></fo:block></fo:table-cell>
            </fo:table-row> 
            
            <fo:table-row>
                <fo:table-cell number-columns-spanned="5" >
                    <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                </fo:table-cell>
            </fo:table-row>
            
        </fo:table-body>
    </fo:table>
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

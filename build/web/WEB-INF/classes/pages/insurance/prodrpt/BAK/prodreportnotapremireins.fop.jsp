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
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->
    <fo:simple-page-master master-name="only"
                  page-width="22cm"
                  page-height="21cm"
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
		
	  <fo:block font-size="12pt" text-align="left">
         Kepada Yth
      </fo:block> 
      <fo:block font-size="12pt" text-align="left" space-after.optimum="10pt">
         <%=JSPUtil.printX(form.getStEntityName())%>
      </fo:block> 
     
      <fo:block font-size="10pt" text-align="center" space-after.optimum="10pt">
        <fo:table table-layout="fixed">
	      	 <fo:table-column column-width="18mm"/>
	         <fo:table-column column-width="3mm"/>
	         <fo:table-column column-width="25mm"/>
        <fo:table-body>
             <fo:table-row>
        	 <fo:table-cell number-columns-spanned="3"><fo:block text-align="center">{L-ENG NOTA PREMI REASURANSI-L}{L-INA NOTA PREMI REASURANSI-L}</fo:block></fo:table-cell>
            </fo:table-row>
        	<fo:table-row>
        	 <fo:table-cell><fo:block text-align="left">NOTA</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="left">:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="left"></fo:block></fo:table-cell>
            </fo:table-row>
             <fo:table-row>
        	 <fo:table-cell><fo:block text-align="left">BULAN</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="left">:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="left"><%=DateUtil.getDateStr(form.getPolicyDateFrom(),"^^ yyyy")%></fo:block></fo:table-cell>
            </fo:table-row>
             <fo:table-row>
        	 <fo:table-cell><fo:block text-align="left">TREATY</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="left">:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="left"><%=DateUtil.getYear(form.getPolicyDateFrom())%></fo:block></fo:table-cell>
            </fo:table-row>
          </fo:table-body>
         </fo:table>
       </fo:block> 

     
      
    
      <!-- defines text title level 1-->
      <fo:block font-size="7pt" line-height="8pt"></fo:block>


<!-- GARIS  -->
     <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="10" line-height="0.15pt" space-after.optimum="10pt"></fo:block>

      <!-- Normal text -->

 
      <fo:block font-size="10pt">
        <fo:table table-layout="fixed">
	      	 <fo:table-column column-width="50mm"/>
	         <fo:table-column column-width="40mm"/>
	         <fo:table-column column-width="15mm"/>
	         <fo:table-column column-width="40mm"/>
	         <fo:table-column column-width="45mm"/>
        <fo:table-body>
        
        	<fo:table-row>
        	 <fo:table-cell><fo:block text-align="center">{L-ENG Jenis Pertanggungan -L}{L-INA Jenis Pertanggungan -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">Gross Premi</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">Pct(%)</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">Komisi</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">Netto</fo:block></fo:table-cell>
            </fo:table-row>

			         <fo:table-row>
                      <fo:table-cell number-columns-spanned="5" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="10pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
<% 
BigDecimal totalGrossPremi = null;
BigDecimal totalKomisi = null;
BigDecimal totalNetto = null;

for (int i = 0; i < l.size(); i++) {
	InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
	
	totalGrossPremi = BDUtil.add(totalGrossPremi,pol.getDbPremiAmt());
	totalKomisi = BDUtil.add(totalKomisi,pol.getDbNDBrok2Pct());
	totalNetto = BDUtil.add(totalNetto,pol.getDbPremiNetto());
 %>        
 
 			<fo:table-row>
 			 <fo:table-cell><fo:block> <%=JSPUtil.printX(pol.getStCoverTypeCode())%> - <%=JSPUtil.printX(pol.getStCoTreatyID())%></fo:block></fo:table-cell>
 			 <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiAmt(),2)%></fo:block></fo:table-cell>
 			 <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDBrok1Pct(),2)%></fo:block></fo:table-cell>
 			 <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDBrok2Pct(),2)%></fo:block></fo:table-cell>
 			 <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiNetto(),2)%></fo:block></fo:table-cell>
 			 
 			 <fo:table-cell><fo:block>
 			 </fo:block></fo:table-cell>
 			</fo:table-row>
 			
 					<fo:table-row>
                      <fo:table-cell number-columns-spanned="5" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row> 
 
<% } %>             

               <fo:table-row>
                <fo:table-cell></fo:table-cell>
                <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(totalGrossPremi,2)%></fo:block></fo:table-cell>
                <fo:table-cell></fo:table-cell>
                <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(totalKomisi,2)%></fo:block></fo:table-cell>
                <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(totalNetto,2)%></fo:block></fo:table-cell>
               
                </fo:table-row> 
               
               		<fo:table-row>
                      <fo:table-cell number-columns-spanned="5" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="10pt"></fo:block>
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

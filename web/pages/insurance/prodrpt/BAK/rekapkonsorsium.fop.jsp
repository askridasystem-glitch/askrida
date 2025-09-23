<%@ page import="com.webfin.insurance.model.*, 
                 com.crux.ff.model.FlexFieldHeaderView, 
                 com.crux.ff.model.FlexFieldDetailView, 
                 com.crux.util.*, 
                 com.crux.util.fop.FOPUtil, 
                 java.math.BigDecimal, 
                 com.crux.lov.LOVManager, 
                 java.util.Iterator, 
                 com.crux.web.controller.SessionManager, 
                 com.webfin.insurance.form.ProductionRecapReportForm,
                 com.webfin.insurance.form.ProductionReportForm,
                 com.webfin.insurance.model.*,
                 java.util.Date,
                 java.util.HashMap"%><?xml version="1.0" encoding="utf-8"?> 
<% 
 
   final DTOList l = (DTOList)request.getAttribute("RPT"); 
   
   final LookUpUtil regions = (LookUpUtil) LOVManager.getInstance().getLOV("LOV_CostCenter", null); 
   
 
   final LookUpUtil poltypes = (LookUpUtil) LOVManager.getInstance().getLOV("LOV_PolicyType", null); 
   final LookUpUtil custcategory = (LookUpUtil) LOVManager.getInstance().getLOV("LOV_CustCategory1", null); 
 
   final HashMap amountMap = (HashMap)l.getAttribute("AMOUNT_MAP"); 
   
   BigDecimal jumlah = new BigDecimal(0);
   
   BigDecimal tot_jumlah = new BigDecimal(0);
   
   BigDecimal persen = new BigDecimal(0);
   
   BigDecimal tot_persen = new BigDecimal(0);
   
   BigDecimal jml_inward = new BigDecimal(0);
   
   final ProductionReportForm form = (ProductionReportForm)SessionManager.getInstance().getCurrentForm(); 
 
 
   //final ProductionRecapReportForm pprc = (ProductionRecapReportForm)SessionManager.getInstance().getCurrentForm(); 
 
%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
 
  <!-- defines page layout --> 
  <fo:layout-master-set> 
 
    <fo:simple-page-master master-name="only" 
                  page-height="17cm"
                  page-width="20cm"
                  margin-top="2cm"
                  margin-bottom="2cm"
                  margin-left="2cm"
                  margin-right="2.5cm">
      <fo:region-body margin-top="1cm" margin-bottom="1.5cm"/> 
      <fo:region-before extent="3cm"/> 
      <fo:region-after extent="1.5cm"/> 
    </fo:simple-page-master> 
 
  </fo:layout-master-set> 
  <!-- end: defines page layout --> 
 
  <!-- actual layout --> 
  <fo:page-sequence master-reference="only" initial-page-number="1"> 
 
    <!-- usage of page layout --> 
    <!-- header --> 
    
   
    
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
<fo:block text-align="end"
      font-size="8pt" font-family="serif" line-height="1em + 2pt">
      {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
ref-id="end-of-document"/>
      </fo:block>
    </fo:static-content>
    
   
      
 	  
 	  <%--  
      <fo:block text-align="center" font-size="9pt" font-family="tahoma" line-height="16pt" > 
        {L-ENG PER BRANCH-L}{L-INA PER CABANG-L}
      </fo:block> 
      --%>
      
  
    
    
 
 
   <fo:flow flow-name="xsl-region-body">
   
   <fo:block text-align="center" font-size="16pt" font-family="tahoma" line-height="16pt" > 
        {L-ENG CONSORTIUM RECAPITULATION PREMIUM-L}{L-INA REKAPITULASI PRODUKSI PREMI KONSORSIUM-L}
      </fo:block> 
   
    <fo:block font-size="9pt" text-align="center">
       <% if (form.getPeriodFrom()!=null || form.getPeriodTo()!=null) {%> 
         Periode : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
       <% } %>
      </fo:block> 
      
      <fo:block font-size="9pt">
       <% if (form.getStBranchDesc()!=null) {%> 
         Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>  
       <% } %>
      </fo:block>
      
      <fo:block font-size="9pt">
       <% if (form.getStFltCoverTypeDesc()!=null) {%> 
         Jenis Penutupan  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>  
       <% } %>
      </fo:block>
   
    
      <fo:block font-size="14pt" space-after.optimum="5pt"> 
        
        <fo:table table-layout="fixed">
         <fo:table-column column-width="15mm"/> 
         <fo:table-column column-width="60mm"/> 
         <fo:table-column column-width="70mm"/> 

         <fo:table-body> 
 
   <!-- GARIS DALAM KOLOM --> 
 
                    <fo:table-row> 
                      <fo:table-cell number-columns-spanned="3" > 
                           <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                      </fo:table-cell> 
                    </fo:table-row> 
 
		           <fo:table-row space-after.optimum="10pt">
		             <fo:table-cell display-align="center"><fo:block text-align="center" space-after.optimum="10pt">NO</fo:block></fo:table-cell>
		             <fo:table-cell ><fo:block display-align="center" space-after.optimum="10pt">KETERANGAN</fo:block></fo:table-cell>
		             <fo:table-cell><fo:block text-align="center" space-after.optimum="10pt">PREMI BRUTO</fo:block></fo:table-cell>
		           </fo:table-row>

 
   <!-- GARIS DALAM KOLOM --> 
 
                    <fo:table-row> 
                      <fo:table-cell number-columns-spanned="3" > 
                           <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                      </fo:table-cell> 
                    </fo:table-row> 
                    

       <% 
          final HashMap totals = new HashMap(); 
 
          { 
            final Iterator regit = custcategory.getIterator(); 
 
            int n=0; 
 
 
            while (regit.hasNext()) { 
               String regCode = (String) regit.next(); 
               n++; 
               
               final InsurancePolicyView vpol = (InsurancePolicyView)amountMap.get(regCode); 
 
               BigDecimal amt = vpol==null?null:vpol.getDbPremiTotalAfterDisc(); 
                  
				if(amt==null) amt=BDUtil.zero; 
           
           	   String kode = new String("");
           	   
           	   if(regCode.length() == 1) kode = "0"+regCode;
           	   else kode = regCode;
           	   
           	   tot_jumlah = tot_jumlah.add(amt);
           	   
         %> 
 
           <fo:table-row space-after.optimum="10pt">
             <fo:table-cell ><fo:block text-align="center" space-after.optimum="10pt"><%=regCode%>.</fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block space-after.optimum="10pt"><%=custcategory.getDescription(regCode)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end" space-after.optimum="10pt"><%=JSPUtil.printX(amt,0)%></fo:block></fo:table-cell>        
           </fo:table-row>
           
            
         <% }}%>
         
          
 
   <!-- GARIS DALAM KOLOM --> 
 
                    <fo:table-row> 
                      <fo:table-cell number-columns-spanned="3" > 
                           <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                      </fo:table-cell> 
                    </fo:table-row> 
 			
            <fo:table-row> 
             <fo:table-cell ><fo:block></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block>JUMLAH</fo:block></fo:table-cell> 
            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot_jumlah,0)%></fo:block></fo:table-cell>  
           </fo:table-row>
 
 
   <!-- GARIS DALAM KOLOM --> 
 
                    <fo:table-row> 
                      <fo:table-cell number-columns-spanned="3" > 
                           <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                      </fo:table-cell> 
                    </fo:table-row> 
 
   <!-- GARIS DALAM KOLOM --> 
 
 
        </fo:table-body> 
         </fo:table> 
     </fo:block>
     <fo:block> </fo:block> 
     <fo:block font-size="8pt">
         {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy HH:mm:ss")%>  
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

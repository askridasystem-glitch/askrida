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
   
   final ProductionReportForm form = (ProductionReportForm)SessionManager.getInstance().getCurrentForm(); 
 
 
   //final ProductionRecapReportForm pprc = (ProductionRecapReportForm)SessionManager.getInstance().getCurrentForm(); 
 
%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
 
  <!-- defines page layout --> 
  <fo:layout-master-set> 
 
    <fo:simple-page-master master-name="only" 
                  page-height="28cm"
                  page-width="27cm"
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
 
      <fo:block text-align="center" font-size="16pt" font-family="tahoma" line-height="16pt" > 
        {L-ENG RECAPITULATION PREMIUM-L}{L-INA REKAPITULASI PRODUKSI PREMI-L}
      </fo:block> 
 
 	  <%--  
      <fo:block text-align="center" font-size="9pt" font-family="tahoma" line-height="16pt" > 
        {L-ENG PER TYPE-L}{L-INA PER JENIS-L}
      </fo:block> 
      --%>
      
    </fo:static-content> 
    
    
 
 
   <fo:flow flow-name="xsl-region-body">
   
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
   
    
      <fo:block font-size="9pt"> 
        <fo:table table-layout="fixed"> 
         <fo:table-column column-width="15mm"/> 
         <fo:table-column column-width="60mm"/> 
         <% 
            {final Iterator it = custcategory.getIterator(); 
            while (it.hasNext()) { 
               String cod = (String) it.next(); 
 
         %> 
         <fo:table-column column-width="30mm"/> 
         <% }} %>
         
        
         <fo:table-column column-width="30mm"/> 
         <fo:table-column column-width="15mm"/> 
 
         <fo:table-body> 
 
   <!-- GARIS DALAM KOLOM --> 
 
                    <fo:table-row> 
                      <fo:table-cell number-columns-spanned="8" > 
                           <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                      </fo:table-cell> 
                    </fo:table-row> 
 
           <fo:table-row> 
           	    
             <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block text-align="center">KODE</fo:block></fo:table-cell>
             <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block>JENIS BISNIS</fo:block></fo:table-cell>
             <fo:table-cell number-columns-spanned="<%=custcategory.size()%>" ><fo:block text-align="center">SUMBER BISNIS</fo:block></fo:table-cell>
             <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block text-align="center">JUMLAH</fo:block></fo:table-cell>
			 <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block text-align="center">%</fo:block></fo:table-cell>
            </fo:table-row>
           	
           	
 
           <fo:table-row> 
             <%--<fo:table-cell ><fo:block>No</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>DAERAH</fo:block></fo:table-cell>--%>
             <% 
                {final Iterator it = custcategory.getIterator(); 
               while (it.hasNext()) { 
                  String cod = (String) it.next(); 
 
            %> 
             <fo:table-cell ><fo:block text-align="end"><%=custcategory.getDescription(cod)%></fo:block></fo:table-cell> 
            <% }  
            
            }%> 
            
            <%--  
            
			--%>
           </fo:table-row> 
           
         
 
   <!-- GARIS DALAM KOLOM --> 
 
                    <fo:table-row> 
                      <fo:table-cell number-columns-spanned="8" > 
                           <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                      </fo:table-cell> 
                    </fo:table-row> 
             
               
        <% 
        
          { 
            final Iterator regit = poltypes.getIterator(); 
 
            int n=0; 
 
 
            while (regit.hasNext()) { 
               String regCode = (String) regit.next(); 
               n++; 
           
           	   String kode = new String("");
           	   
           	   if(regCode.length() == 1) kode = "0"+regCode;
           	   else kode = regCode;

                {final Iterator it = custcategory.getIterator(); 
               while (it.hasNext()) { 
                  String cod = (String) it.next(); 
 
                  final InsurancePolicyView vpol = (InsurancePolicyView)amountMap.get(regCode+"/"+cod); 
 
                  BigDecimal amt = vpol==null?null:vpol.getDbPremiTotalAfterDisc(); 
                  
     		      if(amt==null) amt=BDUtil.zero; 

 				  tot_jumlah = tot_jumlah.add(amt);
 				  
             }} 
           }
          }
        

         %>       
                    

       <% 
          final HashMap totals = new HashMap(); 
 
          { 
            final Iterator regit = poltypes.getIterator(); 
 
            int n=0; 
 
 
            while (regit.hasNext()) { 
               String regCode = (String) regit.next(); 
               n++; 
           
           	   String kode = new String("");
           	   
           	   if(regCode.length() == 1) kode = "0"+regCode;
           	   else kode = regCode;
           	   
         %> 
 
           <fo:table-row> 
           
           
           
             <fo:table-cell ><fo:block text-align="center"><%=kode%></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block><%=poltypes.getDescription(regCode)%></fo:block></fo:table-cell> 
             <% 
                {final Iterator it = custcategory.getIterator(); 
               while (it.hasNext()) { 
                  String cod = (String) it.next(); 
 
                  //BigDecimal amt = pprc.getAmount(regCode,cod); 
 
                  final InsurancePolicyView vpol = (InsurancePolicyView)amountMap.get(regCode+"/"+cod); 
 
                  BigDecimal amt = vpol==null?null:vpol.getDbPremiTotalAfterDisc(); 
                  
                  
 
                  if(amt==null) amt=BDUtil.zero; 
 
                  final String key = "O"+cod; 
 
                  totals.put( 
                          key, 
                          BDUtil.add( 
                                  (BigDecimal) totals.get(key), 
                                  amt 
                          ) 
                  ); 
                  
                  jumlah = jumlah.add(amt);
 				  
 				  //tot_jumlah = tot_jumlah.add(jumlah);
 				  
 				  if (jumlah.equals(BDUtil.zero)) persen = BDUtil.zero;
 				  
 				  else
 				  	persen = (jumlah.divide(tot_jumlah,BigDecimal.ROUND_CEILING)).multiply(new BigDecimal(100));
 				  
 				    
 
 
             %> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(amt,0)%></fo:block></fo:table-cell>
             <% }} %> 
         
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(jumlah,0)%></fo:block></fo:table-cell>
             
             <% 
             	tot_persen = tot_persen.add(persen);
             	jumlah = new BigDecimal(0);
             	
             
              %>
             
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(persen,3)%></fo:block></fo:table-cell>
             
           </fo:table-row> 
         <% }}%>
         
          
 
   <!-- GARIS DALAM KOLOM --> 
 
                    <fo:table-row> 
                      <fo:table-cell number-columns-spanned="8" > 
                           <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                      </fo:table-cell> 
                    </fo:table-row> 
 
            <fo:table-row> 
             <fo:table-cell ><fo:block></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block>SUB-TOTAL</fo:block></fo:table-cell> 
             <% 
                {final Iterator it = custcategory.getIterator(); 
               while (it.hasNext()) { 
                  String cod = (String) it.next(); 
 
                  final String key = "O"+cod; 
 
                  final Object tot = totals.get(key); 
 
 
 
             %> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot,0)%></fo:block></fo:table-cell> 
            <% }} %> 
              
            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot_jumlah,0)%></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot_persen,3)%></fo:block></fo:table-cell>

           </fo:table-row> 
 
           <% 
              for (int i = 0; i < l.size(); i++) { 
                 InsurancePolicyView pol = (InsurancePolicyView) l.get(i); 
 
                 if (!pol.isInward()) continue; 
 
                 final String key = "I"+pol.getStBusinessSourceCode(); 
 
                 BigDecimal amt = BDUtil.add( 
                                                   (BigDecimal) totals.get(key), 
                                                   pol.getDbPremiTotalAfterDisc() 
                                           ); 
 
                 if (amt==null) amt=BDUtil.zero; 
 
                 totals.put( 
                          key, 
                         amt 
                  ); 
              } 
 
           %> 
 
   <!-- GARIS DALAM KOLOM --> 
 
                    <fo:table-row> 
                      <fo:table-cell number-columns-spanned="8" > 
                           <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                      </fo:table-cell> 
                    </fo:table-row> 
 
           <fo:table-row> 
             <fo:table-cell ><fo:block></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block>INWARD</fo:block></fo:table-cell> 
             <% 
                {final Iterator it = custcategory.getIterator(); 
               while (it.hasNext()) { 
                  String cod = (String) it.next(); 
 
                  final String key = "I"+cod; 
 
                  Object tot = totals.get(key); 
 
                  if (tot==null) tot=BDUtil.zero; 
 
             %> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot,0)%></fo:block></fo:table-cell> 
             
            <% }} %> 
            
              
            <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
           </fo:table-row> 
 
   <!-- GARIS DALAM KOLOM --> 
 
                    <fo:table-row> 
                      <fo:table-cell number-columns-spanned="8" > 
                           <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                      </fo:table-cell> 
                    </fo:table-row> 
 
           <fo:table-row> 
             <fo:table-cell ><fo:block></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block>TOTAL</fo:block></fo:table-cell> 
             <% 
                {final Iterator it = custcategory.getIterator(); 
               while (it.hasNext()) { 
                  String cod = (String) it.next(); 
 
                  BigDecimal tot = (BigDecimal) totals.get("I"+cod); 
 
                  tot = BDUtil.add(tot, (BigDecimal) totals.get("O"+cod)); 
 
                  if (tot==null) tot=BDUtil.zero; 
 
             %> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot,0)%></fo:block></fo:table-cell> 
            
            <% }} %> 
              
             
            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot_jumlah,0)%></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot_persen,3)%></fo:block></fo:table-cell>
           </fo:table-row> 
 
   <!-- GARIS DALAM KOLOM --> 
 
                    <fo:table-row> 
                      <fo:table-cell number-columns-spanned="8" > 
                           <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt"></fo:block> 
                      </fo:table-cell> 
                    </fo:table-row> 
 
        </fo:table-body> 
         </fo:table> 
     </fo:block>
     <fo:block> </fo:block> 
     <fo:block font-size="8pt">
         {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
      </fo:block> 
   </fo:flow> 
   </fo:page-sequence> 
</fo:root> 

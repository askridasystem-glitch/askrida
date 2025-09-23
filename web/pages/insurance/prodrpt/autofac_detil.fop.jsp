<%@ page import="com.webfin.insurance.model.*, 
                 com.crux.ff.model.FlexFieldHeaderView, 
                 com.crux.ff.model.FlexFieldDetailView, 
                 com.crux.util.*, 
                 com.crux.util.fop.FOPUtil, 
                 java.math.BigDecimal, 
                 com.crux.web.form.FormManager, 
                 com.crux.web.controller.SessionManager, 
                 com.webfin.insurance.form.ProductionReportForm, 
				 com.webfin.entity.model.EntityView,
                 java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 
 
   final DTOList l = (DTOList)request.getAttribute("RPT"); 
 
   final ProductionReportForm form = (ProductionReportForm)SessionManager.getInstance().getCurrentForm(); 
   
   BigDecimal rate1 = new BigDecimal(0);       
   BigDecimal rate2 = new BigDecimal(0);
   BigDecimal total = new BigDecimal(0);  
   BigDecimal total1 = new BigDecimal(0); 
   BigDecimal total2 = new BigDecimal(0); 
 
%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
 
  <!-- defines page layout --> 
  <fo:layout-master-set> 
 
    <!-- layout for the first page --> 
    <fo:simple-page-master master-name="only" 
                  page-height="21cm"
                  page-width="29.5cm"
                  margin-top="3cm"
                  margin-bottom="0.5cm"
                  margin-left="3cm"
                  margin-right="3cm">
 
      <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/> 
      <fo:region-before extent="3cm"/> 
      <fo:region-after extent="1.5cm"/> 
    </fo:simple-page-master> 
 
  </fo:layout-master-set> 
  <!-- end: defines page layout --> 
 
  <!-- actual layout --> 
  <fo:page-sequence master-reference="only" initial-page-number="1"> 
 
    <!-- usage of page layout --> 
    <!-- header --> 
    
 
    <fo:flow flow-name="xsl-region-body"> 
    
    <fo:block font-size="10pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="100mm"/>
         <fo:table-column />
         <fo:table-body>
         
       <fo:table-row>
	  <fo:table-cell >
       <fo:block font-weight="bold">Kepada YTH,</fo:block>
       <fo:block font-weight="bold">REASURANSI INTERNASIONAL INDONESIA</fo:block>
       <fo:block font-weight="bold">Jl. Salemba Raya No. 30</fo:block>
       <fo:block font-weight="bold">Jakarta Pusat 10430</fo:block>
      </fo:table-cell>
      </fo:table-row>  
      
        </fo:table-body>
       </fo:table>
       </fo:block>
       
       <fo:block space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
    
      <fo:block font-size="10pt"> 
        <fo:table table-layout="fixed">  
         <fo:table-column column-width="10mm"/><!-- No -->
         <fo:table-column column-width="40mm"/><!-- No -->
         <fo:table-column column-width="40mm"/><!-- Entry Date --> 
         <fo:table-column column-width="60mm"/><!-- Police Date -->
         <fo:table-column column-width="5mm"/><!-- Policy No. -->
         <fo:table-column column-width="35mm"/><!-- Policy No. -->
         <fo:table-column column-width="5mm"/><!-- Policy No. -->
         <fo:table-column column-width="5mm"/><!-- Policy No. -->
         <fo:table-column column-width="35mm"/><!-- Policy No. -->
         <fo:table-header>
            
      <fo:table-row>
	  <fo:table-cell number-columns-spanned="9">
      <fo:block font-family="tahoma" font-weight="bold" font-size="20pt" text-align="center">             
      AUTOMATIC FACULTATIVE
      </fo:block>
      <fo:block font-family="tahoma" font-weight="bold" font-size="20pt" text-align="center">             
      PA KREASI + PHK
      </fo:block>
      </fo:table-cell>
      </fo:table-row>
      
      <fo:table-row>
       <fo:table-cell number-columns-spanned="9"><fo:block space-before.optimum="10pt" space-after.optimum="10pt"></fo:block></fo:table-cell>
     </fo:table-row>
       
     <fo:table-row>
	  <fo:table-cell number-columns-spanned="9">
      <fo:block font-weight="bold">
       <% if (form.getStNoUrut()!=null) {%> 
         Note Claim : <%=JSPUtil.printX(form.getStNoUrut())%>
       <% } %>
      </fo:block>
      </fo:table-cell>
      </fo:table-row>

      <fo:table-row>
	  <fo:table-cell number-columns-spanned="9">
      <fo:block font-weight="bold">
       <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
         Month : <%=DateUtil.getMonth(form.getPolicyDateFrom())%> - <%=DateUtil.getYear2Digit(form.getPolicyDateFrom())%>
       <% } %>
      </fo:block>
      </fo:table-cell>
      </fo:table-row>
      
      			<fo:table-row>   
                      <fo:table-cell number-columns-spanned="9">  
                           <fo:block border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row> 
         
           <fo:table-row>   
             <fo:table-cell><fo:block text-align="center">{L-ENG NO.-L}{L-INA NO. -L}</fo:block></fo:table-cell><!-- No --> 
             <fo:table-cell><fo:block text-align="center">{L-ENG DLA NO.-L}{L-INA NO. LKP -L}</fo:block></fo:table-cell><!-- No --> 
             <fo:table-cell ><fo:block text-align="center">{L-ENG POLICY NO.-L}{L-INA NO. POLIS-L}</fo:block></fo:table-cell><!-- Entry Date --> 
             <fo:table-cell><fo:block text-align="center">{L-ENG NAME -L}{L-INA NAMA -L}</fo:block></fo:table-cell><!-- Police Date --> <!-- Entry Date --> 
             <fo:table-cell><fo:block ></fo:block></fo:table-cell><!-- Police Date --> 
             <fo:table-cell ><fo:block text-align="end">{L-ENG NET CLAIM -L}{L-INA NET CLAIM -L}</fo:block></fo:table-cell><!-- Policy No. --> 
             <fo:table-cell><fo:block ></fo:block></fo:table-cell><!-- Police Date --> 
             <fo:table-cell><fo:block ></fo:block></fo:table-cell><!-- Police Date --> 
             <fo:table-cell ><fo:block text-align="end">{L-ENG NET CLAIM -L}{L-INA NET CLAIM -L}</fo:block></fo:table-cell><!-- Policy No. --> 
            </fo:table-row>   
   
   <!-- GARIS DALAM KOLOM -->  
   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="9">  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>  
                    
                    </fo:table-header> 
                    
                    <fo:table-body>
   
<% 
	BigDecimal [] t = new BigDecimal[1]; 

   for (int i = 0; i < l.size(); i++) { 
      InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
      
      int n=0;   
      t[n] = BDUtil.add(t[n++], pol.getDbClaimCustAmount());
      	
      if (pol.getStClaimCauseID().equalsIgnoreCase("1576")) {
      	rate1 = BDUtil.mul(pol.getDbClaimCustAmount(), new BigDecimal(0.075));
      	total = BDUtil.add(total,rate1);
      } else {
      	rate2 = BDUtil.mul(pol.getDbClaimCustAmount(), new BigDecimal(0.45));
      	total = BDUtil.add(total,rate2);
      }
%>     

            <fo:table-row>   
             <fo:table-cell><fo:block text-align="center"><%= JSPUtil.printX(String.valueOf(i+1)) %>.</fo:block></fo:table-cell>  
             <fo:table-cell><fo:block><%= JSPUtil.printX(pol.getStDLANo()) %></fo:block></fo:table-cell>  
             <fo:table-cell><fo:block><%= JSPUtil.printX(pol.getStPolicyNo()) %></fo:block></fo:table-cell> 
             <fo:table-cell><fo:block><%= JSPUtil.printX(pol.getStCustomerName()) %></fo:block></fo:table-cell> 
             <fo:table-cell><fo:block>Rp. </fo:block></fo:table-cell> 
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimCustAmount(),2)%></fo:block></fo:table-cell>
			 <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>Rp. </fo:block></fo:table-cell>
<% if (pol.getStClaimCauseID().equalsIgnoreCase("1576")) {%>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(rate1,2)%></fo:block></fo:table-cell>
<% } else { %>
			<fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(rate2,2)%></fo:block></fo:table-cell>
<% } %>        
           </fo:table-row>
   

<% } %>         
   
   <!-- GARIS DALAM KOLOM -->  
   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="9">  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>   
   
           <fo:table-row>   
             <fo:table-cell number-columns-spanned="4"><fo:block text-align="center">TOTAL KLAIM </fo:block></fo:table-cell>
             <fo:table-cell ><fo:block >Rp. </fo:block></fo:table-cell><!-- Name of Insured --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell><!-- Total Sum Insured --> 
             <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell><!-- Claim Estimated --> 
             <fo:table-cell ><fo:block >Rp. </fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(total,2)%></fo:block></fo:table-cell><!-- Claim Approved -->  
            </fo:table-row>   
   
   <!-- GARIS DALAM KOLOM -->   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="9" >  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>   

         </fo:table-body>   
       </fo:table>   
       </fo:block> 
       
   <fo:block font-size="8pt">
         {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
      </fo:block>    
      
      <fo:block font-size="10pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="170mm"/>
         <fo:table-column />
         <fo:table-body>
         
            <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell >
               <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block>
               <fo:block text-align="center">Bagian Klaim</fo:block>
             </fo:table-cell>
           </fo:table-row>
           
        </fo:table-body>
       </fo:table>
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

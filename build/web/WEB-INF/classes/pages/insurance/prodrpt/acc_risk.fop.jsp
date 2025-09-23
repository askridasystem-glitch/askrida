<%@ page import="com.webfin.insurance.model.*, 
                 com.crux.ff.model.FlexFieldHeaderView, 
                 com.crux.ff.model.FlexFieldDetailView, 
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
                  page-height="21cm"
                  page-width="32cm"
                  margin-top="1cm"
                  margin-bottom="1cm"
                  margin-left="0.5cm"
                  margin-right="0.5cm">
 
      <fo:region-body margin-top="1.5cm" margin-bottom="0.5cm"/> 
      <fo:region-before extent="1cm"/> 
      <fo:region-after extent="0.5cm"/> 
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
    </fo:static-content>
  
  <fo:static-content flow-name="xsl-region-after">
      <fo:block text-align="end"
      font-size="8pt" font-family="serif" line-height="1em + 2pt">
      {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
ref-id="end-of-document"/>
      </fo:block>
    </fo:static-content>
    
 
    <fo:flow flow-name="xsl-region-body"> 
 
      <!-- defines text title level 1--> 
 
       <fo:block font-size="18pt"
      		font-family="tahoma"
            line-height="16pt"
            space-after.optimum="10pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       {L-ENG RISK ACCUMULATION-L}{L-INA RESIKO AKUMULASI-L}
     <!--   \<%=form.getStReportTitle()%>-->
      </fo:block> 
 
      <!-- Normal text -->  
  
      <fo:block font-size="14pt">
       <% if (form.getStRiskLocation()!=null) {%> 
         Risk Location :<%=JSPUtil.printX(form.getStRiskLocation())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="14pt">
       <% if (form.getStPostCode()!=null) {%> 
         Post Code :<%=JSPUtil.printX(form.getStPostCode())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="14pt">
       <% if (form.getStRiskCardNo()!=null) {%> 
         Risk Card No. :<%=JSPUtil.printX(form.getStRiskCardNo())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="14pt">
       <% if (form.getStRiskCode()!=null) {%> 
         Risk Category :<%=JSPUtil.printX(form.getStRiskCode())%>  
       <% } %>
      </fo:block> 
      
      <fo:block font-size="14pt">
       <% if (form.getStZoneCode()!=null) {%> 
         Zone Code :<%=JSPUtil.printX(form.getStZoneCode())%> ( <%=JSPUtil.printX(form.getStZoneCodeName())%> )
       <% } %>
      </fo:block> 
      
 <!--    \<fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="1pt" space-after.optimum="10pt"></fo:block>  --> 
       
      <fo:block font-size="10pt"> 
        <fo:table table-layout="fixed">  
         <fo:table-column column-width="10mm"/><!-- No -->
         <fo:table-column column-width="20mm"/><!-- Policy Date -->
         <fo:table-column column-width="20mm"/><!-- Period Start--> 
         <fo:table-column column-width="45mm"/><!-- Policy No -->
         <fo:table-column column-width="15mm"/><!-- Risk Code -->
         <fo:table-column column-width="70mm"/><!-- The Insured -->
         <fo:table-column column-width="30mm"/><!-- Total Insured -->
         <fo:table-column column-width="30mm"/><!-- Premium --> 
         <fo:table-column column-width="30mm"/><!-- Claim -->
         <fo:table-header>
         
         <fo:table-row>   
             <fo:table-cell ></fo:table-cell>
             <fo:table-cell ></fo:table-cell>
             <fo:table-cell ></fo:table-cell>
             <fo:table-cell ></fo:table-cell>  
           	 <fo:table-cell ></fo:table-cell>
             <fo:table-cell ></fo:table-cell> 
			 <fo:table-cell ></fo:table-cell>
			 <fo:table-cell ></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end" font-weight="bold">{L-ENG (In IDR)-L}{L-INA (dalam rupiah)-L}</fo:block></fo:table-cell>  
         </fo:table-row>  
         
         			<fo:table-row>   
                      <fo:table-cell number-columns-spanned="9" >  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>  
         
          <fo:table-row>   
             <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Entry Date.-L}{L-INA Tgl. Entry -L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Date-L}{L-INA Tgl. Polis-L}</fo:block></fo:table-cell>  
           	 <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Number -L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>  
			 <fo:table-cell ><fo:block text-align="center">{L-ENG Risk Code -L}{L-INA Kode Resiko -L}</fo:block></fo:table-cell>  
			 <fo:table-cell ><fo:block text-align="center">{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell> 
			 <fo:table-cell ><fo:block text-align="center">{L-ENG Total Sum Insured-L}{L-INA Total Sum Insured-L}</fo:block></fo:table-cell>
			 <fo:table-cell ><fo:block text-align="center">{L-ENG Gross Premium-L}{L-INA Premi Bruto-L}</fo:block></fo:table-cell>  
			 <fo:table-cell ><fo:block text-align="center">{L-ENG Claim Paid-L}{L-INA Biaya Klaim-L}</fo:block></fo:table-cell>  
       	</fo:table-row>  
           
           
           
   
   <!-- GARIS DALAM KOLOM -->   
   
                     <fo:table-row>   
                      <fo:table-cell number-columns-spanned="9" >  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>  
                    
    </fo:table-header>

                    <fo:table-body>
  
   <!-- GARIS  -->  
  <!--   \<fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt"></fo:block> -->  
     
   
<%   
   
   
   BigDecimal [] t = new BigDecimal[3];
   
   for (int i = 0; i < l.size(); i++) { 
           InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
           
      int n=0; 
      t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmountRp());
      t[n] = BDUtil.add(t[n++], pol.getDbPremiTotalRp());
      t[n] = BDUtil.add(t[n++], pol.getDbClaimAmountRp());
      
      
      String no_polis = pol.getStPolicyNo();
      
      String no_polis_cetak = no_polis.substring(0,4)+"-"+no_polis.substring(4,8)+"-"+no_polis.substring(8,12)+"-"+no_polis.substring(12,16)+"-"+no_polis.substring(16,18);
         
   		int p=-1;   
   
      final DTOList objects = pol.getObjects();   
   
      while (true) {
         p++;   
         if (p>=objects.size()) break;   
   
         final InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView)objects.get(p);

%>   
			
   			
   			<fo:table-row>   
             <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtCreateDate())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
      		 <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPolicyDate())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
			 <fo:table-cell ><fo:block><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No --> 
			 <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(obj.getStReference3())%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No --> 
			 <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>    <!-- No --><!-- The Insured --> 
			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmountRp(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium --> 
			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotalRp(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium --> 
			 <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmountRp(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium --> 
		  </fo:table-row>  
   			
<%   
   
  }    }
      %>   
   
   <!-- GARIS DALAM KOLOM -->   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="9">  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>  
   
           <fo:table-row>   
             <fo:table-cell ></fo:table-cell>    
             <fo:table-cell ></fo:table-cell>  
             <fo:table-cell ></fo:table-cell> 
             <fo:table-cell ></fo:table-cell> 
             <fo:table-cell number-columns-spanned="2"><fo:block> TOTAL : </fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0],0)%></fo:block></fo:table-cell><!-- Total Premi --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1],0)%></fo:block></fo:table-cell><!-- Total Premi --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2],0)%></fo:block></fo:table-cell><!-- Total Fee --> 
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
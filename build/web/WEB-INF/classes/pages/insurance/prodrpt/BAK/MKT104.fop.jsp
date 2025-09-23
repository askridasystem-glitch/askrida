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
 
   //if (true) throw new NullPointerException(); 
 
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
                  margin-left="0cm"
                  margin-right="1cm">
 
      <fo:region-body margin-top="1cm" margin-bottom="1.5cm"/> 
      <fo:region-before extent="0.5cm"/> 
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
    </fo:static-content>
  
  <fo:static-content flow-name="xsl-region-after">
      <fo:block text-align="end" 
            font-size="6pt"
            font-family="serif"
            line-height="12pt">
        Production Report MKT104 - PT. Asuransi Bangun Askrida 
      </fo:block> 
    <fo:block text-align="end"
      font-size="8pt" font-family="serif" line-height="1em + 2pt">
      {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
ref-id="end-of-document"/>
      </fo:block>      
    </fo:static-content>

 
    <fo:flow flow-name="xsl-region-body"> 
 
      <!-- defines text title level 1--> 
 
      <fo:block font-size="18pt"
            line-height="16pt"
            space-after.optimum="2pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       Register Production Premium
      </fo:block> 
 
      <!-- Normal text -->  
  
      <fo:block font-size="16pt" text-align="center">
       <% if (form.getExpirePeriodFrom()!=null || form.getExpirePeriodTo()!=null) {%> 
         Expire Date From : <%=JSPUtil.printX(form.getExpirePeriodFrom())%> To <%=JSPUtil.printX(form.getExpirePeriodTo())%>
       <% } %>
      </fo:block>  

      <fo:block font-size="16pt" text-align="center">
       <% if (form.getPeriodFrom()!=null || form.getPeriodTo()!=null) {%> 
         Period From : <%=JSPUtil.printX(form.getPeriodFrom())%> To <%=JSPUtil.printX(form.getPeriodTo())%>
       <% } %>
      </fo:block>  
  
      <fo:block font-size="16pt" text-align="center">
       <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
         Policy Date From : <%=JSPUtil.printX(form.getPolicyDateFrom())%> To <%=JSPUtil.printX(form.getPolicyDateTo())%>
       <% } %>
      </fo:block>  
  
      <fo:block font-size="16pt" text-align="center">
       <% if (form.getStPolicyTypeDesc()!=null) {%> 
         Policy Type : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="16pt" text-align="center">
       <% if (form.getStFltCoverTypeDesc()!=null) {%> 
         Cover Type  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>  
       <% } %>
      </fo:block>  
  
      <fo:block font-size="16pt" text-align="center">
       <% if (form.getStCustCategory1Desc()!=null) {%> 
         Cust Category : <%=JSPUtil.printX(form.getStCustCategory1Desc())%>  
       <% } %>
      </fo:block>  
  
      <fo:block font-size="16pt" text-align="center">
       <% if (form.getStRiskLocation()!=null) {%> 
         Risk Location :<%=JSPUtil.printX(form.getStRiskLocation())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="16pt" text-align="center">
       <% if (form.getStPostCode()!=null) {%> 
         Post Code :<%=JSPUtil.printX(form.getStPostCode())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="16pt" text-align="center">
       <% if (form.getStEntityName()!=null) {%> 
         Customer Name :<%=JSPUtil.printX(form.getStEntityName())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="16pt" text-align="center">
       <% if (form.getStRiskCardNo()!=null) {%> 
         Risk Card No. :<%=JSPUtil.printX(form.getStRiskCardNo())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="16pt" text-align="center">
       <% if (form.getStRiskCode()!=null) {%> 
         Risk Code :<%=JSPUtil.printX(form.getStRiskCode())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="16pt" text-align="center">
       <% if (form.getStBranchDesc()!=null) {%> 
         Branch : <%=JSPUtil.printX(form.getStBranchDesc())%>  
       <% } %>
      </fo:block>  
  
      <fo:block font-size="8pt">
         Print Date : <%=DateUtil.getDateStr(new Date(),"d MMM yyyy")%>  
      </fo:block>  
  
   <!-- GARIS  -->  
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt"></fo:block>  
  
      <fo:block font-size="8pt"> 
        <fo:table table-layout="fixed">  
         <fo:table-column column-width="10mm"/><!-- No -->
         <fo:table-column column-width="30mm"/><!-- Policy No -->
         <fo:table-column column-width="50mm"/><!-- The Insured --> 
         <fo:table-column column-width="15mm"/><!-- Policy Date --> 
         <fo:table-column column-width="15mm"/><!-- Period Start--> 
         <fo:table-column column-width="15mm"/><!-- Period End --> 
         <fo:table-column column-width="10mm"/><!-- CCY --> 
         <fo:table-column column-width="40mm"/><!-- Sum Insured --> 
         <fo:table-column column-width="30mm"/><!-- Premium --> 
         <fo:table-column column-width="30mm"/><!-- Adm Cost --> 
         <fo:table-column column-width="30mm"/><!-- Discount --> 
         
         <fo:table-column column-width="30mm"/><!-- Total Comm --> 
         
   
         <fo:table-column />   
         <fo:table-body>   
           <fo:table-row>   
             <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG No. Policy.-L}{L-INA No. Polis -L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Date-L}{L-INA Tgl. Polis-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Inc. Date-L}{L-INA Tgl. Mulai-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Exp. Date-L}{L-INA Tgl. Akhir-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG CCY-L}{L-INA Valuta-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Sum Insured -L}{L-INA H. Pertanggungan-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Premium-L}{L-INA Premi-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Adm Cost -L}{L-INA Biaya Adm-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Discount-L}{L-INA Discount-L}</fo:block></fo:table-cell>  
             
             <fo:table-cell ><fo:block text-align="center">{L-ENG Total Comm-L}{L-INA Total Komisi-L}</fo:block></fo:table-cell>  
             
           </fo:table-row>   
   
   <!-- GARIS DALAM KOLOM -->   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="15">  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>   
   
<%   
   
   
   BigDecimal [] t = new BigDecimal[30];   
   
   int no=0;
   int no_sblm=0;
   for (int i = 0; i < l.size(); i++) { 
     
	    no=no+1;
      InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
   
   
   /*
   for (int i = 0; i < l.size(); i++) {   
      InsurancePolicyView pol = (InsurancePolicyView) l.get(i);   */
   
      int n=0;   
      t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());   
      t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());   
      t[n] = BDUtil.add(t[n++], pol.getDbTotalItemAmount("PCOST"));   
      t[n] = BDUtil.add(t[n++], pol.getDbTotalItemAmount("SFEE"));   
      t[n] = BDUtil.add(t[n++], pol.getDbTotalFee());   
      t[n] = BDUtil.add(t[n++], pol.getDbTotalDisc());   
      t[n] = BDUtil.add(t[n++], pol.getDbTotalComm());   
      t[n] = BDUtil.add(t[n++], pol.getDbPremiNetto());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDPremi1());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDPremi2());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDPremi3());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDPremi4());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDComm1());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDComm2());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDComm3());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDComm4());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDDisc1Pct());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDDisc1());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDDisc2Pct());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDDisc2());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDBrok1Pct());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDBrok1());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDHFeePct());   
      t[n] = BDUtil.add(t[n++], pol.getDbNDHFee());   
   
  /*    int p=-1;   
   
      final DTOList objects = pol.getObjects();   
   
      while (true) {   
         p++;   
         if (p>=objects.size()) break;   
   
         final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(p);   */
   
   
%>   
            <fo:table-row>   
             <fo:table-cell ><fo:block><%=i+1%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>    <!-- No --><!-- The Insured --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
             <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
             <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
             <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>    <!-- No --><!-- CCY --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Sum Insured --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalFee())%></fo:block></fo:table-cell>    <!-- No --><!-- Adm Cost --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalDisc(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Discount --> 
             
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalComm(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Total Comm --> 
             
   
           </fo:table-row>   
   
<%   
      }   
   
      %>   
   
   <!-- GARIS DALAM KOLOM -->   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="15">  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>   
   
           <fo:table-row>   
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell><!-- Total TSI --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell><!-- Total Premi --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[4],2)%></fo:block></fo:table-cell><!-- Total Comm --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[5],2)%></fo:block></fo:table-cell><!-- Total Nett Premi --> 
               
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[6],2)%></fo:block></fo:table-cell>  
             
   
           </fo:table-row>   
   
   <!-- GARIS DALAM KOLOM -->   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="15" >  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
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

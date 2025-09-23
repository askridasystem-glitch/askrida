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
                  page-width="33cm"
                  margin-top="1cm"
                  margin-bottom="2cm"
                  margin-left="1.5cm"
                  margin-right="1.5cm">
 
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
      <fo:block text-align="end" 
            font-size="6pt"
            font-family="serif"
            line-height="12pt">
        Production Report MKT00 - PT. Asuransi Bangun Askrida Page:<fo:page-number/> 
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
       {L-ENG Production Premium Report-L}{L-INA Laporan Produksi Premi-L} 
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
         <fo:table-column column-width="15mm"/><!-- Policy Date --> 
         <fo:table-column column-width="20mm"/><!-- User Code --> 
         <fo:table-column column-width="30mm"/><!-- Policy No -->
         <fo:table-column column-width="50mm"/><!-- The Insured --> 
         <fo:table-column column-width="50mm"/><!-- Address--> 
         <fo:table-column column-width="10mm"/><!-- CCY --> 
         <fo:table-column column-width="40mm"/><!-- Sum Insured --> 
         <fo:table-column column-width="30mm"/><!-- Premium --> 
         <fo:table-column column-width="30mm"/><!-- Premi Net --> 
   
         <fo:table-column />   
         <fo:table-body>   
           <fo:table-row>   
             <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Date-L}{L-INA Tgl. Polis-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG User Code-L}{L-INA Kd. User-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG No. Policy.-L}{L-INA No. Polis -L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Address of Insured-L}{L-INA Alamat-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG CCY-L}{L-INA Valuta-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Sum Insured -L}{L-INA H. Pertanggungan-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Premium-L}{L-INA Premi-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG PremiNet-L}{L-INA PremiNett-L}</fo:block></fo:table-cell>  
           </fo:table-row>   
   
   <!-- GARIS DALAM KOLOM -->   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="11">  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>   
   
<%   
   
   
   BigDecimal [] t = new BigDecimal[30];   
   
   
   for (int i = 1; i < l.size(); i++) {   
      InsurancePolicyView pol = (InsurancePolicyView) l.get(i);   
   
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
   
      int p=-1;   
   
      final DTOList objects = pol.getObjects();   
   
      while (true) {   
         p++;   
         if (p>=objects.size()) break;   
   
         final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(p);   
   
   
%>   
            <fo:table-row>   
             <fo:table-cell ><fo:block><%=i%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCreateWho())%></fo:block></fo:table-cell>    <!-- No --><!-- User Code --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>    <!-- No --><!-- The Insured --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerAddress())%></fo:block></fo:table-cell>    <!-- No --><!-- Address--> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>    <!-- No --><!-- CCY --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Sum Insured --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiNetto(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Premi Net --> 
   
           </fo:table-row>   
   
<%   
      }   
   
      }%>   
   
   <!-- GARIS DALAM KOLOM -->   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="11">  
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
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[7],2)%></fo:block></fo:table-cell>  
   
           </fo:table-row>   
   
   <!-- GARIS DALAM KOLOM -->   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="11" >  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>   

         </fo:table-body>   
       </fo:table>   
       </fo:block>   
   
    </fo:flow>   
  </fo:page-sequence>   
</fo:root>   

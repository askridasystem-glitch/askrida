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
                  page-width="70cm"
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
        Claim Report CLM100 PT. Asuransi Bangun Askrida Page:<fo:page-number/> 
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
       {L-ENG Production Claim Report-L}{L-INA Laporan Produksi Klaim-L} 
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

      <fo:block font-size="0" text-align="center">
       <% if (form.getStFltClaimStatus()!=null) { %> 
         Claim Status :<%=JSPUtil.printX(form.getStFltClaimStatusDesc())%>  
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
         <fo:table-column column-width="30mm"/><!-- Policy No. -->
         <fo:table-column column-width="30mm"/><!-- PLA No. --> 
         <fo:table-column column-width="30mm"/><!-- DLA No. --> 
         <fo:table-column column-width="60mm"/><!-- Customer Name --> 
         <fo:table-column column-width="60mm"/><!-- Customer Address --> 
         <fo:table-column column-width="20mm"/><!-- Inception Date --> 
         <fo:table-column column-width="20mm"/><!-- Expired Date --> 
         <fo:table-column column-width="20mm"/><!-- Claim Date --> 
         <fo:table-column column-width="60mm"/><!-- Claim Object --> 
         <fo:table-column column-width="30mm"/><!-- Sum Insured --> 
         <fo:table-column column-width="30mm"/><!-- Premium --> 
         <fo:table-column column-width="50mm"/><!-- Claim Caused --> 
         <fo:table-column column-width="50mm"/><!-- Claim Location --> 
         <fo:table-column column-width="50mm"/><!-- Claim Person Name --> 
         <fo:table-column column-width="30mm"/><!-- Claim Estimated --> 
         <fo:table-column column-width="30mm"/><!-- Claim Approved --> 
         <fo:table-column column-width="30mm"/><!-- Claim R/I --> 
         <fo:table-column column-width="30mm"/><!-- Claim Ratio --> 
   
         <fo:table-column />   
         <fo:table-body>   
           <fo:table-row>   
             <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell><!-- No --> 
             <fo:table-cell ><fo:block text-align="center">Policy No.</fo:block></fo:table-cell><!-- Policy No. --> 
             <fo:table-cell ><fo:block text-align="center">PLA No.</fo:block></fo:table-cell><!-- PLA No. --> 
             <fo:table-cell ><fo:block text-align="center">DLA No.</fo:block></fo:table-cell><!-- DLA No. --> 
             <fo:table-cell ><fo:block text-align="center">Customer Name</fo:block></fo:table-cell><!-- Customer Name --> 
             <fo:table-cell ><fo:block text-align="center">Customer Address</fo:block></fo:table-cell><!-- Customer Address --> 
             <fo:table-cell ><fo:block text-align="center">Inception Date</fo:block></fo:table-cell><!-- Inception Date --> 
             <fo:table-cell ><fo:block text-align="center">Expired Date</fo:block></fo:table-cell><!-- Expired Date --> 
             <fo:table-cell ><fo:block text-align="center">Claim Date</fo:block></fo:table-cell><!-- Claim Date --> 
             <fo:table-cell ><fo:block text-align="center">Claim Object</fo:block></fo:table-cell><!-- Claim Object --> 
             <fo:table-cell ><fo:block text-align="center">Sum Insured</fo:block></fo:table-cell><!-- Sum Insured --> 
             <fo:table-cell ><fo:block text-align="center">Premium</fo:block></fo:table-cell><!-- Premium --> 
             <fo:table-cell ><fo:block text-align="center">Claim Caused</fo:block></fo:table-cell><!-- Claim Caused --> 
             <fo:table-cell ><fo:block text-align="center">Claim Location</fo:block></fo:table-cell><!-- Claim Location --> 
             <fo:table-cell ><fo:block text-align="center">Claim Person Name</fo:block></fo:table-cell><!-- Claim Person Name --> 
             <fo:table-cell ><fo:block text-align="center">Claim Estimated</fo:block></fo:table-cell><!-- Claim Estimated --> 
             <fo:table-cell ><fo:block text-align="center">Claim Approved</fo:block></fo:table-cell><!-- Claim Approved --> 
             <fo:table-cell ><fo:block text-align="center">Claim R/I</fo:block></fo:table-cell><!-- Claim R/I --> 
             <fo:table-cell ><fo:block text-align="center">Claim Ratio</fo:block></fo:table-cell><!-- Claim Ratio --> 
           </fo:table-row>   
   
   <!-- GARIS DALAM KOLOM -->   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="19">  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>   
   
<%   
   
   
   BigDecimal [] t = new BigDecimal[30];   
   
   
   for (int i = 0; i < l.size(); i++) {   
      InsurancePolicyView pol = (InsurancePolicyView) l.get(i);   
   
      InsurancePolicyObjectView clo = pol.getClaimObject();   
   
      if (clo==null) clo = new InsurancePolicyObjDefaultView();   
   
      int n=0;   
      t[n] = BDUtil.add(t[n++], clo.getDbObjectInsuredAmount());   
      t[n] = BDUtil.add(t[n++], clo.getDbObjectPremiAmount());   
      t[n] = BDUtil.add(t[n++], pol.getDbClaimAmountEstimate());   
      t[n] = BDUtil.add(t[n++], pol.getDbClaimAmountApproved());   
      t[n] = BDUtil.add(t[n++], pol.getDbClaimREAmount());   
   
      int p=-1;   
   
      final DTOList objects = pol.getObjects();   
   
      while (true) {   
         p++;   
         if (p>=objects.size()) break;   
   
         final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(p);   
   
   
%>   
            <fo:table-row>   
             <fo:table-cell ><fo:block><%=i+1%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No. --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStPLANo())%></fo:block></fo:table-cell>    <!-- No --><!-- PLA No. --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStDLANo())%></fo:block></fo:table-cell>    <!-- No --><!-- DLA No. --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>    <!-- No --><!-- Customer Name --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerAddress())%></fo:block></fo:table-cell>    <!-- No --><!-- Customer Address --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtPeriodStart())%></fo:block></fo:table-cell>    <!-- No --><!-- Inception Date --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>    <!-- No --><!-- Expired Date --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtClaimDate())%></fo:block></fo:table-cell>    <!-- No --><!-- Claim Date --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(clo==null?"?":clo.getStObjectDescription())%></fo:block></fo:table-cell>    <!-- No --><!-- Claim Object --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(clo.getDbObjectInsuredAmount(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Sum Insured --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(clo.getDbObjectPremiAmount(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimCauseDesc())%></fo:block></fo:table-cell>    <!-- No --><!-- Claim Caused --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimEventLocation())%></fo:block></fo:table-cell>    <!-- No --><!-- Claim Location --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimPersonName())%></fo:block></fo:table-cell>    <!-- No --><!-- Claim Person Name --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmountEstimate(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Claim Estimated --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimAmountApproved(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Claim Approved --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbClaimREAmount(),2)%></fo:block></fo:table-cell>    <!-- No --><!-- Claim R/I --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printPct(clo.getDbClaimRatio(),3)%>%</fo:block></fo:table-cell>    <!-- No --><!-- Claim Ratio --> 
   
           </fo:table-row>   
   
<%   
      }   
   
      }%>   
   
   <!-- GARIS DALAM KOLOM -->   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="19">  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>   
   
           <fo:table-row>   
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- No --> 
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- Policy No. --> 
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- PLA No. --> 
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- DLA No. --> 
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- Customer Name --> 
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- Customer Address --> 
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- Inception Date --> 
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- Expired Date --> 
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- Claim Date --> 
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell><!-- Claim Object --> 
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell><!-- Sum Insured --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell><!-- Premium --> 
             <fo:table-cell ><fo:block text-align="end"> </fo:block></fo:table-cell><!-- Claim Caused --> 
             <fo:table-cell ><fo:block text-align="end"> </fo:block></fo:table-cell><!-- Claim Location --> 
             <fo:table-cell ><fo:block text-align="end"> </fo:block></fo:table-cell><!-- Claim Person Name --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2],2)%></fo:block></fo:table-cell><!-- Claim Estimated --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3],2)%></fo:block></fo:table-cell><!-- Claim Approved --> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[4],2)%></fo:block></fo:table-cell><!-- Claim R/I --> 
             <fo:table-cell ><fo:block text-align="end"> </fo:block></fo:table-cell><!-- Claim Ratio --> 
   
           </fo:table-row>   
   
   <!-- GARIS DALAM KOLOM -->   
   
                    <fo:table-row>   
                      <fo:table-cell number-columns-spanned="19" >  
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>   

         </fo:table-body>   
       </fo:table>   
       </fo:block>   
   
    </fo:flow>   
  </fo:page-sequence>   
</fo:root>   

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
                  page-width="41.7cm"
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
            line-height="12pt" >
        Insurance Policy - PT. Asuransi Bangun Askrida Page:<fo:page-number/>
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">
      <fo:block font-size="8pt">
<% if (form.getExpirePeriodFrom()!=null || form.getExpirePeriodTo()!=null) { %>
         Expire Date From : <%=JSPUtil.printX(form.getExpirePeriodFrom())%>
         Expire Date To   : <%=JSPUtil.printX(form.getExpirePeriodTo())%>
<% } %>


<% if (form.getStFltCoverType()!=null) { %>
         Cover Type       : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>
<% } %>

<% if (form.getStFltClaimStatus()!=null) { %>
         Claim Status       : <%=JSPUtil.printX(form.getStFltClaimStatusDesc())%>
<% } %>


         Period From      : <%=JSPUtil.printX(form.getPeriodFrom())%>
         Period To        : <%=JSPUtil.printX(form.getPeriodTo())%>

         Policy Date From : <%=JSPUtil.printX(form.getPolicyDateFrom())%>
         Policy Date To   : <%=JSPUtil.printX(form.getPolicyDateTo())%>

<% if (form.getStCustCategory1()!=null) { %>
         Cust Category    : <%=JSPUtil.printX(form.getStCustCategory1Desc())%>
<% } %>

<% if (form.getStRiskCode()!=null) { %>
         Risk Code       : <%=JSPUtil.printX(form.getStRiskCode())%>
<% } %>

<% if (form.getStRiskLocation()!=null) { %>
         Risk Location       : <%=JSPUtil.printX(form.getStRiskLocation())%>
<% } %>

<% if (form.getStRiskCardNo()!=null) { %>
         Risk Card No       : <%=JSPUtil.printX(form.getStRiskCardNo())%>
<% } %>

<% if (form.getStPostCode()!=null) { %>
         Post Code         : <%=JSPUtil.printX(form.getStPostCode())%>
<% } %>


<% if (form.getStEntityID()!=null) { %>
         Post Code         : <%=JSPUtil.printX(form.getStEntityName())%>
<% } %>


         Branch           : <%=JSPUtil.printX(form.getStBranchDesc())%>
         Tgl              : <%=DateUtil.getDateStr(new Date(),"d MMM yyyy")%>
      </fo:block>

      <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>

         <fo:table-column column-width="20mm"/>

         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>

         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>


         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>

         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>

         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>

         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>

         <fo:table-column />
         <fo:table-body>
           <fo:table-row>

             <fo:table-cell ><fo:block>No</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Pol No</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>PLA</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>DLA</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Cusname</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Cusad</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Pstart</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>PE</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>CD</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>OD</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>OAI</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>OPA</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>CCD</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>CEL</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>CPN</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>CAE</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>CAA</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>CRE</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>CRO</fo:block></fo:table-cell>
           </fo:table-row>

<%


   BigDecimal [] t = new BigDecimal[10];


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

      /*final DTOList objects = pol.getObjects();

      while (true) {
         p++;
         if (p>=objects.size()) break;

         final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(p);*/


%>
            <fo:table-row>
             <fo:table-cell ><fo:block><%=i+1%></fo:block></fo:table-cell>
             <%--<fo:table-cell ><fo:block><%=clo.getStPolicyObjectID()%></fo:block></fo:table-cell>--%>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStPLANo())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStDLANo())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtPeriodStart())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtClaimDate())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(clo==null?"?":clo.getStObjectDescription())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(clo.getDbObjectInsuredAmount())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(clo.getDbObjectPremiTotalAmount(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimCauseDesc())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimEventLocation())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimPersonName())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbClaimAmountEstimate())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbClaimAmountApproved())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbClaimREAmount())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printPct(clo.getDbClaimRatio(),3)%>%</fo:block></fo:table-cell>

           </fo:table-row>

<%
      //}

      }%>
           <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[2],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[3],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[4],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>

           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

    </fo:flow>
  </fo:page-sequence>
</fo:root>

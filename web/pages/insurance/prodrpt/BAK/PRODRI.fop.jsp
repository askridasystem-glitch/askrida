<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil,
                 java.math.BigDecimal,
                 com.crux.web.form.FormManager,
                 com.crux.web.controller.SessionManager,
                 com.webfin.insurance.form.ProductionReportForm,
                 java.util.Date,
                 com.crux.common.model.HashDTO"%><?xml version="1.0" encoding="utf-8"?>
<%

   final DTOList l = (DTOList) request.getAttribute("RPT");

   final ProductionReportForm form = (ProductionReportForm)SessionManager.getInstance().getCurrentForm();

   //if (true) throw new NullPointerException();

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->
    <fo:simple-page-master master-name="only"
                  page-height="21cm"
                  page-width="160.7cm"
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
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("pol_no"))%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(("treaty_type"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_ent_id"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_ent_name"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("treaty_type"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_claim_amount"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_premi_amount"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_sharepct"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_tsi_amount"))%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_premi_amt_FAC"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_premi_amt_FACO"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_premi_amt_FACP"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_premi_amt_OR"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_premi_amt_QS"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_premi_amt_QSKR"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_premi_amt_SPL"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_premi_amt_XOL1"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_premi_amt_XOL2"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_premi_amt_XOL3"))%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_claim_amt_FAC"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_claim_amt_FACO"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_claim_amt_FACP"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_claim_amt_OR"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_claim_amt_QS"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_claim_amt_QSKR"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_claim_amt_SPL"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_claim_amt_XOL1"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_claim_amt_XOL2"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_claim_amt_XOL3"))%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_tsi_amt_FAC"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_tsi_amt_FACO"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_tsi_amt_FACP"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_tsi_amt_OR"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_tsi_amt_QS"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_tsi_amt_QSKR"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_tsi_amt_SPL"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_tsi_amt_XOL1"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_tsi_amt_XOL2"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(("ri_tsi_amt_XOL3"))%></fo:block></fo:table-cell>
           </fo:table-row>

<%

  
   HashDTO totz = new HashDTO();

   totz.setFieldValueByFieldName("ri_ent_name","TOTAL");

   totz.setFieldValueByFieldName("ri_claim_amount",l.getTotal("ri_claim_amount"));
   totz.setFieldValueByFieldName("ri_premi_amt_FAC",l.getTotal("ri_premi_amt_FAC"));
   totz.setFieldValueByFieldName("ri_premi_amt_FACO",l.getTotal("ri_premi_amt_FACO"));
   totz.setFieldValueByFieldName("ri_premi_amt_FACP",l.getTotal("ri_premi_amt_FACP"));
   totz.setFieldValueByFieldName("ri_premi_amt_OR",l.getTotal("ri_premi_amt_OR"));
   totz.setFieldValueByFieldName("ri_premi_amt_QS",l.getTotal("ri_premi_amt_QS"));
   totz.setFieldValueByFieldName("ri_premi_amt_QSKR",l.getTotal("ri_premi_amt_QSKR"));

  l.add(totz);
  
   for (int i = 0; i < l.size(); i++) {
      HashDTO pol = (HashDTO) l.get(i);

      if (i==0) System.out.println(pol.getKeys());

      boolean isTotalLine = i==l.size()-1;


%>

<% if (isTotalLine) {%>
            <fo:table-row>
               <!--************* CETAK GARIS DISINI *************************-->
               <fo:table-cell number-columns-spanned="12" >
                     <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block>
               </fo:table-cell>
            </fo:table-row>
<% }%>

            <fo:table-row>
             <fo:table-cell ><fo:block><%=isTotalLine?"":String.valueOf(i)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("pol_no"))%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName(""))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_ent_id"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_ent_name"))%></fo:block></fo:table-cell>


             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("treaty_type"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_claim_amount"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_premi_amount"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_sharepct"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_tsi_amount"))%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_premi_amt_FAC"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_premi_amt_FACO"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_premi_amt_FACP"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_premi_amt_OR"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_premi_amt_QS"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_premi_amt_QSKR"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_premi_amt_SPL"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_premi_amt_XOL1"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_premi_amt_XOL2"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_premi_amt_XOL3"))%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_claim_amt_FAC"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_claim_amt_FACO"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_claim_amt_FACP"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_claim_amt_OR"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_claim_amt_QS"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_claim_amt_QSKR"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_claim_amt_SPL"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_claim_amt_XOL1"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_claim_amt_XOL2"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_claim_amt_XOL3"))%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_tsi_amt_FAC"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_tsi_amt_FACO"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_tsi_amt_FACP"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_tsi_amt_OR"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_tsi_amt_QS"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_tsi_amt_QSKR"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_tsi_amt_SPL"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_tsi_amt_XOL1"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_tsi_amt_XOL2"))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getFieldValueByFieldName("ri_tsi_amt_XOL3"))%></fo:block></fo:table-cell>


           </fo:table-row>

<%
      //}

      }%>
         </fo:table-body>
       </fo:table>
       </fo:block>

    </fo:flow>
  </fo:page-sequence>
</fo:root>

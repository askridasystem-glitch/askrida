<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil,
                 java.math.BigDecimal,
                 com.crux.web.form.FormManager,
                 com.crux.web.controller.SessionManager,
                 com.webfin.insurance.form.ProductionReportForm"%><?xml version="1.0" encoding="utf-8"?>
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
         Expire Date From : <%=JSPUtil.printX(form.getExpirePeriodFrom())%>
         Expire Date To : <%=JSPUtil.printX(form.getExpirePeriodTo())%>

         Period From : <%=JSPUtil.printX(form.getPeriodFrom())%>
         Period To : <%=JSPUtil.printX(form.getPeriodTo())%>

         Policy Date From : <%=JSPUtil.printX(form.getPolicyDateFrom())%>
         Policy Date To : <%=JSPUtil.printX(form.getPolicyDateTo())%>

         Cover Type  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>
         Cust Category : <%=JSPUtil.printX(form.getStCustCategory1Desc())%>
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
             <fo:table-cell ><fo:block>Policy No</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Name of Insured</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Address of Insured</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Kode Pos</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Entity ID</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Entity Name</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Period</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Ccy</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Sum Insured</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Premium</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Adm Cost</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Discount</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Total Comm</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>PremiNet</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Producer</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Producer ID</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>User</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>User Code</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Branch</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Branch Code</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>SPPA No</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>SPPA Date</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Proposal No</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Proposal Date</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Policy Nomerator</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Policy Date</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>Uraian</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>Premi Rate 1</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Premi Rate 2</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Premi Rate 3</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Premi Rate 4</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>Premi 1</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Premi 2</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Premi 3</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Premi 4</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>Komisi 1</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Komisi 2</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Komisi 3</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Komisi 4</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>Discount 1 Pct</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Discount 1</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Discount 2 Pct</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Discount 2</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>Brokerage 1 Pct</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Brokerage 1 </fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>HFee 1 Pct</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>HFee 1 </fo:block></fo:table-cell>
           </fo:table-row>

<%


   BigDecimal [] t = new BigDecimal[10];


   for (int i = 0; i < l.size(); i++) {
      InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

      int n=0;
      t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());
      t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());
      t[n] = BDUtil.add(t[n++], pol.getDbTotalFee());
      t[n] = BDUtil.add(t[n++], pol.getDbTotalDisc());
      t[n] = BDUtil.add(t[n++], pol.getDbTotalComm());
      t[n] = BDUtil.add(t[n++], pol.getDbPremiNetto());

      int p=-1;

      /*final DTOList objects = pol.getObjects();

      while (true) {
         p++;
         if (p>=objects.size()) break;

         final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(p);*/


%>
            <fo:table-row>
             <fo:table-cell ><fo:block><%=i%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStEntityPostalCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStEntityID())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStEntityName())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbTotalFee())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbTotalDisc(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbTotalComm(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbPremiNetto(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStProducerName())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStProducerID())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCreateWho())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCreateWho())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCostCenterDesc())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCostCenterCode())%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStSPPANoTrace())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtSPPADateTrace())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStProposalNo())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStProposalDate())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStNomerator())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStObjectDescription())%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDPremiRate1(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDPremiRate2(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDPremiRate3(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDPremiRate4(),2)%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDPremi1(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDPremi2(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDPremi3(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDPremi4(),2)%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDComm1(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDComm2(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDComm3(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDComm4(),2)%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDDisc1Pct(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDDisc1(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDDisc2Pct(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDDisc2(),2)%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDBrok1Pct(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDBrok1(),2)%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDHFeePct(),2)%> </fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbNDHFee(),2)%> </fo:block></fo:table-cell>

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
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[2])%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[3],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[4],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[5],2)%></fo:block></fo:table-cell>
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
            <fo:table-cell ><fo:block></fo:block></fo:table-cell>

            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
            <fo:table-cell ><fo:block></fo:block></fo:table-cell>

            <fo:table-cell ><fo:block>Discount 1 Pct</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Discount 1</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Discount 2 Pct</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Discount 2</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>Brokerage 1 Pct</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Brokerage 1 </fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>HFee 1 Pct</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>HFee 1 </fo:block></fo:table-cell>

           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

    </fo:flow>
  </fo:page-sequence>
</fo:root>

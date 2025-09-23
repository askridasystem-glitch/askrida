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

      <!-- defines text title level 1-->

      <fo:block font-size="18pt" line-height="16pt" space-after.optimum="2pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       {L-ENG Production Report (ALL PARAMETERS)-L}{L-INA Laporan Produksi (SEMUA PARAMETER)-L}
      </fo:block>

      <fo:block font-size="18pt" line-height="16pt" space-after.optimum="2pt"
            color="black"
            text-align="center"
            padding-top="10pt">
         From : <%=JSPUtil.printX(form.getPolicyDateFrom())%> UP TO <%=JSPUtil.printX(form.getPolicyDateTo())%>
      </fo:block>

      <!-- Normal text -->
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
         Expire Date From : <%=JSPUtil.printX(form.getExpirePeriodFrom())%>
      </fo:block>

   <!-- GARIS  -->
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt"></fo:block>

      <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="10mm"/>    <!-- No -->
         <fo:table-column column-width="30mm"/>    <!-- Policy No -->
         <fo:table-column column-width="50mm"/>    <!-- The Insured -->
         <fo:table-column column-width="50mm"/>    <!-- Address-->
         <fo:table-column column-width="15mm"/>    <!-- Kodepos -->
         <fo:table-column column-width="20mm"/>    <!-- Entity ID -->
         <fo:table-column column-width="50mm"/>    <!-- Entity Name -->
         <fo:table-column column-width="15mm"/>    <!-- Period -->
         <fo:table-column column-width="10mm"/>    <!-- CCY -->
         <fo:table-column column-width="40mm"/>    <!-- Sum Insured -->
         <fo:table-column column-width="30mm"/>    <!-- Premium -->
         <fo:table-column column-width="30mm"/>    <!-- Adm Cost -->
         <fo:table-column column-width="30mm"/>    <!-- Discount -->
         <fo:table-column column-width="30mm"/>    <!-- Total Comm -->
         <fo:table-column column-width="30mm"/>    <!-- Premi Net -->
         <fo:table-column column-width="50mm"/>    <!-- Producer -->
         <fo:table-column column-width="20mm"/>    <!-- Producer ID -->
         <fo:table-column column-width="30mm"/>    <!-- User -->
         <fo:table-column column-width="20mm"/>    <!-- User Code -->
         <fo:table-column column-width="40mm"/>    <!-- Branch -->
         <fo:table-column column-width="10mm"/>    <!-- Branch Code -->
         <fo:table-column column-width="30mm"/>    <!-- SPPA No -->
         <fo:table-column column-width="17mm"/>    <!-- SPPA Date -->
         <fo:table-column column-width="30mm"/>    <!-- Proposal No -->
         <fo:table-column column-width="17mm"/>    <!-- Proposal Date -->
         <fo:table-column column-width="20mm"/>    <!-- Nomerator -->
         <fo:table-column column-width="17mm"/>    <!-- Policy Date -->

         <fo:table-column column-width="50mm"/>    <!-- Uraian -->

         <fo:table-column column-width="20mm"/>    <!-- Rate 1 -->
         <fo:table-column column-width="20mm"/>    <!-- Rate 2 -->
         <fo:table-column column-width="20mm"/>    <!-- Rate 3 -->
         <fo:table-column column-width="20mm"/>    <!-- Rate 4 -->

         <fo:table-column column-width="30mm"/>    <!-- Premi 1 -->
         <fo:table-column column-width="30mm"/>    <!-- Premi 2 -->
         <fo:table-column column-width="30mm"/>    <!-- Premi 3 -->
         <fo:table-column column-width="30mm"/>    <!-- Premi 4 -->


         <fo:table-column column-width="30mm"/>    <!-- Komisi 1 -->
         <fo:table-column column-width="30mm"/>    <!-- Komisi 2 -->
         <fo:table-column column-width="30mm"/>    <!-- Komisi 3 -->
         <fo:table-column column-width="30mm"/>    <!-- Komisi 4 -->

         <fo:table-column column-width="10mm"/>    <!-- Discount 1 Pct -->
         <fo:table-column column-width="30mm"/>    <!-- Discount 1 -->
         <fo:table-column column-width="10mm"/>    <!-- Discount 2 Pct -->
         <fo:table-column column-width="30mm"/>    <!-- Discount 2 -->

         <fo:table-column column-width="10mm"/>    <!-- Broker 1 Pct -->
         <fo:table-column column-width="30mm"/>    <!-- Broker 1 -->
         
         <fo:table-column column-width="10mm"/>    <!-- HFee 1 Pct -->
         <fo:table-column column-width="30mm"/>    <!-- HFee 1 -->

         <fo:table-column />
         <fo:table-body>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG No. Policy.-L}{L-INA No. Polis -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Address of Insured-L}{L-INA Alamat-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Post Code -L}{L-INA Kode Pos -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Entity ID-L}{L-INA Kode Agen-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Entity Name -L}{L-INA Nama Agen-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Period-L}{L-INA J. Waktu-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG CCY-L}{L-INA Valuta-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Sum Insured -L}{L-INA H. Pertanggungan-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Premium-L}{L-INA Premi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Adm Cost -L}{L-INA Biaya Adm-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Discount-L}{L-INA Discount-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Total Comm-L}{L-INA Total Komisi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG PremiNet-L}{L-INA PremiNett-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Producer-L}{L-INA Agen-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Producer ID-L}{L-INA Kd. Agen-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG User-L}{L-INA User-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG User Code-L}{L-INA Kd. User-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Branch-L}{L-INA Cabang-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Branch Code-L}{L-INA Kd. Cabang-L}</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>{L-ENG SPPA No-L}{L-INA No. SPPA-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG SPPA Date-L}{L-INA Tgl. SPPA-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Proposal No.-L}{L-INA No. Proposal-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Proposal Date-L}{L-INA Tgl. Proposal-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Nomerator-L}{L-INA Nomerator-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Policy Date-L}{L-INA Tgl. Polis-L}</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>{L-ENG Description-L}{L-INA Uraian-L}</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>{L-ENG Rate 1-L}{L-INA Rate1-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Rate 2-L}{L-INA Rate2-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Rate 3-L}{L-INA Rate3-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Rate 4-L}{L-INA Rate4-L}</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>{L-ENG Premi 1-L}{L-INA Premi1-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Premi 2-L}{L-INA Premi2-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Premi 3-L}{L-INA Premi3-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Premi 4-L}{L-INA Premi4-L}</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>{L-ENG Comm 1-L}{L-INA Komisi 1-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Comm 2-L}{L-INA Komisi 2-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Comm 3-L}{L-INA Komisi 3-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Comm 4-L}{L-INA Komisi 4-L}</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>{L-ENG Discount 1 Pct-L}{L-INA Diskon 1 Pct-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Discount 1-L}{L-INA Diskon 1-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Discount 2 Pct-L}{L-INA Diskon 2 Pct-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Discount 2-L}{L-INA Diskon 2-L}</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>{L-ENG Brokerage 1 Pct-L}{L-INA Brokerage 1 Pct-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG Brokerage 1-L}{L-INA Brokerage 1-L}</fo:block></fo:table-cell>

             <fo:table-cell ><fo:block>{L-ENG HFee 1 Pct-L}{L-INA HFee 1 Pct -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG HFee 1-L}{L-INA HFee 1-L}</fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="32" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

<%


   BigDecimal [] t = new BigDecimal[10];


   for (int i = 1; i < l.size(); i++) {
      InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

      int n=0;
      t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());
      t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());
      t[n] = BDUtil.add(t[n++], pol.getDbTotalFee());
      t[n] = BDUtil.add(t[n++], pol.getDbTotalDisc());
      t[n] = BDUtil.add(t[n++], pol.getDbTotalComm());
      t[n] = BDUtil.add(t[n++], pol.getDbPremiNetto());

      int p=-1;

      final DTOList objects = pol.getObjects();

      while (true) {
         p++;
         if (p>=objects.size()) break;

         final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(p);


%>
            <fo:table-row>
             <fo:table-cell ><fo:block><%=i%></fo:block></fo:table-cell>    <!-- No -->
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
      }

      }%>

   <!-- GARIS DALAM KOLOM -->

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="32" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

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

   <!-- GARIS DALAM KOLOM -->

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="32" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

    </fo:flow>
  </fo:page-sequence>
</fo:root>



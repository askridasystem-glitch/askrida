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
                  page-width="35cm"
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
        ProdReport - PT. Asuransi Bangun Askrida Page:<fo:page-number/>
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">

      <!-- defines text title level 1-->

      <fo:block font-size="18pt" line-height="16pt" space-after.optimum="2pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       {L-ENG Production Report (per Customer)-L}{L-INA Laporan Produksi (per Nasabah)-L}
      </fo:block>

      <fo:block font-size="18pt" line-height="16pt" space-after.optimum="2pt"
            color="black"
            text-align="center"
            padding-top="10pt">
         From : <%=JSPUtil.printX(form.getPolicyDateFrom())%>{L-ENG up to-L}{L-INA s/d-L} <%=JSPUtil.printX(form.getPolicyDateTo())%>
      </fo:block>

      <!-- Normal text -->
      <fo:block font-size="8pt">

      </fo:block>

      <fo:block font-size="8pt">
         {L-ENG Customer :-L}{L-INA Nasabah :-L}<%=JSPUtil.printX(form.getStCustCategory1Desc())%>
      </fo:block>

   <!-- GARIS  -->
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt"></fo:block>

      <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="10mm"/>    <!-- No -->
         <fo:table-column column-width="20mm"/>    <!-- Branch -->
         <fo:table-column column-width="30mm"/>    <!-- Policy No -->
         <fo:table-column column-width="50mm"/>    <!-- The Insured -->
         <fo:table-column column-width="15mm"/>    <!-- Tgl Polis -->
         <fo:table-column column-width="15mm"/>    <!-- Mulai -->
         <fo:table-column column-width="15mm"/>    <!-- Sampai -->
         <fo:table-column column-width="10mm"/>    <!-- CCY -->
         <fo:table-column column-width="30mm"/>    <!-- Sum Insured -->
         <fo:table-column column-width="25mm"/>    <!-- Premium -->
         <fo:table-column column-width="25mm"/>    <!-- Discount -->
         <fo:table-column column-width="25mm"/>    <!-- Adm Cost -->
         <fo:table-column column-width="25mm"/>    <!-- Total Comm -->
         <fo:table-column column-width="25mm"/>    <!-- Premi Net -->


         <fo:table-column />
         <fo:table-body>
           <fo:table-row>
             <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">{L-ENG Branch-L}{L-INA Cabang-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">{L-ENG No. Policy.-L}{L-INA No. Polis -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Date-L}{L-INA Tgl. Polis-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">{L-ENG From-L}{L-INA Mulai-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">{L-ENG To-L}{L-INA Sampai-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">{L-ENG CCY-L}{L-INA Valuta-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">{L-ENG Sum Insured -L}{L-INA H. Pertanggungan-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">{L-ENG Premium-L}{L-INA Premi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">{L-ENG Discount -L}{L-INA Diskon-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">{L-ENG Adm. Cost-L}{L-INA Biaya Adm.-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">{L-ENG Total Comm-L}{L-INA Total Komisi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">{L-ENG PremiNet-L}{L-INA PremiNett-L}</fo:block></fo:table-cell>

           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="14" >
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
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCostCenterDesc())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtPeriodStart())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtPeriodEnd())%> </fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalDisc(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalFee())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalComm(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiNetto(),2)%></fo:block></fo:table-cell>

           </fo:table-row>

<%
      }

      }%>

   <!-- GARIS DALAM KOLOM -->

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="14" >
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
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[2])%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[4],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[5],2)%></fo:block></fo:table-cell>

           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="14" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

    </fo:flow>
  </fo:page-sequence>
</fo:root>



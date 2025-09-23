<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

   final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
   final String fontsize = (String) request.getAttribute("FONTSIZE");
   pol.loadClausules();

   //if (true) throw new NullPointerException();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->

    <fo:simple-page-master master-name="only"
                  page-height="29.7cm"
                  page-width="21cm"
                  margin-top="1cm"
                  margin-bottom="2cm"
                  margin-left="2.5cm"
                  margin-right="2.5cm">

      <fo:region-body margin-top="1cm" margin-bottom="1.5cm"/>
      <fo:region-before extent="3cm"/>
      <fo:region-after extent="1.5cm"/>
    </fo:simple-page-master>

  </fo:layout-master-set>
  <!-- end: defines page layout -->

  <!-- actual layout -->
  <fo:page-sequence master-reference="only" initial-page-number="1">

    <!-- usage of page layout -->
    <!-- HEADER -->

    <fo:static-content flow-name="xsl-region-before">
      <fo:block text-align="end"
            font-size="6pt"
            font-family="TAHOMA"
            line-height="12pt" >
        Insurance Policy - PT. Asuransi Bangun Askrida NOMERATOR : <%=JSPUtil.printX(pol.getStNomerator())%> PrintCode:<%=pol.getStPrintCode()%> Page:<fo:page-number/>
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">

   <!-- GARIS  -->
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>

      <!-- defines text title level 1-->

      <fo:block font-size="16pt" font-family="TAHOMA" line-height="16pt" space-after.optimum="10pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       {L-ENG Insurance -L}{L-INA Asuransi -L}<%=pol.getStPolicyTypeDesc()%> 
      </fo:block>

      <!-- Normal text -->

<!-- DYNAMIC HEADER WORDING -->


   <!-- GARIS -->
     <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>

      <!-- Normal text -->

   <!-- ROW -->
      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="20pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc()%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStCustomerName()%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Address-L}{L-INA Alamat-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStCustomerAddress()%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG See Attached-L}{L-INA Terlampir-L}</fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Inception Date-L}{L-INA Awal Pertanggungan-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG See Attached-L}{L-INA Terlampir-L}</fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Expiry Date-L}{L-INA Akhir Pertanggungan -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG See Attached-L}{L-INA Terlampir-L}</fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Clauses-L}{L-INA Klausula -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG See Attached-L}{L-INA Terlampir-L}</fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Interest Insured-L}{L-INA Yang Dipertanggungkan -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG See Attached-L}{L-INA Terlampir-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                 <fo:table-cell ><fo:block text-align="left"><%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Coverage-L}{L-INA Jaminan -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG See Attached-L}{L-INA Terlampir-L}</fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Rate-L}{L-INA Suku Premi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG See Attached-L}{L-INA Terlampir-L}</fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Deductible-L}{L-INA Risiko Sendiri -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>{L-ENG See Attached-L}{L-INA Terlampir-L}</fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Total Premium-L}{L-INA Jumlah Premi -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                 <fo:table-cell ><fo:block text-align="left"><%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
           </fo:table-row>



         </fo:table-body>
       </fo:table>
       </fo:block>


   <!-- ROW -->
      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="2pt" text-align="justify" > </fo:block>


       <fo:block font-size="<%=fontsize%>"
                font-family="sans-serif"
                line-height="10pt"
                space-after.optimum="15pt"
                text-align="center" >
                {L-ENG SAY :-L}{L-INA TERBILANG :-L} <%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbTotalDue(),2),"RUPIAH",NumberSpell.INDONESIA)%>
      </fo:block>

       <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="60mm"/>
         <fo:table-column column-width="80mm"/>
         <fo:table-body>
            <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell >
               <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d MMM yyyy")%></fo:block>
               <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>

             </fo:table-cell>
           </fo:table-row>
        </fo:table-body>
       </fo:table>
       </fo:block>

    </fo:flow>
  </fo:page-sequence>
</fo:root>




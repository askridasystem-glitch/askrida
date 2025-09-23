<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView"%><?xml version="1.0" encoding="utf-8"?>
<%

   final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
   pol.loadClausules();
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
    <!-- header -->
    <fo:static-content flow-name="xsl-region-before">
      <fo:block text-align="end"
            font-size="6pt"
            font-family="TAHOMA"
            line-height="12pt" >
        Insurance Policy - PT. Asuransi Bangun Askrida PrintCode:<%=pol.getStPrintCode()%> Page:<fo:page-number/>
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">

// BUAT GARIS
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>

      <!-- defines text title level 1-->

      <fo:block font-size="16pt" font-family="TAHOMA" line-height="16pt" space-after.optimum="10pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       Lampiran Polis Asuransi <%=pol.getStPolicyTypeDesc()%> 
      </fo:block>

      <!-- Normal text -->

// BUAT GARIS
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>

// JARAK ROW	
      <fo:block font-size="5pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt" text-align="justify" > </fo:block>

      <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Policy No.-L}{L-INA No. Polis-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= pol.getStPolicyNo()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Jenis Asuransi</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Nama Tertanggung</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStCustomerName()%></fo:block></fo:table-cell>
           </fo:table-row>
 
          <fo:table-row>
             <fo:table-cell ><fo:block>Alamat</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStCustomerAddress()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Jangka Waktu</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStPeriodLength()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Awal Pertanggungan</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.print(pol.getDtPeriodStart())%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Akhir Pertanggungan</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.print(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

// JARAK ROW	
      <fo:block font-size="5pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt" text-align="justify" > </fo:block>

// BUAT GARIS
     <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>

       <fo:block font-size="10pt" space-before.optimum="10pt">
       URAIAN PERTANGGUNGAN
      </fo:block>
// JARAK ROW	
      <fo:block font-size="5pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt" text-align="justify" > </fo:block>

// BUAT GARIS	
     <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>

// JARAK ROW	
      <fo:block font-size="5pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt" text-align="justify" > </fo:block>

<%
   final DTOList objects = pol.getObjects();

   final FlexFieldHeaderView objectMap = pol.getPolicyType().getObjectMap();

   final DTOList objectMapDetails = objectMap==null?null:objectMap.getDetails();

   for (int i = 0; i < objects.size(); i++) {
      InsurancePolicyObjectView io = (InsurancePolicyObjectView) objects.get(i);

      %>
      <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>
      <%

      if (objectMapDetails!=null)
            for (int j = 0; j < objectMapDetails.size(); j++) {
               FlexFieldDetailView iomd = (FlexFieldDetailView) objectMapDetails.get(j);

               final Object desc = iomd.getDesc(io);

%>
            <fo:table-row>
             <fo:table-cell ><fo:block><%=JSPUtil.print(iomd.getStFieldDesc())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.print(desc)%></fo:block></fo:table-cell>
           </fo:table-row>
               <%
            }
      else {

         final InsurancePolicyVehicleView veh = (InsurancePolicyVehicleView) io;

               %>
         <%
      }

%>
            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>
            <fo:table-row>
             <fo:table-cell ><fo:block>Harga Pertanggungan</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
               <fo:block font-size="8pt">
              <fo:table table-layout="fixed">
               <fo:table-column column-width="40mm"/>
               <fo:table-column column-width="2mm"/>
               <fo:table-column column-width="5mm"/>
               <fo:table-column column-width="30mm"/>
               <fo:table-body>
<%
   final DTOList suminsureds = io.getSuminsureds();

   for (int j = 0; j < suminsureds.size(); j++) {
      InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);
      %>

            <fo:table-row>
             <fo:table-cell ><fo:block><%=JSPUtil.print(tsi.getStInsuranceTSIDesc())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(tsi.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>
      <%

   }
%>
               </fo:table-body>
                </fo:table>
                </fo:block>
             </fo:block></fo:table-cell>

           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block  >Jaminan / Suku Premi</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
               <fo:block font-size="8pt">
              <fo:table table-layout="fixed">
               <fo:table-column column-width="40mm"/>
               <fo:table-column column-width="2mm"/>
               <fo:table-column column-width="10mm"/>
               <fo:table-column column-width="5mm"/>
               <fo:table-column column-width="20mm"/>
               <fo:table-body>
<%
   final DTOList coverage = io.getCoverage();

   for (int j = 0; j < coverage.size(); j++) {
      InsurancePolicyCoverView cover = (InsurancePolicyCoverView) coverage.get(j);

      %>

            <fo:table-row>
             <fo:table-cell ><fo:block><%=JSPUtil.print(cover.getStInsuranceCoverDesc())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(cover.getDbRatePct(),2)%> %</fo:block></fo:table-cell>
             <%--<fo:table-cell ><fo:block text-align="center">x</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(cover.getDbPremi(),2)%></fo:block></fo:table-cell>--%>
           </fo:table-row>
      <%

   }
%>
               </fo:table-body>
                </fo:table>
                </fo:block>
             </fo:block></fo:table-cell>
           </fo:table-row>
         </fo:table-body>
       </fo:table>
       </fo:block>
<%} %>

// JARAK ROW	
      <fo:block font-size="5pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt" text-align="justify" > </fo:block>

// BUAT GARIS	
     <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>

// JARAK ROW	
      <fo:block font-size="5pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt" text-align="justify" > </fo:block>

        <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>
            <fo:table-row>
             <fo:table-cell ><fo:block>Perhitungan Premi</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
               <fo:table table-layout="fixed">
                  <fo:table-column column-width="30mm"/>
                  <fo:table-column column-width="2mm"/>
                  <fo:table-column column-width="5mm"/>
                  <fo:table-column column-width="30mm"/>
                  <fo:table-column column-width="2mm"/>
                  <fo:table-column column-width="15mm"/>
                  <fo:table-column column-width="2mm"/>
                  <fo:table-column column-width="15mm"/>
                  <fo:table-column column-width="2mm"/>
                  <fo:table-column column-width="5mm"/>
                  <fo:table-column column-width="30mm"/>
                  <fo:table-body>


<%
   for (int i = 0; i < objects.size(); i++) {
      InsurancePolicyObjectView io = (InsurancePolicyObjectView) objects.get(i);

   final DTOList coverage = io.getCoverage();

   for (int j = 0; j < coverage.size(); j++) {
      InsurancePolicyCoverView cover = (InsurancePolicyCoverView) coverage.get(j);

      final boolean entryRate = cover.isEntryRate();

%>
<%if (entryRate) {%>
                     <fo:table-row>
                      <fo:table-cell ><fo:block><%=JSPUtil.print(cover.getInsuranceCoverage().getStDescription())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.print(cover.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">x</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.print(cover.getDbRatePct(),4)%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">x</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.print(pol.getDbPeriodRateDesc())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.print(cover.getDbPremi(),2)%></fo:block></fo:table-cell>
                    </fo:table-row>
<% 
} 
else {
%>
                    <fo:table-row>
                      <fo:table-cell ><fo:block><%=JSPUtil.print(cover.getInsuranceCoverage().getStDescription())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.print(cover.getDbPremi(),2)%></fo:block></fo:table-cell>
                    </fo:table-row>
<% } %>
<% } %>
<% } %>
                 </fo:table-body>
                </fo:table>
             </fo:block></fo:table-cell>
           </fo:table-row>
        </fo:table-body>
       </fo:table>
       </fo:block>

    </fo:flow>
  </fo:page-sequence>
</fo:root>



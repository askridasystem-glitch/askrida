<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil,
                 com.webfin.entity.model.EntityView,
                 com.webfin.ar.model.ARTaxView,
                 java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
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
    <!-- komisi cuma 13.5cm -->

    <fo:simple-page-master master-name="only"
                  page-height="13.5cm"
                  page-width="21cm"
                  margin-top="1cm"
                  margin-bottom="0.5cm"
                  margin-left="1.5cm"
                  margin-right="1.5cm">	

      <fo:region-body margin-top="1cm" margin-bottom="1cm"/>
      <fo:region-before extent="1.5cm"/>
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
            font-family="sans-serif"
            line-height="12pt" >
        Insurance Policy - PT. Asuransi Bangun Askrida Page:<fo:page-number/>
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">

<%
   final DTOList det = pol.getDetails();

   int ln=0;

   for (int i = 0; i < det.size(); i++) {
      InsurancePolicyItemsView dt = (InsurancePolicyItemsView) det.get(i);


      if (!dt.isComission()) continue;

      final EntityView agent = dt.getEntity();

      ln++;

%>
   <%if (ln>1) {%>
   <fo:block break-after="page"></fo:block>
   <% }%>

   <!-- ROW -->
      <fo:block font-size="<%=fontsize%>" font-family="SANS-SERIF" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell ><fo:block>To</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= agent.getStEntityName()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Address</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=agent.getStAddress()%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- ROW -->
                    <fo:table-row><fo:table-cell>
                           <fo:block space-after.optimum="10pt"></fo:block>
                    </fo:table-cell></fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="180mm"/>
         <fo:table-column />
         <fo:table-body>

   <!-- GARIS DALAM KOLOM -->

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="1" >
                           <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block font-size="<%=fontsize%>" text-align="center">{L-ENG COMMISSION NOTE-L}{L-INA NOTA KOMISI-L}</fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="1" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG The Insured-L}{L-INA Nama Tertanggung -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStCustomerName()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Address-L}{L-INA Alamat -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStCustomerAddress()%></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Premium Amount-L}{L-INA Jumlah Premi -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Inception Date-L}{L-INA Awal Pertanggungan-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%> {L-ENG   up to   -L}{L-INA   s/d   -L}<%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="3" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Commission-L}{L-INA Komisi -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(dt.getDbAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

<%
   final ARTaxView tx = dt.getTax();
   
   if (tx!=null) {

%>
           <fo:table-row>
             <fo:table-cell ><fo:block><%=tx.getStDescription()%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>

             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(dt.getDbTaxAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Due To You-L}{L-INA Untuk Saudara -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(dt.getDbNetAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

   <!-- ROW -->
      <fo:block font-size="<%=fontsize%>" font-family="SANS-SERIF" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="180mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="center">{L-ENG SAY :-L}{L-INA TERBILANG :-L} <%=NumberSpell.readNumber(JSPUtil.printX(dt.getDbNetAmount(),2), pol.getStCurrencyCode())%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="1" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

   <!-- ROW -->
      <fo:block font-size="<%=fontsize%>" font-family="SANS-SERIF" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="53mm"/>
         <fo:table-column column-width="53mm"/>
         <fo:table-column column-width="53mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= JSPUtil.printX(pol.getStCostCenterDesc())%>, <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>

<% } %>

         </fo:table-body>
       </fo:table>
       </fo:block>

<% } %>

    </fo:flow>
  </fo:page-sequence>
</fo:root>




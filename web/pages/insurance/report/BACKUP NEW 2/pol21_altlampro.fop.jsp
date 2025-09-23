<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil,
                 java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
<%

   final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
   final String fontsize = (String) request.getAttribute("FONTSIZE");
   pol.loadClausules();
   
   String no_polis = pol.getStPolicyNo();
      
   String no_polis_cetak = no_polis.substring(0,4)+"-"+no_polis.substring(4,8)+"-"+no_polis.substring(8,12)+"-"+no_polis.substring(12,16)+"-"+no_polis.substring(16,18);


   //if (true) throw new NullPointerException();

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->
    <fo:simple-page-master master-name="only"
                  page-height="29.7cm"
                  page-width="21.5cm"
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
        PT. Asuransi Bangun Askrida
      </fo:block>
    <fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
      <fo:retrieve-marker retrieve-class-name="message"
      retrieve-boundary="page"
      retrieve-position="first-starting-within-page"/>
      </fo:block>
</fo:block-container>
<fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
      <fo:retrieve-marker retrieve-class-name="term"
      retrieve-boundary="page"
      retrieve-position="last-ending-within-page"/>
      </fo:block>
</fo:block-container>
    </fo:static-content>
  
  <fo:static-content flow-name="xsl-region-after">
    <fo:block text-align="end"
      font-size="6pt" font-family="serif" line-height="1em + 2pt">
      {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
ref-id="end-of-document"/>
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">
<% {%>
      <!-- defines text title level 1-->

      <fo:block font-size="16pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt"
            color="black"
            text-align="left"
            padding-top="10pt">
       {L-ENG List of Attachement -L}{L-INA Lampiran Polis Asuransi -L} <%=pol.getStPolicyTypeDesc2()%> {L-ENG Insurance -L}{L-INA -L}
      </fo:block>
      
      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt"
            color="black"
            text-align="left"
            padding-top="10pt">
      </fo:block>
      
      <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-after.optimum="10pt"></fo:block>


      <!-- Normal text -->

      <!-- defines text title level 1-->
      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="4mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Policy No.-L}{L-INA Nomor Polis-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>  <%= no_polis_cetak%></fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Policy Date-L}{L-INA Tanggal Polis-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>  <%=JSPUtil.print(pol.getDtPolicyDate())%></fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG On Behalf of-L}{L-INA Atas Nama-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>  <%=pol.getStCustomerName()%></fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
           </fo:table-row>
           
            <fo:table-row>
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
           </fo:table-row>


         </fo:table-body>
       </fo:table>
       </fo:block>
      
      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt"
            color="black"
            text-align="left"
            padding-top="10pt">
      </fo:block>

      <!-- Normal text -->

      <!-- defines text title level 1-->

      

   <!-- GARIS  -->
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-after.optimum="10pt"></fo:block>

      <!-- Normal text -->

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="38mm"/>
         <fo:table-column column-width="8mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Name of Member -L}{L-INA Nama Peserta -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Birth Date -L}{L-INA Tanggal Lahir -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Year -L}{L-INA Usia -L}</fo:block></fo:table-cell>
             <%--  
             <fo:table-cell><fo:block>{L-ENG Inc. Date. -L}{L-INA Awal -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Exp. Date -L}{L-INA Akhir -L}</fo:block></fo:table-cell>
             --%>
             <fo:table-cell><fo:block text-align="center">{L-ENG Time Length-L}{L-INA Jangka Waktu -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">{L-ENG Time -L}{L-INA Lama (Bulan) -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">{L-ENG Sum Insured -L}{L-INA H. Pertanggungan -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">{L-ENG Premium -L}{L-INA Premi -L}</fo:block></fo:table-cell>
           </fo:table-row>

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="9" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

			<%--  
           <fo:table-row>
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell></fo:table-cell>
                       
                      <fo:table-cell></fo:table-cell>
                      
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
           </fo:table-row>
			--%>
<%

   DTOList objects = pol.getObjects();

   //FlexFieldHeaderView objectMap = pol.getPolicyType().getObjectMap();

   for (int i = 0; i < objects.size(); i++) {
      InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

      final DTOList suminsureds = obj.getSuminsureds();

      //String postCodeDesc = (String) objectMap.getDesc("streference6",obj);

      for (int j = 0; j < suminsureds.size(); j++) {
         InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(j);

%>
<% if (j==0) {%>
            <fo:table-row>
             <fo:table-cell><fo:block><%=JSPUtil.printX(String.valueOf(i+1))%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getDtReference1())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference2())%></fo:block></fo:table-cell>
             <%--  
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getDtReference2())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getDtReference3())%></fo:block></fo:table-cell>
             --%>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getDtReference2())%> s/d <%=JSPUtil.printX(obj.getDtReference3())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference6())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbObjectInsuredAmount(),0)%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbReference6(),0)%></fo:block></fo:table-cell>
           </fo:table-row>

<% } else {%>

          <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <%--  
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             --%>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(tsi.getStInsuranceTSIDesc())%> <%=JSPUtil.printX(tsi.getDbInsuredAmount())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
           </fo:table-row>

<% }%>
<%
      }
      }
%>

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="9" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

                    <fo:table-row>
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell><fo:block>TOTAL SELURUH</fo:block></fo:table-cell>
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell></fo:table-cell>
                      <%--  
                      <fo:table-cell></fo:table-cell>
                      --%>
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell number-columns-spanned="1" ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),0)%></fo:block>
                      </fo:table-cell><fo:table-cell number-columns-spanned="1" ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),0)%></fo:block></fo:table-cell>
                    </fo:table-row>

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="9" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

                      </fo:table-body>
       </fo:table>
       </fo:block>
       
       
		<fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="52mm"/>
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-body>
            <fo:table-row>
                <fo:table-cell><fo:block>JUMLAH HARGA PERTANGGUNGAN</fo:block></fo:table-cell>
                <fo:table-cell><fo:block>= <%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),0)%></fo:block></fo:table-cell>
            </fo:table-row>
            <fo:table-row>
                <fo:table-cell><fo:block>JUMLAH PREMI</fo:block></fo:table-cell>
                <fo:table-cell><fo:block>= <%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),0)%></fo:block></fo:table-cell>
            </fo:table-row>
        </fo:table-body>
       </fo:table>
       </fo:block>


       <fo:block font-size="<%=fontsize%>" space-before.optimum="20pt">
      </fo:block>

       <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="100mm"/>
         <fo:table-column column-width="100mm"/>
         <fo:table-body>
            <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell >
               <fo:block text-align="center"><%= JSPUtil.printX(pol.getCostCenter(pol.getCostCenter(pol.getStCostCenterCode()).getStSubCostCenterCode()).getStDescription())%>, <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%></fo:block>
               <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>

             </fo:table-cell>
           </fo:table-row>
        </fo:table-body>
       </fo:table>
       </fo:block>

<% }%>

 <fo:block id="end-of-document"><fo:marker
    marker-class-name="term">
<fo:instream-foreign-object>
<svg xmlns="http://www.w3.org/2000/svg" width="15cm" height="1cm" xml:space="preserve">     
<rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
</svg>
</fo:instream-foreign-object>
</fo:marker>
  </fo:block>

    </fo:flow>
  </fo:page-sequence>

</fo:root>

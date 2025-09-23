<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

   final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
   final String fontsize = (String) request.getAttribute("FONTSIZE");
   final String attached = (String) request.getAttribute("attached");
   boolean tanpaTanggal = attached.equalsIgnoreCase("3")?false:true;

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

      <!-- defines text title level 1-->

      <fo:block font-size="16pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       {L-ENG List of Attachement -L}{L-INA Lampiran Polis Asuransi -L} <%=pol.getStPolicyTypeDesc2()%> {L-ENG Insurance -L}{L-INA -L}
      </fo:block>

      <!-- Normal text -->

      <!-- defines text title level 1-->

      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       {L-ENG On Behalf of : -L}{L-INA Atas Nama : -L}<%=pol.getStCustomerName()%>
      </fo:block>

      <!-- Normal text -->

      <!-- defines text title level 1-->

      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       {L-ENG Policy No. : -L}{L-INA Nomor Polis : -L}<%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%></fo:block>
	<fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="5pt" space-after.optimum="20pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       {L-ENG Period : -L}{L-INA Periode : -L} <%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%> {L-ENG until -L}{L-INA s/d -L} <%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:block>
			

   <!-- GARIS  -->
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-after.optimum="10pt"></fo:block>

      <!-- Normal text -->

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="40mm"/>
         <fo:table-column column-width="60mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="40mm"/>
         <fo:table-column column-width="50mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Name of Principal -L}{L-INA Nama Principal -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Address of Principal -L}{L-INA Alamat Principal  -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Author of Principal -L}{L-INA Pejabat Principal  -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Name of Surety -L}{L-INA Nama Surety -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Address of Surety -L}{L-INA Alamat Surety  -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Author of Surety -L}{L-INA Pejabat Surety  -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">{L-ENG Sum Insured -L}{L-INA H. Pertanggungan -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">{L-ENG Premium -L}{L-INA Premi -L}</fo:block></fo:table-cell>
           </fo:table-row>

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="9" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

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

<%
   DTOList objects = pol.getObjects();

   for (int i = 0; i < objects.size(); i++) {
      InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

%>

            <fo:table-row>
             <fo:table-cell><fo:block><%=JSPUtil.printX(String.valueOf(i+1))%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference3())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference4())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getDesc("stReference6"))%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference7())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference8())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbObjectInsuredAmount(),2)%></fo:block></fo:table-cell>
<% if (pol.getStCoverTypeCode().equalsIgnoreCase("COINSOUT")) { %>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbObjectPremiTotalBeforeCoinsuranceAmount(),2)%></fo:block></fo:table-cell>
<% } else { %>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbObjectPremiTotalAmount(),2)%></fo:block></fo:table-cell>
<% } %>
           </fo:table-row>s

<% }%>

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="9" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

                    <fo:table-row>
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell number-columns-spanned="1" >
                      <fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%></fo:block>
                      
                  </fo:table-cell>
                      <fo:table-cell number-columns-spanned="1" >
                      <fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block>
                  </fo:table-cell>
                    </fo:table-row>

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="9" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

                      </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" space-before.optimum="30pt">
      </fo:block>

       <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="130mm"/>
         <fo:table-column column-width="130mm"/>
         <fo:table-body>
            <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell >
<% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
               <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L} 
               <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if(tanpaTanggal){%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%></fo:block>
<% } else { %>  
			  <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L} 
               Jakarta<%if(tanpaTanggal){%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%></fo:block>
<% } %>
               <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>

             </fo:table-cell>
           </fo:table-row>
        </fo:table-body>
       </fo:table>
       </fo:block>
       
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

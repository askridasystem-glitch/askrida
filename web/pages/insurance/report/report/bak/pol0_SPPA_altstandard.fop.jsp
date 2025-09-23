<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil,
                 java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

   final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
   final String fontsize = (String) request.getAttribute("FONTSIZE");

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
            font-family="sans-serif"
            line-height="12pt" >
        SPPA - PT. Asuransi Bangun Askrida
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">

   <!-- GARIS  -->
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>

      <!-- defines text title level 1-->

      <fo:block font-size="16pt" line-height="16pt" space-after.optimum="5pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       {L-ENG Insurance Proposal Form -L}{L-INA Surat Permohonan Penutupan Asuransi -L}
      </fo:block>

      <!-- Normal text -->

<!-- DYNAMIC HEADER WORDING -->


   <!-- GARIS -->
     <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>

      <!-- Normal text -->

   <!-- ROW -->
      <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

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
             <fo:table-cell ><fo:block><%=pol.getStPeriodLength()%>{L-ENG Days -L}{L-INA Hari -L}</fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Inception Date-L}{L-INA Awal Pertanggungan-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Expiry Date-L}{L-INA Akhir Pertanggungan -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

<%
   final FlexFieldHeaderView polSPPAMap = pol.getSPPAFF();

      final DTOList polSPPAMapDetails = polSPPAMap==null?null:polSPPAMap.getDetails();

   if (polSPPAMapDetails !=null) {

      %>
      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>
      <%


            for (int j = 0; j < polSPPAMapDetails.size(); j++) {
               FlexFieldDetailView iomd = (FlexFieldDetailView) polSPPAMapDetails.get(j);

               final Object desc = iomd.getDesc(pol);

%>
            <fo:table-row>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(iomd.getStFieldDesc())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
           </fo:table-row>
               <%
            }
%>
         </fo:table-body>
       </fo:table>
       </fo:block>
<% } %>


       <fo:block font-size="<%=fontsize%>" space-before.optimum="10pt">
       {L-ENG CLAUSES ATTACHED-L}{L-INA KLAUSULA TERLEKAT-L}
      </fo:block>

   <!-- ROW -->
      <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="2pt" text-align="justify" > </fo:block>

      <fo:block font-size="<%=fontsize%>" >

      <fo:table table-layout="fixed">
         <fo:table-column column-width="5mm"/>
         <fo:table-column />
         <fo:table-body>
           <fo:table-row>
             <fo:table-cell ></fo:table-cell>
             <fo:table-cell >
               <fo:list-block space-after.optimum="5pt">

<%
   final DTOList clausules = pol.getReportClausules();
   for (int i = 0; i < clausules.size(); i++) {
      InsurancePolicyClausulesView cl = (InsurancePolicyClausulesView) clausules.get(i);

      if (!cl.isSelected()) continue;

      //out.println(cl.getStDescription()+"\n");

%>

           <fo:list-item>
             <fo:list-item-label end-indent="label-end()">
               <fo:block>&#x2022;</fo:block>
             </fo:list-item-label>
             <fo:list-item-body start-indent="body-start()">
               <fo:block font-size="<%=fontsize%>" >
                 <fo:inline text-decoration="none"><%=cl.getStDescription()%></fo:inline>
               </fo:block>
             </fo:list-item-body>
           </fo:list-item>

<%} %>
               </fo:list-block>
             </fo:table-cell>
           </fo:table-row>
         </fo:table-body>
      </fo:table>
      </fo:block>


       <fo:block font-size="<%=fontsize%>" space-before.optimum="5pt">
       {L-ENG RISK DETAILS-L}{L-INA URAIAN RISIKO-L}
      </fo:block>

   <!-- ROW -->
      <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="2pt" text-align="justify" > </fo:block>

<%
   final DTOList objects = pol.getObjects();

   for (int i = 0; i < objects.size(); i++) {
      InsurancePolicyObjectView io = (InsurancePolicyObjectView) objects.get(i);

      final FlexFieldHeaderView objectMap = io.getSPPAFF();

      final DTOList objectMapDetails = objectMap==null?null:objectMap.getDetails();

      %>
      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="90mm"/>
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
             <fo:table-cell ><fo:block><%=JSPUtil.printX(iomd.getStFieldDesc())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
           </fo:table-row>
               <%
            }

%>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>


<%} %>


<fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>

      
      <fo:block font-size="6pt"
                font-family="sans-serif"
                line-height="10pt"
                space-after.optimum="10pt"
                text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%> PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>]

      </fo:block>
     



       <fo:block font-size="8pt">
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




<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil,
                 com.webfin.entity.model.EntityView"%><?xml version="1.0" encoding="utf-8"?>
<%

   final DTOList l = (DTOList) request.getAttribute("RPT");

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->

    <fo:simple-page-master master-name="only"
                  page-height="15.7cm"
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
        NOTA PENUTUPAN REASURANSI FAKULTATIVE - PT. Asuransi Bangun Askrida Page:<fo:page-number/>
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">


<%
   int pn=0;

   for (int n = 0; n < l.size(); n++) {
      InsurancePolicyView pol = (InsurancePolicyView) l.get(n);

      final boolean lastPage = n==l.size()-1;

      final DTOList objects = pol.getObjects();

      for (int i = 0; i < objects.size(); i++) {
         InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);

         final DTOList treatyDetails = obj.getTreatyDetails();

         for (int j = 0; j < treatyDetails.size(); j++) {
            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

            final boolean nonProportional = trd.getTreatyDetail().getTreatyType().isNonProportional();

            if (!nonProportional) continue;

            final DTOList shares = trd.getShares();

            for (int k = 0; k < shares.size(); k++) {
               InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

               final EntityView reasuradur = ri.getEntity();

               pn++;

%>
<%if (pn>1) {%>
         <fo:block break-after="page"></fo:block>
<% } %>



   <!-- GARIS  -->
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>

      <!-- defines text title level 1-->

      <fo:block font-size="16pt" font-family="TAHOMA" line-height="16pt" space-after.optimum="10pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       NOTA PENUTUPAN REASURANSI FAKULTATIVE
      </fo:block>

      <!-- Normal text -->

<!-- DYNAMIC HEADER WORDING -->


   <!-- GARIS -->
     <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>

      <!-- Normal text -->

   <!-- ROW -->
      <fo:block font-size="10pt" font-family="TAHOMA" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

      <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell ><fo:block>policy id</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= pol.getStPolicyID()%></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= pol.getStPolicyNo()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc()%></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block>Penanggung</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= pol.getHoldingCompany().getStEntityName()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Reasuradur</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= reasuradur.getStEntityName() %></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Nama tertanggung</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Yang dipertanggungkan</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStObjectDescription2())%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Jangka waktu</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^ yyyy")+" sampai ")+DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Terletak di</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStObjectDescription())%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Kondisi</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
<%
   final DTOList covers = obj.getCoverage();

   for (int m = 0; m < covers.size(); m++) {
      InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covers.get(m);

      if (m>0) out.print(",");
      out.print(JSPUtil.print(cov.getStInsuranceCoverDesc()));
%>
<% } %>
             </fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Co insurance</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
<%
   final DTOList coins = pol.getCoins();
   if (coins.size()>1) {
%>

                  <fo:table table-layout="fixed">
                     <fo:table-column column-width="35mm"/>
                     <fo:table-column column-width="2mm"/>
                     <fo:table-column />
                     <fo:table-body>
<%
   for (int m = 0; m < coins.size(); m++) {
      InsurancePolicyCoinsView co = (InsurancePolicyCoinsView) coins.get(m);

%>
                        <fo:table-row>
                           <fo:table-cell ><fo:block><%=JSPUtil.printX(co.getStEntityName())%></fo:block></fo:table-cell>
                           <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                           <fo:table-cell ><fo:block><%=JSPUtil.printPct(co.getDbSharePct(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
<% } %>
                     </fo:table-body>
                  </fo:table>
<% }else {%>
                  NONE
<% } %>
                  </fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Saham reasuradur</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(ri.getDbTSIAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Keterangan</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(ri.getStNotes())%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>Premi reasuransi</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
               <fo:table table-layout="fixed">
                     <fo:table-column column-width="35mm"/>
                     <fo:table-column column-width="2mm"/>
                     <fo:table-column />
                     <fo:table-body>
                        <fo:table-row>
                           <fo:table-cell ><fo:block>Premi</fo:block></fo:table-cell>
                           <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                           <fo:table-cell ><fo:block><%=JSPUtil.print(ri.getDbPremiAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                           <fo:table-cell ><fo:block>Komisi <%=JSPUtil.print(ri.getDbRICommRate(),2)%>%</fo:block></fo:table-cell>
                           <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                           <fo:table-cell ><fo:block><%=JSPUtil.print(ri.getDbRICommAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                           <fo:table-cell ><fo:block>Netto</fo:block></fo:table-cell>
                           <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                           <fo:table-cell ><fo:block><%=JSPUtil.print(ri.getDbPremiNet(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                  </fo:table>

             </fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>


       <%
            }
         }

      }

%>




      <% } %>
    </fo:flow>
  </fo:page-sequence>
</fo:root>




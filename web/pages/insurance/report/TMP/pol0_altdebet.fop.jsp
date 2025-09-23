<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.DateUtil"%><?xml version="1.0" encoding="utf-8"?>
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
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">

     <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>

      <!-- defines text title level 1-->
      <fo:block font-size="16pt"
            font-family="TAHOMA"
            line-height="14pt"
            space-after.optimum="10pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       DEBET NOTE 
      </fo:block>

      <!-- Normal text -->

     <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>

      <!-- Normal text -->
      <fo:block font-size="10pt"
                font-family="TAHOMA"
                line-height="10pt"
                space-after.optimum="10pt"
                text-align="justify" >
      </fo:block>

      <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>Nomor Polis</fo:block></fo:table-cell>
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

           <fo:table-row>
             <fo:table-cell ><fo:block>XXX</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>XXX</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.print(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>XXX</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.print(pol.getDbTotalDue(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>XXX</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.print(pol.getDbPremiTotalAfterDisc(),2)%></fo:block></fo:table-cell>
           </fo:table-row>


         </fo:table-body>
       </fo:table>
       </fo:block>

<!-- JARAK ROW -->
       <fo:block font-size="10pt" space-before.optimum="10pt"></fo:block>

<!-- BUAT GARIS -->
     <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>

<!-- JARAK ROW -->
       <fo:block font-size="10pt" space-before.optimum="10pt"></fo:block>

<!-- JARAK ROW -->
       <fo:block font-size="10pt" space-before.optimum="10pt"></fo:block>

<%

   final DTOList objects = pol.getObjects();
   final FlexFieldHeaderView objectMap = pol.getPolicyType().getObjectMap();
   final DTOList objectMapDetails = objectMap==null?null:objectMap.getDetails();

   for (int i = 0; i < objects.size(); i++) {
      InsurancePolicyObjectView io = (InsurancePolicyObjectView) objects.get(i);

      %>
      <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>
      <%

      if (objectMapDetails!=null)
            for (int j = 0; j < objectMapDetails.size(); j++) {
               FlexFieldDetailView iomd = (FlexFieldDetailView) objectMapDetails.get(j);

               final Object desc = iomd.getDesc(io);

%>
               <%
            }
      else {	

         final InsurancePolicyVehicleView veh = (InsurancePolicyVehicleView) io;

               %>

         <%
      }

%>

           <fo:table-row>
             <fo:table-cell ><fo:block>
               <fo:block font-size="8pt">
              <fo:table table-layout="fixed">
               <fo:table-column column-width="50mm"/>
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

       <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="-2mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>
            <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>

               <fo:table table-layout="fixed">
                  <fo:table-column column-width="30mm"/>
                  <fo:table-column column-width="2mm"/>
                  <fo:table-column column-width="5mm"/>
                  <fo:table-column column-width="30mm"/>
                  <fo:table-column column-width="2mm"/>
                  <fo:table-column column-width="15mm"/>
                  <fo:table-column column-width="2mm"/>
                  <fo:table-column column-width="20mm"/>
                  <fo:table-column column-width="2mm"/>
                  <fo:table-column column-width="5mm"/>
                  <fo:table-column column-width="20mm"/>
                  <fo:table-body>


<%

   //final DTOList coverage = io.getCoverage();

   for (int j = 0; j < coverage.size(); j++) {
      InsurancePolicyCoverView cover = (InsurancePolicyCoverView) coverage.get(j);

      final boolean entryRate = cover.isEntryRate();

%>
<%if (entryRate) {%>
<% } else {%>
<% } %>
<% } %>
                 </fo:table-body>
                </fo:table>
             </fo:block></fo:table-cell>
           </fo:table-row>
        </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="8pt" space-after.optimum="10pt"> </fo:block>
<%} %>

    <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="-2mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>
            <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
               <fo:table table-layout="fixed">
                  <fo:table-column column-width="30mm"/>
                  <fo:table-column column-width="71mm"/>
                  <fo:table-column column-width="2mm"/>
                  <fo:table-column column-width="10mm"/>
                  <fo:table-column column-width="20mm"/>
                  <fo:table-body>
<%

      final DTOList details = pol.getDetails();

%>

                    <fo:table-row>
                      <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="end">PREMI</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.print(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
                    </fo:table-row>
<%

   for (int i = 0; i < details.size(); i++) {
      InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);
      if (!item.isDiscount()) continue;

      String desc = item.getStDescription2();

      if (item.isEntryByRate()) desc+=JSPUtil.print(item.getDbRate()+" %");

%>
                     <fo:table-row>
                      <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(desc)%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.print(item.getDbAmount(),2)%></fo:block></fo:table-cell>
                    </fo:table-row>
<% } %>
                    <fo:table-row>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell number-columns-spanned="2" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

                    <fo:table-row>
                      <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="end">TOTAL</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.print(pol.getDbPremiTotalAfterDisc(),2)%></fo:block></fo:table-cell>
                    </fo:table-row>

<%

   for (int i = 0; i < details.size(); i++) {
      InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

      if (!item.isFee()) continue;
%>
                     <fo:table-row>
                      <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(item.getStDescription2())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.print(item.getDbAmount(),2)%></fo:block></fo:table-cell>
                    </fo:table-row>
<% } %>

                    <fo:table-row>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell number-columns-spanned="2" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

                    <fo:table-row>
                      <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="end">Tagihan premi</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.print(pol.getDbTotalDue(),2)%></fo:block></fo:table-cell>
                    </fo:table-row>

                    <fo:table-row>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell number-columns-spanned="2" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

                 </fo:table-body>
                </fo:table>
             </fo:block></fo:table-cell>
           </fo:table-row>
        </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="8pt" space-after.optimum="30pt"></fo:block>

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


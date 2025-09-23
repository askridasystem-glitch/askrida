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
      <fo:block text-align="end"
            font-size="6pt"
            font-family="serif"
            line-height="12pt" >
        Insurance Policy - PT. Asuransi Bangun Askrida PrintCode:<%=pol.getStPrintCode()%> Page:<fo:page-number/>
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">

      <!-- defines text title level 1-->
      <fo:block font-size="18pt"
            font-family="sans-serif"
            line-height="24pt"
            space-after.optimum="15pt"
            background-color="blue"
            color="white"
            text-align="center"
            padding-top="0pt">
        <%=pol.getStPolicyTypeDesc()%> INSURANCE
      </fo:block>

      <!-- defines text title level 2-->
      <fo:block font-size="16pt"
            font-family="sans-serif"
            line-height="20pt"
            space-before.optimum="10pt"
            space-after.optimum="10pt"
            text-align="start"
            padding-top="0pt">
        Policy No <%=pol.getStPolicyNo()%>
      </fo:block>

      <fo:block font-family="monospace" wrap-option="no-wrap" linefeed-treatment="preserve"
        white-space-treatment="preserve" white-space-collapse="false"
        space-before="6pt" padding="6pt" font-size="9pt">
         <%=pol.getStEndorseNotes()%>
        <%--<fo:basic-link external-destination="normal.pdf">normal.pdf</fo:basic-link>--%>
      </fo:block>

      <!-- Normal text -->
      <fo:block font-size="12pt"
                font-family="sans-serif"
                line-height="15pt"
                space-after.optimum="15pt"
                text-align="start">
a written proposal by completing the Questionnaire(s) which together with any other statements made in writing by the Insured for the purpose of this policy is deemed to be incorporated herein.
        <%--<fo:basic-link external-destination="normal.pdf">normal.pdf</fo:basic-link>--%>
      </fo:block>

      <!-- Normal text -->
      <fo:block font-size="12pt"
                font-family="sans-serif"
                line-height="15pt"
                space-after.optimum="15pt"
                text-align="justify" >
        Now this Policy of Insurance witnesseth that subject to the Insured having paid to the Insurers the premium mentioned in the Schedule(s) and subject to the terms, exclusions, provisions and conditions contained herein or endorsed hereon the Insurers will indemnify the Insured in the manner and to the extent hereinafter provided.
      </fo:block>

      <fo:block border-width="2pt" border-style="solid" border-before-precedence="0" line-height="2pt"></fo:block>

      <fo:block break-after="page"></fo:block>

      <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>
           <fo:table-row>
             <fo:table-cell ><fo:block>Type</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc()%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>The Insured</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStCustomerName()%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>Address</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStCustomerAddress()%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>Period of Insurance</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStPeriodLength()%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>Inception Date</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d MMM yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>Expiry Date</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d MMM yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>


       <fo:block font-size="10pt" space-before.optimum="20pt">
       EXTENSION CLAUSES
      </fo:block>

      <fo:block font-size="8pt" font-family="sans-serif">

      <fo:table table-layout="fixed">
         <fo:table-column column-width="5mm"/>
         <fo:table-column />
         <fo:table-body>
           <fo:table-row>
             <fo:table-cell ></fo:table-cell>
             <fo:table-cell >
               <fo:list-block space-after.optimum="13pt">
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
               <fo:block font-size="8pt" font-family="sans-serif">
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


       <fo:block font-size="10pt" space-before.optimum="10pt">
       RISK DETAILS
      </fo:block>


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
            <fo:table-row>
             <fo:table-cell ><fo:block>Risk Description</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=io.getStObjectDescription()%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>Vehicle Type</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=veh.getStVehicleTypeDesc()%>)</fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>Reg No</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=veh.getStPoliceRegNo()%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>Chassis No</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=veh.getStChassisNo()%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>Engine No</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=veh.getStEngineNo()%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>Seat Num</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=veh.getLgSeatNumber()%></fo:block></fo:table-cell>
           </fo:table-row>
         <%
      }

%>
            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>
            <fo:table-row>
             <fo:table-cell ><fo:block>Insured Amount</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
               <fo:block font-size="8pt">
              <fo:table table-layout="fixed">
               <fo:table-column column-width="30mm"/>
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
             <fo:table-cell ><fo:block  >Coverage Rate</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
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
           <fo:table-row>
             <fo:table-cell ><fo:block>Deductibles</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>Extension Covers</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>Rates</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
           </fo:table-row>
         </fo:table-body>
       </fo:table>
       </fo:block>
<%} %>

        <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>
            <fo:table-row>
             <fo:table-cell ><fo:block>Premium Calculation</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
               <fo:table table-layout="fixed">
                  <fo:table-column column-width="40mm"/>
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
<% } else {%>
                    <fo:table-row>
                      <fo:table-cell ><fo:block><%=JSPUtil.print(cover.getInsuranceCoverage().getStDescription())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
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

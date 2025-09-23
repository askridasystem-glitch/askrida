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

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

         </fo:table-body>
       </fo:table>
       </fo:block>


       <fo:block font-size="<%=fontsize%>" space-before.optimum="10pt">
       {L-ENG CLAUSES ATTACHED-L}{L-INA KLAUSULA TERLEKAT-L}
      </fo:block>

   <!-- ROW -->
      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="2pt" text-align="justify" > </fo:block>

      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA">

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
               <fo:block font-size="<%=fontsize%>" font-family="TAHOMA">
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
       {L-ENG INTEREST INSURED-L}{L-INA OBJEK PERTANGGUNGAN-L}
      </fo:block>

   <!-- ROW -->
      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="2pt" text-align="justify" > </fo:block>

<%
   final DTOList objects = pol.getObjects();

   final FlexFieldHeaderView objectMap = pol.getPolicyType().getObjectMap();

   final DTOList objectMapDetails = objectMap==null?null:objectMap.getDetails();

   for (int i = 0; i < objects.size(); i++) {
      InsurancePolicyObjectView io = (InsurancePolicyObjectView) objects.get(i);

      %>
      <fo:block font-size="<%=fontsize%>">
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
           </fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Expiry Date-L}{L-INA Akhir Pertanggungan -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d MMM yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>

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
             <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
               <fo:block font-size="<%=fontsize%>">
              <fo:table table-layout="fixed">
               <fo:table-column column-width="35mm"/>
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
             <fo:table-cell ><fo:block >{L-ENG Coverage / Rate-L}{L-INA Jaminan / Suku Premi-L} </fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
               <fo:block font-size="<%=fontsize%>">
              <fo:table table-layout="fixed">
               <fo:table-column column-width="35mm"/>
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
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printPct(cover.getDbRatePct(),2)%> %</fo:block></fo:table-cell>
             <%--<fo:table-cell ><fo:block text-align="center">x</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(cover.getDbPremi(),2)%></fo:block></fo:table-cell>--%>
           </fo:table-row>
      <%

   }
%>
               </fo:table-body>
                </fo:table>
                </fo:block>

      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

             </fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Deductible-L}{L-INA Risiko Sendiri-L} </fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
                  <fo:block font-size="<%=fontsize%>">
                    <fo:table table-layout="fixed">
                     <fo:table-column column-width="35mm"/>
                     <fo:table-column column-width="2mm"/>
                     <fo:table-column />
                     <fo:table-body>
      <%
         final DTOList deductibles = io.getDeductibles();

         for (int j = 0; j < deductibles.size(); j++) {
            InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(j);

            %>

                  <fo:table-row>
                   <fo:table-cell ><fo:block><%=JSPUtil.print(ded.getStClaimCauseDesc())%></fo:block></fo:table-cell>
                   <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                   <fo:table-cell ><fo:block><%=JSPUtil.print(ded.getStAutoDesc())%></fo:block></fo:table-cell>
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

   <!-- ROW -->
      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="10pt" color="black" text-align="left" padding-top="10pt">{L-ENG PREMIUM CALCULATION :-L}{L-INA PERHITUNGAN PREMI :-L} </fo:block>

       <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>
            <fo:table-row>
             <fo:table-cell ><fo:block>
               <fo:table table-layout="fixed">
                  <fo:table-column column-width="35mm"/>
                  <fo:table-column column-width="2mm"/>
                  <fo:table-column column-width="5mm"/>
                  <fo:table-column column-width="20mm"/>
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

      if (Tools.compare(cover.getDbPremi(),BDUtil.zero)<=0) continue;

%>
                     <fo:table-row>
                      <fo:table-cell ><fo:block><%=JSPUtil.printX(cover.getInsuranceCoverage().getStDescription())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">:</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                      <fo:table-cell  number-columns-spanned="5"><fo:block  text-align="end"><%=JSPUtil.printX(cover.getStCalculationDesc())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(cover.getDbPremi(),2)%></fo:block></fo:table-cell>
                    </fo:table-row>
<% } %>

                 </fo:table-body>
                </fo:table>
             </fo:block></fo:table-cell>
           </fo:table-row>
        </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" space-after.optimum="10pt"> </fo:block>
<%} %>

    <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>
            <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
               <fo:table table-layout="fixed">
                  <fo:table-column column-width="-2mm"/>
                  <fo:table-column column-width="71mm"/>
                  <fo:table-column column-width="2mm"/>
                  <fo:table-column column-width="5mm"/>
                  <fo:table-column column-width="20mm"/>
                  <fo:table-body>
<%

      final DTOList details = pol.getDetails();

%>

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
                      <fo:table-cell ><fo:block text-align="end">SUBTOTAL</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
                    </fo:table-row>

<%

   for (int i = 0; i < details.size(); i++) {
      InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);
      if (!item.isDiscount()) continue;

      String desc = item.getStDescription2();

      if (item.isEntryByRate()) desc+=JSPUtil.printX(item.getDbRate()+" %");

%>
                     <fo:table-row>
                      <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(item.getDbAmount(),2)%></fo:block></fo:table-cell>
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
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotalAfterDisc(),2)%></fo:block></fo:table-cell>
                    </fo:table-row>


<%

   for (int i = 0; i < details.size(); i++) {
      InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

      if (!item.isFee()) continue;
%>
                     <fo:table-row>
                      <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(item.getStDescription2())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(item.getDbAmount(),2)%></fo:block></fo:table-cell>
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
                      <fo:table-cell ><fo:block text-align="end">{L-ENG DUE TO US-L}{L-INA TAGIHAN PREMI-L}</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center">=</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block  text-align="end"><%=JSPUtil.printX(pol.getDbTotalDue(),2)%></fo:block></fo:table-cell>
                    </fo:table-row>

                    <fo:table-row>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell number-columns-spanned="2" >
                           <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="0.75pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>


                 </fo:table-body>
                </fo:table>
             </fo:block></fo:table-cell>
           </fo:table-row>
        </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" space-after.optimum="30pt"></fo:block>

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





<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil,
                 com.webfin.entity.model.EntityView"%><?xml version="1.0" encoding="utf-8"?>
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
                  margin-bottom="1cm"
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
        COSLIP - PT. Asuransi Bangun Askrida Page:<fo:page-number/>
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">


<%
   int pn=0;

      final DTOList objects = pol.getObjects();

      for (int i = 0; i < objects.size(); i++) {
         InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

         DTOList coinz = pol.getCoins();

         for (int j = 0; j < coinz.size(); j++) {
            InsurancePolicyCoinsView ci = (InsurancePolicyCoinsView) coinz.get(j);

            if (ci.isHoldingCompany()) continue;

            EntityView reasuradur = ci.getEntity();

            String ciSlipNo = pol.getStPolicyNo()+j;


/*
         final DTOList treatyDetails = obj.getTreatyDetails();

         for (int j = 0; j < treatyDetails.size(); j++) {
            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

            final boolean nonProportional = trd.getTreatyDetail().getTreatyType().isNonProportional();

            if (!nonProportional) continue;

            final DTOList shares = trd.getShares();

            for (int k = 0; k < shares.size(); k++) {
               InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

               final EntityView reasuradur = ri.getEntity();
*/

               pn++;

%>
<%if (pn>1) {%>
         <fo:block break-after="page"></fo:block>
<% } %>

      <!-- defines text title level 1-->

      <fo:block font-size="16pt" font-family="TAHOMA" line-height="16pt" color="black" text-align="center">
       {L-ENG COINSURANCE SLIP-L}{L-INA SLIP KOASURANSI-L}
      </fo:block>

      <fo:block font-size="12pt" font-family="TAHOMA" line-height="16pt" space-after.optimum="10pt" color="black" text-align="center"> NO. <%=JSPUtil.printX(ciSlipNo)%>
      </fo:block>

      <!-- Normal text -->

<!-- DYNAMIC HEADER WORDING -->


      <!-- Normal text -->

      <fo:block font-size="10pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="40mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

   <!-- GARIS DALAM KOLOM -->
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="3" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG The Insurer-L}{L-INA Penanggung-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= pol.getHoldingCompany().getStEntityName()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG The Reinsurer-L}{L-INA Reasuradur-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= reasuradur.getStEntityName() %></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="3" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= pol.getStPolicyNo()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Address-L}{L-INA Alamat-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Sum Insured-L}{L-INA Sum Insured-L} </fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
                  <fo:table table-layout="fixed">
                     <fo:table-column column-width="55mm"/>
                     <fo:table-column column-width="8mm"/>
                     <fo:table-column />
                     <fo:table-body>
             <%
                final DTOList suminsureds = obj.getSuminsureds();

                for (int l = 0; l < suminsureds.size(); l++) {
                   InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(l);
             %>
                        <fo:table-row>
                         <fo:table-cell ><fo:block space-after.optimum="10pt"><%=JSPUtil.printX(tsi.getStDescriptionAuto())%></fo:block></fo:table-cell>
                         <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                         <fo:table-cell ><fo:block><%=JSPUtil.printX(tsi.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>
                       </fo:table-row>
             <% } %>
                    </fo:table-body>
                 </fo:table>

             </fo:block></fo:table-cell>
           </fo:table-row>

<%
   final FlexFieldHeaderView objectMap = pol.getPolicyType().getObjectMap();

   final DTOList objectMapDetails = objectMap==null?null:objectMap.getDetails();

   final InsurancePolicyObjDefaultView io = obj;

%>
      <%

      if (objectMapDetails!=null)
            for (int o = 0; o < objectMapDetails.size(); o++) {
               FlexFieldDetailView iomd = (FlexFieldDetailView) objectMapDetails.get(o);

               final Object desc = iomd.getDesc(io);

%>
            <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt"><%=JSPUtil.printX(iomd.getStFieldDesc())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
           </fo:table-row>
               <%
            }
%>

           <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")+" {L-ENG Up To-L}{L-INA Sampai-L} ")+DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Conditions-L}{L-INA Kondisi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
<%
   final DTOList covers = obj.getCoverage();

   for (int m = 0; m < covers.size(); m++) {
      InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covers.get(m);

      if (m>0) out.print("; ");
      out.print(JSPUtil.print(cov.getStInsuranceCoverDesc()));
%>
<% } %>
             </fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Co-Insurance-L}{L-INA Ko-Asuransi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
<%
   final DTOList coins = pol.getCoins();
   if (coins.size()>1) {
%>

                  <fo:table table-layout="fixed">
                     <fo:table-column column-width="50mm"/>
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
                           <fo:table-cell ><fo:block><%=JSPUtil.printX(co.getDbSharePct(),2)%> %</fo:block></fo:table-cell>
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
             <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Total Sum Insured-L}{L-INA Jumlah Pertanggungan-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
               <fo:table table-layout="fixed">
                     <fo:table-column column-width="35mm"/>
                     <fo:table-column column-width="2mm"/>
                     <fo:table-column column-width="10mm"/>
                     <fo:table-column column-width="25mm"/>
                     <fo:table-column />
                     <fo:table-body>
                        <fo:table-row>
                           <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                           <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
               </fo:table>
            </fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Reinsurances Share-L}{L-INA Saham Reasuradur-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
               <fo:table table-layout="fixed">
                     <fo:table-column column-width="35mm"/>
                     <fo:table-column column-width="2mm"/>
                     <fo:table-column column-width="10mm"/>
                     <fo:table-column column-width="25mm"/>
                     <fo:table-column />
                     <fo:table-body>
                        <fo:table-row>
                           <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                           <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(ci.getDbAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
               </fo:table>
            </fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Rate of Premium-L}{L-INA Rate Premi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%="?"%>%</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENGHandling Fee-L}{L-INA Handling Fee-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.print(ci.getDbHandlingFeeRate(),2)%>%</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Coinsurance Premium-L}{L-INA Premi koasuransi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
               <fo:table table-layout="fixed">
                     <fo:table-column column-width="35mm"/>
                     <fo:table-column column-width="2mm"/>
                     <fo:table-column column-width="10mm"/>
                     <fo:table-column column-width="25mm"/>


                     <fo:table-column />
                     <fo:table-body>
                        <fo:table-row>
                           <fo:table-cell ><fo:block>{L-ENG Premium-L}{L-INA Premi-L}</fo:block></fo:table-cell>
                           <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>

                           <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ci.getDbPremiAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                           <fo:table-cell ><fo:block>{L-ENG Handling Fee-L}{L-INA Handling Fee-L} <%=JSPUtil.print(ci.getDbHandlingFeeRate(),2)%>%</fo:block></fo:table-cell>
                           <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>

                           <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ci.getDbHandlingFeeAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                    <fo:table-row>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell number-columns-spanned="2" >
                           <fo:block border-width="0.50pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="0.75pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

                        <fo:table-row>
                           <fo:table-cell ><fo:block>{L-ENG Nett Premium-L}{L-INA Premi Netto-L}</fo:block></fo:table-cell>
                           <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>

                           <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ci.getDbPremiAmountNet(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                    <fo:table-row>
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

       <fo:block font-size="10pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="60mm"/>
         <fo:table-column column-width="150mm"/>
         <fo:table-body>
            <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell >
               <fo:block text-align="center" space-before.optimum="20pt">JAKARTA, <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%></fo:block>
               <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>

             </fo:table-cell>
           </fo:table-row>
        </fo:table-body>
       </fo:table>
       </fo:block>


       <%
            }

      }

%>


    </fo:flow>
  </fo:page-sequence>
</fo:root>




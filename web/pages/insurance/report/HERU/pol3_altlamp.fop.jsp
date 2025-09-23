<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

   final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
   pol.loadClausules();

   //if (true) throw new NullPointerException();

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->
    <fo:simple-page-master master-name="only"
                  page-height="21cm"
                  page-width="30cm"
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
        Insurance Policy - PT. Asuransi Bangun Askrida Page:<fo:page-number/>
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">

      <!-- defines text title level 1-->

      <fo:block font-size="16pt" font-family="TAHOMA" line-height="16pt" space-after.optimum="10pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       {L-ENG List of Attachement Motor Vehicle Insurance -L}{L-INA Lampiran Polis Kendaraan Bermotor -L}
      </fo:block>

   <!-- GARIS  -->
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-after.optimum="10pt"></fo:block>

      <!-- Normal text -->

      <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="40mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="40mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column />
         <fo:table-body>


           <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Type of Vehicle -L}{L-INA Merek / Jenis -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Police No. -L}{L-INA No. Polisi -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Year -L}{L-INA Tahun -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Chassis No. -L}{L-INA No. Rangka -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Engine No. -L}{L-INA No. Mesin -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Seat -L}{L-INA T. Duduk -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Deductible -L}{L-INA R. Sendiri -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Sum Insured -L}{L-INA H. Pertanggungan -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG Premium -L}{L-INA Premi -L}</fo:block></fo:table-cell>
           </fo:table-row>

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="10" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

<%
   DTOList objects = pol.getObjects();

   for (int i = 0; i < objects.size(); i++) {
      InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

%>


            <fo:table-row>
             <fo:table-cell><fo:block><%=JSPUtil.printX("?")%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference2())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference3())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference4())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference5())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference6())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX("?")%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getDbObjectInsuredAmount(),2)%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getDbObjectPremiTotalAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

<% }%>

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="10" >
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
                      <fo:table-cell></fo:table-cell>
                      <fo:table-cell number-columns-spanned="1" >
                      <fo:block><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%></fo:block>
                      <fo:block><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block>
                  </fo:table-cell>
                    </fo:table-row>

                      </fo:table-body>

       </fo:table>
       </fo:block>

       <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="130mm"/>
         <fo:table-column column-width="130mm"/>
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


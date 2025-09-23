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
                  page-height="21cm"
                  page-width="30cm"
                  margin-top="1cm"
                  margin-bottom="2cm"
                  margin-left="0.5cm"
                  margin-right="0.5cm">
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

       <fo:block font-size="16pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       {L-ENG List of Attachement -L}{L-INA Lampiran Polis Asuransi -L} <%=pol.getStPolicyTypeDesc2()%> {L-ENG Insurance -L}{L-INA -L}
      </fo:block>

   <!-- GARIS  -->
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-after.optimum="10pt"></fo:block>

      <!-- Normal text -->

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="40mm"/>
         <fo:table-column column-width="45mm"/>
         <fo:table-column column-width="25mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="50mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column />
         <fo:table-body>


           <fo:table-row>
             <fo:table-cell><fo:block text-align="center">{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">{L-ENG Title of Tournament-L}{L-INA Nama Turnamen-L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">{L-ENG Location-L}{L-INA Lokasi-L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">{L-ENG Date of Event-L}{L-INA Tanggal Turnamen-L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">{L-ENG Participants-L}{L-INA Para Peserta-L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">{L-ENG Hole and Par Number-L}{L-INA No. Hole dan Par-L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center">{L-ENG Type of Present-L}{L-INA Bentuk Hadiah-L}</fo:block></fo:table-cell>
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
             <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(String.valueOf(i+1))%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference2())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(obj.getDtReference1())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(obj.getStReference3())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(obj.getStReference4())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(obj.getStReference5())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbObjectInsuredAmount(),2)%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbObjectPremiTotalAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

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

       <fo:block font-size="<%=fontsize%>">
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


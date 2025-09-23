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
                  page-width="29.7cm"
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

      <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column />
         <fo:table-body>
           <fo:table-row>
             <fo:table-cell ><fo:block>Merk/Jenis</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>No Polisi</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>No Chassis</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Insured Amount</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Deductible</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Tahun</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Tempat Duduk</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>No Mesin</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Premi</fo:block></fo:table-cell>
           </fo:table-row>

<%
   DTOList objects = pol.getObjects();

   for (int i = 0; i < objects.size(); i++) {
      InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

%>
            <fo:table-row>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStReference2())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStReference4())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getDbObjectInsuredAmount(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStDeductibleDesc())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStReference3())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStReference6())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStReference5())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getDbObjectPremiTotalAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

<% }%>

         </fo:table-body>
       </fo:table>
       </fo:block>

    </fo:flow>
  </fo:page-sequence>
</fo:root>

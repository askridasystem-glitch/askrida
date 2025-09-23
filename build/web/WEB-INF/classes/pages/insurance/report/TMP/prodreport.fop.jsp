<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil,
                 java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
<%

   final DTOList l = (DTOList)request.getAttribute("RPT");

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
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="20mm"/>
         <fo:table-column />
         <fo:table-body>
           <fo:table-row>
             <fo:table-cell ><fo:block>No</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Policy No</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Name of Insured</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Period</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Ccy</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Sum Insured</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Premium</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Adm Cost</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Discount</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Total Comm</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>PremiNet</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>Producer</fo:block></fo:table-cell>
           </fo:table-row>

<%


   BigDecimal [] t = new BigDecimal[10];


   for (int i = 0; i < l.size(); i++) {
      InsurancePolicyView obj = (InsurancePolicyView) l.get(i);

      int n=0;
      t[n] = BDUtil.add(t[n++], obj.getDbInsuredAmount());
      t[n] = BDUtil.add(t[n++], obj.getDbPremiTotal());
      t[n] = BDUtil.add(t[n++], obj.getDbTotalFee());
      t[n] = BDUtil.add(t[n++], obj.getDbTotalDisc());
      t[n] = BDUtil.add(t[n++], obj.getDbTotalComm());
      t[n] = BDUtil.add(t[n++], obj.getDbPremiNetto());

%>
            <fo:table-row>
             <fo:table-cell ><fo:block><%=i%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStPolicyNo())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStCustomerName())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getDtPolicyDate())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getDbTotalFee())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getDbTotalDisc(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getDbTotalComm(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getDbPremiNetto(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStProducerName())%></fo:block></fo:table-cell>
           </fo:table-row>

<% }%>
           <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[2])%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[3],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[4],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(t[5],2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

    </fo:flow>
  </fo:page-sequence>
</fo:root>

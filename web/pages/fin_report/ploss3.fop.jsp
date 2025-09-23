<%@ page import="com.webfin.gl.util.GLUtil,
                 com.webfin.gl.ejb.GLReportEngine2,
                 com.crux.util.JSPUtil,
                 com.webfin.gl.report2.form.FinReportForm,
                 com.crux.util.DateUtil,
                 java.util.Date"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines the layout master -->
  <fo:layout-master-set>
    <fo:simple-page-master master-name="first"
                           page-height="29.7cm"
                           page-width="21cm"
                           margin-top="1cm"
                           margin-bottom="2cm"
                           margin-left="2.5cm"
                           margin-right="2.5cm">
      <fo:region-body margin-top="3cm" margin-bottom="1.5cm"/>
      <fo:region-before extent="3cm"/>
      <fo:region-after extent="1.5cm"/>
    </fo:simple-page-master>
  </fo:layout-master-set>

  <!-- starts actual layout -->
  <fo:page-sequence master-reference="first">

<%
   GLReportEngine2 glr = new GLReportEngine2();

   FinReportForm form = (FinReportForm) request.getAttribute("FORM");

   long lPeriodFrom = form.getLPeriodFrom();
   long lPeriodTo = form.getLPeriodTo();
   long lYearFrom = form.getLYearFrom();
   long lYearTo = form.getLYearTo();

   glr.setBranch(form.getBranch());

%>


<fo:flow flow-name="xsl-region-body">

<%
   String bw = "0.5pt";

   int n=1;
%>

   <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >PENJELASAN LABA (RUGI) OPERASIONAL</fo:block>
   <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getPeriodTitleDescription())%></fo:block>
   <fo:block space-after.optimum="14pt"/>

  <fo:block font-family="Helvetica" font-size="6pt" display-align="center" border-width="0.1pt">
   <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>">
      <fo:table-column column-width="5mm"/>
      <fo:table-column column-width="75mm"/>
      <fo:table-column column-width="25mm"/>
      <fo:table-column column-width="25mm"/>
      <fo:table-column column-width="25mm"/>
      <fo:table-body>
         <fo:table-row  >
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  ><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="8pt" font-weight="bold">No</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="8pt" font-weight="bold">URAIAN</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="8pt" font-weight="bold">SALDO</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="8pt" font-weight="bold">MUTASI</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="8pt" font-weight="bold">SALDO</fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block font-weight="bold" font-style="italic">Pendapatan Premi Bruto</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block font-weight="bold" font-style="italic">Premi Bruto</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >a. Premi Penutupan Langsung</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >b. Premi Penutupan Tidak Langsung</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >a. Komisi Dibayar</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="10mm" >Jumlah Premi Bruto ( 3 + 4 - 5 )</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="0mm" >Premi Reasuransi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >a. Premi Reasuransi Dibayar</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >a. Premi Reasuransi Diterima</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="10mm" >Jumlah Premi Reasuransi ( 8 - 9 )</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="0mm" >Penurunan (Kenaikan) PYBMP</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >a. PYBMP tahun lalu</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >b. PYBMP tahun berjalan</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="10mm" >Jumlah Pendapatan Premi Netto ( 6 - 10 + 14 )</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="0mm" >Beban Klaim Netto</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="0mm" >Beban Klaim</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >a. Klaim Bruto</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >b. Klaim Reasuransi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >c. Kenaikan (Penurunan) EKRS</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="7mm" >c. 1. EKRS tahun berjalan</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="7mm" >c. 2. EKRS tahun lalu</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="10mm" >Jumlah Beban Klaim ( 18 - 19 + 21 - 22 )</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Beban Underwriting Lain Netto</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="10mm" >Jumlah Beban Klaim Netto ( 23 + 24 )</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="0mm" >Hasil Underwriting ( 15 - 25 )</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Hasil Investasi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Beban Usaha</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >a. Beban Pemasaran</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >b. Beban Umum</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >c. Beban Administrasi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="10mm" >Jumlah Beban Usaha (29+30+31)</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="0mm" >Laba (RUGI) USAHA ASURANSI (26+27-32)</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="0mm" >Hasil(Beban) Lain</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="0mm" >Laba sebelum pajak</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="0mm" >Pajak Penghasilan</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="right"><%=n++%>.</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="0mm" >LABA SETELAH PAJAK</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom-1,lPeriodTo-1,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("MUT|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>

      </fo:table-body>
    </fo:table>

    <fo:table table-layout="fixed">
      <fo:table-column column-width="50mm"/>
      <fo:table-column />
      <fo:table-body>
         <fo:table-row>
            <fo:table-cell/>
            <fo:table-cell>

      <fo:table table-layout="fixed">
         <fo:table-column column-width="50mm"/>
         <fo:table-column column-width="50mm"/>
         <fo:table-body>
            <fo:table-row height="10mm"/>
            <fo:table-row>
               <fo:table-cell number-columns-spanned="2" >
                  <fo:block text-align="center">S.E. &amp; O.</fo:block>
                  <fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block>
                  <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                  <fo:block text-align="center">DIREKSI</fo:block>
               </fo:table-cell>
            </fo:table-row>
            <fo:table-row height="20mm">

            </fo:table-row>
            <fo:table-row>
               <fo:table-cell>
                  <fo:block text-align="center" text-decoration="underline">H. MAIRIZAL MEIRAD,SE,MM</fo:block>
                  <fo:block text-align="center">DIREKTUR UTAMA</fo:block>
               </fo:table-cell>
               <fo:table-cell>
                  <fo:block text-align="center" text-decoration="underline">H. POLLY POERDANADIREDKA,SE,AAAIK</fo:block>
                  <fo:block text-align="center">DIREKTUR</fo:block>
               </fo:table-cell>
            </fo:table-row>
         </fo:table-body>

       </fo:table>

            </fo:table-cell>

         </fo:table-row>
         </fo:table-body>
         </fo:table>




 </fo:block>



</fo:flow>
</fo:page-sequence>
</fo:root>


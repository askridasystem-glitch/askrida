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
%>

   <fo:block space-after.optimum="14pt" font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">LAPORAN LABA RUGI</fo:block>


  <fo:block font-family="Helvetica" font-size="8pt" display-align="center" border-width="0.1pt">
   <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>">
      <fo:table-column column-width="130mm"/>
      <fo:table-column column-width="30mm"/>
      <fo:table-body>
         <fo:table-row  >
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  ><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">NILAI</fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block font-weight="bold" font-style="italic">Pendapatan UNDERWRITING</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Premi Bruto</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","61","61",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Dikurangi :</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Premi Reasuransi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","63","63",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Kenaikan (Penurunan) Premi yang belum merupakan pendapatan</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","64","64",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row >
          <fo:table-cell  border-width="<%=bw%>" padding="2pt" border-top-style="solid" border-bottom-style="solid"><fo:block font-weight="bold" font-style="italic">Jumlah Pendapatan Underwriting ... 1</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
        </fo:table-row>

        <fo:table-row height="3mm">
          <fo:table-cell border-width="<%=bw%>" padding="2pt"></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block font-weight="bold" font-style="italic">Beban Underwriting</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Komisi Tanggungan Sendiri</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Klaim Tanggungan Sendiri</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Kenaikan (penurunan) estimasi klaim tanggungan sendiri</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Beban Underwriting Rupa-rupa</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row >
          <fo:table-cell  border-width="<%=bw%>" padding="2pt" border-top-style="solid" border-bottom-style="solid"><fo:block font-weight="bold" font-style="italic">Jumlah Beban Underwriting ... 2</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
        </fo:table-row>

        <fo:table-row height="3mm">
          <fo:table-cell border-width="<%=bw%>" padding="2pt"></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Hasil Underwriting ( 1 - 2 )</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Hasil Investasi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ></fo:block><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","65","65",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:table-cell>
        </fo:table-row>

        <fo:table-row height="3mm">
          <fo:table-cell border-width="<%=bw%>" padding="2pt"></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Beban Usaha Manajemen</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  >?</fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Laba Usaha Operasi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Hasil (Beban) Non Operasional</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Laba (Usaha) sebelum pajak</fo:block></fo:table-cell>
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


<%@ page import="com.webfin.gl.util.GLUtil,
                 com.webfin.gl.ejb.GLReportEngine2,
                 com.crux.util.JSPUtil,
                 com.webfin.gl.report2.form.FinReportForm,
                 com.crux.util.DateUtil,
                 java.util.Date,
                 com.crux.util.BDUtil,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil,
                 java.math.BigDecimal"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines the layout master -->
  <fo:layout-master-set>
    <fo:simple-page-master master-name="first"
                           page-height="31cm"
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

   <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">NERACA</fo:block>
   <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getPeriodTitleDescription())%></fo:block>
   <fo:block space-after.optimum="14pt"/>

  <fo:block font-family="Helvetica" font-size="8pt" display-align="center" border-width="0.1pt">
   <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>">
      <fo:table-column column-width="50mm"/>
      <fo:table-column column-width="30mm"/>
      <fo:table-column column-width="50mm"/>
      <fo:table-column column-width="30mm"/>
      <fo:table-body>
         <fo:table-row  >
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="2" ><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">AKTIVA</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="2" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">KEWAJIBAN DAN EKUITAS</fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell   padding="2pt"><fo:block>DEPOSITO</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=0","1111","1112",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block>UTANG KLAIM</fo:block></fo:table-cell>
          <%--  
          <fo:table-cell border-width="<%=bw%>" text-align="end"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|NEG|ADD=1","33","33",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          --%>
          <fo:table-cell border-width="<%=bw%>" text-align="end"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|ADD=1","33","33",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>

        </fo:table-row>
        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block>SERTIFIKAT DEPOSITO</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=0","1113","1113",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>ESTIMASI KLAIM RETENSI SENDIRI   </fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|ADD=1","32","32",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block>SURAT BERHARGA REKSADANA</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=0","112","114",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>PREMI YG BELUM MERUPAKAN PENDAPATAN   </fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|ADD=1","34","34",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block>PENYERTAAN</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=0","115","115",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>UTANG REASURANSI   </fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|ADD=1","42","42",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block>PROPERTI</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=0","116","116",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>UTANG KOMISI   </fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|ADD=1","43","43",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block>PROPERTI PINJAMAN HIPOTEK</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=0","117","117",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>UTANG PAJAK   </fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|ADD=1","44","44",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block>INVESTASI LAIN</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=0","119","119",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>BIAYA YG MASIH HARUS DIBAYAR</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|ADD=1","46","47",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell  padding="2pt"></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>UTANG LAIN-LAIN</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=1","48","48",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>

         <fo:table-row height="5mm">
          <fo:table-cell  padding="2pt"></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
         <fo:table-cell  padding="2pt"><fo:block>JUMLAH INVESTASI</fo:block></fo:table-cell>
         <fo:table-cell border-width="<%=bw%>" padding="2pt" border-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getDbVariable("0"),0)%></fo:block></fo:table-cell>
         <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>JUMLAH KEWAJIBAN</fo:block></fo:table-cell>
         <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getDbVariable("1"),0)%></fo:block></fo:table-cell>
       </fo:table-row>

        <fo:table-row height="5mm">
          <fo:table-cell  padding="2pt"></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Kas dan Bank</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=0","120","123",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>UTANG SUBORDINASI</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|NEG|ADD=1","49","49",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Premi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedExcluded("BAL|ADD=0","13","13","1333",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),new BigDecimal(-1)),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Reasuransi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=0","14","14",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-weight="bold">EKUITAS</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Hasil Investasi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=0","15","15",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>MODAL DASAR 3000 LBR @ RP.10.000.000</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|NEG|ADD=4","511","512",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Lain</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(
                  BDUtil.add(
                     glr.getGLNew("BAL|ADD=0","16","16",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),
                     glr.getGLNew("BAL|ADD=0","124","124",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo)
                          )

                  ,0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>MODAL DITEMPATKAN DAN DISETOR</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|NEG|ADD=4","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Biaya Dibayar Dimuka</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=0","17","17",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>AGIO SAHAM</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|NEG|ADD=4","513","513",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Aktiva Tetap</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=0","18","18",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>CADANGAN UMUM</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|NEG|ADD=3","51620","51621",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Aktiva Lain-lain</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=0","19","19",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>CADANGAN KHUSUS</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|NEG|ADD=3","51622","51622",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Aktiva Pajak Tangguhan</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|NEG|ADD=1,2","19144","19144",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>Laba (Rugi) ditahan</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|NEG|ADD=3","51623","51623",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm"></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(glr.getGLNew("BAL|ADD=0","","",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>Selisih Penilaian Kembali</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|NEG|ADD=3","515","515",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>SALDO LABA(RUGI) TAHUN LALU</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(glr.getGLNew("BAL|NEG|ADD=3","51610","51610",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),0)%></fo:block></fo:table-cell>
        </fo:table-row>
<%

   BigDecimal jumlahAktiva = glr.getDbVariable("0");
   BigDecimal jumlahKwajiban = glr.getDbVariable("1");
   BigDecimal jumlahLB = glr.getDbVariable("3");
   BigDecimal jumlahModal = glr.getDbVariable("4");
   BigDecimal jumlahAktivaPajak = glr.getGLNew("BAL|NEG|ADD=1,2","19144","19144",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);

   BigDecimal saldoRLthBerjalan = BDUtil.sub(jumlahAktiva, jumlahKwajiban);
   saldoRLthBerjalan = BDUtil.sub(saldoRLthBerjalan, jumlahLB);
   saldoRLthBerjalan = BDUtil.sub(saldoRLthBerjalan, jumlahModal);

   BigDecimal saldoRLthBerjalan2 = BDUtil.sub(jumlahAktiva, jumlahKwajiban);
   saldoRLthBerjalan2 = BDUtil.add(saldoRLthBerjalan2, jumlahLB);
   saldoRLthBerjalan2 = BDUtil.add(saldoRLthBerjalan2, jumlahModal);
   saldoRLthBerjalan2 = BDUtil.sub(saldoRLthBerjalan2, jumlahAktivaPajak);
   
   BigDecimal jumlahPassiva = BDUtil.add(jumlahKwajiban, jumlahModal);
   jumlahPassiva = BDUtil.add(jumlahPassiva, jumlahLB);
   jumlahPassiva = BDUtil.add(jumlahPassiva, saldoRLthBerjalan);


%>
        <fo:table-row>
          <fo:table-cell  padding="2pt"><fo:block></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>SALDO LABA(RUGI) TAHUN BERJALAN</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(saldoRLthBerjalan2,0)%></fo:block></fo:table-cell>
        </fo:table-row>

        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-top-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" font-weight="bold"  >JUMLAH AKTIVA</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="10mm"  font-weight="bold" background-color="#C0C0C0"  ><%=JSPUtil.printX(glr.getDbVariable("0"),0)%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid" border-top-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0"  font-weight="bold">JUMLAH KEWAJIBAN &amp; EKUITAS</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="10mm" font-weight="bold" background-color="#C0C0C0"  ><%=JSPUtil.printX(jumlahPassiva,0)%></fo:block></fo:table-cell>
        </fo:table-row>

      </fo:table-body>
    </fo:table>

    <fo:block font-size="6pt"
                font-family="sans-serif"
                line-height="10pt"
                space-after.optimum="15pt"
                text-align="left" >
                Print Stamp : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"hhmmssyyyyMMdd "))%>

      </fo:block>

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
                  <fo:block text-align="center" text-decoration="underline"><%=JSPUtil.printX(form.getPerson1Name())%></fo:block>
                  <fo:block text-align="center"><%=JSPUtil.printX(form.getPerson1Title())%></fo:block>
               </fo:table-cell>
               <fo:table-cell>
                  <fo:block text-align="center" text-decoration="underline"><%=JSPUtil.printX(form.getPerson2Name())%></fo:block>
                  <fo:block text-align="center"><%=JSPUtil.printX(form.getPerson2Title())%></fo:block>
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


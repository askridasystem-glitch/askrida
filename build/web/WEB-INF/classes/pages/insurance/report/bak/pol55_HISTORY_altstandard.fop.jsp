<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil,
                 java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

   final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
   final String fontsize = (String) request.getAttribute("FONTSIZE");

   //if (true) throw new NullPointerException();

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->
    <fo:simple-page-master master-name="only"
                  page-height="30.7cm"
                  page-width="21cm"
                  margin-top="2cm"
                  margin-bottom="0.5cm"
                  margin-left="2cm"
                  margin-right="2cm">
      <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/>
      <fo:region-before extent="3cm"/>
      <fo:region-after extent="0.5cm"/>
    </fo:simple-page-master>

  </fo:layout-master-set>
  <!-- end: defines page layout -->

  <!-- actual layout -->
  <fo:page-sequence master-reference="only" initial-page-number="1">

    <!-- usage of page layout -->
    <!-- header -->

    <fo:flow flow-name="xsl-region-body">

      <!-- defines text title level 1-->

      <fo:block font-size="14pt" font-family="TAHOMA" line-height="0pt" space-after.optimum="10pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       PERSETUJUAN PRINSIP JAMINAN KONTRA BANK GARANSI
      </fo:block>
      
      <fo:block font-size="12pt" font-family="TAHOMA" line-height="0pt" space-after.optimum="10pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       <%=JSPUtil.printX(pol.getStPolicyTypeDesc2())%>
      </fo:block>

      <!-- Normal text -->

   <!-- GARIS  -->
     <fo:block border-width="0.50pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" space-before.optimum="1pt" text-align="justify"></fo:block>

      <!-- defines text title level 1-->

      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="0pt" 
            color="black"
            text-align="center"
            padding-top="10pt">
      Nomor : <%= pol.getStReference1()%> Tanggal : <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%>
      </fo:block>

      <!-- Normal text -->

   <!-- GARIS  -->
     <fo:block border-width="0.50pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" space-before.optimum="10pt" space-after.optimum="10pt" text-align="justify"></fo:block>

      <!-- Normal text -->

      <fo:block font-size="<%=fontsize%>" text-align="justify" line-space="1pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="145mm"/>
         <fo:table-column />
         <fo:table-body>

<%
   DTOList objects = pol.getObjects();

   for (int i = 0; i < objects.size(); i++) {
      InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

%>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="170mm"/>
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Kepada : -L}</fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block><%=pol.getStCustomerName()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell><fo:block space-after.optimum="15pt"><%=pol.getStCustomerAddress()%></fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
             <fo:table-cell><fo:block space-after.optimum="15pt">Di - <%= JSPUtil.printX(pol.getStCostCenterDesc())%></fo:block></fo:table-cell>
           </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="170mm"/>
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block space-after.optimum="5pt">{L-ENG XXX -L}{L-INA Dengan ini kami beritahukan bahwa permohonan Jaminan Kontra Bank Garansi dari :  -L}</fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="1" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="135mm"/>
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Nama Terjamin -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Alamat -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block space-after.optimum="5pt"><%=JSPUtil.printX(obj.getStReference3())%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="3" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="170mm"/>
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA dengan Surat Permohonan Kontra Bank Garansi -L}</fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="1" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="70mm"/>
         <fo:table-column column-width="70mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Nomor :  -L}<%=JSPUtil.printX(obj.getStReference4())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Tanggal :  -L}<%=DateUtil.getDateStr(obj.getDtReference1(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="3" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="170mm"/>
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Secara prinsip dapat kami setujui untuk dijamin, dengan ketentuan sebagai berikut : -L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
           </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="165mm"/>
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA 1. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Bank Garansi yang diberikan kepada TERJAMIN sesuai dengan ketentuan berikut : -L}</fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="2" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="5mm"/>

         <fo:table-column column-width="60mm"/>
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="100mm"/>
          <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Jenis Bank Garansi -L} </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStPolicyTypeDesc2())%></fo:block></fo:table-cell>
           </fo:table-row>


            <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Jangka Waktu Bank Garansi -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%> {L-ENG XXX -L}{L-INA s/d -L}<%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%>(<%=pol.getStPeriodLength()%> {L-ENG XXX) -L}{L-INA hari sejak tanggal penerbitan Bank Garansi)</fo:block></fo:table-cell>
           </fo:table-row>


            <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Nilai Jaminan -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%> {L-ENG XXX -L}{L-INA (Terbilang : -L}<%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(),2),"",NumberSpell.INDONESIA)%> RUPIAH)</fo:block></fo:table-cell>

           </fo:table-row>

  					<% if(obj.getStReference7()!=null){%>
            <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA No. SPK / Tgl SPK -L} </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference7())%>  <%=DateUtil.getDateStr(obj.getDtReference2(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>
           <%}%>
           
           <% if(obj.getStReference11()!=null){%>
           <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA No. Kontrak / Tgl Kontrak -L} </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference11())%>  <%=DateUtil.getDateStr(obj.getDtReference3(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>
           <%}%>
           
           <% if(obj.getStReference12()!=null){%>
           <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA No.BG / Tgl BG -L} </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference12())%>  <%=DateUtil.getDateStr(obj.getDtReference4(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>
           <%}%>
           
           <% if(obj.getStReference13()!=null){%>
           <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA No. Undangan / Tgl Und. -L} </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference13())%>  <%=DateUtil.getDateStr(obj.getDtReference5(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>
           <%}%>

           <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
           </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="165mm"/>
          <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Bank Garansi ini diperlukan untuk : -L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
           </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="5mm"/>

         <fo:table-column column-width="60mm"/>
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="100mm"/>
          <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Nama Pemilik Proyek -L}</fo:block></fo:table-cell> 
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference5())%></fo:block></fo:table-cell>
         </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Nama Proyek -L}</fo:block></fo:table-cell> 
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference6())%></fo:block></fo:table-cell>
         </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Nilai Proyek -L}</fo:block></fo:table-cell> 
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%> <%=JSPUtil.printX(obj.getDbReference3())%></fo:block></fo:table-cell>
         </fo:table-row>

   <!-- GARIS DALAM KOLOM -->
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="4" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="165mm"/>

         <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>            
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Agunan yang diberikan dengan data sebagai berikut : -L}</fo:block></fo:table-cell>
           </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="60mm"/>
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="100mm"/>
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Jenis Agunan-L}</fo:block></fo:table-cell> 
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference8())%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Nilai Agunan-L}</fo:block></fo:table-cell> 
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%> <%=JSPUtil.printX(obj.getDbReference4())%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="4" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                               
           <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
           </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>
       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="165mm"/>
          <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA 2. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Persetujuan prinsip ini berlaku untuk jangka waktu selama-lamanya 7 (tujuh) hari kerja terhitung sejak tanggal persetujuan prinsip ini. Apabila dalam jangka waktu 7 (tujuh) hari kerja, -L}<%=pol.getStCustomerName()%> {L-ENG XXX -L}{L-INA belum menerbitkan Bank Garansi, maka persetujuan prinsip ini menjadi batal. -L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA 3. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Persetujuan prinsip ini tidak berlaku sebagai SERTIFIKAT JAMINAN KONTRA BANK GARANSI. Asli SERTIFIKAT JAMINAN KONTRA BANK GARANSI akan kami terbitkan setelah -L}<%=pol.getStCustomerName()%> {L-ENG XXX -L}{L-INA melaporkan realisasi penerbitan Bank Garansi kepada kami serta kami telah menerima biaya penjaminan dari TERJAMIN -L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Dalam hal -L} <%=pol.getStCustomerName()%> {L-ENG XXX -L}{L-INA telah menerbitkan Bank Garansi berdasarkan persetujuan prinsip ini, maka persetujuan prinsip ini mengikat PT. Asuransi Bangun Askrida untuk menerbitkan SERTIFIKAT JAMINAN KONTRA BANK GARANSI. -L}</fo:block></fo:table-cell>
           </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>
       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="170mm"/>
          <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Dengan telah diterbitkannya persetujuan prinsip ini, kami harap bantuan -L} <%=pol.getStCustomerName()%> {L-ENG XXX -L}{L-INA untuk menagih biaya penjaminan kepada TERJAMIN, dengan rincian sebagai berikut : -L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
           </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>
       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="75mm"/>
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="30mm"/>
          <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Jasa Jaminan *) -L} </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbReference5(),2)%></fo:block></fo:table-cell>
           </fo:table-row>


            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Biaya Administrasi -L} </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbReference6(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Bea Materai (sesuai ketentuan yang berlaku) -L} </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(obj.getDbReference7(),2)%></fo:block></fo:table-cell>
           </fo:table-row>
 
            <fo:table-row>
             <fo:table-cell><fo:block text-align="end">{L-ENG XXX -L}{L-INA Jumlah -L} </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(obj.getDbReference5(),BDUtil.add(obj.getDbReference6(),obj.getDbReference7())),2)%></fo:block></fo:table-cell>
           </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>
       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="170mm"/>
          <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA *) Dibayar dalam rupiah dengan kurs tengah Bank Indonesia yang berlaku pada saat realisasi penerbitan Bank Garansi -L} </fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Hasil penerimaan Jasa Jaminan, Biaya Administrasi dan Bea Materai tersebut harap di kreditkan ke rekening -L} <%=JSPUtil.printX(obj.getStReference10())%> {L-ENG XXX -L}{L-INA di -L} <%=JSPUtil.printX(obj.getStReference9Desc())%> {L-ENG XXX -L}{L-INAuntuk keuntungan PT. Asuransi Bangun Askrida, dengan rincian sebagai berikut : -L}</fo:block></fo:table-cell>
           </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>
       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="75mm"/>
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="30mm"/>
          <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Jasa Jaminan -L} </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotalAfterDisc(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Biaya Administrasi -L} </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalItemAmount("PCOST"),2)%></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Bea Materai (sesuai ketentuan yang berlaku) -L} </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalItemAmount("SFEE"),2)%></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block text-align="end">{L-ENG XXX -L}{L-INA Jumlah -L} </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalDue(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="60mm"/>
         <fo:table-column column-width="40mm"/>
         <fo:table-column column-width="60mm"/>
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"> PT. ASURANSI BANGUN ASKRIDA </fo:block></fo:table-cell>
           </fo:table-row>


            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="70pt"></fo:block></fo:table-cell>
           </fo:table-row>	

                    <fo:table-row>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell number-columns-spanned="1" >
                           <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

<% }%>

    </fo:table-body>
       </fo:table>
       </fo:block>
       
       <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>

      
      <fo:block font-size="6pt"
                font-family="sans-serif"
                line-height="10pt"
                space-after.optimum="10pt"
                text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%> PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>]

      </fo:block>

    </fo:flow>
  </fo:page-sequence>
</fo:root>


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
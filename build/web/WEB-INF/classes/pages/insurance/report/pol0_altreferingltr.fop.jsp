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
        PrintCode:<%=pol.getStPrintCode()%> Page:<fo:page-number/>
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">

      <!-- defines text title level 1-->

      <!-- Normal text -->

<!-- DYNAMIC HEADER WORDING -->

      <!-- Normal text -->

   <!-- ROW -->
      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="160mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block space-after.optimum="10pt" text-align="end"><%= JSPUtil.printX(pol.getStCostCenterDesc())%>, <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="20mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column column-width="70mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG TO-L}{L-INA KEPADA-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block space-after.optimum="5pt"><%=pol.getStCustomerName()%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- ROW -->

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG ADDRESS-L}{L-INA ALAMAT-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block space-after.optimum="5pt"><%=pol.getStCustomerAddress()%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="3" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="20pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="160mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify" space-after.optimum="15pt">{L-ENG Dear Sir / Madam -L}{L-INA Dengan hormat,-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify" space-after.optimum="15pt">{L-ENG We herewith, will introduce our company PT. Asuransi Bangun Askrida (ASKRIDA) as a company owned by Bank Pembangunan Daerah and All Indonesia Provence that have an acivity in General Insurance. -L}{L-INA Bersama ini perkenankanlah kami PT. Asuransi Bangun Askrida (ASKRIDA) yang merupakan satu-satunya perusahaan swasta nasional milik Bank Pembangunan Daerah dan Pemerintah Propinsi Seluruh Indonesia, yang bergerak di bidang Asuransi Umum.-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify" space-after.optimum="15pt">{L-ENG As a professional Insurance Company, ASKRIDA always give the best services in several type as follow : -L}{L-INA Sebagai Perusahaan Asuransi yang dikelola secara profesional, ASKRIDA senantiasa memberikan pelayanan yang terbaik antara lain dalam bentuk :-L}</fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="150mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify"></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG a. -L}{L-INA a.-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG Protection and much benefit in insurance cover. -L}{L-INA Perlindungan dan manfaat yang luas dalam penutupan pertanggungan-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify"></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG b. -L}{L-INA b.-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify" space-after.optimum="15pt">{L-ENG Fast and easy in handling of claim -L}{L-INAPenyelesaian klaim yang mudah, cepat dan tidak berbelit-belit-L}</fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="160mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify" space-after.optimum="15pt">{L-ENG In case of product development, we hope you could give a chance to us to introduce about insurance protection for your assets. -L}{L-INA Dalam rangka pengembangan produk, melalui surat ini kami mohon kiranya Bapak / Ibu berkenan memberikan kesempatan kepada kami untuk memberikan proteksi / perlindungan Asuransi bagi assets-assets yang ada.-L}</fo:block></fo:table-cell>
           </fo:table-row>


           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify" space-after.optimum="15pt">{L-ENG With this reffering letter, we attach a Company Profile. -L}{L-INA Bersama surat perkenalan dan penawaran ini, terlampir kami sampaikan Company Profile.-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify" space-after.optimum="15pt">{L-ENG The other side, we provide to you that ASKRIDA as a general insurance company will give some protections against : -L}{L-INA Selain itu kami sampaikan pula kepada Bapak / Ibu sebagaimana halnya Perusahaan-perusahaan Asuransi Umum lainnya ASKRIDA juga memberikan pelayanan Asuransi terhadap : -L}</fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="150mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify"></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG a. -L}{L-INA a. -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG Fire Insurance -L}{L-INA Asuransi Kebakaran-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify"></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG b. -L}{L-INA b. -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG Fire Consortium -L}{L-INA Konsorsium Kebakaran-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify"></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG c. -L}{L-INA c. -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG Motor Vehicle -L}{L-INA Kendaraan Bermotor-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify"></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG d. -L}{L-INA d. -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG Personal Accident -L}{L-INA Kecelakaan Diri-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify"></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG e. -L}{L-INA e. -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG PA Kreasi -L}{L-INA PA Kreasi-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify"></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG f. -L}{L-INA f. -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG Cash in Safe -L}{L-INA Penyimpanan Uang-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify"></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG g. -L}{L-INA g. -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG Cash In Transit -L}{L-INA Uang Dalam Perjalanan-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify"></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG h. -L}{L-INA h. -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG Contractors All Risks -L}{L-INA Contractors All Risks-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify"></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG i. -L}{L-INA i. -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG Erection All Risks -L}{L-INA Erection All Risks-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify"></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG j. -L}{L-INA j. -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG Machinery Breakdown -L}{L-INA Machinery Breakdown-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify"></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG k. -L}{L-INA k. -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG Surety Bond -L}{L-INA Surety Bond-L}</fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify"></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify">{L-ENG l. -L}{L-INA l. -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="justify" space-after.optimum="15pt">{L-ENG e.t.c. -L}{L-INA Dan lain-lain-L}</fo:block></fo:table-cell>
           </fo:table-row>





         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="160mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="justify" space-after.optimum="15pt">{L-ENG We appreciate to you for this moment, and if you need the detail of information, we with pleasure will give a presentation at the time as we considered. Many thanks for your attention -L}{L-INA Demikian untuk Bapak / Ibu maklumi, bila Bapak / Ibu memerlukan penjelasan dan informasi lebih rinci dengan senang hati kami siap memberikan penjelasan lebih lanjut pada waktu dan kesempatan yang kita sepakati. Atas perhatian dan kesempatan yang telah diberikan, kami ucapkan terima kasih.-L}</fo:block></fo:table-cell>
           </fo:table-row>


         </fo:table-body>
       </fo:table>
       </fo:block>


      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="70mm"/>
         <fo:table-column />
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell ><fo:block text-align="center">{L-ENG Sincerely Yours-L}{L-INA Hormat Kami-L}</fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
           </fo:table-row>
  
      </fo:table-body>
       </fo:table>
       </fo:block>

    </fo:flow>
  </fo:page-sequence>
</fo:root>




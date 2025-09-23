<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 java.util.Date,
                 com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

   final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
   final String fontsize = (String) request.getAttribute("FONTSIZE");
   final String attached = (String) request.getAttribute("attached");
   boolean isAttached = attached.equalsIgnoreCase("3")?false:true;

   boolean effective = pol.isEffective();

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->
    <fo:simple-page-master master-name="only"
                  page-height="30cm"
                  page-width="21cm"
                  margin-top="0.80cm"
                  margin-bottom="0.5cm"
                  margin-left="1.5cm"
                  margin-right="1.5cm">
      <fo:region-body margin-top="0.5cm" margin-bottom="0.5cm"/>
      <fo:region-before extent="3cm"/>
      <fo:region-after extent="0.5cm"/>
    </fo:simple-page-master>

  </fo:layout-master-set>
  <!-- end: defines page layout -->

  <!-- actual layout -->
  <fo:page-sequence master-reference="only" initial-page-number="1">

    <fo:flow flow-name="xsl-region-body">
    
    <% if (!effective) {%>
      <fo:block font-size="16pt" font-family="TAHOMA"
            line-height="16pt" space-after.optimum="0pt"
            color="red"
            text-align="center"
            padding-top="10pt">
        SPECIMEN
      </fo:block>
<% }%>

   <!-- GARIS  -->
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-before.optimum="50pt" space-after.optimum="1pt"></fo:block>
	
      <!-- defines text title level 1-->

      <fo:block font-size="16pt" font-family="TAHOMA" line-height="0pt" space-after.optimum="10pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       <%=pol.getStPolicyTypeDesc2()%>
      </fo:block>

      <!-- Normal text -->

      <!-- defines text title level 1-->

      <fo:block font-size="12pt" font-family="TAHOMA" line-height="0pt" space-after.optimum="10pt"
            color="black"
            text-align="center"
            padding-top="10pt">
       (Surety Bond)
      </fo:block>

      <!-- Normal text -->

   <!-- GARIS  -->
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt" text-align="justify"></fo:block>

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
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="175mm"/>
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 1. -L}{L-INA 1. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Oleh karena -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference5())%></fo:inline> {L-ENG 1. -L}{L-INA selanjutnya disebut <fo:inline font-weight="bold">"PEJABAT PEMBUAT KOMITMEN"</fo:inline> -L} {L-ENG 1. -L}{L-INA telah mengundang : -L}</fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
             <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block><fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference1())%></fo:inline> {L-ENG 1. -L}{L-INA yang beralamat di -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference3())%></fo:inline>{L-ENG 1. -L}{L-INA selanjutnya disebut <fo:inline font-weight="bold">"PENYEDIA JASA"</fo:inline> untuk pekerjaan -L}<fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference6())%></fo:inline></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 2. -L}{L-INA 2. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Dan oleh karena itu <fo:inline font-weight="bold">PENYEDIA JASA</fo:inline> terikat oleh kontrak yang mewajibkan <fo:inline font-weight="bold">PENYEDIA JASA</fo:inline> memberikan jaminan pelaksanaan kepada <fo:inline font-weight="bold">PEJABAT PEMBUAT KOMITMEN</fo:inline> sebesar ....% persen -L}</fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 3. -L}{L-INA 3. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Maka kami <fo:inline font-weight="bold">PENJAMIN</fo:inline> yang bertanggung jawab dan mewakili -L}<fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference9Desc())%></fo:inline>{L-ENG 1. -L}{L-INA selanjutnya disebut <fo:inline font-weight="bold">"BANK"</fo:inline>, berwenang penuh untuk menandatangani dan melaksanakan kewajiban atas nama <fo:inline font-weight="bold">BANK</fo:inline>, dengan ini menyatakan bahwa <fo:inline font-weight="bold">BANK</fo:inline> menjamin <fo:inline font-weight="bold">PEJABAT PEMBUAT KOMITMEN</fo:inline> atas seluruh nilai uang sebesar Rp. -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:inline> {L-ENG 1. -L}{L-INA (terbilang -L} <%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbPremiTotal(),2),"",NumberSpell.INDONESIA)%> RUPIAH)-L}</fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 4. -L}{L-INA 4. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Ketentuan kewajiban ini adalah: -L}</fo:block></fo:table-cell>
           </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="170mm"/>
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG a. -L}{L-INA a. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Setelah <fo:inline font-weight="bold">PENYEDIA JASA</fo:inline> menandatangani kontrak tersebut diatas dengan <fo:inline font-weight="bold">PEJABAT PEMBUAT KOMITMEN</fo:inline>, maka <fo:inline font-weight="bold">BANK</fo:inline> wajib membayar sejumlah uang kepada <fo:inline font-weight="bold">PEJABAT PEMBUAT KOMITMEN</fo:inline> sampai dengan sebesar nilai uang yang disebutkan diatas, setelah mendapat perintah tertulis dari <fo:inline font-weight="bold">PEJABAT PEMBUAT KOMITMEN</fo:inline> untuk membayar ganti rugi kepada <fo:inline font-weight="bold">PEJABAT PEMBUAT KOMITMEN</fo:inline> atas kerugian yang diakibatkan oleh cacat maupun kekurangan atau kegagalan <fo:inline font-weight="bold">PENYEDIA JASA</fo:inline> dalam pelaksanaan pekerjaan sebagaimana yang disyaratkan dalam kontrak tersebut di atas.-L}</fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG b. -L}{L-INA b. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA <fo:inline font-weight="bold">BANK</fo:inline> harus menyerahkan uang yang diperlukan oleh <fo:inline font-weight="bold">PEJABAT PEMBUAT KOMITMEN</fo:inline> dalam waktu waktu 7 (tujuh) hari kalender setelah ada permintaan pertama tanpa penundaan dan tanpa perlu ada pemberitahuan sebelumnya mengenai proses hukum dan adminstratif dan tanpa perlu pembuktian kepada <fo:inline font-weight="bold">BANK</fo:inline> mengenai adanya cacat atau kekurangan atau kegagalan pelaksanaan pada pihak <fo:inline font-weight="bold">PENYEDIA JASA</fo:inline>. -L}</fo:block></fo:table-cell>
           </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="175mm"/>
         <fo:table-body>
         
            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 5. -L}{L-INA 5. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Jaminan ini berlaku sejak tanggal penandatanganan kontrak sampai dengan 14 (empat belas) hari setealah tanggal masa pemeliharaan berakhir berdasarkan kontak atau sampai <fo:inline font-weight="bold">PEJABAT PEMBUAT KOMITMEN</fo:inline> mengeluarkan suatu instruksi kepada <fo:inline font-weight="bold">BANK</fo:inline> yang menyatakan bahwa jaminan ini boleh diakhiri. -L}</fo:block></fo:table-cell>
           </fo:table-row>
           
            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 6. -L}{L-INA 6. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Permintaan pembayaran berkenaan dengan jaminan ini harus telah disampaikan kepada <fo:inline font-weight="bold">BANK</fo:inline> selambat-lambatnya 30 (tiga puluh) hari kalender setelah tanggal berakhirnya jaminan <fo:inline font-weight="bold">BANK</fo:inline> ini yang dinyatakan pada butir 5 diatas.-L}</fo:block></fo:table-cell>
           </fo:table-row>
           
            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 7. -L}{L-INA 7. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA <fo:inline font-weight="bold">BANK</fo:inline> menyanggupi memperpanjang jangka waktu berlakunya jaminan ini berdasarkan syarat-syarat yang sama sebagaimana disebutkan diatas sesuai dengan adanya perubahan atau perpanjangan waktu kontrak sebagaimana yang selanjutnya dapat dilakukan sesuai ketentuan-ketentuan kontrak. -L}</fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 8. -L}{L-INA 8. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Menunjuk ketentuan Pasal 1832 KUHP, <fo:inline font-weight="bold">BANK</fo:inline> mengesampingkan hak preferensinya atas harta benda milik <fo:inline font-weight="bold">PENYEDIA JASA</fo:inline> yang berkenaan dengan penyitaan dan penjualan harta benda tersebut untuk melunasi hutangnya sebagaimana ditentukan dalam Pasal 1831 KUHP. -L}</fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>
           
<% if (isAttached) {%>
            <fo:table-row>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Dengan itikad baik, kami <fo:inline font-weight="bold">PENJAMIN</fo:inline> yang secara sah mewakili <fo:inline font-weight="bold">BANK</fo:inline>, dengan ini membubuhkan tandatangan serta cap dan materai pada jaminan ini pada tanggal -L} <%if(obj.getDtReference2()!=null){%><%=DateUtil.getDateStr(obj.getDtReference2(),"d ^^ yyyy")%><%}else{%><%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%>.</fo:block></fo:table-cell>
           </fo:table-row>
<% } else { %>
			<fo:table-row>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Dengan itikad baik, kami <fo:inline font-weight="bold">PENJAMIN</fo:inline> yang secara sah mewakili <fo:inline font-weight="bold">BANK</fo:inline>, dengan ini membubuhkan tandatangan serta cap dan materai pada jaminan ini. -L}</fo:block></fo:table-cell>
           </fo:table-row>
<% } %>

    </fo:table-body>
       </fo:table>
       </fo:block>
       
      <%-- mulai rincian --%> 
       
        <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="0pt" space-after.optimum="5pt"
            color="black"
            text-align="center"
            padding-top="10pt">
      </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
       
      
       <fo:table table-layout="fixed">
         <fo:table-column column-width="85mm"/>
         <fo:table-column column-width="85mm"/>
         <fo:table-body>
         
         <fo:table-row>
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG XXX -L}{L-INA B A N K -L}</fo:block></fo:table-cell>
         </fo:table-row>
         
         <fo:table-row>
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">Tandatangan, cap dan materai</fo:block></fo:table-cell>
        </fo:table-row>
         
         <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
         </fo:table-row>
         
         </fo:table-body>
        </fo:table>
        
        <fo:table table-layout="fixed">
         <fo:table-column column-width="15mm"/>
         <fo:table-column column-width="55mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="55mm"/>
         <fo:table-column />
         <fo:table-body>
         
         <fo:table-row>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>
              <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
         	  <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>

         </fo:table-row>
         
         <fo:table-row>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>
         </fo:table-row>
         
         <fo:table-row>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>
            <fo:table-cell ><fo:block space-after.optimum="40pt"></fo:block></fo:table-cell>
            <fo:table-cell><fo:block></fo:block></fo:table-cell>
            <fo:table-cell ><fo:block space-after.optimum="40pt"></fo:block></fo:table-cell>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>
         </fo:table-row>
         
         <%if(pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")){%>
             <fo:table-row>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>
             	<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
       			<fo:table-cell><fo:block></fo:block></fo:table-cell>
       			<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference8())%></fo:block></fo:table-cell>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>	
	        </fo:table-row>
        
                     <fo:table-row>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>
                      <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                      <fo:table-cell><fo:block></fo:block></fo:table-cell>
                       <fo:table-cell number-columns-spanned="1" >
                           <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0.2" line-height="0.15pt" space-after.optimum="0.5pt"></fo:block>
                      </fo:table-cell>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>
                    </fo:table-row>

         
          <fo:table-row>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
              <fo:table-cell><fo:block></fo:block></fo:table-cell>
              <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">Penjamin</fo:block></fo:table-cell>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>

          </fo:table-row>

         <% }else{ %>
          <% } %>
         
         
         
         </fo:table-body>
        </fo:table>
<% }%>      
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



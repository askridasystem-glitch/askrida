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
             <fo:table-cell><fo:block><fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference1())%></fo:inline> {L-ENG 1. -L}{L-INA yang beralamat di -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference3())%></fo:inline>{L-ENG 1. -L}{L-INA selanjutnya disebut <fo:inline font-weight="bold">"PESERTA LELANG"</fo:inline> mengajukan penawaran untuk -L}<fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference6())%></fo:inline></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 2. -L}{L-INA 2. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Dan oleh karena itu <fo:inline font-weight="bold">PESERTA LELANG</fo:inline> terkait pada istruksi kepada <fo:inline font-weight="bold">PESERTA LELANG</fo:inline> mengenai pekerjaan tersebut di atas yang mewajibkan <fo:inline font-weight="bold">PESERTA LELANG</fo:inline> memberikan kepada <fo:inline font-weight="bold">PEJABAT PEMBUAT KOMITMEN</fo:inline> suatu jaminan penawaran sebesar Rp. -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:inline> {L-ENG 1. -L}{L-INA (terbilang -L} <%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbPremiTotal(),2),"",NumberSpell.INDONESIA)%> RUPIAH)</fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 3. -L}{L-INA 3. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Maka kami <fo:inline font-weight="bold">"PENJAMIN"</fo:inline> yang bertanggung jawab dan mewakili -L}<fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference9Desc())%></fo:inline>{L-ENG 1. -L}{L-INA selanjutnya disebut <fo:inline font-weight="bold">"BANK"</fo:inline>, berwenang penuh untuk menandatangani dan melaksanakan kewajiban atas nama <fo:inline font-weight="bold">BANK</fo:inline>, dengan ini menyatakan bahwa <fo:inline font-weight="bold">BANK</fo:inline> menjamin <fo:inline font-weight="bold">PEJABAT PEMBUAT KOMITMEN</fo:inline> atas seluruh nilai uang sebesar tersebut di atas sebagai jaminan penawaran dari <fo:inline font-weight="bold">PESERTA LELANG</fo:inline> yang mengajukan penawaran untuk pekerjaan tersebut di atas tertanggal -L} <%=JSPUtil.printX(obj.getDtReference4())%> </fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 4. -L}{L-INA 4. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Syarat-syarat kewajiban ini adalah: -L}</fo:block></fo:table-cell>
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
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Apabila <fo:inline font-weight="bold">PESERTA LELANG</fo:inline> menarik kembali penawarannya sebelum berakhirnya masa laku penawaran yang dinyatakan dalam surat penawarannya, atau -L}</fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG b. -L}{L-INA b. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Apabila <fo:inline font-weight="bold">PESERTA LELANG</fo:inline> ditunjuk sebagai pemenang lelang dan dalam masa laku penawaran dan <fo:inline font-weight="bold">PESERTA LELANG</fo:inline> gagal atau menolak :-L}</fo:block></fo:table-cell>
           </fo:table-row>

    </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block font-size="<%=fontsize%>" text-align = "justify">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="5mm"/>
         <fo:table-column column-width="165mm"/>
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG (i). -L}{L-INA (i) -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Memberikan jaminan pelaksanaan yang diperlukan; atau -L}</fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG (ii). -L}{L-INA (ii) -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Untuk menandatangani kontrak; atau -L}</fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG (iii). -L}{L-INA (iii) -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Hasil koreksi aritmatik terhadap penawarannya sebagaimana tersebut pada Pasal 31 Instruksi Kepada <fo:inline font-weight="bold">PESERTA LELANG</fo:inline>.-L}</fo:block></fo:table-cell>
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
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA maka <fo:inline font-weight="bold">BANK</fo:inline> wajib membayar sepenuhnya jaminan penawaran tersebut di atas kepada <fo:inline font-weight="bold">PEJABAT PEMBUAT KOMITMEN</fo:inline> dalam waktu 7 (tujuh) hari setelah menerima permintaan pertama dari <fo:inline font-weight="bold">PEJABAT PEMBUAT KOMITMEN</fo:inline>, dan tanpa mempertimbangkan adanya keberatan dari <fo:inline font-weight="bold">PESERTA LELANG</fo:inline> -L}</fo:block></fo:table-cell>
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
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Jaminan ini berlaku sepenuhnya selama jangka waktu -L}  {L-ENG XXX -L}{L-INA ( -L}  {L-ENG XXX -L}{L-INA ) hari kalender sejak batas akhir pemasukan penawaran. -L}</fo:block></fo:table-cell>
           </fo:table-row>
           
            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 6. -L}{L-INA 6. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Setiap permintaan pembayaran atas jaminan ini harus telah diterima oleh <fo:inline font-weight="bold">BANK</fo:inline> selambat-lambatnya 30 (tiga puluh) hari setelah tanggal berakhir berlakunya jaminan <fo:inline font-weight="bold">BANK</fo:inline> sebagaimana disebutkan dalam butir 5 diatas. -L}</fo:block></fo:table-cell>
           </fo:table-row>
           
            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 7. -L}{L-INA 7. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Menunjuk ketentuan Pasal 1832 KUHP, <fo:inline font-weight="bold">BANK</fo:inline> mengesampingkan hak preferensinya atas harta benda milik <fo:inline font-weight="bold">PESERTA LELANG</fo:inline> yang berkenaan dengan penyitaan dan penjualan harta benda tersebut untuk melunasi hutangnya sebagaimana ditentukan dalam Pasal 1831 KUHP. -L}</fo:block></fo:table-cell>
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



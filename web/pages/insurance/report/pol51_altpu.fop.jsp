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
         <fo:table-column column-width="25mm"/>
         <fo:table-column column-width="25mm"/>
         <fo:table-column column-width="60mm"/>
         <fo:table-column column-width="60mm"/>
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG Bond No. : -L}{L-INA No. Bond : -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block text-align = "end">{L-ENG Value : -L}{L-INA Nilai : -L}<%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block><%=JSPUtil.print(pol.getStCurrencyCode())%><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%> </fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
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
             <fo:table-cell><fo:block>{L-ENG 1. -L}{L-INA 1. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Dengan ini dinyatakan bahwa kami : -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference1())%></fo:inline> {L-ENG 1. -L}{L-INA yang beralamat di -L} <%=JSPUtil.printX(obj.getStReference3())%>  {L-ENG 1. -L}{L-INA sebagai Kontraktor, selanjutnya disebut PRINCIPAL, dan -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference6Desc())%></fo:inline> {L-ENG 1. -L}{L-INA yang beralamat di -L}<%=JSPUtil.printX(obj.getStReference7())%>{L-ENG 1. -L}{L-INA sebagai PENJAMIN, selanjutnya disebut sebagai SURETY, bertanggung jawab dan dengan tegas terikat pada -L} <fo:inline font-weight="bold"><%=pol.getStCustomerName()%></fo:inline> {L-ENG 1. -L}{L-INA yang beralamat di -L} <%=pol.getStCustomerAddress()%>{L-ENG 1. -L}{L-INA sebagai Pemilik, selanjutnya disebut sebagai OBLIGEE atas uang sejumlah -L}<%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%> {L-ENG 1. -L}{L-INA Terbilang ( -L}<%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(),2))%> RUPIAH) </fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 2. -L}{L-INA 2. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Maka kami PRINCIPAL dan SURETY dengan ini mengikatkan diri untuk melakukan pembayaran jumlah tersebut diatas dengan baik dan benar bilamana PRINCIPAL tidak memenuhi kewajiban sebagaimana ditetapkan dalam Instruksi kepada Peserta Lelang untuk Pekerjaan -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference11())%></fo:inline> {L-ENG 1. -L}{L-INA yang diselenggarakan oleh OBLIGEE pada tanggal -L} <%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%> {L-ENG 1. -L}{L-INA (tanggal Pelelangan) -L} di  <%=JSPUtil.printX(obj.getStReference12())%> </fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 3. -L}{L-INA 3. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Syarat-syarat kewajiban ini adalah : -L}</fo:block></fo:table-cell>
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
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Apabila peserta lelang menarik kembali penawarannya sebelum berakhirnya masa laku penawaran yang dinyatakan dalam surat penawarannya, atau -L}</fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG b. -L}{L-INA b. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Apabila peserta lelang ditunjuk sebagai pemenang lelang dan dalam masa laku penawaran dan peserta lelang gagal atau menolak :-L}</fo:block></fo:table-cell>
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
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Hasil koreksi aritmatik terhadap penawarannya sebagaimana tersebut pada Pasal 31 Instruksi Kepada Peserta Lelang.-L}</fo:block></fo:table-cell>
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
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA maka SURETY wajib membayar sepenuhnya jaminan penawaran tersebut di atas kepada OBLIGEE dalam waktu 7 (tujuh) hari setelah menerima permintaan pertama dari OBLIGEE, dan tanpa mempertimbangkan adanya keberatan dari PRINCIPAL -L}</fo:block></fo:table-cell>
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
             <fo:table-cell><fo:block>{L-ENG 4. -L}{L-INA 4. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Tuntutan penagihan (klaim) atas surat Jaminan ini dilaksanakan oleh OBLIGEE secara tertulis kepada SURETY segera setelah timbul cidera janji (Wanprestasi/default) oleh pihak PRINCIPAL sesuai dengan ketentuan-ketentuan dalam Dokumen Lelang. -L}</fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA SURETY akan membayar kepada OBLIGEE dalam jumlah penuh selambat-lambatnya 30 (tiga puluh) hari kalender setelah menerima tuntutan penagihan dari pihak OBLIGEE berdasar keputusan OBLIGEE mengenai pengenaan sanksi akibat tindakan cidera oleh pihak PRINCIPAL. -L}</fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 5. -L}{L-INA 5. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Menunjuk pada pasal 1832 KUH Perdata dengan ini ditegaskan kembali bahwa SURETY melepaskan hak-hak istimewanya untuk menuntut supaya harta benda pihak yang dijamin lebih dahulu disita dan dijual guna melunasi hutangnya sebagaimana dimaksud dalam Pasal 1831 KUH Perdata. -L}</fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell><fo:block>{L-ENG 6. -L}{L-INA 6. -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Setiap pengajuan ganti rugi terhadap SURETY berdasarkan jaminan ini harus sudah diajukan selambat-lambatnya dalam waktu 3 (tiga) bulan sesudah berakhirnya masa laku Jaminan ini. -L}</fo:block></fo:table-cell>
           </fo:table-row>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
           </fo:table-row>
           
<% if (isAttached) {%>
            <fo:table-row>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Ditanda tangani serta dibubuhkan materai di -L} <%= JSPUtil.printX(pol.getStCostCenterDesc())%>  {L-ENG XXX -L}{L-INA pada tanggal -L} <%if(obj.getDtReference2()!=null){%><%=DateUtil.getDateStr(obj.getDtReference2(),"d ^^ yyyy")%><%}else{%><%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%>.</fo:block></fo:table-cell>
           </fo:table-row>
<% } else { %>
			<fo:table-row>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Ditanda tangani serta dibubuhkan materai di -L} <%= JSPUtil.printX(pol.getStCostCenterDesc())%>.</fo:block></fo:table-cell>
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
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG XXX -L}{L-INA PRINCIPAL -L}</fo:block></fo:table-cell>
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG XXX -L}{L-INA SURETY -L}</fo:block></fo:table-cell>
         </fo:table-row>
         
         <fo:table-row>
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
             
         <%if(pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")){%>
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference6Desc())%></fo:block></fo:table-cell>
         <% }else{ %>
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
         <% } %>
         
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
             	<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference4())%></fo:block></fo:table-cell>
       			<fo:table-cell><fo:block></fo:block></fo:table-cell>
       			<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference8())%></fo:block></fo:table-cell>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>	
	        </fo:table-row>
        
                     <fo:table-row>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>
                      <fo:table-cell number-columns-spanned="1" >
                           <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                      </fo:table-cell>
                      <fo:table-cell><fo:block></fo:block></fo:table-cell>
                       <fo:table-cell number-columns-spanned="1" >
                           <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0.2" line-height="0.15pt" space-after.optimum="0.5pt"></fo:block>
                      </fo:table-cell>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>
                    </fo:table-row>

         
          <fo:table-row>
         	 <fo:table-cell><fo:block></fo:block></fo:table-cell>
             <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference5())%></fo:block></fo:table-cell>
              <fo:table-cell><fo:block></fo:block></fo:table-cell>
              <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference9())%></fo:block></fo:table-cell>
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



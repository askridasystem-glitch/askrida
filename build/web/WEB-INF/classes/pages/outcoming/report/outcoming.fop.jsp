<%@ page import="com.webfin.outcoming.model.*,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil,
                 java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

   final OutcomingView outcoming = (OutcomingView)request.getAttribute("OUTCOMING");
   
   String fontsize = "10pt";

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->

    <fo:simple-page-master master-name="only"
                  page-height="29.7cm"
                  page-width="19cm"
                  margin-top="1cm"
                  margin-bottom="2cm"
                  margin-left="2cm"
                  margin-right="2cm">

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

    <fo:flow flow-name="xsl-region-body">
    
   <!-- GARIS  -->
     <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="10pt"></fo:block>

      <!-- defines text title level 1-->

      <fo:block font-size="16pt" font-family="TAHOMA" line-height="16pt" color="black" text-align="center" space-after.optimum="10pt">
       Surat Keluar
      </fo:block>

   <!-- GARIS -->
     <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>

      <!-- Normal text -->

   <!-- ROW -->
      <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Header Number-L}{L-INA Nomor Surat -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= outcoming.getStRefNo()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Sender-L}{L-INA Pengirim-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=outcoming.getStSender()%></fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
                 <fo:table-cell ><fo:block>Penerima</fo:block></fo:table-cell>
                 <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                 <fo:table-cell ><fo:block>
                     <fo:block font-size="<%=fontsize%>">
                     <fo:table table-layout="fixed">
                     <fo:table-column />
                     <fo:table-body>
                     <%
                        DTOList dist = outcoming.getDistributions();

                        for(int i=0;i< dist.size();i++){
                            OutcomingDistributionView rec = (OutcomingDistributionView) dist.get(i);

                     %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%if(dist.size()>1){%> - <%}%> <%=rec.getStReceiver()%> (<%=rec.getUser(rec.getStReceiver()).getStUserName()%>) </fo:block></fo:table-cell>
                            
                        </fo:table-row>
                     <%

                        }
                     %>
                     </fo:table-body>
                     </fo:table>
                     </fo:block>
                         
                 </fo:block></fo:table-cell>
           </fo:table-row>
               
           

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Title-L}{L-INA Judul-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(outcoming.getStSubject())%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Date-L}{L-INA Tanggal Surat-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=DateUtil.getDateStr(outcoming.getDtLetterDate(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Message-L}{L-INA Pesan-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block wrap-option="yes-wrap" linefeed-treatment="preserve"
        white-space-treatment="preserve" white-space-collapse="false"><%=JSPUtil.xmlEscape(outcoming.getStNote())%></fo:block></fo:table-cell>
           </fo:table-row>
         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="7pt"
                font-family="sans-serif"
                line-height="10pt"
                space-before.optimum="30pt"
                text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%>

      </fo:block>

    </fo:flow>
  </fo:page-sequence>
</fo:root>




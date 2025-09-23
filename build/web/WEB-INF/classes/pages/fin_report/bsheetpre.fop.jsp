<%@ page import="com.webfin.gl.util.GLUtil,
                 com.webfin.gl.ejb.GLReportEngine2,
                 com.webfin.gl.report2.form.FinReportForm,
                 java.util.Date,
                 java.math.BigDecimal,
                 com.crux.ff.FlexTableManager,
                 com.crux.ff.model.FlexTableView,
                 java.util.HashMap,
                 com.webfin.gl.model.GLInfoView,
                 com.crux.util.*,
                 java.util.ArrayList,
                 com.webfin.gl.model.JournalView,
                 com.crux.util.fop.FOPUtil"%><?xml version="1.0" ?>

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
  <fo:page-sequence master-reference="first" initial-page-number="1">
   <fo:static-content flow-name="xsl-region-before"> 
    <fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
      <fo:retrieve-marker retrieve-class-name="message"
      retrieve-boundary="page"
      retrieve-position="first-starting-within-page"/>
      </fo:block>
</fo:block-container>
<fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
      <fo:retrieve-marker retrieve-class-name="term"
      retrieve-boundary="page"
      retrieve-position="last-ending-within-page"/>
      </fo:block>
</fo:block-container>
    </fo:static-content>
  
  <fo:static-content flow-name="xsl-region-after">
  <fo:block text-align="end"
      font-size="8pt" font-family="serif" line-height="1em + 2pt">
      {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
ref-id="end-of-document"/>
      </fo:block>      
    </fo:static-content>

<%
   GLReportEngine2 glr = new GLReportEngine2();

   FinReportForm form = (FinReportForm) request.getAttribute("FORM");

   long lPeriodFrom = form.getLPeriodFrom();
   long lPeriodTo = form.getLPeriodTo();
   long lYearFrom = form.getLYearFrom();
   long lYearTo = form.getLYearTo();

   glr.setBranch(form.getBranch());

   HashMap refmap = form.getRptRef();
  
  String rptfmt = form.getRptfmt();

   boolean rptfmt_default = "default".equals(rptfmt);
   boolean rptfmt_model1 = "model1".equals(rptfmt);

   ArrayList cmap = new ArrayList();

   cmap.add(new Integer(30));
   cmap.add(new Integer(60));
   cmap.add(new Integer(20));
   

   ArrayList cw = FOPUtil.computeColumnWidth(cmap,16,3,"cm");

%>


<fo:flow flow-name="xsl-region-body">

<%
   String bw = "0.5pt";
%>


   <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">TITIPAN PREMI RINCI</fo:block>


   <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getPeriodTitleDescription())%></fo:block>
   <fo:block space-after.optimum="14pt"/>

  <fo:block font-family="Helvetica" font-size="6pt" display-align="center" border-width="0.1pt">
   <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>">
<%
   for (int i = 0; i < cw.size(); i++) {
      String cwx = (String) cw.get(i);
      %>
      <fo:table-column column-width="<%=cwx%>"/>
      <%
   }
%>
      <%--<fo:table-column column-width="100mm"/>
      <fo:table-column column-width="60mm"/>--%>
      <fo:table-body>
         <fo:table-row  >
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">NO POLIS</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">URAIAN</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">JUMLAH</fo:block></fo:table-cell>
        </fo:table-row>
        
        <fo:table-row>          
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" font-style="italic" font-weight="bold" start-indent="5mm">Titipan Premi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
         </fo:table-row>
        

<%
         DTOList a1 = glr.getGLAccounts("BAL|ADD=1","4892","4892",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal a2 = null;
         BigDecimal a3 = null;
         for (int j = 0; j < a1.size(); j++) {
            JournalView gli = (JournalView) a1.get(j);
			
			a2 = gli.getDbCredit();
			
			a3 = BDUtil.add(a3, a2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStTransactionNo()%></fo:block></fo:table-cell>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(a2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm"></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(a3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         				<fo:table-row>
          					<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
          						<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        				</fo:table-row>
        				
      </fo:table-body>
    </fo:table>

    <fo:block font-style="italic">IT-08-10-2005 KODASI '98</fo:block>

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
<fo:block id="end-of-document"><fo:marker
    marker-class-name="term">
<fo:instream-foreign-object>
<svg xmlns="http://www.w3.org/2000/svg" width="15cm" height="1cm" xml:space="preserve">     
<rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
</svg>
</fo:instream-foreign-object>
</fo:marker>
  </fo:block>


</fo:flow>
</fo:page-sequence>
</fo:root>


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

   cmap.add(new Integer(60));
   cmap.add(new Integer(20));

   ArrayList cw = FOPUtil.computeColumnWidth(cmap,16,2,"cm");

%>


<fo:flow flow-name="xsl-region-body">

<%
   String bw = "0.5pt";
%>


   <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">NERACA DETAIL RINCI</fo:block>


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
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">URAIAN</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">JUMLAH</fo:block></fo:table-cell>
        </fo:table-row>
        
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" font-weight="bold">AKTIVA</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>
        
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" font-style="italic" font-weight="bold" start-indent="5mm">INVESTASI</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>
        
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Deposito</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList a1 = glr.getGLJeDetail("BAL|ADD=1","1111","1112",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal a2 = null;
         BigDecimal a3 = null;
         for (int j = 0; j < a1.size(); j++) {
            JournalView gli = (JournalView) a1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) a2 = gli.getDbCredit();
			else a2 = gli.getDbDebit();
			
			a3 = BDUtil.add(a3, a2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(a2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(a3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         				<fo:table-row>
          					<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
          						<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        				</fo:table-row>
                    
                    
                    
         <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Sertifikat Deposito</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList b1 = glr.getGLJeDetail("BAL|ADD=1","1113","1113",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal b2 = null;
         BigDecimal b3 = null;
         for (int j = 0; j < b1.size(); j++) {
            JournalView gli = (JournalView) b1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) b2 = gli.getDbCredit();
			else b2 = gli.getDbDebit();
			
			b3 = BDUtil.add(b3, b2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(b2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(b3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
          					<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>

 		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Surat Berharga</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>
        
<%
      DTOList c1 = glr.getGLJeDetail("BAL|ADD=1","112","112",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);

           BigDecimal c2 = null;
         BigDecimal c3 = null;
         for (int j = 0; j < c1.size(); j++) {
            JournalView gli = (JournalView) c1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) c2 = gli.getDbCredit();
			else c2 = gli.getDbDebit();
			
			c3 = BDUtil.add(c3, c2);
%>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(c2,2)%></fo:block></fo:table-cell>
        </fo:table-row>
<%
         }
%> 

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(c3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
        			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
        	
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Penyertaan</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList d1 = glr.getGLJeDetail("BAL|ADD=1","115","115",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal d2 = null;
         BigDecimal d3 = null;
         for (int j = 0; j < d1.size(); j++) {
            JournalView gli = (JournalView) d1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) d2 = gli.getDbCredit();
			else d2 = gli.getDbDebit();
			
			d3 = BDUtil.add(d3, d2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(d2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%
         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(d3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
          				<fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Property</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList e1 = glr.getGLJeDetail("BAL|ADD=1","116","116",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal e2 = null;
         BigDecimal e3 = null;
         for (int j = 0; j < e1.size(); j++) {
            JournalView gli = (JournalView) e1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) e2 = gli.getDbCredit();
			else e2 = gli.getDbDebit();
			
			e3 = BDUtil.add(e3, e2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(e2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(e3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			 <fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
         			 
         			 
       <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Pinjaman Hipotik</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList f1 = glr.getGLJeDetail("BAL|ADD=1","117","117",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal f2 = null;
         BigDecimal f3 = null;
         for (int j = 0; j < f1.size(); j++) {
            JournalView gli = (JournalView) f1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) f2 = gli.getDbCredit();
			else f2 = gli.getDbDebit();
			
			f3 = BDUtil.add(f3, f2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(f2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(f3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Investasi Lain</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList g1 = glr.getGLJeDetail("BAL|ADD=1","119","119",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal g2 = null;
         BigDecimal g3 = null;
         for (int j = 0; j < g1.size(); j++) {
            JournalView gli = (JournalView) g1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) g2 = gli.getDbCredit();
			else g2 = gli.getDbDebit();
			
			g3 = BDUtil.add(g3, g2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(g2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(g3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
							<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Kas dan Bank</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList h1 = glr.getGLJeDetail("BAL|ADD=1","120","123",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal h2 = null;
         BigDecimal h3 = null;
         for (int j = 0; j < h1.size(); j++) {
            JournalView gli = (JournalView) h1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) h2 = gli.getDbCredit();
			else h2 = gli.getDbDebit();
			
			h3 = BDUtil.add(h3, h2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(h2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(h3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
        				<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
                                                                  
		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Piutang Premi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList i1 = glr.getGLJeDetail("BAL|ADD=1","13","13",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal i2 = null;
         BigDecimal i3 = null;
         for (int j = 0; j < i1.size(); j++) {
            JournalView gli = (JournalView) i1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) i2 = gli.getDbCredit();
			else i2 = gli.getDbDebit();
			
			i3 = BDUtil.add(i3, i2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(i2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(i3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        <fo:table-row>
        
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
                     
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Piutang Reasuransi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList j1 = glr.getGLJeDetail("BAL|ADD=1","14","14",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal j2 = null;
         BigDecimal j3 = null;
         for (int j = 0; j < j1.size(); j++) {
            JournalView gli = (JournalView) j1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) j2 = gli.getDbCredit();
			else j2 = gli.getDbDebit();
			
			j3 = BDUtil.add(j3, j2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(j2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(j3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
					<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>   
        			                 
         <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Piutang Hasil Investasi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList k1 = glr.getGLJeDetail("BAL|ADD=1","15","15",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal k2 = null;
         BigDecimal k3 = null;
         for (int j = 0; j < k1.size(); j++) {
            JournalView gli = (JournalView) k1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) k2 = gli.getDbCredit();
			else k2 = gli.getDbDebit();
			
			k3 = BDUtil.add(k3, k2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(k2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(k3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>  
                    
          <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Piutang Lain</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList l1 = glr.getGLJeDetail("BAL|ADD=1","16","16",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal l2 = null;
         BigDecimal l3 = null;
         for (int j = 0; j < k1.size(); j++) {
            JournalView gli = (JournalView) l1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) l2 = gli.getDbCredit();
			else l2 = gli.getDbDebit();
			
			l3 = BDUtil.add(l3, l2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(l2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(l3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
                    
         <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Biaya Dibayar Dimuka</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList m1 = glr.getGLJeDetail("BAL|ADD=1","17","17",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal m2 = null;
         BigDecimal m3 = null;
         for (int j = 0; j < k1.size(); j++) {
            JournalView gli = (JournalView) m1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) m2 = gli.getDbCredit();
			else m2 = gli.getDbDebit();
			
			m3 = BDUtil.add(m3, m2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(m2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(m3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>            
                    
         <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Aktiva Tetap</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList n1 = glr.getGLJeDetail("BAL|ADD=1","18","18",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal n2 = null;
         BigDecimal n3 = null;
         for (int j = 0; j < n1.size(); j++) {
            JournalView gli = (JournalView) k1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) n2 = gli.getDbCredit();
			else n2 = gli.getDbDebit();
			
			n3 = BDUtil.add(n3, n2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(n2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(n3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row> 
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Aktiva Lain-Lain</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList o1 = glr.getGLJeDetail("BAL|ADD=1","19","19",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal o2 = null;
         BigDecimal o3 = null;
         for (int j = 0; j < o1.size(); j++) {
            JournalView gli = (JournalView) o1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) o2 = gli.getDbCredit();
			else o2 = gli.getDbDebit();
			
			o3 = BDUtil.add(o3, o2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(o2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(o3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
        			
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Aktiva Pajak Tangguhan</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList p1 = glr.getGLJeDetail("BAL|ADD=1","19144","19144",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal p2 = null;
         BigDecimal p3 = null;
         for (int j = 0; j < p1.size(); j++) {
            JournalView gli = (JournalView) p1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) p2 = gli.getDbCredit();
			else p2 = gli.getDbDebit();
			
			p3 = BDUtil.add(p3, p2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(p2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(p3,2)%></fo:block></fo:table-cell>
        </fo:table-row> 
        
        	
        			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
        			
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" font-weight="bold">KEWAJIBAN DAN EKUITAS</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>
        
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" font-style="italic" font-weight="bold" start-indent="5mm">KEWAJIBAN</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>
                    
         <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" font-weight="bold" start-indent="10mm">Utang Klaim</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList q1 = glr.getGLJeDetail("BAL|ADD=1","33","33",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal q2 = null;
         BigDecimal q3 = null;
         for (int j = 0; j < q1.size(); j++) {
            JournalView gli = (JournalView) q1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) q2 = gli.getDbCredit();
			else q2 = gli.getDbDebit();
			
			q3 = BDUtil.add(q3, q2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(q2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="8pt" font-weight="bold"><%=JSPUtil.printX(q3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>           
                    
         <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Estimasi Klaim Retensi Sendiri</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList r1 = glr.getGLJeDetail("BAL|ADD=1","32","32",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal r2 = null;
         BigDecimal r3 = null;
         for (int j = 0; j < r1.size(); j++) {
            JournalView gli = (JournalView) r1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) r2 = gli.getDbCredit();
			else r2 = gli.getDbDebit();
			
			r3 = BDUtil.add(r3, r2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(r2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(r3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Premi Yang Belum Merupakan Pendapatan</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList s1 = glr.getGLJeDetail("BAL|ADD=1","34","34",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal s2 = null;
         BigDecimal s3 = null;
         for (int j = 0; j < s1.size(); j++) {
            JournalView gli = (JournalView) s1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) s2 = gli.getDbCredit();
			else s2 = gli.getDbDebit();
			
			s3 = BDUtil.add(s3, s2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(s2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(s3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Utang Reasuransi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList t1 = glr.getGLJeDetail("BAL|ADD=1","42","42",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal t2 = null;
         BigDecimal t3 = null;
         for (int j = 0; j < t1.size(); j++) {
            JournalView gli = (JournalView) t1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) t2 = gli.getDbCredit();
			else t2 = gli.getDbDebit();
			
			t3 = BDUtil.add(t3, t2);
            %>
        <fo:table-row>
          <fo:table-cell border-width="<%=bw%>" border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(t2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(t3,2)%></fo:block></fo:table-cell>
        </fo:table-row> 
        
        			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>         
                    
         <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Utang Komisi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList u1 = glr.getGLJeDetail("BAL|ADD=1","43","43",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal u2 = null;
         BigDecimal u3 = null;
         for (int j = 0; j < u1.size(); j++) {
            JournalView gli = (JournalView) u1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) u2 = gli.getDbCredit();
			else u2 = gli.getDbDebit();
			
			u3 = BDUtil.add(u3, u2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(u2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(u3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row> 
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Utang Pajak</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList v1 = glr.getGLJeDetail("BAL|ADD=1","44","44",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal v2 = null;
         BigDecimal v3 = null;
         for (int j = 0; j < v1.size(); j++) {
            JournalView gli = (JournalView) v1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) v2 = gli.getDbCredit();
			else v2 = gli.getDbDebit();
			
			v3 = BDUtil.add(v3, v2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(v2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(v3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row> 
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Biaya Yang Masih Harus Dibayar</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList w1 = glr.getGLJeDetail("BAL|ADD=1","46","47",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal w2 = null;
         BigDecimal w3 = null;
         for (int j = 0; j < w1.size(); j++) {
            JournalView gli = (JournalView) w1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) w2 = gli.getDbCredit();
			else w2 = gli.getDbDebit();
			
			w3 = BDUtil.add(w3, w2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(w2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(w3,2)%></fo:block></fo:table-cell>
        </fo:table-row> 
        
        				<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row> 
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Utang Subordinasi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList x1 = glr.getGLJeDetail("BAL|ADD=1","34","34",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal x2 = null;
         BigDecimal x3 = null;
         for (int j = 0; j < x1.size(); j++) {
            JournalView gli = (JournalView) x1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) x2 = gli.getDbCredit();
			else x2 = gli.getDbDebit();
			
			x3 = BDUtil.add(x3, x2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(x2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(x3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
        
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">EKUITAS</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>            
        
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Modal Dasar 20.000 Lbr @ Rp. 10.000.000</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>
        
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Modal Ditempatkan Dan Disetor</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList y1 = glr.getGLJeDetail("BAL|ADD=1","511","511",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal y2 = null;
         BigDecimal y3 = null;
         for (int j = 0; j < y1.size(); j++) {
            JournalView gli = (JournalView) y1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) y2 = gli.getDbCredit();
			else y2 = gli.getDbDebit();
			
			y3 = BDUtil.add(y3, y2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(y2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(y3,2)%></fo:block></fo:table-cell>
        </fo:table-row> 
        
        			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>             
                    
         <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Agio Saham</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList z1 = glr.getGLJeDetail("BAL|ADD=1","513","513",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal z2 = null;
         BigDecimal z3 = null;
         for (int j = 0; j < z1.size(); j++) {
            JournalView gli = (JournalView) z1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) z2 = gli.getDbCredit();
			else z2 = gli.getDbDebit();
			
			z3 = BDUtil.add(z3, z2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(z2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(z3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Cadangan Umum</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList aa1 = glr.getGLJeDetail("BAL|ADD=1","51620","51621",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal aa2 = null;
         BigDecimal aa3 = null;
         for (int j = 0; j < aa1.size(); j++) {
            JournalView gli = (JournalView) aa1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) aa2 = gli.getDbCredit();
			else aa2 = gli.getDbDebit();
			
			aa3 = BDUtil.add(aa3, aa2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(aa2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(aa3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row> 
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Cadangan Khusus</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList ab1 = glr.getGLJeDetail("BAL|ADD=1","51622","51622",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal ab2 = null;
         BigDecimal ab3 = null;
         for (int j = 0; j < ab1.size(); j++) {
            JournalView gli = (JournalView) ab1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) ab2 = gli.getDbCredit();
			else ab2 = gli.getDbDebit();
			
			ab3 = BDUtil.add(ab3, ab2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(ab2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(ab3,2)%></fo:block></fo:table-cell>
        </fo:table-row>                  
        
        			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>             
                    
         <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Laba (Rugi) Ditahan</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList ac1 = glr.getGLJeDetail("BAL|ADD=1","51623","51623",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal ac2 = null;
         BigDecimal ac3 = null;
         for (int j = 0; j < ac1.size(); j++) {
            JournalView gli = (JournalView) ac1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) ac2 = gli.getDbCredit();
			else ac2 = gli.getDbDebit();
			
			ac3 = BDUtil.add(ac3, ac2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(ac2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(ac3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Saldo Laba (Rugi) Tahun Lalu</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList ad1 = glr.getGLJeDetail("BAL|ADD=1","51610","51610",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal ad2 = null;
         BigDecimal ad3 = null;
         for (int j = 0; j < ad1.size(); j++) {
            JournalView gli = (JournalView) ad1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) ad2 = gli.getDbCredit();
			else ad2 = gli.getDbDebit();
			
			ad3 = BDUtil.add(ad3, ad2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(ad2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(ad3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="10mm" font-weight="bold">Saldo Laba (Rugi) Tahun Berjalan</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList ae1 = glr.getGLJeDetail("BAL|ADD=1","51611","51611",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal ae2 = null;
         BigDecimal ae3 = null;
         for (int j = 0; j < ae1.size(); j++) {
            JournalView gli = (JournalView) ae1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) ae2 = gli.getDbCredit();
			else ae2 = gli.getDbDebit();
			
			ae3 = BDUtil.add(ae3, ae2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" start-indent="10mm"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(ae2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt" font-weight="bold" start-indent="10mm">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" font-weight="bold"><%=JSPUtil.printX(ae3,2)%></fo:block></fo:table-cell>
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


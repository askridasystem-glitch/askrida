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


   <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">RUGI LABA DETAIL RINCI</fo:block>


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
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" font-style="italic">Pendapatan Premi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>
        
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="5mm">Premi Bruto</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList a1 = glr.getGLJeDetail("BAL|ADD=0","6131","6134",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
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
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="5mm">Premi Reasuransi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList b1 = glr.getGLJeDetail("BAL|ADD=0","63","63",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
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
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="5mm">Kenaikan (Penurunan) Premi yang belum merupakan pendapatan</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>
        
<%
      DTOList c1 = glr.getGLJeDetail("BAL|ADD=0","64","64",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);

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
<% 
BigDecimal jmlh_premi = BDUtil.sub(a3,b3);
jmlh_premi = BDUtil.sub(jmlh_premi,c3);
 %>        			

					<fo:table-row>
                      <fo:table-cell number-columns-spanned="2" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" font-weight="bold" font-style="italic">Jumlah Pendapatan Premi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="8pt" font-weight="bold"><%=JSPUtil.printX(jmlh_premi,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
        			<fo:table-row>
                      <fo:table-cell number-columns-spanned="2" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    
                    <fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="15pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>        
        			
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" font-style="italic">Beban Underwriting</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>
        
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" font-style="italic">Beban Klaim</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>
        
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="5mm">Klaim Bruto</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList d1 = glr.getGLJeDetail("BAL|ADD=1","71","71",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
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
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="5mm">Klaim Reasuransi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList e1 = glr.getGLJeDetail("BAL|ADD=1","72","72",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
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
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="5mm">Kenaikan (Penurunan) Estimasi Klaim Retensi Sendiri</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList f1 = glr.getGLJeDetail("BAL|ADD=1","7500","7500",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
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
        			
<% 
BigDecimal jmlh_klaim = BDUtil.sub(d3,e3);
jmlh_klaim = BDUtil.add(jmlh_klaim,f3);
 %>   
        			
        			<fo:table-row>
                      <fo:table-cell number-columns-spanned="2" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    
		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" font-weight="bold" font-style="italic">Jumlah Beban Klaim</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="8pt" font-weight="bold"><%=JSPUtil.printX(jmlh_klaim,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
        			<fo:table-row>
                      <fo:table-cell number-columns-spanned="2" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    
                    <fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="15pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" start-indent="5mm">Beban Komisi Netto</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList g1 = glr.getGLJeDetail("BAL|ADD=1","77","77",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
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
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" start-indent="5mm">Beban Underwriting Lain Netto</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList h1 = glr.getGLJeDetail("BAL|ADD=1","79","79",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
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
        			
<% 
BigDecimal jmlh_uw = BDUtil.add(jmlh_klaim,g3);
jmlh_uw = BDUtil.add(jmlh_uw,h3);

 %>   
        			
        			<fo:table-row>
                      <fo:table-cell number-columns-spanned="2" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    
		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" font-weight="bold" font-style="italic">Jumlah Beban Underwriting</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="8pt" font-weight="bold"><%=JSPUtil.printX(jmlh_uw,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
        			<fo:table-row>
                      <fo:table-cell number-columns-spanned="2" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>   
                    
                    <fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="15pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>     			
                                                                  
		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt">Hasil Underwriting</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList i1 = glr.getGLJeDetail("BAL|ADD=1","90","90",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal i2 = null;
         BigDecimal i3 = null;
         for (int j = 0; j < i1.size(); j++) {
            JournalView gli = (JournalView) i1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) i2 = gli.getDbCredit();
			else i2 = gli.getDbDebit();
			
			i3 = BDUtil.add(i3, i2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt"><%=gli.getStDescription()%></fo:block></fo:table-cell>
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
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" >Hasil Investasi</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList j1 = glr.getGLJeDetail("BAL|ADD=1","65","65",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal j2 = null;
         BigDecimal j3 = null;
         for (int j = 0; j < j1.size(); j++) {
            JournalView gli = (JournalView) j1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) j2 = gli.getDbCredit();
			else j2 = gli.getDbDebit();
			
			j3 = BDUtil.add(j3, j2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt"><%=gli.getStDescription()%></fo:block></fo:table-cell>
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
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" >Beban Usaha</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList k1 = glr.getGLJeDetail("BAL|ADD=1","81","83",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal k2 = null;
         BigDecimal k3 = null;
         for (int j = 0; j < k1.size(); j++) {
            JournalView gli = (JournalView) k1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) k2 = gli.getDbCredit();
			else k2 = gli.getDbDebit();
			
			k3 = BDUtil.add(k3, k2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt"><%=gli.getStDescription()%></fo:block></fo:table-cell>
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
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt">Laba Usaha</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList l1 = glr.getGLJeDetail("BAL|ADD=1","84","89",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal l2 = null;
         BigDecimal l3 = null;
         for (int j = 0; j < k1.size(); j++) {
            JournalView gli = (JournalView) l1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) l2 = gli.getDbCredit();
			else l2 = gli.getDbDebit();
			
			l3 = BDUtil.add(l3, l2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt"><%=gli.getStDescription()%></fo:block></fo:table-cell>
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
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" >Penghasilan (Beban) Lain-lain</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList m1 = glr.getGLJeDetail("BAL|ADD=1","69","69",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal m2 = null;
         BigDecimal m3 = null;
         for (int j = 0; j < k1.size(); j++) {
            JournalView gli = (JournalView) m1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) m2 = gli.getDbCredit();
			else m2 = gli.getDbDebit();
			
			m3 = BDUtil.add(m3, m2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt"><%=gli.getStDescription()%></fo:block></fo:table-cell>
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
        			
 <%
         BigDecimal laba_pajak = BDUtil.add(l3,m3);
         
 %>
          
                    
         <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" >Laba Sebelum Pajak</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ><%=JSPUtil.printX(laba_pajak,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row> 
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" >Pajak Penghasilan</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList o1 = glr.getGLJeDetail("BAL|ADD=1","90","90",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal o2 = null;
         BigDecimal o3 = null;
         for (int j = 0; j < o1.size(); j++) {
            JournalView gli = (JournalView) o1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) o2 = gli.getDbCredit();
			else o2 = gli.getDbDebit();
			
			o3 = BDUtil.add(o3, o2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt"><%=gli.getStDescription()%></fo:block></fo:table-cell>
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
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" >Pajak Penghasilan (Beban) Tangguhan</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt" ></fo:block></fo:table-cell>
        </fo:table-row>

<%
         DTOList p1 = glr.getGLJeDetail("BAL|ADD=1","910","910",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);
        
           BigDecimal p2 = null;
         BigDecimal p3 = null;
         for (int j = 0; j < p1.size(); j++) {
            JournalView gli = (JournalView) p1.get(j);
			
			if(BDUtil.isZeroOrNull(gli.getDbDebit())) p2 = gli.getDbCredit();
			else p2 = gli.getDbDebit();
			
			p3 = BDUtil.add(p3, p2);
            %>
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt"><%=gli.getStDescription()%></fo:block></fo:table-cell>
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
      
                    
<%--         <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" >Utang Klaim</fo:block></fo:table-cell>
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
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(q2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" font-weight="bold">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="8pt" font-weight="bold"><%=JSPUtil.printX(q3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>           
                    
         <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" >Estimasi Klaim Retensi Sendiri</fo:block></fo:table-cell>
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
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(r2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" font-weight="bold">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="8pt" font-weight="bold"><%=JSPUtil.printX(r3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row>   
                    
        <fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" >Premi Yang Belum Merupakan Pendapatan</fo:block></fo:table-cell>
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
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="6pt"><%=gli.getStDescription()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="6pt"><%=JSPUtil.printX(s2,2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
%>

		<fo:table-row>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="8pt" font-weight="bold">Total</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="8pt" font-weight="bold"><%=JSPUtil.printX(s3,2)%></fo:block></fo:table-cell>
        </fo:table-row>
        
         			<fo:table-row>
          				<fo:table-cell border-left-style="solid"  padding="2pt"><fo:block font-size="10pt" > </fo:block></fo:table-cell>
         				 <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
        			</fo:table-row> --%>
                    
         

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


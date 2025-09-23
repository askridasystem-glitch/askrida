<%@ page import="com.crux.util.fop.FOPTableSource,
				java.util.Date,
				com.crux.util.*,
                 com.webfin.ar.forms.ARReportForm"%><?xml version="1.0" encoding="utf-8"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
<%
   final ARReportForm form = (ARReportForm) request.getAttribute("FRX");
   
   final FOPTableSource source = (FOPTableSource) request.getAttribute("RPT");
   
%>

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->
    <fo:simple-page-master master-name="only"
                  page-width="29.7cm"
                  page-height="21cm"
                  margin-top="0.5cm"
                  margin-bottom="0.5cm"
                  margin-left="1.5cm"
                  margin-right="1.5cm">
      <fo:region-body margin-top="2cm" margin-bottom="0.5cm"/>
      <fo:region-before extent="3cm"/>
      <fo:region-after extent="1.5cm"/>
    </fo:simple-page-master>

  </fo:layout-master-set>
  <!-- end: defines page layout -->

  <!-- actual layout -->
  <fo:page-sequence master-reference="only" initial-page-number="1">

    <!-- usage of page layout -->
    <fo:static-content flow-name="xsl-region-before">
      <fo:block text-align="end"
            font-size="10pt"
            font-family="serif"
            line-height="14pt" >
        Page <fo:page-number/>
      </fo:block>
      <fo:block text-align="center"
            font-size="12pt"
            font-family="serif"
            line-height="14pt" >
        Rekapitulasi <%="AR".equalsIgnoreCase(form.getMode())?"Piutang":"Hutang"%> Reasuransi
        <%--<fo:block>Per 31 Desember 2005</fo:block>--%>
      </fo:block>

    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">

      <!-- defines text title level 1-->
      <fo:block font-size="8pt" line-height="10pt" >
<%
   final FOPTableSource fopTableSource = (FOPTableSource)request.getAttribute("RPT");
   fopTableSource.display(out);
%>
       </fo:block>
    </fo:flow>
  </fo:page-sequence>
</fo:root>

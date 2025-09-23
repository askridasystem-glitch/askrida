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
  <fo:page-sequence master-reference="first">

<%
   GLReportEngine2 glr = new GLReportEngine2();

   FinReportForm form = (FinReportForm) request.getAttribute("FORM");

   long lPeriodFrom = form.getLPeriodFrom();
   long lPeriodTo = form.getLPeriodTo();
   long lYearFrom = form.getLYearFrom();
   long lYearTo = form.getLYearTo();

   glr.setBranch(form.getBranch());

   HashMap refmap = form.getRptRef();
   
   String ftGroup = (String) refmap.get("FT");

   DTOList ftl = FlexTableManager.getInstance().getFlexTable(ftGroup);

   String rptfmt = form.getRptfmt();

   boolean rptfmt_default = "default".equals(rptfmt);
   boolean rptfmt_model1 = "model1".equals(rptfmt);

   ArrayList cmap = new ArrayList();

   if (form.isClShowNo()) cmap.add(new Integer(7));
   if (form.isClTrxNo()) cmap.add(new Integer(20));
   if (form.isClAccNo()) cmap.add(new Integer(20));
   cmap.add(new Integer(60));
   cmap.add(new Integer(20));

   ArrayList cw = FOPUtil.computeColumnWidth(cmap,16,2,"cm");

%>


<fo:flow flow-name="xsl-region-body">

<%
   String bw = "0.5pt";
%>

<%
   for (int i = 0; i < ftl.size(); i++) {
      FlexTableView ft = (FlexTableView) ftl.get(i);

      String opCode = ft.getStReference2();
      String desc = ft.getStReference3();

      boolean isTITLE = "TITLE".equalsIgnoreCase(opCode);

      if (isTITLE) {
         %>
   <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm"><%=desc%></fo:block>
         <%
      }

   }
%>

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
<% if (form.isClShowNo()) {%><fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">No</fo:block></fo:table-cell><% }%>
<% if (form.isClTrxNo()) {%><fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">TRX NO</fo:block></fo:table-cell><% }%>
<% if (form.isClAccNo()) {%><fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">ACCOUNT</fo:block></fo:table-cell><% }%>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">URAIAN</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">SALDO</fo:block></fo:table-cell>
        </fo:table-row>

<%
   String[] indents=null;

   for (int i = 0; i < ftl.size(); i++) {
      FlexTableView ft = (FlexTableView) ftl.get(i);

      //if (i>20) continue;

      String style = ft.getStReference1();
      String opCode = ft.getStReference2();
      String desc = ft.getStReference3();
      String acctFrom = ft.getStReference4();
      String acctTo = ft.getStReference5();
      int iindent = ft.getStReference6()==null?0:Integer.parseInt(ft.getStReference6());
      String parent = ft.getStReferenceID1();
      String flags = ft.getStReference7();

      String indent=(indents!=null && indents.length>iindent)?indents[iindent]:null;

      if (style==null) style="";

      String[] styles = style.split("[\\|]");

      style=styles[0];
      String style1 = styles.length>1?styles[1]:"";

      if (indent!=null) style+=" start-indent=\""+indent+"\"";

      if (opCode==null ) continue;

      boolean isINDENT = "INDENT".equalsIgnoreCase(opCode);
      boolean isDESC = "DESC".equalsIgnoreCase(opCode);
      boolean isACCT = "ACCT".equalsIgnoreCase(opCode);
      boolean isACTD = "ACTD".equalsIgnoreCase(opCode);
      boolean isNL = "NL".equalsIgnoreCase(opCode);
      boolean isTOT = opCode.indexOf("TOT")==0;

      if (isINDENT) {
         indents = desc.split("[\\|]");
      }

      if (isACTD) {

         if (acctFrom == null) continue;
         if (acctTo == null) acctTo = acctFrom;

         String flg = "DET|BAL|ADD=TOT1,TOT2,TOT3,TOT4,TOT5";

         if (flags!=null) flg+="|"+flags;

         DTOList glInfos = glr.getGLInfo(flg,acctFrom,acctTo,lPeriodFrom,lPeriodTo,lYearFrom,lYearTo);

         PropertyMap pmap = PropertyMap.getInstance(flags,"|","=");

         boolean skipZero = pmap.containsKey("SKIPZERO");

         for (int j = 0; j < glInfos.size(); j++) {
            GLInfoView gli = (GLInfoView) glInfos.get(j);

            if (skipZero) {
               if (BDUtil.isZeroOrNull(gli.getDbAmount())) continue;
            }

            %>
        <fo:table-row>
          <% if (form.isClShowNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX(form.getLineNo())%></fo:block></fo:table-cell><%}%>
          <% if (form.isClTrxNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell><%}%>
          <% if (form.isClAccNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX(gli.getStAccountNo())%></fo:block></fo:table-cell><%}%>
          <fo:table-cell border-left-style="solid"  padding="2pt"><fo:block <%=style%>><%=gli.getStAccountDesc()%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX(gli.getDbAmount(),2)%></fo:block></fo:table-cell>
        </fo:table-row>

            <%

         }
      }

      if (isACCT) {

         if (acctFrom == null) continue;
         if (acctTo == null) acctTo = acctFrom;

      %>
        <fo:table-row>
          <% if (form.isClShowNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX(form.getLineNo())%></fo:block></fo:table-cell><%}%>
          <% if (form.isClTrxNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell><%}%>
          <% if (form.isClAccNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell><%}%>
          <fo:table-cell   padding="2pt"><fo:block <%=style%>><%=desc%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX(glr.getSummaryRanged("BAL|ADD=TOT1,TOT2,TOT3,TOT4,TOT5",acctFrom,acctTo,lPeriodFrom,lPeriodTo,lYearFrom,lYearTo),2)%></fo:block></fo:table-cell>
        </fo:table-row>
      <%
      }

      if (isNL) {
      %>
        <fo:table-row>
          <% if (form.isClShowNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell><%}%>
          <% if (form.isClTrxNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell><%}%>
          <% if (form.isClAccNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell><%}%>
          <fo:table-cell   padding="2pt"><fo:block <%=style%>></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
        </fo:table-row>
      <%

      }

      if (isDESC) {
      %>
        <fo:table-row>
          <% if (form.isClShowNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX(form.getLineNo())%></fo:block></fo:table-cell><%}%>
          <% if (form.isClTrxNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell><%}%>
          <% if (form.isClAccNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell><%}%>
          <fo:table-cell   padding="2pt"><fo:block <%=style%>><%=desc%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
        </fo:table-row>
      <%

      }

      if (isTOT) {

         BigDecimal tot = glr.getDbVariable(opCode);
         glr.setVariable(opCode, null);

      %>
        <fo:table-row>
          <% if (form.isClShowNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX(form.getLineNo())%></fo:block></fo:table-cell><%}%>
          <% if (form.isClTrxNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell><%}%>
          <% if (form.isClAccNo()) {%><fo:table-cell border-width="<%=bw%>" padding="2pt" border-right-style="solid"><fo:block text-align="end" <%=style1%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell><%}%>
          <fo:table-cell   padding="2pt"><fo:block <%=style%>><%=desc%></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  <%=style1%>><%=JSPUtil.print(tot,2)%></fo:block></fo:table-cell>
        </fo:table-row>
      <%

      }

   }
%>
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



</fo:flow>
</fo:page-sequence>
</fo:root>


<%@ page import="com.webfin.gl.form.GLListForm,
                 com.crux.web.controller.SessionManager,
                 java.util.ArrayList,
                 com.crux.util.fop.FOPUtil,
                 com.crux.util.SQLAssembler,
                 com.webfin.gl.model.JournalView,
                 com.crux.util.DTOList,
                 com.crux.util.BDUtil,
                 com.crux.util.JSPUtil,
                 java.math.BigDecimal,
                 com.crux.lang.LanguageManager,
                 java.util.Date"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines the layout master -->
  <fo:layout-master-set>
    <fo:simple-page-master master-name="first"
                           page-height="29.7cm"
                           page-width="25cm"
                           margin-top="1cm"
                           margin-bottom="2cm"
                           margin-left="1cm"
                           margin-right="1cm">
      <fo:region-body margin-top="3cm" margin-bottom="1.5cm"/>
      <fo:region-before extent="3cm"/>
      <fo:region-after extent="1.5cm"/>
    </fo:simple-page-master>
  </fo:layout-master-set>

  <!-- starts actual layout -->
  <fo:page-sequence master-reference="first">
  
  
        

<%

   GLListForm form =  (GLListForm) SessionManager.getInstance().getForm(request.getParameter("formid"));

   ArrayList colW = new ArrayList();

   colW.add(new Integer(15));
   colW.add(new Integer(45));
   colW.add(new Integer(55));
   colW.add(new Integer(65));
   colW.add(new Integer(140));
   colW.add(new Integer(50));
   colW.add(new Integer(50));

   //colW=FOPUtil.computeColumnWidth(colW,16,0," cm");

   SQLAssembler sqa = form.getSQA();

   sqa.clearOrder();
   sqa.addOrder("applydate asc");

   DTOList list = sqa.getList(JournalView.class);

   

%>


<fo:flow flow-name="xsl-region-body">

<%
   String bw = "0.5pt";
%>

   <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">DAFTAR MUTASI</fo:block>
   <fo:block font-family="Helvetica" font-weight="bold" text-align="start" font-size="8pt">
   <% if (form.getPolicyNo()!=null) {%>
       <fo:block>
         Policy No.: <%=JSPUtil.printX(form.getPolicyNo())%>
      </fo:block>
   <% } %>
   <% if (form.getTransdatefrom()!=null || form.getTransdateto()!=null) {%>
       <fo:block>
         Date: <%=JSPUtil.printX(form.getTransdatefrom())%> to <%=JSPUtil.printX(form.getTransdateto())%>
      </fo:block>
   <% } %>
   <% if (form.getAccountCode()!=null) { %>
      <fo:block>
         Account : <%=JSPUtil.printX(form.getAccountCode())%>
      </fo:block>
   <% } %>
   <% if (form.getDescription()!=null) {%>
       <fo:block>
         Description : <%=JSPUtil.printX(form.getDescription())%>
      </fo:block>
   <% } %>
   <% if (form.getTransNumber()!=null) {%>
       <fo:block>
         Trans # : <%=JSPUtil.printX(form.getTransNumber())%>
      </fo:block>
   <% } %>
   <% if (form.getBranch()!=null) {%>
       <fo:block>
         Branch : <%=JSPUtil.printX(form.getBranchDescription())%>
      </fo:block>
   <% } %>

   </fo:block>
   <fo:block space-after.optimum="14pt"/>
   
  <fo:block font-family="Helvetica" font-size="6pt" display-align="center" border-width="0.1pt">
  
   <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>" >
   <fo:table-header>
	  <fo:table-row>   
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  ><fo:block line-height="14mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">NO</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">TANGGAL</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">NOREK</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">BUKTI</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">DEBET</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">KREDIT</fo:block></fo:table-cell>
	  </fo:table-row>
	</fo:table-header>
	<%
	BigDecimal tes = new BigDecimal(0);
   for (int i = 0; i < list.size(); i++) {
      JournalView jv = (JournalView) list.get(i);
      
      
      //while(){
         tes = BDUtil.add(tes,jv.getDbDebit());
      //}

   }
%>
	<%--  
	<fo:table-footer>
		<fo:table-row>
	      <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  column-span="2"><fo:block line-width="100mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold"><fo:page-number/></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">Total Perhalaman</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"><fo:retrieve-marker retrieve-class-name="tes" /></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"></fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"></fo:block></fo:table-cell>
		  <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"></fo:block></fo:table-cell>

	</fo:table-row>
	</fo:table-footer>
	--%>
      <%=FOPUtil.printColumnWidth(colW,20,2,"cm")%>
      <fo:table-body>
         <%--  
        
        --%>
        
<%
	//BigDecimal tes = new BigDecimal(0);
   for (int i = 0; i < list.size(); i++) {
      JournalView jv = (JournalView) list.get(i);
      
      //tes = BDUtil.add(tes,jv.getDbDebit());
%>
         <fo:table-row  >
            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.print(String.valueOf(i+1))%></fo:block></fo:table-cell>
            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(jv.getDtApplyDate())%></fo:block></fo:table-cell>
            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(jv.getStAccountNo())%></fo:block></fo:table-cell>
            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="left" font-size="8pt"><%=JSPUtil.printX(jv.getStTransactionNo())%></fo:block></fo:table-cell>
            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="start" font-size="8pt"><%=LanguageManager.getInstance().translate(jv.getStDescription())%></fo:block></fo:table-cell>
            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.print(jv.getDbDebit(),2)%></fo:block></fo:table-cell>
            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.print(jv.getDbCredit(),2)%></fo:block></fo:table-cell>
         </fo:table-row  >

      <%

   }
%>
         <fo:table-row  >
            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="5"><fo:block text-align="center" font-size="8pt">TOTAL MUTASI</fo:block></fo:table-cell>
            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.print((BigDecimal) list.getTotal("debit"),2)%></fo:block></fo:table-cell>
            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.print((BigDecimal) list.getTotal("credit"),2)%></fo:block></fo:table-cell>
         </fo:table-row  >		 

      </fo:table-body>
    </fo:table>  
 </fo:block> 
 
 <fo:block font-weight="bold" text-align="start" font-size="8pt">
         IT-08-10-2005 KODASI '98
  </fo:block>

      <fo:block font-weight="bold" text-align="start" font-size="8pt">
         Print Date: <%=JSPUtil.printDateTime(new Date())%>
  </fo:block>



</fo:flow>
</fo:page-sequence>
</fo:root>


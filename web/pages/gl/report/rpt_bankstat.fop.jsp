<%@ page import="com.webfin.gl.form.GLListForm,
                 com.crux.web.controller.SessionManager,
                 java.util.ArrayList,
                 com.crux.util.fop.FOPUtil,
                 com.crux.util.SQLAssembler,
                 com.webfin.gl.model.JournalView,
                 com.crux.util.DTOList,
                 com.crux.util.JSPUtil,
                 java.math.BigDecimal,
                 java.util.Date,
                 com.webfin.gl.model.AccountView,
                 com.webfin.gl.util.GLUtil"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines the layout master -->
  <fo:layout-master-set>
    <fo:simple-page-master master-name="first"
                           page-width="29.7cm"
                           page-height="21cm"
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

   GLListForm form =  (GLListForm) SessionManager.getInstance().getForm(request.getParameter("formid"));

   /*AccountView account = form.getAccount();

   form.getAccountCode();
   form.getTransdatefrom();*/

   AccountView account = form.getAccount();
   if (account==null) throw new RuntimeException("You must specify account code");

   form.setDescription(null);
   form.setTransNumber(null);
   form.setBranch(null);
   form.setShowReverse("Y");

   ArrayList colW = new ArrayList();

   colW.add(new Integer(5));
   colW.add(new Integer(20));
   colW.add(new Integer(20));
   colW.add(new Integer(60));
   colW.add(new Integer(20));
   colW.add(new Integer(20));
   colW.add(new Integer(20));
   colW.add(new Integer(20));

   //colW=FOPUtil.computeColumnWidth(colW,16,0," cm");

   SQLAssembler sqa = form.getSQA();

   sqa.clearOrder();
   sqa.addOrder("applydate asc");

   DTOList list = sqa.getList(JournalView.class);

   GLUtil.fillBalances(list);
%>


<fo:flow flow-name="xsl-region-body">

<%
   String bw = "0.5pt";
%>

   <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">DAFTAR MUTASI</fo:block>
   <fo:block font-family="Helvetica" font-weight="bold" text-align="start" font-size="8pt">
   <% if (form.getTransdatefrom()!=null || form.getTransdateto()!=null) {%>
       <fo:block>
         Date: <%=JSPUtil.printX(form.getTransdatefrom())%> to <%=JSPUtil.printX(form.getTransdateto())%>
      </fo:block>
   <% } %>
   <% if (form.getAccountCode()!=null) { %>
      <fo:block>
         Account : <%=JSPUtil.printX(form.getAccountCode())%>
      </fo:block>
      <fo:block>
         Account Description : <%=JSPUtil.printX(form.getAccountDescription())%>
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

      <fo:block>
         Print Date: <%=JSPUtil.printDateTime(new Date())%>
      </fo:block>

   </fo:block>
   <fo:block space-after.optimum="14pt"/>

  <fo:block font-family="Helvetica" font-size="6pt" display-align="center" border-width="0.1pt">
   <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>">
      <%=FOPUtil.printColumnWidth(colW,24,2,"cm")%>
      <fo:table-body>
         <fo:table-row  >
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  ><fo:block line-height="5mm"  background-color="#C0C0C0"  text-align="center" font-size="9pt" font-weight="bold">NO</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"  background-color="#C0C0C0" text-align="center" font-size="9pt" font-weight="bold">NO BUKTI</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"  background-color="#C0C0C0" text-align="center" font-size="9pt" font-weight="bold">TANGGAL</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"  background-color="#C0C0C0" text-align="center" font-size="9pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="5mm"  background-color="#C0C0C0" text-align="center" font-size="9pt" font-weight="bold">NOREK</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"  background-color="#C0C0C0" text-align="center" font-size="9pt" font-weight="bold">DEBET</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"  background-color="#C0C0C0" text-align="center" font-size="9pt" font-weight="bold">KREDIT</fo:block></fo:table-cell>
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"  background-color="#C0C0C0" text-align="center" font-size="9pt" font-weight="bold">SALDO</fo:block></fo:table-cell>
        </fo:table-row>
<%
   for (int i = 0; i < list.size(); i++) {
      JournalView jv = (JournalView) list.get(i);
      %>
         <fo:table-row  >
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  ><fo:block line-height="5mm"    text-align="start" font-size="8pt" font-weight=""><%=i+1%></fo:block></fo:table-cell>
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"   text-align="start" font-size="8pt" font-weight=""><%=JSPUtil.print(jv.getStTransactionNo())%></fo:block></fo:table-cell>
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"   text-align="start" font-size="8pt" font-weight=""><%=JSPUtil.print(jv.getDtApplyDate())%></fo:block></fo:table-cell>
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"   text-align="start" font-size="8pt" font-weight=""><%=JSPUtil.printX(jv.getStDescription())%></fo:block></fo:table-cell>
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="5mm"   text-align="start" font-size="8pt" font-weight=""><%=JSPUtil.print(jv.getStAccountNo())%></fo:block></fo:table-cell>
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"   text-align="end" font-size="8pt" font-weight=""><%=JSPUtil.print(jv.getDbDebit(),2)%></fo:block></fo:table-cell>
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"   text-align="end" font-size="8pt" font-weight=""><%=JSPUtil.print(jv.getDbCredit(),2)%></fo:block></fo:table-cell>
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"   text-align="end" font-size="8pt" font-weight=""><%=JSPUtil.print(jv.getDbBalance(),2)%></fo:block></fo:table-cell>
         </fo:table-row  >

      <%

   }
%>
         <fo:table-row  >
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  ><fo:block line-height="5mm"    text-align="center" font-size="8pt" font-weight=""></fo:block></fo:table-cell>
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"   text-align="center" font-size="8pt" font-weight=""></fo:block></fo:table-cell>
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"   text-align="center" font-size="8pt" font-weight=""></fo:block></fo:table-cell>
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"   text-align="center" font-size="8pt" font-weight="">TOTAL MUTASI</fo:block></fo:table-cell>
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="5mm"   text-align="center" font-size="8pt" font-weight=""></fo:block></fo:table-cell>
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"   text-align="center" font-size="8pt" font-weight=""><%=JSPUtil.print((BigDecimal) list.getTotal("debit"),2)%></fo:block></fo:table-cell>
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"   text-align="center" font-size="8pt" font-weight=""><%=JSPUtil.print((BigDecimal) list.getTotal("credit"),2)%></fo:block></fo:table-cell>
             <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="5mm"   text-align="center" font-size="8pt" font-weight="">-</fo:block></fo:table-cell>
         </fo:table-row  >


      </fo:table-body>
    </fo:table>



 </fo:block>



</fo:flow>
</fo:page-sequence>
</fo:root>


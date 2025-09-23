<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.common.codedecode.Codec,
                 com.ots.invoice.model.InvoiceView,
                 com.ots.invoice.validation.InvoiceValidator,
                 com.ots.invoice.filter.InvoiceFilter,
                 com.ots.codec.OTSCodec,
                 com.ots.vendor.validation.VendorValidator"
%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<%
   final DTOList listInvoice = (DTOList) request.getAttribute("INVOICE_LIST");
   final InvoiceFilter invoiceFilter = (InvoiceFilter) request.getAttribute("FILTER");
   //final DTOList vendorCombo = (DTOList) request.getAttribute("VENDOR_COMBO");
   final boolean bCreate = jspUtil.hasResource(OTSCodec.Resource.Invoice.CREATE);
   final boolean bEdit  = jspUtil.hasResource(OTSCodec.Resource.Invoice.EDIT);
   final boolean bView  = jspUtil.hasResource(OTSCodec.Resource.Invoice.VIEW);
   final InvoiceFilter f = (InvoiceFilter) request.getAttribute("FILTER");
%>   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
   <script>
   <!--
   function selectVendorCallBack(o) {
      if (o!=null) {
         f.vendorDesc.value = o.DESC;
         f.vendorID.value = o.VENDORID;
      }
   }

   function selectVendor() {
      openDialog('so.ctl?EVENT=VENDOR_SEARCH', 300,400,selectVendorCallBack);
   }
   -->
   </script>
   <body>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
      <form name=f method=POST action="ctl.ctl">
      <input type=hidden name=EVENT>
      <input type=hidden name="invoiceId">
      <table cellpadding=2 cellspacing=1>
        <%=jspUtil.getHeader("LIST INVOICE")%>
        <tr>
            <td class=row0 >PL/Invoice Number :</td>
            <td><%=jspUtil.getInputText("invoiceNumber", InvoiceValidator.vfInvoiceNumber, invoiceFilter.stInvoiceNumber, 200)%></td>
            <td>&nbsp</td>
            <td>&nbsp</td>
        </tr>
        <tr>
            <td class=row1 >Vendor :</td>
            <td>
               <%=jspUtil.getHiddenText("vendorID", InvoiceValidator.vfVendorID, invoiceFilter.stVendorID, JSPUtil.MANUAL)%>
               <%=jspUtil.getDisplayText2("vendorDesc", VendorValidator.vfVendorName, invoiceFilter.stVendorName, 200)%>
               <%=jspUtil.getButtonNormal("bselectV","...","selectVendor()")%>
               <%=jspUtil.getButtonSubmit("bsearch","  Search","f.EVENT.value='INVOICE_LIST'")%>
            </td>
            <td>&nbsp;
            </td>
            <td>&nbsp</td>
        </tr>
      </table>
      <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
               <br>
            </td>
         </tr>
         <tr>
            <td>
               <%=jspUtil.getPager(request, listInvoice)%>
               <table cellpadding=2 cellspacing=1>
                  <tr class=header>
                     <td>&nbsp;</td>
                     <td><%=jspUtil.getSortHeader(request, listInvoice, "Invoice No", "invoice_number")%></td>
                     <td><%=jspUtil.getSortHeader(request, listInvoice, "Invoice Date", "invoice_date")%></td>
                     <td><%=jspUtil.getSortHeader(request, listInvoice, "Vendor Name", "vendor_name")%></td>
                     <td><%=jspUtil.getSortHeader(request, listInvoice, "Status", "status")%></td>
                  </tr>
<%
   for (int i = 0; i < listInvoice.size(); i++) {
      InvoiceView iv = (InvoiceView) listInvoice.get(i);
%>
                  <tr class=row<%=i%2%>>
                     <td><input type=radio name=invoiceNum value="<%=iv.getLgInvoiceID()%>" onClick="selectInvoice('<%=jspUtil.print(iv.getLgInvoiceID())%>');"></td>
                     <td><%=jspUtil.print(iv.getStInvoiceNumber())%></td>
                     <td><%=jspUtil.print(iv.getDtInvoiceDate())%></td>
                     <td><%=jspUtil.print(iv.getStVendorName())%></td>
                     <td><%=jspUtil.print(iv.getStStatus())%></td>
                  </tr>
<% } %>
               </table>
               <%=jspUtil.getPager(request, listInvoice)%>
            </td>
         </tr>
         <tr>
            <td align=center>
               <br>
               <%
                  if (bCreate)
                  {
                     out.print(jspUtil.getButtonNormal("bcreate","Create Invoice","window.location='"+jspUtil.getControllerURL("INVOICE_CREATE")+"'"));
                  }
                  if (bEdit)
                  {
                  out.print(jspUtil.getButtonSubmit("bedit","Edit Invoice","f.EVENT.value='INVOICE_EDIT'"));
                  }
                  if (bView)
                  {
                     out.print(jspUtil.getButtonSubmit("bview","View Invoice","f.EVENT.value='INVOICE_VIEW'"));
                  }
               %>
            </td>
         </tr>
      </table>
      </form>
   </body>
</html>
<script>
   enablebutton(f.bedit,false);
   enablebutton(f.bview,false);
   function selectInvoice(invoiceId) {
      enablebutton(f.bedit,true);
      enablebutton(f.bview,true);
      f.invoiceId.value = invoiceId;
   }
   function changePageList() {
      f.EVENT.value='INVOICE_LIST';
      f.submit();
   }
</script>
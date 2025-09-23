<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.util.validation.FieldValidator,
                 com.ots.invoice.validation.InvoiceValidator,
                 com.ots.vendor.validation.VendorValidator,
                 com.ots.invoice.filter.InvoiceFilter"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   DTOList luVendor = (DTOList) request.getAttribute("LU_VENDOR");
%>
<script>
<!--
   function selectVendorCallBack(o) {
      if (o!=null) {
         f.vendorDesc.value = o.DESC;
         f.vendorid.value = o.VENDORID;
      }
   }

   function selectVendor() {
      openDialog('so.ctl?EVENT=VENDOR_SEARCH', 300,400,selectVendorCallBack);
   }
   -->
</script>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="INVOICE_DO_DOWNLOAD_TEMPLATE">
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("GENERATE EXCEL TEMPLATE")%>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                  <tr>
                     <td>Select Vendor</td>
                     <td>:</td>
                     <td>
                        <%=jspUtil.getHiddenText("vendorid", InvoiceValidator.vfVendorID, null, JSPUtil.MANDATORY)%>
                        <%=jspUtil.getDisplayText2("vendorDesc", VendorValidator.vfVendorName, null, 200)%>
                        <%=jspUtil.getButtonNormal("bselectV","...","selectVendor()")%>
                     </td>
                  </tr>
                  </table>
               </td>
            </tr>
            <tr>
               <td align=center>
                  <br>
                  Click GENERATE button below and choose "Save as ..." and then open the file using Excel
                  <br>
                  <br>
               </td>
            </tr>
            <tr>
               <td align=center>
                  <%=jspUtil.getButtonSubmit("bgenerate","Generate","if (f.onsubmit()) {document.all.bgenerate.disabled=true;f.submit();}")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
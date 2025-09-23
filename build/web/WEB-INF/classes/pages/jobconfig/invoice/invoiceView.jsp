<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.common.model.ErrorDescription,
                 com.crux.common.controller.Helper,
                 com.crux.common.model.UserSession,
                 com.ots.invoice.model.InvoiceView,
                 com.ots.invoice.validation.InvoiceValidator,
                 com.crux.util.DateUtil,
                 com.ots.invoice.validation.InvoiceDetailValidator,
                 com.ots.invoice.model.InvoiceDetailView,
                 com.crux.util.LookUpUtil,
                 com.crux.common.codedecode.Codec"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<head>
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
</head>
<%
   final InvoiceView invoiceView = (InvoiceView) request.getAttribute("INVOICE");
   DTOList invoiceDtls = invoiceView.getListInvoiceDtl();
   if (invoiceDtls == null) invoiceDtls = new DTOList();
   String stAction = (String) request.getAttribute("ACTION");
   if (stAction == null) stAction = "";
%>
<body>
<form name="form1" method="POST" action="package.ctl">
<input type=hidden name=EVENT value=INVOICE_SAVE>
<input type=hidden name=itemselect>
<input type=hidden name=uomselect>
<input type=hidden name=qtyselect>
<script language="javascript">
<!--
   function addItem() {
      if (!VM_dovalidate(null, 1)) return false;
      form1.EVENT.value='INVOICE_ADD_ITEM';
      form1.submit();
   }

   function delItem(itemcode,uomcode,qty) {
        alert("aaaaaa")
      form1.EVENT.value='INVOICE_DEL_ITEM';
      form1.itemselect.value = itemcode;
      form1.uomselect.value = uomcode;
      form1.qtyselect.value = qty;
      form1.submit();
   }
-->
</script>
<table cellpadding=1 cellspacing=1>
<%=jspUtil.getHeader("VIEW INVOICE")%>
<tr>
	<td>
		<br>
	</td>
   <td></td>
   <td></td>
</tr>
<tr>
	<td class=row0>
		Vendor :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("vendorID", InvoiceValidator.vfVendorID, invoiceView.getStVendorName(), 100)%>
	</td>
</tr>
<tr>
	<td class=row0>
		Invoice Number
	</td>
	<td class="detail">
   <%=jspUtil.getDisplayText("invoiceNumber", InvoiceValidator.vfInvoiceNumber, invoiceView.getStInvoiceNumber(), 150,JSPUtil.MANDATORY)%>
	</td>
</tr>
<tr>
	<td class=row1>
		Invoice Date :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("invoiceDate", InvoiceValidator.vfInvoiceDate, invoiceView.getDtInvoiceDate(), 150,JSPUtil.MANDATORY)%>
	</td>
</tr>
<tr>
	<td class=row1>
		PO Number :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("poNumber", InvoiceValidator.vfPONumber, invoiceView.getStPONumber(), 100)%>
	</td>
</tr>
<tr>
	<td class=row0>
		LOI Number :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("loiNumber", InvoiceValidator.vfLOINumber, invoiceView.getStLOINumber(), 100)%>
	</td>
</tr>
<tr>
	<td class=row0>
		Number of Package :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("numberOfPackage", InvoiceValidator.vfPackageQty, invoiceView.getLgPackageQty(), 150,JSPUtil.MANDATORY)%>
	</td>
</tr>
<tr>
	<td class=row1>
		Weight Gross :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("weightGross", InvoiceValidator.vfWeightGross, invoiceView.getDbWeightGross(), 150,JSPUtil.MANDATORY)%>
	Kg.</td>
</tr>
<tr>
	<td class=row0>
		Weight Net :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("weightNet", InvoiceValidator.vfWeightNet, invoiceView.getDbWeightNet(), 150,JSPUtil.MANDATORY)%>
	Kg.</td>
</tr>
<tr>
	<td class=row1>
		Forwarding Agent :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("forwardingAgent", InvoiceValidator.vfForwardingAgent, invoiceView.getStForwardingAgent(), 150,JSPUtil.MANDATORY)%>
	</td>
</tr>
<tr>
	<td class=row0>
		Receive Date :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("receiveDate", InvoiceValidator.vfReceiveDate, invoiceView.getDtReceiveDate(), 150,JSPUtil.MANDATORY)%>
	</td>
</tr>
<tr>
	<td class=row1>
		EAB :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("eab", InvoiceValidator.vfEAB, invoiceView.getStEAB(), 150,JSPUtil.MANDATORY)%>
	</td>
</tr>
<tr>
	<td class=row0>
		Invoice Amount :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("invoiceAmount", InvoiceValidator.vfInvoiceAmount, invoiceView.getDbInvoiceAmount(), 150,JSPUtil.MANDATORY)%>
	</td>
</tr>
<tr>
	<td class=row1>
		Currency :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("currency", InvoiceValidator.vfCcyCode, invoiceView.getStCcyDescription(), 100)%>
	</td>
</tr>
<tr>
	<td class=row0>
		Contract Number :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("contractNumber", InvoiceValidator.vfContractNumber, invoiceView.getStContractNumber(), 150,JSPUtil.MANDATORY)%>
	</td>
</tr>
<tr>
	<td class=row1>
		PL Number :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("plNumber", InvoiceValidator.vfPLNumber, invoiceView.getStPLNumber(), 150,JSPUtil.MANDATORY)%>
	</td>
</tr>
<tr>
	<td class=row0>
		PL Date :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("plDate", InvoiceValidator.vfPLDate, invoiceView.getDtPLDate(), 150,JSPUtil.MANDATORY)%>
	</td>
</tr>
<tr>
	<td class=row1>
		PL Receive Date :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("plReceiveDate", InvoiceValidator.vfPLReceiveDate, invoiceView.getDtPLReceiveDate(), 150,JSPUtil.MANDATORY)%>
	</td>
</tr>
<tr>
	<td class=row0>
		ASN :
	</td>
	<td class="detail">
<%=jspUtil.getDisplayText("asn", InvoiceValidator.vfASNID, invoiceView.getStASNID(), 150,JSPUtil.MANDATORY)%>
	</td>
</tr>

<tr>
	<td>
		<br>
	</td>
</tr>
<tr>
	<td>
		<b>Invoice Content</b>
	</td>
</tr>
<tr>
	<td colspan=3>
		<table cellpadding=2 cellspacing=1>
		<tr class=header>
			<td width=80>
				ITEM
			</td>
			<td width=200>
				UOM
			</td>
			<td width=100>
				QUANTITY
			</td>
		</tr>
<%
   if (invoiceDtls!=null){
      for (int i = 0; i < invoiceDtls.size(); i++) {
         InvoiceDetailView invoiceDetailView = (InvoiceDetailView) invoiceDtls.get(i);
         if (!invoiceDetailView.isDelete())
         {
%>
		<tr class="row<%=i%2%>">
            <td width=80>
                <%=jspUtil.print(invoiceDetailView.getStItemDesc())%>
            </td>
            <td width=200>
                <%=jspUtil.print(invoiceDetailView.getStUOMDesc())%>
            </td>
            <td width=100>
                <%=jspUtil.getDisplayText("itemqty"+i, InvoiceDetailValidator.vfQty, invoiceDetailView.getLgQty(), 100,JSPUtil.MANDATORY)%>
            </td>
        </tr>
<%
         }
      }
	}
%>
      </table>
	</td>
</tr>
<tr>
	<td><br><br></td>
</tr>
<!--tr>
	<td class=row1>
		Total Quantity
	</td>
	<td>
   <input type=text readonly class=rinput size=20 value="<!--%=jspUtil.print(invoiceView.getLgPackageQty())%>" name=totalprice>
	</td>
</tr-->
<tr>
	<td colspan=2>
		<br><br>
		<%=jspUtil.getButtonNormal("bback","Back","window.location='"+jspUtil.getControllerURL("INVOICE_LIST")+"'")%>
	</td>
</tr>
<script>

   function recalculate() {
      form1.bsubmit.value='Recalculate';
      form1.EVENT.value = 'INVOICE_RECALCULATE';
   }

</script>
</table>
</form>
</body>
</html>

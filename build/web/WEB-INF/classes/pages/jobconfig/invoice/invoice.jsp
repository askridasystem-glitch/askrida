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
                 com.ots.codec.OTSCodec,
                 com.ots.invoice.filter.InvoiceFilter,
                 com.ots.vendor.validation.VendorValidator"%>
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
   DTOList uomCombo = (DTOList) request.getAttribute("UOM_COMBO");
   DTOList ccyCombo = (DTOList) request.getAttribute("CURRENCY_COMBO");

   DTOList costcode_region       = (DTOList) request.getAttribute("COSTCODE_REGION");
   DTOList costcode_costcenter   = (DTOList) request.getAttribute("COSTCODE_COST_CENTER");
   DTOList costcode_account      = (DTOList) request.getAttribute("COSTCODE_ACCOUNT");
   DTOList costcode_activity     = (DTOList) request.getAttribute("COSTCODE_ACTIVITY");

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
      form1.EVENT.value='INVOICE_DEL_ITEM';
      form1.itemselect.value = itemcode;
      form1.uomselect.value = uomcode;
      form1.qtyselect.value = qty;
      form1.submit();
   }

   function selectItemCallBack(o) {
      if (o!=null) {
         form1.itemDesc.value = o.DESC;
         form1.itemId.value   = o.ITEMID;
         form1.itemCode.value = o.ITEMCODE;
      }
   }

   function selectItem() {
      if (form1.vendorID.value=='')
      {
         alert('Vendor harus diisi dulu');
         return;
      }
      openDialog('so.ctl?EVENT=ITEM_SEARCH&vendorID='+form1.vendorID.value, 500,400,selectItemCallBack);
   }

   function selectVendorCallBack(o) {
      if (o!=null) {
         form1.vendorDesc.value = o.DESC;
         form1.vendorID.value = o.VENDORID;
      }
   }

   function selectVendor() {
      openDialog('so.ctl?EVENT=VENDOR_SEARCH', 300,400,selectVendorCallBack);
   }



-->
</script>
<table cellpadding=1 cellspacing=1>

<%
    if (stAction.equals("CREATE"))
    {
        out.println(jspUtil.getHeader("CREATE INVOICE"));
    } else if (stAction.equals("EDIT"))
    {
        out.println(jspUtil.getHeader("EDIT INVOICE"));
    }

%>
<tr>
	<td>
		<br>
	</td>
   <td></td>
   <td></td>
</tr>
<tr>
	<td class=row1>
		Vendor :
	</td>
	<td class="detail">
      <%=jspUtil.getHiddenText("vendorID", InvoiceValidator.vfVendorID, invoiceView.getStVendorID(), 100)%>
      <%=jspUtil.getDisplayText2("vendorDesc", VendorValidator.vfVendorName, invoiceView.getStVendorName(), 100)%>
      <%=jspUtil.getButtonNormal("bselectV","...","selectVendor()")%>
	</td>
</tr>
<tr>
	<td class=row0>
		Invoice Number
	</td>
	<td class="detail">
   <%=jspUtil.getInputText("invoiceNumber", InvoiceValidator.vfInvoiceNumber, invoiceView.getStInvoiceNumber(), 150,JSPUtil.MANDATORY)%>
	</td>
</tr>
<tr>
	<td class=row1>
		Invoice Date :
	</td>
	<td class="detail">
<%
    if (stAction.equalsIgnoreCase("EDIT")){
       out.println(jspUtil.getInputText("invoiceDate", InvoiceValidator.vfInvoiceDate, invoiceView.getDtInvoiceDate(), 150,JSPUtil.MANDATORY));
    } else {
       out.println(jspUtil.getInputText("invoiceDate", InvoiceValidator.vfInvoiceDate, DateUtil.getNewDate(), 150,JSPUtil.MANDATORY));
    }
%>
	</td>
</tr>
<tr>
	<td class=row0>
		PO / Contract Number :
	</td>
	<td class="detail">
<%
    out.println(jspUtil.getInputText("poNumber", InvoiceValidator.vfPONumber, invoiceView.getStPONumber(), 100));
%>
	</td>
</tr>
<tr>
	<td class=row1>
		LOI Number :
	</td>
	<td class="detail">
<%
    out.println(jspUtil.getInputText("loiNumber", InvoiceValidator.vfLOINumber, invoiceView.getStLOINumber(), 100));
%>
	</td>
</tr>
<tr>
	<td class=row0>
		Number of Package:
	</td>
	<td class="detail">
<%
   out.println(jspUtil.getInputText("numberOfPackage", InvoiceValidator.vfNumberOfPackage, invoiceView.getLgNumberOfPackage(), 100));
%>
	</td>
</tr>
<tr>
	<td class=row1>
		Gross Weight :
	</td>
	<td class="detail">
<%
       out.println(jspUtil.getInputText("weightGross", InvoiceValidator.vfWeightGross, invoiceView.getDbWeightGross(), 150));
%> Kg.
	</td>
</tr>
<tr>
	<td class=row0>
		Net Weight :
	</td>
	<td class="detail">
<%
       out.println(jspUtil.getInputText("weightNet", InvoiceValidator.vfWeightNet, invoiceView.getDbWeightNet(), 150));
%> Kg.
	</td>
</tr>
<tr>
	<td class=row1>
		Forwarding Agent :
	</td>
	<td class="detail">
<%
    out.println(jspUtil.getInputText("forwardingAgent", InvoiceValidator.vfForwardingAgent, invoiceView.getStForwardingAgent(), 150));
%>
	</td>
</tr>
<tr>
	<td class=row0>
		Receive Date :
	</td>
	<td class="detail">
<%=jspUtil.printDateTime(invoiceView.getDtReceiveDate())%>
	</td>
</tr>
<tr>
	<td class=row1>
		EAB :
	</td>
	<td class="detail">
<%
    out.println(jspUtil.getInputText("eab", InvoiceValidator.vfEAB, invoiceView.getStEAB(), 150));
%>
	</td>
</tr>
<tr>
	<td class=row0>
		Invoice Amount :
	</td>
	<td class="detail">
<%
    out.println(jspUtil.getInputText("invoiceAmount", InvoiceValidator.vfInvoiceAmount, invoiceView.getDbInvoiceAmount(), 150,JSPUtil.MANDATORY));
%>
	</td>
</tr>
<tr>
	<td class=row1>
		Currency :
	</td>
	<td class="detail">
<%
    out.println(jspUtil.getInputSelect("currency", InvoiceValidator.vfCcyCode, ccyCombo.getComboContent(invoiceView.getStCcyCode()), 100,JSPUtil.MANDATORY));
%>
	</td>
</tr>
<tr>
	<td class=row0>
		Cost Code :
	</td>
	<td class="detail">
<%
   out.println(jspUtil.getInputSelect("costsegment1", InvoiceValidator.vfCostCodeSegment1, costcode_region.getComboContent(invoiceView.getStCodeSegment1()), 90,JSPUtil.MANDATORY));
   out.println(jspUtil.getInputSelect("costsegment2", InvoiceValidator.vfCostCodeSegment2, costcode_costcenter.getComboContent(invoiceView.getStCodeSegment2()), 130,JSPUtil.MANDATORY));
   out.println(jspUtil.getInputSelect("costsegment3", InvoiceValidator.vfCostCodeSegment3, costcode_account.getComboContent(invoiceView.getStCodeSegment3()), 100,JSPUtil.MANDATORY));
   out.println(jspUtil.getInputSelect("costsegment4", InvoiceValidator.vfCostCodeSegment4, costcode_activity.getComboContent(invoiceView.getStCodeSegment4()), 100,JSPUtil.MANDATORY));
%>
<script>
   document.getElementById('costsegment1').options[0].text='Pilih Region';
   document.getElementById('costsegment2').options[0].text='Pilih Cost Center';
   document.getElementById('costsegment3').options[0].text='Pilih Account';
   document.getElementById('costsegment4').options[0].text='Pilih Activity';
</script>
</td>
</tr>
<!--tr>
	<td class=row0>
		Contract Number :
	</td>
	<td class="detail">
<%
    //out.println(jspUtil.getInputText("contractNumber", InvoiceValidator.vfContractNumber, invoiceView.getStContractNumber(), 150,JSPUtil.MANDATORY));
%>
	</td>
</tr-->
<table>
<tr>
	<td class=row1>
		PL Receive ? :
	</td>
	<td class="detail">
    <%
       if (invoiceView.getStPLNumber()==null || invoiceView.isModified())
       {
          out.println(jspUtil.getInputCheck("cekPl",null,invoiceView.isPlReceived(),"submitEvent('"+jspUtil.getForwardEvent("REFRESH")+"')",JSPUtil.MANUAL));
       }
       else
       {
          out.println(jspUtil.getInputCheck("cekPl",null,invoiceView.isPlReceived(),"submitEvent('"+jspUtil.getForwardEvent("REFRESH")+"')",JSPUtil.READONLY));
       }
    %>
	</td>
</tr>
<%
   boolean isPLReceive = invoiceView.isPlReceived();
   if (isPLReceive)
   {
%>
<tr>
	<td class=row0>
		PL Number :
	</td>
	<td class="detail">
<%
    out.println(jspUtil.getInputText("plNumber", InvoiceValidator.vfPLNumber, invoiceView.getStPLNumber(), 150));
%>
	</td>
</tr>
<tr>
	<td class=row1>
		PL Date :
	</td>
	<td class="detail">
<%
    out.println(jspUtil.getInputText("plDate", InvoiceValidator.vfPLDate, invoiceView.getDtPLDate(), 150));
%>
	</td>
</tr>
<tr>
	<td class=row0>
		PL Receive Date :
	</td>
	<td class="detail">
<%
       out.println(jspUtil.printDateTime(invoiceView.getDtPLReceiveDate()));
%>
	</td>
</tr>
<%
   }
%>

<%
   if ("EDIT".equalsIgnoreCase(stAction))
   {
%>
<tr>
	<td class=row1>
		ASN :
	</td>
	<td class="detail">
<%=invoiceView.getStASNID()==null?"":invoiceView.getStASNID()%>
	</td>
</tr>
<%
   }
%>

</table>
<table cellpadding=2 cellspacing=1>
<tr height=4>
   <td bgcolor="000040"></td>
</tr>
<tr>
<td><b>Pengisian Packing List</b></td>
</tr>
<tr>
   <td colspan=4>
      <table>
      <tr class=detail>
			<td width=550 class=detail>Item Description :
            <%=jspUtil.getHiddenText("itemId", InvoiceDetailValidator.vfItemID, null, JSPUtil.MANUAL)%>
            <%=jspUtil.getHiddenText("itemCode", InvoiceDetailValidator.vfItemDesc, null, JSPUtil.MANUAL)%>
            <%=jspUtil.getInputText("itemDesc", InvoiceDetailValidator.vfItemDesc, null, 300)%>
            <%=jspUtil.getButtonNormal("bselect","...","selectItem()")%>
            <%=jspUtil.getButtonNormal("badd","Add Item","addItem()")%>
         </td>

			<!--td width=100 class=detail>UOM :
            <!--%=jspUtil.getInputSelect("uomCode", InvoiceDetailValidator.vfUOMCode, uomCombo.getComboContent(null), 100, JSPUtil.MANUAL | JSPUtil.MANDATORY)%>
			</td>
            <td width=70 class=detail>Qty :
            <!--%=jspUtil.getInputText("itemqty", InvoiceDetailValidator.vfQty, null, 70, JSPUtil.MANUAL)%>
			</td>
            <td width=130 class=detail>Location :
            <!--%=jspUtil.getInputText("location", InvoiceDetailValidator.vfLocation, null, 130, JSPUtil.MANUAL)%>
			</td-->
	  </tr>
      <tr><br>
			<td width=550></td>
		</tr>
	</table>
   </td>
</tr>
<tr>
	<td>
		<br>
	</td>
</tr>
<tr>
	<td colspan=3>
		<table cellpadding=2 cellspacing=1>
		<tr class=header>
			<td width=80>
				KODE ITEM
			</td>
         <td width=100>
				DESKRIPSI ITEM
			</td>
			<td width=80>
				SATUAN UNIT
			</td>
         <td width=80>
				LOKASI
			</td>
			<td width=80>
				JUMLAH UNIT
			</td>
			<td width=40>
				HARGA PERUNIT
			</td>
         <td width=40>
				TOTAL HARGA
			</td>
          <td width=20>
				&nbsp
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
                <%=jspUtil.print(invoiceDetailView.getStItemCode())%>
            </td>
            <td width=100>
                <%=jspUtil.print(invoiceDetailView.getStItemDesc())%>
            </td>
            <td width=80>
                <%=jspUtil.getInputSelect("uomCode"+i, InvoiceDetailValidator.vfUOMCode, jspUtil.print(uomCombo.getComboContent(invoiceDetailView.getStUOMCode())), 100,JSPUtil.MANDATORY)%>
            </td>
            <td width=80>
                <%=jspUtil.print(jspUtil.getInputText("location"+i, InvoiceDetailValidator.vfLocation, invoiceDetailView.getStLocation(), 100,JSPUtil.MANUAL))%>
            </td>
            <td width=80>
                <%=jspUtil.print(jspUtil.getInputText("itemqty"+i, InvoiceDetailValidator.vfQty, invoiceDetailView.getLgQty(), 100,JSPUtil.MANDATORY,"onchange=\"countTotalPrice("+i+");\""))%>
            </td>
            <td width=40>
                <%=jspUtil.print(jspUtil.getInputText("price"+i, InvoiceDetailValidator.vfUnitPrice, invoiceDetailView.getDbUnitprice(), 100,JSPUtil.MANDATORY,"onchange=\"countTotalPrice("+i+");\""))%>
            </td>
            <td width=40>
                <%=jspUtil.print(jspUtil.getInputText("totalprice"+i, InvoiceDetailValidator.vfSubTotal, invoiceDetailView.getDbSubTotal(), 100,JSPUtil.MANUAL))%>
            </td>
            <td>
                <%=jspUtil.getButtonNormal("bdel","Delete","delItem('"+jspUtil.print(invoiceDetailView.getStItemID()+"','"+invoiceDetailView.getStUOMCode()+"','"+invoiceDetailView.getLgQty())+"')")%>
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
</table>
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
		<%=jspUtil.getButtonNormal("bsubmit","Save","checkAmountInvoice("+(invoiceDtls==null?0:invoiceDtls.size())+")")%>
		<%=jspUtil.getButtonNormal("bcancel","Cancel","window.location='"+jspUtil.getControllerURL("INVOICE_LIST")+"'")%>
	</td>
</tr>
<script>
   function recalculate() {
      form1.bsubmit.value='Recalculate';
      form1.EVENT.value = 'INVOICE_RECALCULATE';
   }

   function countTotalPrice(rowcounter)
   {
      var price = document.getElementById('price'+rowcounter).value;
      var unit  = document.getElementById('itemqty'+rowcounter).value;
      var total = moneytofloat(price)*parseFloat(unit);
      document.getElementById('totalprice'+rowcounter).value = parseFloat(total).toString();
   }

   function submitForm()
   {
      if (!confirm("Apakah yakin data mau di save?"))
         {
            return false;
         }
      form1.EVENT.value = 'INVOICE_SAVE';
      form1.submit();
   }

   function checkAmountInvoice(totalRowItem)
   {
      var totalPricePackinglist = 0;
      for (var i=0; i<totalRowItem; i++)
      {
         totalPricePackinglist = parseFloat(totalPricePackinglist) + getFloat(document.getElementById('totalprice'+i).value);
      }

      if (totalPricePackinglist != getFloat(document.form1.invoiceAmount.value))
      {
         if (!confirm("Warning: Total invoice tidak sama dengan total packing list, apakah proses dilanjutkan?"))
         {
            return;
         };
      }
      submitForm();
   }
</script>
</table>
</form>
</body>
</html>

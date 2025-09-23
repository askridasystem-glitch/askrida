<%@ page import="com.ots.item.model.ItemView,
                 com.ots.item.validation.ItemValidator,
                 com.crux.util.JSPUtil,
                 com.ots.vendor.model.VendorView,
                 com.crux.util.DTOList,
                 com.crux.login.model.UserVendorView,
                 com.crux.util.NumberUtil,
                 com.ots.vendor.validation.VendorValidator,
                 com.ots.item.filter.ItemFilter"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type='text/css' rel=STYLESHEET>
<head>
<%
   ItemView item = (ItemView) request.getAttribute("ITEM");
   String itemId = "";
   DTOList vendors = (DTOList) request.getAttribute("VENDOR");
   final ItemFilter itemFilter = (ItemFilter) request.getAttribute("FILTER");
   if (item == null) item = new ItemView();
   else itemId = item.getLgItemId()==null?"":item.getLgItemId().toString();
   String sTitle = (String) item.getAttribute("ACTION");
%>
</head>
<body>
<form name=form1 action="user.ctl" method=POST>
<input type=hidden name=EVENT value="ITEM_SAVE">
<input type=hidden name=itemId value="<%=itemId%>">
<input type=hidden name=ACTION value="<%=sTitle%>">
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
<script>
   var oid;

   function selectVendorCallBack(o) {
      if (o!=null) {
         form1.vendorDesc.value = o.DESC;
         form1.vendorId.value = o.VENDORID;
      }
   }

   function selectVendor() {
      openDialog('so.ctl?EVENT=VENDOR_SEARCH', 300,400,selectVendorCallBack);
   }
</script>

<table cellpadding=1 cellspacing=1>
<%=jspUtil.getHeader(sTitle+" ITEM MASTER")%>
<tr>
	<td width="30%"><br></td>
</tr>
<tr>
	<td class="row0">
		Vendor Name :
	</td>
    <td>
    <%
        if ("CREATE".equalsIgnoreCase(sTitle))
        {
            if (vendors!=null)
            {
                if (vendors.size()==1)
                {
                    VendorView vendor = (VendorView) vendors.get(0);
                    out.println(jspUtil.print(vendor.getStVendorName()));
                    out.println(jspUtil.getHiddenText("vendorId", ItemValidator.vfVendorId, vendor.getStVendorID(), JSPUtil.MANDATORY));
                } else
                {
                    out.println(jspUtil.getHiddenText("vendorId", ItemValidator.vfVendorId, item.getStVendorId(), JSPUtil.MANDATORY));
                    out.println(jspUtil.getDisplayText2("vendorDesc", ItemValidator.vfVendorName, item.getStVendorName(), 200));
                    out.println(jspUtil.getButtonNormal("bselectV","...","selectVendor()"));
                }
            }
        } else
        {
            if (item.getStVendorId()!=null && !"".equals(item.getStVendorId()))
            {
                 out.println(jspUtil.print(item.getStVendorName()));
                 out.println(jspUtil.getHiddenText("vendorId", ItemValidator.vfVendorId, item.getStVendorId(), JSPUtil.MANDATORY));

            }else
            {
              out.println(jspUtil.getHiddenText("vendorId", ItemValidator.vfVendorId, item.getStVendorId(), JSPUtil.MANDATORY));
              out.println(jspUtil.getDisplayText2("vendorDesc", ItemValidator.vfVendorName, item.getStVendorName(), 200));
              out.println(jspUtil.getButtonNormal("bselectV","...","selectVendor()"));
            }
        }

    %>
    </td>
</tr>
<tr>
	<td class="row0">
		Item Code :
	</td>
	<td >
    <%=jspUtil.getInputText("itemCode", ItemValidator.vfItemCode, item.getStItemCode(), 200,JSPUtil.MANDATORY) %>
 	</td>
</tr>
<tr>
	<td class="row0">
		Item Description :
	</td>
	<td >
    <%=jspUtil.getInputText("itemDesc", ItemValidator.vfDescription, item.getStItemDesc(), 200,JSPUtil.MANDATORY) %>
 	</td>
</tr>
<tr>
	<td class="row0">
		Tarif HS :
	</td>
	<td >
    <%=jspUtil.getInputText("tarifHS", ItemValidator.vfTarifHS, item.getStTarifHS(), 100,JSPUtil.MANUAL) %>
 	</td>
</tr>
<tr>
	<td class="row0">
		Tanda Aktif :<%=item.getStActiveFlag()%>
	</td>
	<td >
    <%=jspUtil.getInputCheck("activeFlag", ItemValidator.vfTarifHS, "Y".equals(item.getStActiveFlag()),JSPUtil.MANUAL) %>
 	</td>
</tr>
 </table>
 </td>
</tr>
<br>

</table>
<table cellspacing=1 cellpadding=1>
<tr>
   <td colspan=3 width=600></td>
</tr>
<tr>
	<td colspan=2>
		<br><br>
      <input type=hidden name=deleteindex>
      <%=jspUtil.getButtonSubmit("bsave","Submit") %>
      <%=jspUtil.getButtonNormal("bcancel","Cancel","window.location='"+jspUtil.getControllerURL("FIRST_ITEM_LIST")+"'")%>
   </td>
</tr>
</table>
</form>
<script>

var f= form1;

var cekst="<%=sTitle%>";

function popUpPicCallBack(o) {
   if (o!=null) {
   }
}

function popUpPic() {
   paramPic = new Object();
   paramPic.frm = document.form1;
   openDialog('so.ctl?EVENT=OPEN_FILE', paramPic, 390, 160, popUpPicCallBack);
}

</script>
</body>
</html>

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
<%
   final JSPUtil jspUtil = new JSPUtil(request, response);
   DTOList vendors = (DTOList) request.getAttribute("VENDOR");
%>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type='text/css' rel=STYLESHEET>
<head>
<%
   ItemView item = (ItemView) request.getAttribute("ITEM");
   String itemId = "";
   if (item == null) item = new ItemView();
   else itemId = item.getLgItemId()==null?"":item.getLgItemId().toString();
   final ItemFilter itemFilter = (ItemFilter) request.getAttribute("FILTER");
   String sTitle = (String) item.getAttribute("ACTION");
%>
</head>
<body>
<form name=form1 action="user.ctl" method=POST>
<input type=hidden name=EVENT value="ITEM_SAVE_LIST">
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

<table cellpadding=2 cellspacing=1>
   <%=jspUtil.getHeader("LIST OF ITEM")%>
   <tr>
      <td class="row0">
		Nama Vendor:
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
            out.println(jspUtil.print(item.getStVendorName()));
            out.println(jspUtil.getHiddenText("vendorId", ItemValidator.vfVendorId, item.getStVendorId(), JSPUtil.MANDATORY));
        }

    %>
    </td>
   </tr>
</table>

<table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                     <td class=header width=150>
				            Code
			            </td>
                        <td class=header width=250>
				            Description
			            </td>
                        <td class=header width=200>
				            Tarif
			            </td>
                     </tr>
<%
   for (int i = 0; i < 10; i++) {
%>
                     <tr class=row<%=i%2%>>
					         <td >
                         <%=jspUtil.getInputText("itemCode"+i, ItemValidator.vfItemCode, null, 150,JSPUtil.MANUAL) %>
                        </td>
                        <td >
                         <%=jspUtil.getInputText("itemDesc"+i, ItemValidator.vfDescription, null, 250,JSPUtil.MANUAL) %>
                        </td>
                        <td >
                         <%=jspUtil.getInputText("tarifHS"+i, ItemValidator.vfTarifHS, null, 150,JSPUtil.MANUAL) %>
                        </td>
                     </tr>
<% }
   %>
                  </table>
               </td>
            </tr>
         </table>
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

</script>
</body>
</html>

<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.util.DateUtil
                 ,
                 com.ots.item.validation.ItemValidator,
                 com.ots.vendor.validation.VendorValidator,
                 com.ots.item.filter.ItemFilter"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response);
   final DTOList itemlist = (DTOList)request.getAttribute("LIST_ITEM");
   final ItemFilter itemFilter = (ItemFilter) request.getAttribute("FILTER");
%>
   <head>
        <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
   <script>
   var oid;

   function selectVendorCallBack(o) {
      if (o!=null) {
         form1.vendorDesc.value = o.DESC;
         form1.vendorID.value = o.VENDORID;
      }
   }

   function selectVendor() {
      openDialog('so.ctl?EVENT=VENDOR_SEARCH', 300,400,selectVendorCallBack);
   }

   function selectItem(id) {
      form1.bview.disabled = false;
      form1.bedit.disabled = false;
      oid = id;
      form1.itmid.value = oid;
   }

	function remove(){

      if (oid==null) alert('Please check at least one Item to be deleted')
		else {
			msg = confirm('Are you sure to delete this Item?');
				if (msg){
					form1.EVENT.value='ITEM_DELETE';
					form1.submit();
				} else {
               form1.EVENT.value='ITEM_LIST';
					form1.submit();
            }
		}

	}
   </script>
   <body>
      <form name=form1 method=POST action="ctl.ctl">
	     <input type=hidden name=itmid>
         <input type=hidden name=EVENT value=SELECT_REF_TYPE>
         <table cellpadding=2 cellspacing=1>
         <%=jspUtil.getHeader("LIST OF ITEM")%>
         <tr>
            <td class=row0 >Item :</td>
            <td><%=jspUtil.getInputText("item", ItemValidator.vfDescription, null, 200)%></td>
            <td>&nbsp</td>
            <td>&nbsp</td>
         </tr>
         <tr>
            <td class=row1 >Vendor :</td>
            <td>
               <%=jspUtil.getHiddenText("vendorID", ItemValidator.vfVendorId, itemFilter.stVendorId, JSPUtil.MANUAL)%>
               <%=jspUtil.getDisplayText2("vendorDesc", VendorValidator.vfVendorName, itemFilter.stVendorName, 200)%>
               <%=jspUtil.getButtonNormal("bselectV","...","selectVendor()")%>
               <%=jspUtil.getButtonSubmit("bsearch","  Search","form1.EVENT.value='ITEM_LIST'")%>
            </td>
            <td>&nbsp;
            </td>
            <td>&nbsp</td>
         </tr>
         </table>
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
               <%=jspUtil.getPager(request, itemlist)%>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td class=header width=10>
			            </td>
					    <td class=header width=100>
				            <%=jspUtil.getSortHeader(request, itemlist, "Kode Item","item_code")%>
			            </td>
                        <td class=header width=150>
				            <%=jspUtil.getSortHeader(request, itemlist, "Deskripsi","description")%>
			            </td>
                        <td class=header width=150>
				            <%=jspUtil.getSortHeader(request, itemlist, "Vendor","vendor_name")%>
			            </td>
                     </td>
                        <td class=header width=100>
				            <%=jspUtil.getSortHeader(request, itemlist, "Tarif HS","tarif_hs")%>
			            </td>
                     </td>
                        <td class=header width=50>
				            <%=jspUtil.getSortHeader(request, itemlist, "Tanda Aktif","active_flag")%>
			            </td>
                     </tr>
<%
   if  (itemlist == null) {
       System.out.println("no record");
   } else {
   for (int i = 0; i < itemlist.size(); i++) {
      com.ots.item.model.ItemView item = (com.ots.item.model.ItemView) itemlist.get(i);
%>
                     <tr class=row<%=i%2%>>
					    <td>
				             <input type="radio" name="itemid" value="<%=jspUtil.print(item.getLgItemId())%>" onClick="selectItem('<%=jspUtil.print(item.getLgItemId())%>')">
			            </td>
                        <td><%=JSPUtil.print(item.getStItemCode())%></td>
                        <td><%=JSPUtil.print(item.getStItemDesc())%></td>
                        <td><%=JSPUtil.print(item.getStVendorName())%></td>
                        <td><%=JSPUtil.print(item.getStTarifHS())%></td>
                        <td><%=jspUtil.getInputCheck("active_flag", null, "Y".equals(item.getStActiveFlag()),JSPUtil.READONLY) %></td>
                     </tr>
<% }
   }%>
                  </table>
                   <%=jspUtil.getPager(request, itemlist)%>
               </td>
            </tr>
         </table>
		 </table>
<table cellpadding=2 cellspacing=1>
<tr>
	<td>

	<input type="submit" value="Create Item" onClick="form1.EVENT.value='ITEM_CREATE'">
	</td>
	<td>
    <input type="submit" disabled name=bedit value="Edit Item" onClick="form1.EVENT.value='ITEM_EDIT'">
	</td>
   <td>
	<input type="submit" disabled name=bview value="View Item" onClick="form1.EVENT.value='ITEM_VIEW'">
	</td>
   <td>&nbsp;</td>
</tr>
</table>

</form>
<script>
function changePageList() {
      form1.EVENT.value='ITEM_LIST';
      form1.submit();
   }
</script>
</body>

</html>

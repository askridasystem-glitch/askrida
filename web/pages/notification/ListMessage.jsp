<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.util.DateUtil,
                 com.ots.item.validation.ItemValidator,
                 com.ots.vendor.validation.VendorValidator,
                 com.ots.item.filter.ItemFilter,
                 com.ots.notification.model.NotifyMessageView,
                 com.ots.codec.OTSCodec"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response);
   final DTOList messageList = (DTOList)request.getAttribute("LIST_MESSAGE");
%>
   <head>
        <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
   <script>
   var oid;

   function selectItem(id) {
      form1.bview.disabled = false;
      form1.bedit.disabled = false;
      form1.bdel.disabled = false;
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
         <input type=hidden name=EVENT value=LIST_MESSAGES>
         <table cellpadding=2 cellspacing=1>
         <%=jspUtil.getHeader("LIST OF MESSAGE NOTIFICATION")%>
         </table>
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
               <%=jspUtil.getPager(request, messageList)%>
                  <table cellpadding=2 cellspacing=1>
                     <td class=header width=100>
				            <%=jspUtil.getSortHeader(request, messageList, "Date","create_date")%>
			            </td>
                     </td>
                        <td class=header width=200>
				            <%=jspUtil.getSortHeader(request, messageList, "Subyek","subject")%>
			            </td>
                     <td class=header width=100>
				            Message
			            </td>
                     <td class=header width=200>
				            <%=jspUtil.getSortHeader(request, messageList, "Event","event")%>
			            </td>
                     <td class=header width=200>
				            <%=jspUtil.getSortHeader(request, messageList, "mode","moda")%>
			            </td>
                     <td class=header width=200>
				            <%=jspUtil.getSortHeader(request, messageList, "Status","status")%>
			            </td>
                     </tr>
<%
   if  (messageList == null) {
       System.out.println("no record");
   } else {
   for (int i = 0; i < messageList.size(); i++) {
      NotifyMessageView message = (NotifyMessageView) messageList.get(i);
%>
                     <tr class=row<%=i%2%>>
                        <td><%=JSPUtil.print(message.getDtCreateDate())%></td>
					         <td><%=JSPUtil.print(message.getStSubject())%></td>
                        <td><%=JSPUtil.print(message.getStMessage())%></td>
                        <td><%=JSPUtil.print(message.getStEvent())%></td>
                        <td><%=JSPUtil.print(message.getStModa())%></td>
                        <td><%=JSPUtil.print(message.getStStatus())%></td>
                     </tr>
<% }
   }%>
                  </table>
                   <%=jspUtil.getPager(request, messageList)%>
               </td>
            </tr>
         </table>
		 </table>
<table cellpadding=2 cellspacing=1>
</table>

</form>
<script>
function changePageList() {
      form1.EVENT.value='LIST_MESSAGES';
      form1.submit();
   }
</script>
</body>

</html>

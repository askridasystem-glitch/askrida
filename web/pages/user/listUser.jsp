<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList
                 ,
                 com.crux.login.model.UserSessionView,
                 com.crux.util.DateUtil"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response);
   final DTOList userlist = (DTOList)request.getAttribute("USER_LIST");
%>
   <head>
        <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
   <script>
   var oid;

   /*function checkradio(){

      alert("test");
      alert(form1.userid.length);
      for(var i=0;i<form1.userid.length;i++){
            if (form1.userid[i].checked){
                selectUser(document.form1.userid[i].value);
                break();
            }
        }

   } */

   function selectUser(id) {
      form1.bview.disabled = false;
      form1.bedit.disabled = false;
      form1.bset.disabled = false;
      form1.bdel.disabled = false;
      oid = id;
      form1.usrid.value = oid;
   }

	function remove(){

      if (oid==null) alert('Please check at least one User to be deleted')
		else {
			msg = confirm('Are you sure to delete this User?');
				if (msg){
					form1.EVENT.value='USER_DELETE';
					form1.submit();
				} else {
               form1.EVENT.value='USER_LIST';
					form1.submit();
            }
		}

	}
   </script>
   <body>
      <form name=form1 method=POST action="ctl.ctl">
	     <input type=hidden name=usrid>
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("LIST OF USER")%>
            <tr>
               <td>
               <%=jspUtil.getPager(request, userlist)%>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td class=header width=10>
			            </td>
					    <td class=header width=100>
				            <%=jspUtil.getSortHeader(request, userlist, "User ID","user_id")%>
			            </td>
                        <td class=header width=200>
				            <%=jspUtil.getSortHeader(request, userlist, "User Name","user_name")%>
			            </td>
                        <td class=header width=200>
				            <%=jspUtil.getSortHeader(request, userlist, "Email Address","email_address")%>
			            </td>
                        <td class=header width=200>
				            <%=jspUtil.getSortHeader(request, userlist, "Last Login","last_login")%>
			            </td>
                     </tr>
<%

   if  (userlist == null) {
       System.out.println("no record");
   } else {
   for (int i = 0; i < userlist.size(); i++) {
      UserSessionView usr = (UserSessionView) userlist.get(i);
%>
                     <tr class=row<%=i%2%>>
					    <td>
				             <input type="radio" name="userid" value="<%=jspUtil.print(usr.getStUserID())%>" onClick="selectUser('<%=jspUtil.print(usr.getStUserID())%>')">
			            </td>
                        <td><%=JSPUtil.print(usr.getStUserID())%></td>
                        <td><%=JSPUtil.print(usr.getStUserName())%></td>
                        <td><%=JSPUtil.print(usr.getStEmail())%></td>
                        <td><%=JSPUtil.print(DateUtil.getDateTimeStr(usr.getDtLastLogin()))%></td>
                     </tr>
<% }
   }%>
                  </table>
                   <%=jspUtil.getPager(request, userlist)%>
               </td>
            </tr>
         </table>
		 </table>
<table cellpadding=2 cellspacing=1>
<tr>
	<td>
    <input type=hidden name=EVENT value=SELECT_REF_TYPE>
	<input type="submit" value="Create User" onClick="form1.EVENT.value='USER_CREATE'">
	</td>
	<td>
	<td>
    <input type="submit" disabled name=bedit value="Edit User" onClick="form1.EVENT.value='USER_EDIT'">
	</td>
    <td>
	<input type="submit" disabled name=bview value="View User" onClick="form1.EVENT.value='USER_VIEW'">
	</td>
    <td>
    <input type="button" disabled name=bdel value="Delete User" onClick="remove()">
        </td>
    <td>
	<input type="submit" disabled name=bset value="Set Password" onClick="form1.EVENT.value='SET_PASSWORD'">
	</td>
	<td>&nbsp;</td>
</tr>
</table>

</form>
<script>
function changePageList() {
      form1.EVENT.value='USER_LIST_DISPLAY';
      form1.submit();
   }
</script>
</body>

</html>

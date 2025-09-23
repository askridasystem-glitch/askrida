<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.gl.model.AccountView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%

   final DTOList list = (DTOList) request.getAttribute("LIST");

%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="GL_ACCOUNTS_REFRESH">
         <input type=hidden name=accountid value="">
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("ACCOUNTS")%>
            <tr>
               <td>
                  <%=jspUtil.getPager(request,list)%>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>ACCOUNT NO</td>
                        <td>DESCRIPTION</td>
                        <td>TYPE</td>
                        <td>Allocated</td>
                     </tr>
<%
   for (int i = 0; i < list.size(); i++) {
      AccountView act = (AccountView) list.get(i);

%>
                     <tr class=row<%=i%2%>>
                        <td><input type=radio name=sel onclick="f.accountid.value='<%=jspUtil.print(act.getLgAccountID())%>'"></td>
                        <td><%=jspUtil.print(act.getStAccountNo())%></td>
                        <td><%=jspUtil.print(act.getStDescription())%></td>
                        <td><%=jspUtil.print(act.getStAccountType())%></td>
                        <td><%=jspUtil.printFlag(act.getStAllocatedFlag())%></td>
                     </tr>
<% } %>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <%=jspUtil.getButtonNormal("crt","Create","openDialog('so.ctl?EVENT=GL_ACCOUNTS_CREATE', 500,400,refreshcb);")%>
                  <%=jspUtil.getButtonNormal("edt","Edit","openDialog('so.ctl?EVENT=GL_ACCOUNTS_EDIT&accountid='+f.accountid.value, 500,400,refreshcb);")%>
                  <%=jspUtil.getButtonNormal("v","View","openDialog('so.ctl?EVENT=GL_ACCOUNTS_VIEW&accountid='+f.accountid.value, 500,400,refreshcb);")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
<script>
   function refreshcb(o) {
      if (o!=null) f.submit();
   }

   function changePageList() {
      f.submit();
   }
</script>
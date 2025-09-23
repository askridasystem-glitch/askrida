<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.gl.model.JournalView,
                 com.crux.util.Tools"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%

   final DTOList list = (DTOList) request.getAttribute("LIST");
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="GL_ENTRY">
         <input type=hidden name=trxhdrid value="GL_ENTRY">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("TRANSACTIONS")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>TRANS #</td>
                        <td>DATE</td>
                        <td>ACCOUNT #</td>
                        <td>DESC</td>
                        <td>JT</td>
                        <td align=right>DEBIT</td>
                        <td align=right>CREDIT</td>
                     </tr>
<%
   JournalView lastjv = new JournalView();
   for (int i = 0; i < list.size(); i++) {
      JournalView jv = (JournalView) list.get(i);

      final boolean isdetail = Tools.isEqual(jv.getStTransactionHeaderID(), lastjv.getStTransactionHeaderID());
%>
                     <tr class=row<%=i%2%>>
                        <td><% if (!isdetail) {%><input type=radio name=sel onclick="f.trxhdrid.value='<%=jspUtil.print(jv.getStTransactionHeaderID())%>'"><% } %></td>
                        <td><%=jspUtil.print(!isdetail?jv.getStTransactionNo():null)%></td>
                        <td><%=jspUtil.print(!isdetail?jv.getDtApplyDate():null)%></td>
                        <td><%=jspUtil.print(jv.getStAccountNo())%></td>
                        <td><%=jspUtil.print(jv.getStDescription())%></td>
                        <td><%=jspUtil.print(jv.getStJournalCode())%></td>
                        <td align=right><%=jspUtil.print(jv.getDbDebit(),2)%></td>
                        <td align=right><%=jspUtil.print(jv.getDbCredit(),2)%></td>
                     </tr>
<% lastjv=jv;} %>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <%=jspUtil.getButtonNormal("crt","Create","openDialog('so.ctl?EVENT=GL_ENTRY_ADD', 700,500,refreshcb);")%>
                  <%=jspUtil.getButtonNormal("edt","Edit","openDialog('so.ctl?EVENT=GL_ENTRY_EDIT&trxhdrid='+f.trxhdrid.value, 700,500,refreshcb);")%>
                  <%=jspUtil.getButtonNormal("v","View","openDialog('so.ctl?EVENT=GL_ENTRY_VIEW&trxno='+f.trxno.value, 700,500,refreshcb);")%>
                  <%=jspUtil.getButtonNormal("edt","Approve","openDialog('so.ctl?EVENT=GL_ENTRY_VIEW&trxno='+f.trxno.value, 700,500,refreshcb);")%>
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
</script>
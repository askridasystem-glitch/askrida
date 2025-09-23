<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.gl.model.JournalMasterView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>

<%

   final DTOList list = (DTOList)request.getAttribute("LIST");
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <input type=hidden name=jmid value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("JOURNAL MASTER")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>JOURNAL CODE</td>
                        <td>DESCRIPTION</td>
                        <td>TYPE</td>
                        <td>END DATE</td>
                     </tr>
<%
      for (int i = 0; i < list.size(); i++) {
         JournalMasterView jm = (JournalMasterView) list.get(i);
%>
                     <tr class=row<%=i%2%>>
                        <td><input type=radio name=x onclick="f.jmid.value='<%=jspUtil.print(jm.getStJournalCode())%>'"></td>
                        <td><%=jspUtil.print(jm.getStJournalCode())%></td>
                        <td><%=jspUtil.print(jm.getStDescription())%></td>
                        <td><%=jspUtil.print(jm.getStJournalType())%></td>
                        <td><%=jspUtil.print(jm.getDtEndDate())%></td>
                     </tr>
<% } %>
                  </table>
               </td>
            </tr>

            <tr>
               <td>
                  <%=jspUtil.getButtonSubmit("bcreate","CREATE","f.EVENT.value='GL_J_MASTER_ADD'")%>
                  <%=jspUtil.getButtonSubmit("bedit","EDIT","f.EVENT.value='GL_J_MASTER_EDIT'")%>
                  <%=jspUtil.getButtonSubmit("bvuew","VIEW","f.EVENT.value='GL_J_MASTER_VIEW'")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.validation.FieldValidator,
                 com.crux.util.DTOList,
                 com.webfin.insurance.model.InsurancePolicyView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   final String key = request.getParameter("key");
   
   final String costcenter = request.getParameter("costcenter");

   final DTOList list = (DTOList) request.getAttribute("LIST");
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="INS_POLICY_SEARCH">
         <input type=hidden name=costcenter value=<%=costcenter%>>
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("SEARCH INVOICE")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td>Policy No</td>
                        <td>:</td>
                        <td><%=jspUtil.getInputText("key",new FieldValidator("Search Key","string",50),key,200,JSPUtil.MANDATORY)%></td>
                     </tr>
                  </table>
               </td>
            </tr>

            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>POL ID</td>
                        <td>Nomor Polis</td>
                        <td>Tanggal Disetujui</td>
                        <td>Setujui</td>
                     </tr>
<%
   for (int i = 0; i < list.size(); i++) {
      InsurancePolicyView pol = (InsurancePolicyView) list.get(i);

%>
                     <tr class=row<%=i%2%>>
                        <td>
                           <%=jspUtil.getButtonNormal("b","*","selectInvoice('"+pol.getStPolicyID()+"','"+ i +"')")%>
                        </td>
                        <td><%=jspUtil.print(pol.getStPolicyID())%></td>
                        <td id=polno<%=i%>><%=jspUtil.print(pol.getStPolicyNo())%></td>
                        <td><%=jspUtil.print(pol.getDtApprovedDate())%></td>
                        <td><%=jspUtil.print(pol.getStEffectiveFlag())%></td>
                     </tr>
<% } %>
                  </table>
               </td>
            </tr>

         </table>
      </form>
   </body>
</html>
<script>
   function selectInvoice(o,i) {
      dialogReturn({POL_ID:o, POL_NO:document.getElementById('polno'+i).innerText});
      window.close();
   }
</script>
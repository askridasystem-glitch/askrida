<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.validation.FieldValidator,
                 com.crux.util.DTOList,
                 com.webfin.insurance.model.InsurancePolicyView,
                 com.webfin.ar.model.ARInvoiceView"%>
<%@ taglib prefix="c" uri="crux" %>                 
  <html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   final String key = request.getParameter("key");
   
   final String arsid = request.getParameter("arsid");

   final DTOList list = (DTOList) request.getAttribute("LIST");
    
    final String cc_code = request.getParameter("cc_code");
    
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="AR_RECEIPT_SEARCH_LKS">
         <input type=hidden name=ccy value="">
         <input type=hidden name=ivtype value="">
         <input type=hidden name=arsid value="<%=arsid%>">
         <input type=hidden name=cc_code value="<%=cc_code%>">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader2("SEARCH LKS")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td>No Polis/LKS</td>
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
                        <td>ID</td>
                        <td>NO POLIS</td>
                        <td>NO LKS</td>
                        <td align=right>Amount</td>
                     </tr>
<%
   for (int i = 0; i < list.size(); i++) {
      InsurancePolicyView pol = (InsurancePolicyView) list.get(i);

%>
                     <tr class=row<%=i%2%>>
                        <td>
                           <%=jspUtil.getButtonNormal("b","*","selectInvoice('"+pol.getStPolicyID()+"')")%>
                        </td>
                        <td><%=jspUtil.print(pol.getStPolicyID())%></td>
                        <td><%=jspUtil.print(pol.getStPolicyNo())%></td>
                        <td><%=jspUtil.print(pol.getStPLANo())%></td>
                        <td align=right><%=jspUtil.printX(pol.getDbClaimAmountEstimate(),2)%></td>
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
   function selectInvoice(o) {
      dialogReturn({POLICY_ID:o});
      window.close();
   }
</script>